package org.verapdf.model.impl.pb.pd.signatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDFormField;
import org.verapdf.model.pdlayer.PDSignature;
import org.verapdf.model.pdlayer.PDSignatureField;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSignatureField extends PBoxPDFormField implements PDSignatureField {

    /**
     * Type name for {@code PBoxPDSignatureField}
     */
    public static final String SIGNATURE_FIELD_TYPE = "PDSignatureField";

    public static final String SIGNATURE_DICTIONARY = "V";

    /**
     * @param pdSignatureField {@link org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField}
     *                         object.
     * @param document         {@link PDDocument} containing representation of initial PDF file.
     */
    public PBoxPDSignatureField(org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField pdSignatureField,
                                PDDocument document) {
        super(pdSignatureField, SIGNATURE_FIELD_TYPE);
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

    /**
     * Here we suppose that signature can be present in signature dictionary
     * only indirectly. If it is not then we will get ClassCastException.
     */
    private List<PDSignature> getSignatureDictionary() {
        org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature signature =
                ((org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField) this.simplePDObject).getSignature();
        if (signature != null) {
            List<PDSignature> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            COSObject signatureReference = (COSObject) ((org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField)
                    this.simplePDObject).getCOSObject().getItem(COSName.V);
            list.add(new PBoxPDSignature(signature, document, signatureReference));
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }
}
