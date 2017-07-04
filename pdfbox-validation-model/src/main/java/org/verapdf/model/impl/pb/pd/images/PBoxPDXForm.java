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
package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.impl.pb.pd.PBoxPDGroup;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDGroup;
import org.verapdf.model.pdlayer.PDXForm;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXForm extends PBoxPDXObject implements PDXForm {

	public static final String X_FORM_TYPE = "PDXForm";

	public static final String GROUP = "Group";
	public static final String PS = "PS";
	public static final String REF = "Ref";
	public static final String CONTENT_STREAM = "contentStream";

	private List<PDContentStream> contentStreams = null;
	private List<PDGroup> groups = null;
	private boolean groupContainsTransparency = false;
	private boolean contentStreamContainsTransparency = false;

	public PBoxPDXForm(PDFormXObject simplePDObject, PDInheritableResources resources, PDDocument document,
			PDFAFlavour flavour) {
		super(simplePDObject, resources, X_FORM_TYPE, document, flavour);
	}

	@Override
	public String getSubtype2() {
		String subType = null;
		try (final COSStream subtype2 = ((PDFormXObject) this.simplePDObject).getCOSStream()) {
			COSBase item = subtype2.getDictionaryObject(COSName.getPDFName("Subtype2"));
			if (item instanceof COSName)
				subType = ((COSName) item).getName();
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		return subType;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case GROUP:
			return this.getGroup();
		case PS:
			return this.getPS();
		case REF:
			return this.getREF();
		case CONTENT_STREAM:
			return this.getContentStream();
		default:
			return super.getLinkedObjects(link);
		}
	}

	private List<PDGroup> getGroup() {
		if (this.groups == null) {
			initializeGroups();
		}
		return this.groups;
	}

	private List<CosStream> getPS() {
		try (final COSStream cosStream = ((PDFormXObject) this.simplePDObject).getCOSStream();
				COSStream ps = (COSStream) cosStream.getDictionaryObject(COSName.PS)) {
			if (ps != null) {
				List<CosStream> postScript = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				postScript.add(new PBCosStream(ps, this.document, this.flavour));
				return Collections.unmodifiableList(postScript);
			}
		} catch (IOException excep) {
			// TODO Auto-generated catch block
			excep.printStackTrace();
		}
		return Collections.emptyList();

	}

	private List<CosDict> getREF() {
		return this.getLinkToDictionary(REF);
	}

	private List<PDContentStream> getContentStream() {
		if (this.contentStreams == null) {
			parseContentStream();
		}
		return this.contentStreams;
	}

	private void initializeGroups() {
		org.apache.pdfbox.pdmodel.graphics.form.PDGroup group = ((PDFormXObject) this.simplePDObject).getGroup();
		if (group != null) {
			this.groupContainsTransparency = COSName.TRANSPARENCY.equals(group.getSubType());
			List<PDGroup> groupsToAdd = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			groupsToAdd.add(new PBoxPDGroup(group));
			this.groups = Collections.unmodifiableList(groupsToAdd);
		} else {
			this.groups = Collections.emptyList();
		}
	}

	private void parseContentStream() {
		List<PDContentStream> streams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		PBoxPDContentStream pdContentStream = new PBoxPDContentStream((PDFormXObject) this.simplePDObject,
				this.resources, this.document, this.flavour);
		this.contentStreamContainsTransparency = pdContentStream.isContainsTransparency();
		streams.add(pdContentStream);
		this.contentStreams = streams;
	}

	/**
	 * @return true if current form object contains transparency group or
	 *         transparency in its content stream
	 */
	public boolean containsTransparency() {
		if (groups == null) {
			initializeGroups();
		}
		if (contentStreams == null) {
			parseContentStream();
		}

		return groupContainsTransparency || contentStreamContainsTransparency;
	}
}
