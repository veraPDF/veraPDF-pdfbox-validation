package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.external.JPEG2000;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.external.PBoxJPEG2000;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDXImage;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImage extends PBoxPDXObject implements PDXImage {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDXImage.class);

    public static final String X_IMAGE_TYPE = "PDXImage";

    public static final String IMAGE_CS = "imageCS";
    public static final String ALTERNATES = "Alternates";
    public static final String INTENT = "Intent";
    public static final String JPX_STREAM = "jpxStream";

    private final boolean interpolate;
    private List<JPEG2000> jpeg2000List = null;
    private PDColorSpace colorSpaceFromImage = null;

    public PBoxPDXImage(PDImageXObjectProxy simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, PDInheritableResources.EMPTY_EXTENDED_RESOURCES, X_IMAGE_TYPE, document, flavour);
        this.interpolate = simplePDObject.getInterpolate();
    }

    @Override
    public Boolean getInterpolate() {
        return Boolean.valueOf(this.interpolate);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case INTENT:
                return this.getIntent();
            case IMAGE_CS:
                return this.getImageCS();
            case ALTERNATES:
                return this.getAlternates();
            case JPX_STREAM:
                return this.getJPXStream();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CosRenderingIntent> getIntent() {
        COSDictionary imageStream = (COSDictionary) this.simplePDObject
                .getCOSObject();
        COSName intent = imageStream.getCOSName(COSName.getPDFName(INTENT));
        if (intent != null) {
            List<CosRenderingIntent> intents = new ArrayList<>(
                    MAX_NUMBER_OF_ELEMENTS);
            intents.add(new PBCosRenderingIntent(intent));
            return Collections.unmodifiableList(intents);
        }
        return Collections.emptyList();
    }

    private List<PDColorSpace> getImageCS() {
        if (this.jpeg2000List == null) {
            this.jpeg2000List = parseJPXStream();
        }
        PDImageXObjectProxy image = (PDImageXObjectProxy) this.simplePDObject;
        if (!image.isStencil()) {
            try {
                PDColorSpace buffer = ColorSpaceFactory
                        .getColorSpace(image.getColorSpace(), this.document, this.flavour);
                if (buffer == null) {
                    buffer = this.colorSpaceFromImage;
                }
                if (buffer != null) {
                    List<PDColorSpace> colorSpaces =
                            new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                    colorSpaces.add(buffer);
                    return Collections.unmodifiableList(colorSpaces);
                }
            } catch (IOException e) {
                LOGGER.warn("Could not obtain Image XObject color space. " + e.getMessage(), e);
            }
        }
        return Collections.emptyList();
    }

    private List<? extends PDXImage> getAlternates() {
        final List<PDXImage> alternates = new ArrayList<>();
        final COSStream imageStream = ((PDImageXObjectProxy) this.simplePDObject)
                .getCOSStream();
        final COSBase buffer = imageStream.getDictionaryObject(COSName
                .getPDFName(ALTERNATES));
        this.addAlternates(alternates, buffer, ((PDImageXObjectProxy) this.simplePDObject)
                .getResources());
        return alternates;
    }

    private void addAlternates(List<PDXImage> alternates, COSBase buffer,
                               PDResources resources) {
        if (buffer instanceof COSArray) {
            for (COSBase element : (COSArray) buffer) {
                if (element instanceof COSObject) {
                    element = ((COSObject) element).getObject();
                }
                if (element instanceof COSDictionary) {
                    this.addAlternate(alternates, (COSDictionary) element,
                            resources);
                }
            }
        }
    }

    private void addAlternate(List<PDXImage> alternates, COSDictionary buffer,
                              PDResources resources) {
        COSBase alternatesImages = buffer.getDictionaryObject(COSName.IMAGE);
        if (alternatesImages instanceof COSStream) {

            final PDStream stream = new PDStream((COSStream) alternatesImages);
            PDImageXObjectProxy imageXObject = new PDImageXObjectProxy(stream, resources);
            alternates.add(new PBoxPDXImage(imageXObject, this.document, this.flavour));
        }
    }

    private List<JPEG2000> getJPXStream() {
        if (jpeg2000List == null) {
            jpeg2000List = parseJPXStream();
        }
        return jpeg2000List;
    }

    private List<JPEG2000> parseJPXStream() {
        try {
            PDStream stream = ((PDImageXObjectProxy) (this.simplePDObject)).getPDStream();
            List<COSName> filters = stream.getFilters();
            if (filters != null && filters.contains(COSName.JPX_DECODE)) {
                // TODO: handle the case when jpx stream is additionally hex encoded
                InputStream image = stream.getStream().getFilteredStream();
                ArrayList<JPEG2000> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                PBoxJPEG2000 jpeg2000 = PBoxJPEG2000.fromStream(image, this.document, this.flavour);
                this.colorSpaceFromImage = jpeg2000.getImageColorSpace();
                list.add(jpeg2000);
                return Collections.unmodifiableList(list);
            }
        } catch (IOException e) {
            LOGGER.warn("Problems with stream obtain.", e);
        }
        return Collections.emptyList();
    }

	/**
     * @return true if current image contains SMask value of type stream or SMaskInData value greater then 0
     */
    public boolean containsTransparency() {
        if (this.simplePDObject == null) {
            return false;
        }

        COSBase base = this.simplePDObject.getCOSObject();
        if (base instanceof COSStream) {
            COSStream stream = (COSStream) base;
            if (stream.getDictionaryObject(COSName.SMASK) instanceof COSStream) {
                return true;
            }

            COSBase sMaskInData = stream.getDictionaryObject(COSName.getPDFName("SMaskInData"));
            if (sMaskInData instanceof COSNumber) {
                return ((COSNumber) sMaskInData).doubleValue() > 0;
            }
        }

        return false;
    }
}
