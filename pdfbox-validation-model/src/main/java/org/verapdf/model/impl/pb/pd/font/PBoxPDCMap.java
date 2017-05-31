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
package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.apache.fontbox.cmap.CMapParser;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.CMapFile;
import org.verapdf.model.impl.pb.external.PBoxCMapFile;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDCMap;
import org.verapdf.model.pdlayer.PDReferencedCMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCMap extends PBoxPDObject implements PDCMap {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDCMap.class);

    public static final String CMAP_TYPE = "PDCMap";

    public static final String EMBEDDED_FILE = "embeddedFile";
    public static final String USE_C_MAP = "UseCMap";

    public PBoxPDCMap(CMap cMap, COSStream cMapFile) {
        super(cMap, cMapFile, CMAP_TYPE);
    }

    public PBoxPDCMap(CMap cMap, COSStream cMapFile, String type) {
        super(cMap, cMapFile, type);
    }

	@Override
	public String getCMapName() {
		return this.cMap.getName();
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case EMBEDDED_FILE:
                return this.getEmbeddedFile();
            case USE_C_MAP:
                return this.getUseCMap();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CMapFile> getEmbeddedFile() {
        if (this.simplePDObject instanceof COSStream) {
			List<CMapFile> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            result.add(new PBoxCMapFile((COSStream) this.simplePDObject));
            return Collections.unmodifiableList(result);
        }
		return Collections.emptyList();
    }

    private List<PDReferencedCMap> getUseCMap() {
        if (this.simplePDObject instanceof COSStream) {
            COSBase useCMap = ((COSStream) this.simplePDObject).getDictionaryObject(USE_C_MAP);
            try {
                CMapParser cMapParser = new CMapParser();
                PDReferencedCMap pBoxPDCMap = null;
                CMap pdfboxCMap;

                if (useCMap instanceof COSName) {
                    pdfboxCMap = cMapParser.parsePredefined(((COSName) useCMap).getName());
                    pBoxPDCMap = new PBoxPDReferencedCMap(pdfboxCMap, null);
                } else if (useCMap instanceof COSStream) {
                    pdfboxCMap = cMapParser.parse(((COSStream) useCMap).getUnfilteredStream());
                    pBoxPDCMap = new PBoxPDReferencedCMap(pdfboxCMap, (COSStream) useCMap);
                }

                if (pBoxPDCMap != null) {
                    List<PDReferencedCMap> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                    result.add(pBoxPDCMap);
                    return Collections.unmodifiableList(result);
                }
            } catch (IOException e) {
                LOGGER.debug("Error while processing cmap", e);
            }
        }
        return Collections.emptyList();
    }

    /*
    private List<PDCMap> getUseCMap() {
        if (this.cMap != null) {
            List<PDCMap> useCMap = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            if (this.cMap.getUsedCMap() != null) {
                useCMap.add(new PBoxPDCMap(this.cMap.getUsedCMap(), null));
                return Collections.unmodifiableList(useCMap);
            }
        }
        return Collections.emptyList();
    }
    */

}
