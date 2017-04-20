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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosNumber;

/**
 * Base class for text position operators (Td and TD)
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOp_General_Td extends PBOpTextPosition {

	/** Name of link to the horizontal offset for Td and TD operators */
    public static final String HORIZONTAL_OFFSET = "horizontalOffset";

	/** Name of link to the vertical offset for Td and TD operators */
	public static final String VERTICAL_OFFSET = "verticalOffset";

    protected PBOp_General_Td(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case VERTICAL_OFFSET:
				return this.getVerticalOffset();
			case HORIZONTAL_OFFSET:
				return this.getHorizontalOffset();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<CosNumber> getHorizontalOffset() {
		if (this.arguments.size() > 1) {
			COSBase number = this.arguments
					.get(this.arguments.size() - 2);
			if (number instanceof COSNumber) {
				List<CosNumber> offset = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				offset.add(PBCosNumber.fromPDFBoxNumber(number));
				return Collections.unmodifiableList(offset);
			}
		}
        return Collections.emptyList();
    }

    private List<CosNumber> getVerticalOffset() {
        return this.getLastNumber();
    }
}
