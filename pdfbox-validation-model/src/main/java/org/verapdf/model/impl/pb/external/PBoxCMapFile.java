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

import org.apache.fontbox.cmap.CIDRange;
import org.apache.fontbox.cmap.CMap;
import org.apache.fontbox.cmap.CMapParser;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.external.CMapFile;

import java.io.IOException;

/**
 * Current class is representation of CMapFile of pdf document
 * implemented by Apache PDFBox library
 *
 * @author Timur Kamalov
 */
public class PBoxCMapFile extends PBoxExternal implements CMapFile {

    private static final Logger LOGGER = Logger.getLogger(PBoxCMapFile.class.getCanonicalName());

    /**
     * Type name for {@code PBoxCMapFile}
     */
    public static final String CMAP_FILE_TYPE = "CMapFile";

    private final COSStream fileStream;

    /**
     * Default constructor.
     *
     * @param fileStream stream of CMapFile
     */
    public PBoxCMapFile(final COSStream fileStream) {
        super(CMAP_FILE_TYPE);
        this.fileStream = fileStream;
    }

    /**
     * @return value of {@code WMode} key
     */
    @Override
	public Long getWMode() {
        try {
            CMap map = new CMapParser().parse(fileStream.getUnfilteredStream());
            return Long.valueOf(map.getWMode());
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.INFO, "Could not parse CMap. " + e.getMessage());
        }
        return null;
    }

    /**
     * @return value of {@code WMode} key of parent dictionary
     */
    @Override
    public Long getdictWMode() {
        return Long.valueOf(this.fileStream.getInt(COSName.getPDFName("WMode"), 0));
    }

    @Override
    public Long getmaximalCID() {
        try {
            CMap map = new CMapParser().parse(fileStream.getUnfilteredStream());
            return (long) Math.max(map.getCodeToCidRanges().stream().map(CIDRange::getMaxCid).max(Integer::compare).orElse(0),
                    map.getCodeToCid().values().stream().max(Integer::compare).orElse(0));
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.INFO, "Could not get CID. " + e.getMessage());
        }
        return null;
    }
}
