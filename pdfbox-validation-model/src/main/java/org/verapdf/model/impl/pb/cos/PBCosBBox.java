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
package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.coslayer.CosBBox;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Implementation of bounding box
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosBBox extends PBCosArray implements CosBBox {

	private static final Logger LOGGER = Logger.getLogger(PBCosBBox.class);

	public static final String COS_BBOX_TYPE = "CosBBox";
	private static final int LEFT_CORNER_POSITION = 0;
	private static final int BOTTOM_CORNER_POSITION = 1;
	private static final int RIGHT_CORNER_POSITION = 2;
	private static final int TOP_CORNER_POSITION = 3;

	/**
	 * Default constructor
	 *
	 * @param array pdfbox COSArray
	 */
	public PBCosBBox(COSArray array, PDDocument document, PDFAFlavour flavour) {
		super(array, COS_BBOX_TYPE, document, flavour);
	}

	/**
	 * @return top corner of bounding box
	 */
	@Override
	public Double gettop() {
		return this.getRequiredValue(TOP_CORNER_POSITION);
	}

	/**
	 * @return bottom corner of bounding box
	 */
	@Override
	public Double getbottom() {
		return this.getRequiredValue(BOTTOM_CORNER_POSITION);
	}

	/**
	 * @return left corner of bounding box
	 */
	@Override
	public Double getleft() {
		return this.getRequiredValue(LEFT_CORNER_POSITION);
	}

	/**
	 * @return right corner of bounding box
	 */
	@Override
	public Double getright() {
		return this.getRequiredValue(RIGHT_CORNER_POSITION);
	}

	private Double getRequiredValue(final int position) {
		COSArray array = (COSArray) this.baseObject;
		if (array.size() > position) {
			COSBase base = array.get(position);
			if (base instanceof COSNumber) {
				return Double.valueOf(((COSNumber) base).doubleValue());
			}
				LOGGER.debug("In bbox expected number but got "
						+ base.getClass().getSimpleName());
		} else {
			LOGGER.debug("Expected size of bbox array greater than"
					+ position + "but got " + array.size());
		}
		return null;
	}
}
