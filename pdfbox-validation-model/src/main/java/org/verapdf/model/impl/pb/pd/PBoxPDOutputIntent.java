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
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;
import org.verapdf.model.tools.IDGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDOutputIntent.class.getCanonicalName());

	public static final String OUTPUT_INTENT_TYPE = "PDOutputIntent";

	public static final String DEST_PROFILE = "destProfile";

	private final String destOutputProfileIndirect;
	private ICCOutputProfile iccOutputProfile;


	public PBoxPDOutputIntent(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject) {
		super(simplePDObject, OUTPUT_INTENT_TYPE);
		this.destOutputProfileIndirect = PBoxPDOutputIntent.getDestOutputProfileIndirect(simplePDObject);
	}

	private static String getDestOutputProfileIndirect(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent) {
		COSDictionary dictionary = (COSDictionary) intent.getCOSObject();
		COSBase item = dictionary.getItem(COSName.DEST_OUTPUT_PROFILE);
		return IDGenerator.generateID(item);
	}

	@Override
	public String getdestOutputProfileIndirect() {
		return this.destOutputProfileIndirect;
	}

	@Override
	public String getS() {
		COSBase dict = this.simplePDObject.getCOSObject();
		if (dict instanceof COSDictionary) {
			return ((COSDictionary) dict).getNameAsString(COSName.S);
		}
		return null;
	}

	@Override
	public String getICCProfileMD5() {
		return null;
	}

	@Override
	public String getOutputConditionIdentifier() {
		org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent outInt =
				(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) simplePDObject;
		return outInt.getOutputConditionIdentifier();
	}

	@Override
	public Boolean getcontainsDestOutputProfileRef() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.getPDFName("DestOutputProfileRef"));
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case DEST_PROFILE:
			return this.getDestProfile();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private ICCOutputProfile parseDestProfile() {
		COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) this.simplePDObject)
				.getDestOutputIntent();
		if (dest != null) {
			return new PBoxICCOutputProfile(dest, getS());
		}
		return null;
	}

	public String getColorSpace() {
		if (iccOutputProfile == null) {
			iccOutputProfile = parseDestProfile();
		}
		return iccOutputProfile != null && "GTS_PDFA1".equals(getS())? iccOutputProfile.getcolorSpace() : null;
	}

	private List<ICCOutputProfile> getDestProfile() {
		if (iccOutputProfile == null) {
			iccOutputProfile = parseDestProfile();
		}
		if (iccOutputProfile == null) {
			return Collections.emptyList();
		}
		List<ICCOutputProfile> profile = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		profile.add(iccOutputProfile);
		return Collections.unmodifiableList(profile);
	}
}
