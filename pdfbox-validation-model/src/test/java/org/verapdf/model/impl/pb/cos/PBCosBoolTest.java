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

import org.apache.pdfbox.cos.COSBoolean;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosBool;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosBoolTest extends BaseTest {

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(PBCosBool.COS_BOOLEAN_TYPE) ? PBCosBool.COS_BOOLEAN_TYPE : null;
        expectedID = null;

        COSBoolean bool = COSBoolean.getBoolean(Boolean.TRUE);
        actual = PBCosBool.valueOf(bool);
    }

    @Test
    public void testBooleanValue() {
        Assert.assertTrue(((CosBool) actual).getvalue().equals(Boolean.TRUE));
    }

    @AfterClass
    public static void tearDown() {
        expectedType = null;
        actual = null;
    }
}
