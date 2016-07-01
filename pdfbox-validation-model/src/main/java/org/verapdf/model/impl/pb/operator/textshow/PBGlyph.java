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

}