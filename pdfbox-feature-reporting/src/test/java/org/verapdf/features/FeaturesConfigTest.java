package org.verapdf.features;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.TestNodeGenerator;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

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
		testFeatureExclusion(FeatureObjectType.INFORMATION_DICTIONARY);
	}

	@Test
	public void metadataTest() {
		testFeatureExclusion(FeatureObjectType.METADATA);
	}

	@Test
	public void documentSecurityTest() {
		testFeatureExclusion(FeatureObjectType.DOCUMENT_SECURITY);
	}

	@Test
	public void signaturesTest() {
		testFeatureExclusion(FeatureObjectType.SIGNATURE);
	}

	@Test
	public void lowLevelInfoTest() {
		testFeatureExclusion(FeatureObjectType.LOW_LEVEL_INFO);
	}

	@Test
	public void embeddedFilesTest() {
		testFeatureExclusion(FeatureObjectType.EMBEDDED_FILE);
	}

	@Test
	public void iccProfilesTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.ICCPROFILE);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.ICCPROFILE).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.OUTPUTINTENT),
				"destOutputIntent"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"iccProfile"));
	}

	@Test
	public void outputIntentTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.OUTPUTINTENT);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.OUTPUTINTENT).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.ICCPROFILE),
				"parents", "outputIntent"));
	}

	@Test
	public void outlinesTest() {
		testFeatureExclusion(FeatureObjectType.OUTLINES);
	}

	@Test
	public void annotationsTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.ANNOTATION);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.ANNOTATION).isEmpty());
		assertNull(
				getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "annotations"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"parents", "annotation"));
	}

	@Test
	public void pagesTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.PAGE);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.PAGE).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.ANNOTATION),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN), "parents",
				"page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.SHADING), "parents",
				"page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.POSTSCRIPT_XOBJECT),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT),
				"parents", "page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "parents",
				"page"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES),
				"parents", "page"));
	}

	@Test
	public void graphicsStateTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.EXT_G_STATE);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"graphicsStates"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "graphicsStates"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"graphicsState"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "graphicsStates"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"graphicsStates"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "parents",
				"graphicsState"));
	}

	@Test
	public void colorSpaceTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.COLORSPACE);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.ICCPROFILE),
				"parents", "iccBased"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"colorSpaces"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "colorSpaces"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.SHADING),
				"colorSpace"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"group", "colorSpace"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "colorSpaces"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT),
				"colorSpace"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"colorSpaces"));
	}

	@Test
	public void patternsTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.PATTERN);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.PATTERN).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.SHADING), "parents",
				"pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.POSTSCRIPT_XOBJECT),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "parents",
				"pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES),
				"parents", "pattern"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"patterns"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "patterns"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"patterns"));
	}

	@Test
	public void shadingsTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.SHADING);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.SHADING).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"parents", "shading"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"shadings"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "shadings"));
		assertNull(
				getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN), "shading"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "shadings"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"shadings"));
	}

	@Test
	public void xobjectsTest() {
		EnumSet<FeatureObjectType> xobjs = EnumSet.of(FeatureObjectType.FAILED_XOBJECT, FeatureObjectType.FORM_XOBJECT,
				FeatureObjectType.IMAGE_XOBJECT, FeatureObjectType.POSTSCRIPT_XOBJECT);
		FeatureExtractorConfig config = configMissingFeatures(xobjs);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT).isEmpty());
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT).isEmpty());
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.POSTSCRIPT_XOBJECT).isEmpty());
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE),
				"parents", "xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"parents", "xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.SHADING), "parents",
				"xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN), "parents",
				"xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "parents",
				"xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES),
				"parents", "xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"xobjects"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.ANNOTATION),
				"resources", "xobject"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "xobjects"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"xobjects"));
	}

	@Test
	public void fontsTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.FONT);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.FONT).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.SHADING), "parents",
				"font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN), "parents",
				"font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.POSTSCRIPT_XOBJECT),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES),
				"parents", "font"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"fonts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE),
				"resources", "fonts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "fonts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "fonts"));
	}

	@Test
	public void propertiesDictionariesTest() {
		FeatureExtractorConfig config = configMissingFeature(FeatureObjectType.PROPERTIES);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, config);
		assertTrue(collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES).isEmpty());
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PAGE), "resources",
				"propertiesDicts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.PATTERN),
				"resources", "propertiesDicts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT),
				"resources", "propertiesDicts"));
		assertNull(getFirstNodeFromListWithPath(collection.getFeatureTreesForType(FeatureObjectType.FONT), "resources",
				"propertiesDicts"));
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

	public void testFeatureExclusion(FeatureObjectType type) {
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document, configMissingFeature(type));
		assertTrue(collection.getFeatureTreesForType(type).isEmpty());
	}

	private static FeatureExtractorConfig configMissingFeature(FeatureObjectType toExclude) {
		EnumSet<FeatureObjectType> testSet = EnumSet.allOf(FeatureObjectType.class);
		testSet.remove(toExclude);
		return FeatureExtractorConfigImpl.fromFeatureSet(testSet);
	}

	private static FeatureExtractorConfig configMissingFeatures(EnumSet<FeatureObjectType> toExclude) {
		EnumSet<FeatureObjectType> testSet = EnumSet.allOf(FeatureObjectType.class);
		for (FeatureObjectType type : toExclude) {
			testSet.remove(type);
		}
		return FeatureExtractorConfigImpl.fromFeatureSet(testSet);
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
