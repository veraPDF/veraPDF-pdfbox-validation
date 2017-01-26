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
package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparationTest extends PBoxPDColorSpaceTest {

	private static final String COLOR_SPACE_NAME = "SeparationCS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDSeparation.SEPARATION_TYPE) ? PBoxPDSeparation.SEPARATION_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDColorSpace separation = document.getPage(0).getResources().getColorSpace(COSName.getPDFName(COLOR_SPACE_NAME));
		actual = new PBoxPDSeparation((PDSeparation) separation, document, null);
	}

	@Test
	public void testNumberOfComponentsMethod() {
		super.testNumberOfComponentsMethod(1);
	}

	@Test
	public void testAlternateLink() {
		List<? extends Object> alternate = actual.getLinkedObjects(PBoxPDSeparation.ALTERNATE);
		Assert.assertEquals(1, alternate.size());
		for (Object object : alternate) {
			Assert.assertTrue(object instanceof org.verapdf.model.pdlayer.PDColorSpace);
		}
	}
}
