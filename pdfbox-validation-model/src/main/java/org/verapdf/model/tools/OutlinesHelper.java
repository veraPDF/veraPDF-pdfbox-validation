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

import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.impl.pb.pd.PBoxPDOutline;
import org.verapdf.model.pdlayer.PDOutline;

import java.util.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class OutlinesHelper {

	private OutlinesHelper() {
		// disable default constructor
	}

	public static List<PDOutline> getOutlines(PDDocumentCatalog catalog) {
		Map<PDOutlineItem, String> outlines = getOutlinesMap(catalog);
		if (outlines.size() > 0) {
			List<PDOutline> result = new ArrayList<>(outlines.size());
			for (Map.Entry<PDOutlineItem, String> entry : outlines.entrySet()) {
				result.add(new PBoxPDOutline(entry.getKey(), entry.getValue()));
			}
			outlines.clear();
			return Collections.unmodifiableList(result);
		}
			return Collections.emptyList();
	}

	private static Map<PDOutlineItem, String> getOutlinesMap(PDDocumentCatalog catalog) {
		if (catalog != null) {
			PDDocumentOutline documentOutline = catalog.getDocumentOutline();
			if (documentOutline != null) {
				PDOutlineItem firstChild = documentOutline.getFirstChild();
				if (firstChild != null) {
					Deque<PDOutlineItem> stack = new ArrayDeque<>();
					stack.push(firstChild);
					return getOutlinesMap(stack);
				}
			}
		}

		return Collections.emptyMap();
	}

	private static Map<PDOutlineItem, String> getOutlinesMap(Deque<PDOutlineItem> stack) {
		Map<PDOutlineItem, String> result = new HashMap<>();
		do {
			PDOutlineItem item = stack.pop();
			PDOutlineItem nextSibling = item.getNextSibling();
			PDOutlineItem firstChild = item.getFirstChild();
			if (nextSibling != null && !result.containsKey(nextSibling)) {
				stack.add(nextSibling);
			}
			if (firstChild != null && !result.containsKey(firstChild)) {
				stack.add(firstChild);
			}
			result.put(item, IDGenerator.generateID(item));
		} while (!stack.isEmpty());

		return result;
	}

}
