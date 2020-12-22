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
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPD3DStream;
import org.verapdf.model.impl.pb.pd.PBoxPDAnnot;
import org.verapdf.model.pdlayer.PD3DAnnot;
import org.verapdf.model.pdlayer.PD3DStream;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxPD3DAnnot extends PBoxPDAnnot implements PD3DAnnot {

	public static final String ANNOTATION_3D_TYPE = "PD3DAnnot";

	public static final String stream3D = "stream3D";

	public static final COSName key3DRef = COSName.getPDFName("3DRef");
	public static final COSName key3DD = COSName.getPDFName("3DD");
	public static final COSName key3D = COSName.getPDFName("3D");

	public PBoxPD3DAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour) {
		super(annot, pageResources, document, flavour, ANNOTATION_3D_TYPE);
	}

	private List<PD3DStream> get3DStream() {
		COSStream stream = null;
		COSBase object = ((COSDictionary)simplePDObject.getCOSObject()).getDictionaryObject(key3DD);
			if (object != null && object instanceof COSDictionary) {
				COSName type = ((COSDictionary) object).getCOSName(COSName.TYPE);
				if (key3DRef.equals(type)) {
					object = ((COSDictionary) object).getDictionaryObject(key3DD);
				}
			}
			if (object != null && object instanceof COSStream) {
				COSName type = ((COSStream)object).getCOSName(COSName.TYPE);
				if (key3D.equals(type)) {
					stream = (COSStream)object;
				}
			}
		if (stream != null) {
			List<PD3DStream> streams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			streams.add(new PBoxPD3DStream(stream));
			return streams;
		}
		return Collections.emptyList();
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case stream3D:
				return this.get3DStream();
			default:
				return super.getLinkedObjects(link);
		}
	}

}
