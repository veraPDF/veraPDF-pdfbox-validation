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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.cos.PBCosString;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all operators that uses one string as operand
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStringTextShow extends PBOpTextShow {

	/** Name of link to the showing strings for operators ", ', Tj */
    public static final String SHOW_STRING = "showString";

    protected PBOpStringTextShow(List<COSBase> arguments, GraphicState state,
                                 PDInheritableResources resources, final String opType, PDDocument document, PDFAFlavour flavour) {
        super(arguments, state, resources, opType, document, flavour);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (SHOW_STRING.equals(link)) {
            return this.getShowString();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosString> getShowString() {
		if (!this.arguments.isEmpty()) {
			COSBase base = this.arguments
					.get(this.arguments.size() - 1);
			if (base instanceof COSString) {
				List<CosString> string =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				string.add(new PBCosString((COSString) base));
				return Collections.unmodifiableList(string);
			}
		}
        return Collections.emptyList();
    }

}
