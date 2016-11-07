package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

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
	 * @return PROPERTIES instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PROPERTIES;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (properties != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
			if (id != null) {
				root.setAttribute("id", id);
			}

			COSBase type = properties.getDictionaryObject(COSName.TYPE);
			if (type instanceof COSName) {
				PBCreateNodeHelper.addNotEmptyNode("type", ((COSName) type).getName(), root);
			}

			collection.addNewFeatureTree(FeatureObjectType.PROPERTIES, root);
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
