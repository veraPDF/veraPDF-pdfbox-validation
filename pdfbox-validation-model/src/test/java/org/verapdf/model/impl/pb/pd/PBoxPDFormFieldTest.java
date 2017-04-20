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
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDFormField;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDFormFieldTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";
	public static final String FORM_TYPE = "Btn";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDFormField.FORM_FIELD_TYPE) ? PBoxPDFormField.FORM_FIELD_TYPE : null;
		expectedID = "11 0 obj PDFormField";

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDFormField(document.getDocumentCatalog().getAcroForm().getFields().get(0));
	}

	@Test
	public void testFTMethod() {
		Assert.assertEquals(FORM_TYPE, ((PDFormField) actual).getFT());
	}

	@Test
	public void testAdditionalActionLink() {
		List<? extends Object> additionalActions = actual.getLinkedObjects(PBoxPDFormField.ADDITIONAL_ACTION);
		Assert.assertEquals(0, additionalActions.size());
		for (Object object : additionalActions) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

}
