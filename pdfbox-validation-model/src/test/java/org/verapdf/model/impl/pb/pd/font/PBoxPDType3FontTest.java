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
package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDType3Font;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType3FontTest extends PBoxPDSimpleFontTest {

	private static final String TYPE3_FONT_NAME = "T3_0";
	private static final String TYPE3_SUBTYPE = "Type3";

	private static final Long WIDTHS_SIZE = new Long(2l);
	private static final Long FIRST_CHAR = new Long(97l);
	private static final Long LAST_CHAR = new Long(98l);

	private static final long CONTENT_STREAMS_SIZE = 2l;

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDType3Font.TYPE3_FONT_TYPE) ? PBoxPDType3Font.TYPE3_FONT_TYPE : null;

		setUp(FILE_RELATIVE_PATH);
		PDResources pageResources = document.getPage(0).getResources();
		org.apache.pdfbox.pdmodel.font.PDType3Font type3Font =
				(org.apache.pdfbox.pdmodel.font.PDType3Font)
						pageResources.getFont(COSName.getPDFName(TYPE3_FONT_NAME));
		PDInheritableResources resources = PDInheritableResources.getInstance(pageResources, type3Font.getResources());
		actual = new PBoxPDType3Font(type3Font, defaultRenderingMode, resources, document, null);

		expectedID = type3Font.getCOSObject().hashCode() + " null";
	}

	@Override
	public void testBaseFont() {
		List<? extends org.verapdf.model.baselayer.Object> baseFonts = actual.getLinkedObjects(PBoxPDFont.BASE_FONT);
		Assert.assertEquals(0, baseFonts.size());
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(TYPE3_SUBTYPE, ((PDType3Font) actual).getSubtype());
	}

	@Override
	public void testWidthsSize() {
		Assert.assertEquals(WIDTHS_SIZE, ((PDType3Font) actual).getWidths_size());
	}

	@Override
	public void testLastChar() {
		Assert.assertEquals(LAST_CHAR, ((PDType3Font) actual).getLastChar());
	}

	@Override
	public void testFirstChar() {
		Assert.assertEquals(FIRST_CHAR, ((PDType3Font) actual).getFirstChar());
	}

	@Override
	public void testIsStandard() {
		Assert.assertFalse(((PDType3Font) actual).getisStandard().booleanValue());
	}

	@Test
	public void testCharStrings() {
		List<? extends Object> contentStreams = ((PDType3Font) actual).getLinkedObjects(PBoxPDType3Font.CHAR_STRINGS);
		Assert.assertEquals(contentStreams.size(), CONTENT_STREAMS_SIZE);
		Assert.assertTrue(contentStreams.get(0) instanceof PBoxPDContentStream);
		Assert.assertTrue(contentStreams.get(1) instanceof PBoxPDContentStream);
	}

	@Override
	public void testEncoding() {
		Assert.assertEquals(((PDType3Font) actual).getEncoding(), PBoxPDSimpleFont.CUSTOM_ENCODING);
	}

}
