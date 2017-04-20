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
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.pdlayer.PDSimpleFont;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDTrueTypeFontTest extends PBoxPDSimpleFontTest {

	private static final String TRUETYPE_FONT_NAME = "TT0";
	private static final String TRUETYPE_SUBTYPE = "TrueType";

	private static final Long WIDTHS_SIZE = new Long(90l);
	private static final Long FIRST_CHAR =  new Long(32l);
	private static final Long LAST_CHAR =  new Long(121l);

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDTrueTypeFont.TRUETYPE_FONT_TYPE) ? PBoxPDTrueTypeFont.TRUETYPE_FONT_TYPE : null;

		setUp(FILE_RELATIVE_PATH);
		PDTrueTypeFont trueTypeFont = (PDTrueTypeFont) document.getPage(0).getResources().getFont(COSName.getPDFName(TRUETYPE_FONT_NAME));
		actual = new PBoxPDTrueTypeFont(trueTypeFont, defaultRenderingMode);

		expectedID = trueTypeFont.getCOSObject().hashCode() + " CUQUFZ+GillSansMT";
	}

	@Override
	public void testBaseFont() {
		List<? extends Object> baseFonts = actual.getLinkedObjects(PBoxPDFont.BASE_FONT);
		Object object = baseFonts.get(0);
		Assert.assertEquals("CosUnicodeName", object.getObjectType());
		Assert.assertEquals("CUQUFZ+GillSansMT", ((CosName) object).getinternalRepresentation());
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(TRUETYPE_SUBTYPE, ((PDSimpleFont) actual).getSubtype());
	}

	@Override
	public void testWidthsSize() {
		Assert.assertEquals(WIDTHS_SIZE, ((PDSimpleFont) actual).getWidths_size());
	}

	@Override
	public void testLastChar() {
		Assert.assertEquals(LAST_CHAR, ((PDSimpleFont) actual).getLastChar());
	}

	@Override
	public void testFirstChar() {
		Assert.assertEquals(FIRST_CHAR, ((PDSimpleFont) actual).getFirstChar());
	}

	@Override
	public void testIsStandard() {
		Assert.assertFalse(((PDSimpleFont) actual).getisStandard().booleanValue());
	}

	@Override
	public void testEncoding() {
		Assert.assertEquals(((org.verapdf.model.pdlayer.PDTrueTypeFont) actual).getEncoding(), "WinAnsiEncoding");
	}

}
