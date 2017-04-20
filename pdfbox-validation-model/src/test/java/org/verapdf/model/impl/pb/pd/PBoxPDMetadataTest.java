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
import org.verapdf.model.impl.axl.AXLMainXMPPackage;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadataTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDMetadata.METADATA_TYPE) ? PBoxPDMetadata.METADATA_TYPE : null;
		expectedID = "5 0 obj PDMetadata";

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDMetadata(document.getDocumentCatalog().getMetadata(), Boolean.TRUE, document, PDFAFlavour.PDFA_1_B);
	}

	@Test
	public void testFiltersMethod() {
		Assert.assertNull(((PDMetadata) actual).getFilter());
	}

	@Test
	public void testXMPPackageLink() {
		List<? extends org.verapdf.model.baselayer.Object> packages =
				actual.getLinkedObjects(PBoxPDMetadata.XMP_PACKAGE);
		Assert.assertEquals(1, packages.size());
		for (Object object : packages) {
			Assert.assertEquals(AXLMainXMPPackage.MAIN_XMP_PACKAGE_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testStreamLink() {
		List<? extends org.verapdf.model.baselayer.Object> packages =
				actual.getLinkedObjects(PBoxPDMetadata.STREAM);
		Assert.assertEquals(1, packages.size());
		for (Object object : packages) {
			Assert.assertEquals(PBCosStream.COS_STREAM_TYPE, object.getObjectType());
		}
	}

}
