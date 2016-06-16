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

		if (StaticContainers.separations.containsKey(simplePDObject.getColorantName())) {
			StaticContainers.separations.get(simplePDObject.getColorantName()).add(this);
		} else {
			final List<PBoxPDSeparation> separationList = new ArrayList<>();
			separationList.add(this);
			StaticContainers.separations.put(simplePDObject.getColorantName(), separationList);
		}
	}

	@Override
	public Boolean getareTintAndAlternateConsistent() {
		String name = ((PDSeparation) simplePDObject).getColorantName();

		if (StaticContainers.inconsistentSeparations.contains(name)) {
			return Boolean.FALSE;
		}

		if (StaticContainers.separations.get(name).size() > 1) {
			for (PBoxPDSeparation pBoxPDSeparation : StaticContainers.separations.get(name)) {
				if (pBoxPDSeparation.equals(this)) {
					continue;
				}

				COSArray toCompare = pBoxPDSeparation.colorSpace;
				COSBase alternateSpaceToCompare = unwrapObject(toCompare.get(2));
				COSBase tintTransformToCompare = unwrapObject(toCompare.get(3));

				COSBase alternateSpaceCurrent = unwrapObject(colorSpace.get(2));
				COSBase tintTransformCurrent = unwrapObject(colorSpace.get(3));

				if (!alternateSpaceToCompare.equals(alternateSpaceCurrent) || !tintTransformToCompare.equals(tintTransformCurrent)) {
					StaticContainers.inconsistentSeparations.add(name);
					return Boolean.FALSE;
				}
			}
		}

		return Boolean.TRUE;
	}

	private COSBase unwrapObject(COSBase object) {
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
}
