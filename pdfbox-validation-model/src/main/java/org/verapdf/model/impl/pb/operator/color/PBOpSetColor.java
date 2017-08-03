package org.verapdf.model.impl.pb.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosNumber;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpSetColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Set color operators without colorspace, Op_SC, Op_sc.
 *
 * @author Sergey Shemyakov
 */
public class PBOpSetColor extends PBOperator implements OpSetColor {

    /**
     * Type name for {@code PBOpColor}
     */
    public static final String OP_SET_COLOR_TYPE = "OpSetColor";
    public static final String COLOR_VALUES = "colorValues";

    protected PBOpSetColor(List<COSBase> arguments, String type) {
        super(arguments, type);
    }

    public PBOpSetColor(List<COSBase> arguments) {
        this(arguments, OP_SET_COLOR_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case COLOR_VALUES:
                return getColorValues();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CosNumber> getColorValues() {
        List<CosNumber> list = new ArrayList<>();
        for (COSBase base : this.arguments) {
            if (base instanceof COSNumber) {
                list.add(PBCosNumber.fromPDFBoxNumber(base));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
