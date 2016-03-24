package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosUnicodeName;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Implementation of Unicode name
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosUnicodeName extends PBCosName implements CosUnicodeName {

	public static final Logger LOGGER = Logger.getLogger(PBCosUnicodeName.class);

	public static final String COS_UNICODE_NAME_TYPE = "CosUnicodeName";

	/**
	 * Default constructor.
	 *
	 * @param cosName Apache pdfbox
	 */
	public PBCosUnicodeName(COSName cosName) {
		super(cosName, COS_UNICODE_NAME_TYPE);
	}

	/**
	 * @return true if name is valid UTF-8 string
	 */
	// TODO : check implementation correctness
	@Override
	public Boolean getisValidUtf8() {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		CharsetEncoder encoder = Charset.forName("Windows-1252").newEncoder();
		ByteBuffer tmp;
		try {
			tmp = encoder.encode(CharBuffer.wrap(((COSName) this.baseObject).getName()));
		} catch (CharacterCodingException e) {
			return false;
		}

		try {
			decoder.decode(tmp);
			return true;
		} catch (CharacterCodingException e){
			return false;
		}
	}

	/**
	 * @return converted to UTF-8 name
	 */
	// TODO : check implementation correctness
	@Override
	public String getunicodeValue() {
		String name = ((COSName) this.baseObject).getName();
		byte[] bytes = name.getBytes();
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("Can not transform " + name + " to unicode string.", e);
			return null;
		}
	}

}
