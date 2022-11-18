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

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceNAttributes;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceNProcess;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.impl.pb.pd.functions.PBoxPDFunction;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDDeviceN;
import org.verapdf.model.pdlayer.PDSeparation;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.*;

/**
 * DeviceN color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceN extends PBoxPDColorSpace implements PDDeviceN {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDDeviceN.class.getCanonicalName());

	public static final String DEVICE_N_TYPE = "PDDeviceN";

	public static final String TINT_TRANSFORM = "tintTransform";
	public static final String ALTERNATE = "alternate";
	public static final String COLORANT_NAMES = "colorantNames";
	public static final String COLORANTS = "Colorants";
	public static final String PROCESS_COLOR = "processColor";

	public static final int COLORANT_NAMES_POSITION = 1;

	public static final Set<COSName> IGNORED_COLORANTS;

	static {
		Set<COSName> tempIgnore = new HashSet<>();
		tempIgnore.add(COSName.getPDFName("Cyan"));
		tempIgnore.add(COSName.getPDFName("Magenta"));
		tempIgnore.add(COSName.getPDFName("Yellow"));
		tempIgnore.add(COSName.getPDFName("Black"));
		IGNORED_COLORANTS = Collections.unmodifiableSet(tempIgnore);
	}

	private final boolean areColorantsPresent;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBoxPDDeviceN(
			org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, DEVICE_N_TYPE);
		this.areColorantsPresent = PBoxPDDeviceN.areColorantsPresent(simplePDObject);
		this.document = document;
		this.flavour = flavour;
	}

	private static boolean areColorantsPresent(
			org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
		PDDeviceNAttributes attributes = simplePDObject.getAttributes();
		if (attributes != null) {
			COSDictionary attrDict = attributes.getCOSDictionary();
			COSBase colorantsDict = attrDict.getDictionaryObject(COSName.COLORANTS);
			if (colorantsDict instanceof COSDictionary) {
				COSArray array = (COSArray) simplePDObject.getCOSObject();
				COSBase colorantsArray = array.get(1);

				if (colorantsArray instanceof COSArray) {
					return PBoxPDDeviceN.areColorantsPresent((COSDictionary) colorantsDict, colorantsArray);
				}
			}
		}
		return false;
	}

	private static boolean areColorantsPresent(COSDictionary colorantsDict, COSBase colorantsArray) {
		Set<COSName> colorantDictionaryEntries = colorantsDict.keySet();
		for (int i = 0; i < ((COSArray) colorantsArray).size(); i++) {
			COSBase object = ((COSArray) colorantsArray).getObject(i);
			if (object instanceof COSName &&
					object != COSName.NONE &&
					!IGNORED_COLORANTS.contains(object) &&
					!colorantDictionaryEntries.contains(object)
					) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean getareColorantsPresent() {
		return Boolean.valueOf(this.areColorantsPresent);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ALTERNATE:
				return this.getAlternate();
			case COLORANT_NAMES:
				return this.getColorantNames();
			case COLORANTS:
				return this.getColorants();
			case PROCESS_COLOR:
				return this.getProcessColor();
			case TINT_TRANSFORM:
				return this.getTintTransform();
			default:
				return super.getLinkedObjects(link);
		}
	}
	private List<PDColorSpace> getProcessColor() {
		PDDeviceNAttributes attributes = ((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject).getAttributes();
		if (attributes == null) {
			return Collections.emptyList();
		}
		PDDeviceNProcess process = attributes.getProcess();
		if (process == null) {
			return Collections.emptyList();
		}
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace = process.getColorSpace();
			if (colorSpace != null) {
				return Collections.singletonList(
						ColorSpaceFactory.getColorSpace(colorSpace, this.document, this.flavour));
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, "Problems with process color space obtain in PDDeviceN. " + e.getMessage());
		}
		return Collections.emptyList();
	}


	private List<PDColorSpace> getAlternate() {
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace alternateColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject)
							.getAlternateColorSpace();
			PDColorSpace space = ColorSpaceFactory.getColorSpace(alternateColorSpace, this.document, this.flavour);
			if (space != null) {
				List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpace.add(space);
				return Collections.unmodifiableList(colorSpace);
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, "Can not get alternate color space from DeviceN. " + e.getMessage());
		}
		return Collections.emptyList();
	}

	private List<CosUnicodeName> getColorantNames() {
		COSArray array = (COSArray) this.simplePDObject.getCOSObject();
		COSBase colorants = array.getObject(COLORANT_NAMES_POSITION);
		if (colorants instanceof COSArray) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(((COSArray) colorants).size());
			for (COSBase colorant : (COSArray) colorants) {
				if (colorant instanceof COSName) {
					list.add(new PBCosUnicodeName((COSName) colorant));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<PDSeparation> getColorants() {
		PDDeviceNAttributes attributes =
				((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject).getAttributes();
		if (attributes != null) {
			COSDictionary dictionary = attributes.getCOSDictionary();
			COSBase colorantsDict = dictionary.getDictionaryObject(COSName.COLORANTS);
			if (colorantsDict instanceof COSDictionary) {
				return this.getColorants((COSDictionary) colorantsDict);
			}
		}
		return Collections.emptyList();
	}

	private List<PDSeparation> getColorants(COSDictionary colorantsDict) {
		ArrayList<PDSeparation> list = new ArrayList<>(colorantsDict.size());
		for (COSBase value : colorantsDict.getValues()) {
			try {
				org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace =
						org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace.create(value);
				if (colorSpace instanceof org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) {
					list.add((PBoxPDSeparation) ColorSpaceFactory.getColorSpace(colorSpace, this.document, this.flavour));
				}
			} catch (IOException e) {
				LOGGER.log(java.util.logging.Level.INFO, "Problems with color space obtain. " + e.getMessage());
			}
		}
		return Collections.unmodifiableList(list);
	}
	//Stub
	private List<PBoxPDFunction> getTintTransform() {
		return Collections.emptyList();
	}
}
