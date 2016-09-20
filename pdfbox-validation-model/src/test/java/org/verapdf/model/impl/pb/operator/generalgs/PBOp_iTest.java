package org.verapdf.model.impl.pb.operator.generalgs;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_iTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_i.OP_I_TYPE, null);
	}

	@Test
	public void testFlatnessLink() {
		testLinkToReal(PBOp_i.FLATNESS, PBCosInteger.COS_INTEGER_TYPE, Long.valueOf(50));
	}
}
