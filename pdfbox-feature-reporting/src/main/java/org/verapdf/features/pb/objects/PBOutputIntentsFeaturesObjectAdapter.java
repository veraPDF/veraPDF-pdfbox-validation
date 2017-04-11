/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.features.objects.OutputIntentFeaturesObjectAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Feature object adapter for output intents
 *
 * @author Maksim Bezrukov
 */
public class PBOutputIntentsFeaturesObjectAdapter implements OutputIntentFeaturesObjectAdapter {

	private PDOutputIntent outInt;
	private String iccProfileID;
	private String subtype;
	private List<String> errors;

	/**
	 * Constructs new OutputIntent Feature Object adapter
	 *
	 * @param outInt       pdfbox class represents OutputIntent object
	 * @param iccProfileID id of the icc profile which use in this outputIntent
	 */
	public PBOutputIntentsFeaturesObjectAdapter(PDOutputIntent outInt, String iccProfileID) {
		this.outInt = outInt;
		this.iccProfileID = iccProfileID;
		if (this.outInt != null) {
			COSBase base = this.outInt.getCOSObject();
			if (base instanceof COSDictionary) {
				COSDictionary dict = (COSDictionary) base;
				COSBase baseType = dict.getDictionaryObject(COSName.S);
				while (baseType instanceof COSObject) {
					baseType = ((COSObject) baseType).getObject();
				}
				if (baseType != null) {
					if (baseType instanceof COSName) {
						this.subtype = ((COSName) baseType).getName();
					} else {
						this.errors = new ArrayList<>();
						this.errors.add("Subtype is not of Name type");
					}
				}
			}
		}
	}

	@Override
	public String getICCProfileID() {
		return this.iccProfileID;
	}

	@Override
	public String getSubType() {
		return this.subtype;
	}

	@Override
	public String getOutputCondition() {
		if (this.outInt != null) {
			return outInt.getOutputCondition();
		}
		return null;
	}

	@Override
	public String getOutputConditionIdentifier() {
		if (this.outInt != null) {
			return outInt.getOutputConditionIdentifier();
		}
		return null;
	}

	@Override
	public String getRegistryName() {
		if (this.outInt != null) {
			return outInt.getRegistryName();
		}
		return null;
	}

	@Override
	public String getInfo() {
		if (this.outInt != null) {
			return outInt.getInfo();
		}
		return null;
	}

	@Override
	public List<String> getErrors() {
		return this.errors == null ?
				Collections.<String>emptyList() : Collections.unmodifiableList(this.errors);
	}
}
