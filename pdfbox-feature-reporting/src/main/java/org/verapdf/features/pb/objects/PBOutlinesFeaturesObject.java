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
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.ColorComponent;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Feature object for outlines
 *
 * @author Maksim Bezrukov
 */
public class PBOutlinesFeaturesObject implements IFeaturesObject {

	private PDDocumentOutline outline;

	/**
	 * Constructs new OutputIntent Feature Object
	 *
	 * @param outline pdfbox class represents outlines object
	 */
	public PBOutlinesFeaturesObject(PDDocumentOutline outline) {
		this.outline = outline;
	}

	/**
	 * @return OUTLINES instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.OUTLINES;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection)
			throws FeatureParsingException {
		if (outline != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("outlines");

			if (outline.children() != null) {
				Set<PDOutlineItem> items = new HashSet<>();
				for (PDOutlineItem item : outline.children()) {
					if (!items.contains(item)) {
						createItem(item, root, collection, items);
					}
				}
			}

			collection
					.addNewFeatureTree(FeatureObjectType.OUTLINES, root);
			return root;
		}
		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private static void createItem(PDOutlineItem item, FeatureTreeNode root,
								   FeatureExtractionResult collection, Set<PDOutlineItem> items) throws FeatureParsingException {
		if (item != null) {
			items.add(item);
			FeatureTreeNode itemNode = root.addChild(
					"outline");

			PBCreateNodeHelper.addNotEmptyNode("title", item.getTitle(),
					itemNode);


			FeatureTreeNode color = itemNode.addChild(
					"color");

			PDColor clr = item.getTextColor();
			float[] rgb = clr.getComponents();
			if (rgb.length == ColorComponent.RGB_COMPONENTS.getColors().size()) {
				color.setAttributes(ColorComponent.RGB_COMPONENTS.createAttributesMap(rgb));
			} else {
				ErrorsHelper.addErrorIntoCollection(collection,
						color,
						"Color must be in rgb form");
			}


			FeatureTreeNode style = itemNode.addChild("style");
			style.setAttribute("italic", String.valueOf(item.isItalic()));
			style.setAttribute("bold", String.valueOf(item.isBold()));

			for (PDOutlineItem child : item.children()) {
				if (!items.contains(child)) {
					createItem(child, itemNode, collection, items);
				}
			}
		}
	}
}
