package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;

/**
 * @author Maksim Bezrukov
 */
public interface TransparencyBehaviour {

    boolean containsTransparency(GraphicState gs);
}
