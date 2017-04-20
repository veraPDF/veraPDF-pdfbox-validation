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

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of CosArray interface of abstract model. This
 * class is analogue of COSArray in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosArray extends PBCosObject implements CosArray {

    /** Type name for PBCosArray */
    public static final String COS_ARRAY_TYPE = "CosArray";

    public static final String ELEMENTS = "elements";
    private final int size;

    private final PDDocument document;
    private final PDFAFlavour flavour;

    /**
     * Default constructor
     * @param array pdfbox COSArray
     */
    public PBCosArray(COSArray array, PDDocument document, PDFAFlavour flavour) {
        this(array, COS_ARRAY_TYPE, document, flavour);
    }

	/**
	 * Constructor used by child classes
	 *
	 * @param array pdfbox COSArray
	 * @param type type of object
	 */
	public PBCosArray(COSArray array, String type, PDDocument document, PDFAFlavour flavour) {
		super(array, type);
		this.size = array.size();
        this.document = document;
        this.flavour = flavour;
	}

    /**
     * Getter for array size.
     *
     * @return size of array
     */
    @Override
    public Long getsize() {
        return Long.valueOf(this.size);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (link.equals(ELEMENTS)) {
            return this.getElements();
        }
        return super.getLinkedObjects(link);
    }

    /**
     * Get all elements of array.
     *
     * @return elements of array
     */
    private List<CosObject> getElements() {
        List<CosObject> list = new ArrayList<>(this.getsize().intValue());
        for (COSBase base : (COSArray) this.baseObject) {
            if (base != null) {
                list.add(getFromValue(base, this.document, this.flavour));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
