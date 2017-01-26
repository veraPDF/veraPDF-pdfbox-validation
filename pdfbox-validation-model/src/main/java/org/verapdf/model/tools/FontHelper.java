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

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.preflight.font.container.*;
import org.verapdf.model.factory.font.FontFactory;

/**
 * Class for transforming Apache PDFBox font to
 * Apache Preflight font container
 *
 * @author Timur Kamalov
 */
public class FontHelper {

	/** CID font type 0 value of Subtype entry for type 0 font */
	public static final String CID_FONT_TYPE_0 = "CIDFontType0";
	/** CID font type 1 value of Subtype entry for type 0 font */
	public static final String CID_FONT_TYPE_2 = "CIDFontType2";

	/**
	 * Transform Apache PDFBox font to Apache Preflight
	 * font container representation
	 *
	 * @param font Apache PDFBox font
	 * @return Apache Preflight font container
	 */
	public static FontContainer<? extends PDFontLike> getFontContainer(PDFont font) {
		if (font == null) {
			return null;
		}
		switch (font.getSubType()) {
			case FontFactory.TYPE_1:
			case FontFactory.MM_TYPE_1:
				return new Type1Container((PDSimpleFont) font);
			case FontFactory.TRUE_TYPE:
				return new TrueTypeContainer((PDTrueTypeFont) font);
			case FontFactory.TYPE_3:
				return new Type3Container((PDType3Font) font);
			case FontFactory.TYPE_0: {
				Type0Container container = new Type0Container(font);
				PDCIDFont pdcidFont = ((PDType0Font) font)
						.getDescendantFont();
				String cidType = pdcidFont.getCOSObject()
						.getNameAsString(COSName.SUBTYPE);
				if (CID_FONT_TYPE_0.equals(cidType)) {
					CIDType0Container type0Container =
							new CIDType0Container((PDCIDFontType0) pdcidFont);
					container.setDelegateFontContainer(type0Container);
				} else if (CID_FONT_TYPE_2.equals(cidType)) {
					CIDType2Container type2Container =
							new CIDType2Container((PDCIDFontType2) pdcidFont);
					container.setDelegateFontContainer(type2Container);
				}
				return container;
			}
			default:
				return null;
		}
	}

}
