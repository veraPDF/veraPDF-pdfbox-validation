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

import java.util.logging.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.verapdf.features.objects.FontFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Feature object for fonts
 *
 * @author Maksim Bezrukov
 */
public class PBFontFeaturesObjectAdapter implements FontFeaturesObjectAdapter {

	private static final Logger LOGGER = Logger
			.getLogger(PBFontFeaturesObjectAdapter.class.getCanonicalName());

	private PDFontLike fontLike;
	private String id;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;

	private String type;
	private String baseFont;
	private Long firstChar;
	private Long lastChar;
	private String encoding;
	private double[] bbox;
	private double[] matrix;
	private boolean isCIDSystemInfoPresent;
	private Double defaultWidth;
	private String cidSysInfoRegistry;
	private String cidSysInfoOrdering;
	private Long cidSysInfoSupplement;
	private PDFontDescriptorAdapter fontDescriptor;

	/**
	 * Constructs new font features object
	 *
	 * @param fontLike        PDFontLike which represents font for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this font
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this font
	 * @param patternChild    set of pattern id which contains in resource dictionary of this font
	 * @param shadingChild    set of shading id which contains in resource dictionary of this font
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this font
	 * @param fontChild       set of font id which contains in resource dictionary of this font
	 * @param propertiesChild set of properties id which contains in resource dictionary of this font
	 */
	public PBFontFeaturesObjectAdapter(PDFontLike fontLike, String id, Set<String> extGStateChild,
									   Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild,
									   Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild) {
		this.fontLike = fontLike;
		this.id = id;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
		init();
	}

	private void init() {
		if (fontLike != null) {
			if (fontLike instanceof PDFont) {
				PDFont font = (PDFont) fontLike;
				this.type = font.getSubType();
				if (!(font instanceof PDType3Font)) {
					this.baseFont = font.getName();
				}

				if (font instanceof PDSimpleFont) {
					PDSimpleFont sFont = (PDSimpleFont) font;

					int fc = sFont.getCOSObject().getInt(COSName.FIRST_CHAR);
					if (fc != -1) {
						this.firstChar = Long.valueOf(fc);
					}
					int lc = sFont.getCOSObject().getInt(COSName.LAST_CHAR);
					if (lc != -1) {
						this.lastChar = Long.valueOf(lc);
					}
					COSBase enc = sFont.getCOSObject().getDictionaryObject(COSName.ENCODING);
					if (enc instanceof COSName) {
						this.encoding = ((COSName) enc).getName();
					} else if (enc instanceof COSDictionary) {
						COSBase name = ((COSDictionary) enc).getDictionaryObject(COSName.BASE_ENCODING);
						if (name instanceof COSName) {
							this.encoding = ((COSName) name).getName();
						}
					}
					if (sFont instanceof PDType3Font) {
						PDType3Font type3 = (PDType3Font) sFont;
						this.bbox = PBAdapterHelper.parseRectangle(type3.getFontBBox());
						this.matrix = PBAdapterHelper.parseFloatMatrix(type3.getFontMatrix().getValues());

					}
				}

			} else if (fontLike instanceof PDCIDFont) {
				PDCIDFont cid = (PDCIDFont) fontLike;
				this.type = cid.getCOSObject().getNameAsString(COSName.SUBTYPE);
				this.baseFont = cid.getBaseFont();
				COSBase dw = cid.getCOSObject().getDictionaryObject(COSName.DW);
				if (dw instanceof COSInteger) {
					this.defaultWidth = Double.valueOf(((COSNumber) dw).intValue());
				}

				PDCIDSystemInfo cidSystemInfo = cid.getCIDSystemInfo();
				this.isCIDSystemInfoPresent = cidSystemInfo != null;
				if (this.isCIDSystemInfoPresent) {
					this.cidSysInfoRegistry = cidSystemInfo.getRegistry();
					this.cidSysInfoOrdering = cidSystemInfo.getOrdering();
					this.cidSysInfoSupplement = Long.valueOf(cidSystemInfo.getSupplement());

				}
			}

			PDFontDescriptor fontDescriptor = fontLike.getFontDescriptor();
			if (fontDescriptor != null) {
				this.fontDescriptor = new PDFontDescriptorAdapter(fontDescriptor);
			}
		}
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Set<String> getExtGStateChild() {
		return this.extGStateChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.extGStateChild);
	}

