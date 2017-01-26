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

import org.apache.pdfbox.cos.COSString;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : rewrite for real document
public class PBCosStringTest extends BaseTest {

    private static CosString secondActual;

    private static String firstExpected;
    private static String secondExpected;

    @BeforeClass
    public static void setUp() throws IOException {
        expectedType = TYPES.contains(PBCosString.COS_STRING_TYPE) ? PBCosString.COS_STRING_TYPE : null;
        expectedID = null;

        setExpectedResult();

        final String string = "AAFFFEEE";
        COSString value = COSString.parseHex(string);
        value.setContainsOnlyHex(false);
        value.setHexCount(Long.valueOf(string.length()));

        actual = new PBCosString(value);
        secondActual = new PBCosString(new COSString(secondExpected));
    }

    private static void setExpectedResult() {
        byte[] bytes = new byte[]{(byte) 0xAA, (byte) 0xFF, (byte) 0xFE, (byte) 0xEE};
        firstExpected = new String(bytes, Charset.forName("US-ASCII"));
        secondExpected = "Hello, World!";
    }

    @Test
    public void testGetValueMethod() {
        Assert.assertEquals(firstExpected, ((CosString) actual).getvalue());
        Assert.assertEquals(secondExpected, secondActual.getvalue());
    }

    @Test
    public void testGetIsHexMethod() {
        Assert.assertTrue(((CosString) actual).getisHex().booleanValue());
        Assert.assertFalse(secondActual.getisHex().booleanValue());
    }

    @Test
    public void testGetIsHexSymbolsMethod() {
        Assert.assertFalse(((CosString) actual).getcontainsOnlyHex().booleanValue());
        Assert.assertTrue(secondActual.getcontainsOnlyHex().booleanValue());
    }

    @Test
    public void testGetHexCountMethod() {
        Assert.assertEquals(((CosString) actual).gethexCount(), Long.valueOf(8));
        Assert.assertEquals(secondActual.gethexCount(), Long.valueOf(0));
    }

    @AfterClass
    public static void tearDown() throws IOException {
        BaseTest.tearDown();

        actual = null;
        secondActual = null;
        firstExpected = null;
        secondExpected = null;
    }
}
