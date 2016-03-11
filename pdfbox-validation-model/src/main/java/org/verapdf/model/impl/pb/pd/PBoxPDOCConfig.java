package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.pdlayer.PDOCConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
		this.groupNames = null;
		this.duplicateName = false;
	}

	public PBoxPDOCConfig(COSObjectable simplePDObject, List<String> groupNames, boolean duplicateName) {
		super(simplePDObject, OC_CONFIG_TYPE);
		this.groupNames = groupNames;
		this.duplicateName = duplicateName;
	}

	@Override
	public Boolean getdoesOrderContainAllOCGs() {
		COSBase order = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.ORDER);
		if (order != null) {
			if (order instanceof COSArray) {
				int groupsInOrder = 0;
				for (int i = 0; i < ((COSArray) order).size(); i++) {
					COSBase element = ((COSArray) order).getObject(i);
					if (element instanceof COSArray) {
						groupsInOrder += ((COSArray) element).size();
						if (!checkCOSArrayInOrder((COSArray) element)) {
							return Boolean.FALSE;
						}
					} else if (element instanceof COSString) {
						groupsInOrder++;
						if (!checkCOSStringInOrder((COSString) element)) {
							return Boolean.FALSE;
						}
					} else if (element instanceof COSDictionary) {
						groupsInOrder++;
						if (!checkCOSDictionaryInOrder((COSDictionary) element)) {
							return Boolean.FALSE;
						}
					} else {
						LOGGER.warn("Invalid object type in order array. Ignoring the object.");
					}
				}
				if (groupsInOrder < groupNames.size()) {
					return Boolean.FALSE;
				}
			} else {
				LOGGER.warn("Invalid object type of Order entry. Ignoring the Order entry.");
			}
		} else {
			if (groupNames.size() > 0) {
				return Boolean.FALSE;
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
						LOGGER.warn("Invalid object type in the AS array. Ignoring the object.");
					}
				}
				return result;
			} else {
				LOGGER.warn("Invalid object type of AS entry. Ignoring the entry.");
				return result;
			}
		} else {
			return null;
		}
	}

	@Override
	public Boolean gethasDuplicateName() {
		return this.duplicateName;
	}

	@Override
	public String getName() {
		return ((COSDictionary) this.simplePDObject).getString(COSName.NAME);
	}

	private Boolean checkCOSArrayInOrder(COSArray array) {
		for (int i = 0; i < array.size(); i++) {
			COSBase element = array.getObject(i);
			if (element instanceof COSString) {
				if (!checkCOSStringInOrder((COSString) element)) {
					return Boolean.FALSE;
				}
			} else if (element instanceof COSDictionary) {
				if (!checkCOSDictionaryInOrder((COSDictionary) element)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	private Boolean checkCOSStringInOrder(COSString element) {
		if (!groupNames.contains((element).getString())) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private Boolean checkCOSDictionaryInOrder(COSDictionary element) {
		if (!groupNames.contains(element.getString(COSName.NAME))) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

}
