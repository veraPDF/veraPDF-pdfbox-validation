/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.pd;

import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDGroup;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroup extends PBoxPDObject implements PDGroup {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDGroup.class.getCanonicalName());

	public static final String GROUP_TYPE = "PDGroup";

	public static final String COLOR_SPACE = "colorSpace";

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBoxPDGroup(
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup simplePDObject, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, GROUP_TYPE);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public String getS() {
		return ((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject)
				.getSubType().getName();
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (COLOR_SPACE.equals(link)) {
			return this.getColorSpace();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDColorSpace> getColorSpace() {
		// TODO: missing resources dependence (needed to handle default colorspace)
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject).getColorSpace();
			PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbColorSpace, this.document, this.flavour);
			if (colorSpace != null) {
				List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(colorSpace);
				return Collections.unmodifiableList(colorSpaces);
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO,
					"Problems with color space obtaining on group. "
							+ e.getMessage());
		}
		return Collections.emptyList();
	}
}
