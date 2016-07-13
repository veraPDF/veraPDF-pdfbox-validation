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
import java.util.List;

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
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), "destOutputIntent"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "iccProfile"));
    }

    @Test
    public void outputIntentTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().outputIntents(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE), "parents", "outputIntent"));
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
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "annotations"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "parents", "annotation"));
    }

    @Test
    public void pagesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().pages(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "parents", "page"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES), "parents", "page"));
    }

    @Test
    public void graphicsStateTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().graphicsStates(false).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "graphicsStates"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "graphicsStates"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "graphicsState"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "graphicsStates"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "graphicsStates"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "parents", "graphicsState"));
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

    private static FeatureTreeNode getFirstNodeFromListWithPath(List<FeatureTreeNode> list, String... names) {
        for (FeatureTreeNode root : list) {
            FeatureTreeNode result = root;
            for (String name : names) {
                result = getFirstChildNodeWithName(result, name);
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
