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

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.ImageFeaturesData;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Features object for image xobject
 *
 * @author Maksim Bezrukov
 */
public class PBImageXObjectFeaturesObject implements IFeaturesObject {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PBImageXObjectFeaturesObject.class);

	private static final String ID = "id";

	private PDImageXObjectProxy imageXObject;
	private String id;
	private String colorSpaceChild;
	private String maskChild;
	private String sMaskChild;
	private Set<String> alternatesChild;

	/**
	 * Constructs new shading features object
	 *
	 * @param imageXObject
	 *            PDImageXObject which represents image xobject for feature
	 *            report
	 * @param id
	 *            id of the object
	 * @param colorSpaceChild
	 *            colorSpace id which contains in this image xobject
	 * @param maskChild
	 *            image xobject id which contains in this image xobject as it's
	 *            mask
	 * @param sMaskChild
	 *            image xobject id which contains in this image xobject as it's
	 *            smask
	 * @param alternatesChild
	 *            set of image xobject ids which contains in this image xobject
	 *            as alternates
	 */
	public PBImageXObjectFeaturesObject(PDImageXObjectProxy imageXObject, String id, String colorSpaceChild,
			String maskChild, String sMaskChild, Set<String> alternatesChild) {
		this.imageXObject = imageXObject;
		this.id = id;
		this.colorSpaceChild = colorSpaceChild;
		this.maskChild = maskChild;
		this.sMaskChild = sMaskChild;
		this.alternatesChild = alternatesChild;
	}

	/**
	 * @return IMAGE_XOBJECT instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.IMAGE_XOBJECT;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection
	 *            collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the
	 *         constructed collection tree
	 * @throws FeatureParsingException
	 *             occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (imageXObject != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
			root.setAttribute("type", "image");
			if (id != null) {
				root.setAttribute(ID, id);
			}

			root.addChild("width").setValue(String.valueOf(imageXObject.getWidth()));
			root.addChild("height").setValue(String.valueOf(imageXObject.getHeight()));

			if (colorSpaceChild != null) {
				FeatureTreeNode shading = root.addChild("colorSpace");
				shading.setAttribute(ID, colorSpaceChild);
			}

			root.addChild("bitsPerComponent")
					.setValue(String.valueOf(imageXObject.getBitsPerComponent()));
			root.addChild("imageMask").setValue(String.valueOf(imageXObject.isStencil()));

			if (maskChild != null) {
				FeatureTreeNode mask = root.addChild("mask");
				mask.setAttribute(ID, maskChild);
			}

			root.addChild("interpolate")
					.setValue(String.valueOf(imageXObject.getInterpolate()));
			PBCreateNodeHelper.parseIDSet(alternatesChild, "alternate", "alternates", root);
			if (sMaskChild != null) {
				FeatureTreeNode mask = root.addChild("sMask");
				mask.setAttribute(ID, sMaskChild);
			}

			if (imageXObject.getCOSStream().getItem(COSName.STRUCT_PARENT) != null) {
				root.addChild("structParent")
						.setValue(String.valueOf(imageXObject.getStructParent()));
			}

			try {
				if (imageXObject.getStream().getFilters() != null && !imageXObject.getStream().getFilters().isEmpty()) {
					FeatureTreeNode filters = root.addChild("filters");
					for (COSName name : imageXObject.getStream().getFilters()) {
						PBCreateNodeHelper.addNotEmptyNode("filter", name.getName(), filters);
					}
				}
			} catch (IOException e) {
				LOGGER.info(e);
			}

			PBCreateNodeHelper.parseMetadata(imageXObject.getMetadata(), "metadata", root, collection);

			collection.addNewFeatureTree(FeatureObjectType.IMAGE_XOBJECT, root);
			return root;
		}

		return null;
	}

	/**
	 * @return null if it can not get image xobject stream and features data of
	 *         the image in other case.
	 */
	@Override
	public FeaturesData getData() {
		try {
			InputStream metadata = null;
			if (imageXObject.getMetadata() != null) {
				try {
					metadata = imageXObject.getMetadata().getStream().getUnfilteredStream();
				} catch (IOException e) {
					LOGGER.debug("Can not get metadata stream for image xobject", e);
				}
			}

			List<ImageFeaturesData.Filter> filters = new ArrayList<>();
			if (imageXObject.getPDStream().getFilters() != null) {
				List<String> filtersNames = new ArrayList<>();
				for (COSName filter : imageXObject.getPDStream().getFilters()) {
					filtersNames.add(filter.getName());
				}

				List<COSDictionary> decodeList = getDecodeList(
						imageXObject.getCOSStream().getDictionaryObject(COSName.DECODE_PARMS));

				for (int i = 0; i < filtersNames.size(); ++i) {
					String filterName = filtersNames.get(i);
					COSDictionary dic = i < decodeList.size() ? decodeList.get(i) : null;
					switch (filterName) {
					case "LZWDecode":
						filters.add(ImageFeaturesData.Filter.newInstance(filterName, createLZWFilterMap(dic),
								null));
						break;
					case "FlateDecode":
						filters.add(ImageFeaturesData.Filter.newInstance(filterName, createFlatFilterMap(dic),
								null));
						break;
					case "CCITTFaxDecode":
						filters.add(ImageFeaturesData.Filter.newInstance(filterName, getCCITTFaxFiltersMap(dic), null));
						break;
					case "DCTDecode":
						filters.add(ImageFeaturesData.Filter.newInstance(filterName, getDCTFiltersMap(dic), null));
						break;
					case "JBIG2Decode":
						InputStream global = null;
						if (dic != null && dic.getDictionaryObject(COSName.JBIG2_GLOBALS) instanceof COSStream) {
							global = ((COSStream) dic.getDictionaryObject(COSName.JBIG2_GLOBALS)).getUnfilteredStream();
						}
						filters.add(ImageFeaturesData.Filter.newInstance(filterName, new HashMap<String, String>(),
								global));
						break;
					case "Crypt":
						if (!(dic != null && COSName.IDENTITY.equals(dic.getCOSName(COSName.NAME)))) {
							LOGGER.debug("An Image has a Crypt filter");
							return null;
						}
						//$FALL-THROUGH$
					default:
						filters.add(
								ImageFeaturesData.Filter.newInstance(filterName, new HashMap<String, String>(), null));
					}
				}
			}

			Integer width = getIntegerWithDefault(imageXObject.getCOSStream().getDictionaryObject(COSName.WIDTH), null);
			Integer height = getIntegerWithDefault(imageXObject.getCOSStream().getDictionaryObject(COSName.HEIGHT),
					null);

			return ImageFeaturesData.newInstance(metadata, imageXObject.getCOSStream().getFilteredStream(), width, height, filters);
		} catch (IOException e) {
			LOGGER.debug("Error in obtaining features data for fonts", e);
			return null;
		}
	}

	private static List<COSDictionary> getDecodeList(COSBase base) {
		List<COSDictionary> res = new ArrayList<>();

		if (base instanceof COSDictionary) {
			res.add((COSDictionary) base);
		} else if (base instanceof COSArray) {
			for (COSBase baseElem : (COSArray) base) {
				if (baseElem instanceof COSDictionary) {
					res.add((COSDictionary) baseElem);
				} else {
					res.add(null);
				}
			}
		}

		return res;
	}

	private static Map<String, String> getCCITTFaxFiltersMap(COSDictionary base) {
		Map<String, String> res = new HashMap<>();
		if (base != null) {
			putIntegerAsStringWithDefault(res, "K", base.getDictionaryObject(COSName.K), Integer.valueOf(0));
			putBooleanAsStringWithDefault(res, "EndOfLine", base.getDictionaryObject(COSName.COLORS), Boolean.FALSE);
			putBooleanAsStringWithDefault(res, "EncodedByteAlign", base.getDictionaryObject(COSName.BITS_PER_COMPONENT),
					Boolean.FALSE);
			putIntegerAsStringWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS),
					Integer.valueOf(1728));
			putIntegerAsStringWithDefault(res, "Rows", base.getDictionaryObject(COSName.ROWS), Integer.valueOf(0));
			putBooleanAsStringWithDefault(res, "EndOfBlock", base.getDictionaryObject(COSName.getPDFName("EndOfBlock")),
					Boolean.TRUE);
			putBooleanAsStringWithDefault(res, "BlackIs1", base.getDictionaryObject(COSName.BLACK_IS_1), Boolean.FALSE);
			putIntegerAsStringWithDefault(res, "DamagedRowsBeforeError",
					base.getDictionaryObject(COSName.getPDFName("DamagedRowsBeforeError")), Integer.valueOf(0));
		} else {
			res.put("K", "0");
			res.put("EndOfLine", "false");
			res.put("EncodedByteAlign", "false");
			res.put("Columns", "1728");
			res.put("Rows", "0");
			res.put("EndOfBlock", "true");
			res.put("BlackIs1", "false");
			res.put("DamagedRowsBeforeError", "0");
		}

		return res;
	}

	private static Map<String, String> getDCTFiltersMap(COSDictionary base) {
		Map<String, String> res = new HashMap<>();
		if (base != null && base.getDictionaryObject(COSName.getPDFName("ColorTransform")) != null
				&& base.getDictionaryObject(COSName.getPDFName("ColorTransform")) instanceof COSInteger) {
			res.put("ColorTransform", String.valueOf(
					((COSInteger) (base).getDictionaryObject(COSName.getPDFName("ColorTransform"))).intValue()));
		}
		return res;
	}

	private static Map<String, String> createLZWFilterMap(COSDictionary base) {
		if (base == null) {
			Map<String, String> retVal = createDefaultFlatFilterMap();
			retVal.put("EarlyChange", "1");
			return retVal;
		}

		Map<String, String> retVal = createFlatFilterMap(base);
		putIntegerAsStringWithDefault(retVal, "EarlyChange", base.getDictionaryObject(COSName.EARLY_CHANGE),
				Integer.valueOf(1));
		return retVal;

	}

	private static Map<String, String> createFlatFilterMap(COSDictionary base) {
		if (base == null)
			return createDefaultFlatFilterMap();

		Map<String, String> res = new HashMap<>();

		putIntegerAsStringWithDefault(res, "Predictor", base.getDictionaryObject(COSName.PREDICTOR),
				Integer.valueOf(1));
		putIntegerAsStringWithDefault(res, "Colors", base.getDictionaryObject(COSName.COLORS), Integer.valueOf(1));
		putIntegerAsStringWithDefault(res, "BitsPerComponent", base.getDictionaryObject(COSName.BITS_PER_COMPONENT),
				Integer.valueOf(8));
		putIntegerAsStringWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS), Integer.valueOf(1));
		return res;
	}

	private static Map<String, String> createDefaultFlatFilterMap() {
		Map<String, String> res = new HashMap<>();
		res.put("Predictor", "1");
		res.put("Colors", "1");
		res.put("BitsPerComponent", "8");
		res.put("Columns", "1");
		return res;
	}

	private static Integer getIntegerWithDefault(Object value, Integer defaultValue) {
		if (value instanceof COSInteger) {
			return Integer.valueOf(((COSInteger) value).intValue());
		}
		return defaultValue;
	}

	private static void putIntegerAsStringWithDefault(Map<String, String> map, String key, Object value,
			Integer defaultValue) {
		if (value instanceof COSInteger) {
			map.put(key, String.valueOf(((COSInteger) value).intValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}

	private static void putBooleanAsStringWithDefault(Map<String, String> map, String key, Object value,
			Boolean defaultValue) {
		if (value instanceof COSBoolean) {
			map.put(key, String.valueOf(((COSBoolean) value).getValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}
}
