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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDFieldAdditionalActions;
import org.verapdf.model.pdlayer.PDAdditionalActions;
import org.verapdf.model.pdlayer.PDFormField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDFormField extends PBoxPDObject implements PDFormField {

	public static final String FORM_FIELD_TYPE = "PDFormField";

    public static final String ADDITIONAL_ACTION = "AA";

    public static final String LANG = "Lang";

    public PBoxPDFormField(PDField simplePDObject) {
        super(simplePDObject, FORM_FIELD_TYPE);
    }

    protected PBoxPDFormField(PDField field, final String type) {
        super(field, type);
    }

    @Override
    public String getFT() {
        return ((PDField) this.simplePDObject).getFieldType();
    }

    @Override
    public Boolean getcontainsAA() {
        COSBase pageObject = this.simplePDObject.getCOSObject();
        return pageObject != null && pageObject instanceof COSDictionary &&
                ((COSDictionary) pageObject).containsKey(COSName.AA);
    }

    @Override
    public String getTU() {
        return null;
    }

    @Override
    public Long getFf() {
        return null;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case ADDITIONAL_ACTION:
                return this.getAdditionalAction();
            case LANG:
                return Collections.emptyList();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<PDAdditionalActions> getAdditionalAction() {
        PDFormFieldAdditionalActions pbActions = ((PDField) this.simplePDObject)
                .getActions();
        if (pbActions != null && pbActions.getCOSObject().size() != 0) {
			List<PDAdditionalActions> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            actions.add(new PBoxPDFieldAdditionalActions(pbActions));
            return Collections.unmodifiableList(actions);
        }
        return Collections.emptyList();
    }

}
