package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.verapdf.model.external.JPEG2000;

import java.io.*;

/**
 * @author Maksim Bezrukov
 */
public class PBoxJPEG2000 extends PBoxExternal implements JPEG2000 {

    private static final Logger LOGGER = Logger.getLogger(PBoxJPEG2000.class);

    public static final String JPEG_2000_TYPE = "JPEG2000";

    private static final Long DEFAULT_NR_COLOR_CHANNELS = Long.valueOf(0);
    private static final Long DEFAULT_NR_COLOR_SPACE_SPECS = Long.valueOf(0);
    private static final Long DEFAULT_NR_COLOR_SPACES_WITH_APPROX_FIELD = Long.valueOf(0);
    private static final Long DEFAULT_COLR_METHOD = Long.valueOf(0);
    private static final Long DEFAULT_COLR_ENUM_CS = null;
    private static final Long DEFAULT_BIT_DEPTH = Long.valueOf(0);
    private static final Boolean DEFAULT_BPCC_BOX_PRESENT = Boolean.FALSE;
    private static final byte[] sign = {0x00, 0x00, 0x00, 0x0C, 0x6A, 0x50, 0x20, 0x20, 0x0D, 0x0A, -0x79, 0x0A};

    private static final byte[] header = {0x6A, 0x70, 0x32, 0x68};
    private static final byte[] ihdr = {0x69, 0x68, 0x64, 0x72};
    private static final byte[] bpcc = {0x62, 0x70, 0x63, 0x63};
    private static final byte[] colr = {0x63, 0x6F, 0x6C, 0x72};

    private final Long nrColorChannels;
    private final Long nrColorSpaceSpecs;
    private final Long nrColorSpacesWithApproxField;
    private final Long colrMethod;
    private final Long colrEnumCS;
    private final Long bitDepth;
    private final Boolean bpccBoxPresent;

    private PBoxJPEG2000(Long nrColorChannels, Long nrColorSpaceSpecs, Long nrColorSpacesWithApproxField, Long colrMethod, Long colrEnumCS, Long bitDepth, Boolean bpccBoxPresent) {
        super(JPEG_2000_TYPE);
        this.nrColorChannels = nrColorChannels;
        this.nrColorSpaceSpecs = nrColorSpaceSpecs;
        this.nrColorSpacesWithApproxField = nrColorSpacesWithApproxField;
        this.colrMethod = colrMethod;
        this.colrEnumCS = colrEnumCS;
        this.bitDepth = bitDepth;
        this.bpccBoxPresent = bpccBoxPresent;
    }

    /**
     * Creates new PBoxJPEG2000 object that implements JPEG2000 object from the model from the given jp2 image stream
     *
     * @param stream image stream to parse
     * @return created PBoxJPEG2000 object
     */
    public static PBoxJPEG2000 fromStream(InputStream stream) {
        Builder builder = new Builder();

        byte[] sign = new byte[12];
        try {
            // Check if the stream starts with valid jp2 signature
            if (stream.read(sign) != 12 || !isValidSignature(sign)) {
                LOGGER.warn("File contains wrong signature");
                return builder.build();
            }
            // Finding the beginning of the header box content
            long headerLeft = findHeader(stream);

            if (headerLeft >= 0) {
                parseHeader(stream, headerLeft, builder);
            }

        } catch (IOException e) {
            LOGGER.debug(e);
        }
        return builder.build();
    }

