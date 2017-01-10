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
package org.verapdf.model.impl.pb.operator.base;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.tools.resources.PDInheritableResources;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOperatorTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "operator/Operators.pdf";

	private static final COSObjectKey KEY = new COSObjectKey(15, 0);

	protected static void setUpOperatorsList(String type, String id) throws IOException, URISyntaxException {
		expectedType = TYPES.contains(type) ? type : null;
		expectedID = id;
		setUp(FILE_RELATIVE_PATH);

		COSObject objectFromPool = document.getDocument().getObjectFromPool(KEY);
		COSStream stream = (COSStream) objectFromPool.getObject();
		PDResources resources = document.getPage(0).getResources();

		PDFStreamParser parser = new PDFStreamParser(stream, true);
		parser.parse();

		List<Operator> operators = new OperatorFactory().operatorsFromTokens(parser.getTokens(),
				PDInheritableResources.getInstance(resources), document, null);
		actual = getActual(operators, expectedType);

		operators.clear();
	}

	private static Object getActual(List<Operator> operators, String type) {
		for (Operator operator : operators) {
			if (type.equals(operator.getObjectType())) {
				return operator;
			}
		}

		return null;
	}

	protected void testLinksToReals(String link, int count, String type) {
		List<? extends Object> linkedObjects = actual.getLinkedObjects(link);

		Assert.assertEquals(count, linkedObjects.size());

		for (Object object : linkedObjects) {
			Assert.assertEquals(type, object.getObjectType());
		}

	}

	protected static void testLinkToDictionary(String link, String type, Number expectedResult) {
		Object object = testObject(link, 1, type);
		Assert.assertEquals(expectedResult, ((CosDict) object).getsize());
	}

	protected static Object testObject(String link, int count, String type) {
		List<? extends Object> linkedObjects = actual.getLinkedObjects(link);

		Assert.assertEquals(count, linkedObjects.size());

		if (count > 0) {
			Object object = linkedObjects.get(0);
			Assert.assertEquals(type, object.getObjectType());
			return object;
		}

		return null;
	}

	protected static void testReal(String link, int expectedValue) {
		CosReal object = (CosReal) testObject(link, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertNotNull(object);
		Assert.assertEquals(Long.valueOf(expectedValue), object.getintValue());
	}

	protected static void testInteger(String link, int expectedValue) {
		CosInteger object = (CosInteger) testObject(link, 1, PBCosInteger.COS_INTEGER_TYPE);
		Assert.assertNotNull(object);
		Assert.assertEquals(Long.valueOf(expectedValue), object.getintValue());
	}

}
