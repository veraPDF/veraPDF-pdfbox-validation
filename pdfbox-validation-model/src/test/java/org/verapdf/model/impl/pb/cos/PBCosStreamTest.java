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

import org.apache.pdfbox.cos.*;
import org.junit.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFilter;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosStreamTest extends BaseTest {

    private static final int EXPECTED_ORIGINAL_LENGTH = 10;
    private static String[] expectedFiltersArray;
    public static final String EXPECTED_FILTER_NAME = COSName.ASCII85_DECODE.getName();

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosStream.COS_STREAM_TYPE) ? PBCosStream.COS_STREAM_TYPE : null;
        expectedID = null;

        COSStream stream = getCosStream();

        actual = new PBCosStream(stream, document, null);
    }

    private static COSStream getCosStream() throws IOException {
        COSArray array = new COSArray();
        expectedFiltersArray = new String[2];

        array.add(COSName.LZW_DECODE);
        array.add(COSName.FLATE_DECODE);

        expectedFiltersArray[0] = COSName.LZW_DECODE.getName();
        expectedFiltersArray[1] = COSName.FLATE_DECODE.getName();

        COSObject object = new COSObject(new COSDictionary());
        object.setObjectNumber(10);
        object.setGenerationNumber(0);

        COSStream stream = new COSStream(new COSDictionary());
        stream.setOriginLength(Long.valueOf(EXPECTED_ORIGINAL_LENGTH));
        stream.setInt(COSName.LENGTH, EXPECTED_ORIGINAL_LENGTH - 1);
        stream.setItem(COSName.FILTER, array);
        stream.setItem(COSName.F_FILTER, COSName.ASCII85_DECODE);
        stream.setItem(COSName.F_DECODE_PARMS, object);
        return stream;
    }

    @Test
    public void testGetLengthMethod() {
        Assert.assertEquals(EXPECTED_ORIGINAL_LENGTH - 1, ((CosStream) actual).getLength().longValue());
    }

    @Test
    public void testGetFiltersMethod() {
        List<? extends Object> actualFilters = ((CosStream) actual).getLinkedObjects(PBCosStream.FILTERS);
        Assert.assertEquals(expectedFiltersArray.length, actualFilters.size());
        for (int i = 0; i < actualFilters.size(); i++) {
            Object filter = actualFilters.get(i);
            if (filter instanceof CosFilter) {
                Assert.assertEquals(expectedFiltersArray[i], ((CosFilter) filter).getinternalRepresentation());
            } else {
                Assert.fail();
            }
        }
    }

    @Test
    public void testGetFMethod() {
        Assert.assertTrue(((CosStream) actual).getF() == null);
    }

    @Test
    public void testFFilterMethod() {
        Assert.assertEquals(EXPECTED_FILTER_NAME, ((CosStream) actual).getFFilter());
    }

    @Test
    public void testFDecodeParmsMethod() {
        Assert.assertTrue(((CosStream) actual).getFDecodeParms() != null);
    }

    @Test
    public void testGetStreamKeywordCRLFCompliant() {
        Assert.assertTrue(((CosStream) actual).getstreamKeywordCRLFCompliant().booleanValue());
    }

	@Test
	public void testGetEndstreamKeywordEOLCompliant() {
		Assert.assertTrue(((CosStream) actual).getendstreamKeywordEOLCompliant().booleanValue());
	}

    @Test
    public void testGetIsLengthCorrect() {
        Assert.assertNotEquals(((CosStream) actual).getrealLength(), ((CosStream) actual).getLength());
    }

    @Test
    public void testParentLink() {
        final List<? extends Object> parentLink = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(parentLink.size(), 0);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        BaseTest.tearDown();
        for (int i = 0; i < expectedFiltersArray.length; i++) {
            expectedFiltersArray[i] = null;
        }
        expectedFiltersArray = null;
    }
}
