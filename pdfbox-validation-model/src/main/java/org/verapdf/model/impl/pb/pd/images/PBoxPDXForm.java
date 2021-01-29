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
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.model.baselayer.Object;
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
	public static final String CONTENT_STREAM = "contentStream";
	public static final String XFORM_TRANSPARENCY_GROUP = "xFormTransparencyGroup";
	public static final String PARENT_XFORM_TRANSPARENCY_GROUP = "parentXFormTransparencyGroup";

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
	public Boolean getcontainsPS() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.PS);
	}

	@Override
	public Boolean getcontainsRef() {
		COSBase pageObject = this.simplePDObject.getCOSObject();
		return pageObject != null && pageObject instanceof COSDictionary &&
				((COSDictionary) pageObject).containsKey(COSName.getPDFName("Ref"));
	}

	@Override
	public Boolean getisUniqueSemanticParent() {
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
		case GROUP:
			return this.getGroup();
		case CONTENT_STREAM:
			return this.getContentStream();
		case XFORM_TRANSPARENCY_GROUP:
			return Collections.emptyList();
		case PARENT_XFORM_TRANSPARENCY_GROUP:
			return Collections.emptyList();
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
			groupsToAdd.add(new PBoxPDGroup(group, this.document, this.flavour));
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
