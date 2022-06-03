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

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDNumberTreeNode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDParentTreeValue;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionFactory;
import org.apache.pdfbox.pdmodel.interactive.action.PDAnnotationAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosBM;
import org.verapdf.model.coslayer.CosLang;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosBM;
import org.verapdf.model.impl.pb.cos.PBCosLang;
import org.verapdf.model.impl.pb.cos.PBCosNumber;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAnnotationAdditionalActions;
import org.verapdf.model.impl.pb.pd.annotations.PBoxPD3DAnnot;
import org.verapdf.model.impl.pb.pd.annotations.PBoxPDLinkAnnot;
import org.verapdf.model.impl.pb.pd.annotations.PBoxPDPrinterMarkAnnot;
import org.verapdf.model.impl.pb.pd.annotations.PBoxPDTrapNetAnnot;
import org.verapdf.model.impl.pb.pd.annotations.PBoxPDWidgetAnnot;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAdditionalActions;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnot extends PBoxPDObject implements PDAnnot {

	public static final String ANNOTATION_TYPE = "PDAnnot";

	public static final String DICT = "Dict";
	public static final String STREAM = "Stream";

	public static final String APPEARANCE = "appearance";
	public static final String C = "C";
	public static final String IC = "IC";
	public static final String A = "A";
	public static final String ADDITIONAL_ACTION = "AA";
	public static final String LANG = "Lang";
	public static final String LINK = "Link";
	public static final String PRINTER_MARK = "PrinterMark";
	public static final String WIDGET = "Widget";
	public static final String TRAP_NET = "TrapNet";
	public static final String TYPE_3D = "3D";
	public static final String BM = "BM";

	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;

	private final PDResources pageResources;

	private final boolean isFKeyPresent;

	private final String subtype;
	private final String ap;
	private final int annotationFlag;
	private final Double ca;
	private final String nType;
	private final String ft;
	private final Double width;
	private final Double height;

	private final PDDocument document;
	private final PDFAFlavour flavour;
	private final PDPage pdPage;

	private List<PDContentStream> appearance = null;
	private List<CosBM> blendMode = null;
	private boolean containsTransparency = false;

	public PBoxPDAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour, String type, PDPage pdPage) {
		super(annot, type);
		this.pageResources = pageResources;
		this.subtype = annot.getSubtype();
		this.ap = getAP(annot);

		COSDictionary annotDict = annot.getCOSObject();
		this.isFKeyPresent = annotDict.containsKey(COSName.F);

		this.annotationFlag = annot.getAnnotationFlags();
		this.ca = PBoxPDAnnot.getCA(annot);
		this.nType = getN_type(annot);
		this.ft = PBoxPDAnnot.getFT(annot);
		this.width = PBoxPDAnnot.getWidth(annot);
		this.height = PBoxPDAnnot.getHeight(annot);
		this.document = document;
		this.flavour = flavour;
		this.pdPage = pdPage;
	}

	public PBoxPDAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour, PDPage pdPage) {
		this(annot, pageResources, document, flavour, ANNOTATION_TYPE, pdPage);
	}

	private static String getAP(PDAnnotation annot) {
		COSBase apLocal = annot.getCOSObject().getDictionaryObject(COSName.AP);
		if (apLocal != null && apLocal instanceof COSDictionary) {
			StringBuilder result = new StringBuilder();
			for (COSName key : ((COSDictionary) apLocal).keySet()) {
				result.append(key.getName());
				result.append(' ');
			}
			// remove last whitespace character
			return result.length() <= 0 ? result.toString() : result.substring(0, result.length() - 1);
		}
		return null;
	}

	private static Double getCA(PDAnnotation annot) {
		COSBase caLocal = annot.getCOSObject().getDictionaryObject(COSName.CA);
		return !(caLocal instanceof COSNumber) ? null : Double.valueOf(((COSNumber) caLocal).doubleValue());
	}

	private static String getN_type(PDAnnotation annot) {
		PDAppearanceDictionary appearanceDictionary = annot.getAppearance();
		if (appearanceDictionary != null) {
			PDAppearanceEntry normalAppearance = appearanceDictionary.getNormalAppearance();
			if (normalAppearance == null) {
				return null;
			} else if (normalAppearance.isSubDictionary()) {
				return DICT;
			} else {
				return STREAM;
			}
		}
		return null;
	}

	private static String getFT(PDAnnotation annot) {
		Set<COSObjectKey> visitedKeys = new HashSet<>();
		COSBase curr = annot.getCOSObject();
		while (curr instanceof COSDictionary) {
			COSDictionary currDict = (COSDictionary) curr;
			COSObjectKey key = currDict.getKey();
			if (key != null) {
				if (visitedKeys.contains(key)) {
					return null;
				}
				visitedKeys.add(key);
			}
			if (currDict.containsKey(COSName.FT)) {
				return currDict.getNameAsString(COSName.FT);
			}
			curr = currDict.getItem(COSName.PARENT);
			if (curr instanceof COSObject) {
				curr = ((COSObject) curr).getObject();
			}
		}
		return null;
	}

	private static Double getWidth(PDAnnotation annot) {
		return PBoxPDAnnot.getDifference(annot, X_AXIS);
	}

	private static Double getHeight(PDAnnotation annot) {
		return PBoxPDAnnot.getDifference(annot, Y_AXIS);
	}

	private static Double getDifference(PDAnnotation annot, int shift) {
		COSBase array = annot.getCOSObject().getDictionaryObject(COSName.RECT);
		if (array instanceof COSArray && ((COSArray) array).size() == 4) {
			COSBase less = ((COSArray) array).getObject(shift);
			COSBase great = ((COSArray) array).getObject(2 + shift);
			if (less instanceof COSNumber && great instanceof COSNumber) {
				return Double.valueOf(((COSNumber) great).doubleValue() - ((COSNumber) less).doubleValue());
			}
		}
		return null;
	}

	public PDResources getPageResources() {
		return pageResources;
	}

	@Override
	public String getSubtype() {
		return this.subtype;
	}

	@Override
	public String getAP() {
		return this.ap;
	}

	@Override
	public Long getF() {
		return isFKeyPresent ? Long.valueOf(this.annotationFlag) : null;
	}

	@Override
	public Double getCA() {
		return this.ca;
	}

	@Override
	public String getN_type() {
		return this.nType;
	}

	@Override
	public String getFT() {
		return this.ft;
	}

	@Override
	public Double getwidth() {
		return this.width;
	}

	@Override
	public Double getheight() {
		return this.height;
	}

	@Override
	public Boolean getcontainsAA() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.AA);
	}

	@Override
	public String getstructParentType() {
		PDStructureTreeRoot structTreeRoot = this.document.getDocumentCatalog().getStructureTreeRoot();
		int structParent = ((PDAnnotation)this.simplePDObject).getStructParent();
		if (structTreeRoot != null) {
			PDNumberTreeNode parentTreeRoot = structTreeRoot.getParentTree();
			COSBase structureElement = null;
			try {
				PDParentTreeValue treeValue = (PDParentTreeValue)parentTreeRoot.getValue(structParent);
				if (treeValue != null) {
					structureElement = treeValue.getCOSObject();
				}
			} catch (IOException var6) {
				return null;
			}
			if (structureElement != null && structureElement instanceof COSDictionary) {
				return ((COSDictionary)structureElement).getNameAsString(COSName.S);
			}
		}
		return null;
	}

	private List<CosLang> getLang() {
		PDStructureTreeRoot structTreeRoot = this.document.getDocumentCatalog().getStructureTreeRoot();
		int structParent = ((PDAnnotation) this.simplePDObject).getStructParent();
		if (structTreeRoot != null && structParent != 0) {
			PDNumberTreeNode parentTreeRoot = structTreeRoot.getParentTree();
			COSBase structureElement;
			try {
				PDParentTreeValue treeValue = parentTreeRoot == null ? null : (PDParentTreeValue) parentTreeRoot.getValue(structParent);
				structureElement = treeValue == null ? null : treeValue.getCOSObject();
			} catch (IOException e) {
				return Collections.emptyList();
			}
			if (structureElement instanceof COSDictionary) {
				String lang = ((COSDictionary) structureElement).getNameAsString(COSName.LANG);
				if (lang != null) {
					List<CosLang> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					list.add(new PBCosLang(new COSString(lang)));
					return Collections.unmodifiableList(list);
				}
			}
		}
		return Collections.emptyList();
	}

	@Override
	public String getContents() {
		return ((PDAnnotation) simplePDObject).getContents();
	}

	@Override
	public String getAlt() {
		PDStructureTreeRoot structTreeRoot = this.document.getDocumentCatalog().getStructureTreeRoot();
		int structParent = ((PDAnnotation) this.simplePDObject).getStructParent();
		if (structTreeRoot != null && structParent != 0) {
			PDNumberTreeNode parentTreeRoot = structTreeRoot.getParentTree();
			COSBase structureElement;
			try {
				PDParentTreeValue treeValue = parentTreeRoot == null ? null : (PDParentTreeValue) parentTreeRoot.getValue(structParent);
				structureElement = treeValue == null ? null : treeValue.getCOSObject();
			} catch (IOException e) {
				return null;
			}
			if (structureElement instanceof COSDictionary) {
				return ((COSDictionary) structureElement).getNameAsString(COSName.ALT);
			}
		}
		return null;
	}

	@Override
	public Boolean getisOutsideCropBox() {
		PDRectangle cropBox = pdPage.getCropBox();
		PDRectangle rectangle = ((PDAnnotation) simplePDObject).getRectangle();
		if (rectangle != null) {
			return cropBox.getLowerLeftY() >= rectangle.getUpperRightY() || cropBox.getLowerLeftX() >= rectangle.getUpperRightX()
			       || cropBox.getUpperRightY() <= rectangle.getLowerLeftY() || cropBox.getUpperRightX() <= rectangle.getLowerLeftX();
		}
		return null;
	}

	@Override
	public String getkeys() {
		return ((COSDictionary)simplePDObject).keySet().stream()
				.map(COSName::getName)
				.collect(Collectors.joining("&"));
	}

	@Override
	public Boolean getcontainsA() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.A);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case ADDITIONAL_ACTION:
			return this.getAdditionalActions();
		case A:
			return this.getA();
		case IC:
			return this.getIC();
		case C:
			return this.getC();
		case APPEARANCE:
			return this.getAppearance();
		case LANG:
			return this.getLang();
		case BM:
			return this.getBM();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<CosBM> getBM() {
		if (this.blendMode == null) {
			this.blendMode = parseBM();
		}
		return this.blendMode;
	}

	private List<CosBM> parseBM() {
		COSBase BM = ((COSDictionary)simplePDObject.getCOSObject()).getDictionaryObject(COSName.BM);
		if (BM == null || flavour == null || flavour.getPart() != PDFAFlavour.Specification.ISO_19005_4) {
			return Collections.emptyList();
		}
		if (BM instanceof COSName) {
			this.containsTransparency = true;
			List<CosBM> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosBM((COSName)BM));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	protected List<PDAdditionalActions> getAdditionalActions() {
		COSBase actionDictionary = ((PDAnnotation) simplePDObject).getCOSObject().getDictionaryObject(COSName.AA);
		if (actionDictionary instanceof COSDictionary && ((COSDictionary) actionDictionary).size() != 0) {
			List<PDAdditionalActions> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			PDAnnotationAdditionalActions additionalActions = new PDAnnotationAdditionalActions(
					(COSDictionary) actionDictionary);
			actions.add(new PBoxPDAnnotationAdditionalActions(additionalActions));
			return Collections.unmodifiableList(actions);
		}
		return Collections.emptyList();
	}

	private List<PDAction> getA() {
		COSBase actionDictionary = ((PDAnnotation) this.simplePDObject).getCOSObject().getDictionaryObject(COSName.A);
		if (actionDictionary instanceof COSDictionary) {
			org.apache.pdfbox.pdmodel.interactive.action.PDAction action = PDActionFactory
					.createAction((COSDictionary) actionDictionary);
			PDAction result = PBoxPDAction.getAction(action);
			if (result != null) {
				List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				actions.add(result);
				return Collections.unmodifiableList(actions);
			}
		}
		return Collections.emptyList();
	}

	private List<CosNumber> getIC() {
		return this.getNumbersFromArray(COSName.IC);
	}

	private List<CosNumber> getC() {
		return this.getNumbersFromArray(COSName.C);
	}

	private List<CosNumber> getNumbersFromArray(COSName arrayName) {
		COSBase colorArray = ((PDAnnotation) this.simplePDObject).getCOSObject().getDictionaryObject(arrayName);
		if (colorArray instanceof COSArray) {
			List<CosNumber> color = new ArrayList<>(((COSArray) colorArray).size());
			for (COSBase colorValue : (COSArray) colorArray) {
				if (colorValue instanceof COSNumber) {
					color.add(PBCosNumber.fromPDFBoxNumber(colorValue));
				}
			}
			return Collections.unmodifiableList(color);
		}
		return Collections.emptyList();
	}

	/**
	 * @return normal appearance stream (N key in the appearance dictionary) of
	 *         the annotation
	 */
	private List<PDContentStream> getAppearance() {
		if (this.appearance == null) {
			parseAppearance();
		}
		return this.appearance;
	}

	boolean isContainsTransparency() {
		if (this.appearance == null) {
			parseAppearance();
		}
		if (this.blendMode == null) {
			this.blendMode = parseBM();
		}
		return this.containsTransparency;
	}

	private void parseAppearance() {
		PDAppearanceDictionary appearanceDictionary = ((PDAnnotation) this.simplePDObject).getAppearance();
		if (appearanceDictionary != null) {
			COSDictionary dictionary = appearanceDictionary.getCOSObject();
			COSBase normalAppearanceBase = dictionary.getDictionaryObject(COSName.N);
			COSBase downAppearanceBase = dictionary.getDictionaryObject(COSName.D);
			COSBase rolloverAppearanceBase = dictionary.getDictionaryObject(COSName.R);
			if (normalAppearanceBase != null || downAppearanceBase != null || rolloverAppearanceBase != null) {
				List<PDContentStream> appearances = new ArrayList<>();
				addContentStreamsFromAppearanceEntry(normalAppearanceBase, appearances);
				addContentStreamsFromAppearanceEntry(downAppearanceBase, appearances);
				addContentStreamsFromAppearanceEntry(rolloverAppearanceBase, appearances);
				this.appearance = Collections.unmodifiableList(appearances);
			} else {
				this.appearance = Collections.emptyList();
			}
		} else {
			this.appearance = Collections.emptyList();
		}
	}

	private void addContentStreamsFromAppearanceEntry(COSBase appearanceEntry, List<PDContentStream> appearances) {
		if (appearanceEntry != null) {
			PDAppearanceEntry newAppearance = new PDAppearanceEntry(appearanceEntry);
			if (newAppearance.isStream()) {
				addAppearance(appearances, newAppearance.getAppearanceStream());
			} else {
				Map<COSName, PDAppearanceStream> subDictionary = newAppearance.getSubDictionary();
				for (PDAppearanceStream stream : subDictionary.values()) {
					addAppearance(appearances, stream);
				}
			}
		}
	}

	private void addAppearance(List<PDContentStream> list, PDAppearanceStream toAdd) {
		if (toAdd != null) {
			PDInheritableResources resources = PDInheritableResources.getInstance(this.pageResources,
					toAdd.getResources());
			PBoxPDContentStream stream = new PBoxPDContentStream(toAdd, resources, this.document, this.flavour);
			this.containsTransparency |= stream.isContainsTransparency();
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup group = toAdd.getGroup();
			this.containsTransparency |= group != null && COSName.TRANSPARENCY.equals(group.getSubType());
			list.add(stream);
		}
	}

	public static PBoxPDAnnot createAnnot(PDAnnotation annot, PDResources pageResources, PDDocument document, PDFAFlavour flavour, PDPage pdPage) {
		String subtype = annot.getSubtype();
		if (subtype == null) {
			return new PBoxPDAnnot(annot, pageResources, document, flavour, pdPage);
		}
		switch (subtype) {
			case WIDGET:
				return new PBoxPDWidgetAnnot(annot, pageResources, document, flavour, pdPage);
			case TYPE_3D:
				return new PBoxPD3DAnnot(annot, pageResources, document, flavour, pdPage);
			case TRAP_NET:
				return new PBoxPDTrapNetAnnot(annot, pageResources, document, flavour, pdPage);
			case LINK:
				return new PBoxPDLinkAnnot(annot, pageResources, document, flavour, pdPage);
			case PRINTER_MARK:
				return new PBoxPDPrinterMarkAnnot(annot, pageResources, document, flavour, pdPage);
			default:
				return new PBoxPDAnnot(annot, pageResources, document, flavour, pdPage);
		}
	}
}
