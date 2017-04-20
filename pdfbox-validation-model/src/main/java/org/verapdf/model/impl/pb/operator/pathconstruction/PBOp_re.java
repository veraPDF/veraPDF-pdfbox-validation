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
package org.verapdf.model.impl.pb.operator.pathconstruction;

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.operator.Op_re;

/**
 * Operator which appends a rectangle to the current path
 * as a complete sub path, with lower-left corner (x, y)
 * and dimensions width and height in user space
 *
 * @author Timur Kamalov
 */
public class PBOp_re extends PBOpPathConstruction implements Op_re {

	/** Type name for {@code PBOp_re} */
    public static final String OP_RE_TYPE = "Op_re";

	/** Name of link to the rectangle box */
    public static final String RECT_BOX = "rectBox";

    public PBOp_re(List<COSBase> arguments) {
        super(arguments, OP_RE_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
       if(RECT_BOX.equals(link)) {
           return this.getRectBox();
       }
       return super.getLinkedObjects(link);
    }

    private List<CosNumber> getRectBox() {
        return this.getListOfNumbers();
    }

}
