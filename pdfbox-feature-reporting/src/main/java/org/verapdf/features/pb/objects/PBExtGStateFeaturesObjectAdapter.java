/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.features.objects.ExtGStateFeaturesObjectAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Feature object adapter for extended graphics state
 *
 * @author Maksim Bezrukov
 */
public class PBExtGStateFeaturesObjectAdapter implements ExtGStateFeaturesObjectAdapter {

	private PDExtendedGraphicsState exGState;
	private String id;
	private String fontChildID;

	/**
	 * Constructs new extended graphics state feature object adapter
	 *
	 * @param exGState         PDExtendedGraphicsState which represents extended graphics state for feature report
	 * @param id               id of the object
	 * @param fontChildID      id of the font child
	 */
	public PBExtGStateFeaturesObjectAdapter(PDExtendedGraphicsState exGState,
											String id,
											String fontChildID) {
		this.exGState = exGState;
		this.id = id;
		this.fontChildID = fontChildID;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getFontChildId() {
		return this.fontChildID;
	}

	@Override
	public Boolean getTransparency() {
		if (this.exGState != null) {
			return !this.exGState.getAlphaSourceFlag();
		}
		return null;
	}

	@Override
	public Boolean getStrokeAdjustment() {
		if (this.exGState != null) {
			return this.exGState.getAutomaticStrokeAdjustment();
		}
		return null;
	}

	@Override
	public Boolean getOverprintForStroke() {
		if (this.exGState != null) {
			return this.exGState.getStrokingOverprintControl();
		}
		return null;
	}

	@Override
	public Boolean getOverprintForFill() {
		if (this.exGState != null) {
			return this.exGState.getNonStrokingOverprintControl();
		}
		return null;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.exGState != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
