package org.verapdf.model;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.pdfa.ValidationModelParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Current class is entry point to model implementation.
 *
 * @author Evgeniy Muravitskiy
 */
public final class ModelParser implements ValidationModelParser, Closeable {

    private static final Logger LOGGER = Logger.getLogger(ModelParser.class);

    private static final PDFAFlavour DEFAULT_FLAVOUR = PDFAFlavour.PDFA_1_B;

    private PDDocument document;

    private final PDFAFlavour flavour;

    private ModelParser(PDDocument document, PDFAFlavour flavour) throws IOException {
        this.document = document;
        this.flavour = flavour;
    }

    public static ModelParser createModelWithFlavour(InputStream toLoad, PDFAFlavour flavour) throws IOException {
        PDDocument document = PDDocument.load(toLoad, false, true);
        PDFAFlavour resultFlavour;
        if (flavour == PDFAFlavour.AUTO) {
            resultFlavour = obtainFlavour(document);
        } else if (flavour == PDFAFlavour.NO_FLAVOUR || flavour == null) {
            resultFlavour = DEFAULT_FLAVOUR;
        } else {
            resultFlavour = flavour;
        }
        return new ModelParser(document, resultFlavour);
    }

    private static PDFAFlavour obtainFlavour(PDDocument document) {
        if (document == null) {
            return DEFAULT_FLAVOUR;
        }
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
    public org.verapdf.model.baselayer.Object getRoot() throws IOException {
        return new PBCosDocument(this.document, this.flavour);
    }

    @Override
    public PDFAFlavour getFlavour() {
        return this.flavour;
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
