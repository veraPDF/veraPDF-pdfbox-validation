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
package org.verapdf.model.impl.pb.pd.annotations;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.interactive.action.PDAnnotationAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.impl.pb.pd.PBoxPDAnnot;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDWidgetAdditionalActions;
import org.verapdf.model.pdlayer.PDAdditionalActions;
import org.verapdf.model.pdlayer.PDWidgetAnnot;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxPDWidgetAnnot extends PBoxPDAnnot implements PDWidgetAnnot {

	public static final String WIDGET_ANNOTATION_TYPE = "PDWidgetAnnot";

	public PBoxPDWidgetAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour, PDPage pdPage) {
		super(annot, pageResources, document, flavour, WIDGET_ANNOTATION_TYPE, pdPage);
	}

	@Override
	public String getTU() {
		if (((PDAnnotation) simplePDObject).getCOSObject().getString(COSName.T) == null) {
			COSBase parent = ((PDAnnotation) simplePDObject).getCOSObject().getDictionaryObject(COSName.PARENT);
			return parent != null ? ((COSDictionary)parent).getString(COSName.TU) : null;
		}
		return ((PDAnnotation) simplePDObject).getCOSObject().getString(COSName.TU);
	}

	@Override
	public Boolean getcontainsLbl() {
		return null;
	}

	@Override
	protected List<PDAdditionalActions> getAdditionalActions() {
		COSBase actionDictionary = ((PDAnnotation) simplePDObject).getCOSObject().getDictionaryObject(COSName.AA);
		if (actionDictionary instanceof COSDictionary && ((COSDictionary) actionDictionary).size() != 0) {
			List<PDAdditionalActions> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			PDAnnotationAdditionalActions additionalActions = new PDAnnotationAdditionalActions(
					(COSDictionary) actionDictionary);
			actions.add(new PBoxPDWidgetAdditionalActions(additionalActions));
			return Collections.unmodifiableList(actions);
		}
		return Collections.emptyList();
	}

}
