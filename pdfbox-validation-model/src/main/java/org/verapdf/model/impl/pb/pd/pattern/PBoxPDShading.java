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
package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.pd.PBoxPDResource;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDShading;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShading extends PBoxPDResource implements PDShading {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDShading.class);

    public static final String SHADING_TYPE = "PDShading";

    public static final String COLOR_SPACE = "colorSpace";

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBoxPDShading(
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, SHADING_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (COLOR_SPACE.equals(link)) {
            return this.getColorSpace();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getColorSpace() {
        try {
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace cs =
					((org.apache.pdfbox.pdmodel.graphics.shading.PDShading) this.simplePDObject)
                    .getColorSpace();
            if (cs != null) {
				List<PDColorSpace> colorSpaces =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(ColorSpaceFactory.getColorSpace(cs, this.document, this.flavour));
				return Collections.unmodifiableList(colorSpaces);
            }
        } catch (IOException e) {
            LOGGER.debug("Problems with color space obtaining from shading. "
                    + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
