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
package org.verapdf.model.impl.pb.operator.textposition;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_TD_Big;

import java.util.List;

/**
 * Operator which moves to the start of the next line, offset
 * from the start of the current line by (tx , ty ).
 * As a side effect, this operator sets the leading parameter
 * in the text state
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TD_Big extends PBOp_General_Td implements Op_TD_Big {

	/** Type name for {@code PBOp_TD_Big} */
    public static final String OP_TD_BIG_TYPE = "Op_TD_Big";

    public PBOp_TD_Big(List<COSBase> arguments) {
        super(arguments, OP_TD_BIG_TYPE);
    }
}
