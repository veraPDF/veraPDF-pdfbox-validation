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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPageTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDPage.PAGE_TYPE) ? PBoxPDPage.PAGE_TYPE : null;
		expectedID = "8 0 obj PDPage";

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDPage(document.getPage(0), document, null);
	}

	@Test
	public void testGroupLink() {
		List<? extends Object> groups = ((PDPage) actual).getLinkedObjects(PBoxPDPage.GROUP);
		Assert.assertEquals(0, groups.size());
		for (Object object : groups) {
			Assert.assertEquals(PBoxPDGroup.GROUP_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testAnnotsLink() {
		List<? extends Object> annots = ((PDPage) actual).getLinkedObjects(PBoxPDPage.ANNOTS);
		Assert.assertEquals(0, annots.size());
		for (Object object : annots) {
			Assert.assertEquals(PBoxPDAnnot.ANNOTATION_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testActionLink() {
		List<? extends Object> actions = ((PDPage) actual).getLinkedObjects(PBoxPDPage.ACTION);
		Assert.assertEquals(0, actions.size());
		for (Object object : actions) {
			Assert.assertEquals(PBoxPDAction.ACTION_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> streams = ((PDPage) actual).getLinkedObjects(PBoxPDPage.CONTENT_STREAM);
		Assert.assertEquals(1, streams.size());
		for (Object object : streams) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}

}
