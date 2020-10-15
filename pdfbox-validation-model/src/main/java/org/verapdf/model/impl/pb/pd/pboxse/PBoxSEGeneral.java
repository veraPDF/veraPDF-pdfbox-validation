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

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.impl.pb.pd.PBoxPDStructElem;
import org.verapdf.model.impl.pb.pd.TaggedPDFConstants;
import org.verapdf.model.tools.TaggedPDFRoleMapHelper;

public abstract class PBoxSEGeneral extends PBoxPDStructElem {

    private final String standardType;

    protected PBoxSEGeneral(COSDictionary structElemDictionary, TaggedPDFRoleMapHelper roleMapHelper, String standardType, String type) {
        super(structElemDictionary, roleMapHelper, type);
        this.standardType = standardType;
    }

    public static PBoxSEGeneral createTypedStructElem(COSDictionary structElemDictionary, TaggedPDFRoleMapHelper roleMapHelper){
        String standardType = PBoxPDStructElem.getStructureElementStandardType(structElemDictionary, roleMapHelper);

        if (standardType == null) {
            return new PBoxSENonStandard(structElemDictionary, roleMapHelper, null);
        }

        switch (standardType) {
            case TaggedPDFConstants.ANNOT:
                return new PBoxSEAnnot(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.ART:
                return new PBoxSEArt(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.ARTIFACT:
                return new PBoxSEArtifact(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.ASIDE:
                return new PBoxSEAside(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.BIB_ENTRY:
                return new PBoxSEBibEntry(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.BLOCK_QUOTE:
                return new PBoxSEBlockQuote(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.CAPTION:
                return new PBoxSECaption(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.CODE:
                return new PBoxSECode(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.DIV:
                return new PBoxSEDiv(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.DOCUMENT:
                return new PBoxSEDocument(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.DOCUMENT_FRAGMENT:
                return new PBoxSEDocumentFragment(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.EM:
                return new PBoxSEEm(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.FENOTE:
                return new PBoxSEFENote(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.FIGURE:
                return new PBoxSEFigure(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.FORM:
                return new PBoxSEForm(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.FORMULA:
                return new PBoxSEFormula(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.H:
                return new PBoxSEH(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.INDEX:
                return new PBoxSEIndex(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.L:
                return new PBoxSEL(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.LBL:
                return new PBoxSELbl(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.LBODY:
                return new PBoxSELBody(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.LI:
                return new PBoxSELI(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.LINK:
                return new PBoxSELink(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.NON_STRUCT:
                return new PBoxSENonStruct(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.NOTE:
                return new PBoxSENote(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.P:
                return new PBoxSEP(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.PART:
                return new PBoxSEPart(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.PRIVATE:
                return new PBoxSEPrivate(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.QUOTE:
                return new PBoxSEQuote(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.RB:
                return new PBoxSERB(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.REFERENCE:
                return new PBoxSEReference(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.RP:
                return new PBoxSERP(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.RT:
                return new PBoxSERT(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.RUBY:
                return new PBoxSERuby(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.SECT:
                return new PBoxSESect(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.SPAN:
                return new PBoxSESpan(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.STRONG:
                return new PBoxSEStrong(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.SUB:
                return new PBoxSESub(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TABLE:
                return new PBoxSETable(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TBODY:
                return new PBoxSETBody(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TD:
                return new PBoxSETD(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TFOOT:
                return new PBoxSETFoot(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TH:
                return new PBoxSETH(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.THEAD:
                return new PBoxSETHead(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TITLE:
                return new PBoxSETitle(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TOC:
                return new PBoxSETOC(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TOCI:
                return new PBoxSETOCI(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.TR:
                return new PBoxSETR(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.WARICHU:
                return new PBoxSEWarichu(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.WP:
                return new PBoxSEWP(structElemDictionary, roleMapHelper);
            case TaggedPDFConstants.WT:
                return new PBoxSEWT(structElemDictionary, roleMapHelper);
            default:
                if (standardType.matches(TaggedPDFConstants.HN_REGEXP)) {
                    return new PBoxSEHn(structElemDictionary, roleMapHelper, standardType);
                } else {
                    return new PBoxSENonStandard(structElemDictionary, roleMapHelper, standardType);
                }
        }
    }

    @Override
    public String getstandardType() {
        return this.standardType;
    }
}
