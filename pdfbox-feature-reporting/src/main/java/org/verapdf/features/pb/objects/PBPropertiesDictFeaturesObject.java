package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * @author Maksim Bezrukov
 */
public class PBPropertiesDictFeaturesObject implements IFeaturesObject {

	private COSDictionary properties;
	private String id;

	/**
	 * Constructs new propertiesDict features object
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 */
	public PBPropertiesDictFeaturesObject(COSDictionary properties, String id) {
		this.properties = properties;
		this.id = id;
	}

	/**
	 * @return PROPERTIES instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.PROPERTIES;
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
		if (properties != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
			if (id != null) {
				root.setAttribute("id", id);
			}

			COSBase type = properties.getDictionaryObject(COSName.TYPE);
			if (type instanceof COSName) {
				PBCreateNodeHelper.addNotEmptyNode("type", ((COSName) type).getName(), root);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.PROPERTIES, root);
			return root;
		}

		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

}
