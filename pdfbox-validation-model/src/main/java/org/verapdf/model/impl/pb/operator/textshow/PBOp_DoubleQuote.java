package org.verapdf.model.impl.pb.operator.textshow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.cos.PBCosNumber;
import org.verapdf.model.operator.Op_DoubleQuote;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Operator which moves to the next line and shows a text string,
 * using the word spacing and the character spacing (setting
 * the corresponding parameters in the text state)
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DoubleQuote extends PBOpStringTextShow implements
		Op_DoubleQuote {

	/** Type name for {@code PBOp_DoubleQuote} */
	public static final String OP_DOUBLIE_QUOTE_TYPE = "Op_DoubleQuote";

	/** Name of link to the word spacing */
	public static final String WORD_SPACING = "wordSpacing";
	/** Name of link to the character spacing */
	public static final String CHARACTER_SPACING = "characterSpacing";

	/** Position of word spacing property in operands */
	public static final int WORD_SPACING_POSITION = 0;
	/** Position of character spacing property in operands */
	public static final int CHARACTER_SPACING_POSITION = 1;
	/** Number of operands */
	public static final int COUNT_OF_OPERATOR_OPERANDS = 3;

	public PBOp_DoubleQuote(List<COSBase> arguments, GraphicState state, PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(arguments, state, resources, OP_DOUBLIE_QUOTE_TYPE, document, flavour);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case WORD_SPACING:
				return this.getWordSpacing();
			case CHARACTER_SPACING:
				return this.getCharacterSpacing();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosNumber> getWordSpacing() {
		return getSpecialNumber(WORD_SPACING_POSITION);
	}

	private List<CosNumber> getCharacterSpacing() {
		return getSpecialNumber(CHARACTER_SPACING_POSITION);
	}

	private List<CosNumber> getSpecialNumber(int operandNumber) {
		final int size = this.arguments.size();
		if (size >= COUNT_OF_OPERATOR_OPERANDS) {
			int index = size - COUNT_OF_OPERATOR_OPERANDS + operandNumber;
			COSBase base = this.arguments.get(index);
			if (base instanceof COSNumber) {
				List<CosNumber> number = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				number.add(PBCosNumber.fromPDFBoxNumber(base));
				return Collections.unmodifiableList(number);
			}
		}
		return Collections.emptyList();
	}
}
