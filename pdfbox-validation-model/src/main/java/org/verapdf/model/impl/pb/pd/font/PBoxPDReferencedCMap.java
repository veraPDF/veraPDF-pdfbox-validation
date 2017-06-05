package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.pdlayer.PDReferencedCMap;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDReferencedCMap extends PBoxPDCMap implements PDReferencedCMap {

    public static final String REFERENCED_CMAP_TYPE = "PDReferencedCMap";

    public PBoxPDReferencedCMap(CMap cMap, COSStream cMapFile) {
        super(cMap, cMapFile, REFERENCED_CMAP_TYPE);
    }

}
