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
package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;
import org.verapdf.model.tools.IDGenerator;

import java.io.IOException;

/**
 * @author Timur Kamalov
 */
public class PBGlyph extends GenericModelObject implements Glyph {

	private static final Logger LOGGER = Logger.getLogger(PBGlyph.class);

	public final static String GLYPH_TYPE = "Glyph";

	private final String id;

	private Boolean glyphPresent;
	private Boolean widthsConsistent;
	private String name;
	private String toUnicode;
	private Long renderingMode;

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, int renderingMode) {
		this(glyphPresent, widthsConsistent, font, glyphCode, GLYPH_TYPE, renderingMode);
	}

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, String type, int renderingMode) {
		super(type);
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;
		this.renderingMode = Long.valueOf(renderingMode);

		if (font instanceof PDSimpleFont) {
			Encoding encoding = ((PDSimpleFont) font).getEncoding();
			this.name = encoding == null ? null : encoding.getName(glyphCode);
		} else if (font instanceof PDType0Font){
			try {
				if (((PDType0Font) font).codeToGID(glyphCode) == 0) {
					this.name = ".notdef";
				} else {
					this.name = null;
				}
			} catch (IOException e) {
				LOGGER.debug("Can't convert code to glyph",e);
				this.name = null;
			}
		}

		try {
			this.toUnicode = font.toUnicode(glyphCode);
		} catch (IOException e) {
			LOGGER.debug(e);
			this.toUnicode = null;
		}
		this.id = IDGenerator.generateID(font.getCOSObject().hashCode(), font.getName(), glyphCode, renderingMode);
	}

	@Override
	public String getname() {
		return this.name;
	}

	@Override
	public Boolean getisGlyphPresent() {
		return this.glyphPresent;
	}

	@Override
	public String gettoUnicode() {
		return this.toUnicode;
	}

	@Override
	public Long getrenderingMode() {
		return this.renderingMode;
	}

	@Override
	public Boolean getisWidthConsistent() {
		return this.widthsConsistent;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public Boolean getunicodePUA() {
		for (int i = 0; i < toUnicode.length(); ++i) {
			char unicode = this.toUnicode.charAt(0);
			if (unicode >= 0xE000 && unicode <= 0xF8FF) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Boolean getactualTextPresent() {
		// actual text obtaining should be implemented
		return Boolean.FALSE;
	}
}