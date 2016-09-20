package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.log4j.Logger;
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

	private static final Logger LOGGER = Logger.getLogger(PBOp_EI.class);

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
			LOGGER.debug(e);
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
				LOGGER.debug("Problem with color space coping.", e);
			}
		}

		return pageRes;
	}

}
