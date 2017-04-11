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

import org.verapdf.features.objects.PostScriptFeaturesObjectAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Features object adapter for postscript xobject
 *
 * @author Maksim Bezrukov
 */
public class PBPostScriptXObjectFeaturesObjectAdapter implements PostScriptFeaturesObjectAdapter {

	private String id;

	/**
	 * Constructs new tilling pattern features object adapter
	 *
	 * @param id            id of the object
	 */
	public PBPostScriptXObjectFeaturesObjectAdapter(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}
}
