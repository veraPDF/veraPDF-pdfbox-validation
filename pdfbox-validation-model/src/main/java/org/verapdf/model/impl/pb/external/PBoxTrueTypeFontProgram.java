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
package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.ttf.CmapTable;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.log4j.Logger;
import org.verapdf.model.external.TrueTypeFontProgram;

import java.io.IOException;

/**
 * Current class is representation of true type font program
 *
 * @author Timur Kamalov
 */
public class PBoxTrueTypeFontProgram extends PBoxFontProgram implements TrueTypeFontProgram {

	private static final Logger LOGGER = Logger.getLogger(PBoxTrueTypeFontProgram.class);

	/** Type name of {@code PBoxTrueTypeFontProgram} */
	public static final String TRUE_TYPE_PROGRAM_TYPE = "TrueTypeFontProgram";

	private final Boolean isSymbolic;

	/**
	 * Default constructor.
	 *
	 * @param fontProgram processed font program stream
	 * @param isSymbolic
	 */
	public PBoxTrueTypeFontProgram(FontBoxFont fontProgram, Boolean isSymbolic) {
		super(fontProgram, TRUE_TYPE_PROGRAM_TYPE);
		this.isSymbolic = isSymbolic;
	}

	/**
	 * @return number of CMap`s
	 */
	@Override
	public Long getnrCmaps() {
		try {
			CmapTable cmap = ((TrueTypeFont) this.fontProgram).getCmap();
			if (cmap != null) {
				int nrCmaps = cmap.getCmaps().length;
				return Long.valueOf(nrCmaps);
			}
		} catch (IOException e) {
			LOGGER.debug(e);
		}
		return null;
	}

	@Override
	public Boolean getisSymbolic() {
		return this.isSymbolic;
	}

	@Override
	// TODO : implement me
	public Boolean getcmap30Present() {
		return Boolean.FALSE;
	}

	@Override
	// TODO : implement me
	public Boolean getcmap31Present() {
		return Boolean.FALSE;
	}

	@Override
	// TODO : implement me
	public Boolean getcmap10Present() {
		return Boolean.FALSE;
	}
}
