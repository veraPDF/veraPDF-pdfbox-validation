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
package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIIFilter;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosIIFilter;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDInlineImage;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImage extends PBoxPDObject implements PDInlineImage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDInlineImage.class);

	public static final String F = "F";

	public static final String INLINE_IMAGE_TYPE = "PDInlineImage";

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBoxPDInlineImage(org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage simplePDObject, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, INLINE_IMAGE_TYPE);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public Boolean getInterpolate() {
		return Boolean.valueOf(((PDImage) this.simplePDObject)
				.getInterpolate());
	}

	@Override
	public String getSubtype() {
		return null;
	}

	//TODO : implement
	@Override
	public Boolean getisInherited() {
		return Boolean.FALSE;
	}

	@Override
	public Boolean getcontainsOPI() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.getPDFName("OPI"));
	}

	@Override
	public Boolean getcontainsSMask() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.SMASK);
	}

	@Override
	public Boolean getcontainsAlternates() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.getPDFName("Alternates"));
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case PBoxPDXImage.INTENT:
				return this.getIntent();
			case PBoxPDXImage.IMAGE_CS:
				return this.getImageCS();
			case PBoxPDXObject.S_MASK:
			case PBoxPDXObject.OPI:
			case PBoxPDXImage.ALTERNATES:
			case PBoxPDXImage.JPX_STREAM:
				return Collections.emptyList();
			case F:
				return getFilters();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosIIFilter> getFilters() {
		List<String> filters = ((org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage) this.simplePDObject).getFilters();
		List<CosIIFilter> result = new ArrayList<>();
		if (filters != null) {
			for (String filter : filters) {
				result.add(new PBCosIIFilter(filter));
			}
		}
		return result;
	}

	private List<PDColorSpace> getImageCS() {
		try {
			PDColorSpace buffer = ColorSpaceFactory
					.getColorSpace(((PDImage) this.simplePDObject)
							.getColorSpace(), this.document, this.flavour);
			if (buffer != null) {
				List<PDColorSpace> colorSpaces =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(buffer);
				return Collections.unmodifiableList(colorSpaces);
			}
		} catch (IOException e) {
			LOGGER.debug(
					"Problems with color space obtaining from InlineImage XObject. "
							+ e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	private List<CosRenderingIntent> getIntent() {
		COSDictionary imageStream = (COSDictionary) this.simplePDObject
				.getCOSObject();
		COSName intent = imageStream.getCOSName(COSName.getPDFName(
				PBoxPDXImage.INTENT));
		if (intent != null) {
			List<CosRenderingIntent> intents = new ArrayList<>(
					MAX_NUMBER_OF_ELEMENTS);
			intents.add(new PBCosRenderingIntent(intent));
			return Collections.unmodifiableList(intents);
		}
		return Collections.emptyList();
	}

}
