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
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDGroup;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Set;

/**
 * Feature object for form xobjects
 *
 * @author Maksim Bezrukov
 */
public class PBFormXObjectFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDFormXObject formXObject;
	private String id;
	private String groupColorSpaceChild;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;

	/**
	 * Constructs new form xobject features object
	 *
	 * @param formXObject          PDFormXObject which represents form xobject for feature report
	 * @param id                   id of the object
	 * @param groupColorSpaceChild id of the group xobject which contains in the given form xobject
	 * @param extGStateChild       set of external graphics state id which contains in resource dictionary of this xobject
	 * @param colorSpaceChild      set of ColorSpace id which contains in resource dictionary of this xobject
	 * @param patternChild         set of pattern id which contains in resource dictionary of this xobject
	 * @param shadingChild         set of shading id which contains in resource dictionary of this xobject
	 * @param xobjectChild         set of XObject id which contains in resource dictionary of this xobject
	 * @param fontChild            set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild      set of properties id which contains in resource dictionary of this xobject
	 */
	public PBFormXObjectFeaturesObject(PDFormXObject formXObject, String id, String groupColorSpaceChild,
									   Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild,
									   Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild,
									   Set<String> propertiesChild) {
		this.formXObject = formXObject;
		this.id = id;
		this.groupColorSpaceChild = groupColorSpaceChild;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
	}

	/**
	 * @return FORM_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.FORM_XOBJECT;
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
		if (formXObject != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
			root.setAttribute("type", "form");
			if (id != null) {
				root.setAttribute(ID, id);
			}

			PBCreateNodeHelper.addBoxFeature("bbox", formXObject.getBBox(), root);
			PBCreateNodeHelper.parseFloatMatrix(formXObject.getMatrix().getValues(), root.addChild("matrix"));

			PDGroup group = formXObject.getGroup();
			if (group != null) {
				FeatureTreeNode groupNode = root.addChild("group");
				if (group.getSubType() != null) {
					PBCreateNodeHelper.addNotEmptyNode("subtype", group.getSubType().getName(), groupNode);
					if ("Transparency".equals(group.getSubType().getName())) {
						if (groupColorSpaceChild != null) {
							FeatureTreeNode clr = groupNode.addChild("colorSpace");
							clr.setAttribute(ID, groupColorSpaceChild);
						}

						groupNode.addChild("isolated").setValue(String.valueOf(group.isIsolated()));
						groupNode.addChild("knockout").setValue(String.valueOf(group.isKnockout()));
					}

				}
			}

			if (formXObject.getCOSStream().getItem(COSName.STRUCT_PARENTS) != null) {
				root.addChild("structParents").setValue(String.valueOf(formXObject.getStructParents()));
			}


			COSBase cosBase = formXObject.getCOSStream().getDictionaryObject(COSName.METADATA);
			if (cosBase instanceof COSStream) {
				PDMetadata meta = new PDMetadata((COSStream) cosBase);
				PBCreateNodeHelper.parseMetadata(meta, "metadata", root, collection);
			}

			parseResources(root);

			collection.addNewFeatureTree(FeatureObjectType.FORM_XOBJECT, root);
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
