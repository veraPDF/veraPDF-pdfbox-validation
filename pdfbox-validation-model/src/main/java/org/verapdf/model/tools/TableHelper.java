/**
 * This file is part of veraPDF Validation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Validation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Validation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Validation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.impl.pb.pd.TaggedPDFConstants;

public class TableHelper {

    public static Integer getColSpan(COSDictionary simplePDObject) {
        return TableHelper.getSpanValue(simplePDObject, COSName.getPDFName("COL_SPAN"));
    }

    public static Integer getRowSpan(COSDictionary simplePDObject) {
        return TableHelper.getSpanValue(simplePDObject, COSName.getPDFName("ROW_SPAN"));
    }

    private static Integer getSpanValue(COSDictionary simplePDObject, COSName spanName) {
        Integer defaultValue = 1;
        COSBase aValue = simplePDObject.getDictionaryObject(COSName.A);
        if (aValue == null) {
            return defaultValue;
        }
        if (aValue instanceof COSArray) {
            for (COSBase object : (COSArray) aValue) {
                Integer spanValue = getSpanValue(object, spanName);
                if (spanValue != null) {
                    return spanValue;
                }
            }
        }
        Integer spanValue = getSpanValue(aValue, spanName);
        return spanValue != null ? spanValue : defaultValue;
    }

    private static Integer getSpanValue(COSBase object, COSName spanName) {
        if (object instanceof COSDictionary && TaggedPDFConstants.TABLE.equals(((COSDictionary) object).getString(COSName.O))) {
            return ((COSDictionary) object).getInt(spanName);
        }
        return null;
    }
}