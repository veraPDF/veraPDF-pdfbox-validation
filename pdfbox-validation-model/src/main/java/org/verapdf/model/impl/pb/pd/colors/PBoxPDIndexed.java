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
package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDIndexed;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDIndexed extends PBoxPDColorSpace implements PDIndexed {

	public static final String INDEXED_TYPE = "PDIndexed";

    public static final String BASE = "base";

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBoxPDIndexed(
            org.apache.pdfbox.pdmodel.graphics.color.PDIndexed simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, INDEXED_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (BASE.equals(link)) {
            return this.getBase();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getBase() {
        org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace baseColorSpace =
				((org.apache.pdfbox.pdmodel.graphics.color.PDIndexed) this.simplePDObject)
                .getBaseColorSpace();
        PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(baseColorSpace, this.document, this.flavour);
        if (colorSpace != null) {
			List<PDColorSpace> base = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			base.add(colorSpace);
			return Collections.unmodifiableList(base);
        }
        return Collections.emptyList();
    }
}
