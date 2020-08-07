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

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionRemoteGoTo;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionRendition;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PDF action object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAction extends PBoxPDObject implements PDAction {

	public static final String ACTION_TYPE = "PDAction";

	public static final String NEXT = "Next";

    public PBoxPDAction(
            org.apache.pdfbox.pdmodel.interactive.action.PDAction simplePDObject) {
        this(simplePDObject, ACTION_TYPE);
    }

	public PBoxPDAction(
			org.apache.pdfbox.pdmodel.interactive.action.PDAction simplePDObject,
			final String type) {
		super(simplePDObject, type);
	}

    @Override
    public String getS() {
        return ((org.apache.pdfbox.pdmodel.interactive.action.PDAction) simplePDObject)
                .getSubType();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (NEXT.equals(link)) {
            return this.getNext();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDAction> getNext() {
        List<org.apache.pdfbox.pdmodel.interactive.action.PDAction> nextActionList =
				((org.apache.pdfbox.pdmodel.interactive.action.PDAction) this.simplePDObject)
                .getNext();
        if (nextActionList != null) {
			List<PDAction> actions = new ArrayList<>(nextActionList.size());
			for (org.apache.pdfbox.pdmodel.interactive.action.PDAction action : nextActionList) {
				PDAction result = getAction(action);
				if (result != null) {
					actions.add(result);
				}
			}
			return Collections.unmodifiableList(actions);
        }
        return Collections.emptyList();
    }

	public static PDAction getAction(org.apache.pdfbox.pdmodel.interactive.action.PDAction action) {
		if (action == null) {
			return null;
		}

		switch (action.getSubType()) {
			case "Named":
				return new PBoxPDNamedAction((PDActionNamed) action);
			case "GoTo":
				return new PBoxPDGoToAction((PDActionGoTo) action);
			case "GoToR":
				return new PBoxPDGoToRemoteAction((PDActionRemoteGoTo) action);
			case "Rendition":
				return new PBoxPDRenditionAction((PDActionRendition) action);
			default:
				return new PBoxPDAction(action);
		}
	}
}
