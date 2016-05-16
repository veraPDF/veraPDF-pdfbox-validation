package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDFormField;
import org.verapdf.model.pdlayer.PDSignature;
import org.verapdf.model.pdlayer.PDSignatureField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSignatureField extends PBoxPDFormField implements PDSignatureField {

	/** Type name for {@code PBoxPDSignatureField} */
	public static final String SIGNATURE_FIELD_TYPE = "PDSignatureField";

	public static final String SIGNATURE_DICTIONARY = "V";

	/**
	 * @param pdSignatureField {@link org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField}
	 * object.
	 * @param contentStream stream with initial PDF file.
	 * @param document {@link PDDocument} containing representation of initial PDF file.
	 *
	 */
	public PBoxPDSignatureField(org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField pdSignatureField,
								PDContentStream contentStream, PDDocument document) {
		super(pdSignatureField, SIGNATURE_FIELD_TYPE);
		this.contentStream = contentStream;
		this.document = document;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case SIGNATURE_DICTIONARY:
				return getSignatureDictionary();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDSignature> getSignatureDictionary() {
		org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature signature =
				((org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField) this.simplePDObject).getSignature();
		if (signature != null) {
			List<PDSignature> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxPDSignature(signature, contentStream, document));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
