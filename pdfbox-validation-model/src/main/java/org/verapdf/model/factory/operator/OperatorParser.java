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
/**
 *
 */
package org.verapdf.model.factory.operator;

import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.operator.color.PBOpColor;
import org.verapdf.model.impl.pb.operator.color.PBOpSetColor;
import org.verapdf.model.impl.pb.operator.generalgs.*;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_BI;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_EI;
import org.verapdf.model.impl.pb.operator.inlineimage.PBOp_ID;
import org.verapdf.model.impl.pb.operator.markedcontent.*;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_WStar;
import org.verapdf.model.impl.pb.operator.opclip.PBOp_W_clip;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_BX;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_EX;
import org.verapdf.model.impl.pb.operator.opcompability.PBOp_Undefined;
import org.verapdf.model.impl.pb.operator.pathconstruction.*;
import org.verapdf.model.impl.pb.operator.pathpaint.*;
import org.verapdf.model.impl.pb.operator.shading.PBOp_sh;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_Q_grestore;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_cm;
import org.verapdf.model.impl.pb.operator.specialgs.PBOp_q_gsave;
import org.verapdf.model.impl.pb.operator.textobject.PBOpTextObject;
import org.verapdf.model.impl.pb.operator.textposition.PBOpTextPosition;
import org.verapdf.model.impl.pb.operator.textposition.PBOp_TD_Big;
import org.verapdf.model.impl.pb.operator.textposition.PBOp_Td;
import org.verapdf.model.impl.pb.operator.textposition.PBOp_Tm;
import org.verapdf.model.impl.pb.operator.textshow.*;
import org.verapdf.model.impl.pb.operator.textstate.*;
import org.verapdf.model.impl.pb.operator.type3font.PBOp_d0;
import org.verapdf.model.impl.pb.operator.type3font.PBOp_d1;
import org.verapdf.model.impl.pb.operator.xobject.PBOp_Do;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDColorSpace;
import org.verapdf.model.impl.pb.pd.font.PBoxPDFont;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.constants.Operators;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stateful parser that create veraPDF Model operator instances from individual
 * PDF Tokens. The parsing process holds some state (previously in the factory).
 * Separated this into it's own class as the parsing is pretty dense.
 *
 * @author carlwilson
 */
class OperatorParser {

	private static final Logger LOGGER = Logger.getLogger(OperatorParser.class.getCanonicalName());
	private static final String MSG_PROBLEM_OBTAINING_RESOURCE = "Problem encountered while obtaining resources for ";

	private final Deque<GraphicState> graphicStateStack = new ArrayDeque<>();
	private final GraphicState graphicState = new GraphicState();

	private final PDDocument document;
	private final PDFAFlavour flavour;

	OperatorParser(PDDocument document, PDFAFlavour flavour) {
		// limit the scope
		this.document = document;
		this.flavour = flavour;
	}

	public GraphicState getGraphicState() {
		GraphicState gs = new GraphicState();
		gs.copyProperties(this.graphicState);
		return gs;
	}

