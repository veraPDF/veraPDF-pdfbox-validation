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
package org.verapdf.model;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.component.Components;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;
import org.verapdf.features.AbstractFeaturesExtractor;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.metadata.fixer.entity.PDFDocument;
import org.verapdf.metadata.fixer.impl.pb.model.PDFDocumentImpl;
import org.verapdf.model.impl.pb.containers.StaticContainers;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelParser implements PDFAParser {
	private static final ReleaseDetails pdfBoxReleaseDetails = ReleaseDetails.addDetailsFromResource(
			ReleaseDetails.APPLICATION_PROPERTIES_ROOT + "pdfbox-parser." + ReleaseDetails.PROPERTIES_EXT);
	private static final URI id = URI.create("http://pdfa.verapdf.org/parser#pdfbox");
	private static final ComponentDetails details = Components.veraDetails(id, "PDFBox Parser",
			pdfBoxReleaseDetails.getVersion(), "veraPDF PDFBox based model parser.");

	private static final Logger logger = Logger.getLogger(ModelParser.class);

	private PDDocument document;

	private final PDFAFlavour flavour;

	private ModelParser(final InputStream docStream, PDFAFlavour flavour) throws IOException {
		this.document = PDDocument.load(docStream, false, true);
		this.flavour = (flavour == PDFAFlavour.NO_FLAVOUR) ? obtainFlavour(this.document) : flavour;
	}

	private ModelParser(final File pdfFile, PDFAFlavour flavour) throws IOException {
		this.document = PDDocument.load(pdfFile, false, true);
		this.flavour = (flavour == PDFAFlavour.NO_FLAVOUR) ? obtainFlavour(this.document) : flavour;
	}

	public static ModelParser createModelWithFlavour(InputStream toLoad, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException {
		try {
			cleanUp();
			return new ModelParser(toLoad, flavour);
		} catch (InvalidPasswordException excep) {
			throw new EncryptedPdfException("The PDF stream appears to be encrypted.", excep);
		} catch (IOException excep) {
			throw new ModelParsingException("Couldn't parse stream", excep);
		}
	}

	public static ModelParser createModelWithFlavour(File pdfFile, PDFAFlavour flavour)
			throws ModelParsingException, EncryptedPdfException {
		try {
			cleanUp();
			return new ModelParser(pdfFile, flavour);
		} catch (InvalidPasswordException excep) {
			throw new EncryptedPdfException("The PDF stream appears to be encrypted.", excep);
		} catch (IOException excep) {
			throw new ModelParsingException("Couldn't parse stream", excep);
		}
	}

	private static PDFAFlavour obtainFlavour(PDDocument document) {
		PDFAFlavour defaultFlavour = Foundries.defaultInstance().defaultFlavour();
		if (document == null || document.getDocumentCatalog() == null) {
			return defaultFlavour;
		}
		PDMetadata metadata = document.getDocumentCatalog().getMetadata();
		if (metadata == null) {
			return defaultFlavour;
		}
		try (InputStream is = metadata.exportXMPMetadata()) {
			VeraPDFMeta veraPDFMeta = VeraPDFMeta.parse(is);
			Integer identificationPart = veraPDFMeta.getIdentificationPart();
			String identificationConformance = veraPDFMeta.getIdentificationConformance();
			PDFAFlavour pdfaFlavour = PDFAFlavour.byFlavourId(identificationPart + identificationConformance);
			// TODO: remove that logic after updating NO_FLAVOUR into base pdf validation flavour
			if (pdfaFlavour == PDFAFlavour.NO_FLAVOUR) {
				return defaultFlavour;
			}
			return pdfaFlavour;
		} catch (IOException | XMPException e) {
			logger.error(e);
			return defaultFlavour;
		}
	}

	private static void cleanUp() {
		StaticContainers.clearAllContainers();
	}

	@Override
	public PDFDocument getPDFDocument() {
		return new PDFDocumentImpl(this.document);
	}

	@Override
	public ComponentDetails getDetails() {
		return details;
	}

	/**
	 * Method return root object of model implementation from pdf box model
	 * together with the hierarchy.
	 *
	 * @return root object representing by
	 *         {@link org.verapdf.model.coslayer.CosDocument}
	 * @throws IOException
	 *             when target file is not pdf or pdf file is not contain root
	 *             object
	 */
	@Override
	public org.verapdf.model.baselayer.Object getRoot() {
		return new PBCosDocument(this.document, this.flavour);
	}

	@Override
	public PDFAFlavour getFlavour() {
		return this.flavour;
	}

	@Override
	public FeatureExtractionResult getFeatures(FeatureExtractorConfig config) {
		return PBFeatureParser.getFeaturesCollection(this.document, config);
	}

	@Override
	public FeatureExtractionResult getFeatures(FeatureExtractorConfig config,
			List<AbstractFeaturesExtractor> extractors) {
		return PBFeatureParser.getFeaturesCollection(this.document, extractors, config);
	}

	@Override
	public void close() {
		try {
			if (this.document != null) {
				this.document.close();
			}
		} catch (IOException e) {
			logger.error("Problems with document close.", e);
		}
	}
}
