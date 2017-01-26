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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDPage;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Set;

/**
 * Feature object for page
 *
 * @author Maksim Bezrukov
 */
public class PBPageFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDPage page;
	private String thumb;
	private Set<String> annotsId;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;
	private int index;

	/**
	 * Constructs new Page Feature Object
	 *
	 * @param page            pdfbox class represents page object
	 * @param thumb           thumbnail image id
	 * @param annotsId        set of annotations id which contains in this page
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this page
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this page
	 * @param patternChild    set of pattern id which contains in resource dictionary of this page
	 * @param shadingChild    set of shading id which contains in resource dictionary of this page
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this page
	 * @param fontChild       set of font id which contains in resource dictionary of this page
	 * @param propertiesChild set of properties id which contains in resource dictionary of this page
	 * @param index           page index
	 */
	public PBPageFeaturesObject(PDPage page,
								String thumb,
								Set<String> annotsId,
								Set<String> extGStateChild,
								Set<String> colorSpaceChild,
								Set<String> patternChild,
								Set<String> shadingChild,
								Set<String> xobjectChild,
								Set<String> fontChild,
								Set<String> propertiesChild,
								int index) {
		this.page = page;
		this.thumb = thumb;
		this.annotsId = annotsId;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
		this.index = index;
	}

	/**
	 * @return PAGE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PAGE;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (page != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("page");

			root.setAttribute("orderNumber", Integer.toString(index));

			PBCreateNodeHelper.addBoxFeature("mediaBox", page.getMediaBox(), root);
			PBCreateNodeHelper.addBoxFeature("cropBox", page.getCropBox(), root);
			PBCreateNodeHelper.addBoxFeature("trimBox", page.getTrimBox(), root);
			PBCreateNodeHelper.addBoxFeature("bleedBox", page.getBleedBox(), root);
			PBCreateNodeHelper.addBoxFeature("artBox", page.getArtBox(), root);

			root.addChild("rotation").setValue(String.valueOf(page.getRotation()));

			COSBase base = page.getCOSObject().getDictionaryObject(COSName.getPDFName("PZ"));
			if (base != null) {
				FeatureTreeNode scaling = root.addChild("scaling");

				while (base instanceof COSObject) {
					base = ((COSObject) base).getObject();
				}

				if (base instanceof COSNumber) {
					COSNumber number = (COSNumber) base;
					scaling.setValue(String.valueOf(number.doubleValue()));
				} else {
					ErrorsHelper.addErrorIntoCollection(collection,
							scaling,
							"Scaling is not a number");
				}
			}

			if (thumb != null) {
				FeatureTreeNode thumbNode = root.addChild("thumbnail");
				thumbNode.setAttribute(ID, thumb);
			}

			PBCreateNodeHelper.parseMetadata(page.getMetadata(), "metadata", root, collection);

			PBCreateNodeHelper.parseIDSet(annotsId, "annotation", "annotations", root);

			parseResources(root);

			collection.addNewFeatureTree(FeatureObjectType.PAGE, root);

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

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {
		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = root.addChild("resources");

			PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}
}
