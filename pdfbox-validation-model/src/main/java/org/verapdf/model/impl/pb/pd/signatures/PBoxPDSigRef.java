package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDSigRef;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSigRef extends PBoxPDObject implements PDSigRef{

	private static final Logger LOGGER = Logger.getLogger(PBoxPDSigRef.class);

	/** Type name for {@code PBoxPDSigRef} */
	public static final String SIGNATURE_REFERENCE_TYPE = "PDSigRef";

	/**
	 * @param dictionary is signature reference dictionary.
	 */
	public PBoxPDSigRef(COSDictionary dictionary, PDDocument document) {
		super(dictionary, SIGNATURE_REFERENCE_TYPE);
		this.document = document;
	}

	/**
	 * @return true if any of the entries /DigestLocation, /DigestMethod, or
	 * /DigestValue is present.
	 */
	@Override
	public Boolean getcontainsDigestEntries() {
		COSDictionary dictionary = ((COSDictionary)this.simplePDObject);
		return dictionary.containsKey(COSName.DIGEST_LOCATION) ||
				dictionary.containsKey(COSName.DIGEST_VALUE) ||
				dictionary.containsKey(COSName.DIGEST_METHOD);
	}

	/**
	 * @return true if the document permissions dictionary contains DocMDP entry.
	 */
	@Override
	public Boolean getpermsContainDocMDP() {
		COSDictionary documentCatalog =
				this.document.getDocumentCatalog().getCOSObject();
		COSDictionary perms = (COSDictionary)
				documentCatalog.getDictionaryObject(COSName.PERMS);
		if (perms == null) {
			LOGGER.error("Document catalog doesn't contain /Perms entry");
			return false;	//TODO: do we return false?
		}
		return perms.containsKey(COSName.DOC_MDP);
	}
}
