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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDAnnot;
import org.verapdf.model.pdlayer.PDLinkAnnot;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxPDLinkAnnot extends PBoxPDAnnot implements PDLinkAnnot {

	public static final String LINK_ANNOTATION_TYPE = "PDLinkAnnot";

	public static final String DEST = "Dest";

	public PBoxPDLinkAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour, PDPage pdPage) {
		super(annot, pageResources, document, flavour, LINK_ANNOTATION_TYPE, pdPage);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case DEST:
				return Collections.emptyList();
			default:
				return super.getLinkedObjects(link);
		}
	}

	@Override
	public String getdifferentTargetAnnotObjectKey() {
		return null;
	}

	@Override
	public String getsameTargetAnnotObjectKey() {
		return null;
	}
}
