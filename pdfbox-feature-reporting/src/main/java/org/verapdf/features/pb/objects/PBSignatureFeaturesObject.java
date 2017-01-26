/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.SignatureFeaturesData;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * @author Maksim Bezrukov
 */
public class PBSignatureFeaturesObject implements IFeaturesObject {

    private PDSignature signature;

    public PBSignatureFeaturesObject(PDSignature signature) {
        this.signature = signature;
    }

    @Override
    public FeatureObjectType getType() {
        return FeatureObjectType.SIGNATURE;
    }

    @Override
    public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {

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

            collection.addNewFeatureTree(FeatureObjectType.SIGNATURE, root);

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
