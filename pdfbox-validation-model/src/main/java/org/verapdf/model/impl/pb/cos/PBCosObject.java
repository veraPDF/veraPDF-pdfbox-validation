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

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.visitor.cos.pb.PBCosVisitor;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;

/**
 * Current class is representation of CosObject interface of abstract model.
 * This class is analogue of COSBase in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosObject extends GenericModelObject implements CosObject {

    /** Type name for PBCosObject */
    private static final Logger LOGGER = Logger.getLogger(PBCosObject.class.getCanonicalName());

	public static final int MAX_NUMBER_OF_ELEMENTS = 1;

	protected final COSBase baseObject;

    protected PBCosObject(final COSBase baseObject, final String type) {
		super(type);
        this.baseObject = baseObject;
    }

    /**
     * Transform object of pdf box to corresponding object of abstract model
     * implementation. For transforming using {@code PBCosVisitor}.
     *
     * @param base
     *            the base object that all objects in the PDF document will
     *            extend in pdf box
     * @return object of abstract model implementation, transformed from
     *         {@code base}
     */
    public static CosObject getFromValue(COSBase base, PDDocument document, PDFAFlavour flavour) {
        try {
            if (base != null) {
                PBCosVisitor visitor = PBCosVisitor.getInstance(document, flavour);
                if (base instanceof COSObject) {
                    return (CosObject) PBCosVisitor
                            .visitFromObject((COSObject) base, document, flavour);
                }
                return (CosObject) base.accept(visitor);
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.INFO,
                    "Problems with wrapping pdfbox object \"" + base.toString()
                            + "\". " + e.getMessage());
        }
        return null;
    }
}
