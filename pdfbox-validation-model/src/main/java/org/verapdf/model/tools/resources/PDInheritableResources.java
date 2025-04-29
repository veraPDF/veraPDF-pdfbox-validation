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

import java.util.logging.Logger;
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
import java.util.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class PDInheritableResources {

	private static final Logger LOGGER = Logger.getLogger(PDInheritableResources.class.getCanonicalName());

	public static final PDResources EMPTY_RESOURCES = new PDResources();
	public static final PDInheritableResources EMPTY_EXTENDED_RESOURCES = new PDEmptyInheritableResources();

	private final PDResources currentResources;
	private final PDResources inheritedResources;

	private Set<COSName> undefinedResourceNames = new HashSet<>();
	private Set<COSName> inheritedResourceNames = new HashSet<>();

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
					inheritedResourceNames.add(name);
				}
			}
			fontCache.put(name, font);
			ret = font;
		}
		if (ret == null) {
			undefinedResourceNames.add(name);
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
					undefinedResourceNames.add(name);
				}
				return colorSpace;
			}
			PDColorSpace colorSpace = this.currentResources.getColorSpace(name);
			if (colorSpace != null) {
				return colorSpace;
			}
		} catch (IOException e) {
			LOGGER.log(java.util.logging.Level.INFO, "Problems during color space obtain from current resource dictionary. "
					+ "Trying to find it in inherited dictionary " + e.getMessage());
		}
		PDColorSpace colorSpace = this.inheritedResources.getColorSpace(name);
		colorSpace = setInheritedColorSpace(colorSpace, name);
		if (colorSpace == null) {
			undefinedResourceNames.add(name);
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
			inheritedResourceNames.add(name);
			return state;
		}
		undefinedResourceNames.add(name);
		return null;
	}

	public PDShading getShading(COSName name) throws IOException {
		PDShading shading = this.currentResources.getShading(name);
		if (shading != null) {
			return shading;
		}
		shading = this.inheritedResources.getShading(name);
		if (shading != null) {
			inheritedResourceNames.add(name);
			shading.setInherited(true);
			return shading;
		}
		undefinedResourceNames.add(name);
		return null;
	}

	public PDAbstractPattern getPattern(COSName name) throws IOException {
		PDAbstractPattern pattern = this.currentResources.getPattern(name);
		if (pattern != null) {
			return pattern;
		}
		pattern = this.inheritedResources.getPattern(name);
		if (pattern != null) {
			inheritedResourceNames.add(name);
			pattern.setInherited(true);
			return pattern;
		}
		undefinedResourceNames.add(name);
		return null;
	}

	public PDXObject getXObject(COSName name) throws IOException {
		PDXObject object = this.currentResources.getXObject(name);
		if (object != null) {
			return object;
		}
		object = this.inheritedResources.getXObject(name);
		if (object != null) {
			inheritedResourceNames.add(name);
			object.setInherited(true);
			return object;
		}
		undefinedResourceNames.add(name);
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

	private PDColorSpace setInheritedColorSpace(PDColorSpace colorSpace, COSName name) {
		if (colorSpace == PDDeviceCMYK.INSTANCE) {
			return PDDeviceCMYK.INHERITED_INSTANCE;
		} else if (colorSpace == PDDeviceRGB.INSTANCE) {
			return PDDeviceRGB.INHERITED_INSTANCE;
		} else if (colorSpace == PDDeviceGray.INSTANCE) {
			return PDDeviceGray.INHERITED_INSTANCE;
		}
		colorSpace.setInherited(true);
		inheritedResourceNames.add(name);
		return colorSpace;
	}

	public static PDInheritableResources getInstance(PDResources pageResources) {
		return getInstance(null, pageResources);
	}

	public static PDInheritableResources getInstance(PDResources inheritedResources, PDResources currentResources) {
		PDResources inheritedResourcesDictionary = inheritedResources != null ? inheritedResources : EMPTY_RESOURCES;
		PDResources currentResourcesDictionary = currentResources != null ? currentResources : EMPTY_RESOURCES;
		return new PDInheritableResources(inheritedResourcesDictionary, currentResourcesDictionary);
	}

	public Set<COSName> getUndefinedResourceNames() {
		return undefinedResourceNames;
	}

	public Set<COSName> getInheritedResourceNames() {
		return inheritedResourceNames;
	}

}
