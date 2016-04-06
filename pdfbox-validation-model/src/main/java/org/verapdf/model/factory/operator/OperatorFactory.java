package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.*;

/**
 * Class for converting pdfbox operators to the veraPDF-library operators
 *
 * @author Timur Kamalov
 */
public final class OperatorFactory {

    private static final Logger LOGGER = Logger
            .getLogger(OperatorFactory.class);
    private static final String MSG_UNEXPECTED_OBJECT_TYPE = "Unexpected type of object in tokens: ";
    private static final String GS_CLONE_MALFUNCTION = "GraphicsState clone function threw CloneNotSupportedException.";

	private boolean isLastParsedContainsTransparency= false;
	private static final Set<String> PAINT_OPERATORS_WITHOUT_TEXT = new HashSet<>(Arrays.asList(new String[]{
			Operators.S_STROKE,
			Operators.S_CLOSE_STROKE,
			Operators.F_FILL,
			Operators.F_FILL_OBSOLETE,
			Operators.F_STAR_FILL,
			Operators.B_FILL_STROKE,
			Operators.B_STAR_EOFILL_STROKE,
			Operators.B_CLOSEPATH_FILL_STROKE,
			Operators.B_STAR_CLOSEPATH_EOFILL_STROKE,
			Operators.SH,
			Operators.DO,
			Operators.EI
	}));

	private static final Set<String> PAINT_OPERATORS_TEXT = new HashSet<>(Arrays.asList(new String[]{
			Operators.TJ_SHOW,
			Operators.QUOTE,
			Operators.DOUBLE_QUOTE,
			Operators.TJ_SHOW_POS
	}));

    public OperatorFactory() {
        // Disable default constructor
    }

	public boolean isLastParsedContainsTransparency() {
		return isLastParsedContainsTransparency;
	}

	/**
	 * Converts pdfbox operators and arguments from content stream
	 * to the corresponding {@link Operator} objects of veraPDF-library
	 *
	 * @param pdfBoxTokens list of {@link COSBase} or
	 * 					   {@link org.apache.pdfbox.contentstream.operator.Operator}
	 * 					   objects
	 * @param resources    resources for a given stream
	 * @return list of {@link Operator} objects of veraPDF-library
	 */
    public List<Operator> operatorsFromTokens(List<Object> pdfBoxTokens,
                                                     PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
        List<Operator> result = new ArrayList<>();
        List<COSBase> arguments = new ArrayList<>();
		this.isLastParsedContainsTransparency = false;
        OperatorParser parser = new OperatorParser(document, flavour);

        for (Object pdfBoxToken : pdfBoxTokens) {
            if (pdfBoxToken instanceof COSBase) {
                arguments.add((COSBase) pdfBoxToken);
            } else if (pdfBoxToken instanceof org.apache.pdfbox.contentstream.operator.Operator) {
                try {
                    parser.parseOperator(result,
                            (org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken,
                            resources, arguments);

					String parsedOperatorType = ((org.apache.pdfbox.contentstream.operator.Operator) pdfBoxToken).getName();
					GraphicState graphicState = parser.getGraphicState();
					if (PAINT_OPERATORS_WITHOUT_TEXT.contains(parsedOperatorType)
							|| (PAINT_OPERATORS_TEXT.contains(parsedOperatorType) && graphicState.getRenderingMode() != RenderingMode.NEITHER)) {
						isLastParsedContainsTransparency |= containsTransparency(graphicState);
					}
                } catch (CloneNotSupportedException e) {
					LOGGER.debug("GraphicsState clone issues for pdfBoxToken:" + pdfBoxToken);
					LOGGER.debug(GS_CLONE_MALFUNCTION, e);
				} catch (IOException e) {
					LOGGER.debug(e);
				}
				arguments = new ArrayList<>();
			} else {
                LOGGER.error(MSG_UNEXPECTED_OBJECT_TYPE
                        + pdfBoxToken.getClass().getName());
            }
        }
        return result;
    }

	private static boolean containsTransparency(GraphicState graphicState) {
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

		return graphicState.getCa() < 1f || graphicState.getCa_ns() < 1f;
	}
}
