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
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.pdlayer.PDSimpleFont;
import org.verapdf.model.pdlayer.PDType1Font;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1FontTest extends PBoxPDSimpleFontTest {

	private static final String TYPE1_FONT_NAME = "T1_0";
	private static final String TYPE1_SUBTYPE = "Type1";

	private static final String CHAR_SET = "/space/one/E/T/b/d/e/f/m/n/o/p/t/y";
	private static final String ENCODING = "WinAnsiEncoding";

	private static final Long WIDTHS_SIZE = new Long(90l);
	private static final Long FIRST_CHAR = new Long(32l);
	private static final Long LAST_CHAR = new Long(121l);

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDType1Font.TYPE1_FONT_TYPE) ? PBoxPDType1Font.TYPE1_FONT_TYPE : null;

		setUp(FILE_RELATIVE_PATH);
		PDType1CFont type1Font = (PDType1CFont) document.getPage(0).getResources().getFont(COSName.getPDFName(TYPE1_FONT_NAME));
		actual = new PBoxPDType1Font(type1Font, defaultRenderingMode, null);

		expectedID = type1Font.getCOSObject().hashCode() + " OLXYQW+MyriadPro-Regular";
	}

	@Override
	public void testBaseFont() {
		List<? extends Object> baseFonts = actual.getLinkedObjects(PBoxPDFont.BASE_FONT);
		Object object = baseFonts.get(0);
		Assert.assertEquals("CosUnicodeName", object.getObjectType());
		Assert.assertEquals("OLXYQW+MyriadPro-Regular", ((CosName) object).getinternalRepresentation());
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(TYPE1_SUBTYPE, ((PDSimpleFont) actual).getSubtype());
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
		Assert.assertEquals(((PDType1Font) actual).getEncoding(), ENCODING);
	}

	@Test
	public void testCharSet() {
		Assert.assertEquals(((PDType1Font) actual).getCharSet(), CHAR_SET);
	}

}
