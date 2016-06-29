package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDStructElem;
import org.verapdf.model.pdlayer.PDStructTreeRoot;
import org.verapdf.model.tools.TaggedPDFHelper;
import org.verapdf.model.tools.TaggedPDFRoleMapHelper;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Current class is representation of PDF`s logical structure facilities.
 * Implemented by Apache PDFBox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDStructTreeRoot extends PBoxPDObject implements PDStructTreeRoot {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDStructTreeRoot.class);

	/** Type name for {@code PBoxPDStructTreeRoot} */
	public static final String STRUCT_TREE_ROOT_TYPE = "PDStructTreeRoot";

	/** Link name for {@code K} key */
	public static final String CHILDREN = "K";

	private List<PDStructElem> children = null;

	private PDFAFlavour flavour;

	/**
	 * Default constructor
	 *
	 * @param treeRoot structure tree root implementation
	 */
	public PBoxPDStructTreeRoot(PDStructureTreeRoot treeRoot, PDFAFlavour flavour) {
		super(treeRoot, STRUCT_TREE_ROOT_TYPE);
		this.flavour = flavour;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (CHILDREN.equals(link)) {
			return this.getChildren();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDStructElem> getChildren() {
		if (this.children == null) {
			this.children = parseChildren();
		}
		return this.children;
	}

	private List<PDStructElem> parseChildren() {
		COSDictionary parent = ((PDStructureTreeRoot) this.simplePDObject).getCOSObject();
		return TaggedPDFHelper.getChildren(parent, new TaggedPDFRoleMapHelper(getRoleMap(), this.flavour), LOGGER);
	}

	private Map<String, String> getRoleMap() {
		Map<String, java.lang.Object> tempMap = ((PDStructureTreeRoot) this.simplePDObject).getRoleMap();
		Map<String, String> resMap = new HashMap<>();
		for (Map.Entry<String, java.lang.Object> entry : tempMap.entrySet()) {
			resMap.put(entry.getKey(), entry.getValue().toString());
		}
		return resMap;
	}

	@Override
	public String gettopLevelFirstElementStandartType() {
		if (this.children == null) {
			this.children = parseChildren();
		}

		if (!this.children.isEmpty()) {
			return this.children.get(0).getstandardType();
		} else {
			return null;
		}
	}
}
