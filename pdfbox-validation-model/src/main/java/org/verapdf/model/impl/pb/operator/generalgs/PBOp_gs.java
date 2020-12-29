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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDExtGState;
import org.verapdf.model.operator.Op_gs;
import org.verapdf.model.pdlayer.PDExtGState;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator defining the specified parameters in the graphics state.
 *
 * @author Timur Kamalov
 */
public class PBOp_gs extends PBOpGeneralGS implements Op_gs {

	/** Type name for {@code PBOp_gs} */
    public static final String OP_GS_TYPE = "Op_gs";

	/** Name of link to the extended graphic state */
    public static final String EXT_G_STATE = "extGState";

    private PDExtendedGraphicsState extGState;

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBOp_gs(List<COSBase> arguments,
				   PDExtendedGraphicsState extGState, PDDocument document, PDFAFlavour flavour) {
        super(arguments, OP_GS_TYPE);
        this.extGState = extGState;
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (EXT_G_STATE.equals(link)) {
            return this.getExtGState();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDExtGState> getExtGState() {
        if (this.extGState != null) {
			List<PDExtGState> extGStates = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			extGStates.add(new PBoxPDExtGState(this.extGState, this.flavour));
			return Collections.unmodifiableList(extGStates);
        }
        return Collections.emptyList();
    }
}
