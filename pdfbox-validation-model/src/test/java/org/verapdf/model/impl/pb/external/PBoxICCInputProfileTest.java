package org.verapdf.model.impl.pb.external;

import java.io.ByteArrayInputStream;
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
