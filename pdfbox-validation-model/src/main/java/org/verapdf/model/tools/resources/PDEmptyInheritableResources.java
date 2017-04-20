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
package org.verapdf.model.tools.resources;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

/**
 * @author Evgeniy Muravitskiy
 */
class PDEmptyInheritableResources extends PDInheritableResources {

	PDEmptyInheritableResources() {
		super(EMPTY_RESOURCES, EMPTY_RESOURCES);
	}

	@Override
	public PDFont getFont(COSName name) {
		return null;
	}

	@Override
	public PDColorSpace getColorSpace(COSName name) {
		return null;
	}

	@Override
	public PDExtendedGraphicsState getExtGState(COSName name) {
		return null;
	}

	@Override
	public PDShading getShading(COSName name) {
		return null;
	}

	@Override
	public PDAbstractPattern getPattern(COSName name) {
		return null;
	}

	@Override
	public PDXObject getXObject(COSName name) {
		return null;
	}
}
