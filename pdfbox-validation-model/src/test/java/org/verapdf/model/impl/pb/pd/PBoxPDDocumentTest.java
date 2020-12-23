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
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocumentTest extends BaseTest{

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDDocument.PD_DOCUMENT_TYPE) ? PBoxPDDocument.PD_DOCUMENT_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDDocument(document, null);
	}

	@Test
	public void testOutlinesLink() {
		List<? extends Object> outlines = actual.getLinkedObjects(PBoxPDDocument.OUTLINES);
		Assert.assertEquals(6, outlines.size());
		for (Object object : outlines) {
			Assert.assertEquals(PBoxPDOutline.OUTLINE_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testOpenActionLink() {
		isCorrectActions(PBoxPDDocument.OPEN_ACTION);
	}

	@Test
	public void testActionsLink() {
		isCorrectActions(PBoxPDDocument.ACTIONS);
	}

	@Test
	public void testPagesLink() {
		List<? extends Object> pages = actual.getLinkedObjects(PBoxPDDocument.PAGES);
		Assert.assertEquals(1, pages.size());
		for (Object object : pages) {
			Assert.assertEquals(PBoxPDPage.PAGE_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testMetadataLink() {
		List<? extends Object> metadata = actual.getLinkedObjects(PBoxPDDocument.METADATA);
		Assert.assertEquals(1, metadata.size());
		for (Object object : metadata) {
			Assert.assertEquals(PBoxPDMetadata.METADATA_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testOutputIntentsLink() {
		List<? extends Object> outputIntents = actual.getLinkedObjects(PBoxPDDocument.OUTPUT_INTENTS);
		Assert.assertEquals(1, outputIntents.size());
		for (Object object : outputIntents) {
			Assert.assertEquals(PBoxOutputIntents.OUTPUT_INTENTS_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testAcroFormsLink() {
		Assert.assertEquals(0, actual.getLinkedObjects(PBoxPDDocument.ACRO_FORMS).size());
	}

	private static void isCorrectActions(String link) {
		List<? extends Object> actions = actual.getLinkedObjects(link);
		Assert.assertEquals(0, actions.size());
		for (Object object : actions) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}
}
