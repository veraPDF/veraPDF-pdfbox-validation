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
/**
 * 
 */
package org.verapdf.pdfbox.foundry;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.metadata.fixer.PBoxMetadataFixerImpl;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.AbstractFoundry;
import org.verapdf.pdfa.VeraPDFFoundry;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.MetadataFixer;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 22 Sep 2016:09:20:18
 */

class PdfBoxFoundry extends AbstractFoundry {
	private static final URI id = URI.create("http://pdfa.verapdf.org/Foundry#pdfbox");
	private static final ReleaseDetails pdfBoxReleaseDetails = ReleaseDetails.addDetailsFromResource(
			ReleaseDetails.APPLICATION_PROPERTIES_ROOT + "pdfbox-validation." + ReleaseDetails.PROPERTIES_EXT);

	private static final ComponentDetails details = Components.veraDetails(id, "VeraPDF PDFBox Foundry",
			pdfBoxReleaseDetails.getVersion(), "This foundry instance provides the PDF Box based validation library.");
	private static final PdfBoxFoundry instance = new PdfBoxFoundry();

	private PdfBoxFoundry() {
		super();
	}

	@Override
	public ComponentDetails getDetails() {
		return details;
	}

	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newPdfParser(java.io.InputStream)
	 */
	@Override
	public PDFAParser createParser(InputStream pdfStream) throws ModelParsingException, EncryptedPdfException {
		return createParser(pdfStream, PDFAFlavour.NO_FLAVOUR);
	}

	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newPdfParser(java.io.InputStream,
	 *      org.verapdf.pdfa.flavours.PDFAFlavour)
	 */
	@Override
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(pdfStream, flavour);
	}

	@Override
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, PDFAFlavour defaultFlavour)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(pdfStream, flavour, defaultFlavour);
	}

	@Override
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, String password)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(pdfStream, flavour);
	}

	@Override
	public PDFAParser createParser(InputStream pdfStream, PDFAFlavour flavour, PDFAFlavour defaultFlavour, String password)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(pdfStream, flavour, defaultFlavour);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PDFAParser createParser(File file, PDFAFlavour pdfaFlavour)
			throws ModelParsingException, EncryptedPdfException {
		return ModelParser.createModelWithFlavour(file, pdfaFlavour);
	}

	@Override
	public PDFAParser createParser(File file)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(file, PDFAFlavour.NO_FLAVOUR);
	}

	@Override
	public PDFAParser createParser(File file, PDFAFlavour pdfaFlavour, String password)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(file, pdfaFlavour);
	}

	@Override
	public PDFAParser createParser(File file, PDFAFlavour pdfaFlavour, PDFAFlavour defaultFlavour, String password)
			throws ModelParsingException, EncryptedPdfException {
		return createParser(file, pdfaFlavour, defaultFlavour);
	}

	@Override
	public PDFAParser createParser(File file, PDFAFlavour pdfaFlavour, PDFAFlavour defaultFlavour)
			throws ModelParsingException, EncryptedPdfException {
		return ModelParser.createModelWithFlavour(file, pdfaFlavour, defaultFlavour);
	}

	/**
	 * @see org.verapdf.pdfa.VeraPDFFoundry#newMetadataFixer(org.verapdf.metadata.fixer.utils.FixerConfig)
	 */
	@Override
	public MetadataFixer createMetadataFixer() {
		return new PBoxMetadataFixerImpl();
	}

	@Override
	public String getParserId() {
		return "PDFBox";
	}

	public static ReleaseDetails getReleaseDetails() {
		return pdfBoxReleaseDetails;
	}

	static VeraPDFFoundry getInstance() {
		return instance;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
}
