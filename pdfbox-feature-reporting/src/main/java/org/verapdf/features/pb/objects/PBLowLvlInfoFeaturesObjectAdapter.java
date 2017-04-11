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
import org.verapdf.features.objects.LowLvlInfoFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.io.IOException;
import java.util.*;

/**
 * Feature object adapter for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PBLowLvlInfoFeaturesObjectAdapter implements LowLvlInfoFeaturesObjectAdapter {

	private static final Logger LOGGER = Logger
			.getLogger(PBLowLvlInfoFeaturesObjectAdapter.class);

	private int objectsNumber;
	private String creationId;
	private String modId;
	private Set<String> filters;
	private List<String> errors;
	private static final Map<String, String> filtersAbbreviations;

	static {
		Map<String, String> filtersAbbreviationsTemp = new HashMap<>();
		filtersAbbreviationsTemp.put("AHx", "ASCIIHexDecode");
		filtersAbbreviationsTemp.put("A85", "ASCII85Decode");
		filtersAbbreviationsTemp.put("LZW", "LZWDecode");
		filtersAbbreviationsTemp.put("Fl", "FlateDecode");
		filtersAbbreviationsTemp.put("RL", "RunLengthDecode");
		filtersAbbreviationsTemp.put("CCF", "CCITTFaxDecode");
		filtersAbbreviationsTemp.put("DCT", "DCTDecode");
		filtersAbbreviations = Collections.unmodifiableMap(filtersAbbreviationsTemp);
	}

	/**
	 * Constructs new low level info feature object adapter.
	 *
	 * @param document
	 *            pdfbox class represents document object
	 */
	public PBLowLvlInfoFeaturesObjectAdapter(COSDocument document) {
		if (document != null) {
			List<COSObject> objects = document.getObjects();
			this.errors = new ArrayList<>();
			if (objects != null) {
				this.objectsNumber = objects.size();
			}
			addDocumentId(document.getDocumentID());
			this.filters = getAllFilters(document);
		}
	}

	private Set<String> getAllFilters(COSDocument document) {
		Set<String> res = new HashSet<>();
		for (COSBase base : document.getObjects()) {
			while (base instanceof COSObject) {
				base = ((COSObject) base).getObject();
			}
			if (base instanceof COSStream) {
				try (COSStream stream = (COSStream) base) {
					COSBase baseFilter = stream.getFilters();
					if (baseFilter != null) {
						addFiltersFromBase(res, baseFilter);
					}
				} catch (IOException excep) {
					LOGGER.debug(excep);
					this.errors.add(excep.getMessage());
				}
			}
		}
		return res;
	}

	private void addDocumentId(COSArray ids) {
		if (ids != null) {
			this.creationId = PBAdapterHelper.getStringFromBase(ids.get(0));
			this.modId = PBAdapterHelper.getStringFromBase(ids.get(1));
			if (ids.size() != 2 || creationId == null || modId == null) {
				this.errors.add("Document's ID must be an array of two not null elements");
			}
		}
	}

	private static void addFiltersFromBase(Set<String> res, COSBase base) {
		if (base instanceof COSName) {
			String name = ((COSName) base).getName();
			if (filtersAbbreviations.keySet().contains(name)) {
				name = filtersAbbreviations.get(name);
			}
			res.add(name);
		} else if (base instanceof COSArray) {
			for (COSBase baseElement : (COSArray) base) {
				if (baseElement instanceof COSName) {
					String name = ((COSName) baseElement).getName();
					if (filtersAbbreviations.keySet().contains(name)) {
						name = filtersAbbreviations.get(name);
					}
					res.add(name);
				}
			}
		}
	}

	@Override
	public int getIndirectObjectsNumber() {
		return this.objectsNumber;
	}

	@Override
	public String getCreationId() {
		return this.creationId;
	}

	@Override
	public String getModificationId() {
		return this.modId;
	}

	@Override
	public Set<String> getFilters() {
		return this.filters == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.filters);
	}

	@Override
	public List<String> getErrors() {
		return this.errors == null ?
				Collections.<String>emptyList() : Collections.unmodifiableList(this.errors);
	}
}
