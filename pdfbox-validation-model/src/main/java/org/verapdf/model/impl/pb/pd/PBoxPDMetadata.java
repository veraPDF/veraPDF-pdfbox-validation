package org.verapdf.model.impl.pb.pd;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.axl.AXLMainXMPPackage;
import org.verapdf.model.impl.axl.AXLXMPPackage;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.model.xmplayer.XMPPackage;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadata extends PBoxPDObject implements PDMetadata {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDMetadata.class);

    public static final String METADATA_TYPE = "PDMetadata";

    public static final String XMP_PACKAGE = "XMPPackage";
    public static final String STREAM = "stream";

    private boolean isMainMetadata;
    private org.apache.pdfbox.pdmodel.common.PDMetadata mainMetadata;
    private PDFAFlavour flavour;

    public PBoxPDMetadata(
            org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject,
            Boolean isMainMetadata,
            PDDocument document,
            PDFAFlavour flavour) {
        super(simplePDObject, METADATA_TYPE);
        this.isMainMetadata = isMainMetadata.booleanValue();
        if (document != null && document.getDocumentCatalog() != null && document.getDocumentCatalog().getMetadata() != null) {
            this.mainMetadata = document.getDocumentCatalog().getMetadata();
        } else {
            this.mainMetadata = null;
        }
        this.flavour = flavour;
    }

    @Override
    public String getFilter() {
        List<COSName> filters = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                .getFilters();
        if (filters != null && !filters.isEmpty()) {
            StringBuilder result = new StringBuilder();
            for (COSName filter : filters) {
                result.append(filter.getName()).append(' ');
            }
            return result.substring(0, result.length() - 1);
        }
        return null;
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case XMP_PACKAGE:
				return this.getXMPPackage();
			case STREAM:
				return this.getStream();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<XMPPackage> getXMPPackage() {
        List<XMPPackage> xmp = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        try {
            COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                    .getStream();
            if (stream != null) {
                VeraPDFMeta metadata = VeraPDFMeta.parse(stream.getUnfilteredStream());
                if (isMainMetadata) {
                    xmp.add(new AXLMainXMPPackage(metadata, true, this.flavour));
                } else {
                    COSStream mainStream = mainMetadata.getStream();
                    VeraPDFXMPNode mainExtensionNode = null;
                    if (mainStream != null) {
                        VeraPDFMeta mainMeta = VeraPDFMeta.parse(mainStream.getUnfilteredStream());
                        mainExtensionNode = mainMeta.getExtensionSchemasNode();
                    }
                    xmp.add(new AXLXMPPackage(metadata, true, mainExtensionNode, this.flavour));
                }
            }
        } catch (XMPException | IOException e) {
            LOGGER.debug("Problems with parsing metadata. " + e.getMessage(), e);
            xmp.add(new AXLXMPPackage(null, false, null, this.flavour));
        }
        return xmp;
    }

    private List<CosStream> getStream() {
        COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject)
                .getStream();
		if (stream != null) {
			List<CosStream> streams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			streams.add(new PBCosStream(stream, this.document, this.flavour));
			return Collections.unmodifiableList(streams);
		}
        return Collections.emptyList();
    }
}