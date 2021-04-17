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
package org.verapdf.model.impl.pb.pd.actions;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionRendition;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDMediaClip;
import org.verapdf.model.pdlayer.PDRenditionAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDRenditionAction extends PBoxPDAction implements PDRenditionAction {

    public static final String RENDITION_ACTION_TYPE = "PDRenditionAction";

    public static final String C = "C";

    public PBoxPDRenditionAction(PDActionRendition simplePDObject) {
        super(simplePDObject, RENDITION_ACTION_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case C:
                return this.getC();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<PDMediaClip> getC() {
        COSBase object = ((PDAction) this.simplePDObject).getCOSObject().getDictionaryObject(COSName.R);
        if (object != null) {
            object = ((COSDictionary) object).getDictionaryObject(COSName.C);
            if (object != null) {
                List<PDMediaClip> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                list.add(new PBoxPDMediaClip((COSDictionary)object));
                return Collections.unmodifiableList(list);
            }
        }
        return Collections.emptyList();
    }


}
