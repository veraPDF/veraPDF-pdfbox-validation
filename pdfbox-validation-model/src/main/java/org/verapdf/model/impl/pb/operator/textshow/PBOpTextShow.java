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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.preflight.font.container.FontContainer;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.FontHelper;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Base class for all text show operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpTextShow extends PBOperator implements OpTextShow {

	private static final Logger LOGGER = Logger.getLogger(PBOpTextShow.class);

	private static final String MSG_PROBLEM_OBTAINING_RESOURCE = "Problem encountered while obtaining resources for ";

	/** Name of link to the used font */
	public static final String FONT = "font";
	/** Name of link to the used glyphs */
	public static final String USED_GLYPHS = "usedGlyphs";
	/** Name of link to the fill color space */
	public static final String FILL_COLOR_SPACE = "fillCS";
	/** Name of link to the stroke color space */
	public static final String STROKE_COLOR_SPACE = "strokeCS";

	protected final GraphicState state;
	private final PDInheritableResources resources;

	protected final PDDocument document;
	protected final PDFAFlavour flavour;

	private List<PDFont> fonts = null;
	private List<PDColorSpace> fillCS = null;
	private List<PDColorSpace> strokeCS = null;

	protected PBOpTextShow(List<COSBase> arguments, GraphicState state, PDInheritableResources resources,
			final String opType, PDDocument document, PDFAFlavour flavour) {
		super(arguments, opType);
		this.state = state;
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case FONT:
			return this.getFont();
		case USED_GLYPHS:
			return this.getUsedGlyphs();
		case FILL_COLOR_SPACE:
			return this.getFillColorSpace();
		case STROKE_COLOR_SPACE:
			return this.getStrokeColorSpace();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<PDFont> getFont() {
		if (this.fonts == null) {
			this.fonts = parseFont();
		}
		return this.fonts;
	}

	public PDFont getVeraModelFont() {
		if (this.fonts == null) {
			this.fonts = parseFont();
		}
		return this.fonts.isEmpty() ? null : this.fonts.get(0);
	}

	private List<PDFont> parseFont() {
		PDFont font = FontFactory.parseFont(getFontFromResources(), this.state.getRenderingMode(), this.resources,
				this.document, this.flavour);
		if (font != null) {
			List<PDFont> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			result.add(font);
			return Collections.unmodifiableList(result);
		}
		return Collections.emptyList();
	}

	private List<PBGlyph> getUsedGlyphs() {
		org.apache.pdfbox.pdmodel.font.PDFont font = getFontFromResources();
		FontContainer<? extends PDFontLike> fontContainer = FontHelper.getFontContainer(font);

		if (fontContainer == null) {
			return Collections.emptyList();
		}
        boolean fontProgramIsInvalid = font.isDamaged() || fontProgramIsNull(font);

		List<PBGlyph> res = new ArrayList<>();
		List<byte[]> strings = getStrings(this.arguments);
		for (byte[] string : strings) {
			try (InputStream inputStream = new ByteArrayInputStream(string)) {
				while (inputStream.available() > 0) {
					int code = font.readCode(inputStream);
					Boolean glyphPresent = null;
					Boolean widthsConsistent = null ;
					if(!fontProgramIsInvalid) {
						// every font contains notdef glyph. But if we call method
						// of font container we can't distinguish case of code 0
						// and glyph that is not present indeed.
						glyphPresent = code == 0 ? true :
								Boolean.valueOf(fontContainer.hasGlyph(code));
						widthsConsistent = Boolean.valueOf(this.checkWidths(code));
					}
					PBGlyph glyph;
					if (font.getSubType().equals(FontFactory.TYPE_0)) {
						int CID = ((PDType0Font) font).codeToCID(code);
						glyph = new PBCIDGlyph(glyphPresent, widthsConsistent, font, code, CID,
								this.state.getRenderingMode().intValue());
					} else {
						glyph = new PBGlyph(glyphPresent, widthsConsistent, font, code,
								this.state.getRenderingMode().intValue());
					}
					res.add(glyph);
				}
			} catch (IOException e) {
				LOGGER.debug("Error processing text show operator's string argument : " + new String(string));
				LOGGER.info(e);
			}
		}
		return res;
	}

	private List<PDColorSpace> getFillColorSpace() {
		if (this.fillCS == null) {
			this.fillCS = parseFillColorSpace();
		}
		return this.fillCS;
	}

	/**
	 * @return char codes that has been used by this operator
	 */
	public byte[] getCharCodes() {
		List<byte[]> strings = PBOpTextShow.getStrings(this.arguments);
		Set<Byte> resSet = new HashSet<>();
		for (byte[] string : strings) {
			for (byte b : string) {
				resSet.add(Byte.valueOf(b));
			}
		}
		byte[] res = new byte[resSet.size()];
		int i = 0;
		for (Byte b : resSet) {
			res[i++] = b.byteValue();
		}
		return res;
	}

	/**
	 * @return fill color space object from veraPDF model of current operator
	 */
	public PDColorSpace getVeraModelFillColorSpace() {
		if (this.fillCS == null) {
			this.fillCS = parseFillColorSpace();
		}
		return this.fillCS.isEmpty() ? null : this.fillCS.get(0);
	}

	private List<PDColorSpace> getStrokeColorSpace() {
		if (this.strokeCS == null) {
			this.strokeCS = parseStrokeColorSpace();
		}
		return this.strokeCS;
	}

	/**
	 * @return stroke color space object from veraPDF model of current operator
	 */
	public PDColorSpace getVeraModelStrokeColorSpace() {
		if (this.strokeCS == null) {
			this.strokeCS = parseStrokeColorSpace();
		}
		return this.strokeCS.isEmpty() ? null : this.strokeCS.get(0);
	}

	private List<PDColorSpace> parseFillColorSpace() {
		if (this.state.getRenderingMode().isFill()) {
			return this.getColorSpace(this.state.getFillColorSpace(), this.state.getFillPattern(),
					this.state.isOverprintingFlagNonStroke());
		}
		return Collections.emptyList();
	}

	private List<PDColorSpace> parseStrokeColorSpace() {
		if (this.state.getRenderingMode().isStroke()) {
			return this.getColorSpace(this.state.getStrokeColorSpace(), this.state.getStrokePattern(),
					this.state.isOverprintingFlagStroke());
		}
		return Collections.emptyList();
	}

	private List<PDColorSpace> getColorSpace(org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace usedColorSpace,
			PDAbstractPattern pattern, boolean op) {
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(usedColorSpace, pattern, this.resources,
				this.state.getOpm(), op, this.document, this.flavour);
		if (colorSpace != null) {
			List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			colorSpaces.add(colorSpace);
			return Collections.unmodifiableList(colorSpaces);
		}
		return Collections.emptyList();
	}

	private boolean checkWidths(int glyphCode) throws IOException {
		org.apache.pdfbox.pdmodel.font.PDFont font = getFontFromResources();
		float expectedWidth = font.getWidth(glyphCode);
		float foundWidth = font.getWidthFromFont(glyphCode);
		// consistent is defined to be a difference of no more than 1/1000 unit.
		return Math.abs(foundWidth - expectedWidth) <= 1;
	}

	private static List<byte[]> getStrings(List<COSBase> argList) {
		if (!argList.isEmpty()) {
			List<byte[]> res = new ArrayList<>();
			COSBase arg = argList.get(0);
			if (arg instanceof COSArray) {
				addArrayElements(res, (COSArray) arg);
			} else {
				if (arg instanceof COSString) {
					res.add(((COSString) arg).getBytes());
				}
			}
			return res;
		}
		return Collections.emptyList();
	}

	private static void addArrayElements(List<byte[]> res, COSArray arg) {
		for (COSBase element : arg) {
			if (element instanceof COSString) {
				res.add(((COSString) element).getBytes());
			}
		}
	}

	private org.apache.pdfbox.pdmodel.font.PDFont getFontFromResources() {
		if (resources == null) {
			return null;
		}
		try {
			return resources.getFont(this.state.getFontName());
		} catch (IOException e) {
			LOGGER.debug(MSG_PROBLEM_OBTAINING_RESOURCE + this.state.getFontName().getName() + ". " + e.getMessage(),
					e);
			return null;
		}
	}

	private static boolean fontProgramIsNull(org.apache.pdfbox.pdmodel.font.PDFont font) {
		if(font instanceof PDType3Font) {
			return false;
		} else if(font instanceof PDType0Font) {
			return descendantFontProgramIsNull((PDType0Font) font);
		} else if (!font.getSubType().equals(FontFactory.TYPE_3) && (font.isEmbedded())) {
			PDStream fontFile;
			if (font.getSubType().equals(FontFactory.TYPE_1) ||
					font.getSubType().equals(FontFactory.MM_TYPE_1)) {
				if (font instanceof PDType1CFont) {
					fontFile = font.getFontDescriptor().getFontFile3();
				} else {
					fontFile = font.getFontDescriptor().getFontFile();
				}
			} else if (font.getSubType().equals(FontFactory.CID_FONT_TYPE_2) ||
					font.getSubType().equals(FontFactory.TRUE_TYPE)) {
				fontFile = font.getFontDescriptor().getFontFile2();
			} else {
				fontFile = font.getFontDescriptor().getFontFile3();
			}
			if (fontFile != null) {
				return false;
			}
		}
		return true;
	}

	private static boolean descendantFontProgramIsNull(PDType0Font font) {
		org.apache.pdfbox.pdmodel.font.PDCIDFont descendant = font.getDescendantFont();
		if(descendant instanceof PDCIDFontType2) {
			if(descendant.getFontDescriptor() != null) {
				return descendant.getFontDescriptor().getFontFile3() == null &&
						descendant.getFontDescriptor().getFontFile2() == null;
			}
		} else {
			return descendant.getFontDescriptor().getFontFile3() == null;
		}
		return true;
	}

}
