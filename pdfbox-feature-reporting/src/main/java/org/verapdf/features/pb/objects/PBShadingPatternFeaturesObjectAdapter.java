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

import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.verapdf.features.objects.ShadingPatternFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.util.Collections;
import java.util.List;

/**
 * Features object adapter for shading pattern
 *
 * @author Maksim Bezrukov
 */
public class PBShadingPatternFeaturesObjectAdapter implements ShadingPatternFeaturesObjectAdapter {

	private PDShadingPattern shadingPattern;
	private String id;
	private String shadingChild;
	private String extGStateChild;

	/**
	 * Constructs new tilling pattern features object adapter
	 *
	 * @param shadingPattern PDShadingPattern which represents shading pattern for feature report
	 * @param id             id of the object
	 * @param extGStateChild external graphics state id which contains in this shading pattern
	 * @param shadingChild   shading id which contains in this shading pattern
	 */
	public PBShadingPatternFeaturesObjectAdapter(PDShadingPattern shadingPattern, String id, String shadingChild, String extGStateChild) {
		this.shadingPattern = shadingPattern;
		this.id = id;
		this.shadingChild = shadingChild;
		this.extGStateChild = extGStateChild;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getShadingChild() {
		return this.shadingChild;
	}

	@Override
	public String getExtGStateChild() {
		return this.extGStateChild;
	}

	@Override
	public double[] getMatrix() {
		if (this.shadingPattern != null) {
			return PBAdapterHelper.parseFloatMatrix(this.shadingPattern.getMatrix().getValues());
		}
		return null;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.shadingPattern != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
