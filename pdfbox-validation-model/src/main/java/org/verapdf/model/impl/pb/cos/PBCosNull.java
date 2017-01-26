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

import org.apache.pdfbox.cos.COSNull;
import org.verapdf.model.coslayer.CosNull;

/**
 * Current class is representation of CosNull interface of abstract model. This
 * class is analogue of COSNull in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public final class PBCosNull extends PBCosObject implements CosNull {

    /** Type name for PBCosNull */
    public static final String COS_NULL_TYPE = "CosNull";

    /**
     * PDF null object
     */
    private static CosNull NULL;

    private PBCosNull(COSNull nil) {
        super(nil, COS_NULL_TYPE);
    }

    /**
     * Method to get instance of PBCosNull object
     * @return PBCosNull object
     */
	public static CosNull getInstance() {
        return NULL == null ? NULL = new PBCosNull(COSNull.NULL) : NULL;
	}
}