package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Features object for shading
 *
 * @author Maksim Bezrukov
 */
public class PBShadingFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDShading shading;
	private String id;
	private String colorSpaceChild;

	/**
	 * Constructs new shading features object
	 *
	 * @param shading         PDShading which represents shading for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this shading
	 */
	public PBShadingFeaturesObject(PDShading shading, String id, String colorSpaceChild) {
		this.shading = shading;
		this.id = id;
		this.colorSpaceChild = colorSpaceChild;
	}

	/**
	 * @return SHADING instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.SHADING;
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
		if (shading != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("shading");
			if (id != null) {
				root.setAttribute(ID, id);
			}

			root.addChild("shadingType").setValue(String.valueOf(shading.getShadingType()));

			if (colorSpaceChild != null) {
				FeatureTreeNode shadingClr = root.addChild("colorSpace");
				shadingClr.setAttribute(ID, colorSpaceChild);
			}

			PBCreateNodeHelper.addBoxFeature("bbox", shading.getBBox(), root);

			root.addChild("antiAlias").setValue(String.valueOf(shading.getAntiAlias()));

			collection.addNewFeatureTree(FeatureObjectType.SHADING, root);
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