	void parseOperator(List<Operator> operators,
					   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
					   PDInheritableResources resources, List<COSBase> arguments)
			throws CloneNotSupportedException, IOException {
		String operatorName = pdfBoxOperator.getName();
		PDColorSpace cs;
		switch (operatorName) {
			// GENERAL GS
			case Operators.D_SET_DASH:
				operators.add(new PBOp_d(arguments, this.document, this.flavour));
				break;
			case Operators.GS:
				this.addExtGState(operators, resources, arguments);
				break;
			case Operators.I_SETFLAT:
				operators.add(new PBOp_i(arguments));
				break;
			case Operators.J_LINE_CAP:
				operators.add(new PBOp_J_line_cap(arguments));
				break;
			case Operators.J_LINE_JOIN:
				operators.add(new PBOp_j_line_join(arguments));
				break;
			case Operators.M_MITER_LIMIT:
				operators.add(new PBOp_M_miter_limit(arguments));
				break;
			case Operators.RI:
				operators.add(new PBOp_ri(arguments));
				break;
			case Operators.W_LINE_WIDTH:
				operators.add(new PBOp_w_line_width(arguments));
				break;

			// MARKED CONTENT
			case Operators.BMC:
				operators.add(new PBOp_BMC(arguments, document, flavour));
				break;
			case Operators.BDC:
				operators.add(new PBOp_BDC(arguments, document, flavour));
				break;
			case Operators.EMC:
				operators.add(new PBOp_EMC(arguments, document, flavour));
				break;
			case Operators.MP:
				operators.add(new PBOp_MP(arguments, document, flavour));
				break;
			case Operators.DP:
				operators.add(new PBOp_DP(arguments, document, flavour));
				break;

			// CLIP
			case Operators.W_CLIP:
				operators.add(new PBOp_W_clip(arguments));
				break;
			case Operators.W_STAR_EOCLIP:
				operators.add(new PBOp_WStar(arguments));
				break;

			// COLOR
			case Operators.G_STROKE: {
				cs = resources == null ? PDDeviceGray.INSTANCE :
						resources.getColorSpace(COSName.DEVICEGRAY);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(this.getStrokeColorOperator(arguments));
				break;
			}
			case Operators.G_FILL: {
				cs = resources == null ? PDDeviceGray.INSTANCE :
						resources.getColorSpace(COSName.DEVICEGRAY);
				this.graphicState.setFillColorSpace(cs);
				operators.add(this.getFillColorOperator(arguments));
				break;
			}
			case Operators.RG_STROKE: {
				cs = resources == null ? PDDeviceRGB.INSTANCE :
						resources.getColorSpace(COSName.DEVICERGB);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(this.getStrokeColorOperator(arguments));
				break;
			}
			case Operators.RG_FILL: {
				cs = resources == null ? PDDeviceRGB.INSTANCE :
						resources.getColorSpace(COSName.DEVICERGB);
				this.graphicState.setFillColorSpace(cs);
				operators.add(this.getFillColorOperator(arguments));
				break;
			}
			case Operators.K_STROKE: {
				cs = resources == null ? PDDeviceCMYK.INSTANCE :
						resources.getColorSpace(COSName.DEVICECMYK);
				this.graphicState.setStrokeColorSpace(cs);
				operators.add(this.getStrokeColorOperator(arguments));
				break;
			}
			case Operators.K_FILL: {
				cs = resources == null ? PDDeviceCMYK.INSTANCE :
						resources.getColorSpace(COSName.DEVICECMYK);
				this.graphicState.setFillColorSpace(cs);
				operators.add(this.getFillColorOperator(arguments));
				break;
			}
			case Operators.CS_STROKE:
				this.graphicState.setStrokeColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(this.getStrokeColorOperator(arguments));
				break;
			case Operators.CS_FILL:
				this.graphicState.setFillColorSpace(getColorSpaceFromResources(
						resources, getLastCOSName(arguments)));
				operators.add(this.getFillColorOperator(arguments));
				break;
			case Operators.SCN_STROKE:
				this.setStrokePatternColorSpace(operators, graphicState.getStrokeColorSpace(),
						resources, arguments);
				break;
			case Operators.SCN_FILL:
				this.setFillPatternColorSpace(operators, graphicState.getFillColorSpace(),
						resources, arguments);
				break;
			case Operators.SC_STROKE:
				operators.add(new PBOpSetColor(arguments));
				break;
			case Operators.SC_FILL:
				operators.add(new PBOpSetColor(arguments));
				break;

			// TEXT OBJECT
			case Operators.ET:
			case Operators.BT:
				operators.add(new PBOpTextObject(arguments));
				break;

			// TEXT POSITION
			case Operators.TD_MOVE:
				operators.add(new PBOp_Td(arguments));
				break;
			case Operators.TD_MOVE_SET_LEADING:
				operators.add(new PBOp_TD_Big(arguments));
				break;
			case Operators.TM:
				operators.add(new PBOp_Tm(arguments));
				break;
			case Operators.T_STAR:
				operators.add(new PBOpTextPosition(arguments));
				break;

			// TEXT SHOW
			case Operators.TJ_SHOW:
				PBOp_Tj tj = new PBOp_Tj(arguments, this.graphicState.clone(), resources, this.document, this.flavour);
				addFontAndColorSpace(tj);
				operators.add(tj);
				break;
			case Operators.TJ_SHOW_POS:
				PBOp_TJ_Big tj_big = new PBOp_TJ_Big(arguments, this.graphicState.clone(), resources, this.document, this.flavour);
				addFontAndColorSpace(tj_big);
				operators.add(tj_big);
				break;
			case Operators.QUOTE:
				PBOp_Quote quote = new PBOp_Quote(arguments, this.graphicState.clone(), resources, this.document, this.flavour);
				addFontAndColorSpace(quote);
				operators.add(quote);
				break;
			case Operators.DOUBLE_QUOTE:
				PBOp_DoubleQuote doubleQuote = new PBOp_DoubleQuote(arguments, this.graphicState.clone(), resources, this.document, this.flavour);
				addFontAndColorSpace(doubleQuote);
				operators.add(doubleQuote);
				break;

			// TEXT STATE
			case Operators.TZ:
				operators.add(new PBOp_Tz(arguments));
				break;
			case Operators.TR:
				this.graphicState.setRenderingMode(getRenderingMode(arguments));
				operators.add(new PBOp_Tr(arguments));
				break;
			case Operators.TF:
				this.graphicState.setFontName(getFirstCOSName(arguments));
				operators.add(new PBOp_Tf(arguments));
				break;
			case Operators.TC:
				operators.add(new PBOp_Tc(arguments));
				break;
			case Operators.TW:
				operators.add(new PBOp_Tw(arguments));
				break;
			case Operators.TL:
				operators.add(new PBOp_Tl(arguments));
				break;
			case Operators.TS:
				operators.add(new PBOp_Ts(arguments));
				break;

			// TYPE 3 FONT
			case Operators.D0:
				operators.add(new PBOp_d0(arguments));
				break;
			case Operators.D1:
				operators.add(new PBOp_d1(arguments));
				break;

			// INLINE IMAGE
			case Operators.BI:
				addInlineImage(operators, pdfBoxOperator, resources, arguments, document, flavour);
				break;

			// COMPABILITY
			case Operators.BX:
				operators.add(new PBOp_BX(arguments));
				break;
			case Operators.EX:
				operators.add(new PBOp_EX(arguments));
				break;

			// PATH CONSTRUCTION
			case Operators.C_CURVE_TO:
				operators.add(new PBOp_c(arguments));
				break;
			case Operators.H_CLOSEPATH:
				operators.add(new PBOp_h(arguments));
				break;
			case Operators.L_LINE_TO:
				operators.add(new PBOp_l(arguments));
				break;
			case Operators.M_MOVE_TO:
				operators.add(new PBOp_m_moveto(arguments));
				break;
			case Operators.RE:
				operators.add(new PBOp_re(arguments));
				break;
			case Operators.V:
				operators.add(new PBOp_v(arguments));
				break;
			case Operators.Y:
				operators.add(new PBOp_y(arguments));
				break;

			// PATH PAINT
			case Operators.B_CLOSEPATH_FILL_STROKE:
				PBOp_b_closepath_fill_stroke b_closepath_fill_stroke = new PBOp_b_closepath_fill_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(b_closepath_fill_stroke);
				operators.add(b_closepath_fill_stroke);
				break;
			case Operators.B_FILL_STROKE:
				PBOp_B_fill_stroke b_fill_stroke = new PBOp_B_fill_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(b_fill_stroke);
				operators.add(b_fill_stroke);
				break;
			case Operators.B_STAR_CLOSEPATH_EOFILL_STROKE:
				PBOp_bstar_closepath_eofill_stroke bstar_closepath_eofill_stroke = new PBOp_bstar_closepath_eofill_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(bstar_closepath_eofill_stroke);
				operators.add(bstar_closepath_eofill_stroke);
				break;
			case Operators.B_STAR_EOFILL_STROKE:
				PBOp_BStar_eofill_stroke bStar_eofill_stroke = new PBOp_BStar_eofill_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(bStar_eofill_stroke);
				operators.add(bStar_eofill_stroke);
				break;
			case Operators.F_FILL:
				PBOp_f_fill f_fill = new PBOp_f_fill(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(f_fill);
				operators.add(f_fill);
				break;
			case Operators.F_FILL_OBSOLETE:
				PBOp_F_fill_obsolete f_fill_obsolete = new PBOp_F_fill_obsolete(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(f_fill_obsolete);
				operators.add(f_fill_obsolete);
				break;
			case Operators.F_STAR_FILL:
				PBOp_FStar fStar = new PBOp_FStar(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(fStar);
				operators.add(fStar);
				break;
			case Operators.N:
				PBOp_n op_n = new PBOp_n(arguments, document, flavour);
				addColorSpace(op_n);
				operators.add(op_n);
				break;
			case Operators.S_CLOSE_STROKE:
				PBOp_s_close_stroke s_close_stroke = new PBOp_s_close_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(s_close_stroke);
				operators.add(s_close_stroke);
				break;
			case Operators.S_STROKE:
				PBOp_S_stroke s_stroke = new PBOp_S_stroke(arguments,
						this.graphicState, resources, document, flavour);
				addColorSpace(s_stroke);
				operators.add(s_stroke);
				break;

			// SHADING
			case Operators.SH:
				operators.add(new PBOp_sh(arguments, getShadingFromResources(resources,
						getLastCOSName(arguments)), document, flavour));
				break;

			// SPECIAL GS
			case Operators.CM_CONCAT:
				operators.add(new PBOp_cm(arguments));
				break;
			case Operators.Q_GRESTORE:
				if (!graphicStateStack.isEmpty()) {
					this.graphicState.copyProperties(this.graphicStateStack.pop());
				}
				operators.add(new PBOp_Q_grestore(arguments));
				break;
			case Operators.Q_GSAVE:
				this.graphicStateStack.push(this.graphicState.clone());
				operators.add(new PBOp_q_gsave(arguments, this.graphicStateStack.size()));
				break;

			// XOBJECT
			case Operators.DO:
				PBOp_Do op = new PBOp_Do(arguments, getXObjectFromResources(resources,
						getLastCOSName(arguments)), resources, document, flavour);
				List<org.verapdf.model.pdlayer.PDXObject> pdxObjects = op.getXObject();
				if (!pdxObjects.isEmpty()) {
					PBoxPDXObject xobj = (PBoxPDXObject) pdxObjects.get(0);
					this.graphicState.setVeraXObject(xobj);
				}
				operators.add(op);
				break;
			default:
				operators.add(new PBOp_Undefined(operatorName, arguments));
				break;
		}
	}

	private PBOpColor getStrokeColorOperator(List<COSBase> arguments) {
		org.verapdf.model.pdlayer.PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(
				graphicState.getStrokeColorSpace(), document, flavour);
		return new PBOpColor(arguments, colorSpace);
	}

	private PBOpColor getFillColorOperator(List<COSBase> arguments) {
		org.verapdf.model.pdlayer.PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(
				graphicState.getFillColorSpace(), document, flavour);
		return new PBOpColor(arguments, colorSpace);
	}

	private void setFillPatternColorSpace(List<Operator> operators, PDColorSpace colorSpace,
										  PDInheritableResources resources, List<COSBase> arguments) {
		if (colorSpace != null &&
				ColorSpaceFactory.PATTERN.equals(colorSpace.getName())) {
			graphicState.setFillPattern(getPatternFromResources(resources,
					getLastCOSName(arguments)));
		}
		org.verapdf.model.pdlayer.PDColorSpace modelColorSpace = ColorSpaceFactory.getColorSpace(
				graphicState.getFillColorSpace(), document, flavour);
		operators.add(new PBOpColor(arguments, modelColorSpace));
	}

	private void setStrokePatternColorSpace(List<Operator> operators, PDColorSpace colorSpace,
											PDInheritableResources resources, List<COSBase> arguments) {
		if (colorSpace != null &&
				ColorSpaceFactory.PATTERN.equals(colorSpace.getName())) {
			graphicState.setStrokePattern(getPatternFromResources(resources,
					getLastCOSName(arguments)));
		}
		org.verapdf.model.pdlayer.PDColorSpace modelColorSpace = ColorSpaceFactory.getColorSpace(
				graphicState.getStrokeColorSpace(), document, flavour);
		operators.add(new PBOpColor(arguments, modelColorSpace));
	}

	private void addExtGState(List<Operator> operators,
							  PDInheritableResources resources, List<COSBase> arguments) {
		PDExtendedGraphicsState extGState = getExtGStateFromResources(resources,
				getLastCOSName(arguments));
		graphicState.copyPropertiesFromExtGState(extGState);
		operators.add(new PBOp_gs(arguments, extGState, this.document, this.flavour));
	}

	private static void addInlineImage(List<Operator> operators,
									   org.apache.pdfbox.contentstream.operator.Operator pdfBoxOperator,
									   PDInheritableResources resources,
									   List<COSBase> arguments,
									   PDDocument document,
									   PDFAFlavour flavour) {
		if (pdfBoxOperator.getImageParameters() != null &&
				pdfBoxOperator.getImageData() != null) {
			arguments.add(pdfBoxOperator.getImageParameters());
			operators.add(new PBOp_BI(new ArrayList<COSBase>()));
			operators.add(new PBOp_ID(arguments, document, flavour));
			operators.add(new PBOp_EI(arguments,
					pdfBoxOperator.getImageData(), resources, document, flavour));
		}
	}

	private static COSName getFirstCOSName(List<COSBase> arguments) {
		COSBase lastElement = arguments.isEmpty() ? null : arguments.get(0);
		if (lastElement instanceof COSName) {
			return (COSName) lastElement;
		}
		return null;
	}

	private static COSName getLastCOSName(List<COSBase> arguments) {
		COSBase lastElement = arguments.isEmpty() ? null : arguments.get(arguments
				.size() - 1);
		if (lastElement instanceof COSName) {
			return (COSName) lastElement;
		}
		return null;
	}

	private static PDXObject getXObjectFromResources(PDInheritableResources resources,
													 COSName xobject) {
		if (resources == null) {
			return null;
		}
		try {
			return resources.getXObject(xobject);
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO,
					MSG_PROBLEM_OBTAINING_RESOURCE + xobject + ". "
							+ e.getMessage());
			return null;
		}
	}

	private static PDColorSpace getColorSpaceFromResources(
			PDInheritableResources resources, COSName colorSpace) {
		if (resources == null) {
			return null;
		}
		try {
			return resources.getColorSpace(colorSpace);
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO,
					MSG_PROBLEM_OBTAINING_RESOURCE + colorSpace + ". "
							+ e.getMessage());
			return null;
		}
	}

	private static PDShading getShadingFromResources(
			PDInheritableResources resources, COSName shading) {
		if (resources == null) {
			return null;
		}
		try {
			return resources.getShading(shading);
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO,
					MSG_PROBLEM_OBTAINING_RESOURCE + shading + ". "
							+ e.getMessage());
			return null;
		}
	}

