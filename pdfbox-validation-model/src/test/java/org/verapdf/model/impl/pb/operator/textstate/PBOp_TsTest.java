package org.verapdf.model.impl.pb.operator.textstate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TsTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Ts.OP_TS_TYPE, null);
	}

	@Test
	public void testRiseLink() {
		testInteger(PBOp_Ts.RISE, 0);
	}
}
