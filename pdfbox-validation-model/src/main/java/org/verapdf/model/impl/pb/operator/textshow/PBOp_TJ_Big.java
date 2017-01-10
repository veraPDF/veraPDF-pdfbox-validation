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
package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.operator.Op_TJ_Big;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which shows one or more text strings,
 * allowing individual glyph positioning
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TJ_Big extends PBOpTextShow implements Op_TJ_Big {

	/** Type name for {@code PBOp_TJ_Big} */
	public static final String OP_TJ_BIG_TYPE = "Op_TJ_Big";

	/** Name of link to the set of strings and numbers */
    public static final String SPECIAL_STRINGS = "specialStrings";

    public PBOp_TJ_Big(List<COSBase> arguments, GraphicState state,
                       PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
        super(arguments, state, resources, OP_TJ_BIG_TYPE, document, flavour);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (SPECIAL_STRINGS.equals(link)) {
            return this.getSpecialStrings();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosArray> getSpecialStrings() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments.get(
					this.arguments.size() - 1);
			if (base instanceof COSArray) {
				List<CosArray> array =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				array.add(new PBCosArray((COSArray) base, this.document, this.flavour));
				return Collections.unmodifiableList(array);
			}
		}
        return Collections.emptyList();
    }
}
