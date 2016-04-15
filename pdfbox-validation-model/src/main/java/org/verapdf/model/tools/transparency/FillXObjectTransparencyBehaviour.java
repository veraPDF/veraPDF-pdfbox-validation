package org.verapdf.model.tools.transparency;

import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXForm;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXImage;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;

/**
 * @author Maksim Bezrukov
 */
public class FillXObjectTransparencyBehaviour implements TransparencyBehaviour {

	private static final FillXObjectTransparencyBehaviour INSTANCE = new FillXObjectTransparencyBehaviour();

	private FillXObjectTransparencyBehaviour() {
	}

	public static FillXObjectTransparencyBehaviour getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean containsTransparency(GraphicState gs) {
		return FillTransparencyBehaviour.getInstance().containsTransparency(gs)
				|| xObjectContainsTransparency(gs.getXObject());
	}

	private boolean xObjectContainsTransparency(PBoxPDXObject xobj) {

		if (xobj instanceof PBoxPDXForm) {
			return ((PBoxPDXForm) xobj).containsTransparency();
		} else if (xobj instanceof PBoxPDXImage) {
			return ((PBoxPDXImage) xobj).containsTransparency();
		}

		return false;
	}
}
