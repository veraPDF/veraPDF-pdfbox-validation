package org.verapdf.model.impl.pb.operator.textstate;

import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.operator.Op_Ts;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_Ts extends PBOpTextState implements Op_Ts {

	public static final String OP_TS_TYPE = "Op_Ts";

	public static final String RISE = "rise";

	public PBOp_Ts(List<COSBase> arguments) {
		super(arguments, OP_TS_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (RISE.equals(link)) {
			return this.getRise();
		}
		return super.getLinkedObjects(link);
	}

	private List<CosNumber> getRise() {
		return this.getLastNumber();
	}
}
