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
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.external.FontProgram;

/**
 * @author Timur Kamalov
 */
public class PBoxFontProgram extends PBoxExternal implements FontProgram {

	/** Type name for {@code PBoxFontProgram} */
    public static final String FONT_PROGRAM_TYPE = "FontProgram";

    protected FontBoxFont fontProgram;
    protected PDStream fontProgramStream;

	/**
	 * Default constructor defined by not processed stream
	 *
	 * @param fontProgramStream font program stream
	 */
    public PBoxFontProgram(PDStream fontProgramStream) {
        super(FONT_PROGRAM_TYPE);
        this.fontProgramStream = fontProgramStream;
    }

	/**
	 * Default constructor defined by processed stream
	 * and represented by {@link FontBoxFont}
	 *
	 * @param fontProgram processed font program stream
	 */
    public PBoxFontProgram(FontBoxFont fontProgram) {
		this(fontProgram, FONT_PROGRAM_TYPE);
    }

	/**
	 * Constructor used by child classes
	 *
	 * @param fontProgram processed font program stream
	 * @param type type of child
	 */
    public PBoxFontProgram(FontBoxFont fontProgram, String type) {
        super(type);
        this.fontProgram = fontProgram;
    }

}