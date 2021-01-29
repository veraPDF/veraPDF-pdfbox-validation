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
package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCInputProfile;
import org.verapdf.model.impl.pb.external.PBoxICCInputProfile;
import org.verapdf.model.pdlayer.PDICCBased;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ICCBased color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDICCBased extends PBoxPDColorSpace implements PDICCBased {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDICCBased.class);

	public static final String ICC_BASED_TYPE = "PDICCBased";

	public static final String ICC_PROFILE = "iccProfile";

	public PBoxPDICCBased(org.apache.pdfbox.pdmodel.graphics.color.PDICCBased simplePDObject) {
		super(simplePDObject, ICC_BASED_TYPE);
	}

	protected PBoxPDICCBased(org.apache.pdfbox.pdmodel.graphics.color.PDICCBased simplePDObject, String type) {
		super(simplePDObject, type);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (ICC_PROFILE.equals(link)) {
			return this.getICCProfile();
		}
		return super.getLinkedObjects(link);
	}

	private List<ICCInputProfile> getICCProfile() {
		PDStream pdStream = ((org.apache.pdfbox.pdmodel.graphics.color.PDICCBased) this.simplePDObject)
				.getPDStream();
		List<ICCInputProfile> inputProfile = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		inputProfile.add(new PBoxICCInputProfile(pdStream.getStream()));
		return Collections.unmodifiableList(inputProfile);
	}

	@Override
	public String getICCProfileIndirect() {
		return null;
	}

	@Override
	public String getcurrentTransparencyProfileIndirect() {
		return null;
	}

}
