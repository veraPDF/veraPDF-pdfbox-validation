package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.cff.CFFFont;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType0;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDCIDFont;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCIDFont extends PBoxPDFont implements PDCIDFont {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDCIDFont.class);

	private final PDDocument pdDocument;
	private final PDFAFlavour flavour;

	public static final String CID_FONT_TYPE = "PDCIDFont";

	public static final String CID_SET = "CIDSet";

	public static final String IDENTITY = "Identity";
	public static final String CUSTOM = "Custom";

	public PBoxPDCIDFont(PDFontLike font, RenderingMode renderingMode, PDDocument document, PDFAFlavour flavour) {
		super(font, renderingMode, CID_FONT_TYPE);
		this.pdDocument = document;
		this.flavour = flavour;
	}

	@Override
	public String getCIDToGIDMap() {
		if (this.pdFontLike instanceof PDCIDFontType2) {
			COSBase map = ((PDCIDFontType2) this.pdFontLike).getCOSObject().getDictionaryObject(COSName.CID_TO_GID_MAP);
			if (map instanceof COSStream) {
				return CUSTOM;
			} else if (map instanceof COSName && IDENTITY.equals(((COSName) map).getName())) {
				return IDENTITY;
			}
		}
		return null;
	}

	@Override
	public Boolean getcidSetListsAllGlyphs() {
		try {
			PDStream cidSet = getCIDSetStream();
			if (cidSet != null) {
				try (InputStream stream = ((COSStream) cidSet.getCOSObject()).getUnfilteredStream()) {
					int length = cidSet.getLength();
					byte[] cidSetBytes = getCIDsFromCIDSet(stream, length);

					// reverse bit order in bit set (convert to big endian)
					BitSet bitSet = toBitSetBigEndian(cidSetBytes);

					org.apache.pdfbox.pdmodel.font.PDCIDFont cidFont = (org.apache.pdfbox.pdmodel.font.PDCIDFont) this.pdFontLike;
					for (int i = 1; i < bitSet.size(); i++) {
						if (bitSet.get(i) && !cidFont.hasGlyph(i)) {
							return Boolean.FALSE;
						}
					}
					if (!flavour.equals(PDFAFlavour.PDFA_1_A) || !flavour.equals(PDFAFlavour.PDFA_1_B)) {
						// on this levels we need to ensure that all glyphs
						// present
						// in font program are described in cid set
						if (cidFont instanceof PDCIDFontType0) {
							CFFFont cffFont = ((PDCIDFontType0) cidFont).getCFFFont();
							if (cffFont == null) {
								return Boolean.FALSE;
							}
							if (bitSet.cardinality() < cffFont.getNumCharStrings()) {
								return Boolean.FALSE;
							}
						} else if (cidFont instanceof PDCIDFontType2) {
							try (TrueTypeFont trueTypeFont = ((PDCIDFontType2) cidFont).getTrueTypeFont()) {
								if (bitSet.cardinality() < trueTypeFont.getNumberOfGlyphs()) {
									return Boolean.FALSE;
								}
							}
						}
					}
				}

			}
		} catch (IOException e) {
			LOGGER.debug("Error while parsing embedded font program. " + e.getMessage(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CID_SET.equals(link)) {
			return this.getCIDSet();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosStream> getCIDSet() {
		PDStream cidSet = getCIDSetStream();
		if (cidSet != null) {
			List<CosStream> res = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			res.add(new PBCosStream(cidSet.getStream(), this.pdDocument, this.flavour));
			return Collections.unmodifiableList(res);
		}
		return Collections.emptyList();
	}

	private PDStream getCIDSetStream() {
		PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
		PDStream cidSet;
		if (fontDescriptor != null) {
			cidSet = fontDescriptor.getCIDSet();
			return cidSet;
		}
		return null;
	}

	private static byte[] getCIDsFromCIDSet(InputStream cidSet, int length) throws IOException {
		byte[] cidSetBytes = new byte[length];
		if (cidSet.read(cidSetBytes) != length) {
			LOGGER.debug("Did not read necessary number of cid set bytes");
		}
		return cidSetBytes;
	}

	private static BitSet toBitSetBigEndian(byte[] source) {
		BitSet bitSet = new BitSet(source.length * 8);
		int i = 0;
		for (int j = 0; j < source.length; j++) {
			int b = source[j] >= 0 ? source[j] : 256 + source[j];
			for (int k = 0; k < 8; k++) {
				bitSet.set(i++, (b & 0x80) != 0);
				b = b << 1;
			}
		}

		return bitSet;
	}

}
