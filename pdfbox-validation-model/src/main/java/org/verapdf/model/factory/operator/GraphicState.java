package org.verapdf.model.factory.operator;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.bouncycastle.util.Arrays;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDColorSpace;
import org.verapdf.model.impl.pb.pd.font.PBoxPDFont;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;

import java.io.IOException;

/**
 * Implementation of graphic state for content stream.
 * Contains follows settings: fill and stroke color spaces,
 * pattern, rendering mode and font
 *
 * @author Timur Kamalov
 */
public class GraphicState implements Cloneable {

    private static final Logger LOGGER = Logger.getLogger(GraphicState.class);

    private PDColorSpace fillColorSpace = PDDeviceGray.INSTANCE;
    private PDColorSpace strokeColorSpace = PDDeviceGray.INSTANCE;
    private PDAbstractPattern fillPattern = null;
	private PDAbstractPattern strokePattern = null;
    private RenderingMode renderingMode = RenderingMode.FILL;
    private COSName fontName;
	private boolean overprintingFlagStroke = false;
	private boolean overprintingFlagNonStroke = false;
	private int opm = 0;

	// fields for transparency checks
	private COSBase sMask = null;
	private float ca = 1;
	private float ca_ns = 1;
	private COSBase bm = null;

	// fields for transparency checks. This is veraPDF implementation of XObject
	private PBoxPDXObject veraXObject = null;
	private PBoxPDColorSpace veraFillColorSpace = null;
	private PBoxPDColorSpace veraStrokeColorSpace = null;
	private PBoxPDFont veraFont = null;
	private byte[] charCodes = null;

	/**
	 * @return fill color space of current state
	 */
    public PDColorSpace getFillColorSpace() {
        return fillColorSpace;
    }

	/**
	 * @param fillColorSpace set fill color space to current state
	 */
    public void setFillColorSpace(PDColorSpace fillColorSpace) {
        this.fillColorSpace = fillColorSpace;
    }

	/**
	 * @return stroke color space of current state
	 */
    public PDColorSpace getStrokeColorSpace() {
        return strokeColorSpace;
    }

	/**
	 * @param strokeColorSpace set stroke color space to current state
	 */
    public void setStrokeColorSpace(PDColorSpace strokeColorSpace) {
        this.strokeColorSpace = strokeColorSpace;
    }

	/**
	 * @return fill pattern of current state
	 */
	public PDAbstractPattern getFillPattern() {
		return fillPattern;
	}

	/**
	 * @param fillPattern set fill pattern to current state
	 */
	public void setFillPattern(PDAbstractPattern fillPattern) {
		this.fillPattern = fillPattern;
	}

	/**
	 * @return stroke pattern of current state
	 */
	public PDAbstractPattern getStrokePattern() {
		return strokePattern;
	}

	/**
	 * @param strokePattern set stroke pattern to current state
	 */
	public void setStrokePattern(PDAbstractPattern strokePattern) {
		this.strokePattern = strokePattern;
	}

	/**
	 * @return fill color space object of veraPDF model implementation of current state
	 */
	public PBoxPDColorSpace getVeraFillColorSpace() {
		return veraFillColorSpace;
	}

	/**
	 * @param veraFillColorSpace set fill color space object of veraPDF model implementation to current state
	 */
	public void setVeraFillColorSpace(PBoxPDColorSpace veraFillColorSpace) {
		this.veraFillColorSpace = veraFillColorSpace;
	}

	/**
	 * @return stroke color space object of veraPDF model implementation of current state
	 */
	public PBoxPDColorSpace getVeraStrokeColorSpace() {
		return veraStrokeColorSpace;
	}

	/**
	 * @param veraStrokeColorSpace set stroke color space object of veraPDF model implementation to current state
	 */
	public void setVeraStrokeColorSpace(PBoxPDColorSpace veraStrokeColorSpace) {
		this.veraStrokeColorSpace = veraStrokeColorSpace;
	}

	/**
	 * @return font object of veraPDF model implementation of current state
	 */
	public PBoxPDFont getVeraFont() {
		return veraFont;
	}

	/**
	 * @param veraFont setfont object of veraPDF model implementation to current state
	 */
	public void setVeraFont(PBoxPDFont veraFont) {
		this.veraFont = veraFont;
	}

	/**
	 * @return rendering mode of current state
	 */
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

	/**
	 * @param renderingMode set renderint mode to color space
	 */
    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    /**
     * @return name of the current font
     */
    public COSName getFontName() {
        return fontName;
    }

    /**
     * @param fontName set name of the current font
     */
    public void setFontName(COSName fontName) {
        this.fontName = fontName;
    }

	/**
	 * @return SMask base object of current state
	 */
	public COSBase getSMask() {
		return sMask;
	}

	/**
	 * @param sMask set SMask base object to current state
	 */
	public void setSMask(COSBase sMask) {
		this.sMask = sMask;
	}

	/**
	 * @return CA value of current state
	 */
	public float getCa() {
		return ca;
	}

	/**
	 * @param ca set CA value to current state
	 */
	public void setCa(float ca) {
		this.ca = ca;
	}

	/**
	 * @return ca value of current state
	 */
	public float getCa_ns() {
		return ca_ns;
	}

	/**
	 * @param ca_ns set ca value to current state
	 */
	public void setCa_ns(float ca_ns) {
		this.ca_ns = ca_ns;
	}

	/**
	 * @return BM object of current state
	 */
	public COSBase getBm() {
		return bm;
	}

	/**
	 * @param bm set BM object to current state
	 */
	public void setBm(COSBase bm) {
		this.bm = bm;
	}

	/**
	 * @return XObject object of veraPDF model implementation of current state
	 */
	public PBoxPDXObject getVeraXObject() {
		return veraXObject;
	}

