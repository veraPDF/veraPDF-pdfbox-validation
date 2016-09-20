package org.verapdf.model.impl.pb.operator.textstate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TcTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tc.OP_TC_TYPE, null);
	}

	@Test
	public void testCharSpacingLink() {
		testInteger(PBOp_Tc.CHAR_SPACING, 1);
	}
}
