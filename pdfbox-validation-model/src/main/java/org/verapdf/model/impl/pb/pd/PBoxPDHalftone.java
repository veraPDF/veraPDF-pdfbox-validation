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

import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.functions.PBoxPDFunction;
import org.verapdf.model.pdlayer.PDHalftone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDHalftone extends PBoxPDObject implements PDHalftone {

	public static final String HALFTONE_TYPE = "PDHalftone";

	private static final String HALFTONES = "halftones";
	private static final String CUSTOM_FUNCTION = "customFunction";

	private final String halftoneName;
	private final Long halftoneType;
	private final String colorantName;

	public PBoxPDHalftone(COSDictionary dict) {
		this(dict, null);
	}

	public PBoxPDHalftone(COSDictionary dict, String colorantName) {
		super(dict, HALFTONE_TYPE);
		this.halftoneName = PBoxPDHalftone.getHalftoneName(dict);
		this.halftoneType = PBoxPDHalftone.getHalftoneType(dict);
		this.colorantName = colorantName;
	}

	public PBoxPDHalftone(COSName name) {
		super(name, HALFTONE_TYPE);
		boolean isDefault = COSName.getPDFName("Default").equals(name);
		this.halftoneName = isDefault ? null : name.getName();
		this.halftoneType = isDefault ? Long.valueOf(1L) : null;
		this.colorantName = null;
	}

	private static Long getHalftoneType(COSDictionary dict) {
		COSBase type = dict.getDictionaryObject(COSName.getPDFName("HalftoneType"));
		return !(type instanceof COSNumber) ? null :
				Long.valueOf(((COSNumber) type).longValue());
	}

	private static String getHalftoneName(COSDictionary dict) {
		COSBase name = dict.getDictionaryObject(COSName.getPDFName("HalftoneName"));
		if (name instanceof COSName) {
			return ((COSName) name).getName();
		} else if (name instanceof COSString) {
			return ((COSString) name).getString();
		} else if (name != null) {
			return name.toString();
		} else {
			return null;
		}
	}

	@Override
	public Long getHalftoneType() {
		return this.halftoneType;
	}

	@Override
	public String getHalftoneName() {
		return this.halftoneName;
	}

	@Override
	public String getcolorantName() {
		return this.colorantName;
	}

	@Override
	public String getTransferFunction() {
		if (this.simplePDObject instanceof COSDictionary) {
			COSBase base = ((COSDictionary) this.simplePDObject).getDictionaryObject(COSName.getPDFName("TransferFunction"));
			return base == null ? null : base instanceof COSName ?
					((COSName) base).getName() : base.toString();
		}
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case HALFTONES:
				return this.getHalftones();
			case CUSTOM_FUNCTION:
				return this.getCustomFunction();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<org.verapdf.model.pdlayer.PDHalftone> getHalftones() {
		if (halftoneType == null || halftoneType != 5L) {
			return Collections.emptyList();
		}
		List<org.verapdf.model.pdlayer.PDHalftone> halftones = new ArrayList<>();
		if (this.simplePDObject instanceof COSDictionary) {
			COSDictionary object = (COSDictionary) this.simplePDObject;
			for (COSName key : object.keySet()) {
				COSBase value = object.getDictionaryObject(key);
				if (value instanceof COSDictionary) {
					PBoxPDHalftone halftone = new PBoxPDHalftone((COSDictionary) value, key.getName());
					halftones.add(halftone);
				}
			}
		}
		return Collections.unmodifiableList(halftones);
	}
	//Stub
	private List<PBoxPDFunction> getCustomFunction() {
		return Collections.emptyList();
	}
}
