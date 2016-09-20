package org.verapdf.model.impl.pb.operator.inlineimage;

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpInlineImage;

/**
 * Base class for operators, such as BI, ID and EI
 *
 * @author Timur Kamalov
 */
public class PBOpInlineImage extends PBOperator implements OpInlineImage {

    protected PBOpInlineImage(List<COSBase> arguments, final String type) {
        super(arguments, type);
    }

}
