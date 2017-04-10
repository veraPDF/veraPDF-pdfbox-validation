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

import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.verapdf.features.objects.TilingPatternFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Feature object adapter for tilling pattern
 *
 * @author Maksim Bezrukov
 */
public class PBTilingPatternFeaturesObjectAdapter implements TilingPatternFeaturesObjectAdapter {

	private PDTilingPattern tilingPattern;
	private String id;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;

	/**
	 * Constructs new tilling pattern features object adapter
	 *
	 * @param tilingPattern   PDTilingPattern which represents tilling pattern for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this pattern
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this pattern
	 * @param patternChild    set of pattern id which contains in resource dictionary of this pattern
	 * @param shadingChild    set of shading id which contains in resource dictionary of this pattern
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this pattern
	 * @param fontChild       set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild set of properties id which contains in resource dictionary of this pattern
	 */
	public PBTilingPatternFeaturesObjectAdapter(PDTilingPattern tilingPattern, String id, Set<String> extGStateChild,
												Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild,
												Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild) {
		this.tilingPattern = tilingPattern;
		this.id = id;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public Set<String> getExtGStateChild() {
		return this.extGStateChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.extGStateChild);
	}

	@Override
	public Set<String> getColorSpaceChild() {
		return this.colorSpaceChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.colorSpaceChild);
	}

	@Override
	public Set<String> getPatternChild() {
		return this.patternChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.patternChild);
	}

	@Override
	public Set<String> getShadingChild() {
		return this.shadingChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.shadingChild);
	}

	@Override
	public Set<String> getXObjectChild() {
		return this.xobjectChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.xobjectChild);
	}

	@Override
	public Set<String> getFontChild() {
		return this.fontChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.fontChild);
	}

	@Override
	public Set<String> getPropertiesChild() {
		return this.propertiesChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.propertiesChild);
	}

	@Override
	public Long getPaintType() {
		if (this.tilingPattern != null) {
			return Long.valueOf(this.tilingPattern.getPaintType());
		}
		return null;
	}

	@Override
	public Long getTilingType() {
		if (this.tilingPattern != null) {
			return Long.valueOf(this.tilingPattern.getTilingType());
		}
		return null;
	}

	@Override
	public double[] getBBox() {
		if (this.tilingPattern != null) {
			return PBAdapterHelper.parseRectangle(this.tilingPattern.getBBox());
		}
		return null;
	}

	@Override
	public Double getXStep() {
		if (this.tilingPattern != null) {
			return Double.valueOf(this.tilingPattern.getXStep());
		}
		return null;
	}

	@Override
	public Double getYStep() {
		if (this.tilingPattern != null) {
			return Double.valueOf(this.tilingPattern.getYStep());
		}
		return null;
	}

	@Override
	public double[] getMatrix() {
		if (this.tilingPattern != null) {
			return PBAdapterHelper.parseFloatMatrix(this.tilingPattern.getMatrix().getValues());
		}
		return null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
