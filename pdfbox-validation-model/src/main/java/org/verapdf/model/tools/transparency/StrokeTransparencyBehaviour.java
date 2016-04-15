package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class StrokeTransparencyBehaviour implements TransparencyBehaviour {

    private static final StrokeTransparencyBehaviour INSTANCE = new StrokeTransparencyBehaviour();

    private StrokeTransparencyBehaviour() {
    }

    public static StrokeTransparencyBehaviour getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean containsTransparency(GraphicState gs) {
        return NeitherTransparencyBehaviour.getInstance().containsTransparency(gs) || gs.getCa() < 1.0f;
    }
}
