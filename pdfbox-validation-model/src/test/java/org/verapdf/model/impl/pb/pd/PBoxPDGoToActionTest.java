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
package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGoToActionTest extends PBoxPDActionTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		actual = new PBoxPDGoToAction((PDActionGoTo) setUp(PBoxPDGoToAction.GOTO_ACTION_TYPE, 66));
		expectedID = "66 0 obj PDGoToAction";
	}

	@Override
	@Test
	public void testSMethod() {
		Assert.assertEquals("GoTo", ((PDAction) actual).getS());
	}

	@Override
	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(1, nextAction.size());
		Assert.assertEquals(PBoxPDNamedAction.NAMED_ACTION_TYPE, nextAction.get(0).getObjectType());
	}
}
