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
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PD3DStream;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author Maxim Plushchov
 */
public class PBoxPD3DStream extends PBoxPDObject implements PD3DStream {

	public static final String STREAM_3D_TYPE = "PD3DStream";

	public static final String COLOR_SPACE = "colorSpace";

	private static final Logger LOGGER = Logger.getLogger(PBoxPD3DStream.class.getCanonicalName());

	private final PDResources resources;

	public PBoxPD3DStream(COSStream stream, PDResources resources) {
		super(stream, STREAM_3D_TYPE);
		this.resources = resources;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case COLOR_SPACE:
				return this.getColorSpace();
			default:
				return super.getLinkedObjects(link);
		}
	}

	@Override
	public String getSubtype() {
		COSName subtype = ((COSStream)simplePDObject).getCOSName(COSName.SUBTYPE);
		return subtype == null ? null : subtype.getName();
	}

	private List<PDColorSpace> getColorSpace() {
		try {
			COSBase rawColorSpace = ((COSStream) simplePDObject).getItem(COSName.COLORSPACE);
			COSName colorSpaceName = null;
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace = null;
			if (rawColorSpace != null) {
				if (rawColorSpace instanceof COSName) {
					colorSpaceName = (COSName) rawColorSpace;
				} else if (rawColorSpace instanceof COSArray) {
					COSArray array = (COSArray) rawColorSpace;
					if (array.size() == 1 && array.get(0) instanceof COSName) {
						colorSpaceName = (COSName) array.get(0);
					} else {
						try {
							colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace.create(rawColorSpace);
						} catch (IOException e) {
							LOGGER.log(Level.WARNING,"There is no colorSpace by rawColorSpace");
						}
					}
				}
			}
			List<PDColorSpace> colorSpaces = new ArrayList<>(PBoxPDObject.MAX_NUMBER_OF_ELEMENTS);
			if (colorSpaceName != null) {
				colorSpace = resources.getColorSpace(colorSpaceName);

				if (colorSpace != null) {
					colorSpaces.add(ColorSpaceFactory.getColorSpace(colorSpace, this.document, null));
					return Collections.unmodifiableList(colorSpaces);
				}
			} else if (colorSpace != null) {
				colorSpaces.add(ColorSpaceFactory.getColorSpace(colorSpace, this.document, null));
				return Collections.unmodifiableList(colorSpaces);
			}
		} catch (IOException e) {
			return Collections.emptyList();
		}
		return Collections.emptyList();
	}
}
