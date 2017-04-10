/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.features.objects.OutlinesFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Feature object adapter for outlines
 *
 * @author Maksim Bezrukov
 */
public class PBOutlinesFeaturesObjectAdapter implements OutlinesFeaturesObjectAdapter {

	private PDDocumentOutline outline;

	/**
	 * Constructs new OutputIntent Feature Object adapter
	 *
	 * @param outline pdfbox class represents outlines object
	 */
	public PBOutlinesFeaturesObjectAdapter(PDDocumentOutline outline) {
		this.outline = outline;
	}

	@Override
	public List<OutlineFeaturesObjectAdapter> getChildren() {
		if (outline != null) {
			List<OutlineFeaturesObjectAdapter> res = new ArrayList<>();
			for (PDOutlineItem item : outline.children()) {
				res.add(new PBOutlineFeaturesObjectAdapter(item));
			}
			return Collections.unmodifiableList(res);
		}
		return Collections.emptyList();
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}

	private static class PBOutlineFeaturesObjectAdapter implements OutlineFeaturesObjectAdapter {

		private PDOutlineItem outline;

		PBOutlineFeaturesObjectAdapter(PDOutlineItem outline) {
			this.outline = outline;
		}

		@Override
		public Integer getKeyNumber() {
			if (outline != null) {
				COSDictionary dict = outline.getCOSObject();
				if (dict != null) {
					COSObjectKey key = dict.getKey();
					if (key != null) {
						return Integer.valueOf((int) key.getNumber());
					}
				}
			}
			return null;
		}

		@Override
		public String getTitle() {
			if (outline != null) {
				return outline.getTitle();
			}
			return null;
		}

		@Override
		public double[] getColor() {
			if (this.outline != null) {
				PDColor color = outline.getTextColor();
				return PBAdapterHelper.castFloatArrayToDouble(color.getComponents());
			}
			return null;
		}

		@Override
		public boolean isItalic() {
			return outline != null && outline.isItalic();
		}

		@Override
		public boolean isBold() {
			return outline != null && outline.isBold();
		}

		@Override
		public List<OutlineFeaturesObjectAdapter> getChildren() {
			if (outline != null) {
				List<OutlineFeaturesObjectAdapter> res = new ArrayList<>();
				for (PDOutlineItem item : outline.children()) {
					res.add(new PBOutlineFeaturesObjectAdapter(item));
				}
				return Collections.unmodifiableList(res);
			}
			return Collections.emptyList();
		}
	}
}
