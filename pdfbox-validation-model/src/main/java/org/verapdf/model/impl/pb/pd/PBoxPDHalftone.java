package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.pdlayer.PDHalftone;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDHalftone extends PBoxPDObject implements PDHalftone {

	public static final String HALFTONE_TYPE = "PDHalftone";
	private final String halftoneName;
	private final Long halftoneType;

	public PBoxPDHalftone(COSDictionary dict) {
		super(dict, HALFTONE_TYPE);
		this.halftoneName = PBoxPDHalftone.getHalftoneName(dict);
		this.halftoneType = PBoxPDHalftone.getHalftoneType(dict);
	}

	public PBoxPDHalftone(COSName name) {
		super(name, HALFTONE_TYPE);
		this.halftoneName = name.getName();
		this.halftoneType = null;
	}

	private static Long getHalftoneType(COSDictionary dict) {
		COSBase type = dict.getDictionaryObject(COSName.getPDFName("HalftoneType"));
		return !(type instanceof COSNumber) ? null :
				Long.valueOf(((COSNumber) type).longValue());
	}

	private static String getHalftoneName(COSDictionary dict) {
		COSBase name = dict.getDictionaryObject(COSName.getPDFName("HalftoneName"));
		if (name instanceof COSName) {
			return ((COSName) name).getName();
		} else if (name instanceof COSString) {
			return ((COSString) name).getString();
		} else if (name != null) {
			return name.toString();
		} else {
			return null;
		}
	}

	@Override
	public Long getHalftoneType() {
		return this.halftoneType;
	}

	@Override
	public String getHalftoneName() {
		return this.halftoneName;
	}
}
