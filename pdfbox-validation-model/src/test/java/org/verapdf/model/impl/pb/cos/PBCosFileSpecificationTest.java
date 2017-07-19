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
package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.external.PBoxEmbeddedFile;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecificationTest extends BaseTest {

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosFileSpecification.COS_FILE_SPECIFICATION_TYPE) ?
														PBCosFileSpecification.COS_FILE_SPECIFICATION_TYPE : null;
        expectedID = null;

        COSDictionary specification = new COSDictionary();
        specification.setItem(COSName.EF, new COSDictionary());

        actual = new PBCosFileSpecification(specification, document, null);
    }

    @Test
    public void testGetEFMethod() {
		List<? extends Object> links = actual.getLinkedObjects(PBCosFileSpecification.EF);
		Assert.assertEquals(0, links.size());
	}

    @Test
    public void testParentLink() {
        final List<? extends Object> parentLink = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(parentLink.size(), 0);
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
