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
package org.verapdf.model.tools.resources;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
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

	public static final PDResources EMPTY_RESOURCES = new PDResources();
	public static final PDInheritableResources EMPTY_EXTENDED_RESOURCES = new PDEmptyInheritableResources();

	private final PDResources currentResources;
	private final PDResources inheritedResources;

	private boolean containsUndefinedResource = false;

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
	public PDResources getCurrentResources() {
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
		if (ret == null) {
			containsUndefinedResource = true;
		}
		return ret;
	}

	public PDColorSpace getColorSpace(COSName name) throws IOException {
		try {
			/*
			 * if name is name of device depended color space and default color
			 * space defined only in page resource dictionary that wee need to
			 * get it from page resource dictionary
			 */
			if (this.isDefaultColorSpaceUsed(name)) {
				PDColorSpace colorSpace = this.inheritedResources.getColorSpace(name);
				if (colorSpace == null) {
					containsUndefinedResource = true;
				}
				return colorSpace;
			}
			PDColorSpace colorSpace = this.currentResources.getColorSpace(name);
			if (colorSpace != null) {
				return colorSpace;
			}
		} catch (IOException e) {
			LOGGER.debug("Problems during color space obtain from current resource dictionary. "
					+ "Trying to find it in inherited dictionary", e);
		}
		PDColorSpace colorSpace = this.inheritedResources.getColorSpace(name);
		colorSpace = setInheritedColorSpace(colorSpace);
		if (colorSpace == null) {
			containsUndefinedResource = true;
		}
		return colorSpace;
	}

	public PDExtendedGraphicsState getExtGState(COSName name) {
		PDExtendedGraphicsState state = this.currentResources.getExtGState(name);
		if (state != null) {
			return state;
		}
		state = this.inheritedResources.getExtGState(name);
		if (state != null) {
			state.setInherited(true);
			return state;
		}
		containsUndefinedResource = true;
		return null;
	}

	public PDShading getShading(COSName name) throws IOException {
		PDShading shading = this.currentResources.getShading(name);
		if (shading != null) {
			return shading;
		}
		shading = this.inheritedResources.getShading(name);
		if (shading != null) {
			shading.setInherited(true);
			return shading;
		}
		containsUndefinedResource = true;
		return null;
	}

	public PDAbstractPattern getPattern(COSName name) throws IOException {
		PDAbstractPattern pattern = this.currentResources.getPattern(name);
		if (pattern != null) {
			return pattern;
		}
		pattern = this.inheritedResources.getPattern(name);
		if (pattern != null) {
			pattern.setInherited(true);
			return pattern;
		}
		containsUndefinedResource = true;
		return null;
	}

	public PDXObject getXObject(COSName name) throws IOException {
		PDXObject object = this.currentResources.getXObject(name);
		if (object != null) {
			return object;
		}
		object = this.inheritedResources.getXObject(name);
		if (object != null) {
			object.setInherited(true);
			return object;
		}
		containsUndefinedResource = true;
		return null;
	}

	private boolean isDefaultColorSpaceUsed(COSName name) {
		if (PDInheritableResources.isDeviceDepended(name)) {
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

	private static boolean isDeviceDepended(COSName name) {
		return COSName.DEVICERGB.equals(name) || COSName.DEVICEGRAY.equals(name) || COSName.DEVICECMYK.equals(name);
	}

	private static PDColorSpace setInheritedColorSpace(PDColorSpace colorSpace) {
		if (colorSpace == PDDeviceCMYK.INSTANCE) {
			return PDDeviceCMYK.INHERITED_INSTANCE;
		} else if (colorSpace == PDDeviceRGB.INSTANCE) {
			return PDDeviceRGB.INHERITED_INSTANCE;
		} else if (colorSpace == PDDeviceGray.INSTANCE) {
			return PDDeviceGray.INHERITED_INSTANCE;
		}
		colorSpace.setInherited(true);
		return colorSpace;
	}

	public static PDInheritableResources getInstance(PDResources pageResources) {
		return getInstance(null, pageResources);
	}

	public static PDInheritableResources getInstance(PDResources inheritedResources, PDResources currentResources) {
		inheritedResources = inheritedResources != null && currentResources == null ? inheritedResources
				: EMPTY_RESOURCES;
		currentResources = currentResources != null ? currentResources : EMPTY_RESOURCES;
		return new PDInheritableResources(inheritedResources, currentResources);
	}

	public boolean getContainsUndefinedResource() {
		return containsUndefinedResource;
	}

}
