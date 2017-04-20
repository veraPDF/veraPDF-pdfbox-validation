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

import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;

/**
 * Current class is representation of CosName interface of abstract model. This
 * class is analogue of COSName in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosName extends PBCosObject implements CosName {

    /** Type name for PBCosName */
    public static final String COS_NAME_TYPE = "CosName";

    private final String internalRepresentation;

    /**
     * Default constructor
     * @param cosName pdfbox COSName
     */
    public PBCosName(COSName cosName) {
        this(cosName, COS_NAME_TYPE);
    }

    /**
     * Constructor for child classes
     * @param cosName pdfbox COSName
     * @param type child class type
     */
    public PBCosName(COSName cosName, final String type) {
        super(cosName, type);
        this.internalRepresentation = cosName.getName();
    }

    /**
     * Get Unicode string representation of the Name object after applying
     * escape mechanism and converting to Unicode using Utf8 encoding
     */
    @Override
    public String getinternalRepresentation() {
        return this.internalRepresentation;
    }

}
