package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDSigRef;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSigRef extends PBoxPDObject implements PDSigRef{

	/** Type name for {@code PBoxPDSigRef} */
	public static final String SIGNATURE_REFERENCE_FIELD_TYPE = "PDSigRef";
	//public static final COSName DIGEST_LOCATION = new COSName("DigestLocation");
	//public static final COSName DIGEST_VALUE = new COSName("DigestValue");

	/**
	 * @param dictionary is signature reference dictionary.
	 */
	public PBoxPDSigRef(COSDictionary dictionary) { //TODO: should we check validity of passed dictionary?
		super(dictionary, SIGNATURE_REFERENCE_FIELD_TYPE);
	}

	/**
	 * @return true if any of the entries /DigestLocation, /DigestMethod, or
	 * /DigestValue is present.
	 */
	@Override
	public Boolean getcontainsDigestEntries() {
		COSDictionary dictionary = ((COSDictionary)this.simplePDObject);
		if(dictionary.containsKey(COSName.DIGEST_LOCATION) ||
				dictionary.containsKey(COSName.DIGEST_VALUE) ||
				dictionary.containsKey(COSName.DIGEST_METHOD)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return true if the document permissions dictionary contains DocMDP entry.
	 */
	@Override
	public Boolean getpermsContainDocMDP() {
		COSDictionary dictionary = ((COSDictionary)this.simplePDObject);
		if(!dictionary.containsKey(COSName.TRANSFORM_METHOD)) {	// TODO: what to do in such situations?
			return false;
		}
		COSName transformMethod = (COSName)
				dictionary.getDictionaryObject(COSName.TRANSFORM_METHOD);
		return (transformMethod.compareTo(COSName.DOC_MDP) == 0);
	}
}
