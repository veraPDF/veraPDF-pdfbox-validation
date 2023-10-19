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

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.pb.operator.textshow.PUAHelper;

/**
 * Current class is representation of CosString interface of abstract model.
 * This class is analogue of COSString in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosString extends PBCosObject implements CosString {

    /** Type name for PBCosString */
    public static final String COS_STRING_TYPE = "CosString";
    private final String value;
    private final boolean isHex;
    private final boolean containsOnlyHex;
    private final Long hexCount;

    /**
     * Default constructor
     * @param cosString pdfbox COSString
     */
    public PBCosString(COSString cosString) {
        this(cosString, COS_STRING_TYPE);
    }

    protected PBCosString(COSString cosString, String type) {
        super(cosString, type);
        this.value = cosString.getASCII();
        this.isHex = cosString.isHex();
        this.containsOnlyHex = cosString.isContainsOnlyHex();
        this.hexCount = cosString.getHexCount();
    }

    /**
     * Get Unicode string value stored in the PDF object
     */
    @Override
    public String getvalue() {
        return this.value;
    }

    /**
     * true if the string is stored in Hex format
     */
    @Override
    public Boolean getisHex() {
        return Boolean.valueOf(this.isHex);
    }

    /**
     * true if all symbols below range 0-9,a-f,A-F
     */
    @Override
    public Boolean getcontainsOnlyHex() {
        return Boolean.valueOf(this.containsOnlyHex);
    }

    /**
     * contains original hexa string length
     */
    @Override
    public Long gethexCount() {
        return this.hexCount;
    }

    @Override
    public Boolean getcontainsPUA() {
        return PUAHelper.containPUA(((COSString)baseObject).getString());
    }
}
