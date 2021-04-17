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

import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.*;

/**
 * @author Maksim Bezrukov
 */
public class TaggedPDFRoleMapHelper {

	private static Set<String> PDF_1_4_STANDARD_ROLE_TYPES;
	private static Set<String> PDF_1_7_STANDARD_ROLE_TYPES;

	static {
		Set<String> tempSet = new HashSet<>();
		// Standard structure types for grouping elements PDF 1.4 and PDF 1.7
		tempSet.add("Document");
		tempSet.add("Part");
		tempSet.add("Art");
		tempSet.add("Sect");
		tempSet.add("Div");
		tempSet.add("BlockQuote");
		tempSet.add("Caption");
		tempSet.add("TOC");
		tempSet.add("TOCI");
		tempSet.add("Index");
		tempSet.add("NonStruct");
		tempSet.add("Private");

		// Standard structure types for paragraphlike elements PDF 1.4 and PDF
		// 1.7
		tempSet.add("H");
		tempSet.add("H1");
		tempSet.add("H2");
		tempSet.add("H3");
		tempSet.add("H4");
		tempSet.add("H5");
		tempSet.add("H6");
		tempSet.add("P");

		// Standard structure types for list elements PDF 1.4 and PDF 1.7
		tempSet.add("L");
		tempSet.add("LI");
		tempSet.add("Lbl");
		tempSet.add("LBody");

		// Standard structure types for table elements PDF 1.4 and PDF 1.7
		tempSet.add("Table");
		tempSet.add("TR");
		tempSet.add("TH");
		tempSet.add("TD");

		// Standard structure types for inline-level structure elements PDF 1.4
		// and PDF 1.7
		tempSet.add("Span");
		tempSet.add("Quote");
		tempSet.add("Note");
		tempSet.add("Reference");
		tempSet.add("BibEntry");
		tempSet.add("Code");
		tempSet.add("Link");

		// Standard structure types for illustration elements PDF 1.4 and PDF
		// 1.7
		tempSet.add("Figure");
		tempSet.add("Formula");
		tempSet.add("Form");

		PDF_1_4_STANDARD_ROLE_TYPES = new HashSet<>(tempSet);

		// Standard structure types for table elements PDF 1.7
		tempSet.add("THead");
		tempSet.add("TBody");
		tempSet.add("TFoot");

		// Standard structure types for inline-level structure elements PDF 1.7
		tempSet.add("Annot");
		tempSet.add("Ruby");
		tempSet.add("Warichu");

		// Standard structure types for Ruby and Warichu elements PDF 1.7
		// Elements "Ruby" and "Warichu" are removed here, because they are
		// already in set
		tempSet.add("RB");
		tempSet.add("RT");
		tempSet.add("RP");
		tempSet.add("WT");
		tempSet.add("WP");

		PDF_1_7_STANDARD_ROLE_TYPES = new HashSet<>(tempSet);
	}

	private Map<String, String> roleMap;
	private PDFAFlavour flavour;

	/**
	 * Creates new TaggedPDFRoleMapHelper
	 * 
	 * @param roleMap
	 *            role map from PDF
	 * @param flavour
	 *            current pdfa flavour
	 */
	public TaggedPDFRoleMapHelper(Map<String, String> roleMap, PDFAFlavour flavour) {
		this.roleMap = roleMap == null ? Collections.<String, String>emptyMap() : new HashMap<>(roleMap);
		this.flavour = flavour;
	}

	/**
	 * Obtains standard type for the given one
	 * 
	 * @param type
	 *            the type for obtaining the standard
	 * @return standard type for the given one or null in cases when there is no
	 *         standard for the given or there is a cycle of the custom types
	 */
	public String getStandardType(String type) {
		if (type == null) {
			return null;
		}
		Set<String> currentStandardTypes;
		boolean isFastStop;
		if (flavour != null && flavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
			currentStandardTypes = PDF_1_4_STANDARD_ROLE_TYPES;
			isFastStop = true;
		} else {
			currentStandardTypes = PDF_1_7_STANDARD_ROLE_TYPES;
			isFastStop = false;
		}
		return getStandardType(type, currentStandardTypes, isFastStop);
	}

	public Boolean isRemappedStandardType(String type) {
		if (type == null) {
			return false;
		}
		Set<String> currentStandardTypes;
		if (flavour != null && flavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
			currentStandardTypes = PDF_1_4_STANDARD_ROLE_TYPES;
		} else {
			currentStandardTypes = PDF_1_7_STANDARD_ROLE_TYPES;
		}
		String res = roleMap.get(type);
		return currentStandardTypes.contains(type) && res != null;
	}

	private String getStandardType(String type, Set<String> currentStandardTypes, boolean isFastStop) {
		Set<String> visitedTypes = new HashSet<>();
		visitedTypes.add(type);
		String res = roleMap.get(type);
		if ((isFastStop || res == null || visitedTypes.contains(res)) && currentStandardTypes.contains(type)) {
			return type;
		}
		while (res != null && !visitedTypes.contains(res)) {
			if (currentStandardTypes.contains(res)) {
				return res;
			}
			visitedTypes.add(res);
			res = roleMap.get(res);
		}
		return null;
	}

	public Boolean circularMappingExist(String type) {
		if (type == null) {
			return null;
		}
		Set<String> visitedTypes = new HashSet<>();
		visitedTypes.add(type);
		String res = roleMap.get(type);
		while (res != null) {
			if (visitedTypes.contains(res)) {
				return true;
			}
			visitedTypes.add(res);
			res = roleMap.get(res);
		}
		return false;
	}
}
