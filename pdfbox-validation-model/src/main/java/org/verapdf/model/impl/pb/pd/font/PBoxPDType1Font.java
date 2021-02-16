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

import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.cff.CFFFont;
import org.apache.fontbox.type1.Type1Font;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.pdlayer.PDType1Font;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1Font extends PBoxPDSimpleFont implements PDType1Font {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDType1Font.class);

	public static final String UNDEFINED_GLYPH = ".notdef";
	private final PDFAFlavour flavour;

	public static final String TYPE1_FONT_TYPE = "PDType1Font";

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1Font font, RenderingMode renderingMode, PDFAFlavour flavour) {
		super(font, renderingMode, TYPE1_FONT_TYPE);
		this.flavour = flavour;
	}

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1CFont font, RenderingMode renderingMode, PDFAFlavour flavour) {
		super(font, renderingMode, TYPE1_FONT_TYPE);
		this.flavour = flavour;
	}

	@Override
	public Boolean getcharSetListsAllGlyphs() {
		try {
			PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
			if (fontDescriptor != null) {
				String charSet = fontDescriptor.getCharSet();
				if (charSet != null) {
					String[] splittedCharSet = fontDescriptor.getCharSet().split("/");
					// TODO : Log warning if charset doesn't start with '/'

					FontBoxFont font = ((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).getFontBoxFont();
					for (int i = 1; i < splittedCharSet.length; i++) {
						if (!font.hasGlyph(splittedCharSet[i])) {
							return Boolean.FALSE;
						}
					}
					if (flavour.getPart() != PDFAFlavour.Specification.ISO_19005_1) {
						if (font instanceof Type1Font) {
							if (((Type1Font) font).getCharStringsDict().size() != splittedCharSet.length) {
								return Boolean.FALSE;
							}
						} else if (font instanceof CFFFont) {
							if (((CFFFont) font).getNumCharStrings() != splittedCharSet.length) {
								return Boolean.FALSE;
							}
						}
					}

					// Do not check .undef glyph presence in font file, though it's required by ISO-32000
//					if (!font.hasGlyph(UNDEFINED_GLYPH)) {
//						return Boolean.FALSE;
//					}
					return Boolean.TRUE;
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Error while parsing embedded font program. " + e.getMessage(), e);
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
				.isStandard14());
	}

	@Override
	public String getCharSet() {
		PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
		return fontDescriptor != null ? fontDescriptor.getCharSet() : null;
	}

}
