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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PBLowLvlInfoFeaturesObject implements IFeaturesObject {

	private COSDocument document;
	private static final Map<String, String> filtersAbbreviations;

	static {
		Map<String, String> filtersAbbreviationsTemp = new HashMap<>();

		filtersAbbreviationsTemp.put("AHx", "ASCIIHexDecode");
		filtersAbbreviationsTemp.put("A85", "ASCII85Decode");
		filtersAbbreviationsTemp.put("LZW", "LZWDecode");
		filtersAbbreviationsTemp.put("Fl", "FlateDecode");
		filtersAbbreviationsTemp.put("RL", "RunLengthDecode");
		filtersAbbreviationsTemp.put("CCF", "CCITTFaxDecode");
		filtersAbbreviationsTemp.put("DCT", "DCTDecode");
		filtersAbbreviations = Collections.unmodifiableMap(filtersAbbreviationsTemp);
	}

	/**
	 * Constructs new low level info feature object.
	 *
	 * @param document
	 *            pdfbox class represents document object
	 */
	public PBLowLvlInfoFeaturesObject(COSDocument document) {
		this.document = document;
	}

	/**
	 * @return LOW_LVL_INFO instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.LOW_LEVEL_INFO;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection
	 *            collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the
	 *         constructed collection tree
	 * @throws FeatureParsingException
	 *             occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (document != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("lowLevelInfo");

			if (document.getObjects() != null) {
				root.addChild("indirectObjectsNumber")
						.setValue(String.valueOf(document.getObjects().size()));
			}

			addDocumentId(root, collection);

			Set<String> filters = getAllFilters();

			if (!filters.isEmpty()) {
				FeatureTreeNode filtersNode = root.addChild("filters");

				for (String filter : filters) {
					if (filter != null) {
						FeatureTreeNode filterNode = filtersNode.addChild("filter");
						filterNode.setAttribute("name", filter);
					}
				}
			}

			collection.addNewFeatureTree(FeatureObjectType.LOW_LEVEL_INFO, root);
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

	private Set<String> getAllFilters() {
		Set<String> res = new HashSet<>();

		for (COSBase base : document.getObjects()) {

			while (base instanceof COSObject) {
				base = ((COSObject) base).getObject();
			}

			if (base instanceof COSStream) {
				try (COSStream stream = (COSStream) base) {

					COSBase baseFilter = stream.getFilters();

					if (baseFilter != null) {
						addFiltersFromBase(res, baseFilter);
					}
				} catch (IOException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				}
			}
		}

		return res;
	}

	private void addDocumentId(FeatureTreeNode root, FeatureExtractionResult collection) throws FeatureParsingException {
		COSArray ids = document.getDocumentID();
		if (ids != null) {
			String creationId = PBCreateNodeHelper.getStringFromBase(ids.get(0));
			String modificationId = PBCreateNodeHelper.getStringFromBase(ids.get(1));

			FeatureTreeNode documentId = root.addChild("documentId");

			if (creationId != null || modificationId != null) {
				if (creationId != null) {
					documentId.setAttribute("creationId", creationId);
				}
				if (modificationId != null) {
					documentId.setAttribute("modificationId", modificationId);
				}
			}

			if (ids.size() != 2 || creationId == null || modificationId == null) {
				ErrorsHelper.addErrorIntoCollection(collection, documentId,
						"Document's ID must be an array of two not null elements");
			}
		}
	}

	private static void addFiltersFromBase(Set<String> res, COSBase base) {
		if (base instanceof COSName) {
			String name = ((COSName) base).getName();
			if (filtersAbbreviations.keySet().contains(name)) {
				name = filtersAbbreviations.get(name);
			}
			res.add(name);

		} else if (base instanceof COSArray) {

			for (COSBase baseElement : (COSArray) base) {
				if (baseElement instanceof COSName) {
					String name = ((COSName) baseElement).getName();
					if (filtersAbbreviations.keySet().contains(name)) {
						name = filtersAbbreviations.get(name);
					}
					res.add(name);
				}
			}
		}
	}
}
