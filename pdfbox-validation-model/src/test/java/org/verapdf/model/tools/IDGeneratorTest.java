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
package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType1Font;

import java.io.IOException;

/**
 * @author Evgeniy Muravitskiy
 */
public class IDGeneratorTest {

	@Test
	public void testCOSObjectIDGenerator() throws IOException {
		COSObject object = new COSObject(null);
		object.setObjectNumber(10);
		object.setGenerationNumber(0);
		String expectedID = "10 0";
		String actualID = IDGenerator.generateID(object);
		Assert.assertEquals(expectedID, actualID);
	}

	@Test
	public void testCOSDictionaryIDGenerator() {
		COSDictionary dictionary = new COSDictionary();
		String actual = IDGenerator.generateID(dictionary);
		Assert.assertNull(actual);
	}

	@Test
	public void testGlyphIDGenerator() {
		String actualID = IDGenerator.generateID(1298374, "name", 10, 3);
		String expectedID = "1298374 name 10 3";
		Assert.assertEquals(expectedID, actualID);
	}

	@Test
	public void testFontIDGenerator() throws IOException {
		COSDictionary dictionary = new COSDictionary();
		dictionary.setItem(COSName.BASE_FONT, COSName.getPDFName("Arial+MTT"));
		PDType1Font font = new PDType1Font(dictionary);
		PBoxPDType1Font actualFont = new PBoxPDType1Font(font, RenderingMode.FILL);

		String actualID = actualFont.getID();
		String expectedID = dictionary.hashCode() + " Arial+MTT";
		Assert.assertEquals(expectedID, actualID);
	}
}
