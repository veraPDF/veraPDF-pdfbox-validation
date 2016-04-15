package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class FillStrokeTransparencyBehaviour implements TransparencyBehaviour {

    private static final FillStrokeTransparencyBehaviour INSTANCE = new FillStrokeTransparencyBehaviour();

    private FillStrokeTransparencyBehaviour() {
    }

    public static FillStrokeTransparencyBehaviour getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean containsTransparency(GraphicState graphicState) {
        return NeitherTransparencyBehaviour.getInstance().containsTransparency(graphicState)
                || graphicState.getCa_ns() < 1.0f
                || graphicState.getCa() < 1.0f;
    }
}
