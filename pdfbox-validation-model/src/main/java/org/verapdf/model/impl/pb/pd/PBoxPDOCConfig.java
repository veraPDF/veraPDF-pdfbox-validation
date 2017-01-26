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
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.pdlayer.PDOCConfig;

import java.util.*;

/**
 * @author Timur Kamalov
 */
public class PBoxPDOCConfig extends PBoxPDObject implements PDOCConfig {

	public static final Logger LOGGER = Logger.getLogger(PBoxPDOCConfig.class);

	public static final String OC_CONFIG_TYPE = "PDOCConfig";

	public static final String EVENT_KEY = "Event";

	private final List<String> groupNames;
	private final boolean duplicateName;

	public PBoxPDOCConfig(COSObjectable simplePDObject) {
		super(simplePDObject, OC_CONFIG_TYPE);
		this.groupNames = Collections.emptyList();
		this.duplicateName = false;
	}

	public PBoxPDOCConfig(COSObjectable simplePDObject, List<String> groupNames, boolean duplicateName) {
		super(simplePDObject, OC_CONFIG_TYPE);
		this.groupNames = groupNames == null ? Collections.<String>emptyList() : groupNames;
		this.duplicateName = duplicateName;
	}

	@Override
	public Boolean getdoesOrderContainAllOCGs() {
		Set<String> groupNamesSet = new TreeSet<>(groupNames);
		COSBase order = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.ORDER);
		if (order != null) {
			if (order instanceof COSArray) {
				for (int i = 0; i < ((COSArray) order).size(); i++) {
					COSBase element = ((COSArray) order).getObject(i);
					if (element instanceof COSArray) {
						processCOSArrayInOrder((COSArray) element, groupNamesSet);
					} else if (element instanceof COSDictionary) {
						processCOSDictionaryInOrder((COSDictionary) element, groupNamesSet);
					} else {
						LOGGER.debug("Invalid object type in order array. Ignoring the object.");
					}
				}
				if (!groupNamesSet.isEmpty()) {
					return Boolean.FALSE;
				}
			} else {
				LOGGER.debug("Invalid object type of Order entry. Ignoring the Order entry.");
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public String getAS() {
		COSBase asArray = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.AS);
		if (asArray != null) {
			String result = "";
			if (asArray instanceof COSArray) {
				for (int i = 0; i < ((COSArray) asArray).size(); i++) {
					COSBase element = ((COSArray) asArray).getObject(i);
					if (element instanceof COSDictionary) {
						String event = ((COSDictionary) element).getString(EVENT_KEY);
						if (event != null && !event.isEmpty()) {
							result = result.concat(event);
						}
					} else {
						LOGGER.debug("Invalid object type in the AS array. Ignoring the object.");
					}
				}
				return result;
			}
			LOGGER.debug("Invalid object type of AS entry. Ignoring the entry.");
			return result;
		}
		return null;
	}

	@Override
	public Boolean gethasDuplicateName() {
		return Boolean.valueOf(this.duplicateName);
	}

	@Override
	public String getName() {
		return ((COSDictionary) this.simplePDObject).getString(COSName.NAME);
	}

	private void processCOSArrayInOrder(COSArray array, Set<String> groupNames) {
		for (int i = 0; i < array.size(); i++) {
			COSBase element = array.getObject(i);
			if (element instanceof COSArray) {
				processCOSArrayInOrder((COSArray) element, groupNames);
			} else if (element instanceof COSDictionary) {
				processCOSDictionaryInOrder((COSDictionary) element, groupNames);
			}
		}
	}

	private void processCOSDictionaryInOrder(COSDictionary element, Set<String> groupNames) {
		groupNames.remove(element.getString(COSName.NAME));
	}

	private static int getLenghtOfFlattenArray(COSArray array) {
		int res = 0;
		for (int i = 0; i < array.size(); i++) {
			COSBase element = array.getObject(i);
			if (element instanceof COSArray) {
				res += getLenghtOfFlattenArray((COSArray) element);
			} else {
				res++;
			}
		}
		return res;
	}
}