package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.pdmodel.graphics.color.PDICCBased;
import org.verapdf.model.pdlayer.PDICCBasedCMYK;

/**
 * @author Maksim Bezrukov
 */
public class PBoxPDICCBasedCMYK extends PBoxPDICCBased implements PDICCBasedCMYK {

	public static final String ICC_BASED_CMYK_TYPE = "PDICCBasedCMYK";

	private final Long opm;
	private final Boolean overprintingFlag;

	public PBoxPDICCBasedCMYK(PDICCBased simplePDObject, int op, boolean overprintingFlag) {
		super(simplePDObject, ICC_BASED_CMYK_TYPE);
		this.opm = Long.valueOf(op);
		this.overprintingFlag = Boolean.valueOf(overprintingFlag);
	}

	@Override
	public Long getOPM() {
		return this.opm;
	}

	@Override
	public Boolean getoverprintFlag() {
		return this.overprintingFlag;
	}
}
