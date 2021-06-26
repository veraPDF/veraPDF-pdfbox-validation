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
package org.verapdf.model.impl.pb.pd.pboxse;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.pd.TaggedPDFConstants;
import org.verapdf.model.selayer.SETH;
import org.verapdf.model.tools.TableHelper;
import org.verapdf.model.tools.TaggedPDFRoleMapHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PBoxSETH extends PBoxSEGeneral implements SETH {

    public static final String TH_STRUCTURE_ELEMENT_TYPE = "SETH";

    public PBoxSETH(COSDictionary structElemDictionary, TaggedPDFRoleMapHelper roleMapHelper) {
        super(structElemDictionary, roleMapHelper, TaggedPDFConstants.TH, TH_STRUCTURE_ELEMENT_TYPE);
    }

    protected String getTHID() {
        return ((COSDictionary)simplePDObject).getString(COSName.ID);
    }

    protected String getScope() {
        COSBase A = ((COSDictionary)simplePDObject).getDictionaryObject(COSName.A);
        if (A != null) {
            if (A instanceof COSArray) {
                for (COSBase object : (COSArray)A) {
                    COSBase baseObject = object instanceof COSObject ? ((COSObject)object).getObject() : object;
                    if (baseObject instanceof COSDictionary &&
                            TaggedPDFConstants.TABLE.equals(((COSDictionary)baseObject).getNameAsString(COSName.O))) {
                        String scope = ((COSDictionary)baseObject).getNameAsString("Scope");
                        if (scope != null) {
                            return scope;
                        }
                    }
                }
            } else if (A instanceof COSDictionary &&
                    TaggedPDFConstants.TABLE.equals(((COSDictionary)A).getNameAsString(COSName.O))) {
                return ((COSDictionary) A).getNameAsString("Scope");
            }
        }
        return null;
    }

    protected List<String> getHeaders() {
        COSBase A = ((COSDictionary)simplePDObject).getDictionaryObject(COSName.A);
        if (A != null) {
            if (A instanceof COSArray) {
                for (COSBase object : (COSArray)A) {
                    COSBase baseObject = object instanceof COSObject ? ((COSObject)object).getObject() : object;
                    if (baseObject instanceof COSDictionary &&
                            TaggedPDFConstants.TABLE.equals(((COSDictionary)baseObject).getNameAsString(COSName.O))) {
                        COSBase Headers = ((COSDictionary) baseObject).getDictionaryObject("Headers");
                        if (Headers != null && Headers instanceof COSArray) {
                            List<String> list = new LinkedList<>();
                            for (COSBase elem : (COSArray)Headers) {
                                if (elem instanceof COSString) {
                                    list.add(((COSString)elem).getString());
                                }
                            }
                            return list;
                        }
                    }
                }
            } else if (A instanceof COSDictionary &&
                    TaggedPDFConstants.TABLE.equals(((COSDictionary)A).getNameAsString(COSName.O))) {
                COSBase Headers = ((COSDictionary) A).getDictionaryObject("Headers");
                if (Headers != null && Headers instanceof COSArray) {
                    List<String> list = new LinkedList<>();
                    for (COSBase elem : (COSArray)Headers) {
                        if (elem instanceof COSString) {
                            list.add(((COSString)elem).getString());
                        }
                    }
                    return list;
                }
            }
        }
        return Collections.emptyList();
    }

    public int getColSpan() {
        return TableHelper.getColSpan((COSDictionary) simplePDObject);
    }

    public int getRowSpan() {
        return TableHelper.getRowSpan((COSDictionary) simplePDObject);
    }

}
