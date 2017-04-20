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

import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;

/**
 * Current class is representation of CosInteger interface of abstract model.
 * All methods described in CosNumber. This class is analogue of COSInteger in
 * pdfbox.
 *
 * @author Evgeniy Muravitskiy
 * @see PBCosNumber
 */
public class PBCosInteger extends PBCosNumber implements CosInteger {

    /** Type name for PBCosInteger */
    public static final String COS_INTEGER_TYPE = "CosInteger";

    /**
     * Default constructor
     * @param value pdfbox COSInteger
     */
    public PBCosInteger(COSInteger value) {
        super(value, COS_INTEGER_TYPE);
    }
}
