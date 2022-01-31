/**
 * This file is part of veraPDF Metadata Fixer, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Metadata Fixer is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Metadata Fixer as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Metadata Fixer as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.impl.pb.model;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.impl.pb.schemas.AdobePDFSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.DublinCoreSchemaImpl;
import org.verapdf.metadata.fixer.impl.pb.schemas.XMPBasicSchemaImpl;
import org.verapdf.metadata.fixer.schemas.AdobePDF;
import org.verapdf.metadata.fixer.schemas.DublinCore;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataImpl implements Metadata {

    private static final Logger LOGGER = Logger.getLogger(MetadataImpl.class);

    private final VeraPDFMeta metadata;
    private final COSStream stream;

    /**
     * @param metadata
     * @param stream
     */
    public MetadataImpl(VeraPDFMeta metadata, COSStream stream) {
        if (metadata == null) {
            throw new IllegalArgumentException(
                    "Metadata package can not be null");
        }
        if (stream == null) {
            throw new IllegalArgumentException(
                    "Metadata stream can not be null");
        }
        this.metadata = metadata;
        this.stream = stream;
    }

    @Override
    public void checkMetadataStream(
            MetadataFixerResultImpl.Builder resultBuilder,
            PDFAFlavour flavour) {
        PDFAFlavour.Specification part = flavour.getPart();
        if (part == PDFAFlavour.Specification.ISO_19005_2 || part == PDFAFlavour.Specification.ISO_19005_3) {
            COSBase filters = this.stream.getFilters();
            if (filters instanceof COSName && COSName.FLATE_DECODE.equals(filters)) {
                return;
            } else if (filters instanceof COSArray && ((COSArray) filters).size() == 1 && COSName.FLATE_DECODE.equals(((COSArray) filters).get(0))) {
                return;
            }
            try {
                this.stream.setFilters(COSName.FLATE_DECODE);
                this.stream.setNeedToBeUpdated(true);
                resultBuilder.addFix("Metadata stream filtered with FlateDecode");
            } catch (IOException e) {
                LOGGER.debug("Problems with setting filter for stream.");
                LOGGER.debug(e);
            }
        }
        this.setRequiredDictionaryValue(COSName.METADATA, COSName.TYPE,
                resultBuilder);
        this.setRequiredDictionaryValue(COSName.getPDFName("XML"),
                COSName.SUBTYPE, resultBuilder);
    }

    private void setRequiredDictionaryValue(COSName value, COSName key,
                                            MetadataFixerResultImpl.Builder resultBuilder) {
        if (!value.equals(this.stream.getDictionaryObject(key))) {
            this.stream.setItem(key, value);
            this.stream.setNeedToBeUpdated(true);
            resultBuilder.addFix(value.getName() + " value of " + key.getName()
                    + " key is set " + "to metadata dictionary");
        }
    }

    @Override
    public void removePDFIdentificationSchema(
            MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour) {
        try {
            if (isValidIdentification()) {
                int part = flavour.getPart().getPartNumber();
                Integer schemaPart = this.metadata.getIdentificationPart();

                if (schemaPart != null &&
                        schemaPart.intValue() != part) {
                    return;
                }
            }

            boolean isDeleted = this.metadata.deleteIdentificationSchema();
            if (isDeleted) {
                this.setNeedToBeUpdated(true);
                resultBuilder.addFix("Identification schema removed");
                resultBuilder.status(MetadataFixerResult.RepairStatus.ID_REMOVED);
            }

        } catch (XMPException e) {
            LOGGER.debug("Can not obtain identification part.", e);
        }
    }

    @Override
    public void addPDFIdentificationSchema(
            MetadataFixerResultImpl.Builder resultBuilder, PDFAFlavour flavour) {
        int part = flavour.getPart().getPartNumber();
        String conformance = flavour.getLevel().getCode().toUpperCase();

        try {
            if (isValidIdentification()) {
                Integer schemaPart = this.metadata.getIdentificationPart();
                String schemaConformance = this.metadata.getIdentificationConformance();

                if (schemaPart != null &&
                        schemaConformance != null &&
                        (schemaPart.intValue() != part ||
                                compare(conformance, schemaConformance) <= 0)) {
                    return;
                }
            }

            this.metadata.setIdentificationPart(Integer.valueOf(part));
            this.metadata.setIdentificationConformance(conformance);
            this.setNeedToBeUpdated(true);
            resultBuilder.addFix("Identification schema added");

        } catch (XMPException e) {
            LOGGER.debug("Can not obtain identification fields.", e);
        }
    }

	private static int compare(String conf, String confToCompare) {
        int confInt = confToInt(conf);
        int confToCompareInt = confToInt(confToCompare);

        return confInt - confToCompareInt;
    }

    private static int confToInt(String conf) {
        switch (conf) {
            case "A":
                return 2;
            case "U":
                return 1;
            case "B":
                return 0;
            default:
                throw new IllegalStateException("Method call with invalid conformance.");
        }
    }

    private boolean isValidIdentification() {
        try {
            Integer identificationPart = this.metadata.getIdentificationPart();
            if (identificationPart == null) {
                return false;
			}
                String identificationConformance = this.metadata.getIdentificationConformance();
                if (identificationPart.intValue() == 1) {
                    return "A".equals(identificationConformance) || "B".equals(identificationConformance);
                } else if (identificationPart.intValue() == 2 || identificationPart.intValue() == 3) {
                    return "A".equals(this.metadata.getIdentificationConformance()) ||
                            "U".equals(identificationConformance) ||
                            "B".equals(identificationConformance);
                } else {
                    return false;
                }
        } catch (XMPException e) {
            LOGGER.debug("Can not obtain identification fields.", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public DublinCore getDublinCoreSchema(InfoDictionary info) {
        return new DublinCoreSchemaImpl(this.metadata, this);
    }

    @Override
    public AdobePDF getAdobePDFSchema(InfoDictionary info) {
        return new AdobePDFSchemaImpl(this.metadata, this);
    }

    @Override
    public XMPBasic getXMPBasicSchema(InfoDictionary info) {
        return new XMPBasicSchemaImpl(this.metadata, this);
    }

    @Override
    public boolean isNeedToBeUpdated() {
        return this.stream.isNeedToBeUpdated();
    }

    @Override
    public void setNeedToBeUpdated(boolean needToBeUpdated) {
        this.stream.setNeedToBeUpdated(true);
    }

    @Override
    public void updateMetadataStream() throws IOException, XMPException {
        if (!this.stream.isNeedToBeUpdated()) {
            return;
        }
        try (OutputStream out = this.stream.createUnfilteredStream()) {
            VeraPDFMeta.serialize(this.metadata, out);
        }
    }
}
