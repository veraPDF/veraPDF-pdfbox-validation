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
				if (!glyphList.containsGlyphName(entry.getValue())) {
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
