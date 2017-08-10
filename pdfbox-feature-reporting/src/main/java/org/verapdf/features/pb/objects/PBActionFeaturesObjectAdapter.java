package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.verapdf.features.objects.ActionFeaturesObjectAdapter;

import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class PBActionFeaturesObjectAdapter implements ActionFeaturesObjectAdapter {

	private final PDAction action;
	private final Location location;

	public PBActionFeaturesObjectAdapter(PDAction action, Location location) {
		this.action = action;
		this.location = location;
	}

	@Override
	public String getType() {
		return this.action.getSubType();
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.action != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
