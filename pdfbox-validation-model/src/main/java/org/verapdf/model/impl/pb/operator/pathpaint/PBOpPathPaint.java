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
package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathPaint;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all path paint operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpPathPaint extends PBOperator implements OpPathPaint {

	/** Name of link to the stroke color space */
    public static final String STROKE_CS = "strokeCS";
	/** Name of link to the fill color space */
    public static final String FILL_CS = "fillCS";

	private final PDColorSpace pbStrokeColorSpace;
	private final PDColorSpace pbFillColorSpace;
	private final PDAbstractPattern fillPattern;
	private final PDAbstractPattern strokePattern;

	private final int op;
	private final boolean overprintingFlagStroke;
	private final boolean overprintingFlagNonStroke;
	private final PDInheritableResources resources;

	private final PDDocument document;
	private final PDFAFlavour flavour;

	private List<org.verapdf.model.pdlayer.PDColorSpace> fillCS = null;
	private List<org.verapdf.model.pdlayer.PDColorSpace> strokeCS = null;

	/**
	 * Default constructor
	 *
	 * @param arguments arguments for current operator, must be empty.
	 * @param state graphic state for current operator
	 * @param resources resources for tilling pattern if it`s used
	 */
    protected PBOpPathPaint(List<COSBase> arguments, final GraphicState state,
			final PDInheritableResources resources, final String opType, PDDocument document, PDFAFlavour flavour) {
		this(arguments, state.getFillPattern(), state.getStrokePattern(), state.getStrokeColorSpace(),
				state.getFillColorSpace(), resources, opType, state.getOpm(), state.isOverprintingFlagStroke(), state.isOverprintingFlagNonStroke(), document, flavour);
    }

	protected PBOpPathPaint(List<COSBase> arguments, PDAbstractPattern fillPattern, PDAbstractPattern strokePattern,
							PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
							PDInheritableResources resources, final String type,
							int op, boolean overprintingFlagStroke, boolean overprintingFlagNonStroke,
							PDDocument document, PDFAFlavour flavour) {
		super(arguments, type);
		this.pbStrokeColorSpace = pbStrokeColorSpace;
		this.pbFillColorSpace = pbFillColorSpace;
		this.fillPattern = fillPattern;
		this.strokePattern = strokePattern;
		this.resources = resources;
		this.op = op;
		this.overprintingFlagStroke = overprintingFlagStroke;
		this.overprintingFlagNonStroke = overprintingFlagNonStroke;
		this.document = document;
		this.flavour = flavour;
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getFillCS() {
		if (this.fillCS == null) {
			this.fillCS = getColorSpace(this.pbFillColorSpace, this.fillPattern, this.overprintingFlagNonStroke);
		}
		return this.fillCS;
	}

	/**
	 * @return fill color space object from veraPDF model of current operator
	 */
	public org.verapdf.model.pdlayer.PDColorSpace getVeraModelFillCS() {
		if (this.fillCS == null) {
			this.fillCS = getColorSpace(this.pbFillColorSpace, this.fillPattern, this.overprintingFlagNonStroke);
		}
		return this.fillCS.isEmpty() ? null : this.fillCS.get(0);
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getStrokeCS() {
		if (this.strokeCS == null) {
			this.strokeCS = this.getColorSpace(this.pbStrokeColorSpace, this.strokePattern, this.overprintingFlagStroke);
		}
		return this.strokeCS;
	}
	/**
	 * @return stroke color space object from veraPDF model of current operator
	 */
	public org.verapdf.model.pdlayer.PDColorSpace getVeraModelStrokeCS() {
		if (this.strokeCS == null) {
			this.strokeCS = this.getColorSpace(this.pbStrokeColorSpace, this.strokePattern, this.overprintingFlagStroke);
		}
		return this.strokeCS.isEmpty() ? null : this.strokeCS.get(0);
	}

	private List<org.verapdf.model.pdlayer.PDColorSpace> getColorSpace(
			PDColorSpace colorSpace, PDAbstractPattern pattern, boolean op) {
		org.verapdf.model.pdlayer.PDColorSpace veraColorSpace =
				ColorSpaceFactory.getColorSpace(colorSpace,
						pattern, this.resources, this.op, op, this.document, this.flavour);
		if (veraColorSpace != null) {
			List<org.verapdf.model.pdlayer.PDColorSpace> list =
					new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(veraColorSpace);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
