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
import org.apache.fontbox.ttf.CmapSubtable;
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

	private Long nrCmaps = 0L;
	private Boolean cmap30Present = Boolean.FALSE;
	private Boolean cmap31Present = Boolean.FALSE;
	private Boolean cmap10Present = Boolean.FALSE;
	private Boolean isSymbolic;

	/**
	 * Default constructor.
	 *
	 * @param fontProgram processed font program stream
	 * @param isSymbolic
	 */
	public PBoxTrueTypeFontProgram(FontBoxFont fontProgram, Boolean isSymbolic) {
		super(fontProgram, TRUE_TYPE_PROGRAM_TYPE);
		this.isSymbolic = isSymbolic;
		try {
			CmapTable cmap = ((TrueTypeFont) this.fontProgram).getCmap();
			if (cmap != null) {
				CmapSubtable[] cmaps = cmap.getCmaps();
				this.nrCmaps = Long.valueOf(cmaps.length);
				for (CmapSubtable cmapSubtable : cmaps) {
					int platformId = cmapSubtable.getPlatformId();
					int platformEncodingId = cmapSubtable.getPlatformEncodingId();
					this.cmap31Present |= platformId == 3 && platformEncodingId == 1;
					this.cmap10Present |= platformId == 1 && platformEncodingId == 0;
					this.cmap30Present |= platformId == 3 && platformEncodingId == 0;
				}
			}
		} catch (IOException e) {
			LOGGER.debug(e);
		}
	}

	/**
	 * @return number of CMap`s
	 */
	@Override
	public Long getnrCmaps() {
		return this.nrCmaps;
	}

	@Override
	public Boolean getisSymbolic() {
		return this.isSymbolic;
	}

	@Override
	public Boolean getcmap30Present() {
		return this.cmap30Present;
	}

	@Override
	public Boolean getcmap31Present() {
		return this.cmap31Present;
	}

	@Override
	public Boolean getcmap10Present() {
		return this.cmap10Present;
	}

}
