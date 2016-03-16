package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.operator.Op_S_stroke;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which strokes the path
 *
 * @author Timur Kamalov
 */
public class PBOp_S_stroke extends PBOpStrokePaint implements Op_S_stroke {

	/** Type name for {@code PBOp_S_stroke} */
	public static final String OP_S_STROKE_TYPE = "Op_S_stroke";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
	public PBOp_S_stroke(List<COSBase> arguments,
						 final GraphicState state,
						 final PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(arguments, state, resources, OP_S_STROKE_TYPE, document, flavour);
	}

}
