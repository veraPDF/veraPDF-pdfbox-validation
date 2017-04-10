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
package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.features.objects.ICCProfileFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Feature object adapter for icc profile
 *
 * @author Maksim Bezrukov
 */
public class PBICCProfileFeaturesObjectAdapter implements ICCProfileFeaturesObjectAdapter {

	private static final Logger LOGGER = Logger
			.getLogger(PBICCProfileFeaturesObjectAdapter.class);

	private static final int HEADER_SIZE = 128;
	private static final int FF_FLAG = 0xFF;
	private static final int REQUIRED_LENGTH = 4;
	private static final int TAGINFO_LENGTH = 12;
	private static final int BITSINBYTE = 8;
	private static final int VERSION_BYTE = 8;
	private static final int SUBVERSION_BYTE = 9;
	private static final int CMMTYPE_BEGIN = 4;
	private static final int CMMTYPE_END = 8;
	private static final int DATACOLORSPACE_BEGIN = 16;
	private static final int DATACOLORSPACE_END = 20;
	private static final int RENDERINGINTENT_BEGIN = 64;
	private static final int RENDERINGINTENT_END = 68;
	private static final int PROFILEID_BEGIN = 84;
	private static final int PROFILEID_END = 100;
	private static final int DEVICEMODEL_BEGIN = 52;
	private static final int DEVICEMODEL_END = 56;
	private static final int DEVICEMANUFACTURER_BEGIN = 48;
	private static final int DEVICEMANUFACTURER_END = 52;
	private static final int CREATOR_BEGIN = 80;
	private static final int CREATOR_END = 84;
	private static final int CREATION_YEAR = 24;
	private static final int CREATION_MONTH = 26;
	private static final int CREATION_DAY = 28;
	private static final int CREATION_HOUR = 30;
	private static final int CREATION_MIN = 32;
	private static final int CREATION_SEC = 34;
	private static final int FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_BEGIN = 8;
	private static final int FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END = 12;
	private static final int NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_BEGIN = 8;
	private static final int NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END = 12;
	private static final int LENGTH_OF_RECORD_IN_MULTILOCALIZEDUNICODETYPE_END = 12;

	private COSStream profile;
	private String id;
	private String version;
	private String cmmType;
	private String dataColorSpace;
	private String creator;
	private Calendar creationDate;
	private String defaultRenderingIntent;
	private String copyright;
	private String description;
	private String profileID;
	private String deviceModel;
	private String deviceManufacturer;
	private List<String> errors;

	/**
	 * Constructs new icc profile feature object adapter
	 *
	 * @param profile   COSStream which represents the icc profile for feature report
	 * @param id        id of the profile
	 */
	public PBICCProfileFeaturesObjectAdapter(COSStream profile, String id) {
		this.profile = profile;
		this.id = id;
		init();
	}

