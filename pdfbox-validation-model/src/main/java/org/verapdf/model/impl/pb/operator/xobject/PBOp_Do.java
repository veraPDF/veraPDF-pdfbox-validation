package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;
import org.verapdf.model.operator.Op_Do;
import org.verapdf.model.pdlayer.PDXObject;
import org.verapdf.model.tools.resources.PDInheritableResources;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which paints the specified XObject
 *
 * @author Timur Kamalov
 */
public class PBOp_Do extends PBOperator implements Op_Do {

	/** Type name for {@code PBOp_Do} */
    public static final String OP_DO_TYPE = "Op_Do";

	/** Name of link to the XObject */
    public static final String X_OBJECT = "xObject";

	private List<PDXObject> xObjects = null;

	private final PDDocument document;
	private final PDFAFlavour flavour;

    private final org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject;
	private final PDInheritableResources resources;

    public PBOp_Do(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject,
			PDInheritableResources resources,
				   PDDocument document,
				   PDFAFlavour flavour) {
        super(arguments, OP_DO_TYPE);
        this.pbXObject = pbXObject;
		this.resources = resources;
		this.document = document;
		this.flavour = flavour;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (X_OBJECT.equals(link)) {
            return this.getXObject();
        }
        return super.getLinkedObjects(link);
    }

	public List<PDXObject> getXObject() {
		if (this.xObjects == null) {
			PDXObject typedPDXObject = PBoxPDXObject.getTypedPDXObject(
					this.pbXObject, this.resources, this.document, this.flavour);
			if (typedPDXObject != null) {
				List<PDXObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(typedPDXObject);
				this.xObjects = Collections.unmodifiableList(list);
			} else {
				this.xObjects = Collections.emptyList();
			}
		}
		return this.xObjects;
	}

}
