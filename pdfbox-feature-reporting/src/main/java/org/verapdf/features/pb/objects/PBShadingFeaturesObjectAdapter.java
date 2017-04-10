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

import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.verapdf.features.objects.ShadingFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.util.Collections;
import java.util.List;

/**
 * Features object adapter for shading
 *
 * @author Maksim Bezrukov
 */
public class PBShadingFeaturesObjectAdapter implements ShadingFeaturesObjectAdapter {

	private PDShading shading;
	private String id;
	private String colorSpaceChild;

	/**
	 * Constructs new shading features object adapter
	 *
	 * @param shading         PDShading which represents shading for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this shading
	 */
	public PBShadingFeaturesObjectAdapter(PDShading shading, String id, String colorSpaceChild) {
		this.shading = shading;
		this.id = id;
		this.colorSpaceChild = colorSpaceChild;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getColorSpaceChild() {
		return this.colorSpaceChild;
	}

	@Override
	public int getShadingType() {
		if (this.shading != null) {
			return this.shading.getShadingType();
		}
		return 0;
	}

	@Override
	public double[] getBBox() {
		if (this.shading != null) {
			return PBAdapterHelper.parseRectangle(this.shading.getBBox());
		}
		return null;
	}

	@Override
	public boolean getAntiAlias() {
		return this.shading != null && this.shading.getAntiAlias();
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