	private void init() {
		if (profile != null) {
			this.errors = new ArrayList<>();
			try {
				byte[] profileBytes = PBAdapterHelper.inputStreamToByteArray(profile.getUnfilteredStream());
				if (profileBytes.length < HEADER_SIZE) {
					this.errors.add("ICCProfile contains less than " + HEADER_SIZE + " bytes");
				} else {
					this.version = getVersion(profileBytes);
					this.cmmType = getString(profileBytes, CMMTYPE_BEGIN, CMMTYPE_END);
					this.dataColorSpace = getString(profileBytes, DATACOLORSPACE_BEGIN, DATACOLORSPACE_END);
					this.creator = getString(profileBytes, CREATOR_BEGIN, CREATOR_END);
					this.creationDate = getCreationDate(profileBytes);
					this.defaultRenderingIntent = getIntent(getString(profileBytes, RENDERINGINTENT_BEGIN, RENDERINGINTENT_END));
					this.copyright = getStringTag(profileBytes, "cprt", true);
					this.description = getStringTag(profileBytes, "desc", false);
					this.profileID = getString(profileBytes, PROFILEID_BEGIN, PROFILEID_END);
					this.deviceModel = getString(profileBytes, DEVICEMODEL_BEGIN, DEVICEMODEL_END);
					this.deviceManufacturer = getString(profileBytes, DEVICEMANUFACTURER_BEGIN, DEVICEMANUFACTURER_END);
				}

			} catch (IOException e) {
				LOGGER.debug("Reading byte array from InputStream error", e);
				this.errors.add(e.getMessage());
			}
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getCMMType() {
		return cmmType;
	}

	@Override
	public String getDataColorSpace() {
		return dataColorSpace;
	}

	@Override
	public String getCreator() {
		return creator;
	}

	@Override
	public Calendar getCreationDate() {
		return creationDate;
	}

	@Override
	public String getDefaultRenderingIntent() {
		return defaultRenderingIntent;
	}

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getProfileID() {
		return profileID;
	}

	@Override
	public String getDeviceModel() {
		return deviceModel;
	}

	@Override
	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	@Override
	public List<String> getErrors() {
		return errors == null ? Collections.<String>emptyList() : Collections.unmodifiableList(errors);
	}

	@Override
	public InputStream getMetadataStream() {
		if (this.profile != null) {
			COSBase cosBase = profile.getDictionaryObject(COSName.METADATA);
			if (cosBase instanceof COSStream) {
				return PBAdapterHelper.getMetadataStream(new PDMetadata((COSStream) cosBase));
			}
		}
		return null;
	}

	@Override
	public InputStream getData() {
		if (profile != null) {
			try {
				return profile.getUnfilteredStream();
			} catch (IOException e) {
				LOGGER.debug("Can not get iccProfile stream", e);
			}
		}
		return null;
	}

	@Override
	public Integer getN() {
		if (profile != null) {
			COSBase nBase = profile.getDictionaryObject(COSName.N);
			if (nBase instanceof COSInteger) {
				return Integer.valueOf(((COSInteger) nBase).intValue());
			}
		}
		return null;
	}

	@Override
	public List<Double> getRange() {
		if (profile != null) {
			List<Double> range = new ArrayList<>();;
			COSBase rangeBase = profile.getDictionaryObject(COSName.RANGE);
			if (rangeBase instanceof COSArray) {
				COSArray array = (COSArray) rangeBase;
				for (COSBase baseNumb : array) {
					if (baseNumb instanceof COSNumber) {
						range.add(Double.valueOf(((COSNumber) baseNumb).doubleValue()));
					} else {
						range.add(null);
					}
				}
			} else {
				Integer n = getN();
				if (n != null) {
					for (int i = 0; i < n.intValue(); ++i) {
						range.add(Double.valueOf(0.));
						range.add(Double.valueOf(1.));
					}
				}
			}
			return Collections.unmodifiableList(range);
		}
		return Collections.emptyList();
	}

	private static String getIntent(String str) {
		if (str == null) {
			return "Perceptual";
		}
		switch (str) {
			case "\u0000\u0000\u0000\u0001":
				return "Media-Relative Colorimetric";
			case "\u0000\u0000\u0000\u0002":
				return "Saturation";
			case "\u0000\u0000\u0000\u0003":
				return "ICC-Absolute Colorimetric";
			default:
				return str;
		}
	}

	private static String getVersion(byte[] header) {

		if (header[VERSION_BYTE] == 0 && header[SUBVERSION_BYTE] == 0) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(header[VERSION_BYTE] & FF_FLAG).append(".");
		builder.append((header[SUBVERSION_BYTE] & FF_FLAG) >>> REQUIRED_LENGTH);
		return builder.toString();
	}

	private static String getString(byte[] header, int begin, int end) {
		StringBuilder builder = new StringBuilder();
		boolean isEmpty = true;
		for (int i = begin; i < end; ++i) {
			if (header[i] != 0) {
				isEmpty = false;
			}
			builder.append((char) header[i]);
		}

		return isEmpty ? null : builder.toString();
	}

	private static Calendar getCreationDate(byte[] header) {

		int year = getCreationPart(header, CREATION_YEAR);
		int month = getCreationPart(header, CREATION_MONTH);
		int day = getCreationPart(header, CREATION_DAY);
		int hour = getCreationPart(header, CREATION_HOUR);
		int min = getCreationPart(header, CREATION_MIN);
		int sec = getCreationPart(header, CREATION_SEC);

		if (year != 0 || month != 0 || day != 0 || hour != 0 || min != 0 || sec != 0) {
			GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
			cal.set(year, month - 1, day, hour, min, sec);
			cal.set(Calendar.MILLISECOND, 0);
			return cal;
		}

		return null;
	}

	private static int getCreationPart(byte[] header, int off) {
		int part = header[off] & FF_FLAG;
		part <<= BITSINBYTE;
		part += header[off + 1] & FF_FLAG;
		return part;
	}

	private static String getStringTag(byte[] profileBytes, String tagName, boolean isCprt) {
		if (profileBytes.length < HEADER_SIZE + REQUIRED_LENGTH) {
			return null;
		}

		int tagsNumberRemained = byteArrayToInt(Arrays.copyOfRange(profileBytes, HEADER_SIZE, HEADER_SIZE + REQUIRED_LENGTH));

		int curOffset = HEADER_SIZE + REQUIRED_LENGTH;

		while (tagsNumberRemained-- > 0 && curOffset + TAGINFO_LENGTH <= profileBytes.length) {
			String tag = new String(Arrays.copyOfRange(profileBytes, curOffset, curOffset + REQUIRED_LENGTH));
			if (tag.equals(tagName)) {
				curOffset += REQUIRED_LENGTH;
				int offset = byteArrayToInt(Arrays.copyOfRange(profileBytes, curOffset,
						curOffset + REQUIRED_LENGTH));
				curOffset += REQUIRED_LENGTH;
				int length = byteArrayToInt(Arrays.copyOfRange(profileBytes, curOffset,
						curOffset + REQUIRED_LENGTH));
				if (profileBytes.length < offset + length) {
					return null;
				}

				String type = new String(Arrays.copyOfRange(profileBytes, offset, offset + REQUIRED_LENGTH));
				if ("mluc".equals(type)) {

					int number = byteArrayToInt(Arrays.copyOfRange(profileBytes, offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_BEGIN,
							offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END));
					int recOffset = offset + NUMBER_OF_RECORDS_IN_MULTILOCALIZEDUNICODETYPE_END + REQUIRED_LENGTH;
					for (int i = 0; i < number; ++i) {
						String local = getString(profileBytes, recOffset, recOffset + REQUIRED_LENGTH);
						if ("enUS".equals(local)) {
							length = byteArrayToInt(Arrays.copyOfRange(profileBytes, recOffset + REQUIRED_LENGTH,
									recOffset + REQUIRED_LENGTH + REQUIRED_LENGTH));
							offset += byteArrayToInt(Arrays.copyOfRange(profileBytes, recOffset + REQUIRED_LENGTH * 2,
									recOffset + REQUIRED_LENGTH * 2 + REQUIRED_LENGTH));
							return new String(Arrays.copyOfRange(profileBytes, offset, offset + length), StandardCharsets.UTF_16BE).trim();
						}
						recOffset += LENGTH_OF_RECORD_IN_MULTILOCALIZEDUNICODETYPE_END;
					}
					return null;
				} else if ("desc".equals(type)) {
					length = byteArrayToInt(Arrays.copyOfRange(profileBytes, offset + FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_BEGIN,
							offset + FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END));
					offset += FIRST_RECORD_STRING_LENGTH_IN_TEXTDESCRIPTIONTYPE_END;
				} else if (isCprt) {
					offset += REQUIRED_LENGTH;
					length -= REQUIRED_LENGTH;
				} else {
					return null;
				}

				return new String(Arrays.copyOfRange(profileBytes, offset, offset + length), StandardCharsets.US_ASCII).trim();
			}
			curOffset += TAGINFO_LENGTH;
		}

		return null;
	}

	private static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < REQUIRED_LENGTH; i++) {
			int shift = (REQUIRED_LENGTH - 1 - i) * BITSINBYTE;
			value += (b[i] & FF_FLAG) << shift;
		}
		return value;
	}
}
