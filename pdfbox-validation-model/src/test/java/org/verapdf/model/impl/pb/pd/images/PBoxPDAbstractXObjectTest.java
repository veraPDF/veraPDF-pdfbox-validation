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
package org.verapdf.model.impl.pb.pd.images;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxPDAbstractXObjectTest extends BaseTest {

	protected static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@Test
	public abstract void testSubtypeMethod();

	@Test
	public void testSMaskLink() {
		List<? extends Object> sMask = actual.getLinkedObjects(PBoxPDXObject.S_MASK);
		Assert.assertEquals(0, sMask.size());
	}

	@Test
	public void testOPILink() {
		List<? extends Object> opi = actual.getLinkedObjects(PBoxPDXObject.OPI);
		Assert.assertEquals(0, opi.size());
	}

}
