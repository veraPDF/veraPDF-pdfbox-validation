package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDPerms;

import java.util.Set;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDPerms extends PBoxPDObject implements PDPerms{

	/** Type name for {@code PBoxPDPerms} */
	public static final String PERMS_TYPE = "PDPerms";

	private static COSName UC3 = COSName.getPDFName("UC3");

	/**
	 * @param dictionary is permissions dictionary.
	 */
	public PBoxPDPerms(COSDictionary dictionary) {
		super(dictionary, PERMS_TYPE);
	}

	/**
	 * @return true if the permissions dictionary contains entries other than
	 * DocMDP and UC3.
	 */
	@Override
	public Boolean getcontainsOtherEntries() {
		Set<COSName> names = ((COSDictionary) this.simplePDObject).keySet();
		for(COSName name : names) {
			if(name.compareTo(UC3) != 0 && name.compareTo(COSName.DOC_MDP) != 0) {
				return true;
			}
		}
		return false;
	}
}
