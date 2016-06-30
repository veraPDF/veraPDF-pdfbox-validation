package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.visitor.cos.pb.PBCosVisitor;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;

/**
 * Current class is representation of CosObject interface of abstract model.
 * This class is analogue of COSBase in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosObject extends GenericModelObject implements CosObject {

    /** Type name for PBCosObject */
    private static final Logger LOGGER = Logger.getLogger(PBCosObject.class);

	public static final int MAX_NUMBER_OF_ELEMENTS = 1;

	protected final COSBase baseObject;

    protected PBCosObject(final COSBase baseObject, final String type) {
		super(type);
        this.baseObject = baseObject;
    }

    /**
     * Transform object of pdf box to corresponding object of abstract model
     * implementation. For transforming using {@code PBCosVisitor}.
     *
     * @param base
     *            the base object that all objects in the PDF document will
     *            extend in pdf box
     * @return object of abstract model implementation, transformed from
     *         {@code base}
     */
    public static CosObject getFromValue(COSBase base, PDDocument document, PDFAFlavour flavour) {
        try {
            if (base != null) {
                PBCosVisitor visitor = PBCosVisitor.getInstance(document, flavour);
                if (base instanceof COSObject) {
                    return (CosObject) PBCosVisitor
                            .visitFromObject((COSObject) base, document, flavour);
                }
                return (CosObject) base.accept(visitor);
            }
        } catch (IOException e) {
            LOGGER.debug(
                    "Problems with wrapping pdfbox object \"" + base.toString()
                            + "\". " + e.getMessage(), e);
        }
        return null;
    }
}
