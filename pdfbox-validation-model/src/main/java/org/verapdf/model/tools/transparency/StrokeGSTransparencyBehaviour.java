package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public class StrokeGSTransparencyBehaviour implements GSTransparencyBehaviour {

    private static final NeitherGSTransparencyBehaviour neither = new NeitherGSTransparencyBehaviour();

    @Override
    public boolean containsTransparency(GraphicState gs) {
        return neither.containsTransparency(gs) || gs.getCa() < 1.0f;
    }
}
