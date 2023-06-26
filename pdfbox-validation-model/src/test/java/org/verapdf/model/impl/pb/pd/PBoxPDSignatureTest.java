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
package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDSigRef;
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDSignature;
import org.verapdf.model.pdlayer.PDSignature;

/**
 * @author Maxime Campy
 */
public class PBoxPDSignatureTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/documentTest.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDSignature.SIGNATURE_TYPE) ? PBoxPDSignature.SIGNATURE_TYPE : null;
		expectedID = "25 0 obj PDSignature";

		setUp(FILE_RELATIVE_PATH);
		final org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField signatureField =
				document.getSignatureFields().get(0);
		final org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature signature =
				signatureField.getSignature();
		final COSObject signatureReference = (COSObject) signatureField.getCOSObject().getItem(COSName.V);
		actual = new PBoxPDSignature(signature, document, signatureReference);
	}

	@Test
	public void testReferenceLink() {
		List<? extends Object> reference = ((PDSignature) actual).getLinkedObjects(PBoxPDSignature.REFERENCE);
		Assert.assertEquals(1, reference.size());
		for (Object object : reference) {
			Assert.assertEquals(PBoxPDSigRef.SIGNATURE_REFERENCE_TYPE, object.getObjectType());
		}
	}
}
