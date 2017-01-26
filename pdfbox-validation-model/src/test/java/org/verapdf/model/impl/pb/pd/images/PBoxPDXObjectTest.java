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
package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXObjectTest extends PBoxPDAbstractXObjectTest {

	private static final String POST_SCRIPT_NAME = "X2";
	private static final String POST_SCRIPT_SUBTYPE = "PS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDXObject.X_OBJECT_TYPE) ? PBoxPDXObject.X_OBJECT_TYPE : null;
		expectedID = "41 0 obj PDXObject";

		setUp(FILE_RELATIVE_PATH);
		PDXObject xObject = document.getPage(0).getResources().getXObject(COSName.getPDFName(POST_SCRIPT_NAME));
		actual = new PBoxPDXObject(xObject, document, null);
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(POST_SCRIPT_SUBTYPE, ((org.verapdf.model.pdlayer.PDXObject) actual).getSubtype());
	}

}
