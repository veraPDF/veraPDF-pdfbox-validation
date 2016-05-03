package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStream extends PBoxPDObject implements
        PDContentStream {

    private static final Logger LOGGER = Logger
            .getLogger(PBoxPDContentStream.class);

	public static final String CONTENT_STREAM_TYPE = "PDContentStream";

	public static final String OPERATORS = "operators";

	private final PDInheritableResources resources;
	private List<Operator> operators = null;
	private boolean containsTransparency = false;

    private final PDDocument document;
    private final PDFAFlavour flavour;

	public PBoxPDContentStream(
			org.apache.pdfbox.contentstream.PDContentStream contentStream,
			PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(contentStream, CONTENT_STREAM_TYPE);
		this.resources = resources;
        this.document = document;
        this.flavour = flavour;
	}

	@Override
	public Boolean gethasExplicitResources() {
		COSStream stream = this.contentStream.getContentStream();
		COSBase resources = stream.getDictionaryObject(COSName.RESOURCES);
		return resources != null;
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (OPERATORS.equals(link)) {
            return this.getOperators();
        }
        return super.getLinkedObjects(link);
    }

    private List<Operator> getOperators() {
		if (this.operators == null) {
			parseOperators();
		}
		return this.operators;
    }

	/**
	 * @return true if this content stream contains transparency
	 */
	public boolean isContainsTransparency() {
		if (this.operators == null) {
			parseOperators();
		}
		return containsTransparency;
	}

	private void parseOperators() {
		try {
			COSStream cStream = this.contentStream.getContentStream();
			if (cStream != null) {
				PDFStreamParser streamParser = new PDFStreamParser(
						cStream, true);
				streamParser.parse();
				OperatorFactory operatorFactory = new OperatorFactory();
				List<Operator> result = operatorFactory.operatorsFromTokens(
						streamParser.getTokens(),
						this.resources, this.document, this.flavour);

				this.containsTransparency = operatorFactory.isLastParsedContainsTransparency();
				this.operators = Collections.unmodifiableList(result);
			}
		} catch (IOException e) {
			LOGGER.error(
					"Error while parsing content stream. " + e.getMessage(), e);
			this.operators = Collections.emptyList();
		}
	}
}
