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

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToRemoteAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDAnnot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnotTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDAnnot.ANNOTATION_TYPE) ? PBoxPDAnnot.ANNOTATION_TYPE : null;
		expectedID = "11 0 obj PDAnnot";

		setUp(FILE_RELATIVE_PATH);
		List<PDAnnotation> annotations = document.getPage(0).getAnnotations();
		PDAnnotation annot = annotations.get(annotations.size() - 1);
		actual = new PBoxPDAnnot(annot, document.getPage(0).getResources(), document, null);
	}

	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals("Widget", ((PDAnnot) actual).getSubtype());
	}

	@Test
	public void testAPMethod() {
		Assert.assertNotNull(((PDAnnot) actual).getAP());
	}

	@Test
	public void testFMethod() {
		Assert.assertEquals(Long.valueOf(4), ((PDAnnot) actual).getF());
	}

	@Test
	public void testCAMethod() {
		Assert.assertNull(((PDAnnot) actual).getCA());
	}

	@Test
	public void testNTypeMethod() {
		Assert.assertEquals("Stream", ((PDAnnot) actual).getN_type());
	}

	@Test
	public void testFTMethod() {
		Assert.assertEquals("Btn", ((PDAnnot) actual).getFT());
	}

	@Test
	public void testAdditionalActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.ADDITIONAL_ACTION);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

	@Test
	public void testActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.A);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDGoToAction.GOTO_ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDGoToRemoteAction.GOTO_REMOTE_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

	@Test
	public void testICLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.IC);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBCosReal.COS_REAL_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testCLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.C);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBCosReal.COS_REAL_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testAppearanceLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.APPEARANCE);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}

}
