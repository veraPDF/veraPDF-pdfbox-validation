package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDShading;
import org.verapdf.model.pdlayer.PDShadingPattern;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShadingPattern extends PBoxPDPattern implements
        PDShadingPattern {

    private static final Logger LOGGER = Logger
            .getLogger(PBoxPDShadingPattern.class);

	public static final String SHADING_PATTERN_TYPE = "PDShadingPattern";

    public static final String SHADING = "shading";

    private final PDDocument document;
    private final PDFAFlavour flavour;

	public PBoxPDShadingPattern(
            org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern simplePDObject, PDDocument document, PDFAFlavour flavour) {
        super(simplePDObject, SHADING_PATTERN_TYPE);
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
		if (SHADING.equals(link)) {
			return this.getShading();
		}
		return super.getLinkedObjects(link);
    }

    private List<PDShading> getShading() {
        try {
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading shading =
					((org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern) this.simplePDObject)
                    .getShading();
            if (shading != null) {
				List<PDShading> shadings =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				shadings.add(new PBoxPDShading(shading, this.document, this.flavour));
				return Collections.unmodifiableList(shadings);
            }
        } catch (IOException e) {
            LOGGER.error("Can`t get shading pattern. " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
