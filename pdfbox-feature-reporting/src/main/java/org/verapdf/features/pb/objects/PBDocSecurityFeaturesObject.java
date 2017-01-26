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

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * Features object for document security
 *
 * @author Maksim Bezrukov
 */
public class PBDocSecurityFeaturesObject implements IFeaturesObject {
	private static final Logger LOGGER = Logger
			.getLogger(PBDocSecurityFeaturesObject.class);
	private PDEncryption encryption;

	/**
	 * Constructs new Document Security Feature Object
	 *
	 * @param encryption pdfbox class represents Encryption object
	 */
	public PBDocSecurityFeaturesObject(PDEncryption encryption) {
		this.encryption = encryption;
	}

	/**
	 * @return DOCUMENT_SECURITY instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.DOCUMENT_SECURITY;
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
		if (encryption != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("documentSecurity");
			PBCreateNodeHelper.addNotEmptyNode("filter", encryption.getFilter(), root);
			PBCreateNodeHelper.addNotEmptyNode("subFilter", encryption.getSubFilter(), root);
			PBCreateNodeHelper.addNotEmptyNode("version", String.valueOf(encryption.getVersion()), root);
			PBCreateNodeHelper.addNotEmptyNode("length", String.valueOf(encryption.getLength()), root);

			try {
				String ownerKey = new COSString(encryption.getOwnerKey()).toHexString();
				PBCreateNodeHelper.addNotEmptyNode("ownerKey", ownerKey, root);
			} catch (IOException e) {
				LOGGER.debug("PDFBox error getting owner key data", e);
				FeatureTreeNode ownerKey = root.addChild("ownerKey");
				ErrorsHelper.addErrorIntoCollection(collection,
						ownerKey,
						e.getMessage());
			}

			try {
				String userKey = new COSString(encryption.getUserKey()).toHexString();
				PBCreateNodeHelper.addNotEmptyNode("userKey", userKey, root);
			} catch (IOException e) {
				LOGGER.debug("PDFBox error getting user key data", e);
				FeatureTreeNode userKey = root.addChild("userKey");
				ErrorsHelper.addErrorIntoCollection(collection,
						userKey,
						e.getMessage());
			}

			PBCreateNodeHelper.addNotEmptyNode("encryptMetadata", String.valueOf(encryption.isEncryptMetaData()), root);

			try {
				if (encryption.getSecurityHandler() != null) {
					AccessPermission accessPermissions = new AccessPermission(encryption.getPermissions());

					PBCreateNodeHelper.addNotEmptyNode("printAllowed", String.valueOf(accessPermissions.canPrint()), root);
					PBCreateNodeHelper.addNotEmptyNode("printDegradedAllowed", String.valueOf(accessPermissions.canPrintDegraded()), root);
					PBCreateNodeHelper.addNotEmptyNode("changesAllowed", String.valueOf(accessPermissions.canModify()), root);
					PBCreateNodeHelper.addNotEmptyNode("modifyAnnotationsAllowed", String.valueOf(accessPermissions.canModifyAnnotations()), root);
					PBCreateNodeHelper.addNotEmptyNode("fillingSigningAllowed", String.valueOf(accessPermissions.canFillInForm()), root);
					PBCreateNodeHelper.addNotEmptyNode("documentAssemblyAllowed", String.valueOf(accessPermissions.canAssembleDocument()), root);
					PBCreateNodeHelper.addNotEmptyNode("extractContentAllowed", String.valueOf(accessPermissions.canExtractContent()), root);
					PBCreateNodeHelper.addNotEmptyNode("extractAccessibilityAllowed", String.valueOf(accessPermissions.canExtractForAccessibility()), root);
				}
			} catch (IOException e) {
				LOGGER.debug("PDFBox reports no matching security handle.", e);
				root.addChild("securityHandler").setValue("No security handler");
			}

			collection.addNewFeatureTree(FeatureObjectType.DOCUMENT_SECURITY, root);
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
