package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class FillTransparencyBehaviour implements TransparencyBehaviour {

	private static final FillTransparencyBehaviour INSTANCE = new FillTransparencyBehaviour();

    private FillTransparencyBehaviour() {
    }

    public static FillTransparencyBehaviour getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean containsTransparency(GraphicState graphicState) {
        return NeitherTransparencyBehaviour.getInstance().containsTransparency(graphicState)
				|| graphicState.getCa_ns() < 1.0f;
    }
}
