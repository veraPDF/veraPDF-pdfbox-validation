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
package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.operator.Op_ID;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator representing inline image. Described by
 * BI and ID
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_ID extends PBOpInlineImage implements Op_ID {

	private final PDDocument document;
	private final PDFAFlavour flavour;

	/** Type name for {@code PBOp_ID} operator */
	public static final String OP_ID_TYPE = "Op_ID";

	/** Name of link to the inline image dictionary */
	public static final String INLINE_IMAGE_DICTIONARY =
			"inlineImageDictionary";

	public PBOp_ID(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
		super(arguments, OP_ID_TYPE);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		if (INLINE_IMAGE_DICTIONARY.equals(link)) {
			return this.getInlineImageDictionary();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosDict> getInlineImageDictionary() {
		if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments
					.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosDict((COSDictionary) dict, this.document, this.flavour));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}
}
