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
package org.verapdf.model.tools;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.pd.PBoxPDStructElem;
import org.verapdf.model.impl.pb.pd.pboxse.PBoxSEGeneral;
import org.verapdf.model.pdlayer.PDStructElem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is utility class for tagged pdf
 *
 * @author Evgeniy Muravitskiy
 */
public class TaggedPDFHelper {

	private static final int MAX_NUMBER_OF_ELEMENTS = 1;

	private TaggedPDFHelper() {
		// disable default constructor
	}

	public static List<PDStructElem> getStructTreeRootChildren(COSDictionary parent,
															   TaggedPDFRoleMapHelper roleMapHelper) {
		return getChildren(parent, roleMapHelper, false);
	}

	public static List<PDStructElem> getStructElemChildren(COSDictionary parent,
														   TaggedPDFRoleMapHelper roleMapHelper) {
		return getChildren(parent, roleMapHelper, true);
	}

	public static List<String> getStructElemChildrenStandardTypes(COSDictionary parent,
														   TaggedPDFRoleMapHelper roleMapHelper) {
		return getChildrenStandardTypes(parent, roleMapHelper, true);
	}

	/**
	 * Get all structure elements for current dictionary
	 *
	 * @param parent parent dictionary
	 * @return list of structure elements
	 */
	private static List<PDStructElem> getChildren(COSDictionary parent, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		COSBase children = parent.getDictionaryObject(COSName.K);
		if (children != null) {
			if (children instanceof COSDictionary && isStructElem((COSDictionary) children, checkType)) {
				List<PDStructElem> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(PBoxSEGeneral.createTypedStructElem((COSDictionary) children, roleMapHelper));
				return Collections.unmodifiableList(list);
			} else if (children instanceof COSArray) {
				return getChildrenFromArray((COSArray) children, roleMapHelper, checkType);
			}
		}
		return Collections.emptyList();
	}

	private static List<String> getChildrenStandardTypes(COSDictionary parent, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		COSBase children = parent.getDictionaryObject(COSName.K);
		if (children != null) {
			if (children instanceof COSDictionary && isStructElem((COSDictionary) children, checkType)) {
				List<String> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(PBoxPDStructElem.getStructureElementStandardType((COSDictionary) children, roleMapHelper));
				return Collections.unmodifiableList(list);
			} else if (children instanceof COSArray) {
				return getChildrenStandardTypesFromArray((COSArray) children, roleMapHelper, checkType);
			}
		}
		return Collections.emptyList();
	}
	/**
	 * Transform array of dictionaries to list of structure elements
	 *
	 * @param children array of children structure elements
	 * @return list of structure elements
	 */
	private static List<PDStructElem> getChildrenFromArray(COSArray children, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		if (children.size() > 0) {
			List<PDStructElem> list = new ArrayList<>();
			for (COSBase element : children) {
				COSBase directElem = element;
				if (directElem instanceof COSObject) {
					directElem = ((COSObject) directElem).getObject();
				}
				if (directElem instanceof COSDictionary && isStructElem((COSDictionary) directElem, checkType)) {
					list.add(PBoxSEGeneral.createTypedStructElem((COSDictionary) directElem, roleMapHelper));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private static List<String> getChildrenStandardTypesFromArray(COSArray children, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		if (children.size() > 0) {
			List<String> list = new ArrayList<>();
			for (COSBase element : children) {
				COSBase directElem = element;
				if (directElem instanceof COSObject) {
					directElem = ((COSObject) directElem).getObject();
				}
				if (directElem instanceof COSDictionary && isStructElem((COSDictionary) directElem, checkType)) {
					list.add(PBoxPDStructElem.getStructureElementStandardType((COSDictionary) directElem, roleMapHelper));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private static boolean isStructElem(COSDictionary dictionary, boolean checkType) {
		if (dictionary == null) {
			return false;
		}
		COSName type = dictionary.getCOSName(COSName.TYPE);
		return !checkType || type == null || type.equals(COSName.getPDFName("StructElem"));
	}
}
