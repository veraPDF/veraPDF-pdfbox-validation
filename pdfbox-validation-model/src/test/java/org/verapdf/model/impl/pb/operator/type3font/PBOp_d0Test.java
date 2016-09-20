package org.verapdf.model.impl.pb.operator.type3font;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d0Test extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_d0.OP_D0_TYPE, null);
	}

	@Test
	public void testHorizontalDisplacementLink() {
		testInteger(PBOp_d0.HORIZONTAL_DISPLACEMENT, 300);
	}

	@Test
	public void testVerticalDisplacementLink() {
		testInteger(PBOp_d0.VERTICAL_DISPLACEMENT, 50);
	}
}
