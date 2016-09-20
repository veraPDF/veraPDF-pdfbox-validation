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
        FeaturesConfig config = createFullFeaturesConfigBuilder().informationDict(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).isEmpty());
    }

    @Test
    public void metadataTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().metadata(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).isEmpty());
    }

    @Test
    public void documentSecurityTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().documentSecurity(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.DOCUMENT_SECURITY).isEmpty());
    }

    @Test
    public void signaturesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().signatures(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SIGNATURE).isEmpty());
    }

    @Test
    public void lowLevelInfoTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().lowLevelInfo(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).isEmpty());
    }

    @Test
    public void embeddedFilesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().embeddedFiles(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).isEmpty());
    }

    @Test
    public void iccProfilesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().iccProfiles(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), "destOutputIntent"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "iccProfile"));
    }

    @Test
    public void outputIntentTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().outputIntents(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE), "parents", "outputIntent"));
    }

    @Test
    public void outlinesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().outlines(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).isEmpty());
    }

    @Test
    public void annotationsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().annotations(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "annotations"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "parents", "annotation"));
    }

    @Test
    public void pagesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().pages(Boolean.FALSE).build();
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
        FeaturesConfig config = createFullFeaturesConfigBuilder().graphicsStates(Boolean.FALSE).build();
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

    @Test
    public void colorSpaceTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().colorSpaces(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE), "parents", "iccBased"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "colorSpaces"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "colorSpaces"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), "colorSpace"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "group", "colorSpace"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "colorSpaces"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT), "colorSpace"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "colorSpaces"));
    }

    @Test
    public void patternsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().patterns(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES), "parents", "pattern"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "patterns"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "patterns"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "patterns"));
    }

    @Test
    public void shadingsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().shadings(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "parents", "shading"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "shadings"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "shadings"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "shading"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "shadings"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "shadings"));
    }
    @Test
    public void xobjectsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().xobjects(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT).isEmpty());
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT).isEmpty());
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT).isEmpty());
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES), "parents", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "xobjects"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION), "resources", "xobject"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "xobjects"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "xobjects"));
    }

    @Test
    public void fontsTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().fonts(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES), "parents", "font"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "fonts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), "resources", "fonts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "fonts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "fonts"));
    }

    @Test
    public void propertiesDictionariesTest() {
        FeaturesConfig config = createFullFeaturesConfigBuilder().propertiesDicts(Boolean.FALSE).build();
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES).isEmpty());
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), "resources", "propertiesDicts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), "resources", "propertiesDicts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), "resources", "propertiesDicts"));
        assertNull(getFirstNodeFromListWithPath(
                collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), "resources", "propertiesDicts"));
    }

    private static FeaturesConfig.Builder createFullFeaturesConfigBuilder() {
        FeaturesConfig.Builder configBuilder = new FeaturesConfig.Builder();
        configBuilder.informationDict(Boolean.TRUE);
        configBuilder.metadata(Boolean.TRUE);
        configBuilder.documentSecurity(Boolean.TRUE);
        configBuilder.signatures(Boolean.TRUE);
        configBuilder.lowLevelInfo(Boolean.TRUE);
        configBuilder.embeddedFiles(Boolean.TRUE);
        configBuilder.iccProfiles(Boolean.TRUE);
        configBuilder.outputIntents(Boolean.TRUE);
        configBuilder.outlines(Boolean.TRUE);
        configBuilder.annotations(Boolean.TRUE);
        configBuilder.pages(Boolean.TRUE);
        configBuilder.graphicsStates(Boolean.TRUE);
        configBuilder.colorSpaces(Boolean.TRUE);
        configBuilder.patterns(Boolean.TRUE);
        configBuilder.shadings(Boolean.TRUE);
        configBuilder.xobjects(Boolean.TRUE);
        configBuilder.fonts(Boolean.TRUE);
        configBuilder.propertiesDicts(Boolean.TRUE);
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
