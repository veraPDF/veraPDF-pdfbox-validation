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

/**
 * @author Maxim Plushchov
 */
public class PBoxPDWidgetAdditionalActions extends PBoxPDAnnotationAdditionalActions {

    private static final String WIDGET_ANNOT_PARENT_TYPE = "WidgetAnnot";

    public PBoxPDWidgetAdditionalActions(COSObjectable additionalActions) {
        super(additionalActions);
    }

    @Override
    public Boolean getcontainsOtherActions() {
        return false;
    }

    @Override
    public String getparentType() {
        return WIDGET_ANNOT_PARENT_TYPE;
    }
}
