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

import org.apache.pdfbox.cos.COSBoolean;
import org.verapdf.model.coslayer.CosBool;

/**
 * Current class is representation of CosBool interface of abstract model. This
 * class is analogue of COSBoolean in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosBool extends PBCosObject implements CosBool {

	public static final CosBool TRUE = new PBCosBool(COSBoolean.TRUE);
	public static final CosBool FALSE = new PBCosBool(COSBoolean.FALSE);

    /** Type name for PBCosBool */
    public static final String COS_BOOLEAN_TYPE = "CosBool";
    private boolean value;

    private PBCosBool(COSBoolean cosBoolean) {
        super(cosBoolean, COS_BOOLEAN_TYPE);
        this.value = cosBoolean.getValue();
    }

    /**
     * Get value of this object
     */
    @Override
    public Boolean getvalue() {
        return Boolean.valueOf(this.value);
    }

    /**
     * This method will create CosBool object instance from pdfbox COSBoolean
     * @param bool pdfbox COSBoolean
     * @return instance of CosBool
     */
	public static CosBool valueOf(COSBoolean bool) {
		return bool.getValue() ? TRUE : FALSE;
	}
}
