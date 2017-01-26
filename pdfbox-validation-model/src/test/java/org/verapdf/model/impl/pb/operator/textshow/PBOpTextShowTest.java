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
package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDDeviceGray;
import org.verapdf.model.impl.pb.pd.font.PBoxPDTrueTypeFont;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpTextShowTest extends PBOperatorTest {

	@Test
	public void testFontLink() {
		testObject(PBOpTextShow.FONT, 1, PBoxPDTrueTypeFont.TRUETYPE_FONT_TYPE);
	}

	@Test
	public void testUsedGlyphsLink() {
		testObject(PBOpTextShow.USED_GLYPHS, getUsedGlyphsAmount(), PBGlyph.GLYPH_TYPE);
	}

	@Test
	public void testFillColorSpaceLink() {
		testObject(PBOpTextShow.FILL_COLOR_SPACE,
				((PBOpTextShow) actual).state.getRenderingMode().isFill() ? 1 : 0,
				PBoxPDDeviceGray.DEVICE_GRAY_TYPE);
	}

	@Test
	public void testStrokeColorSpaceLink() {
		testObject(PBOpTextShow.STROKE_COLOR_SPACE,
				((PBOpTextShow) actual).state.getRenderingMode().isStroke() ? 1 : 0,
				PBoxPDDeviceGray.DEVICE_GRAY_TYPE);
	}

	protected abstract int getUsedGlyphsAmount();

}
