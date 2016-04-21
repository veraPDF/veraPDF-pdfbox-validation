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
				state.getFillColorSpace(), resources, opType, document, flavour);
    }

	protected PBOpPathPaint(List<COSBase> arguments, PDAbstractPattern fillPattern, PDAbstractPattern strokePattern,
							PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
							PDInheritableResources resources, final String type, PDDocument document, PDFAFlavour flavour) {
		super(arguments, type);
		this.pbStrokeColorSpace = pbStrokeColorSpace;
		this.pbFillColorSpace = pbFillColorSpace;
		this.fillPattern = fillPattern;
		this.strokePattern = strokePattern;
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getFillCS() {
		if (this.fillCS == null) {
			this.fillCS = getColorSpace(this.pbFillColorSpace, this.fillPattern);
		}
		return this.fillCS;
	}

	public org.verapdf.model.pdlayer.PDColorSpace getVeraModelFillCS() {
		if (this.fillCS == null) {
			this.fillCS = getColorSpace(this.pbFillColorSpace, this.fillPattern);
		}
		return this.fillCS.isEmpty() ? null : this.fillCS.get(0);
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getStrokeCS() {
		if (this.strokeCS == null) {
			this.strokeCS = this.getColorSpace(this.pbStrokeColorSpace, this.strokePattern);
		}
		return this.strokeCS;
	}

	public org.verapdf.model.pdlayer.PDColorSpace getVeraModelStrokeCS() {
		if (this.strokeCS == null) {
			this.strokeCS = this.getColorSpace(this.pbStrokeColorSpace, this.strokePattern);
		}
		return this.strokeCS.isEmpty() ? null : this.strokeCS.get(0);
	}

	private List<org.verapdf.model.pdlayer.PDColorSpace> getColorSpace(
			PDColorSpace colorSpace, PDAbstractPattern pattern) {
		org.verapdf.model.pdlayer.PDColorSpace veraColorSpace =
				ColorSpaceFactory.getColorSpace(colorSpace,
						pattern, this.resources, this.document, this.flavour);
		if (veraColorSpace != null) {
			List<org.verapdf.model.pdlayer.PDColorSpace> list =
					new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(veraColorSpace);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
