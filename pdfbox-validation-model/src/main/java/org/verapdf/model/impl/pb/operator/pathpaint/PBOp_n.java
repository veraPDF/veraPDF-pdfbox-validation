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
package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.operator.Op_n;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Operator which ends the path object without filling or
 * stroking it. This operator is a “path-painting no-op,”
 * used primarily for the side effect of changing the current
 * clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_n extends PBOpPathPaint implements Op_n {

	/** Type name for {@code PBOp_n} */
	public static final String OP_N_TYPE = "Op_n";

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 */
	public PBOp_n(List<COSBase> arguments, PDDocument document, PDFAFlavour flavour) {
		super(arguments, null, null, null, null, null, OP_N_TYPE, 0, false, false, document, flavour);
	}

}
