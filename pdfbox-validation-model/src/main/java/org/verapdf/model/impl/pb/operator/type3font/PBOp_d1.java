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
package org.verapdf.model.impl.pb.operator.type3font;

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.operator.Op_d1;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d1 extends PBOpType3Font implements Op_d1 {

	public static final String OP_D1_TYPE = "Op_d1";

	public static final String CONTROL_POINTS = "controlPoints";

	public PBOp_d1(List<COSBase> arguments) {
		super(arguments, OP_D1_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CONTROL_POINTS.equals(link)) {
			return this.getControlPoints();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosNumber> getControlPoints() {
		return this.getListOfNumbers();
	}
}
