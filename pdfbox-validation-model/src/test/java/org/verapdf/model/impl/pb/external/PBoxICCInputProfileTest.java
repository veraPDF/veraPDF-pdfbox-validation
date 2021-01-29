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

import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.external.ICCProfile;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCInputProfileTest extends PBoxICCProfileTest {

	public static final int EXPECTED_VERSION = 0x02;
	public static final int EXPECTED_SUBVERSION = 0x1A;

	@BeforeClass
	public static void setUp() throws IOException {
		expectedType = TYPES.contains(PBoxICCInputProfile.ICC_INPUT_PROFILE_TYPE) ?
																PBoxICCInputProfile.ICC_INPUT_PROFILE_TYPE : null;
		expectedID = null;

		setUpActualObject();
	}

	private static void setUpActualObject() throws IOException {
		byte[] bytes = new byte[128];

		bytes[PBoxICCProfile.VERSION_BYTE] = EXPECTED_VERSION;
		bytes[PBoxICCProfile.SUBVERSION_BYTE] = EXPECTED_SUBVERSION;

		for (int i = PBoxICCProfile.DEVICE_CLASS_OFFSET; i < PBoxICCProfile.DEVICE_CLASS_OFFSET + 4; i++) {
			bytes[i] = (byte) PBoxICCProfileTest.expectedDeviceClass.charAt(i - PBoxICCProfile.DEVICE_CLASS_OFFSET);
		}

		COSDictionary dict = new COSDictionary();
		dict.setLong(COSName.N, 3L);
		COSStream stream = new COSStream(dict);
		OutputStream outputStream = stream.createUnfilteredStream();
		outputStream.write(bytes);
		outputStream.close();
		actual = new PBoxICCInputProfile(stream);
	}

	@Override
	@Test
	public void testColorSpaceMethod() {
		Assert.assertEquals(new String(new byte[]{0,0,0,0}), ((ICCProfile) actual).getcolorSpace());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;
	}
}
