package org.verapdf.features.pb.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Features object for postscript xobject
 *
 * @author Maksim Bezrukov
 */
public class PBPostScriptXObjectFeaturesObject implements IFeaturesObject {

	private String id;

	/**
	 * Constructs new tilling pattern features object
	 *
	 * @param id            id of the object
	 */
	public PBPostScriptXObjectFeaturesObject(String id) {
		this.id = id;
	}

	/**
	 * @return POSTSCRIPT_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
		root.setAttribute("type", "postscript");
		if (id != null) {
			root.setAttribute("id", id);
		}

		collection.addNewFeatureTree(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT, root);
		return root;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

}
