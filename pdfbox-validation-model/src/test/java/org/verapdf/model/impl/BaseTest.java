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
package org.verapdf.model.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.ModelHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 * @author Evgeniy Muravitskiy
 */

public abstract class BaseTest {

	private static final String BASE_FOLDER = "/model/impl/pb/";

    protected static org.verapdf.model.baselayer.Object actual;
	protected static PDDocument document;

    protected static String expectedType;
    protected static String expectedID;

	protected final static Set<String> TYPES = ModelHelper.getTypes();

    @Test
    public void testTypeAndID() {
        Assert.assertEquals(expectedType, actual.getObjectType());
        Assert.assertEquals(expectedID, actual.getID());
    }

	@Test
	public void testLinksMethod() {
		List<String> expectedLinks = ModelHelper.getListOfLinks(actual.getObjectType());
		for (String link : expectedLinks) {
			Assert.assertNotNull(actual.getLinkedObjects(link));
		}
		expectedLinks.clear();
	}

	@Test(expected = IllegalAccessError.class)
	public void testNonexistentParentLink() {
		actual.getLinkedObjects("Wrong link.");
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		if (document != null) {
			document.close();
		}
	}

	protected static void setUp(String path) throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(BASE_FOLDER + path);
		File file = new File(fileAbsolutePath);
		document = PDDocument.load(file, false, true);
	}

	protected static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}
