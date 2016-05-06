package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosLang;

/**
 * @author Maksim Bezrukov
 */
public class PBCosLang extends PBCosString implements CosLang {

	/** Type name for PBCosLang */
	public static final String COS_LANG_TYPE = "CosLang";

	public PBCosLang(COSString cosString) {
		super(cosString, COS_STRING_TYPE);
	}
}
