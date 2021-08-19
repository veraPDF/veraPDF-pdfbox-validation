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
package org.verapdf.model.factory.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.PDTrueTypeFont;
import org.verapdf.model.pdlayer.PDType0Font;
import org.verapdf.model.pdlayer.PDType1Font;
import org.verapdf.model.pdlayer.PDType3Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Timur Kamalov
 */
public class FontFactoryTest {

	public static final String FILE_RELATIVE_PATH = "model/impl/pb/pd/Fonts.pdf";

	private static PDResources resources;
	private static PDDocument document;

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		document = PDDocument.load(FontFactoryTest.class.getClassLoader().getResourceAsStream(FILE_RELATIVE_PATH), false, true);
		resources = document.getPage(0).getResources();
	}

	@Test
	public void testType0CID0Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("C0_0"));
		org.verapdf.model.pdlayer.PDFont convertedFont = FontFactory.parseFont(font, document, null);
		Assert.assertTrue(convertedFont instanceof PDType0Font);
	}

	@Test
	public void testType0CID2Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("C2_0"));
		Assert.assertTrue(FontFactory.parseFont(font, document, null) instanceof PDType0Font);
	}

	@Test
	public void testType1Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("T1_0"));
		Assert.assertTrue(FontFactory.parseFont(font, document, null) instanceof PDType1Font);
	}

	@Test
	public void testType3Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("T3_0"));
		Assert.assertTrue(FontFactory.parseFont(font, document, null) instanceof PDType3Font);
	}

	@Test
	public void testTrueTypeGenerating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("TT0"));
		Assert.assertTrue(FontFactory.parseFont(font, document, null) instanceof PDTrueTypeFont);
	}

	@AfterClass
	public static void tearDown() throws IOException {
		resources = null;
		document.close();
		document = null;
	}

}
