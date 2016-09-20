package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType3CharProc;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDType3Font;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.*;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType3Font extends PBoxPDSimpleFont implements PDType3Font {

	public static final String TYPE3_FONT_TYPE = "PDType3Font";

	public static final String CHAR_STRINGS = "charStrings";

	private final PDInheritableResources resources;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	private Map<String, PDContentStream> charStrings = null;

	public PBoxPDType3Font(PDFontLike font, RenderingMode renderingMode, PDInheritableResources resources,
			PDDocument document, PDFAFlavour flavour) {
		super(font, renderingMode, TYPE3_FONT_TYPE);
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.FALSE;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CHAR_STRINGS.equals(link)) {
			return this.getCharStrings();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDContentStream> getCharStrings() {
		if (this.charStrings == null) {
			parseCharStrings();
		}
		return new ArrayList<>(this.charStrings.values());
	}

	public Map<String, PDContentStream> getCharProcStreams() {
		if (this.charStrings == null) {
			parseCharStrings();
		}
		return Collections.unmodifiableMap(this.charStrings);
	}

	public Encoding getEncodingObject() {
		return (this.pdFontLike instanceof PDSimpleFont) ? ((PDSimpleFont) this.pdFontLike).getEncoding() : null;
	}

	private void parseCharStrings() {
		COSDictionary charProcDict = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike).getCharProcs();
		if (charProcDict != null) {
			Set<COSName> keySet = charProcDict.keySet();
			Map<String, PDContentStream> map = new HashMap<>(keySet.size());
			for (COSName cosName : keySet) {
				PDType3CharProc charProc = ((org.apache.pdfbox.pdmodel.font.PDType3Font) this.pdFontLike)
						.getCharProc(cosName);
				PBoxPDContentStream pdContentStream = new PBoxPDContentStream(charProc, this.resources, this.document,
						this.flavour);
				map.put(cosName.getName(), pdContentStream);
			}
			this.charStrings = Collections.unmodifiableMap(map);
		} else {
			this.charStrings = Collections.emptyMap();
		}
	}
}
