package org.verapdf.model.impl.pb.operator.type3font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosNumber;
import org.verapdf.model.operator.Op_d0;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d0 extends PBOpType3Font implements Op_d0 {

	public static final String OP_D0_TYPE = "Op_d0";

	public static final String HORIZONTAL_DISPLACEMENT = "horizontalDisplacement";
	public static final String VERTICAL_DISPLACEMENT = "verticalDisplacement";

	public PBOp_d0(List<COSBase> arguments) {
		super(arguments, OP_D0_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case HORIZONTAL_DISPLACEMENT:
				return this.getHorizontalDisplacement();
			case VERTICAL_DISPLACEMENT:
				return this.getVerticalDisplacement();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosNumber> getHorizontalDisplacement() {
		if (this.arguments.size() > 1) {
			COSBase base = this.arguments.get(this.arguments.size() - 2);
			if (base instanceof COSNumber) {
				List<CosNumber> real = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				real.add(PBCosNumber.fromPDFBoxNumber(base));
				return Collections.unmodifiableList(real);
			}
		}
		return Collections.emptyList();
	}

	private List<CosNumber> getVerticalDisplacement() {
		return this.getLastNumber();
	}
}
