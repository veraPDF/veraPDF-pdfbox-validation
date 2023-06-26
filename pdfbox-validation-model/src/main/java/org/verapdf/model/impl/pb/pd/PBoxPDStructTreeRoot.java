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

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDStructElem;
import org.verapdf.model.pdlayer.PDStructTreeRoot;
import org.verapdf.model.tools.TaggedPDFHelper;
import org.verapdf.model.tools.TaggedPDFRoleMapHelper;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Current class is representation of PDF`s logical structure facilities.
 * Implemented by Apache PDFBox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDStructTreeRoot extends PBoxPDObject implements PDStructTreeRoot {

	/** Type name for {@code PBoxPDStructTreeRoot} */
	public static final String STRUCT_TREE_ROOT_TYPE = "PDStructTreeRoot";

	/** Link name for {@code K} key */
	public static final String CHILDREN = "K";

	/** Link name for {@code roleMapNames} key */
	public static final String ROLE_MAP_NAMES = "roleMapNames";

	private List<PDStructElem> children = null;

	private PDFAFlavour flavour;

	/**
	 * Default constructor
	 *
	 * @param treeRoot
	 *            structure tree root implementation
	 */
	public PBoxPDStructTreeRoot(PDStructureTreeRoot treeRoot, PDFAFlavour flavour) {
		super(treeRoot, STRUCT_TREE_ROOT_TYPE);
		this.flavour = flavour;
	}

	@Override
	public String getkidsStandardTypes() {
		return this.getChildren()
		           .stream()
		           .map(PDStructElem::getstandardType)
		           .filter(Objects::nonNull)
		           .collect(Collectors.joining("&"));
	}

	@Override
	public Boolean gethasContentItems() {
		COSBase children = ((PDStructureTreeRoot) this.simplePDObject).getK();
		if (children != null) {
			if (PBoxPDStructElem.isContentItem(children)) {
				return true;
			}
			if (children instanceof COSArray && ((COSArray) children).size() > 0) {
				for (int i = 0; i < ((COSArray) children).size(); ++i) {
					if (PBoxPDStructElem.isContentItem(((COSArray) children).get(i))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case CHILDREN:
				return this.getChildren();
			case ROLE_MAP_NAMES:
				return getRoleMapNames();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDStructElem> getChildren() {
		if (this.children == null) {
			this.children = parseChildren();
		}
		return this.children;
	}

	private List<PDStructElem> parseChildren() {
		COSDictionary parent = ((PDStructureTreeRoot) this.simplePDObject).getCOSObject();
		return TaggedPDFHelper.getStructTreeRootChildren(parent,
		                                                 new TaggedPDFRoleMapHelper(getRoleMap(), this.flavour));
	}

	private Map<String, String> getRoleMap() {
		Map<String, java.lang.Object> tempMap = ((PDStructureTreeRoot) this.simplePDObject).getRoleMap();
		Map<String, String> resMap = new HashMap<>();
		for (Map.Entry<String, java.lang.Object> entry : tempMap.entrySet()) {
			resMap.put(entry.getKey(), entry.getValue().toString());
		}
		return resMap;
	}

	private List<CosUnicodeName> getRoleMapNames() {
		Map<String, java.lang.Object> tempMap = ((PDStructureTreeRoot) this.simplePDObject).getRoleMap();
		if (tempMap != null) {
			List<CosUnicodeName> res = new ArrayList<>();
			for (Map.Entry<String, java.lang.Object> entry : tempMap.entrySet()) {
				res.add(new PBCosUnicodeName(COSName.getPDFName(entry.getKey())));
				res.add(new PBCosUnicodeName(COSName.getPDFName(entry.getValue().toString())));
			}
			return res;
		}
		return Collections.emptyList();
	}

	@Override
	public String gettopLevelFirstElementStandardType() {
		if (this.children == null) {
			this.children = parseChildren();
		}

		if (!this.children.isEmpty()) {
			return this.children.get(0).getstandardType();
		}
		return null;
	}
}
