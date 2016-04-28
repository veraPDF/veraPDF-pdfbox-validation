package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    protected PBOpTextShow(List<COSBase> arguments,
            GraphicState state, PDInheritableResources resources, final String opType, PDDocument document, PDFAFlavour flavour) {
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
		if (this.state.getRenderingMode().equals(RenderingMode.NEITHER)) {
			return Collections.emptyList();
		}

		PDFont font = FontFactory.parseFont(getFontFromResources(), this.resources, this.document, this.flavour);
		if (font != null) {
			List<PDFont> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			result.add(font);
			return Collections.unmodifiableList(result);
		}
		return Collections.emptyList();
	}

    private List<PBGlyph> getUsedGlyphs() {
		org.apache.pdfbox.pdmodel.font.PDFont font = getFontFromResources();
		FontContainer fontContainer = FontHelper.getFontContainer(font);

		if (fontContainer == null) {
			return Collections.emptyList();
		}

		List<PBGlyph> res = new ArrayList<>();
		List<byte[]> strings = this.getStrings(this.arguments);
        for (byte[] string : strings) {
            try (InputStream inputStream = new ByteArrayInputStream(string)) {
                while (inputStream.available() > 0) {
                    int code = font.readCode(inputStream);
                    boolean glyphPresent = fontContainer.hasGlyph(code);
                    boolean widthsConsistent = this.checkWidths(code);
                    PBGlyph glyph;
					if (font.getSubType().equals(FontFactory.TYPE_0)) {
						int CID = ((PDType0Font) font).codeToCID(code);
						glyph = new PBCIDGlyph(glyphPresent, widthsConsistent,
								font, code, CID);
					} else {
						glyph = new PBGlyph(glyphPresent, widthsConsistent,
								font, code, this.state.getRenderingMode().intValue());
					}
					res.add(glyph);
                }
            } catch (IOException e) {
                LOGGER.error("Error processing text show operator's string argument : "
						+ new String(string));
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
			return this.getColorSpace(this.state.getFillColorSpace(), this.state.getFillPattern());
		} else {
			return Collections.emptyList();
		}
	}

	private List<PDColorSpace> parseStrokeColorSpace() {
		if (this.state.getRenderingMode().isStroke()) {
			return this.getColorSpace(this.state.getStrokeColorSpace(), this.state.getStrokePattern());
		} else {
			return Collections.emptyList();
		}
	}

	private List<PDColorSpace> getColorSpace(org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace usedColorSpace, PDAbstractPattern pattern) {
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(
				usedColorSpace, pattern, this.resources, this.document, this.flavour);
		if (colorSpace != null) {
			List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			colorSpaces.add(colorSpace);
			return Collections.unmodifiableList(colorSpaces);
		}
		return Collections.emptyList();
	}

    private Boolean checkWidths(int glyphCode) throws IOException {
		org.apache.pdfbox.pdmodel.font.PDFont font = getFontFromResources();
		float expectedWidth = font.getWidth(glyphCode);
        float foundWidth = font.getWidthFromFont(glyphCode);
        // consistent is defined to be a difference of no more than 1/1000 unit.
		return Math.abs(foundWidth - expectedWidth) > 1 ? Boolean.FALSE : Boolean.TRUE;
    }

    private List<byte[]> getStrings(List<COSBase> arguments) {
		if (!arguments.isEmpty()) {
			List<byte[]> res = new ArrayList<>();
			COSBase arg = arguments.get(0);
			if (arg instanceof COSArray) {
				this.addArrayElements(res, (COSArray) arg);
			} else {
				if (arg instanceof COSString) {
					res.add(((COSString) arg).getBytes());
				}
			}
			return res;
		} else {
			return Collections.emptyList();
		}
    }

	private void addArrayElements(List<byte[]> res, COSArray arg) {
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
			LOGGER.debug(
					MSG_PROBLEM_OBTAINING_RESOURCE + this.state.getFontName().getName() + ". "
							+ e.getMessage(), e);
			return null;
		}
	}

}
