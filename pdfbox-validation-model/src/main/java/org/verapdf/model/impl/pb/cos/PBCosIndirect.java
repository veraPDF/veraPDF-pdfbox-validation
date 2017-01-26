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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.tools.IDGenerator;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of CosIndirect interface of abstract model.
 * This class is analogue of COSObject in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosIndirect extends PBCosObject implements CosIndirect {

    public static final String DIRECT_OBJECT = "directObject";
    /** Type name for PBCosBool */
    public static final String COS_INDIRECT_TYPE = "CosIndirect";

    private final boolean isSpacingPDFACompliant;

    private final String id;

    private final PDDocument document;
    private final PDFAFlavour flavour;

    /**
     * Default constructor
     * @param indirectObject pdfbox COSObject
     */
    public PBCosIndirect(COSObject indirectObject, PDDocument document, PDFAFlavour flavour) {
        super(indirectObject, COS_INDIRECT_TYPE);
        this.isSpacingPDFACompliant = getspacingCompliesPDFA(indirectObject);
		this.id = IDGenerator.generateID(indirectObject);
        this.document = document;
        this.flavour = flavour;
    }

	@Override
	public String getID() {
		return id;
	}

    @Override
    public List<? extends Object> getLinkedObjects(
			String link) {
        if (DIRECT_OBJECT.equals(link)) {
            return parseDirectObject();
        }
        return super.getLinkedObjects(link);

    }

    /**
     * Get the direct contents of the indirect object
     */
    private List<CosObject> parseDirectObject() {
        List<CosObject> list = new ArrayList<>();
        COSBase base = ((COSObject) baseObject).getObject();
        list.add(base != null ? getFromValue(base, this.document, this.flavour) : PBCosNull.getInstance());
        return Collections.unmodifiableList(list);
    }

    /**
     * true if the words 'obj' and 'endobj' are surrounded by the correct
     * spacings according to PDF/A standard
     */
    @Override
    public Boolean getspacingCompliesPDFA() {
        return Boolean.valueOf(this.isSpacingPDFACompliant);
    }

    /**
     * Get the direct contents of the indirect object
     */
    private static boolean getspacingCompliesPDFA(COSBase base) {
        return ((COSObject) base).isEndOfObjectComplyPDFA().booleanValue()
                && ((COSObject) base).isHeaderFormatComplyPDFA().booleanValue()
                && ((COSObject) base).isHeaderOfObjectComplyPDFA()
                        .booleanValue();
    }
}
