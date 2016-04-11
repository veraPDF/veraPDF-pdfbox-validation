package org.verapdf.model.tools.transparency;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class NeitherGSTransparencyBehaviour implements GSTransparencyBehaviour {

    @Override
    public boolean containsTransparency(GraphicState graphicState) {
        COSBase sMask = graphicState.getSMask();
        if (sMask instanceof COSDictionary) {
            return true;
        }

        COSBase bm = graphicState.getBm();
        if (bm instanceof COSName) {
            COSName bmName = (COSName) bm;
            if (!bmName.equals(COSName.getPDFName("Normal"))) {
                return true;
            }
        } else if (bm instanceof COSArray) {
            COSArray bmArray = (COSArray) bm;
            if (bmArray.size() != 1) {
                return true;
            } else {
                COSBase bmValue = bmArray.get(0);
                if (!(bmValue instanceof COSName && bmValue.equals(COSName.getPDFName("Normal")))) {
                    return true;
                }
            }
        } else if (bm != null) {
            return true;
        }

        return false;
    }
}
