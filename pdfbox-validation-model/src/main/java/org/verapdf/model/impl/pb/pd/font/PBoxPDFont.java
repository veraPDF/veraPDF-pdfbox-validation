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
package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.external.FontProgram;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.impl.pb.external.PBoxFontProgram;
import org.verapdf.model.impl.pb.external.PBoxTrueTypeFontProgram;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.IDGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDFont extends PBoxPDResources implements PDFont {

	public static final String FONT_FILE = "fontFile";
	public static final String BASE_FONT = "BaseFont";

	protected final RenderingMode renderingMode;
	private final String id;

	protected PBoxPDFont(PDFontLike font, RenderingMode renderingMode, final String type) {
		super(font, type);
		this.renderingMode = renderingMode;
		this.id = IDGenerator.generateID(font);
	}

	@Override
	public String getfontFileSubtype() {
		PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
		PDStream fontFile = fontDescriptor.getFontFile();
		if (fontFile == null) {
			fontFile = fontDescriptor.getFontFile2();
			if (fontFile == null) {
				fontFile = fontDescriptor.getFontFile3();
			}
		}
		if (fontFile == null) {
			return null;
		}
		COSBase subtype = ((COSStream) fontFile.getCOSObject()).getItem(COSName.SUBTYPE);
		return ((COSName) subtype).getName();
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getType() {
		String type = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			type = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike).getType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			type = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(COSName.TYPE);
		}
		return type;
	}

	@Override
	public String getfontName() {
		return this.pdFontLike.getName();
	}

	@Override
	public String getSubtype() {
		String subtype = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			subtype = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike).getSubType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			subtype = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(COSName.SUBTYPE);
		}
		return subtype;
	}

	@Override
	public Boolean getisSymbolic() {
		PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
		return Boolean.valueOf(fontDescriptor.isSymbolic());
	}

	@Override
	public Long getrenderingMode() {
		return Long.valueOf(this.renderingMode.intValue());
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case FONT_FILE:
			return this.getFontFile();
		case BASE_FONT:
			return this.getBaseFont();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<FontProgram> getFontFile() {
		if (!getSubtype().equals(FontFactory.TYPE_3) && (this.pdFontLike.isEmbedded())) {
			if (getSubtype().equals(FontFactory.TRUE_TYPE)) {
				PBoxTrueTypeFontProgram trueTypeFontProgram = new PBoxTrueTypeFontProgram(
						((PDTrueTypeFont) this.pdFontLike).getTrueTypeFont(), getisSymbolic());
				return PBoxPDFont.getFontProgramList(trueTypeFontProgram);
			}
			PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
			PDStream fontFile;
			if (getSubtype().equals(FontFactory.TYPE_1)) {
				if (this.pdFontLike instanceof PDType1CFont) {
					fontFile = fontDescriptor.getFontFile3();
				} else {
					fontFile = fontDescriptor.getFontFile();
				}
			} else if (getSubtype().equals(FontFactory.CID_FONT_TYPE_2)) {
				fontFile = fontDescriptor.getFontFile2();
			} else {
				fontFile = fontDescriptor.getFontFile3();
			}
			if (fontFile != null) {
				return PBoxPDFont.getFontProgramList(new PBoxFontProgram(fontFile));
			}
		}
		return Collections.emptyList();
	}

	private static List<FontProgram> getFontProgramList(FontProgram fontProgram) {
		List<FontProgram> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		list.add(fontProgram);
		return Collections.unmodifiableList(list);
	}

	private List<CosUnicodeName> getBaseFont() {
		String name = this.pdFontLike.getName();
		if (name != null) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosUnicodeName(COSName.getPDFName(name)));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
