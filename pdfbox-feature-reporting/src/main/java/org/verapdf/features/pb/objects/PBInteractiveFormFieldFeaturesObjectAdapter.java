package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.verapdf.features.objects.InteractiveFormFieldFeaturesObjectAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class PBInteractiveFormFieldFeaturesObjectAdapter implements InteractiveFormFieldFeaturesObjectAdapter {

	private final PDField formField;

	public PBInteractiveFormFieldFeaturesObjectAdapter(PDField formField) {
		this.formField = formField;
	}

	@Override
	public String getFullyQualifiedName() {
		return this.formField.getFullyQualifiedName();
	}

	@Override
	public String getValue() {
		COSBase value = this.formField.getV();
		if (value instanceof COSBoolean) {
			return String.valueOf(((COSBoolean) value).getValue());
		} else if (value instanceof COSString) {
			return ((COSString) value).getString();
		} else if (value instanceof COSName) {
			return ((COSName) value).getName();
		} else if (value instanceof COSInteger) {
			return String.valueOf(((COSInteger) value).longValue());
		} else if (value instanceof COSNumber) {
			return String.valueOf(((COSNumber) value).doubleValue());
		} else if (value instanceof COSArray) {
			return "--COSArray--";
		} else if (value instanceof COSStream) {
			return "--COSStream--";
		} else if (value instanceof COSDictionary) {
			return "--COSDictionary--";
		} else {
			return null;
		}
	}

	@Override
	public List<InteractiveFormFieldFeaturesObjectAdapter> getChildren() {
		if (this.formField instanceof PDNonTerminalField) {
			List<PDField> childFormFields = ((PDNonTerminalField) this.formField).getChildren();
			if (childFormFields != null && !childFormFields.isEmpty()) {
				List<InteractiveFormFieldFeaturesObjectAdapter> res = new ArrayList<>();
				for (PDField field : childFormFields) {
					res.add(new PBInteractiveFormFieldFeaturesObjectAdapter(field));
				}
			}
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.formField != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
