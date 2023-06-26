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
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDSignatureField;
import org.verapdf.model.pdlayer.PDAcroForm;
import org.verapdf.model.pdlayer.PDFormField;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.verapdf.xmp.impl.ByteBuffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PDF interactive form
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroForm extends PBoxPDObject implements PDAcroForm {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDAcroForm.class.getCanonicalName());

	public static final String ACRO_FORM_TYPE = "PDAcroForm";

    public static final String FORM_FIELDS = "formFields";

    public static final String XDP = "xdp:xdp";
    public static final String CONFIG = "config";
    public static final String ACROBAT = "acrobat";
    public static final String ACROBAT7 = "acrobat7";
    public static final String DYNAMIC_RENDER = "dynamicRender";

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
    public String getdynamicRender() {
        COSBase object = ((COSDictionary) this.simplePDObject.getCOSObject()).getItem(COSName.XFA);
        if (object == null) {
            return null;
        }
        if (object instanceof COSArray) {
            COSArray array = (COSArray) object;
            COSBase afterConfig = null;
            for (int i = 0; i < array.size() - 1; i++) {
                COSBase element = array.get(i);
                if (element instanceof COSString && CONFIG.equals(((COSString) element).getString())) {
                    afterConfig = ((COSObject) array.get(i + 1)).getObject();
                    break;
                }
            }
            object = afterConfig;
        }
        if (object instanceof COSStream) {
            try (InputStream asInputStream = ((COSStream) object).getUnfilteredStream()) {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                builder.setErrorHandler(null);
                Document doc = builder.parse(new InputSource(new ByteBuffer(asInputStream).getByteStream()));
                Node configParent = getProperty(doc, XDP);
                if (configParent == null) {
                    configParent = doc;
                }
                Node config = getProperty(configParent, CONFIG);
                Node acrobat = getProperty(config, ACROBAT);
                Node acrobat7 = getProperty(acrobat, ACROBAT7);
                Node dynamicRender = getProperty(acrobat7, DYNAMIC_RENDER);
                if (dynamicRender != null) {
                    return dynamicRender.getChildNodes().item(0).getNodeValue();
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Problems with parsing XFA");
                return null;
            }
        }
        return null;
    }

    private Node getProperty(Node parent, String propertyName) {
        if (parent == null) {
            return null;
        }
        NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (propertyName.equals(item.getNodeName())) {
                return item;
            }
        }
        return null;
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
			if (field instanceof PDSignatureField) {
				formFields.add(new PBoxPDSignatureField((PDSignatureField) field,
						this.document));
			} else {
				formFields.add(new PBoxPDFormField(field));
			}
        }
		return Collections.unmodifiableList(formFields);
    }
}
