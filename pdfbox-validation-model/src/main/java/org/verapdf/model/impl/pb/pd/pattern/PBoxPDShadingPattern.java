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

import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDShading;
import org.verapdf.model.pdlayer.PDShadingPattern;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShadingPattern extends PBoxPDPattern implements
        PDShadingPattern {

    private static final Logger LOGGER = Logger
            .getLogger(PBoxPDShadingPattern.class.getCanonicalName());

	public static final String SHADING_PATTERN_TYPE = "PDShadingPattern";

    public static final String SHADING = "shading";

    private final PDDocument document;
    private final PDFAFlavour flavour;

	public PBoxPDShadingPattern(
            org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, SHADING_PATTERN_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
		if (SHADING.equals(link)) {
			return this.getShading();
		}
		return super.getLinkedObjects(link);
    }

    private List<PDShading> getShading() {
        try {
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading =
					((org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern) this.simplePDObject)
                    .getShading();
            if (shading != null) {
				List<PDShading> shadings =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				shadings.add(new PBoxPDShading(shading, this.document, this.flavour));
				return Collections.unmodifiableList(shadings);
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.INFO, "Can`t get shading pattern. " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
