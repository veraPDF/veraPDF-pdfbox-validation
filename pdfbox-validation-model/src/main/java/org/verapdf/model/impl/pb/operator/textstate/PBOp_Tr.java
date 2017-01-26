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
package org.verapdf.model.impl.pb.operator.textstate;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.operator.Op_Tr;

import java.util.List;

/**
 * Operator defining the text rendering mode
 *
 * @author Timur Kamalov
 */
public class PBOp_Tr extends PBOpTextState implements Op_Tr {

	/** Type name for {@code PBOp_Tr} */
    public static final String OP_TR_TYPE = "Op_Tr";

    public PBOp_Tr(List<COSBase> arguments) {
        super(arguments, OP_TR_TYPE);
    }

	/**
	 * @return rendering mode
	 */
    @Override
    public Long getrenderingMode() {
        if (!this.arguments.isEmpty()) {
            COSBase renderingMode = this.arguments.get(0);
            if (renderingMode instanceof COSInteger) {
                return Long.valueOf(((COSInteger) renderingMode).longValue());
            }
        }
        return null;
    }
}
