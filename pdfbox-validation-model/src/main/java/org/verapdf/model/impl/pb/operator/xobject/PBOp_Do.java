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
package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;
import org.verapdf.model.operator.Op_Do;
import org.verapdf.model.pdlayer.PDXObject;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which paints the specified XObject
 *
 * @author Timur Kamalov
 */
public class PBOp_Do extends PBOperator implements Op_Do {

	/** Type name for {@code PBOp_Do} */
    public static final String OP_DO_TYPE = "Op_Do";

	/** Name of link to the XObject */
    public static final String X_OBJECT = "xObject";

	private List<PDXObject> xObjects = null;

	private final PDDocument document;
	private final PDFAFlavour flavour;

    private final org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject;
	private final PDInheritableResources resources;

    public PBOp_Do(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject,
			PDInheritableResources resources,
				   PDDocument document,
				   PDFAFlavour flavour) {
        super(arguments, OP_DO_TYPE);
        this.pbXObject = pbXObject;
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (X_OBJECT.equals(link)) {
            return this.getXObject();
        }
        return super.getLinkedObjects(link);
    }

	/**
	 * @return XObject object from veraPDF model used in current operator
	 */
	public List<PDXObject> getXObject() {
		if (this.xObjects == null) {
			PDXObject typedPDXObject = PBoxPDXObject.getTypedPDXObject(
					this.pbXObject, this.resources, this.document, this.flavour);
			if (typedPDXObject != null) {
				List<PDXObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(typedPDXObject);
				this.xObjects = Collections.unmodifiableList(list);
			} else {
				this.xObjects = Collections.emptyList();
			}
		}
		return this.xObjects;
	}

}
