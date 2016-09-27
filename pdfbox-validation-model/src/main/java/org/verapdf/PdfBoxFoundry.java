/**
 * 
 */
package org.verapdf;

import java.io.IOException;
import java.io.InputStream;

import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.impl.fixer.PBoxMetadataFixerImpl;
import org.verapdf.metadata.fixer.impl.pb.model.PDFDocumentImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.MetadataFixer;
import org.verapdf.pdfa.PDFParser;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validators.ReferenceBatchValidator;
import org.verapdf.processor.ProcessorImpl;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 22 Sep 2016:09:20:18
 */

public class PdfBoxFoundry implements VeraPDFFoundry {
	private static PdfBoxFoundry INSTANCE = new PdfBoxFoundry();
	
	private PdfBoxFoundry() {
	}
	
	public static void initialise() {
		ProcessorImpl.initialise(INSTANCE);
		ReferenceBatchValidator.initialise(INSTANCE);
	}
	
	
	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newPdfParser(java.io.InputStream)
	 */
	@Override
	public PDFParser newPdfParser(InputStream pdfStream) throws ModelParsingException, EncryptedPdfException {
		return newPdfParser(pdfStream, PDFAFlavour.AUTO);
	}

	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newPdfParser(java.io.InputStream, org.verapdf.pdfa.flavours.PDFAFlavour)
	 */
	@Override
	public PDFParser newPdfParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException {
		return ModelParser.createModelWithFlavour(pdfStream, flavour);
	}

	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newMetadataFixer(org.verapdf.metadata.fixer.utils.FixerConfig)
	 */
	@Override
	public MetadataFixer newMetadataFixer() {
		return new PBoxMetadataFixerImpl();
	}

}
