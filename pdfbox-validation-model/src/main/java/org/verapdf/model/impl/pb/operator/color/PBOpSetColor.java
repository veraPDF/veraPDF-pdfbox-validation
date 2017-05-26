package org.verapdf.model.impl.pb.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpSetColor;

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

    public PBOpSetColor(List<COSBase> arguments) {
        super(arguments, OP_SET_COLOR_TYPE);
    }
}
