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
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBAdapterHelper;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * @author Maksim Bezrukov
 */
public class PBPropertiesDictFeaturesObject implements IFeaturesObject {

	private COSDictionary properties;
	private String id;

	/**
	 * Constructs new propertiesDict features object
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 */
	public PBPropertiesDictFeaturesObject(COSDictionary properties, String id) {
		this.properties = properties;
		this.id = id;
	}

	/**
	 * @return PROPERTIES instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PROPERTIES;
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
		if (properties != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
			if (id != null) {
				root.setAttribute("id", id);
			}

			COSBase type = properties.getDictionaryObject(COSName.TYPE);
			if (type instanceof COSName) {
				PBAdapterHelper.addNotEmptyNode("type", ((COSName) type).getName(), root);
			}

			collection.addNewFeatureTree(FeatureObjectType.PROPERTIES, root);
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

}