    private static void parseHeader(final InputStream stream, final long headerLeft, final Builder builder) throws IOException {
        long leftInHeader = headerLeft;
        boolean isHeaderReachEnd = leftInHeader == 0;
        Long nrColorSpaceSpecs = null;
        Long nrColorSpacesWithApproxField = null;
        Long firstColrMethod = null;
        Long firstColrEnumCS = null;
        Long colrMethod = null;
        Long colrEnumCS = null;

        while (true) {
            byte[] lbox = new byte[4];
            byte[] tbox = new byte[4];
            if (stream.read(lbox) != 4 || stream.read(tbox) != 4) {
                break;
            }
            int skipped = 8;
            long length = convertArrayToLong(lbox);
            if (length == 1) {
                byte[] xlbox = new byte[8];
                if (stream.read(xlbox) != 8) {
                    break;
                }
                length = convertArrayToLong(xlbox);
                skipped = 16;
            }
            if (length < 0 || (!isHeaderReachEnd && (length == 0 || length > leftInHeader))) {
                break;
            }
            long leftInBox = length - skipped;

            if (matches(tbox, ihdr)) {
                if (leftInBox != 14 && length != 0) {
                    LOGGER.warn("Image header content does not contain 14 bytes");
                    break;
                }
                skipBytes(stream, 8);
                byte[] nc = new byte[2];
                if (stream.read(nc) != 2) {
                    LOGGER.warn("Can not read number of components");
                    break;
                }
                long ncColorChannels = convertArrayToLong(nc);
                builder.setNrColorChannels(ncColorChannels);
                byte[] bpc = new byte[1];
                if (stream.read(bpc) != 1) {
                    LOGGER.warn("Can not read bitDepth");
                    break;
                }
                long bitDepth = bpc[0] + 1;
                builder.setBitDepth(bitDepth);
                skipBytes(stream, 3);
            } else if (matches(tbox, bpcc)) {
                builder.setBpccBoxPresent(Boolean.TRUE);
                skipBytes(stream, leftInBox);
            } else if (matches(tbox, colr)) {
                if (leftInBox < 3) {
                    LOGGER.warn("Founded 'colr' box with length less than 3");
                    break;
                }
                if (nrColorSpaceSpecs == null) {
                    nrColorSpaceSpecs = Long.valueOf(1L);
                } else {
                    ++nrColorSpaceSpecs;
                }
                byte[] meth = new byte[1];
                if (stream.read(meth) != 1) {
                    LOGGER.warn("Can not read METH");
                    break;
                }
                long methValue = convertArrayToLong(meth);
                if (firstColrMethod == null) {
                    firstColrMethod = Long.valueOf(methValue);
                }
                skipBytes(stream, 1);
                byte[] approx = new byte[1];
                if (stream.read(approx) != 1) {
                    LOGGER.warn("Can not read APPROX");
                    break;
                }
                long approxValue = convertArrayToLong(approx);
                if (approxValue == 1) {
                    if (nrColorSpacesWithApproxField == null) {
                        nrColorSpacesWithApproxField = Long.valueOf(1L);
                    } else {
                        ++nrColorSpacesWithApproxField;
                    }
                    if (colrMethod == null) {
                        colrMethod = Long.valueOf(methValue);
                    }
                }
                long read = 3;
                if (methValue == 1) {
                    if (leftInBox < 7) {
                        LOGGER.warn("Founded 'colr' box with meth value 1 and length less than 7");
                        break;
                    }
                    byte[] enumCS = new byte[4];
                    if (stream.read(enumCS) != 4) {
                        LOGGER.warn("Can not read EnumCS");
                        break;
                    }
                    read += 4;
                    long enumCSValue = convertArrayToLong(enumCS);
                    if (firstColrEnumCS == null) {
                        firstColrEnumCS = Long.valueOf(enumCSValue);
                    }
                    if (approxValue == 1 && colrEnumCS == null) {
                        colrEnumCS = Long.valueOf(enumCSValue);
                    }
                }
                skipBytes(stream, leftInBox - read);
            } else {
                skipBytes(stream, leftInBox);
            }

            leftInHeader -= length;
            if ((isHeaderReachEnd && length == 0) || (!isHeaderReachEnd && leftInHeader == 0)) {
                break;
            }
        }

		if (nrColorSpaceSpecs != null) {
			builder.setNrColorSpaceSpecs(nrColorSpaceSpecs);
		}
		if (nrColorSpacesWithApproxField != null) {
			builder.setNrColorSpacesWithApproxField(nrColorSpacesWithApproxField);
		}

        if (nrColorSpacesWithApproxField != null) {
            if (colrMethod != null) {
                builder.setColrMethod(colrMethod);
            }
            if (colrEnumCS != null) {
                builder.setColrEnumCS(colrEnumCS);
            }
        } else if (Long.valueOf(1L).equals(nrColorSpaceSpecs)) {
            if (firstColrMethod != null) {
                builder.setColrMethod(firstColrMethod);
            }
            if (firstColrEnumCS != null) {
                builder.setColrEnumCS(firstColrEnumCS);
            }
        }
    }

    /**
     * Finds the beginning of the header box content and returns its left length
     *
     * @param stream image stream
     * @return left length of the header box or -1 if it has not been found and 0 if it ends at the end of the stream
     * @throws IOException
     */
    private static long findHeader(InputStream stream) throws IOException {
        while (true) {
            byte[] lbox = new byte[4];
            byte[] tbox = new byte[4];
            if (stream.read(lbox) != 4 || stream.read(tbox) != 4) {
                return -1L;
            }
            int skipped = 8;
            long length = convertArrayToLong(lbox);
            if (length == 1) {
                byte[] xlbox = new byte[8];
                if (stream.read(xlbox) != 8) {
                    return -1L;
                }
                length = convertArrayToLong(xlbox);
                skipped = 16;
            }
            long left = length - skipped;
            // Check is current box a header
            if (matches(tbox, header)) {
                if (length == 0) {
                    return 0;
                } else {
                    return left <= 0 ? -1L : left;
                }
            } else {
                if (length == 0 || left < 0) {
                    return -1L;
                } else {
                    skipBytes(stream, left);
                }
            }
        }
    }

