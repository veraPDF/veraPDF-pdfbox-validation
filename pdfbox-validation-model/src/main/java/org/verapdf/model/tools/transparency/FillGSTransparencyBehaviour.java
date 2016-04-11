package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class FillGSTransparencyBehaviour implements GSTransparencyBehaviour {

    private static NeitherGSTransparencyBehaviour neither = new NeitherGSTransparencyBehaviour();

    @Override
    public boolean containsTransparency(GraphicState graphicState) {
        return neither.containsTransparency(graphicState) || graphicState.getCa_ns() < 1.0f;
    }
}
