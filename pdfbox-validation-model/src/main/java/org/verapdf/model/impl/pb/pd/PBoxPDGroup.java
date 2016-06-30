package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDGroup;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroup extends PBoxPDObject implements PDGroup {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDGroup.class);

	public static final String GROUP_TYPE = "PDGroup";

	public static final String COLOR_SPACE = "colorSpace";

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBoxPDGroup(
            org.apache.pdfbox.pdmodel.graphics.form.PDGroup simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, GROUP_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public String getS() {
        return ((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject)
                .getSubType().getName();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (COLOR_SPACE.equals(link)) {
            return this.getColorSpace();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getColorSpace() {
        try {
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject)
                    .getColorSpace();
            PDColorSpace colorSpace = ColorSpaceFactory
                    .getColorSpace(pbColorSpace, this.document, this.flavour);
            if (colorSpace != null) {
				List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(colorSpace);
				return Collections.unmodifiableList(colorSpaces);
            }
        } catch (IOException e) {
            LOGGER.debug(
                    "Problems with color space obtaining on group. "
                            + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
