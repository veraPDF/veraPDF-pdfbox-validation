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
package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDTilingPattern;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDTilingPattern extends PBoxPDPattern implements
        PDTilingPattern {

    public static final String TILING_PATTERN_TYPE = "PDTilingPattern";

    public static final String CONTENT_STREAM = "contentStream";
	private final PDInheritableResources resources;

    private final PDDocument document;
    private final PDFAFlavour flavour;

	private List<PDContentStream> contentStreams = null;
	private boolean containsTransparency = false;

	public PBoxPDTilingPattern(
			org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern simplePDObject,
			PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, TILING_PATTERN_TYPE);
		this.resources = resources;
        this.document = document;
        this.flavour = flavour;
	}

	@Override
    public List<? extends Object> getLinkedObjects(String link) {

        if (CONTENT_STREAM.equals(link)) {
            return this.getContentStream();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDContentStream> getContentStream() {
        if (this.contentStreams == null) {
			parseContentStream();
		}
		return this.contentStreams;
    }

	/**
	 * @return true if content stream of the pattern contains transparency
	 */
	public boolean isContainsTransparency() {
		if (this.contentStreams == null) {
			parseContentStream();
		}
		return this.containsTransparency;
	}

	private void parseContentStream() {
		List<PDContentStream> contentStreams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		PBoxPDContentStream contentStream = new PBoxPDContentStream(
				(org.apache.pdfbox.contentstream.PDContentStream) this.simplePDObject, this.resources, this.document, this.flavour);
		this.containsTransparency |= contentStream.isContainsTransparency();
		contentStreams.add(contentStream);
		this.contentStreams = contentStreams;
	}
}