	/**
	 * @param veraXObject set XObject object of veraPDF model to current state
	 */
	public void setVeraXObject(PBoxPDXObject veraXObject) {
		this.veraXObject = veraXObject;
	}

	/**
	 * @return byte array of char codes from text operator
     */
	public byte[] getCharCodes() {
		return charCodes;
	}

	/**
	 * @param charCodes set byte array of char codes from text operator
     */
	public void setCharCodes(byte[] charCodes) {
		this.charCodes = Arrays.clone(charCodes);
	}

	/**
	 * @return set stroke overprinting flag
	 */
	public boolean isOverprintingFlagStroke() {
		return overprintingFlagStroke;
	}

	/**
	 * @param overprintingFlagStroke current stroke overprinting flag
	 */
	public void setOverprintingFlagStroke(boolean overprintingFlagStroke) {
		this.overprintingFlagStroke = overprintingFlagStroke;
	}

	/**
	 * @return set non stroke overprinting flag
	 */
	public boolean isOverprintingFlagNonStroke() {
		return overprintingFlagNonStroke;
	}

	/**
	 * @param overprintingFlagNonStroke current non stroke overprinting flag
	 */
	public void setOverprintingFlagNonStroke(boolean overprintingFlagNonStroke) {
		this.overprintingFlagNonStroke = overprintingFlagNonStroke;
	}

	/**
	 * @return opm value
	 */
	public int getOpm() {
		return opm;
	}

	/**
	 * @param opm set opm value
	 */
	public void setOpm(int opm) {
		this.opm = opm;
	}

	/**
     * This method will copy properties from passed graphic state to current
     * object
     * 
     * @param graphicState
     *            graphic state to copy properties from
     */
    public void copyProperties(GraphicState graphicState) {
        this.fillColorSpace = graphicState.getFillColorSpace();
        this.strokeColorSpace = graphicState.getStrokeColorSpace();
        this.fillPattern = graphicState.getFillPattern();
		this.strokePattern = graphicState.getStrokePattern();
        this.renderingMode = graphicState.getRenderingMode();
        this.fontName = graphicState.getFontName();
		this.sMask = graphicState.getSMask();
		this.ca_ns = graphicState.getCa_ns();
		this.ca = graphicState.getCa();
		this.bm = graphicState.getBm();
		this.veraXObject = graphicState.getVeraXObject();
		this.veraFillColorSpace = graphicState.getVeraFillColorSpace();
		this.veraStrokeColorSpace = graphicState.getVeraStrokeColorSpace();
		this.veraFont = graphicState.getVeraFont();
		this.charCodes = graphicState.getCharCodes();
		this.overprintingFlagStroke = graphicState.isOverprintingFlagStroke();
		this.overprintingFlagNonStroke = graphicState.isOverprintingFlagNonStroke();
		this.opm = graphicState.getOpm();
    }

	/**
	 * Set font to current state from extended graphic state
	 *
	 * @param extGState extended graphic state
	 */
    public void copyPropertiesFromExtGState(PDExtendedGraphicsState extGState) {
        if (extGState != null) {
            try {
                if (extGState.getFontSetting() != null) {
                    this.fontName = COSName.getPDFName(extGState.getFontSetting().getFont().getName());
                }
				COSDictionary cosObject = extGState.getCOSObject();
				COSBase smask = cosObject.getDictionaryObject(COSName.SMASK);
				if (smask != null) {
					this.sMask = smask;
				}
				COSBase bm = cosObject.getDictionaryObject(COSName.BM);
				if (bm != null) {
					this.bm = bm;
				}
				COSBase ca_ns_base = cosObject.getDictionaryObject(COSName.CA_NS);
				if (ca_ns_base instanceof COSNumber) {
					this.ca_ns = ((COSNumber) ca_ns_base).floatValue();
				}
				COSBase ca_base = cosObject.getDictionaryObject(COSName.CA);
				if (ca_base instanceof COSNumber) {
					this.ca = ((COSNumber) ca_base).floatValue();
				}

				Float overprintMode = extGState.getOverprintMode();
				if (overprintMode != null) {
					this.opm = overprintMode.intValue();
				}
				this.overprintingFlagStroke = extGState.getStrokingOverprintControl();
				this.overprintingFlagNonStroke = extGState.getNonStrokingOverprintControl();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

	/**
	 * Create copy of current graphic state.
	 *
	 * @return copy of current graphic state
	 * @throws CloneNotSupportedException
	 */
    @Override
    public GraphicState clone() throws CloneNotSupportedException {
        GraphicState graphicState = (GraphicState) super.clone();
        graphicState.fillColorSpace = this.fillColorSpace;
        graphicState.strokeColorSpace = this.strokeColorSpace;
        graphicState.fillPattern = this.fillPattern;
		graphicState.strokePattern = this.strokePattern;
        graphicState.renderingMode = this.renderingMode;
        graphicState.fontName = this.fontName;
		graphicState.sMask = this.sMask;
		graphicState.ca_ns = this.ca_ns;
		graphicState.ca = this.ca;
		graphicState.bm = this.bm;
		graphicState.veraXObject = this.veraXObject;
		graphicState.veraFillColorSpace = this.veraFillColorSpace;
		graphicState.veraStrokeColorSpace = this.veraStrokeColorSpace;
		graphicState.veraFont = this.veraFont;
		graphicState.charCodes = this.charCodes;
		graphicState.opm = this.opm;
		graphicState.overprintingFlagStroke = this.overprintingFlagStroke;
		graphicState.overprintingFlagNonStroke = this.overprintingFlagNonStroke;
        return graphicState;
    }

}
