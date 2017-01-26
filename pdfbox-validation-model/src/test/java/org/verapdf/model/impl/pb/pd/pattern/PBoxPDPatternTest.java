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
package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDPattern;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxPDPatternTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/ColorSpaces.pdf";

	protected static void setUp(String patternType, String patternName)
			throws URISyntaxException, IOException {
		expectedType = TYPES.contains(patternType) ? patternType : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName patternCosName = COSName.getPDFName(patternName);
		actual = getPattern(PDInheritableResources.getInstance(resources), patternCosName);
	}

	private static PDPattern getPattern(PDInheritableResources resources,
										COSName patternCosName)
			throws IOException {
		return ColorSpaceFactory.getPattern(resources.getPattern(patternCosName), resources, document, null);
	}

}
