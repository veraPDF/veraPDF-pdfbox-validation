package org.verapdf.model.impl.pb.operator.textstate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TlTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tl.OP_TL_TYPE, null);
	}

	@Test
	public void testLeadingLink() {
		testInteger(PBOp_Tl.LEADING, 0);
	}
}
