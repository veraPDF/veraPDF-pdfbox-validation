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

import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.impl.pb.pd.PBoxPDResource;
import org.verapdf.model.pdlayer.PDColorSpace;

/**
 * Color space object
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDColorSpace extends PBoxPDResource implements PDColorSpace {

    protected PBoxPDColorSpace(
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace simplePDObject,
			final String type) {
        super(simplePDObject, type);
    }

	protected PBoxPDColorSpace(PDAbstractPattern simplePDObject,
							   final String type) {
		super(simplePDObject, type);
	}

    /**
     * @return number of colorants
     */
    @Override
    public Long getnrComponents() {
        if (this.simplePDObject instanceof org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) {
            int numberOfComponents = ((org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace) this.simplePDObject)
                    .getNumberOfComponents();
            return Long.valueOf(numberOfComponents);
        } else if (this.simplePDObject instanceof PDAbstractPattern) {
            return Long.valueOf(-1);
        } else
            return null;
    }

}