	private static PDExtendedGraphicsState getExtGStateFromResources(
			PDInheritableResources resources, COSName extGState) {
		return resources == null ? null : resources.getExtGState(extGState);
	}

	private static RenderingMode getRenderingMode(List<COSBase> arguments) {
		if (!arguments.isEmpty()) {
			COSBase renderingMode = arguments.get(0);
			if (renderingMode instanceof COSInteger) {
				try {
					return RenderingMode.fromInt(((COSInteger) renderingMode).intValue());
				} catch (ArrayIndexOutOfBoundsException e) {
					LOGGER.log(java.util.logging.Level.INFO, "Rendering mode value is incorrect : " + renderingMode + e.getMessage());
				}
			}
		}
		return RenderingMode.FILL;
	}

	private static PDAbstractPattern getPatternFromResources(
			PDInheritableResources resources, COSName pattern) {
		if (resources == null) {
			return null;
		}
		try {
			return resources.getPattern(pattern);
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO,
					MSG_PROBLEM_OBTAINING_RESOURCE + pattern + ". "
							+ e.getMessage());
			return null;
		}
	}

	private void addFontAndColorSpace(PBOpTextShow op) {
		PBoxPDFont font = (PBoxPDFont) op.getVeraModelFont();
		this.graphicState.setVeraFont(font);
		byte[] charCodes = op.getCharCodes();
		this.graphicState.setCharCodes(charCodes);
		PBoxPDColorSpace fillCS = (PBoxPDColorSpace) op.getVeraModelFillColorSpace();
		this.graphicState.setVeraFillColorSpace(fillCS);
		PBoxPDColorSpace strokeCS = (PBoxPDColorSpace) op.getVeraModelStrokeColorSpace();
		this.graphicState.setVeraStrokeColorSpace(strokeCS);
	}

	private void addColorSpace(PBOpPathPaint op) {
		PBoxPDColorSpace fillCS = (PBoxPDColorSpace) op.getVeraModelFillCS();
		this.graphicState.setVeraFillColorSpace(fillCS);
		PBoxPDColorSpace strokeCS = (PBoxPDColorSpace) op.getVeraModelStrokeCS();
		this.graphicState.setVeraStrokeColorSpace(strokeCS);
	}
}
