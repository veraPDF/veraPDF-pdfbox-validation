package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Maksim Bezrukov
 */
public class FeaturesConfigTest {

    private static PDDocument document;

    @BeforeClass
    public static void before() throws URISyntaxException, IOException {
        File pdf = new File(TestNodeGenerator.getSystemIndependentPath("/FR - DS.pdf"));
        document = PDDocument.load(pdf, false, true);
    }

    @Test
    public void infoDictionaryTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().informationDict(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).isEmpty());
    }

    @Test
    public void metadataTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().metadata(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).isEmpty());
    }

    @Test
    public void documentSecurityTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().documentSecurity(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.DOCUMENT_SECURITY).isEmpty());
    }

    @Test
    public void signaturesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().signatures(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SIGNATURE).isEmpty());
    }

    @Test
    public void lowLevelInfoTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().lowLevelInfo(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).isEmpty());
    }

    @Test
    public void embeddedFilesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().embeddedFiles(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).isEmpty());
    }

    @Test
    public void iccProfilesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().iccProfiles(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).isEmpty());

        for (FeatureTreeNode outInt : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT)) {
            FeatureTreeNode destOutputIntent = getFirstChildNodeWithName(outInt, "destOutputIntent");
            assertNull(destOutputIntent);
        }

        for (FeatureTreeNode colorSpace : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE)) {
            FeatureTreeNode iccProfile = getFirstChildNodeWithName(colorSpace, "iccProfile");
            assertNull(iccProfile);
        }
    }

    @Test
    public void outputIntentTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().outputIntents(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).isEmpty());

        for (FeatureTreeNode iccProfile : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(iccProfile, "parents");
            FeatureTreeNode outputIntentParent = getFirstChildNodeWithName(parents, "outputIntent");
            assertNull(outputIntentParent);
        }
    }

    @Test
    public void outlinesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().outlines(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).isEmpty());
    }

    @Test
    public void annotationsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().annotations(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).isEmpty());

        for (FeatureTreeNode page : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE)) {
            FeatureTreeNode annots = getFirstChildNodeWithName(page, "annotations");
            assertNull(annots);
        }

        for (FeatureTreeNode formXObject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(formXObject, "parents");
            FeatureTreeNode outputIntentParent = getFirstChildNodeWithName(parents, "annotation");
            assertNull(outputIntentParent);
        }
    }

    @Test
    public void pagesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().pages(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE).isEmpty());

        for (FeatureTreeNode annotations : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(annotations, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode exGState : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(exGState, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode colorSpace : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(colorSpace, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode pattern : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(pattern, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode shading : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(shading, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode xobject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(xobject, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode xobject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(xobject, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode xobject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(xobject, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode xobject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(xobject, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode font : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(font, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
        for (FeatureTreeNode property : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES)) {
            FeatureTreeNode parents = getFirstChildNodeWithName(property, "parents");
            FeatureTreeNode page = getFirstChildNodeWithName(parents, "page");
            assertNull(page);
        }
    }

    @Test
    public void graphicsStateTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().graphicsStates(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).isEmpty());

        for (FeatureTreeNode page : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE)) {
            FeatureTreeNode resources = getFirstChildNodeWithName(page, "resources");
            FeatureTreeNode gStates = getFirstChildNodeWithName(resources, "graphicsStates");
            assertNull(gStates);
        }
        for (FeatureTreeNode pattern : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN)) {
            FeatureTreeNode resources = getFirstChildNodeWithName(pattern, "resources");
            FeatureTreeNode gStates = getFirstChildNodeWithName(resources, "graphicsStates");
            assertNull(gStates);
            FeatureTreeNode gState = getFirstChildNodeWithName(pattern, "graphicsState");
            assertNull(gState);
        }
        for (FeatureTreeNode xobject : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT)) {
            FeatureTreeNode resources = getFirstChildNodeWithName(xobject, "resources");
            FeatureTreeNode gStates = getFirstChildNodeWithName(resources, "graphicsStates");
            assertNull(gStates);
        }
        for (FeatureTreeNode font : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT)) {
            FeatureTreeNode resources = getFirstChildNodeWithName(font, "resources");
            FeatureTreeNode gStates = getFirstChildNodeWithName(resources, "graphicsStates");
            assertNull(gStates);
            FeatureTreeNode parents = getFirstChildNodeWithName(font, "parents");
            FeatureTreeNode gs = getFirstChildNodeWithName(parents, "graphicsState");
            assertNull(gs);
        }
    }

    private FeaturesConfig.Builder createFullFeaturesConfigBuilder() {
        FeaturesConfig.Builder configBuilder = new FeaturesConfig.Builder();
        configBuilder.informationDict(true);
        configBuilder.metadata(true);
        configBuilder.documentSecurity(true);
        configBuilder.signatures(true);
        configBuilder.lowLevelInfo(true);
        configBuilder.embeddedFiles(true);
        configBuilder.iccProfiles(true);
        configBuilder.outputIntents(true);
        configBuilder.outlines(true);
        configBuilder.annotations(true);
        configBuilder.pages(true);
        configBuilder.graphicsStates(true);
        configBuilder.colorSpaces(true);
        configBuilder.patterns(true);
        configBuilder.shadings(true);
        configBuilder.xobjects(true);
        configBuilder.fonts(true);
        configBuilder.propertiesDicts(true);
        return configBuilder;
    }

    private static FeatureTreeNode getFirstChildNodeWithName(FeatureTreeNode node, String name) {
        if (node == null) {
            return null;
        }
        for (FeatureTreeNode child : node.getChildren()) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }
}
