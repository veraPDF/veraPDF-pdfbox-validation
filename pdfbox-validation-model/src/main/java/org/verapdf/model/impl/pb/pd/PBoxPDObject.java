package org.verapdf.model.impl.pb.pd;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDObject;

import java.util.List;

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
				id = key != null ?
						key.getGeneration() + " " + key.getNumber() + " obj " + this.getObjectType()
						: super.getID();
			}
		}
	}

	protected PBoxPDObject(PDDocument document, final String type) {
		super(type);
		this.document = document;

		if (document != null) {
			COSDocument cosDocument = document.getDocument();
			if (cosDocument != null) {
				COSBase cosBase = cosDocument.getCOSObject();
				if (cosBase != null) {
					COSObjectKey key = cosBase.getKey();
					id = key != null ?
							key.getGeneration() + " " + key.getNumber() + " obj " + this.getObjectType()
							: super.getID();
				}
			}
		}
	}

	protected PBoxPDObject(PDContentStream contentStream, final String type) {
		super(type);
		this.contentStream = contentStream;
		if (contentStream != null) {
			COSStream cosStream = contentStream.getContentStream();
			if (cosStream != null) {
				COSBase cosBase = cosStream.getCOSObject();
				if (cosBase != null) {
					COSObjectKey key = cosBase.getKey();
					id = key != null ?
							key.getGeneration() + " " + key.getNumber() + " obj " + this.getObjectType()
							: super.getID();
				}
			}
		}
	}

	protected PBoxPDObject(PDFontLike pdFontLike, final String type) {
		super(type);
		this.pdFontLike = pdFontLike;

		if (pdFontLike instanceof COSBase) {
			COSObjectKey key = ((COSBase) pdFontLike).getKey();
			id = key != null ?
					key.getGeneration() + " " + key.getNumber() + " obj " + this.getObjectType()
					: super.getID();
		}
	}

	protected PBoxPDObject(CMap cMap, COSStream cMapFile, final String type) {
		super(type);
		this.cMap = cMap;
		this.simplePDObject = cMapFile;

		if (this.simplePDObject != null) {
			COSObjectKey key = this.simplePDObject.getCOSObject().getKey();
			id = key != null ?
					key.getGeneration() + " " + key.getNumber() + " obj " + this.getObjectType()
					: super.getID();
		}
	}

    protected void addAction(List<PDAction> actions,
            org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer) {
        PDAction action = PBoxPDAction.getAction(buffer);
		if (action != null) {
			actions.add(action);
		}
    }

	@Override
	public String getID() {
		return this.id;
	}
}
