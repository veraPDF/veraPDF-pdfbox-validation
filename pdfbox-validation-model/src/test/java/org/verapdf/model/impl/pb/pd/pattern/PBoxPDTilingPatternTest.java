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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDTilingPatternTest extends PBoxPDPatternTest{

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		setUp(PBoxPDTilingPattern.TILING_PATTERN_TYPE, "P0");
		expectedID = "8 0 obj PDTilingPattern";
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> contentStream = actual.getLinkedObjects(PBoxPDTilingPattern.CONTENT_STREAM);
		Assert.assertEquals(1, contentStream.size());
		for (Object object : contentStream) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}
}
