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

import java.util.logging.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.external.JPEG2000;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.external.PBoxJPEG2000;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDSMaskImage;
import org.verapdf.model.pdlayer.PDXImage;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImage extends PBoxPDXObject implements PDXImage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDXImage.class.getCanonicalName());

	public static final String X_IMAGE_TYPE = "PDXImage";

	public static final String IMAGE_CS = "imageCS";
	public static final String ALTERNATES = "Alternates";
	public static final String INTENT = "Intent";
	public static final String JPX_STREAM = "jpxStream";
	public static final String S_MASK = "SMask";

	private final boolean interpolate;
	private List<JPEG2000> jpeg2000List = null;
	private PDColorSpace colorSpaceFromImage = null;

	public PBoxPDXImage(PDImageXObjectProxy simplePDObject, PDInheritableResources resources,
						PDDocument document, PDFAFlavour flavour) {
		this(simplePDObject, resources, X_IMAGE_TYPE, document, flavour);
	}

	protected PBoxPDXImage(PDImageXObjectProxy simplePDObject, PDInheritableResources resources,
						   String type, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, resources, type, document, flavour);
		this.interpolate = simplePDObject.getInterpolate();
	}

	@Override
	public Boolean getInterpolate() {
		return Boolean.valueOf(this.interpolate);
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
		case INTENT:
			return this.getIntent();
		case IMAGE_CS:
			return this.getImageCS();
		case ALTERNATES:
			return this.getAlternates();
		case JPX_STREAM:
			return this.getJPXStream();
		case S_MASK:
			return this.getSMask();
		default:
			return super.getLinkedObjects(link);
		}
	}

	protected List<PDSMaskImage> getSMask() {
		try {
			COSStream cosStream = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) this.simplePDObject).getCOSStream();
			COSBase smaskDictionary = cosStream.getDictionaryObject(COSName.SMASK);
			if (smaskDictionary instanceof COSDictionary) {
				PDSMaskImage xObject = this.getXObject(smaskDictionary);
				if (xObject != null) {
					List<PDSMaskImage> mask = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					mask.add(xObject);
					return Collections.unmodifiableList(mask);
				}
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, "Problems with obtaining SMask. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	private PDSMaskImage getXObject(COSBase smaskDictionary) throws IOException {
		COSName name = ((COSDictionary) smaskDictionary).getCOSName(COSName.NAME);
		String nameAsString = name != null ? name.getName() : null;
		PDResources resourcesLocal = null;
		if (this.simplePDObject instanceof PDFormXObject) {
			resourcesLocal = ((PDFormXObject) this.simplePDObject).getResources();
		}
		org.apache.pdfbox.pdmodel.graphics.PDXObject pbObject = org.apache.pdfbox.pdmodel.graphics.PDXObject
				.createXObject(smaskDictionary, nameAsString, resourcesLocal);
		if (pbObject instanceof PDImageXObjectProxy) {
			return new PBoxPDSMaskImage((PDImageXObjectProxy) pbObject, resources, document, flavour);
		}
		LOGGER.log(java.util.logging.Level.INFO, "SMask object is not an Image XObject");
		return null;
	}

	private List<CosRenderingIntent> getIntent() {
		COSDictionary imageStream = (COSDictionary) this.simplePDObject.getCOSObject();
		COSName intent = imageStream.getCOSName(COSName.getPDFName(INTENT));
		if (intent != null) {
			List<CosRenderingIntent> intents = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			intents.add(new PBCosRenderingIntent(intent));
			return Collections.unmodifiableList(intents);
		}
		return Collections.emptyList();
	}

	private List<PDColorSpace> getImageCS() {
		if (this.jpeg2000List == null) {
			this.jpeg2000List = parseJPXStream();
		}
		PDImageXObjectProxy image = (PDImageXObjectProxy) this.simplePDObject;
		if (!image.isStencil()) {
			try {
				PDColorSpace buffer = ColorSpaceFactory.getColorSpace(image.getColorSpace(),
						null, resources, 0, false, this.document, this.flavour);
				if (buffer == null) {
					buffer = this.colorSpaceFromImage;
				}
				if (buffer != null) {
					List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					colorSpaces.add(buffer);
					return Collections.unmodifiableList(colorSpaces);
				}
			} catch (IOException e) {
				LOGGER.log(java.util.logging.Level.INFO, "Could not obtain Image XObject color space. " + e.getMessage());
			}
		}
		return Collections.emptyList();
	}

	private List<? extends PDXImage> getAlternates() {
		final List<PDXImage> alternates = new ArrayList<>();
		try (final COSStream imageStream = ((PDImageXObjectProxy) this.simplePDObject).getCOSStream()) {
			final COSBase buffer = imageStream.getDictionaryObject(COSName.getPDFName(ALTERNATES));
			this.addAlternates(alternates, buffer, ((PDImageXObjectProxy) this.simplePDObject).getResources());
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		return alternates;
	}

	private void addAlternates(List<PDXImage> alternates, COSBase buffer, PDResources resourcesToAdd) {
		if (buffer instanceof COSArray) {
			for (COSBase element : (COSArray) buffer) {
				if (element instanceof COSObject) {
					element = ((COSObject) element).getObject();
				}
				if (element instanceof COSDictionary) {
					this.addAlternate(alternates, (COSDictionary) element, resourcesToAdd);
				}
			}
		}
	}

	private void addAlternate(List<PDXImage> alternates, COSDictionary buffer, PDResources resourcesToAdd) {
		COSBase alternatesImages = buffer.getDictionaryObject(COSName.IMAGE);
		if (alternatesImages instanceof COSStream) {

			final PDStream stream = new PDStream((COSStream) alternatesImages);
			PDImageXObjectProxy imageXObject = new PDImageXObjectProxy(stream, resourcesToAdd);
			alternates.add(new PBoxPDXImage(imageXObject, resources, this.document, this.flavour));
		}
	}

	private List<JPEG2000> getJPXStream() {
		if (jpeg2000List == null) {
			jpeg2000List = parseJPXStream();
		}
		return jpeg2000List;
	}

	private List<JPEG2000> parseJPXStream() {
		try {
			PDStream stream = ((PDImageXObjectProxy) (this.simplePDObject)).getPDStream();
			List<COSName> filters = stream.getFilters();
			if (filters != null && filters.contains(COSName.JPX_DECODE)) {
				// TODO: handle the case when jpx stream is additionally hex
				// encoded
				try (InputStream image = stream.getStream().getFilteredStream()) {
					ArrayList<JPEG2000> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					PBoxJPEG2000 jpeg2000 = PBoxJPEG2000.fromStream(image, this.document, this.flavour);
					this.colorSpaceFromImage = jpeg2000.getImageColorSpace();
					list.add(jpeg2000);
					return Collections.unmodifiableList(list);
				}
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, "Problems with stream obtain. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	/**
	 * @return true if current image contains SMask value of type stream or
	 *         SMaskInData value greater then 0
	 */
	public boolean containsTransparency() {
		if (this.simplePDObject == null) {
			return false;
		}

		COSBase base = this.simplePDObject.getCOSObject();
		if (base instanceof COSStream) {
			try (COSStream stream = (COSStream) base) {
				if (stream.getDictionaryObject(COSName.SMASK) instanceof COSStream) {
					return true;
				}

				COSBase sMaskInData = stream.getDictionaryObject(COSName.getPDFName("SMaskInData"));
				if (sMaskInData instanceof COSNumber) {
					return ((COSNumber) sMaskInData).doubleValue() > 0;
				}
			} catch (IOException excep) {
				// TODO Auto-generated catch block
				excep.printStackTrace();
			}
		}

		return false;
	}
}
