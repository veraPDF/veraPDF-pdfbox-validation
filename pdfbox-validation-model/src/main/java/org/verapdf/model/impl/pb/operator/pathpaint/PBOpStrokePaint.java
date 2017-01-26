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
package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * Base class for path paint operators which strokes the path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStrokePaint extends PBOpPathPaint {

	protected PBOpStrokePaint(List<COSBase> arguments, final GraphicState state,
							  final PDInheritableResources resources, final String opType, PDDocument document, PDFAFlavour flavour) {
		super(arguments, state, resources, opType, document, flavour);
	}

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		if (STROKE_CS.equals(link)) {
			return this.getStrokeCS();
		}
		return super.getLinkedObjects(link);
	}

}
