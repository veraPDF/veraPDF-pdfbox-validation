package org.verapdf.model.impl.pb.operator.textposition;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TmTest extends PBOpTextPositionTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tm.OP_TM_TYPE, null);
	}

	@Test
	public void testControlPointsLink() {
		testLinksToReals(PBOp_Tm.CONTROL_POINTS, 6, PBCosInteger.COS_INTEGER_TYPE);
	}
}
