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

import org.apache.pdfbox.pdmodel.font.encoding.DictionaryEncoding;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.pdlayer.PDTrueTypeFont;

import java.util.Map;

/**
 * @author Timur Kamalov
 */
public class PBoxPDTrueTypeFont extends PBoxPDSimpleFont implements PDTrueTypeFont {

	public static final String TRUETYPE_FONT_TYPE = "PDTrueTypeFont";

	public PBoxPDTrueTypeFont(org.apache.pdfbox.pdmodel.font.PDTrueTypeFont font, RenderingMode renderingMode) {
		super(font, renderingMode, TRUETYPE_FONT_TYPE);
	}

	//% true if all glyph names in the differences array of the Encoding dictionary are a part of the Adobe Glyph List
	//% and the embedded font program contains the Microsoft Unicode (3,1 - Platform ID=3, Encoding ID=1) cmap subtable
	@Override
	public Boolean getdifferencesAreUnicodeCompliant() {
		Encoding encoding = ((org.apache.pdfbox.pdmodel.font.PDTrueTypeFont) this.pdFontLike).getEncoding();

		if (encoding != null && encoding instanceof DictionaryEncoding) {
			GlyphList glyphList = GlyphList.getAdobeGlyphList();

			Map<Integer, String> differences = ((DictionaryEncoding) encoding).getDifferences();
			if (differences == null || differences.isEmpty()) {
				return Boolean.TRUE;
			}

			for (Map.Entry<Integer, String> entry : differences.entrySet()) {
				if (!glyphList.containsGlyphName(entry.getValue()).booleanValue()) {
					return Boolean.FALSE;
				}
			}

			if (((org.apache.pdfbox.pdmodel.font.PDTrueTypeFont) this.pdFontLike).getCmapWinUnicode() == null) {
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.FALSE;
	}

}
