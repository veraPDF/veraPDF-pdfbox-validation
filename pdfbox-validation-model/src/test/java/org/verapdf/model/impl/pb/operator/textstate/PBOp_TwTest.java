package org.verapdf.model.impl.pb.operator.textstate;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TwTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tw.OP_TW_TYPE, null);
	}

	@Test
	public void testRiseLink() {
		testInteger(PBOp_Tw.WORD_SPACE, 50);
	}
}
