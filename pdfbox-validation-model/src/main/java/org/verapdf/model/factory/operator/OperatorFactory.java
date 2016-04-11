package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.model.tools.transparency.FillGSTransparencyBehaviour;
import org.verapdf.model.tools.transparency.FillStrokeGSTransparencyBehaviour;
import org.verapdf.model.tools.transparency.GSTransparencyBehaviour;
import org.verapdf.model.tools.transparency.StrokeGSTransparencyBehaviour;
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

    private boolean isLastParsedContainsTransparency = false;

    private static final Map<String, GSTransparencyBehaviour> PAINT_OPERATORS_WITHOUT_TEXT;
    static {
        Map<String, GSTransparencyBehaviour> aMap = new HashMap<>();
        GSTransparencyBehaviour stroke = new StrokeGSTransparencyBehaviour();
        GSTransparencyBehaviour fill = new FillGSTransparencyBehaviour();
        GSTransparencyBehaviour fillStroke = new FillStrokeGSTransparencyBehaviour();
        aMap.put(Operators.S_STROKE, stroke);
        aMap.put(Operators.S_CLOSE_STROKE, stroke);
        aMap.put(Operators.F_FILL, fill);
        aMap.put(Operators.F_FILL_OBSOLETE, fill);
        aMap.put(Operators.F_STAR_FILL, fill);
        aMap.put(Operators.B_FILL_STROKE, fillStroke);
        aMap.put(Operators.B_STAR_EOFILL_STROKE, fillStroke);
        aMap.put(Operators.B_CLOSEPATH_FILL_STROKE, fillStroke);
        aMap.put(Operators.B_STAR_CLOSEPATH_EOFILL_STROKE, fillStroke);
        aMap.put(Operators.SH, fill);
        aMap.put(Operators.DO, fill);
        aMap.put(Operators.EI, fill);
        PAINT_OPERATORS_WITHOUT_TEXT = Collections.unmodifiableMap(aMap);
    }

    private static final Set<String> PAINT_OPERATORS_TEXT = new HashSet<>(Arrays.asList(new String[]{
            Operators.TJ_SHOW,
            Operators.QUOTE,
            Operators.DOUBLE_QUOTE,
            Operators.TJ_SHOW_POS
    }));

    private static final Map<RenderingMode, GSTransparencyBehaviour> RENDERING_MODE;
    static {
        Map<RenderingMode, GSTransparencyBehaviour> aMap = new HashMap<>();
        GSTransparencyBehaviour stroke = new StrokeGSTransparencyBehaviour();
        GSTransparencyBehaviour fill = new FillGSTransparencyBehaviour();
        GSTransparencyBehaviour fillStroke = new FillStrokeGSTransparencyBehaviour();
        aMap.put(RenderingMode.FILL, fill);
        aMap.put(RenderingMode.STROKE, stroke);
        aMap.put(RenderingMode.FILL_STROKE, fillStroke);
        aMap.put(RenderingMode.FILL_CLIP, fill);
        aMap.put(RenderingMode.STROKE_CLIP, stroke);
        aMap.put(RenderingMode.FILL_STROKE_CLIP, fillStroke);
        RENDERING_MODE = Collections.unmodifiableMap(aMap);
    }

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
     *                     {@link org.apache.pdfbox.contentstream.operator.Operator}
     *                     objects
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
                    if (PAINT_OPERATORS_WITHOUT_TEXT.containsKey(parsedOperatorType)) {
                        isLastParsedContainsTransparency |= PAINT_OPERATORS_WITHOUT_TEXT.get(parsedOperatorType).containsTransparency(graphicState);
                    } else {
                        RenderingMode renderingMode = graphicState.getRenderingMode();
                        if (PAINT_OPERATORS_TEXT.contains(parsedOperatorType) && RENDERING_MODE.containsKey(renderingMode)) {
                            isLastParsedContainsTransparency |= RENDERING_MODE.get(renderingMode).containsTransparency(graphicState);
                        }
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
}