    private static void skipBytes(InputStream stream, long skipNumber) throws IOException {
        long skippedBytes = stream.skip(skipNumber);
        if (skippedBytes != skipNumber && stream.available() != 0) {
            throw new IllegalStateException("Skipped less bytes that needed.");
        }
    }

    private static long convertArrayToLong(byte[] toConvert) {
        if (toConvert.length < 1 || toConvert.length > 8) {
            throw new IllegalArgumentException("Length of the converting byte array can not be greater than 8");
        }
        long res = 0;
        for (byte aToConvert : toConvert) {
            res <<= 8;
            res += aToConvert & 0xff;
        }
        return res;
    }

    private static boolean isValidSignature(byte[] signature) {
        return matches(signature, sign);
    }

    private static boolean matches(byte[] source, byte[] match) {
        if (match.length != source.length) {
            return false;
        }
        for (int i = 0; i < match.length; ++i) {
            if (source[i] != match[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Long getnrColorChannels() {
        return this.nrColorChannels;
    }

    @Override
    public Long getnrColorSpaceSpecs() {
        return this.nrColorSpaceSpecs;
    }

    @Override
    public Long getnrColorSpacesWithApproxField() {
        return this.nrColorSpacesWithApproxField;
    }

    @Override
    public Long getcolrMethod() {
        return this.colrMethod;
    }

    @Override
    public Long getcolrEnumCS() {
        return this.colrEnumCS;
    }

    @Override
    public Long getbitDepth() {
        return this.bitDepth;
    }

    @Override
    public Boolean getbpccBoxPresent() {
        return this.bpccBoxPresent;
    }

    private static class Builder {
        private Long nrColorChannels = DEFAULT_NR_COLOR_CHANNELS;
        private Long nrColorSpaceSpecs = DEFAULT_NR_COLOR_SPACE_SPECS;
        private Long nrColorSpacesWithApproxField = DEFAULT_NR_COLOR_SPACES_WITH_APPROX_FIELD;
        private Long colrMethod = DEFAULT_COLR_METHOD;
        private Long colrEnumCS = DEFAULT_COLR_ENUM_CS;
        private Long bitDepth = DEFAULT_BIT_DEPTH;
        private Boolean bpccBoxPresent = DEFAULT_BPCC_BOX_PRESENT;

        public PBoxJPEG2000 build() {
            return new PBoxJPEG2000(this.nrColorChannels, this.nrColorSpaceSpecs, this.nrColorSpacesWithApproxField, this.colrMethod, this.colrEnumCS, this.bitDepth, this.bpccBoxPresent);
        }

        public Long getNrColorChannels() {
            return nrColorChannels;
        }

        public Builder setNrColorChannels(Long nrColorChannels) {
            this.nrColorChannels = nrColorChannels;
            return this;
        }

        public Long getNrColorSpaceSpecs() {
            return nrColorSpaceSpecs;
        }

        public Builder setNrColorSpaceSpecs(Long nrColorSpaceSpecs) {
            this.nrColorSpaceSpecs = nrColorSpaceSpecs;
            return this;
        }

        public Long getNrColorSpacesWithApproxField() {
            return nrColorSpacesWithApproxField;
        }

        public Builder setNrColorSpacesWithApproxField(Long nrColorSpacesWithApproxField) {
            this.nrColorSpacesWithApproxField = nrColorSpacesWithApproxField;
            return this;
        }

        public Long getColrMethod() {
            return colrMethod;
        }

        public Builder setColrMethod(Long colrMethod) {
            this.colrMethod = colrMethod;
            return this;
        }

        public Long getColrEnumCS() {
            return colrEnumCS;
        }

        public Builder setColrEnumCS(Long colrEnumCS) {
            this.colrEnumCS = colrEnumCS;
            return this;
        }

        public Long getBitDepth() {
            return bitDepth;
        }

        public Builder setBitDepth(Long bitDepth) {
            this.bitDepth = bitDepth;
            return this;
        }

        public Boolean getBpccBoxPresent() {
            return bpccBoxPresent;
        }

        public Builder setBpccBoxPresent(Boolean bpccBoxPresent) {
            this.bpccBoxPresent = bpccBoxPresent;
            return this;
        }
    }
}
