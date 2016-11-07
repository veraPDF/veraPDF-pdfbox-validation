package org.verapdf.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
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
import org.verapdf.pdfa.PDFAParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelParser implements PDFAParser {

    private static final Logger LOGGER = Logger.getLogger(ModelParser.class);

    private PDDocument document;

    private final PDFAFlavour flavour;

    private ModelParser(final InputStream docStream, PDFAFlavour flavour) throws IOException {
        this.document = PDDocument.load(docStream, false, true);
        this.flavour = (flavour == PDFAFlavour.NO_FLAVOUR) ? obtainFlavour(this.document) : flavour;
    }

    
    public static ModelParser createModelWithFlavour(InputStream toLoad, PDFAFlavour flavour) throws ModelParsingException, EncryptedPdfException {
        try {
            cleanUp();
            return new ModelParser(toLoad, flavour);
        } catch (InvalidPasswordException excep) {
            throw new EncryptedPdfException("The PDF stream appears to be encrypted.", excep);
        } catch (IOException excep) {
            throw new ModelParsingException("Couldn't parse stream", excep);
        }
    }

    private static PDFAFlavour obtainFlavour(PDDocument document) {
    	if (document == null || document.getDocumentCatalog() == null) {
    		return PDFAFlavour.NO_FLAVOUR;
    	}
        PDMetadata metadata = document.getDocumentCatalog().getMetadata();
        if (metadata == null) {
            return PDFAFlavour.NO_FLAVOUR;
        }
        try (InputStream is = metadata.exportXMPMetadata()) {
            VeraPDFMeta veraPDFMeta = VeraPDFMeta.parse(is);
            Integer identificationPart = veraPDFMeta.getIdentificationPart();
            String identificationConformance = veraPDFMeta.getIdentificationConformance();
            PDFAFlavour pdfaFlavour = PDFAFlavour.byFlavourId(identificationPart + identificationConformance);
            return pdfaFlavour;
        } catch (IOException | XMPException e) {
            LOGGER.error(e);
            return PDFAFlavour.NO_FLAVOUR;
        }
    }

    private static void cleanUp() {
        StaticContainers.clearAllContainers();
    }

    @Override
	public PDFDocument getPDFDocument() {
    	return new PDFDocumentImpl(this.document);
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
    public FeatureExtractionResult getFeatures(FeatureExtractorConfig config, List<AbstractFeaturesExtractor> extractors) {
        return PBFeatureParser.getFeaturesCollection(this.document, extractors, config);
    }

    @Override
	public void close() {
		try {
            if (this.document != null) {
                this.document.close();
            }
		} catch (IOException e) {
            LOGGER.error("Problems with document close.", e);
        }
	}
}
