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
package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.external.ICCOutputProfile;
import org.verapdf.model.impl.pb.cos.PBCosDocumentTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCOutputProfileTest extends PBoxICCProfileTest {

	private static final String expectedS = "GTS_PDFA1";

	private static PDDocument doc;

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxICCOutputProfile.ICC_OUTPUT_PROFILE_TYPE)
				? PBoxICCOutputProfile.ICC_OUTPUT_PROFILE_TYPE : null;
		expectedID = null;

		setUpActualObject();
	}

	private static void setUpActualObject() throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(PBCosDocumentTest.FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);

		doc = PDDocument.load(file, false, true);
		PDOutputIntent outputIntent = doc.getDocumentCatalog().getOutputIntents().get(0);
		actual = new PBoxICCOutputProfile(outputIntent.getDestOutputIntent(), COSName.GTS_PDFA1.getName());
	}

	@Test
	public void testSMethod() {
		Assert.assertEquals(expectedS, ((ICCOutputProfile) actual).getS());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		doc.close();
	}
}
