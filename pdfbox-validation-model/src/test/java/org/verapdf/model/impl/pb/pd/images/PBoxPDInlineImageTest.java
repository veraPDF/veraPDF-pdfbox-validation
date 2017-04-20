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

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIIFilter;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImageTest extends PBoxPDXImageTest {

	@BeforeClass
	public static void setUp() throws IOException {
		expectedType = TYPES.contains(PBoxPDInlineImage.INLINE_IMAGE_TYPE) ?
				PBoxPDInlineImage.INLINE_IMAGE_TYPE : null;
		expectedID = null;

		final COSDictionary parameters = getParameters();
		PDInlineImage image = new PDInlineImage(parameters, new byte[0], new PDResources(new COSDictionary()));
		actual = new PBoxPDInlineImage(image, document, null);
	}

	private static COSDictionary getParameters() {
		COSDictionary dictionary = new COSDictionary();
		dictionary.setItem(COSName.CS, COSName.RGB);
		dictionary.setItem(COSName.F, COSName.LZW_DECODE_ABBREVIATION);
		return dictionary;
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertNull(((org.verapdf.model.pdlayer.PDInlineImage) actual).getSubtype());
	}

	@Test
	public void testFMethod() {
		List<? extends Object> actualFilters = ((PBoxPDInlineImage) actual).getLinkedObjects(PBoxPDInlineImage.F);
		Assert.assertEquals(1, actualFilters.size());
		Object filter = actualFilters.get(0);
		if (filter instanceof CosIIFilter) {
			Assert.assertEquals("LZW", ((CosIIFilter) filter).getinternalRepresentation());
		} else {
			Assert.fail();
		}
	}

	@AfterClass
	public static void tearDown() {
		expectedType = null;
		expectedID = null;
		actual = null;
	}

}
