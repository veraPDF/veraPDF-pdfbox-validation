package org.verapdf.model.impl.pb.operator.generalgs;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_M_miter_limitTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_M_miter_limit.OP_M_MITER_LIMIT_TYPE, null);
	}

	@Test
	public void testMiterLimitLink() {
		testLinkToReal(PBOp_M_miter_limit.MITER_LIMIT, PBCosInteger.COS_INTEGER_TYPE, Long.valueOf(1));
	}
}
