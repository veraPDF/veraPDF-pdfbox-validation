package org.verapdf.model.impl.pb.external;

/**
 * @author Sergey Shemyakov
 */
public enum PkcsObjectType {	//TODO: place this enum somewhere, it should not be in this package
	PKCS7,
	PKCS1,
	UNKNOWN;

	private static final String ADBE_PKCS7_DETACHED = "adbe.pkcs7.detached";
	public static final String ADBE_PKCS7_SHA1 = "adbe.pkcs7.sha1";
	public static final String ADBE_X509_RSA_SHA1 = "adbe.x509.rsa_sha1";

	public static PkcsObjectType pkcsObjectFromSubFilter(String subFilter) {
		switch (subFilter.toLowerCase()) {
			case ADBE_PKCS7_DETACHED:
				return PKCS7;
			case ADBE_PKCS7_SHA1:
				return PKCS7;
			case ADBE_X509_RSA_SHA1:
				return PKCS1;
			default:
				return UNKNOWN;
		}
	}
}
