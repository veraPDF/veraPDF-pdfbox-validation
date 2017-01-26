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
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.cos.PBCosObject;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;
import org.verapdf.model.tools.IDGenerator;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntent extends PBoxPDObject implements PDOutputIntent {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDOutputIntent.class);

	public static final String OUTPUT_INTENT_TYPE = "PDOutputIntent";

	public static final String DEST_PROFILE = "destProfile";
	public static final String DEST_OUTPUT_PROFILE_REF = "DestOutputProfileRef";

	private final String destOutputProfileIndirect;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	public PBoxPDOutputIntent(org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent simplePDObject,
			PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, OUTPUT_INTENT_TYPE);
		this.destOutputProfileIndirect = PBoxPDOutputIntent.getDestOutputProfileIndirect(simplePDObject);
		this.document = document;
		this.flavour = flavour;
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
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case DEST_PROFILE:
			return this.getDestProfile();
		case DEST_OUTPUT_PROFILE_REF:
			return this.getDestOutputProfileRef();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<ICCOutputProfile> getDestProfile() {
		COSBase dict = this.simplePDObject.getCOSObject();
		String subType = null;
		if (dict instanceof COSDictionary) {
			subType = ((COSDictionary) dict).getNameAsString(COSName.S);
		}
		try {
			COSStream dest = ((org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent) this.simplePDObject)
					.getDestOutputIntent();
			if (dest != null) {
				return getDestProfilesFromStream(dest, subType);
			}
		} catch (IOException e) {
			LOGGER.debug("Can not read dest output profile. " + e.getMessage(),
					e);
		}
		return Collections.emptyList();
	}

	private static List<ICCOutputProfile> getDestProfilesFromStream(COSStream destStream, final String subType) throws IOException {
		List<ICCOutputProfile> profile = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        profile.add(new PBoxICCOutputProfile(destStream, subType));
		return Collections.unmodifiableList(profile);
	}

	private List<CosObject> getDestOutputProfileRef() {
		COSDictionary dict = (COSDictionary) this.simplePDObject.getCOSObject();
		COSBase ref = dict.getDictionaryObject(COSName.getPDFName(DEST_OUTPUT_PROFILE_REF));
		CosObject value = PBCosObject.getFromValue(ref, this.document, this.flavour);
		if (value != null) {
			ArrayList<CosObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(value);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
