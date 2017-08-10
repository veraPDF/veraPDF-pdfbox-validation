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
package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDSignatureField;
import org.verapdf.model.pdlayer.PDAcroForm;
import org.verapdf.model.pdlayer.PDFormField;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm {

	public static final String ACRO_FORM_TYPE = "PDAcroForm";

    public static final String FORM_FIELDS = "formFields";

	private final boolean needAppearance;

	private final PDDocument document;
	private final PDFAFlavour flavour;

    public PBoxPDAcroForm(
            org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm acroForm, PDDocument document, PDFAFlavour flavour) {
        super(acroForm, ACRO_FORM_TYPE);
		this.needAppearance = acroForm.getNeedAppearances();
		this.document = document;
		this.flavour = flavour;
    }

    @Override
    public Boolean getNeedAppearances() {
        return Boolean.valueOf(this.needAppearance);
    }

	@Override
	public Boolean getcontainsXFA() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.XFA);
	}

	@Override
    public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case FORM_FIELDS:
				return this.getFormFields();
			default:
				return super.getLinkedObjects(link);
		}
    }

	private List<PDFormField> getFormFields() {
        List<PDField> fields = ((org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm) this.simplePDObject)
                .getFields();
		List<PDFormField> formFields =
				new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		for (PDField field : fields) {
			if(field instanceof PDSignatureField) {
				formFields.add(new PBoxPDSignatureField((PDSignatureField) field,
						this.document));
			} else {
				formFields.add(new PBoxPDFormField(field));
			}
        }
		return Collections.unmodifiableList(formFields);
    }
}
