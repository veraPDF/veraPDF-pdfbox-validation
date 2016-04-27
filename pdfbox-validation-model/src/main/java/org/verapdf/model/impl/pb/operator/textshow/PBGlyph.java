package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
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

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode) {
		this(glyphPresent, widthsConsistent, font, glyphCode, GLYPH_TYPE);
	}

	public PBGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, String type) {
		super(type);
		this.glyphPresent = glyphPresent;
		this.widthsConsistent = widthsConsistent;

		if (font instanceof PDSimpleFont) {
			Encoding encoding = ((PDSimpleFont) font).getEncoding();
			this.name = encoding == null ? null : encoding.getName(glyphCode);
		} else {
			this.name = null;
		}

		try {
			this.toUnicode = font.toUnicode(glyphCode);
		} catch (IOException e) {
			LOGGER.debug(e);
			this.toUnicode = null;
		}
		this.id = IDGenerator.generateID(font.getCOSObject().hashCode(), font.getName(), glyphCode);
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
		return null;
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