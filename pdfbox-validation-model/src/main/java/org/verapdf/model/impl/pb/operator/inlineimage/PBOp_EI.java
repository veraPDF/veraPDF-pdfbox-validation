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
package org.verapdf.model.impl.pb.operator.inlineimage;

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.images.PBoxPDInlineImage;
import org.verapdf.model.operator.Op_EI;
import org.verapdf.model.pdlayer.PDInlineImage;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_EI extends PBOpInlineImage implements Op_EI {

	private static final Logger LOGGER = Logger.getLogger(PBOp_EI.class.getCanonicalName());

	public static final String OP_EI_TYPE = "Op_EI";

	public static final String INLINE_IMAGE = "inlineImage";

	private final byte[] imageData;
	private final PDResources resources;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBOp_EI(List<COSBase> arguments, byte[] imageData,
				   PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(arguments, OP_EI_TYPE);
		this.imageData = imageData;
		this.resources = PBOp_EI.getResources(resources);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (INLINE_IMAGE.equals(link)) {
			return this.getInlineImage();
		}

		return super.getLinkedObjects(link);
	}

	private List<PDInlineImage> getInlineImage() {
		try {
			COSBase parameters = this.arguments.get(0);
			org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage inlineImage =
					new org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage(
							(COSDictionary) parameters,
							this.imageData,
							this.resources);

			List<PDInlineImage> inlineImages = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			inlineImages.add(new PBoxPDInlineImage(inlineImage, this.document, this.flavour));
			return Collections.unmodifiableList(inlineImages);
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, e.getMessage());
		}
		return Collections.emptyList();
	}

	private static PDResources getResources(PDInheritableResources resources) {
		PDResources currRes = resources.getCurrentResources();
		COSDictionary dictionary = resources.getInheritedResources().getCOSObject();
		PDResources pageRes = new PDResources(new COSDictionary(dictionary));

		for (COSName name : currRes.getColorSpaceNames()) {
			try {
				pageRes.put(name, currRes.getColorSpace(name));
			} catch (IOException e) {
				LOGGER.log(java.util.logging.Level.INFO, "Problem with color space coping. " + e.getMessage());
			}
		}

		return pageRes;
	}

}
