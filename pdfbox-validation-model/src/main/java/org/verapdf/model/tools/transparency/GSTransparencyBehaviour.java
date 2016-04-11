package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public interface GSTransparencyBehaviour {

    boolean containsTransparency(GraphicState gs);
}
