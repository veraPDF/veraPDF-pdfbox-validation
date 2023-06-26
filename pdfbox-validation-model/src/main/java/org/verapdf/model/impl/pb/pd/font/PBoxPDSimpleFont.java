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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.pdlayer.PDSimpleFont;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDSimpleFont extends PBoxPDFont implements PDSimpleFont {

	public PBoxPDSimpleFont(PDFontLike font, RenderingMode renderingMode, final String type) {
		super(font, renderingMode, type);
	}

    @Override
    public Long getWidths_size() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getWidths().size());
    }

	@Override
	public String getEncoding() {
        COSDictionary fontDict = ((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
				.getCOSObject();
        COSBase encodingDict = fontDict.getDictionaryObject(COSName.ENCODING);
        if (encodingDict == null) {
            return null;
        } else if (encodingDict instanceof COSName) {
            return ((COSName) encodingDict).getName();
        } else if (encodingDict instanceof COSDictionary) {
			return ((COSDictionary) encodingDict).getNameAsString(COSName.BASE_ENCODING);
        }
        return null;
	}

	@Override
	public Boolean getcontainsDifferences() {
		COSDictionary fontDict = ((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike).getCOSObject();
		COSBase encodingDict = fontDict.getDictionaryObject(COSName.ENCODING);
		return (encodingDict instanceof COSDictionary) && ((COSDictionary) encodingDict).getDictionaryObject(COSName.DIFFERENCES) != null;
	}

	@Override
    public Long getLastChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getCOSObject().getInt(COSName.LAST_CHAR));
    }

    @Override
    public Long getFirstChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getCOSObject().getInt(COSName.FIRST_CHAR));
    }

    @Override
    public abstract Boolean getisStandard();

}
