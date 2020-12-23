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

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAdditionalActions;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Maxim Plushchov
 */
public class PBoxPDAdditionalActions extends PBoxPDObject implements PDAdditionalActions {

    public static final String ADDITIONAL_ACTIONS_TYPE = "PDAdditionalActions";

    public static final String ACTIONS = "Actions";

    public PBoxPDAdditionalActions(COSObjectable additionalActions) {
        super(additionalActions, ADDITIONAL_ACTIONS_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case ACTIONS:
                return getActions();
            default:
                return super.getLinkedObjects(link);
        }
    }

    protected List<PDAction> getActions() {
        return Collections.emptyList();
    }

    protected void addAction(List<PDAction> actions, org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer) {
        PDAction action = PBoxPDAction.getAction(buffer);
        if (action != null) {
            actions.add(action);
        }
    }

    @Override
    public Boolean getcontainsOtherActions() {
        COSDictionary actions = (COSDictionary)this.simplePDObject.getCOSObject();
        if (actions.size() != 0) {
            Set<COSName> names = actions.keySet();
            for (COSName name : names) {
                if (name != COSName.E && name != COSName.getPDFName("X") && name != COSName.D && name != COSName.U
                        && name != COSName.getPDFName("Fo") && name != COSName.getPDFName("Bl")) {
                    return true;
                }
            }
        }
        return false;
    }
}
