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
package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDContentStream extends PBoxPDObject implements
        PDContentStream {

    private static final Logger LOGGER = Logger
            .getLogger(PBoxPDContentStream.class);

	public static final String CONTENT_STREAM_TYPE = "PDContentStream";

	public static final String OPERATORS = "operators";
	private static final String RESOURCES = "resources";

	private final PDInheritableResources resources;
	private List<Operator> operators = null;
	private boolean containsTransparency = false;

    private final PDDocument document;
    private final PDFAFlavour flavour;

	public PBoxPDContentStream(
			org.apache.pdfbox.contentstream.PDContentStream contentStream,
			PDInheritableResources resources, PDDocument document, PDFAFlavour flavour) {
		super(contentStream, CONTENT_STREAM_TYPE);
		this.resources = resources;
        this.document = document;
        this.flavour = flavour;
	}

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case OPERATORS:
				return this.getOperators();
			case RESOURCES:
				return this.getResources();
			default:
				return super.getLinkedObjects(link);
		}
    }

    private List<Operator> getOperators() {
		if (this.operators == null) {
			parseOperators();
		}
		return this.operators;
    }

	/**
	 * @return true if this content stream contains transparency
	 */
	public boolean isContainsTransparency() {
		if (this.operators == null) {
			parseOperators();
		}
		return containsTransparency;
	}

	private void parseOperators() {
		try {
			COSStream cStream = this.contentStream.getContentStream();
			if (cStream != null) {
				PDFStreamParser streamParser = new PDFStreamParser(
						cStream, true);
				streamParser.parse();
				OperatorFactory operatorFactory = new OperatorFactory();
				List<Operator> result = operatorFactory.operatorsFromTokens(
						streamParser.getTokens(),
						this.resources, this.document, this.flavour);

				this.containsTransparency = operatorFactory.isLastParsedContainsTransparency();
				this.operators = Collections.unmodifiableList(result);
			} else {
				this.operators = Collections.emptyList();
			}
		} catch (IOException e) {
			LOGGER.debug(
					"Error while parsing content stream. " + e.getMessage(), e);
			this.operators = Collections.emptyList();
		}
	}

	@Override
	public String getundefinedResourceNames() {
		return resources.getUndefinedResourceNames().stream()
				.map(COSName::getName)
				.collect(Collectors.joining(","));
	}

	@Override
	public String getinheritedResourceNames() {
		return resources.getInheritedResourceNames().stream()
				.map(COSName::getName)
				.collect(Collectors.joining(","));
	}

	private List<org.verapdf.model.pdlayer.PDResources> getResources() {
		List<org.verapdf.model.pdlayer.PDResources> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		PDResources resources = this.resources.getCurrentResources();
		if (resources != null) {
			result.add(new PBoxPDResources(resources));
		}
		return result;
	}
}
