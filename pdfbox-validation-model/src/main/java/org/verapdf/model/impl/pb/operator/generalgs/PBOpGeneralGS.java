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
import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpGeneralGS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class of general graphic state operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpGeneralGS extends PBOperator implements OpGeneralGS {

    protected PBOpGeneralGS(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

	protected List<CosInteger> getLastInteger() {
		if (!this.arguments.isEmpty()) {
			COSBase number = this.arguments
					.get(this.arguments.size() - 1);
			if (number instanceof COSInteger) {
				List<CosInteger> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosInteger((COSInteger) number));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

}
