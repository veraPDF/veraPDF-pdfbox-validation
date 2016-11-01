package org.verapdf.metadata.fixer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.verapdf.metadata.fixer.impl.pb.model.PDFDocumentImpl;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

/**
 * @author Evgeniy Muravitskiy
 */
public final class PBoxMetadataFixerImpl extends MetadataFixerImpl {

	public PBoxMetadataFixerImpl() {

	}

	@Override
	public MetadataFixerResult fixMetadata(InputStream toFix, OutputStream outputRepaired, ValidationResult result) throws IOException {
		return super.fixMetadata(outputRepaired, new PDFDocumentImpl(toFix), result, true, XMLProcessedObjectsParser.getInstance());
	}

	@Override
	public MetadataFixerResult fixMetadata(PDFAParser parser, OutputStream outputRepaired, ValidationResult result) {
		return super.fixMetadata(outputRepaired, parser.getPDFDocument(), result, true, XMLProcessedObjectsParser.getInstance());
	}

}
