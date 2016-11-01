package org.verapdf.model.impl.pb.external;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.ModelParser;
import org.verapdf.model.external.EmbeddedFile;
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
				try (ModelParser parser1b = ModelParser.createModelWithFlavour(unfilteredStream,
						PDFAFlavour.PDFA_1_B)) {
					PDFAValidator validator1b = ValidatorFactory.createValidator(PDFAFlavour.PDFA_1_B, false, 1);
					ValidationResult result1b = validator1b.validate(parser1b);
					if (result1b.isCompliant()) {
						return Boolean.TRUE;
					}
				}
				unfilteredStream.reset();
				try (ModelParser parser2b = ModelParser.createModelWithFlavour(unfilteredStream,
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
