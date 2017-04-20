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

import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.verapdf.model.impl.pb.pd.PBoxPDContentStream.CONTENT_STREAM_TYPE;
import static org.verapdf.model.impl.pb.pd.PBoxPDContentStream.OPERATORS;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStreamTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(CONTENT_STREAM_TYPE) ? CONTENT_STREAM_TYPE : null;
		expectedID = "11 0 obj PDContentStream";

		setUp(FILE_RELATIVE_PATH);
		PDPage page = document.getPage(0);
		actual = new PBoxPDContentStream(page, PDInheritableResources.getInstance(page.getResources()), document, null);
	}

	@Test
	public void testOperatorsLink() {
		List<? extends Object> operators = actual.getLinkedObjects(OPERATORS);
		Assert.assertTrue(operators.size() > 0);
		for (Object object : operators) {
			Assert.assertTrue(object instanceof PBOperator);
		}
	}

}
