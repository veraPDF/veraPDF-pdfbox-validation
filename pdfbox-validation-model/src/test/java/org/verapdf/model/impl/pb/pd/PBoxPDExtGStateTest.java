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
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDExtGStateTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDExtGState.EXT_G_STATE_TYPE) ? PBoxPDExtGState.EXT_G_STATE_TYPE : null;
		expectedID = "37 0 obj PDExtGState";

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName next = resources.getExtGStateNames().iterator().next();
		actual = new PBoxPDExtGState(resources.getExtGState(next), document, null);
	}

	@Test
	public void testTRMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getTR());
	}

	@Test
	public void testTR2Method() {
		Assert.assertNull(((PBoxPDExtGState) actual).getTR2());
	}

	@Test
	public void testSMaskMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getSMask());
	}

	@Test
	public void testBMMethod() {
		Assert.assertEquals("Screen", ((PBoxPDExtGState) actual).getBM());
	}

	@Test
	public void testFillCAMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getca());
	}

	@Test
	public void testStrokeCAMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getCA());
	}

	@Test
	public void testRenderingIntentLink() {
		List<? extends Object> ri = actual.getLinkedObjects(PBoxPDExtGState.RI);
		Assert.assertEquals(1, ri.size());
		for (Object object : ri) {
			Assert.assertEquals(PBCosRenderingIntent.COS_RENDERING_INTENT_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testFontSize() {
		List<? extends Object> fontSize = actual.getLinkedObjects(PBoxPDExtGState.FONT_SIZE);
		Assert.assertEquals(0, fontSize.size());
	}

}
