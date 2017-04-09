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

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.encryption.SecurityHandler;
import org.verapdf.features.objects.DocSecurityFeaturesObjectAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features object adapter for document security
 *
 * @author Maksim Bezrukov
 */
public class PBDocSecurityFeaturesObjectAdapter implements DocSecurityFeaturesObjectAdapter {

	private static final Logger LOGGER = Logger
			.getLogger(PBDocSecurityFeaturesObjectAdapter.class);

	private PDEncryption encryption;
	private String ownerKey;
	private String userKey;
	private boolean isUserPermissionsPresent;
	private boolean isPrintAllowed;
	private boolean isPrintDegradedAllowed;
	private boolean isChangesAllowed;
	private boolean isModifyAnnotationsAllowed;
	private boolean isFillingSigningAllowed;
	private boolean isDocumentAssemblyAllowed;
	private boolean isExtractContentAllowed;
	private boolean isExtractAccessibilityAllowed;
	private List<String> errors;

	/**
	 * Constructs new Document Security Feature Object adapter
	 *
	 * @param encryption pdfbox class represents Encryption object
	 */
	public PBDocSecurityFeaturesObjectAdapter(PDEncryption encryption) {
		this.encryption = encryption;
		init();
	}

	private void init() {
		if (encryption != null) {
			this.errors = new ArrayList<>();
			try {
				this.ownerKey = new COSString(encryption.getOwnerKey()).toHexString();
			} catch (IOException e) {
				LOGGER.debug("PDFBox error getting owner key data", e);
				this.errors.add(e.getMessage());
			}

			try {
				this.userKey = new COSString(encryption.getUserKey()).toHexString();
			} catch (IOException e) {
				LOGGER.debug("PDFBox error getting user key data", e);
				this.errors.add(e.getMessage());
			}

			try {
				SecurityHandler securityHandler = encryption.getSecurityHandler();
				this.isUserPermissionsPresent = securityHandler != null;
				if (securityHandler != null) {
					AccessPermission accessPermissions = new AccessPermission(encryption.getPermissions());
					this.isPrintAllowed = accessPermissions.canPrint();
					this.isPrintDegradedAllowed = accessPermissions.canPrintDegraded();
					this.isChangesAllowed = accessPermissions.canModify();
					this.isModifyAnnotationsAllowed = accessPermissions.canModifyAnnotations();
					this.isFillingSigningAllowed = accessPermissions.canFillInForm();
					this.isDocumentAssemblyAllowed = accessPermissions.canAssembleDocument();
					this.isExtractContentAllowed = accessPermissions.canExtractContent();
					this.isExtractAccessibilityAllowed = accessPermissions.canExtractForAccessibility();
				}
			} catch (IOException e) {
				LOGGER.debug("PDFBox reports no matching security handle.", e);
				this.errors.add("PDFBox reports no matching security handle.");
			}
		}
	}

	@Override
	public String getHexEncodedOwnerKey() {
		return ownerKey;
	}

	@Override
	public String getHexEncodedUserKey() {
		return userKey;
	}

	@Override
	public boolean isUserPermissionsPresent() {
		return isUserPermissionsPresent;
	}

	@Override
	public boolean isPrintAllowed() {
		return isPrintAllowed;
	}

	@Override
	public boolean isPrintDegradedAllowed() {
		return isPrintDegradedAllowed;
	}

	@Override
	public boolean isChangesAllowed() {
		return isChangesAllowed;
	}

	@Override
	public boolean isModifyAnnotationsAllowed() {
		return isModifyAnnotationsAllowed;
	}

	@Override
	public boolean isFillingSigningAllowed() {
		return isFillingSigningAllowed;
	}

	@Override
	public boolean isDocumentAssemblyAllowed() {
		return isDocumentAssemblyAllowed;
	}

	@Override
	public boolean isExtractContentAllowed() {
		return isExtractContentAllowed;
	}

	@Override
	public boolean isExtractAccessibilityAllowed() {
		return isExtractAccessibilityAllowed;
	}

	@Override
	public List<String> getErrors() {
		return errors == null ? Collections.<String>emptyList() : Collections.unmodifiableList(errors);
	}

	@Override
	public String getFilter() {
		if (encryption != null) {
			return encryption.getFilter();
		}
		return null;
	}

	@Override
	public String getSubFilter() {
		if (encryption != null) {
			return encryption.getSubFilter();
		}
		return null;
	}

	@Override
	public int getVersion() {
		if (encryption != null) {
			return encryption.getVersion();
		}
		return 0;
	}

	@Override
	public int getLength() {
		if (encryption != null) {
			return encryption.getLength();
		}
		return 0;
	}

	@Override
	public boolean isEncryptMetadata() {
		return encryption == null || encryption.isEncryptMetaData();
	}
}
