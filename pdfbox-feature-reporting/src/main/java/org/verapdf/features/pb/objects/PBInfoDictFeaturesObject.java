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

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Feature object for information dictionary
 *
 * @author Maksim Bezrukov
 */
public class PBInfoDictFeaturesObject implements IFeaturesObject {

	private static final String[] predefinedKeys = {"Title", "Author", "Subject", "Keywords", "Creator", "Producer", "CreationDate", "ModDate", "Trapped"};

	private static final String ENTRY = "entry";
	private static final String KEY = "key";

	private PDDocumentInformation info;

	/**
	 * Constructs new information dictionary feature object.
	 *
	 * @param info pdfbox class represents page object
	 */
	public PBInfoDictFeaturesObject(PDDocumentInformation info) {
		this.info = info;
	}

	/**
	 * @return INFORMATION_DICTIONARY instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.INFORMATION_DICTIONARY;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {

		if (info != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("informationDict");

			addEntry("Title", info.getTitle(), root);
			addEntry("Author", info.getAuthor(), root);
			addEntry("Subject", info.getSubject(), root);
			addEntry("Keywords", info.getKeywords(), root);
			addEntry("Creator", info.getCreator(), root);
			addEntry("Producer", info.getProducer(), root);

			FeatureTreeNode creationDate = PBCreateNodeHelper.createDateNode(ENTRY, root, info.getCreationDate(), collection);
			if (creationDate != null) {
				creationDate.setAttribute(KEY, "CreationDate");
			}

			FeatureTreeNode modificationDate = PBCreateNodeHelper.createDateNode(ENTRY, root, info.getModificationDate(), collection);
			if (modificationDate != null) {
				modificationDate.setAttribute(KEY, "ModDate");
			}

			addEntry("Trapped", info.getTrapped(), root);

			if (info.getMetadataKeys() != null) {
				Set<String> keys = new TreeSet<>(info.getMetadataKeys());
				keys.removeAll(Arrays.asList(predefinedKeys));
				for (String key : keys) {
					addEntry(key, info.getCustomMetadataValue(key), root);
				}
			}

			collection.addNewFeatureTree(FeatureObjectType.INFORMATION_DICTIONARY, root);

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

	private static void addEntry(String name, String value, FeatureTreeNode root) throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode entry = root.addChild(ENTRY);
			entry.setValue(value);
			entry.setAttribute(KEY, name);
		}
	}
}
