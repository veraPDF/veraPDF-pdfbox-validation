package org.verapdf.model.tools.transparency;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDColorSpace;
import org.verapdf.model.impl.pb.pd.font.PBoxPDFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXForm;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXImage;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDTilingPattern;

/**
 * @author Maksim Bezrukov
 */
public class TransparencyBehaviour {

    private boolean isFillCheck = false;
    private boolean isStrokeCheck = false;
    private boolean isXObjectCheck = false;
    private boolean isColorSpaceCheck = false;
    private boolean isFontCheck = false;

    private TransparencyBehaviour() {
    }

    public static TransparencyBehaviour createFillInstance() {
        TransparencyBehaviour tb = new TransparencyBehaviour();
        tb.isFillCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createFillXObjectInstance() {
        TransparencyBehaviour tb = createFillInstance();
        tb.isXObjectCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createFillColorSpaceInstance() {
        TransparencyBehaviour tb = createFillInstance();
        tb.isColorSpaceCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createFillColorSpaceFontInstance() {
        TransparencyBehaviour tb = createFillColorSpaceInstance();
        tb.isFontCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createStrokeColorSpaceInstance() {
        TransparencyBehaviour tb = new TransparencyBehaviour();
        tb.isStrokeCheck = true;
        tb.isColorSpaceCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createStrokeColorSpaceFontInstance() {
        TransparencyBehaviour tb = createStrokeColorSpaceInstance();
        tb.isFontCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createFillStrokeColorSpaceInstance() {
        TransparencyBehaviour tb = new TransparencyBehaviour();
        tb.isFillCheck = true;
        tb.isStrokeCheck = true;
        tb.isColorSpaceCheck = true;
        return tb;
    }

    public static TransparencyBehaviour createFillStrokeColorSpaceFontInstance() {
        TransparencyBehaviour tb = createFillStrokeColorSpaceInstance();
        tb.isFontCheck = true;
        return tb;
    }

    public boolean containsTransparency(GraphicState graphicState) {
        if (baseCheck(graphicState)) {
            return true;
        }

        if (isFillCheck && graphicState.getCa_ns() < 1.0f) {
            return true;
        }

        if (isStrokeCheck && graphicState.getCa() < 1.0f) {
            return true;
        }

        if (isXObjectCheck && xObjectContainsTransparency(graphicState.getVeraXObject())) {
            return true;
        }

        if (isColorSpaceCheck && colorSpaceCheck(graphicState)) {
            return true;
        }

        if (isFontCheck && fontCheck(graphicState)) {
            return true;
        }

        return false;
    }

    private boolean fontCheck(GraphicState graphicState) {
        PBoxPDFont font = graphicState.getVeraFont();
        if (font instanceof PBoxPDType3Font && ((PBoxPDType3Font) font).isContainsTransparency()) {
            return true;
        }
        return false;
    }

    private boolean colorSpaceCheck(GraphicState graphicState) {
        if (isFillCheck) {
            PBoxPDColorSpace fillCS = graphicState.getVeraFillColorSpace();
            if (fillCS instanceof PBoxPDTilingPattern && ((PBoxPDTilingPattern) fillCS).isContainsTransparency()) {
                return true;
            }
        }
        if (isStrokeCheck) {
            PBoxPDColorSpace strokeCS = graphicState.getVeraStrokeColorSpace();
            if (strokeCS instanceof PBoxPDTilingPattern && ((PBoxPDTilingPattern) strokeCS).isContainsTransparency()) {
                return true;
            }
        }
        return false;
    }

    private boolean baseCheck(GraphicState graphicState) {
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

    private boolean xObjectContainsTransparency(PBoxPDXObject xobj) {
        if (xobj instanceof PBoxPDXForm) {
            return ((PBoxPDXForm) xobj).containsTransparency();
        } else if (xobj instanceof PBoxPDXImage) {
            return ((PBoxPDXImage) xobj).containsTransparency();
        }
        return false;
    }
}
