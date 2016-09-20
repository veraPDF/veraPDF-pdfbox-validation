package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.PDInheritableResource;
import org.verapdf.model.pdlayer.PDResource;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDResources extends PBoxPDObject implements PDResource {

	private boolean inherited;

	protected PBoxPDResources(COSObjectable simplePDObject, final String type) {
		super(simplePDObject, type);
		if (simplePDObject instanceof PDInheritableResource) {
			this.inherited = ((PDInheritableResource) simplePDObject).isInherited();
		}
	}

	protected PBoxPDResources(PDFontLike pdFontLike, final String type) {
		super(pdFontLike, type);
		this.inherited = pdFontLike.isInherited();
	}

	@Override
	public Boolean getisInherited() {
		return Boolean.valueOf(this.inherited);
	}

}
