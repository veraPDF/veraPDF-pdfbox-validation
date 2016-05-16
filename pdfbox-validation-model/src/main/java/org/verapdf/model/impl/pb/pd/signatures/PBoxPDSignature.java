package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.log4j.Logger;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.external.PBoxPKCSDataObject;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDSignature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSignature extends PBoxPDObject implements PDSignature {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDSignature.class);

	/** Type name for {@code PBoxPDSignature} */
	public static final String SIGNATURE_TYPE = "PDSignature";

	public static final String CONTENTS = "Contents";
	public static final String REFERENCE = "Reference";

	protected static byte [] contents;

	private static final byte [] eofString = "%%EOF".getBytes();

	/**
	 * @param pdSignature {@link org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature}
	 * object.
	 * @param contentStream stream with initial PDF file.
	 * @param document {@link PDDocument} containing representation of initial PDF file.
	 */
	public PBoxPDSignature(org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature pdSignature,
						   PDContentStream contentStream, PDDocument document) {
		super(pdSignature, SIGNATURE_TYPE);
		this.contentStream = contentStream;
		this.document = document;
		try {
			this.contents = ((org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature)
					this.simplePDObject).getContents(this.contentStream.getContentStream().getUnfilteredStream());
		} catch (IOException e) {
			LOGGER.error("Can't get unfiltered stream from content stream", e);
		}
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case CONTENTS:
				return getContents();
			case REFERENCE:
				return null; //TODO: fixme
			default:
				return super.getLinkedObjects(link);
		}
	}

	/**
	 * @return true if byte range covers entire document except for Contents
	 * entry in signature dictionary
	 */
	@Override
	public Boolean getdoesByteRangeCoverEntireDocument() {	//fixme
		try {
			List<org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature> signatures
					= document.getSignatureDictionaries();

		} catch (IOException e) {
			LOGGER.error("Can't get signature dictionaries from PDDocument", e);
			return false;
		}
		return null;
	}

	private List<PBoxPKCSDataObject> getContents() {
		if(contents != null) {
			List<PBoxPKCSDataObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxPKCSDataObject(new COSString(contents)));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
