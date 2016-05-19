package org.verapdf.model.tools.resources;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Evgeniy Muravitskiy
 */
public class PDInheritableResources {

	private static final Logger LOGGER = Logger.getLogger(PDInheritableResources.class);

	public static final PDResources EMPTY_RESOURCES = new PDEmptyResources();
	public static final PDInheritableResources EMPTY_EXTENDED_RESOURCES = new PDEmptyInheritableResources();

	private final PDResources currentResources;
	private final PDResources inheritedResources;

	private final HashMap<COSName, PDFont> fontCache = new HashMap<>();

	protected PDInheritableResources(PDResources inheritedResources, PDResources currentResources) {
		this.inheritedResources = inheritedResources;
		this.currentResources = currentResources;
	}

	/**
	 * @return inherited resources
	 */
	public PDResources getInheritedResources() {
		return this.inheritedResources;
	}

	/**
	 * @return current resources
	 */
	public PDResources getCurrentResources(){
		return this.currentResources;
	}

	public PDInheritableResources getExtendedResources(PDResources resources) {
		return getInstance(this.currentResources, resources);
	}

	public PDFont getFont(COSName name) throws IOException {
		PDFont ret = fontCache.get(name);
		if (ret == null) {
			PDFont font = this.currentResources.getFont(name);
			if (font == null) {
				font = this.inheritedResources.getFont(name);
				if (font != null) {
					font.setInherited(true);
				}
			}
			fontCache.put(name, font);
			ret = font;
		}
		return ret;
	}

	public PDColorSpace getColorSpace(COSName name) throws IOException {
		try {
			/*
				if name is name of device depended color space and
				default color space defined only in page resource
			 	dictionary that wee need to get it from page
			 	resource dictionary
			*/
			if (this.isDefaultColorSpaceUsed(name)) {
				return this.inheritedResources.getColorSpace(name);
			}
			PDColorSpace colorSpace = this.currentResources.getColorSpace(name);
			if (colorSpace != null) {
				return colorSpace;
			}
		} catch (IOException e) {
			LOGGER.warn("Problems during color space obtain from current resource dictionary. " +
					"Trying to find it in inherited dictionary", e);
		}
		PDColorSpace colorSpace = this.inheritedResources.getColorSpace(name);
		colorSpace.setInherited(true);
		return colorSpace;
	}

	public PDExtendedGraphicsState getExtGState(COSName name) {
		PDExtendedGraphicsState state = this.currentResources.getExtGState(name);
		if (state != null) {
			return state;
		} else {
			state = this.inheritedResources.getExtGState(name);
			if (state != null) {
				state.setInherited(true);
				return state;
			}
		}

		return null;
	}

	public PDShading getShading(COSName name) throws IOException {
		PDShading shading = this.currentResources.getShading(name);
		if (shading != null) {
			return shading;
		} else {
			shading = this.inheritedResources.getShading(name);
			if (shading != null) {
				shading.setInherited(true);
				return shading;
			}
		}

		return null;
	}

	public PDAbstractPattern getPattern(COSName name) throws IOException {
		PDAbstractPattern pattern = this.currentResources.getPattern(name);
		if (pattern != null) {
			return pattern;
		} else {
			pattern = this.inheritedResources.getPattern(name);
			if (pattern != null) {
				pattern.setInherited(true);
				return pattern;
			}
		}

		return null;
	}

	public PDXObject getXObject(COSName name) throws IOException {
		PDXObject object = this.currentResources.getXObject(name);
		if (object != null) {
			return object;
		} else {
			object = this.inheritedResources.getXObject(name);
			if (object != null) {
				object.setInherited(true);
				return object;
			}
		}

		return null;
	}

	private boolean isDefaultColorSpaceUsed(COSName name) {
		if (this.isDeviceDepended(name)) {
			COSName value = PDColorSpace.getDefaultValue(this.currentResources, name);
			if (value != null) {
				return false;
			}
			value = PDColorSpace.getDefaultValue(this.inheritedResources, name);
			if (value != null) {
				return true;
			}
		}
		return false;
	}

	private boolean isDeviceDepended(COSName name) {
		return COSName.DEVICERGB.equals(name) ||
				COSName.DEVICEGRAY.equals(name) || COSName.DEVICECMYK.equals(name);
	}

	public static PDInheritableResources getInstance(PDResources pageResources) {
		return getInstance(null, pageResources);
	}

	public static PDInheritableResources getInstance(
			PDResources inheritedResources, PDResources currentResources) {
		inheritedResources = inheritedResources != null && currentResources == null ? inheritedResources : EMPTY_RESOURCES;
		currentResources = currentResources != null ? currentResources : EMPTY_RESOURCES;
		return new PDInheritableResources(inheritedResources, currentResources);
	}

}
