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

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PD3DStream;

import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxPD3DStream extends PBoxPDObject implements PD3DStream {

	public static final String STREAM_3D_TYPE = "PD3DStream";

	public static final String COLOR_SPACE = "colorSpace";

	public PBoxPD3DStream(COSStream stream) {
		super(stream, STREAM_3D_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case COLOR_SPACE:
				return Collections.emptyList();
			default:
				return super.getLinkedObjects(link);
		}
	}

	@Override
	public String getSubtype() {
		COSName subtype = ((COSStream)simplePDObject).getCOSName(COSName.SUBTYPE);
		return subtype == null ? null : subtype.getName();
	}
}
