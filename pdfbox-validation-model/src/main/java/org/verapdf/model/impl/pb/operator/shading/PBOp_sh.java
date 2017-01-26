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
package org.verapdf.model.impl.pb.operator.shading;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDShading;
import org.verapdf.model.operator.Op_sh;
import org.verapdf.model.pdlayer.PDShading;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which paints the shape and color shading described
 * by a shading dictionary, subject to the current clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_sh extends PBOperator implements Op_sh {

	/** Type name for {@code PBOp_sh} */
    public static final String OP_SH_TYPE = "Op_sh";

	/** Name of link to the shading */
    public static final String SHADING = "shading";

    private org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading;

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBOp_sh(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_SH_TYPE);
        this.shading = shading;
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (SHADING.equals(link)) {
            return this.getShading();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDShading> getShading() {
        if (this.shading != null) {
			List<PDShading> list =
					new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxPDShading(this.shading, this.document, this.flavour));
			return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

}
