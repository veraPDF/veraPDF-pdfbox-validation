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

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.operator.Op_j_line_join;

/**
 * Operator defining the line join style in the graphics state
 *
 * @author Timur Kamalov
 */
public class PBOp_j_line_join extends PBOpGeneralGS implements Op_j_line_join {

	/** Type name for {@code PBOp_j_line_join} */
	public static final String OP_J_LINE_JOIN_TYPE = "Op_j_line_join";

	/** Name of link to the line join for */
    public static final String LINE_JOIN = "lineJoin";

    public PBOp_j_line_join(List<COSBase> arguments) {
        super(arguments, OP_J_LINE_JOIN_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (LINE_JOIN.equals(link)) {
            return this.getLineJoin();
        }
        return super.getLinkedObjects(link);
    }

	private List<CosInteger> getLineJoin() {
		return this.getLastInteger();
	}
}
