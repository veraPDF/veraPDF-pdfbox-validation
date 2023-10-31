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

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.pdlayer.PDObject;

import java.util.stream.Collectors;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDObject extends GenericModelObject implements PDObject {

	public static final int MAX_NUMBER_OF_ELEMENTS = 1;

	protected COSObjectable simplePDObject;
	protected PDDocument document;
	protected PDContentStream contentStream;
	protected PDFontLike pdFontLike;
	protected CMap cMap;
	private String id = null;

	protected PBoxPDObject(COSObjectable simplePDObject, final String type) {
		super(type);
		this.simplePDObject = simplePDObject;

		if (simplePDObject != null) {
			COSBase cosObject = simplePDObject.getCOSObject();
			if (cosObject != null) {
				COSObjectKey key = cosObject.getKey();
				id = key != null ? key.getNumber() + " " + key.getGeneration() + " obj " + this.getObjectType()
						: super.getID();
			}
		}
	}

	protected PBoxPDObject(PDDocument document, final String type) {
		super(type);
		this.document = document;
		if (document == null)
			return;

		COSDocument cosDocument = document.getDocument();
		if (cosDocument != null) {
			COSBase cosBase = cosDocument.getCOSObject();
			if (cosBase != null) {
				COSObjectKey key = cosBase.getKey();
				id = key != null ? key.getNumber() + " " + key.getGeneration() + " obj " + this.getObjectType()
						: super.getID();
			}
		}
	}

	protected PBoxPDObject(PDContentStream contentStream, final String type) {
		super(type);
		this.contentStream = contentStream;
		if (contentStream == null)
			return;

		COSStream cosStream = contentStream.getContentStream();
		if (cosStream != null) {
			COSBase cosBase = cosStream.getCOSObject();
			if (cosBase != null) {
				COSObjectKey key = cosBase.getKey();
				id = key != null ? key.getNumber() + " " + key.getGeneration() + " obj " + this.getObjectType()
						: super.getID();
			}
		}
	}

	protected PBoxPDObject(PDFontLike pdFontLike, final String type) {
		super(type);
		this.pdFontLike = pdFontLike;

		if (pdFontLike instanceof COSBase) {
			COSObjectKey key = ((COSBase) pdFontLike).getKey();
			id = key != null ? key.getNumber() + " " + key.getGeneration() + " obj " + this.getObjectType()
					: super.getID();
		}
	}

	protected PBoxPDObject(CMap cMap, COSStream cMapFile, final String type) {
		super(type);
		this.cMap = cMap;
		this.simplePDObject = cMapFile;

		if (this.simplePDObject != null) {
			COSObjectKey key = this.simplePDObject.getCOSObject().getKey();
			id = key != null ? key.getNumber() + " " + key.getGeneration() + " obj " + this.getObjectType()
					: super.getID();
		}
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getentries() {
		if (this.simplePDObject instanceof COSDictionary) {
			return ((COSDictionary) this.simplePDObject).keySet().stream()
					.map(COSName::getName)
					.collect(Collectors.joining("&"));
		}
		return "";
	}

	@Override
	public String getobjectKey() {
		return null;
	}
}
