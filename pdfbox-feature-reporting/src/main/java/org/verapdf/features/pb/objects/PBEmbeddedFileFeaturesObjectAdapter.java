/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.verapdf.features.objects.EmbeddedFileFeaturesObjectAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Feature object adapter for embedded file
 *
 * @author Maksim Bezrukov
 */
public class PBEmbeddedFileFeaturesObjectAdapter implements EmbeddedFileFeaturesObjectAdapter {

    private static final Logger LOGGER = Logger
            .getLogger(PBEmbeddedFileFeaturesObjectAdapter.class);

    private PDComplexFileSpecification embFile;
    private PDEmbeddedFile ef;
    private int index;
    private Calendar creationDate;
    private Calendar modDate;
    private String checkSum;
    private List<String> errors;

    /**
     * Constructs new Embedded File Feature Object adapter
     *
     * @param embFile pdfbox class represents Embedded File object
     * @param index   page index
     */
    public PBEmbeddedFileFeaturesObjectAdapter(PDComplexFileSpecification embFile, int index) {
        this.embFile = embFile;
        if (this.embFile != null) {
            this.ef = this.embFile.getEmbeddedFile();
            init();
        }
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public String getFileName() {
        if (embFile != null) {
            return embFile.getFilename();
        }
        return null;
    }

    @Override
    public String getDescription() {
        if (embFile != null) {
            return embFile.getFileDescription();
        }
        return null;
    }

    @Override
    public String getAFRelationship() {
        if (embFile != null) {
            COSDictionary dict = embFile.getCOSObject();
            if (dict != null) {
                return dict.getNameAsString(COSName.getPDFName("AFRelationship"));
            }
        }
        return null;
    }

    @Override
    public String getSubtype() {
        if (this.ef != null) {
            return this.ef.getSubtype();
        }
        return null;
    }

    @Override
    public String getFilter() {
        if (this.ef != null) {
            return getFilters(ef.getFilters());
        }
        return null;
    }

    @Override
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    @Override
    public Calendar getModDate() {
        return this.modDate;
    }

    @Override
    public String getCheckSum() {
        return this.checkSum;
    }

    @Override
    public Long getSize() {
        if (ef != null) {
            return Long.valueOf(ef.getSize());
        }
        return null;
    }

    @Override
    public boolean isPDFObjectPresent() {
        return this.embFile != null;
    }

    @Override
    public List<String> getErrors() {
        return this.errors == null ?
                Collections.<String>emptyList() : Collections.unmodifiableList(this.errors);
    }

    private void init() {
        if (ef != null) {
            this.errors = new ArrayList<>();
            try {
                this.creationDate = ef.getCreationDate();
            } catch (IOException e) {
                LOGGER.debug("PDFBox error obtaining creation date", e);
                this.errors.add(e.getMessage());
            }

            try {
                this.modDate = ef.getModDate();
            } catch (IOException e) {
                LOGGER.debug("PDFBox error obtaining modification date", e);
                this.errors.add(e.getMessage());
            }

            COSBase baseParams = ef.getStream().getDictionaryObject(COSName.PARAMS);
            if (baseParams instanceof COSDictionary) {
                COSBase baseChecksum = ((COSDictionary) baseParams).getDictionaryObject(COSName.getPDFName("CheckSum"));
                if (baseChecksum instanceof COSString) {
                    COSString str = (COSString) baseChecksum;
                    if (str.isHex()) {
                        this.checkSum = str.toHexString();
                    } else {
                        this.checkSum = str.getString();
                    }
                }
            }
        }
    }

    @Override
    public InputStream getData() {
        try {
            if (this.ef == null) {
                LOGGER.debug("Missed embedded file in PDComplexFileSpecification");
                return null;
            }
            COSStream stream = this.ef.getStream();
            if (stream != null) {
                return stream.getUnfilteredStream();
            }
        } catch (IOException e) {
            LOGGER.debug("Can not get embedded file stream", e);
        }
        return null;
    }

    private static String getFilters(List<COSName> list) {
        if (list != null) {
            StringBuilder builder = new StringBuilder();
            for (COSName filter : list) {
                if (filter != null && filter.getName() != null) {
                    builder.append(filter.getName());
                    builder.append(" ");
                }
            }
            return builder.toString().trim();
        }
        return null;
    }
}
