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

    private List<PDCMap> getUseCMap() {
        if (this.simplePDObject instanceof COSStream) {
            COSBase useCMap = ((COSStream) this.simplePDObject).getDictionaryObject(USE_C_MAP);
            try {
                CMapParser cMapParser = new CMapParser();
                PBoxPDCMap pBoxPDCMap = null;
                CMap pdfboxCMap;

                if (useCMap instanceof COSName) {
                    pdfboxCMap = cMapParser.parsePredefined(((COSName) useCMap).getName());
                    pBoxPDCMap = new PBoxPDCMap(pdfboxCMap, null);
                } else if (useCMap instanceof COSStream) {
                    pdfboxCMap = cMapParser.parse(((COSStream) useCMap).getUnfilteredStream());
                    pBoxPDCMap = new PBoxPDCMap(pdfboxCMap, (COSStream) useCMap);
                }

                if (pBoxPDCMap != null) {
                    List<PDCMap> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                    result.add(pBoxPDCMap);
                    return Collections.unmodifiableList(result);
                }
            } catch (IOException e) {
                LOGGER.warn("Error while processing cmap", e);
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
