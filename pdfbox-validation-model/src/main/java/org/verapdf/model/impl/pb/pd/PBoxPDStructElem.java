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

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosActualText;
import org.verapdf.model.coslayer.CosLang;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.impl.pb.cos.PBCosActualText;
import org.verapdf.model.impl.pb.cos.PBCosLang;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDStructElem;
import org.verapdf.model.impl.pb.exceptions.LoopedException;
import org.verapdf.model.tools.TaggedPDFHelper;
import org.verapdf.model.tools.TaggedPDFRoleMapHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Current class is representation of node of structure tree root. Implemented
 * by Apache PDFBox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDStructElem extends PBoxPDObject implements PDStructElem {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDStructElem.class.getCanonicalName());

	/**
	 * Type name for {@code PBoxPDStructElem}
	 */
	public static final String STRUCTURE_ELEMENT_TYPE = "PDStructElem";

	/**
	 * Link name for {@code K} key
	 */
	public static final String CHILDREN = "K";
	/**
	 * Link name for {@code S} key
	 */
	public static final String STRUCTURE_TYPE = "S";
	/**
	 * Link name for {@code Lang} key
	 */
	public static final String LANG = "Lang";
	/**
	 * Link name for {@code ActualText} key
	 */
	public static final String ACTUAL_TEXT = "actualText";
	public static final String ALT = "alt";

	private TaggedPDFRoleMapHelper roleMapHelper;

	/**
	 * Default constructor
	 *
	 * @param structElemDictionary
	 *            dictionary of structure element
	 */
	public PBoxPDStructElem(COSDictionary structElemDictionary, TaggedPDFRoleMapHelper roleMapHelper, String type) {
		super(structElemDictionary, type);
		this.roleMapHelper = roleMapHelper;
	}

	/**
	 * @return Type entry of current structure element
	 */
	@Override
	public String getType() {
		COSBase value = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.TYPE);
		if (value instanceof COSName) {
			return ((COSName) value).getName();
		}
		LOGGER.log(java.util.logging.Level.INFO, "In struct element type expected 'COSName' but got: " + value.getClass().getSimpleName());
		return null;
	}

	@Override
	public String getkidsStandardTypes() {
		return this.getChildrenStandardTypes()
		           .stream()
		           .filter(Objects::nonNull)
		           .collect(Collectors.joining("&"));
	}

	@Override
	public String getparentStandardType() {
		COSBase parent = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.P);
		if (parent != null) {
			COSBase type = ((COSDictionary) parent).getDictionaryObject(COSName.S);
			if (type instanceof COSName) {
				return this.roleMapHelper.getStandardType(((COSName) type).getName());
			}
		}
		return null;
	}

	@Override
	public Boolean gethasContentItems() {
		COSBase children = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.K);
		if (children != null) {
			if (isContentItem(children)) {
				return true;
			}
			if (children instanceof COSArray && ((COSArray) children).size() > 0) {
				for (int i = 0; i < ((COSArray) children).size(); ++i) {
					if (isContentItem(((COSArray) children).get(i))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isContentItem(COSBase base) {
		if (base instanceof COSInteger) {
			return true;
		} else if (base instanceof COSDictionary) {
			COSName type = ((COSDictionary) base).getCOSName(COSName.TYPE);
			return type != null && (type.equals(COSName.getPDFName("MCR")) || type.equals(COSName.getPDFName("OBJR")));
		}
		return false;
	}

	@Override
	public String getstandardType() {
		COSBase type = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			return this.roleMapHelper.getStandardType(((COSName) type).getName());
		}
		return null;
	}

	@Override
	public String getparentLang() {
		COSString baseLang = null;
		Set<COSObjectKey> keys = new HashSet<>();
		COSObjectKey key;
		COSDictionary parentDict = (COSDictionary)((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.P);
		PDStructureNode structureParent = PDStructureNode.create(parentDict);
		PDStructureElement parent = null;
		if (structureParent instanceof PDStructureElement) {
			parent = (PDStructureElement) structureParent;
		}
		while (baseLang == null && parent != null) {
			key = parent.getCOSObject().getKey();
			if (keys.contains(key)){
				throw new LoopedException("Struct tree loop found");
			}
			if (key != null) {
				keys.add(key);
			}
			baseLang = (COSString) parent.getCOSObject().getDictionaryObject(COSName.LANG);
			structureParent = parent.getParent();
			if (structureParent instanceof PDStructureElement) {
				parent = (PDStructureElement) structureParent;
			} else {
				parent = null;
			}
		}
		if (baseLang != null) {
			return baseLang.getString();
		}
		return null;
	}

	public static String getStructureElementStandardType(COSDictionary pdStructElem,
														 TaggedPDFRoleMapHelper roleMapHelper){
		COSBase type = pdStructElem.getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			return roleMapHelper.getStandardType(((COSName) type).getName());
		}
		return null;
	}

	@Override
	public Boolean getisRemappedStandardType() {
		COSBase type = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			return this.roleMapHelper.isRemappedStandardType(((COSName) type).getName());
		}
		return null;
	}

	@Override
	public String getAlt() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		if (pageObject != null && pageObject instanceof COSDictionary) {
			return ((COSDictionary) pageObject).getNameAsString(COSName.ALT);
		}
		return null;
	}

	@Override
	public String getActualText() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		if (pageObject != null && pageObject instanceof COSDictionary) {
			return ((COSDictionary) pageObject).getNameAsString(COSName.ACTUAL_TEXT);
		}
		return null;
	}

	@Override
	public String getE() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		if (pageObject != null && pageObject instanceof COSDictionary) {
			return ((COSDictionary) pageObject).getNameAsString(COSName.E);
		}
		return null;
	}

	@Override
	public Boolean getcircularMappingExist() {
		COSBase type = ((COSDictionary)this.simplePDObject).getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			return this.roleMapHelper.circularMappingExist(((COSName) type).getName());
		}
		return null;
	}

	@Override
	public String getroleMapToSameNamespaceTag() {
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case CHILDREN:
			return this.getChildren();
		case STRUCTURE_TYPE:
			return this.getStructureType();
		case LANG:
			return this.getLang();
		case ACTUAL_TEXT:
			return this.getactualText(); 
		case ALT:
			return Collections.emptyList();			
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<String> getChildrenStandardTypes() {
		return TaggedPDFHelper.getStructElemChildrenStandardTypes((COSDictionary) this.simplePDObject, this.roleMapHelper);
	}

	public List<PDStructElem> getChildren() {
		return TaggedPDFHelper.getStructElemChildren((COSDictionary) this.simplePDObject, this.roleMapHelper);
	}

	@Override
	public String getvalueS() {
		COSBase type = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.S);
		return type instanceof COSName ? ((COSName)type).getName() : null;
	}

	private List<CosUnicodeName> getStructureType() {
		COSBase type = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.S);
		if (type instanceof COSName) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosUnicodeName((COSName) type));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<CosLang> getLang() {
		COSBase baseLang = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.LANG);
		if (baseLang instanceof COSString) {
			List<CosLang> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosLang((COSString) baseLang));
			return Collections.unmodifiableList(list);
		}

		return Collections.emptyList();
	}

	private List<CosActualText> getactualText() {
		COSBase object = this.simplePDObject.getCOSObject();
		if (object != null && object instanceof COSDictionary) {
			COSBase actualText = ((COSDictionary) object).getItem(COSName.ACTUAL_TEXT);
			if (actualText != null && actualText instanceof COSString) {
				List<CosActualText> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosActualText((COSString)actualText));
				return list;
			}
		}
		return Collections.emptyList();
	}

}
