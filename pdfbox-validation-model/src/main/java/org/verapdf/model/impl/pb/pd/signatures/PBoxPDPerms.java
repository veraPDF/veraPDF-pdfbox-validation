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
package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDPerms;

import java.util.Set;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDPerms extends PBoxPDObject implements PDPerms{

	/** Type name for {@code PBoxPDPerms} */
	public static final String PERMS_TYPE = "PDPerms";

	private static COSName UR3 = COSName.getPDFName("UR3");

	/**
	 * @param dictionary is permissions dictionary.
	 */
	public PBoxPDPerms(COSDictionary dictionary) {
		super(dictionary, PERMS_TYPE);
	}

	/**
	 * @return true if the permissions dictionary contains entries other than
	 * DocMDP and UR3.
	 */
	@Override
	public Boolean getcontainsOtherEntries() {
		Set<COSName> names = ((COSDictionary) this.simplePDObject).keySet();
		for (COSName name : names) {
			if (name.compareTo(UR3) != 0 && name.compareTo(COSName.DOC_MDP) != 0) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
