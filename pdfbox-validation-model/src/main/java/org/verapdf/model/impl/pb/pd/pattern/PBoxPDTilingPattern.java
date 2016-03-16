package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDTilingPattern;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDTilingPattern extends PBoxPDPattern implements
        PDTilingPattern {

    public static final String TILING_PATTERN_TYPE = "PDTilingPattern";

    public static final String CONTENT_STREAM = "contentStream";
	private final PDInheritableResources resources;

    private final PDDocument document;
    private final PDFAFlavour flavour;

	public PBoxPDTilingPattern(
			org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern simplePDObject,
			PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, TILING_PATTERN_TYPE);
		this.resources = resources;
        this.document = document;
        this.flavour = flavour;
	}

	@Override
    public List<? extends Object> getLinkedObjects(String link) {

        if (CONTENT_STREAM.equals(link)) {
            return this.getContentStream();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDContentStream> getContentStream() {
        List<PDContentStream> contentStreams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        contentStreams.add(new PBoxPDContentStream(
				(org.apache.pdfbox.contentstream.PDContentStream) this.simplePDObject, this.resources, this.document, this.flavour));
        return contentStreams;
    }
}
