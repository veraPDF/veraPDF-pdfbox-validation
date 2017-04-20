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
package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.Op_BMC;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which begins a marked-content sequence
 *
 * @author Timur Kamalov
 */
public class PBOp_BMC extends PBOpMarkedContent implements Op_BMC {

	/** Type name for {@code PBOp_BMC} */
    public static final String OP_BMC_TYPE = "Op_BMC";

    public PBOp_BMC(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_BMC_TYPE, document, flavour);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case TAG:
                return this.getTag();
            case PROPERTIES:
                return this.getPropertiesDict();
            default:
                return super.getLinkedObjects(link);
        }
    }

	@Override
	protected List<CosName> getTag() {
		if (!this.arguments.isEmpty()) {
			COSBase name = this.arguments
					.get(this.arguments.size() - 1);
			if (name instanceof COSName) {
				List<CosName> list =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosName((COSName) name));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

}
