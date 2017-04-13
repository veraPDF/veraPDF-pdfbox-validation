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

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.features.objects.AnnotationFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Feature object adapter for annotation
 *
 * @author Maksim Bezrukov
 */
public class PBAnnotationFeaturesObjectAdapter implements AnnotationFeaturesObjectAdapter {

	private static final int LOCKED_CONTENTS_FLAG = 512;

	private PDAnnotation annot;
	private String id;
	private String popupId;
	private Set<String> formXObjects;


	/**
	 * Constructs new Annotation Feature Object adapter
	 *
	 * @param annot        pdfbox class represents annotation object
	 * @param id           annotation id
	 * @param popupId      id of the popup annotation
	 * @param formXObjects set of id of the form XObjects which used in appearance stream of this annotation
	 */
	public PBAnnotationFeaturesObjectAdapter(PDAnnotation annot, String id,
											 String popupId, Set<String> formXObjects) {
		this.annot = annot;
		this.id = id;
		this.popupId = popupId;
		this.formXObjects = formXObjects;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getPopupId() {
		return popupId;
	}

	@Override
	public Set<String> getFormXObjectsResources() {
		return this.formXObjects == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.formXObjects);
	}

	@Override
	public String getSubtype() {
		if (annot != null) {
			return annot.getSubtype();
		}
		return null;
	}

	@Override
	public double[] getRectangle() {
		if (annot != null) {
			return PBAdapterHelper.parseRectangle(annot.getRectangle());
		}
		return null;
	}

	@Override
	public String getContents() {
		if (annot != null) {
			return annot.getContents();
		}
		return null;
	}

	@Override
	public String getAnnotationName() {
		if (annot != null) {
			return annot.getAnnotationName();
		}
		return null;
	}

	@Override
	public String getModifiedDate() {
		if (annot != null) {
			return annot.getModifiedDate();
		}
		return null;
	}

	@Override
	public double[] getColor() {
		if (annot != null) {
			PDColor color = annot.getColor();
			if (color != null) {
				return PBAdapterHelper.castFloatArrayToDouble(color.getComponents());
			}
		}
		return null;
	}

	@Override
	public boolean isInvisible() {
		return annot != null && annot.isInvisible();
	}

	@Override
	public boolean isHidden() {
		return annot != null && annot.isHidden();
	}

	@Override
	public boolean isPrinted() {
		return annot != null && annot.isPrinted();
	}

	@Override
	public boolean isNoZoom() {
		return annot != null && annot.isNoZoom();
	}

	@Override
	public boolean isNoRotate() {
		return annot != null && annot.isNoRotate();
	}

	@Override
	public boolean isNoView() {
		return annot != null && annot.isNoView();
	}

	@Override
	public boolean isReadOnly() {
		return annot != null && annot.isReadOnly();
	}

	@Override
	public boolean isLocked() {
		return annot != null && annot.isLocked();
	}

	@Override
	public boolean isToggleNoView() {
		return annot != null && annot.isToggleNoView();
	}

	@Override
	public boolean isLockedContents() {
		if (annot != null) {
			return (annot.getAnnotationFlags() & LOCKED_CONTENTS_FLAG) == LOCKED_CONTENTS_FLAG;
		}
		return false;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.annot != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
