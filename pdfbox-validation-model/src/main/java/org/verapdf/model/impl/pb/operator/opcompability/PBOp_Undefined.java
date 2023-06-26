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
package org.verapdf.model.impl.pb.operator.opcompability;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_Undefined;

import java.util.List;

/**
 * Any operator which not defined by PDF Reference
 *
 * @author Timur Kamalov
 */
public class PBOp_Undefined extends PBOpCompatibility implements Op_Undefined {

	/** Type name for {@code PBOp_Undefined} */
    public static final String OP_UNDEFINED_TYPE = "Op_Undefined";
    private final String name;

    public PBOp_Undefined(String operatorName, List<COSBase> arguments) {
        super(arguments, OP_UNDEFINED_TYPE);
        this.name = operatorName;
    }

    @Override
	public String getname() {
    	return name;
	}

}
