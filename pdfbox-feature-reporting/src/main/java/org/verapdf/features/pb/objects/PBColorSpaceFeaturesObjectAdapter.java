/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import java.util.logging.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.color.*;
import org.verapdf.features.objects.ColorSpaceFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Features object adapter for ColorSpace
 *
 * @author Maksim Bezrukov
 */
public class PBColorSpaceFeaturesObjectAdapter implements ColorSpaceFeaturesObjectAdapter {

	private static final Logger LOGGER = Logger.getLogger(PBColorSpaceFeaturesObjectAdapter.class.getCanonicalName());

	private PDColorSpace colorSpace;
	private String id;
	private String iccProfileChild;
	private String colorSpaceChild;
	private Long hival;
	private String hexEncodedLookup;
	private List<String> errors;

	/**
	 * Constructs new colorspace features object adapter
	 *
	 * @param colorSpace        PDColorSpace which represents colorspace for feature report
	 * @param id                id of the object
	 * @param iccProfileChild   id of the iccprofile child
	 * @param colorSpaceChild   id of the colorspace child
	 */
	public PBColorSpaceFeaturesObjectAdapter(PDColorSpace colorSpace,
											 String id,
											 String iccProfileChild,
											 String colorSpaceChild) {
		this.colorSpace = colorSpace;
		this.id = id;
		this.iccProfileChild = iccProfileChild;
		this.colorSpaceChild = colorSpaceChild;
		if (colorSpace instanceof PDIndexed) {
			initIndexed();
		}
	}

	private void initIndexed() {
		PDIndexed index = (PDIndexed) colorSpace;
		this.errors = new ArrayList<>();
		if (index.getCOSObject() instanceof COSArray) {
			if (((COSArray) index.getCOSObject()).size() >= 3 &&
					((COSArray) index.getCOSObject()).getObject(2) instanceof COSNumber) {
				this.hival = Long.valueOf(((COSNumber) ((COSArray) index.getCOSObject()).getObject(2)).intValue());
			} else {
				this.errors.add("Indexed color space has no element hival or hival is not a number");
			}

			if (((COSArray) index.getCOSObject()).size() >= 4) {
				byte[] lookupData = null;
				COSBase lookupTable = ((COSArray) index.getCOSObject()).getObject(3);
				if (lookupTable instanceof COSString) {
					lookupData = ((COSString) lookupTable).getBytes();
				} else if (lookupTable instanceof COSStream) {
					try {
						lookupData = (new PDStream((COSStream) lookupTable)).getByteArray();
					} catch (IOException e) {
						LOGGER.log(java.util.logging.Level.INFO, e.getMessage());
						this.errors.add(e.getMessage());
					}
				} else {
					this.errors.add("Indexed color space has element lookup but it is not a String or a Stream");
				}

				if (lookupData != null) {
					this.hexEncodedLookup = DatatypeConverter.printHexBinary(lookupData);
				}
			} else {
				this.errors.add("Indexed color space has no element lookup");
			}
		} else {
			this.errors.add("Indexed color space is not an array");
		}
	}

	private static double[] parseTristimulus(PDTristimulus tris) {
		if (tris != null) {
			double[] res = new double[3];
			res[0] = tris.getX();
			res[1] = tris.getY();
			res[2] = tris.getZ();
			return res;
		}
		return null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getICCProfileChild() {
		return iccProfileChild;
	}

	@Override
	public String getColorSpaceChild() {
		return colorSpaceChild;
	}

	@Override
	public String getFamily() {
		if (colorSpace != null) {
			return colorSpace.getName();
		}
		return null;
	}

	@Override
	public double[] getWhitePoint() {
		if (colorSpace instanceof PDCIEDictionaryBasedColorSpace) {
			PDCIEDictionaryBasedColorSpace cie = (PDCIEDictionaryBasedColorSpace) colorSpace;
			return parseTristimulus(cie.getWhitepoint());
		}
		return null;
	}

	@Override
	public double[] getBlackPoint() {
		if (colorSpace instanceof PDCIEDictionaryBasedColorSpace) {
			PDCIEDictionaryBasedColorSpace cie = (PDCIEDictionaryBasedColorSpace) colorSpace;
			return parseTristimulus(cie.getBlackPoint());
		}
		return null;
	}

	@Override
	public Double getCalGrayGamma() {
		if (colorSpace instanceof PDCalGray) {
			PDCalGray calGray = (PDCalGray) colorSpace;
			return (double) calGray.getGamma();
		}
		return null;
	}

	@Override
	public double[] getCalRGBGamma() {
		if (colorSpace instanceof PDCalRGB) {
			PDCalRGB calRGB = (PDCalRGB) colorSpace;
			PDGamma pdGamma = calRGB.getGamma();
			double[] res = new double[3];
			res[0] = pdGamma.getR();
			res[1] = pdGamma.getG();
			res[2] = pdGamma.getB();
			return res;
		}
		return null;
	}

	@Override
	public double[] getMatrix() {
		if (colorSpace instanceof PDCalRGB) {
			PDCalRGB calRGB = (PDCalRGB) colorSpace;
			return PBAdapterHelper.castFloatArrayToDouble(calRGB.getMatrix());
		}
		return null;
	}

	@Override
	public double[] getLabRange() {
		if (colorSpace instanceof PDLab) {
			PDLab lab = (PDLab) colorSpace;
			double[] res = new double[4];
			res[0] = lab.getARange().getMin();
			res[1] = lab.getARange().getMax();
			res[2] = lab.getBRange().getMin();
			res[3] = lab.getBRange().getMax();
			return res;
		}
		return null;
	}

	@Override
	public int getNumberOfComponents() {
		if (colorSpace instanceof PDICCBased) {
			PDICCBased icc = (PDICCBased) colorSpace;
			return icc.getNumberOfComponents();
		}
		return 0;
	}

	@Override
	public Long getHival() {
		return this.hival;
	}

	@Override
	public String getHexEncodedLookup() {
		return this.hexEncodedLookup;
	}

	@Override
	public String getColorantName() {
		if (colorSpace instanceof PDSeparation) {
			PDSeparation sep = (PDSeparation) colorSpace;
			return sep.getColorantName();
		}
		return null;
	}

	@Override
	public List<String> getColorantNames() {
		if (colorSpace instanceof PDDeviceN) {
			PDDeviceN devN = (PDDeviceN) colorSpace;
			List<String> devNColorantNames = devN.getColorantNames();
			return devNColorantNames == null ?
					Collections.<String>emptyList() : Collections.unmodifiableList(devNColorantNames);
		}
		return null;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.colorSpace != null;
	}

	@Override
	public List<String> getErrors() {
		return this.errors == null ? Collections.<String>emptyList() : Collections.unmodifiableList(this.errors);
	}
}
