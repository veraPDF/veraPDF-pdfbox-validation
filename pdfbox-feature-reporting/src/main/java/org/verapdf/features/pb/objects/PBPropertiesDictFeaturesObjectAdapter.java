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

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.features.objects.PropertiesDictFeaturesObjectAdapter;

import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class PBPropertiesDictFeaturesObjectAdapter implements PropertiesDictFeaturesObjectAdapter {

	private COSDictionary properties;
	private String id;

	/**
	 * Constructs new propertiesDict features object adapter
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 */
	public PBPropertiesDictFeaturesObjectAdapter(COSDictionary properties, String id) {
		this.properties = properties;
		this.id = id;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getType() {
		if (this.properties != null) {
			COSBase type = properties.getDictionaryObject(COSName.TYPE);
			if (type instanceof COSName) {
				return ((COSName) type).getName();
			}
		}
		return null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
