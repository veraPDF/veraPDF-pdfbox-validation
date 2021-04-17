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
package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.action.PDPageAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosBBox;
import org.verapdf.model.impl.pb.cos.PBCosBBox;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDPageAdditionalActions;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Page representation of pdf document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDPage.class);

	/** Type name for {@code PBoxPDPage} */
	public static final String PAGE_TYPE = "PDPage";

	/** Link name for page annotations */
	public static final String ANNOTS = "annots";
	/** Link name for page additional actions */
	public static final String ACTION = "AA";
	/** Link name for page content stream */
	public static final String CONTENT_STREAM = "contentStream";
	/** Link name for page transparency group */
	public static final String GROUP = "Group";
	/** Link name for page media box */
	public static final String MEDIA_BOX = "MediaBox";
	/** Link name for page crop box */
	public static final String CROP_BOX = "CropBox";
	/** Link name for page bleed box */
	public static final String BLEED_BOX = "BleedBox";
	/** Link name for trim media box */
	public static final String TRIM_BOX = "TrimBox";
	/** Link name for page art box */
	public static final String ART_BOX = "ArtBox";
	/** Link name for output intents */
	public static final String OUTPUT_INTENTS = "outputIntents";
	/** Link name for resource dictionary */
	private static final String RESOURCES = "resources";
	/** Link name for page presentation steps */
	public static final String PRESENTATION_STEPS = "PresSteps";

	public static final String PORTRAIT_ORIENTATION = "Portrait";
	public static final String LANDSCAPE_ORIENTATION = "Landscape";
	public static final String SQUARE_ORIENTATION = "Square";

	public static final String TRANSPARENCY_COLOR_SPACE = "transparencyColorSpace";
	public static final String PARENT_TRANSPARENCY_COLOR_SPACE = "parentTransparencyColorSpace";

	private boolean containsTransparency = false;
	private List<PDContentStream> contentStreams = null;
	private OutputIntents outputIntents = null;
	private List<PDAnnot> annotations = null;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	/**
	 * Default constructor.
	 *
	 * @param simplePDObject Apache PDFBox page representation
	 */
	public PBoxPDPage(org.apache.pdfbox.pdmodel.PDPage simplePDObject, PDDocument document, PDFAFlavour flavour) {
		super((COSObjectable) simplePDObject, PAGE_TYPE);
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public Boolean getcontainsPresSteps() {
		COSBase presSteps = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject().getDictionaryObject(COSName.getPDFName(PRESENTATION_STEPS));
		if (presSteps != null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean getcontainsTransparency() {
		if (this.contentStreams == null) {
			parseContentStream();
		}
		if (this.annotations == null) {
			this.annotations = parseAnnotataions();
		}
		return Boolean.valueOf(this.containsTransparency);
	}

	@Override
	public Boolean getcontainsGroupCS() {
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject();
		COSBase groupDictionary = dictionary.getDictionaryObject(COSName.GROUP);
		if (groupDictionary instanceof COSDictionary) {
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup group =
					new org.apache.pdfbox.pdmodel.graphics.form.PDGroup(
							(COSDictionary) groupDictionary);
			try {
				return Boolean.valueOf(group != null && group.getColorSpace() != null);
			} catch (IOException e) {
				LOGGER.debug("Problem with obtaining group colorspace", e);
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean getcontainsAA() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.AA);
	}

	@Override
	public String getTabs() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		if (pageObject != null && pageObject instanceof COSDictionary) {
			return ((COSDictionary) pageObject).getNameAsString(COSName.getPDFName("Tabs"));
		}
		return null;
	}

	@Override
	public String getorientation() {
		CosBBox mediaBox = getMediaBox().get(0);
		double height = mediaBox.gettop() - mediaBox.getbottom();
		double width = mediaBox.getright() - mediaBox.getleft();
		long rotation = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getRotation();
		if ((height > width && rotation % 180 == 0) || (height < width && rotation % 180 == 90)) {
			return PORTRAIT_ORIENTATION;
		}
		if ((height < width && rotation % 180 == 0) || (height > width && rotation % 180 == 90)) {
			return LANDSCAPE_ORIENTATION;
		}
		return SQUARE_ORIENTATION;
	}

	@Override
	public String getoutputColorSpace() {
		if (this.outputIntents == null) {
			this.outputIntents = parseOutputIntents();
		}
		return this.outputIntents != null ? ((PBoxOutputIntents)this.outputIntents).getColorSpace() : null;
	}

	private List<OutputIntents> getOutputIntents() {
		if (this.outputIntents == null) {
			this.outputIntents = parseOutputIntents();
		}
		if (this.outputIntents != null) {
			List<OutputIntents> array = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			array.add(this.outputIntents);
			return array;
		}
		return Collections.emptyList();
	}

	private OutputIntents parseOutputIntents() {
		if (flavour != null && flavour.getPart() != PDFAFlavour.Specification.ISO_19005_4) {
			return null;
		}
		List<PDOutputIntent> outInts = new ArrayList<>();
		COSArray array = (COSArray) ((COSDictionary)this.simplePDObject.getCOSObject()).getDictionaryObject(COSName.OUTPUT_INTENTS);
		if (array != null) {
			for (COSBase cosBase : array) {
				if (cosBase instanceof COSObject) {
					cosBase = ((COSObject)cosBase).getObject();
				}
				outInts.add(new PDOutputIntent((COSDictionary) cosBase));
			}
		}
		if (outInts.size() > 0) {
			return new PBoxOutputIntents(outInts, document, flavour);
		}
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case GROUP:
				return this.getGroup();
			case ANNOTS:
				return this.getAnnotations();
			case ACTION:
				return this.getActions();
			case CONTENT_STREAM:
				return this.getContentStream();
			case MEDIA_BOX:
				return this.getMediaBox();
			case RESOURCES:
				return Collections.emptyList();
			case CROP_BOX:
				return this.getCropBox();
			case BLEED_BOX:
				return this.getBleedBox();
			case TRIM_BOX:
				return this.getTrimBox();
			case ART_BOX:
				return this.getArtBox();
			case OUTPUT_INTENTS:
				return this.getOutputIntents();
			case TRANSPARENCY_COLOR_SPACE:
				return Collections.emptyList();
			case PARENT_TRANSPARENCY_COLOR_SPACE:
				return Collections.emptyList();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDGroup> getGroup() {
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject();
		COSBase groupDictionary = dictionary.getDictionaryObject(COSName.GROUP);
		if (groupDictionary instanceof COSDictionary) {
			List<PDGroup> groups = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup group =
					new org.apache.pdfbox.pdmodel.graphics.form.PDGroup(
							(COSDictionary) groupDictionary);
			groups.add(new PBoxPDGroup(group, this.document, this.flavour));
			return Collections.unmodifiableList(groups);
		}
		return Collections.emptyList();
	}

	private List<PDContentStream> getContentStream() {
		if (this.contentStreams == null) {
			parseContentStream();
		}
		return this.contentStreams;
	}

	private void parseContentStream() {
		this.contentStreams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		org.apache.pdfbox.pdmodel.PDPage page =
				(org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject;
		PDInheritableResources resources = PDInheritableResources
				.getInstance(page.getInheritedResources(), page.getPageResources());
		PBoxPDContentStream contentStream = new PBoxPDContentStream(page, resources, this.document, this.flavour);
		contentStreams.add(contentStream);
		this.containsTransparency = contentStream.isContainsTransparency();
	}

	private List<PDAdditionalActions> getActions() {
		PDPageAdditionalActions pbActions = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject).getActions();
		if (pbActions != null && pbActions.getCOSObject().size() != 0) {
			List<PDAdditionalActions> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			actions.add(new PBoxPDPageAdditionalActions(pbActions));
			return Collections.unmodifiableList(actions);
		}
		return Collections.emptyList();
	}

	private List<PDAnnot> getAnnotations() {
		if (this.annotations == null) {
			this.annotations = parseAnnotataions();
		}

		return this.annotations;
	}

	private List<PDAnnot> parseAnnotataions() {
		try {
			List<PDAnnotation> pdfboxAnnotations = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
					.getAnnotations();
			if (pdfboxAnnotations != null) {
				List<PDAnnot> annotations = new ArrayList<>(pdfboxAnnotations.size());
				this.addAllAnnotations(annotations, pdfboxAnnotations);
				return Collections.unmodifiableList(annotations);
			}
		} catch (IOException e) {
			LOGGER.debug(
					"Problems in obtaining pdfbox PDAnnotations. "
							+ e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	private void addAllAnnotations(List<PDAnnot> annotations,
								   List<PDAnnotation> pdfboxAnnotations) {
		PDResources pageResources = ((org.apache.pdfbox.pdmodel.PDPage)
				this.simplePDObject).getResources();
		for (PDAnnotation annotation : pdfboxAnnotations) {
			if (annotation != null) {
				PBoxPDAnnot annot = PBoxPDAnnot.createAnnot(annotation, pageResources, this.document, this.flavour);
				this.containsTransparency |= annot.isContainsTransparency();
				annotations.add(annot);
			}
		}
	}

	private List<CosBBox> getMediaBox() {
		return this.getCosBBox(COSName.MEDIA_BOX);
	}

	private List<CosBBox> getCropBox() {
		return this.getCosBBox(COSName.CROP_BOX);
	}

	private List<CosBBox> getBleedBox() {
		return this.getCosBBox(COSName.BLEED_BOX);
	}

	private List<CosBBox> getTrimBox() {
		return this.getCosBBox(COSName.TRIM_BOX);
	}

	private List<CosBBox> getArtBox() {
		return this.getCosBBox(COSName.ART_BOX);
	}

	private List<CosBBox> getCosBBox(COSName key) {
		COSBase array = PDPageTree.getInheritableAttribute((COSDictionary) this.simplePDObject.getCOSObject(), key);
		if (array instanceof COSArray) {
			ArrayList<CosBBox> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosBBox((COSArray) array, this.document, this.flavour));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
