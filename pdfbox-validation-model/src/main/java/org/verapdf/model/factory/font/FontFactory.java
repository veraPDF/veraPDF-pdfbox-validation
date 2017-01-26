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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.impl.pb.pd.font.PBoxPDTrueTypeFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType0Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType1Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Font factory for transforming Apache PDFBox
 * font representation to VeraPDF fonts
 *
 * @author Timur Kamalov
 */
public final class FontFactory {

	/** Type name for {@code Type0} font */
    public static final String TYPE_0 = "Type0";
	/** Type name for {@code Type1} font */
    public static final String TYPE_1 = "Type1";
	/** Type name for {@code MMType1} font */
    public static final String MM_TYPE_1 = "MMType1";
	/** Type name for {@code Type3} font */
    public static final String TYPE_3 = "Type3";
	/** Type name for {@code TrueType} font */
    public static final String TRUE_TYPE = "TrueType";
	/** Type name for {@code CIDFontType2} font */
    public static final String CID_FONT_TYPE_2 = "CIDFontType2";


    private FontFactory() {
        // Disable default constructor
    }

	/**
	 * Transform Apache PDFBox font representation to
	 * VeraPDF font representation
	 *
	 * @param pdfboxFont Apache PDFBox font representation
	 * @return VeraPDF font representation
	 */
	public static PDFont parseFont(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont, RenderingMode renderingMode,
			PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		if (pdfboxFont == null) {
			return null;
		}
		switch (pdfboxFont.getSubType()) {
			case TYPE_0:
				return new PBoxPDType0Font(pdfboxFont, renderingMode, document, flavour);
			case TYPE_1:
			case MM_TYPE_1:
				if (pdfboxFont instanceof PDType1Font) {
					return new PBoxPDType1Font((PDType1Font) pdfboxFont, renderingMode);
				} else if (pdfboxFont instanceof PDType1CFont) {
					return new PBoxPDType1Font((PDType1CFont) pdfboxFont, renderingMode);
				}
			case TYPE_3: {
				PDResources fontResources = ((PDType3Font) pdfboxFont).getResources();
				PDInheritableResources pdResources = resources.getExtendedResources(fontResources);
				return new PBoxPDType3Font(pdfboxFont, renderingMode, pdResources, document, flavour);
			}
			case TRUE_TYPE:
				return new PBoxPDTrueTypeFont((PDTrueTypeFont) pdfboxFont, renderingMode);
			default:
				return null;
		}
	}


	// for testing purposes
	public static PDFont parseFont(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont, PDDocument document, PDFAFlavour flavour) {
		return parseFont(pdfboxFont, RenderingMode.FILL, PDInheritableResources.EMPTY_EXTENDED_RESOURCES, document, flavour);
	}

}
