package org.verapdf.model;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.core.ModelParsingException;
import org.verapdf.features.FeaturesExtractor;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.model.impl.pb.containers.StaticContainers;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.pdfa.PDFParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelParser implements PDFParser, Closeable {

    private static final Logger LOGGER = Logger.getLogger(ModelParser.class);

    private static final PDFAFlavour DEFAULT_FLAVOUR = PDFAFlavour.PDFA_1_B;

    private PDDocument document;

    private final PDFAFlavour flavour;

    private ModelParser(final InputStream docStream, PDFAFlavour flavour) throws IOException {
        this.document = PDDocument.load(docStream, false, true);
        this.flavour = (flavour == PDFAFlavour.AUTO) ? obtainFlavour(this.document) : flavour;
    }

    
    public static ModelParser createModelWithFlavour(InputStream toLoad, PDFAFlavour flavour) throws ModelParsingException {
        try {
            cleanUp();
            return new ModelParser(toLoad, (flavour == PDFAFlavour.NO_FLAVOUR || flavour == null) ? DEFAULT_FLAVOUR : flavour);
        } catch (IOException excep) {
            throw new ModelParsingException("Couldn't parse stream", excep);
        }
    }

    private static PDFAFlavour obtainFlavour(PDDocument document) {
        PDDocumentCatalog documentCatalog = document.getDocumentCatalog();
        if (documentCatalog == null) {
            return DEFAULT_FLAVOUR;
        }
        PDMetadata metadata = documentCatalog.getMetadata();
        if (metadata == null) {
            return DEFAULT_FLAVOUR;
        }
        try {
            InputStream is = metadata.exportXMPMetadata();
            VeraPDFMeta veraPDFMeta = VeraPDFMeta.parse(is);
            Integer identificationPart = veraPDFMeta.getIdentificationPart();
            String identificationConformance = veraPDFMeta.getIdentificationConformance();
            PDFAFlavour pdfaFlavour = PDFAFlavour.byFlavourId(identificationPart + identificationConformance);
            return pdfaFlavour == PDFAFlavour.NO_FLAVOUR ? DEFAULT_FLAVOUR : pdfaFlavour;
        } catch (IOException | XMPException e) {
            LOGGER.error(e);
            return DEFAULT_FLAVOUR;
        }
    }

    private static void cleanUp() {
        StaticContainers.clearAllContainers();
    }

    /**
     * Get {@code PDDocument} object for current file.
     *
     * @return {@link org.apache.pdfbox.pdmodel.PDDocument} object of pdfbox
     *         library.
     * @throws IOException
     *             when target file is not pdf or pdf file is not contain root
     *             object
     */
    public PDDocument getPDDocument() throws IOException {
        return this.document;
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
    public FeaturesCollection getFeatures(List<FeaturesExtractor> extractors) {
        return PBFeatureParser.getFeaturesCollection(this.document, extractors);
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
