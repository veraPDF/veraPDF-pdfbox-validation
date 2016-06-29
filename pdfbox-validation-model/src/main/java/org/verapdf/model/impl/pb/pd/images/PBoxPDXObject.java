package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.graphics.PDPostScriptXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDXObject;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXObject extends PBoxPDResources implements PDXObject {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDXObject.class);

    protected final PDDocument document;
    protected final PDFAFlavour flavour;

    public static final String X_OBJECT_TYPE = "PDXObject";

    public static final String OPI = "OPI";
    public static final String S_MASK = "SMask";

	protected final PDInheritableResources resources;
	private final String subtype;

    public PBoxPDXObject(
            org.apache.pdfbox.pdmodel.graphics.PDXObject simplePDObject, PDDocument document, PDFAFlavour flavour) {
        this(simplePDObject, PDInheritableResources.EMPTY_EXTENDED_RESOURCES, X_OBJECT_TYPE, document, flavour);
    }

	protected PBoxPDXObject(COSObjectable simplePDObject, PDInheritableResources resources, final String type, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, type);
		this.resources = resources;
		this.subtype = this.getSubtype((org.apache.pdfbox.pdmodel.graphics.PDXObject) this.simplePDObject);
        this.document = document;
        this.flavour = flavour;
	}

	private String getSubtype(org.apache.pdfbox.pdmodel.graphics.PDXObject object) {
		COSBase base = object.getCOSStream().getDictionaryObject(COSName.SUBTYPE);
		return base instanceof COSName ? ((COSName) base).getName() : null;
	}

	@Override
    public String getSubtype() {
		return this.subtype;
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case S_MASK:
				return this.getSMask();
			case OPI:
				return this.getOPI();
			default:
				return super.getLinkedObjects(link);
		}
	}

    protected List<PDSMaskImage> getSMask() {
        try {
            COSStream cosStream = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) this.simplePDObject)
                    .getCOSStream();
            COSBase smaskDictionary = cosStream
                    .getDictionaryObject(COSName.SMASK);
            if (smaskDictionary instanceof COSDictionary) {
                PDXObject xObject = this.getXObject(smaskDictionary);
                if (xObject != null) {
					List<PDXObject> mask = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					mask.add(xObject);
					return Collections.unmodifiableList(mask);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Problems with obtaining SMask. " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private PDXObject getXObject(COSBase smaskDictionary) throws IOException {
        COSName name = ((COSDictionary) smaskDictionary)
                .getCOSName(COSName.NAME);
        String nameAsString = name != null ? name.getName() : null;
        PDResources resourcesLocal = null;
        if (this.simplePDObject instanceof PDFormXObject) {
        	resourcesLocal = ((PDFormXObject) this.simplePDObject).getResources();
        }
        org.apache.pdfbox.pdmodel.graphics.PDXObject pbObject =
				org.apache.pdfbox.pdmodel.graphics.PDXObject.createXObject(
						smaskDictionary, nameAsString, resourcesLocal);
		if (pbObject instanceof PDImageXObjectProxy) {
			return new PBoxPDSMaskImage((PDImageXObjectProxy) pbObject, document, flavour);
		} else {
			LOGGER.warn("SMask object is not an Image XObject");
			return null;
		}
    }

    public static PDXObject getTypedPDXObject(
            org.apache.pdfbox.pdmodel.graphics.PDXObject pbObject,
			PDInheritableResources extendedResources,
            PDDocument document,
            PDFAFlavour flavour) {
        if (pbObject instanceof PDFormXObject) {
			PDFormXObject object = (PDFormXObject) pbObject;
			PDInheritableResources resources = extendedResources
					.getExtendedResources(object.getResources());
			return new PBoxPDXForm(object, resources, document, flavour);
        } else if (pbObject instanceof PDImageXObjectProxy) {
            return new PBoxPDXImage((PDImageXObjectProxy) pbObject, document, flavour);
        } else if (pbObject instanceof PDPostScriptXObject) {
            return new PBoxPDXObject(pbObject, document, flavour);
        } else {
            return null;
        }
    }

	protected List<CosDict> getOPI() {
        return this.getLinkToDictionary(OPI);
    }

    protected List<CosDict> getLinkToDictionary(String key) {
        COSDictionary object = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) this.simplePDObject)
                .getCOSStream();
        COSBase item = object.getDictionaryObject(COSName.getPDFName(key));
        if (item instanceof COSDictionary) {
			List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosDict((COSDictionary) item, this.document, this.flavour));
			return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

}
