package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.operator.Op_DP;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which designates a marked-content point
 * with an associated property list
 *
 * @author Timur Kamalov
 */
public class PBOp_DP extends PBOpMarkedContent implements Op_DP {

	/** Type name for {@code PBOp_DP} */
    public static final String OP_DP_TYPE = "Op_DP";

    public PBOp_DP(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_DP_TYPE, document, flavour);
    }

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		switch (link) {
			case TAG:
				return this.getTag();
			case PROPERTIES:
				return this.getPropertiesDict();
			default:
				return super.getLinkedObjects(link);
		}
	}

}
