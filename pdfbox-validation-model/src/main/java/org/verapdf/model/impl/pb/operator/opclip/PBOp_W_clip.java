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
package org.verapdf.model.impl.pb.operator.opclip;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.operator.Op_W_clip;

import java.util.List;

/**
 * Operator which modifies the current clipping path by intersecting
 * it with the current path, using the nonzero winding number rule
 * to determine which regions lie inside the clipping path
 *
 * @author Timur Kamalov
 */
public class PBOp_W_clip extends PBOpClip implements Op_W_clip {

	/** Type name for {@code PBOp_W_clip} */
    public static final String OP_W_CLIP_TYPE = "Op_W_clip";

    public PBOp_W_clip(List<COSBase> arguments) {
        super(arguments, OP_W_CLIP_TYPE);
    }

}
