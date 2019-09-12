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
import org.apache.pdfbox.cos.COSStream;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.util.List;

import static org.verapdf.model.impl.pb.cos.PBCosDict.COS_DICT_TYPE;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDictTest extends BaseTest {

    private static long expectedLength;

    @BeforeClass
    public static void setUp() {
        expectedType = TYPES.contains(COS_DICT_TYPE) ? COS_DICT_TYPE : null;
        expectedID = null;

        COSDictionary dictionary = new COSDictionary();

        for (int index = 1; index < 6; index++) {
            dictionary.setInt(COSName.getPDFName(String.valueOf(index)), index);
        }
        try (COSStream cosStream = new COSStream(new COSDictionary())) {
        	dictionary.setItem(COSName.METADATA, cosStream);
        } catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
        
        expectedLength = dictionary.size();

        actual = new PBCosDict(dictionary, document, null);
    }

    @Test
    public void testGetSizeMethod() {
        Assert.assertEquals(expectedLength, ((CosDict) actual).getsize().longValue());
    }

    @Test
    public void testGetKeysLink() {
        List<CosName> keys = (List<CosName>) actual
                .getLinkedObjects(PBCosDict.KEYS);
        for (int index = 1; index < expectedLength; index++) {
			String expected = keys.get(index - 1).getinternalRepresentation();
			Assert.assertEquals(expected, String.valueOf(index));
        }
		String expected = keys.get(keys.size() - 1).getinternalRepresentation();
		Assert.assertEquals(expected, COSName.METADATA.getName());
    }

    @Test
    public void testGetValuesLink() {
        List<? extends org.verapdf.model.baselayer.Object> values = actual
                .getLinkedObjects(PBCosDict.VALUES);
        for (int index = 1; index < expectedLength; index++) {
            Object object = values.get(index - 1);
            Assert.assertTrue(object instanceof CosInteger);
			Long expected = ((CosInteger) object).getintValue();
			Assert.assertEquals(expected, Long.valueOf(index));
        }
		Object object = values.get(values.size() - 1);
		Assert.assertTrue(object instanceof CosStream);
    }

    @Test
    public void testGetMetadataLink() {
        List<? extends Object> metadata = actual.getLinkedObjects(PBCosDict.METADATA);
        Assert.assertEquals(0, metadata.size());
    }

}
