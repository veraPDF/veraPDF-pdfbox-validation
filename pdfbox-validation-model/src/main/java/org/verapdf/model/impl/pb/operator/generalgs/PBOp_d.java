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
package org.verapdf.model.impl.pb.operator.generalgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_d;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Operator defining the line dash pattern in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_d extends PBOpGeneralGS implements Op_d {

	/** Type name for {@code PBOp_d} */
    public static final String OP_D_TYPE = "Op_d";

	/** Name of link to the dash array */
    public static final String DASH_ARRAY = "dashArray";
	/** Name of link to the dash phase */
    public static final String DASH_PHASE = "dashPhase";

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBOp_d(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_D_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        switch (link) {
        case DASH_ARRAY:
            return this.getDashArray();
        case DASH_PHASE:
            return this.getDashPhase();
        default:
            return super.getLinkedObjects(link);
        }
    }

    private List<CosArray> getDashArray() {
        if (this.arguments.size() > 1) {
			COSBase array = this.arguments
					.get(this.arguments.size() - 2);
			if (array instanceof COSArray) {
				List<CosArray> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosArray((COSArray) array, this.document, this.flavour));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }

    private List<CosNumber> getDashPhase() {
        return this.getLastNumber();
    }

}
