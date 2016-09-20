package org.verapdf.model.impl.pb.operator.specialgs;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_cmTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_cm.OP_CM_TYPE, null);
	}

	@Test
	public void testMatrixLink() {
		testLinksToReals(PBOp_cm.MATRIX, 6, PBCosInteger.COS_INTEGER_TYPE);
	}
}
