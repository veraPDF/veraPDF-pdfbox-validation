package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.external.PKCSDataObject;
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDSignature;
import sun.security.pkcs.ContentInfo;	// TODO: is sun.security OK?
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;

import java.security.cert.X509Certificate;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPKCSDataObject extends PBoxExternal implements PKCSDataObject {

	private static final Logger LOGGER = Logger.getLogger(PBoxPKCSDataObject.class);

	/** Type name for {@code PBoxPKCSDataObject} */
	public static final String PKCS_DATA_OBJECT_TYPE = "PKCSDataObject";

	protected PKCS7 pkcs7;
	protected PkcsObjectType pkcsType;

	protected PBoxPDSignature signatureDictionary;

	/**
	 * Detects PKCS object type (PKCS#1 or PKCS#7) by subFilter entry in
	 * signature dictionary and constructs PKCS#7 object if needed.
	 * @param pkcsData {@link COSString} containing encoded PKCS object.
	 */
	public PBoxPKCSDataObject(COSString pkcsData, PBoxPDSignature signatureDictionary) {
		super(PKCS_DATA_OBJECT_TYPE);
		this.signatureDictionary = signatureDictionary;
		this.pkcsType = PkcsObjectType.pkcsObjectFromSubFilter(signatureDictionary.getSubFilter());

		switch (pkcsType) {
			case PKCS7: {
				try {
					pkcs7 = new PKCS7(pkcsData.getBytes());
				} catch (ParsingException e) {    //TODO: what do we do if some problem happens here?
					LOGGER.error("Passed PKCS7 object can't be read", e);
					pkcs7 = getEmptyPKCS7();
				}
				break;
			}
			case PKCS1:	// We just do nothing
				break;
			case UNKNOWN:	// = default:
				LOGGER.error("PKCS object type in digital signature cannot be recognized");	//TODO: 1) Add some exception to LOGGER (which one?) 2) Don't we want to try parsing this object in case subfilter is not specified?
		}
	}

	/**
	 * @return amount of SignerInfo entries in PKCS#7 object.
	 */
	@Override
	public Long getSignerInfoCount() {	//TODO: what if PKCS#1?
		return new Integer(pkcs7.getSignerInfos().length).longValue();
	}

	/**
	 * @return true if at least one certificate is contained in PKCS#7 object
	 * and all present certificates are not nulls if pkcsType is PKCS#7.
	 *
	 * @return true if Cert entry in signature dictionary is not empty if
	 * pkcsType is PKCS#1.
	 */
	@Override
	public Boolean getsigningCertificatePresent() {
		switch (pkcsType) {
			case PKCS7: {
				X509Certificate[] certificates = pkcs7.getCertificates();
				if (certificates.length == 0) {
					return false;
				} else {
					for (X509Certificate cert : certificates) {
						if (cert == null) {
							return false;
						}
					}
				}
				return true;
			}

			case PKCS1: {
				//TODO: add method getCert to PDSignature in pdfbox
			}

			default:
				return null;	//TODO: return false or throw exception
		}
	}

	private PKCS7 getEmptyPKCS7() {
		return new PKCS7(new AlgorithmId[]{}, new ContentInfo(new byte[]{}),
				new X509Certificate[]{}, new SignerInfo[]{});
	}

}
