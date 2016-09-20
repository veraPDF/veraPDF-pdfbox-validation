package org.verapdf.model.impl.pb.operator.pathconstruction;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_reTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_re.OP_RE_TYPE, null);
	}

	@Test
	public void testRectangleBoxTest() {
		testLinksToReals(PBOp_re.RECT_BOX, 4, PBCosInteger.COS_INTEGER_TYPE);
	}
}
