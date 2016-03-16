package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_s_close_stroke;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which closes and strokes the path
 *
 * @author Timur Kamalov
 */
public class PBOp_s_close_stroke extends PBOpStrokePaint implements
		Op_s_close_stroke {

	/** Type name for {@code PBOp_s_close_stroke} */
	public static final String OP_S_CLOSE_STROKE_TYPE = "Op_s_close_stroke";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_s_close_stroke(List<COSBase> arguments,
							   final GraphicState state,
							   final PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(arguments, state, resources, OP_S_CLOSE_STROKE_TYPE, document, flavour);
	}

}
