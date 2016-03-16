package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_FStar;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which fills the path, using the even-odd rule
 * to determine the region to fill
 *
 * @author Timur Kamalov
 */
public class PBOp_FStar extends PBOpFillPaint implements Op_FStar {

	/** Type name for {@code PBOp_FStar} */
	public static final String OP_FSTAR_TYPE = "Op_FStar";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_FStar(List<COSBase> arguments,
					  final GraphicState state,
					  final PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(arguments, state, resources, OP_FSTAR_TYPE, document, flavour);
	}

}
