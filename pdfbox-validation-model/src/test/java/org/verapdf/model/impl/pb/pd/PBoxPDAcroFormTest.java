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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDAcroForm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroFormTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDAcroForm.ACRO_FORM_TYPE) ? PBoxPDAcroForm.ACRO_FORM_TYPE : null;
		expectedID = "2 0 obj PDAcroForm";

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDAcroForm(document.getDocumentCatalog().getAcroForm(), document, null);
	}

	@Test
	public void testNeedAppearancesMethod() {
		Assert.assertFalse(((PDAcroForm) actual).getNeedAppearances().booleanValue());
	}

	@Test
	public void testFormFieldLink() {
		List<? extends Object> formFields = actual.getLinkedObjects(PBoxPDAcroForm.FORM_FIELDS);
		Assert.assertEquals(1, formFields.size());
		for (Object object : formFields) {
			Assert.assertEquals(PBoxPDFormField.FORM_FIELD_TYPE, object.getObjectType());
		}
	}

}
