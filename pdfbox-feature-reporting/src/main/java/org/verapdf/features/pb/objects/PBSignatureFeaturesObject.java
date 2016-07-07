package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.SignatureFeaturesData;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Maksim Bezrukov
 */
public class PBSignatureFeaturesObject implements IFeaturesObject {

    private PDSignature signature;

    public PBSignatureFeaturesObject(PDSignature signature) {
        this.signature = signature;
    }

    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.SIGNATURE;
    }

    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {

        if (signature != null) {
            FeatureTreeNode root = FeatureTreeNode.createRootNode("signature");

            PBCreateNodeHelper.addNotEmptyNode("filter", signature.getFilter(), root);
            PBCreateNodeHelper.addNotEmptyNode("subFilter", signature.getSubFilter(), root);

            byte[] contents = signature.getContents();
            if (contents != null) {
                PBCreateNodeHelper.addNotEmptyNode("contents", DatatypeConverter.printHexBinary(contents), root);
            }

            PBCreateNodeHelper.addNotEmptyNode("name", signature.getName(), root);
            PBCreateNodeHelper.createDateNode("signDate", root, signature.getSignDate(), collection);
            PBCreateNodeHelper.addNotEmptyNode("location", signature.getLocation(), root);
            PBCreateNodeHelper.addNotEmptyNode("reason", signature.getReason(), root);
            PBCreateNodeHelper.addNotEmptyNode("contactInfo", signature.getContactInfo(), root);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.SIGNATURE, root);

            return root;
        }
        return null;
    }

    @Override
    public FeaturesData getData() {
        byte[] contents = signature.getContents();
        InputStream stream = contents == null ? null : new ByteArrayInputStream(contents);
        return SignatureFeaturesData.newInstance(
                stream, signature.getFilter(),
                signature.getSubFilter(), signature.getName(),
                signature.getSignDate(), signature.getLocation(),
                signature.getReason(), signature.getContactInfo());
    }
}
