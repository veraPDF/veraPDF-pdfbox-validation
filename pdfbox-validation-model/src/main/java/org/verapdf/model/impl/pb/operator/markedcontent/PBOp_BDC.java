package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.operator.Op_BDC;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which begins a marked-content sequence
 * with an associated property list
 *
 * @author Timur Kamalov
 */
public class PBOp_BDC extends PBOpMarkedContent implements Op_BDC {

	/** Type name for {@code PBOp_BDC} */
    public static final String OP_BDC_TYPE = "Op_BDC";

    public PBOp_BDC(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_BDC_TYPE, document, flavour);
    }

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		switch (link) {
			case TAG:
				return this.getTag();
			case PROPERTIES:
				return this.getPropertiesDict();
			case LANG:
				return this.getLang();
			default:
				return super.getLinkedObjects(link);
		}
	}
}
