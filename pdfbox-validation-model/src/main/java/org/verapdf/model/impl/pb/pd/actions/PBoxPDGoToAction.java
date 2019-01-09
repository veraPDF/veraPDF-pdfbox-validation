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

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosNumber;
import org.verapdf.model.pdlayer.PDGoToAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDGoToAction extends PBoxPDAction implements PDGoToAction {

	public static final String GOTO_ACTION_TYPE = "PDGoToAction";

	public static final String D = "D";

	public PBoxPDGoToAction(PDActionGoTo simplePDObject) {
		super(simplePDObject, GOTO_ACTION_TYPE);
	}

	public PBoxPDGoToAction(PDAction simplePDObject, String type) {
		super(simplePDObject, type);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (D.equals(link)) {
			return this.getD();
		}
		return super.getLinkedObjects(link);
	}

	public List<CosNumber> getD() {
		COSDictionary cosDictionary = ((PDAction) simplePDObject).getCOSObject();
		if (cosDictionary != null) {
			COSBase dEntry = cosDictionary.getDictionaryObject(COSName.D);
			if (dEntry instanceof COSArray) {
				List<CosNumber> result = new ArrayList<>();
				for (COSBase cosBase : (COSArray) dEntry) {
					if (cosBase instanceof COSNumber) {
						result.add(PBCosNumber.fromPDFBoxNumber(cosBase));
					}
				}
				return Collections.unmodifiableList(result);
			}
		}
		return Collections.emptyList();
	}

}
