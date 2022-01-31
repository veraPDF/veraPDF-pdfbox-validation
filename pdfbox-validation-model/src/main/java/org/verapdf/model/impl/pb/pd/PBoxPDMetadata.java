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

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
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

	public PBoxPDMetadata(org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject, Boolean isMainMetadata,
			PDDocument document, PDFAFlavour flavour) {
		super(simplePDObject, METADATA_TYPE);
		this.isMainMetadata = isMainMetadata.booleanValue();
		if (document != null && document.getDocumentCatalog() != null
				&& document.getDocumentCatalog().getMetadata() != null) {
			this.mainMetadata = document.getDocumentCatalog().getMetadata();
		} else {
			this.mainMetadata = null;
		}
		this.flavour = flavour;
	}

	@Override
	public String getFilter() {
		List<COSName> filters = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject).getFilters();
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
			COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject).getStream();
			if (stream != null) {
				VeraPDFMeta metadata = VeraPDFMeta.parse(stream.getUnfilteredStream());
				if (isMainMetadata) {
					xmp.add(new AXLMainXMPPackage(metadata, true, this.flavour));
				} else if (this.flavour == null || this.flavour.getPart() == null
						|| this.flavour.getPart().getPartNumber() != 1) {
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
			if (isMainMetadata) {
				xmp.add(new AXLMainXMPPackage(null, false, this.flavour));
			} else if (this.flavour == null || this.flavour.getPart() == null
					|| this.flavour.getPart().getPartNumber() != 1) {
				xmp.add(new AXLXMPPackage(null, false, null, this.flavour));
			}
		}
		return xmp;
	}

	private List<CosStream> getStream() {
		COSStream stream = ((org.apache.pdfbox.pdmodel.common.PDMetadata) this.simplePDObject).getStream();
		if (stream != null) {
			List<CosStream> streams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			streams.add(new PBCosStream(stream, this.document, this.flavour));
			return Collections.unmodifiableList(streams);
		}
		return Collections.emptyList();
	}

	public static boolean isMetadataObject(COSBase obj) {
		return obj instanceof COSStream
				&& ((COSStream) obj).getCOSName(COSName.TYPE) == COSName.METADATA
				&& ((COSStream) obj).getCOSName(COSName.SUBTYPE) == COSName.getPDFName("XML");
	}
}