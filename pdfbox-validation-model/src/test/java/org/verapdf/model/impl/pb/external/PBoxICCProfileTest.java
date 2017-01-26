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
package org.verapdf.model.impl.pb.external;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.external.ICCProfile;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxICCProfileTest extends BaseTest {

	public static final String expectedDeviceClass = "mntr";
	public static final String expectedColorSpace = "RGB ";
	public static final double expectedVersion = 2.1;
	public static final Long expectedColorantsCount = Long.valueOf(3);

	@Test
	public void testDeviceClassMethod() {
		Assert.assertEquals(((ICCProfile) actual).getdeviceClass(), expectedDeviceClass);
	}

	@Test
	public void testColorSpaceMethod() {
		Assert.assertEquals(((ICCProfile) actual).getcolorSpace(), expectedColorSpace);
	}

	@Test
	public void testVersionMethod() {
		Assert.assertEquals(((ICCProfile) actual).getversion().doubleValue(), expectedVersion, 0.001);
	}

	@Test
	public void testNMethod() {
		Assert.assertEquals(((ICCProfile) actual).getN(), expectedColorantsCount);
	}

	@Test
	public void testIsValidMethod() {
		Assert.assertTrue(((ICCProfile) actual).getisValid().booleanValue());
	}
}
