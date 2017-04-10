/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Maksim Bezrukov
 */
public final class PBAdapterHelper {

	private static final Logger LOGGER = Logger.getLogger(PBAdapterHelper.class);

	private PBAdapterHelper() {
	}

	/**
	 * Gets String value from COSBase class
	 *
	 * @param baseParam
	 *            COSBase object
	 * @return String value of a COSString object if the direct object that will
	 *         get from the given COSBase is COSString and null in all other
	 *         cases
	 */
	public static String getStringFromBase(COSBase baseParam) {

		COSBase base = baseParam;

		while (base instanceof COSObject) {
			base = ((COSObject) base).getObject();
		}

		if (base instanceof COSString) {
			COSString str = (COSString) base;
			return str.isHex() ? str.toHexString() : str.getString();
		}
		return null;
	}

	/**
	 * Generates byte array with contents of a stream
	 *
	 * @param is
	 *            input stream for converting
	 * @return byte array with contents of a stream
	 * @throws IOException
	 *             If the first byte cannot be read for any reason other than
	 *             end of file, or if the input stream has been closed, or if
	 *             some other I/O error occurs.
	 */
	public static byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) != -1) {
			baos.write(bytes, 0, length);
		}
		return baos.toByteArray();
	}

	public static InputStream getMetadataStream(PDMetadata metadata) {
		if (metadata != null) {
			COSStream stream = metadata.getStream();
			if (stream != null) {
				try {
					return stream.getUnfilteredStream();
				} catch (IOException e) {
					LOGGER.debug("Error while obtaining unfiltered metadata stream", e);
				}
			}
		}
		return null;
	}

	public static double[] parseFloatMatrix(float[][] array) {
		if (array != null) {
			double[] res = new double[6];
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 2; ++j) {
					res[2 * i + j] = array[i][j];
				}
			}
			return res;
		}
		return null;
	}

	public static double[] castFloatArrayToDouble(float[] array) {
		if (array != null) {
			double[] res = new double[array.length];
			for (int i = 0; i < array.length; ++i) {
				res[i] = array[i];
			}
			return res;
		}
		return null;
	}

	public static double[] parseRectangle(PDRectangle rect) {
		if (rect != null) {
			double[] res = new double[4];
			res[0] = rect.getLowerLeftX();
			res[1] = rect.getLowerLeftY();
			res[2] = rect.getUpperRightX();
			res[3] = rect.getUpperRightY();
			return res;
		}
		return null;
	}
}