	@Override
	public Set<String> getColorSpaceChild() {
		return this.colorSpaceChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.colorSpaceChild);
	}

	@Override
	public Set<String> getPatternChild() {
		return this.patternChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.patternChild);
	}

	@Override
	public Set<String> getShadingChild() {
		return this.shadingChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.shadingChild);
	}

	@Override
	public Set<String> getXObjectChild() {
		return this.xobjectChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.xobjectChild);
	}

	@Override
	public Set<String> getFontChild() {
		return this.fontChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.fontChild);
	}

	@Override
	public Set<String> getPropertiesChild() {
		return this.propertiesChild == null ?
				Collections.<String>emptySet() : Collections.unmodifiableSet(this.propertiesChild);
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getBaseFont() {
		return this.baseFont;
	}

	@Override
	public Long getFirstChar() {
		return this.firstChar;
	}

	@Override
	public Long getLastChar() {
		return this.lastChar;
	}

	@Override
	public String getEncoding() {
		return this.encoding;
	}

	@Override
	public double[] getBoundingBox() {
		return this.bbox;
	}

	@Override
	public double[] getMatrix() {
		return this.matrix;
	}

	@Override
	public boolean isCIDSystemInfoPresent() {
		return this.isCIDSystemInfoPresent;
	}

	@Override
	public Double getDefaultWidth() {
		return this.defaultWidth;
	}

	@Override
	public String getCIDSysInfoRegistry() {
		return this.cidSysInfoRegistry;
	}

	@Override
	public String getCIDSysInfoOrdering() {
		return this.cidSysInfoOrdering;
	}

	@Override
	public Long getCIDSysInfoSupplement() {
		return this.cidSysInfoSupplement;
	}

	@Override
	public FontDescriptorAdapter getFontDescriptor() {
		return this.fontDescriptor;
	}

	@Override
	public boolean isPDFObjectPresent() {
		return this.fontLike != null;
	}

	@Override
	public List<String> getErrors() {
		return Collections.emptyList();
	}

	private static class PDFontDescriptorAdapter implements FontDescriptorAdapter {

		private PDFontDescriptor descriptor;
		private PDStream file;

		PDFontDescriptorAdapter(PDFontDescriptor descriptor) {
			this.descriptor = descriptor;
			file = descriptor.getFontFile();
			if (file == null) {
				file = descriptor.getFontFile2();
			}
			if (file == null) {
				file = descriptor.getFontFile3();
			}
		}

		@Override
		public String getFontName() {
			return descriptor.getFontName();
		}

		@Override
		public String getFontFamily() {
			return descriptor.getFontFamily();
		}

		@Override
		public String getFontStretch() {
			return descriptor.getFontStretch();
		}

		@Override
		public Double getFontWeight() {
			if (descriptor.getCOSObject().containsKey(COSName.FONT_WEIGHT)) {
				return Double.valueOf(descriptor.getFontWeight());
			}
			return null;
		}

		@Override
		public boolean isFixedPitch() {
			return descriptor.isFixedPitch();
		}

		@Override
		public boolean isSerif() {
			return descriptor.isSerif();
		}

		@Override
		public boolean isSymbolic() {
			return descriptor.isSymbolic();
		}

		@Override
		public boolean isScript() {
			return descriptor.isScript();
		}

		@Override
		public boolean isNonSymbolic() {
			return descriptor.isNonSymbolic();
		}

		@Override
		public boolean isItalic() {
			return descriptor.isItalic();
		}

		@Override
		public boolean isAllcap() {
			return descriptor.isAllCap();
		}

		@Override
		public boolean isSmallCap() {
			return descriptor.isSmallCap();
		}

		@Override
		public boolean isForceBold() {
			return descriptor.isForceBold();
		}

		@Override
		public double[] getFontBoundingBox() {
			return PBAdapterHelper.parseRectangle(descriptor.getFontBoundingBox());

		}

		@Override
		public Double getItalicAngle() {
			return Double.valueOf(descriptor.getItalicAngle());
		}

		@Override
		public Double getAscent() {
			return Double.valueOf(descriptor.getAscent());
		}

		@Override
		public Double getDescent() {
			return Double.valueOf(descriptor.getDescent());
		}

		@Override
		public Double getLeading() {
			return Double.valueOf(descriptor.getLeading());
		}

		@Override
		public Double getCapHeight() {
			return Double.valueOf(descriptor.getCapHeight());
		}

		@Override
		public Double getXHeight() {
			return Double.valueOf(descriptor.getXHeight());
		}

		@Override
		public Double getStemV() {
			if (descriptor.getCOSObject().containsKey(COSName.STEM_V)) {
				return Double.valueOf(descriptor.getStemV());
			}
			return null;
		}

		@Override
		public Double getStemH() {
			return Double.valueOf(descriptor.getStemH());
		}

		@Override
		public Double getAverageWidth() {
			return Double.valueOf(descriptor.getAverageWidth());
		}

		@Override
		public Double getMaxWidth() {
			return Double.valueOf(descriptor.getMaxWidth());
		}

		@Override
		public Double getMissingWidth() {
			return Double.valueOf(descriptor.getMissingWidth());
		}

		@Override
		public String getCharSet() {
			return descriptor.getCharSet();
		}

		@Override
		public boolean isEmbedded() {
			return file != null;
		}

		@Override
		public Long getFlags() {
			COSBase fl = descriptor.getCOSObject().getDictionaryObject(COSName.FLAGS);
			if (fl instanceof COSInteger) {
				return Long.valueOf(((COSInteger) fl).intValue());
			}
			return null;
		}

		@Override
		public InputStream getMetadataStream() {
			if (file != null) {
				return PBAdapterHelper.getMetadataStream(file.getMetadata());
			}
			return null;
		}

		@Override
		public InputStream getData() {
			if (file != null) {
				COSStream stream = file.getStream();
				if (stream != null) {
					try {
						return stream.getUnfilteredStream();
					} catch (IOException e) {
						LOGGER.log(java.util.logging.Level.INFO, "Error while obtaining unfiltered font stream. " + e.getMessage());
					}
				}
			}
			return null;
		}
	}
}
