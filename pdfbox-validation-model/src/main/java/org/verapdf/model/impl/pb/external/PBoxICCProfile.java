/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.external.ICCProfile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Embedded ICC profile
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCProfile extends PBoxExternal implements ICCProfile {

    private static final Logger LOGGER = Logger.getLogger(PBoxICCProfile.class);

	/** Length of icc output profile header */
    public static final int HEADER_LENGTH = 128;
	/** Offset of device class in header */
    public static final int DEVICE_CLASS_OFFSET = 12;
	/** Offset of color space in header */
    public static final int COLOR_SPACE_OFFSET = 16;
	/** Expected length for device class and so on */
    public static final int REQUIRED_LENGTH = 4;
	/** Expected length version */
    public static final int VERSION_LENGTH = 3;
	/** Offset of version byte */
    public static final int VERSION_BYTE = 8;
	/** Offset of subversion byte */
    public static final int SUBVERSION_BYTE = 9;

    private byte[] profileHeader;
    private InputStream profileStream;
    private Long dictionaryNumberOfColors;
    private boolean isValid = true;

    protected PBoxICCProfile(COSStream profileStream,
							 String type) {
        super(type);

        try {
            this.profileStream = profileStream.getUnfilteredStream();
            this.dictionaryNumberOfColors = profileStream.getLong(COSName.N);
            if(this.dictionaryNumberOfColors == -1) {
                this.dictionaryNumberOfColors = null;
            }

            initializeProfileHeader();
        } catch (IOException e) {
            this.isValid = false;
            if(this.profileHeader == null) {
                this.profileHeader = new byte[0];
            }
        }
    }

    private void initializeProfileHeader() throws IOException {
        int available = this.profileStream.available();
        int size = available > HEADER_LENGTH ? HEADER_LENGTH : available;
        if(size != HEADER_LENGTH) {
            this.isValid = false;
        }

        this.profileHeader = new byte[size];
        this.profileStream.mark(size);
        this.profileStream.read(this.profileHeader, 0, size);
        this.profileStream.reset();
		// TODO : finalize it correct, when object will destroy
		this.profileStream.close();
    }

    /**
     * @return string representation of device class or null, if profile length
     *         is too small
     */
    @Override
    public String getdeviceClass() {
        return getSubArray(DEVICE_CLASS_OFFSET, REQUIRED_LENGTH);
    }

    /**
     * @return number of colorants for ICC profile, described in profile
     *         dictionary
     */
    @Override
    public Long getN() {
        return this.dictionaryNumberOfColors;
    }

    /**
     * @return string representation of color space or null, if profile length
     *         is too small
     */
    @Override
    public String getcolorSpace() {
        return getSubArray(COLOR_SPACE_OFFSET, REQUIRED_LENGTH);
    }

    private String getSubArray(int start, int length) {
        if (start + length <= this.profileHeader.length) {
            byte[] buffer = new byte[length];
            System.arraycopy(this.profileHeader, start, buffer, 0, length);
            return new String(buffer);
        }
        LOGGER.debug("Length of icc profile less than " + (start + length));
        return null;
    }

    /**
     * @return version of ICC profile or null, if profile length is too small
     */
    @Override
    public Double getversion() {
        if (this.profileHeader.length > SUBVERSION_BYTE) {
            StringBuilder version = new StringBuilder(VERSION_LENGTH);
            version.append(this.profileHeader[VERSION_BYTE] & 0xFF).append('.');
            version.append((this.profileHeader[SUBVERSION_BYTE] >>> REQUIRED_LENGTH) & 0xFF);

            return Double.valueOf(version.toString());
        }
        LOGGER.debug("ICC profile contain less than 10 bytes of data.");
        return null;
    }

	/**
	 * Indicate validity of icc profile.
	 * Need to implemented by customer.
	 *
	 * @return true if profile is valid, false if ICC header is less then 128
     * bytes or stream cannot be read. Other checks should be implemented by
     * customer.
	 */
    @Override
    public Boolean getisValid() {
        return this.isValid;
    }

}
