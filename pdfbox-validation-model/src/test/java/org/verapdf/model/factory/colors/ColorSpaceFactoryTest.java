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
package org.verapdf.model.factory.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDJPXColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.color.ColorSpace.CS_GRAY;

/**
 * @author Evgeniy Muravitskiy
 */
public class ColorSpaceFactoryTest {

	public static final String FILE_RELATIVE_PATH = "/model/impl/pb/pd/ColorSpaces.pdf";

	private static PDResources resources;
	private static PDDocument document;

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);
		document = PDDocument.load(file, false, true);
		resources = document.getPage(0).getResources();
	}

	@Test
	public void testCalGrayGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("CalGrayCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDCalGray);
	}

	@Test
	public void testCalRGBGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("CalRGBCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDCalRGB);
	}

	@Test
	public void testDeviceCMYKGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDDeviceCMYK);
	}

	@Test
	public void testDeviceGrayGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDDeviceGray);
	}

	@Test
	public void testDeviceRGBGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDDeviceRGB);
	}

	@Test
	public void testICCBasedGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("ICCBasedCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDICCBased);
	}

	@Test
	public void testLabGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("LabCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDLab);
	}

	@Test
	public void testSeparationGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("SeparationCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDSeparation);
	}

	@Test
	public void testIndexedGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("IndexedCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDIndexed);
	}

	@Test
	public void testDeviceNGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("DeviceNCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, document, null) instanceof PDDeviceN);
	}

	@Test
	public void testTillingPatternGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("PatternCS"));
		PDAbstractPattern pattern = resources.getPattern(COSName.getPDFName("P0"));
		PDInheritableResources extRes = PDInheritableResources.getInstance(resources);
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, pattern, extRes, 0, false, document, null) instanceof PDTilingPattern);
	}

	@Test
	public void testNullGenerating() {
		Assert.assertNull(ColorSpaceFactory.getColorSpace(null, document, null));
	}

	@Test
	public void testUnsupportedColorSpaceGenerating() {
		PDColorSpace colorSpace = new PDJPXColorSpace(ColorSpace.getInstance(CS_GRAY));
		Assert.assertNull(ColorSpaceFactory.getColorSpace(colorSpace, document, null));
	}

	protected static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

	@AfterClass
	public static void tearDown() throws IOException {
		resources = null;
		document.close();
		document = null;
	}
}
