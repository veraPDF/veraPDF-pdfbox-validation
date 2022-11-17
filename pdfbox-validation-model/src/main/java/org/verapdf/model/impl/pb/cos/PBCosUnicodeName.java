/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.cos;

import java.util.logging.Logger;
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

	public static final Logger LOGGER = Logger.getLogger(PBCosUnicodeName.class.getCanonicalName());

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
			LOGGER.log(java.util.logging.Level.INFO, e.getMessage());
			return Boolean.FALSE;
		}

		try {
			decoder.decode(tmp);
			return Boolean.TRUE;
		} catch (CharacterCodingException e){
			LOGGER.log(java.util.logging.Level.INFO, e.getMessage());
			return Boolean.FALSE;
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
			LOGGER.log(java.util.logging.Level.INFO, "Can not transform " + name + " to unicode string. " + e.getMessage());
			return null;
		}
	}

}
