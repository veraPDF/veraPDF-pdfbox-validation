/**
 * This file is part of veraPDF Validation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Validation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Validation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Validation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.pd.actions;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.action.PDFormFieldAdditionalActions;
import org.verapdf.model.pdlayer.PDAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxPDFieldAdditionalActions extends PBoxPDAdditionalActions {

    private static final String FORM_FIELD_PARENT_TYPE = "FormField";

    private static final int MAX_COUNT_OF_ACTIONS = 4;

    public PBoxPDFieldAdditionalActions(COSObjectable additionalActions) {
        super(additionalActions);
    }

    @Override
    protected List<PDAction> getActions() {
        List<PDAction> actions = new ArrayList<>(MAX_COUNT_OF_ACTIONS);
        PDFormFieldAdditionalActions additionalActions = (PDFormFieldAdditionalActions)simplePDObject;
        org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

        buffer = additionalActions.getC();
        this.addAction(actions, buffer);

        buffer = additionalActions.getF();
        this.addAction(actions, buffer);

        buffer = additionalActions.getK();
        this.addAction(actions, buffer);

        buffer = additionalActions.getV();
        this.addAction(actions, buffer);

        return actions;
    }

    @Override
    public String getparentType() {
        return FORM_FIELD_PARENT_TYPE;
    }
}
