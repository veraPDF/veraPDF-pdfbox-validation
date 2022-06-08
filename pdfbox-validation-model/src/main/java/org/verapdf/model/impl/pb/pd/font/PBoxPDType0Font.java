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

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDCIDSystemInfo;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDCIDFont;
import org.verapdf.model.pdlayer.PDCMap;
import org.verapdf.model.pdlayer.PDType0Font;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType0Font extends PBoxPDFont implements PDType0Font {

    public static final String TYPE_0_FONT_TYPE = "PDType0Font";

    public static final String DESCENDANT_FONTS = "DescendantFonts";
    public static final String ENCODING = "Encoding";

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBoxPDType0Font(PDFontLike font, RenderingMode renderingMode, PDDocument document, PDFAFlavour flavour) {
	    super(font, renderingMode, TYPE_0_FONT_TYPE);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public String getCIDFontOrdering() {
		org.apache.pdfbox.pdmodel.font.PDCIDFont descendantFont =
				((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getDescendantFont();
		if (descendantFont != null) {
			PDCIDSystemInfo cidSystemInfo = descendantFont.getCIDSystemInfo();
			return cidSystemInfo != null ? cidSystemInfo.getOrdering() : null;
		}
		return null;
	}

	@Override
	public String getCMapOrdering() {
		CMap cMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCMap();
		return cMap != null ? cMap.getOrdering() : null;
	}

	@Override
	public String getCIDFontRegistry() {
		org.apache.pdfbox.pdmodel.font.PDCIDFont descendantFont =
				((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getDescendantFont();
		if (descendantFont != null) {
			PDCIDSystemInfo cidSystemInfo = descendantFont.getCIDSystemInfo();
			return cidSystemInfo != null ? cidSystemInfo.getRegistry() : null;
		}
		return null;
	}

	@Override
	public String getCMapRegistry() {
		CMap cMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCMap();
		return cMap != null ? cMap.getRegistry() : null;
	}

	@Override
	public Long getCIDFontSupplement() {
		org.apache.pdfbox.pdmodel.font.PDCIDFont descendantFont =
				((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getDescendantFont();
		if (descendantFont != null) {
			PDCIDSystemInfo cidSystemInfo = descendantFont.getCIDSystemInfo();
			return cidSystemInfo != null ? (long)cidSystemInfo.getSupplement() : null;
		}
		return null;
	}

	@Override
	public Long getCMapSupplement() {
		CMap cMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCMap();
		return cMap != null ? (long)cMap.getSupplement() : null;
	}

	@Override
	public String getcmapName() {
		CMap cMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCMap();
		return cMap != null ? cMap.getName() : null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case DESCENDANT_FONTS:
				return this.getDescendantFonts();
			case ENCODING:
				return this.getEncoding();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<PDCIDFont> getDescendantFonts() {
        org.apache.pdfbox.pdmodel.font.PDCIDFont pdcidFont =
				((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
                .getDescendantFont();
        if (pdcidFont != null) {
			List<PDCIDFont> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxPDCIDFont(pdcidFont, this.renderingMode, this.document, this.flavour));
			return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

    private List<PDCMap> getEncoding() {
        CMap charMap = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike)
                .getCMap();
        if (charMap != null) {
			COSDictionary cosDictionary = ((org.apache.pdfbox.pdmodel.font.PDType0Font) this.pdFontLike).getCOSObject();
			COSBase cmap = cosDictionary.getDictionaryObject(COSName.ENCODING);
			List<PDCMap> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			boolean isCMapCorrect = cmap != null && cmap instanceof COSStream;
			list.add(isCMapCorrect ?
					new PBoxPDCMap(charMap, (COSStream) cmap) : new PBoxPDCMap(charMap, null));
			return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

}
