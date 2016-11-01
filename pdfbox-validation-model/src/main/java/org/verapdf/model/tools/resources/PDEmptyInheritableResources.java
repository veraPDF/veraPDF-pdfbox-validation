package org.verapdf.model.tools.resources;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

/**
 * @author Evgeniy Muravitskiy
 */
class PDEmptyInheritableResources extends PDInheritableResources {

	PDEmptyInheritableResources() {
		super(EMPTY_RESOURCES, EMPTY_RESOURCES);
	}

	@Override
	public PDFont getFont(COSName name) {
		return null;
	}

	@Override
	public PDColorSpace getColorSpace(COSName name) {
		return null;
	}

	@Override
	public PDExtendedGraphicsState getExtGState(COSName name) {
		return null;
	}

	@Override
	public PDShading getShading(COSName name) {
		return null;
	}

	@Override
	public PDAbstractPattern getPattern(COSName name) {
		return null;
	}

	@Override
	public PDXObject getXObject(COSName name) {
		return null;
	}
}
