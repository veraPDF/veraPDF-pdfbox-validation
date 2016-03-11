package org.verapdf.model.impl.pb.pd;

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

	public static final String OC_CONFIG_TYPE = "PDOCConfig";

	public static final String EVENT_KEY = "Event";

	private final List<String> groupNames;
	private final boolean duplicateName;

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
						// TODO : log exception
					}
				}
				if (groupsInOrder < groupNames.size()) {
					return Boolean.FALSE;
				}
			} else {
				//TODO : log exception
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
		if (asArray != null && asArray instanceof COSArray) {
			String result = new String();
			for (int i = 0; i < ((COSArray) asArray).size(); i++) {
				COSBase element = ((COSArray) asArray).getObject(i);
				if (element instanceof COSDictionary) {
					String event = ((COSDictionary) element).getString(EVENT_KEY);
					if (event != null && !event.isEmpty()) {
						result.concat(event);
					}
				} else {
					//TODO : log exception
				}
			}
			return result;
		} else {
			//TODO : log exception
		}
		return null;
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
