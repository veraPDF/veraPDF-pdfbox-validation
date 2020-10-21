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
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDXImage;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImageTest extends PBoxPDAbstractXObjectTest {

	private static final String IMAGE_NAME = "Im0";
	private static final String SUBTYPE = "Image";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDXImage.X_IMAGE_TYPE) ? PBoxPDXImage.X_IMAGE_TYPE : null;
		expectedID = "65 0 obj PDXImage";

		setUp(FILE_RELATIVE_PATH);
		PDXObject xObject = document.getPage(0).getResources().getXObject(COSName.getPDFName(IMAGE_NAME));
		PDInheritableResources inheritableResources =
				PDInheritableResources.getInstance(document.getPage(0).getResources());
		actual = new PBoxPDXImage((PDImageXObjectProxy) xObject, inheritableResources, document, null);
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals(SUBTYPE, ((PDXImage) actual).getSubtype());
	}

	@Test
	public void testInterpolateMethod() {
		Assert.assertFalse(((PDXImage) actual).getInterpolate().booleanValue());
	}

	@Test
	public void testRenderingIntentLink() {
		List<? extends Object> intents = actual.getLinkedObjects(PBoxPDXImage.INTENT);
		Assert.assertEquals(0, intents.size());
	}

	@Test
	public void testImageColorSpaceLink() {
		List<? extends Object> colorSpace = actual.getLinkedObjects(PBoxPDXImage.IMAGE_CS);
		Assert.assertEquals(1, colorSpace.size());
		for (Object object : colorSpace) {
			Assert.assertTrue(object instanceof PDColorSpace);
		}
	}

	@Test
	public void testAlternatesLink() {
		List<? extends Object> alternate = actual.getLinkedObjects(PBoxPDXImage.ALTERNATES);
		Assert.assertEquals(0, alternate.size());
	}

	@Test
	public void testSMaskLink() {
		List<? extends Object> sMask = actual.getLinkedObjects(PBoxPDXImage.S_MASK);
		Assert.assertEquals(0, sMask.size());
	}
}
