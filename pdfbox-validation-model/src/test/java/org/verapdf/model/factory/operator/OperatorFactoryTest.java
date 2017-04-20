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
package org.verapdf.model.factory.operator;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
@RunWith(Parameterized.class)
public class OperatorFactoryTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		List<Object[]> parameters = new ArrayList<>();
		parameters.add(new Object[] {new COSString("Test COSBase case."), new Integer(0)});
		parameters.add(new Object[] {Operator.getOperator("q"), new Integer(1)});
		parameters.add(new Object[] {RenderingIntent.ABSOLUTE_COLORIMETRIC, new Integer(0)});
		parameters.add(new Object[] {"Unsupported type of object.", new Integer(0)});
		return parameters;
	}

	@Parameterized.Parameter
	public Object fInput;

	@Parameterized.Parameter(value = 1)
	public int fExpected;

	@Test
	public void testOperatorsFromTokensMethod() {
		List<Object> input = new ArrayList<>(1);
		input.add(fInput);
		Assert.assertEquals(fExpected, new OperatorFactory().operatorsFromTokens(input, null, null, null).size());
	}

}
