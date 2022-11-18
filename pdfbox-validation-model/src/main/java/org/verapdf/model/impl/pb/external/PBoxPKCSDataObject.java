/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.external;

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.external.PKCSDataObject;
import org.verapdf.pdfa.parsers.pkcs7.PKCS7;
import org.verapdf.pdfa.parsers.pkcs7.X509CertificateImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPKCSDataObject extends PBoxExternal implements PKCSDataObject {

	private static final Logger LOGGER = Logger.getLogger(PBoxPKCSDataObject.class.getCanonicalName());

	/** Type name for {@code PBoxPKCSDataObject} */
	public static final String PKCS_DATA_OBJECT_TYPE = "PKCSDataObject";

	protected PKCS7 pkcs7;

	/**
	 * @param pkcsData {@link COSString} containing encoded PKCS#7 object.
	 */
	public PBoxPKCSDataObject(COSString pkcsData) {
		super(PKCS_DATA_OBJECT_TYPE);
		try {
			pkcs7 = new PKCS7(pkcsData.getBytes());
		} catch (IOException e) {    //TODO: what do we do if some problem happens here?
			LOGGER.log(java.util.logging.Level.INFO, "Passed PKCS7 object can't be read. " + e.getMessage());
			pkcs7 = getEmptyPKCS7();
		}
	}

	/**
	 * @return amount of SignerInfo entries in PKCS#7 object.
	 */
	@Override
	public Long getSignerInfoCount() {
		return (long) pkcs7.getSignerInfosLength();
	}

	/**
	 * @return true if at least one certificate is contained in PKCS#7 object
	 * and all present certificates are not nulls.
	 */
	@Override
	public Boolean getsigningCertificatePresent() {
		List<X509CertificateImpl> certificates = pkcs7.getCertificates();
		if (certificates.isEmpty()) {
			return Boolean.FALSE;
		}
		for (X509CertificateImpl cert : certificates) {
			if (cert == null) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	private static PKCS7 getEmptyPKCS7() {
		return new PKCS7(new ArrayList<>());
	}

}
