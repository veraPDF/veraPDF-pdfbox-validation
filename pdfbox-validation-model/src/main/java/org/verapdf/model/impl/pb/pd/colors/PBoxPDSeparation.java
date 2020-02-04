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

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.containers.StaticContainers;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.impl.pb.pd.functions.PBoxPDFunction;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Separation color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDSeparation extends PBoxPDColorSpace implements org.verapdf.model.pdlayer.PDSeparation {

	public static final String SEPARATION_TYPE = "PDSeparation";

	public static final String TINT_TRANSFORM = "tintTransform";
	public static final String ALTERNATE = "alternate";
	public static final String COLORANT_NAME = "colorantName";

	public static final int COLORANT_NAME_POSITION = 1;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	private COSArray colorSpace;

	public PBoxPDSeparation(
			PDSeparation simplePDObject, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, SEPARATION_TYPE);
		this.document = document;
		this.flavour = flavour;

		this.colorSpace = (COSArray) simplePDObject.getCOSObject();

		if (StaticContainers.getSeparations().containsKey(simplePDObject.getColorantName())) {
			StaticContainers.getSeparations().get(simplePDObject.getColorantName()).add(this);
		} else {
			final List<PBoxPDSeparation> separationList = new ArrayList<>();
			separationList.add(this);
			StaticContainers.getSeparations().put(simplePDObject.getColorantName(), separationList);
		}
	}

	@Override
	public Boolean getareTintAndAlternateConsistent() {
		String name = ((PDSeparation) simplePDObject).getColorantName();

		if (StaticContainers.getInconsistentSeparations().contains(name)) {
			return Boolean.FALSE;
		}

		if (StaticContainers.getSeparations().get(name).size() > 1) {
			for (PBoxPDSeparation pBoxPDSeparation : StaticContainers.getSeparations().get(name)) {
				if (pBoxPDSeparation.equals(this)) {
					continue;
				}

				COSArray toCompare = pBoxPDSeparation.colorSpace;
				COSBase alternateSpaceToCompare = unwrapObject(toCompare.get(2));
				COSBase tintTransformToCompare = unwrapObject(toCompare.get(3));

				COSBase alternateSpaceCurrent = unwrapObject(colorSpace.get(2));
				COSBase tintTransformCurrent = unwrapObject(colorSpace.get(3));

				if (!alternateSpaceToCompare.equals(alternateSpaceCurrent) || !tintTransformToCompare.equals(tintTransformCurrent)) {
					StaticContainers.getInconsistentSeparations().add(name);
					return Boolean.FALSE;
				}
			}
		}

		return Boolean.TRUE;
	}

	private static COSBase unwrapObject(COSBase object) {
		if (object instanceof COSObject) {
			return ((COSObject) object).getObject();
		}
		return object;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ALTERNATE:
				return this.getAlternate();
			case COLORANT_NAME:
				return this.getColorantName();
			case TINT_TRANSFORM:
				return this.getTintTransform();
			default:
				return super.getLinkedObjects(link);
		}
	}

	/**
	 * @return a {@link List} of alternate {@link PDColorSpace} objects
	 */
	public List<PDColorSpace> getAlternate() {
		org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace space =
				((org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) this.simplePDObject)
						.getAlternateColorSpace();
		PDColorSpace currentSpace = ColorSpaceFactory.getColorSpace(space, this.document, this.flavour);
		if (currentSpace != null) {
			List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			colorSpace.add(currentSpace);
			return Collections.unmodifiableList(colorSpace);
		}
		return Collections.emptyList();
	}

	private List<CosUnicodeName> getColorantName() {
		COSArray array = (COSArray) this.simplePDObject.getCOSObject();
		if (array.size() > COLORANT_NAME_POSITION) {
			COSBase object = array.getObject(COLORANT_NAME_POSITION);
			if (object instanceof COSName) {
				ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosUnicodeName((COSName) object));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}
	//Stub
	private List<PBoxPDFunction> getTintTransform() {
		return Collections.emptyList();
	}
}
