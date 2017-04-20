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
import org.apache.pdfbox.cos.COSObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailerTest extends BaseTest {

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosTrailer.COS_TRAILER_TYPE) ? PBCosTrailer.COS_TRAILER_TYPE : null;
        expectedID = null;

        COSDictionary trailer = new COSDictionary();
        COSObject root = new COSObject(new COSDictionary());
        trailer.setInt(COSName.SIZE, 10);
        trailer.setItem(COSName.ROOT, root);
        trailer.setItem(COSName.ENCRYPT, root);

        actual = new PBCosTrailer(trailer, document, null);
    }

    @Test
    public void testGetIsEncryptedMethod() {
        Assert.assertTrue(((CosTrailer) actual).getisEncrypted().booleanValue());
    }

    @Test
    public void testCatalogLink() {
        List<? extends Object> catalog = actual.getLinkedObjects(PBCosTrailer.CATALOG);
        Assert.assertEquals(catalog.size(), 1);
        Assert.assertTrue(catalog.get(0) instanceof CosIndirect);
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
