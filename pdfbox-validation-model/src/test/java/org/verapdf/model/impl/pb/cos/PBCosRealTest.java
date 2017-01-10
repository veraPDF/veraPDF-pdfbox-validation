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

import java.util.Random;

import org.apache.pdfbox.cos.COSFloat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosRealTest extends BaseTest {

    private static float expected;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosReal.COS_REAL_TYPE) ? PBCosReal.COS_REAL_TYPE : null;
        expectedID = null;

        Random random = new Random();
        expected = random.nextFloat();

        actual = new PBCosReal(new COSFloat(expected));
    }

    @Test
    // @carlwilson temporarily ignored as it's a "boxing box" issue
    public void testGetIntegerMethod() {
        Assert.assertEquals(Long.valueOf((long) expected), ((CosReal) actual).getintValue());
    }

    @Test
    public void testGetDoubleMethod() {
        Assert.assertEquals(expected, ((CosReal) actual).getrealValue().doubleValue(), 0.00001);
    }

    @Test
    public void testGetRealMethod() {
        Assert.assertEquals(expected + "", ((CosReal) actual).getstringValue());
    }


    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
