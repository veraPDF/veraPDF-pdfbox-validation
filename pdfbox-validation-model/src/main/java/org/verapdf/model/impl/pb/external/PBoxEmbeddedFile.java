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
package org.verapdf.model.impl.pb.external;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.ModelParser;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * Embedded file representation implemented by Apache PDFBox
 *
 * @author Maksim Bezrukov
 */
public class PBoxEmbeddedFile extends PBoxExternal implements EmbeddedFile {

	private static final Logger LOGGER = Logger.getLogger(PBoxEmbeddedFile.class);

	/** Type name for {@code PBoxEmbeddedFile} */
	public static final String EMBEDDED_FILE_TYPE = "EmbeddedFile";

	private final COSStream stream;

	public PBoxEmbeddedFile(COSDictionary dictionary) {
		super(EMBEDDED_FILE_TYPE);
		COSBase baseStream = dictionary.getDictionaryObject(COSName.F);
		if (baseStream instanceof COSStream) {
			this.stream = (COSStream) baseStream;
		} else {
			this.stream = null;
		}
	}

	@Override
	public String getSubtype() {
		if (this.stream != null) {
			return this.stream.getNameAsString(COSName.SUBTYPE);
		}
		return null;
	}

	@Override
	public Boolean getisValidPDFA12() {
		if (this.stream != null) {
			try {
				InputStream unfilteredStream = stream.getUnfilteredStream();
				unfilteredStream.mark(Integer.MAX_VALUE);
				try (PDFAParser parser1b = ModelParser.createModelWithFlavour(unfilteredStream,
						PDFAFlavour.PDFA_1_B)) {
					PDFAValidator validator1b = ValidatorFactory.createValidator(PDFAFlavour.PDFA_1_B, false, 1);
					ValidationResult result1b = validator1b.validate(parser1b);
					if (result1b.isCompliant()) {
						return Boolean.TRUE;
					}
				}
				unfilteredStream.reset();
				try (PDFAParser parser2b = ModelParser.createModelWithFlavour(unfilteredStream,
						PDFAFlavour.PDFA_2_B)) {
					PDFAValidator validator2b = ValidatorFactory.createValidator(PDFAFlavour.PDFA_2_B, false, 1);
					ValidationResult result2b = validator2b.validate(parser2b);
					return Boolean.valueOf(result2b.isCompliant());
				}
			} catch (Throwable e) {
				LOGGER.debug("Exception during validation of embedded file", e);
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
}
