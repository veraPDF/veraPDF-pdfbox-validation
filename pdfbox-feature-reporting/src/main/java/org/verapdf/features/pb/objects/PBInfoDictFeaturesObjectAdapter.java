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

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.features.objects.InfoDictFeaturesObjectAdapter;

import java.util.*;

/**
 * Feature object adapter for information dictionary
 *
 * @author Maksim Bezrukov
 */
public class PBInfoDictFeaturesObjectAdapter implements InfoDictFeaturesObjectAdapter {

	private static final String[] predefinedKeys = {"Title", "Author", "Subject", "Keywords", "Creator", "Producer", "CreationDate", "ModDate", "Trapped"};

	private PDDocumentInformation info;

	/**
	 * Constructs new information dictionary feature object adapter.
	 *
	 * @param info pdfbox class represents page object
	 */
	public PBInfoDictFeaturesObjectAdapter(PDDocumentInformation info) {
		this.info = info;
	}

	@Override
	public String getTitle() {
		if (info != null) {
			return info.getTitle();
		}
		return null;
	}

	@Override
	public String getAuthor() {
		if (info != null) {
			return info.getAuthor();
		}
		return null;
	}

	@Override
	public String getSubject() {
		if (info != null) {
			return info.getSubject();
		}
		return null;
	}

	@Override
	public String getKeywords() {
		if (info != null) {
			return info.getKeywords();
		}
		return null;
	}

	@Override
	public String getCreator() {
		if (info != null) {
			return info.getCreator();
		}
		return null;
	}

	@Override
	public String getProducer() {
		if (info != null) {
			return info.getProducer();
		}
		return null;
	}

	@Override
	public Calendar getCreationDate() {
		if (info != null) {
			return info.getCreationDate();
		}
		return null;
	}

	@Override
	public Calendar getModDate() {
		if (info != null) {
			return info.getModificationDate();
		}
		return null;
	}

	@Override
	public String getTrapped() {
		if (info != null) {
			return info.getTrapped();
		}
		return null;
	}

	@Override
	public Map<String, String> getCustomValues() {
		if (info != null) {
			Set<String> metadataKeys = info.getMetadataKeys();
			if (metadataKeys != null) {
				Map<String, String> res = new HashMap<>();
				Set<String> keys = new TreeSet<>(metadataKeys);
				keys.removeAll(Arrays.asList(predefinedKeys));
				for (String key : keys) {
					res.put(key, info.getCustomMetadataValue(key));
				}
				return Collections.unmodifiableMap(res);
			}
		}
		return Collections.emptyMap();
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.info != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
