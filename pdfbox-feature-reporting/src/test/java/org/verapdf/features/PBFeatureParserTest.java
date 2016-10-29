package org.verapdf.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

	private static FeaturesCollection collection;

	@BeforeClass
	public static void before() throws URISyntaxException, IOException, JAXBException {
		File pdf = new File(TestNodeGenerator.getSystemIndependentPath("/FR.pdf"));
		try (PDDocument document = PDDocument.load(pdf, false, true)) {
			FeaturesConfig config = FeaturesConfig.fromFeatureSet(EnumSet.allOf(FeatureObjectType.class));
			collection = PBFeatureParser.getFeaturesCollection(document, config);
		}
	}

	@Test
	public void objectsNumberTest() {
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.INFORMATION_DICTIONARY).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.METADATA).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.LOW_LEVEL_INFO).size());
		assertEquals(4, collection.getFeatureTreesForType(FeatureObjectType.EMBEDDED_FILE).size());
		assertEquals(5, collection.getFeatureTreesForType(FeatureObjectType.ICCPROFILE).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.OUTPUTINTENT).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.OUTLINES).size());
		assertEquals(7, collection.getFeatureTreesForType(FeatureObjectType.ANNOTATION).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.PAGE).size());
		assertEquals(4, collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES).size());
		assertEquals(2, collection.getFeatureTreesForType(FeatureObjectType.ERROR).size());
		assertEquals(2, collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT).size());
		assertEquals(1, collection.getFeatureTreesForType(FeatureObjectType.SHADING).size());
		assertEquals(2, collection.getFeatureTreesForType(FeatureObjectType.PATTERN).size());
		// TODO: return this when image colorspace obtaining will be fixed
		// assertEquals(34,
		// collection.getFeatureTreesForType(FeatureObjectType.COLORSPACE).size());
		// assertEquals(10,
		// collection.getFeatureTreesForType(FeatureObjectType.IMAGE_XOBJECT).size());
		assertEquals(12, collection.getFeatureTreesForType(FeatureObjectType.FORM_XOBJECT).size());
		assertEquals(0, collection.getFeatureTreesForType(FeatureObjectType.POSTSCRIPT_XOBJECT).size());
		assertEquals(7, collection.getFeatureTreesForType(FeatureObjectType.FONT).size());

	}

	@Test
	public void typeErrorsCheck() {
		for (FeatureObjectType type : FeatureObjectType.values()) {
			assertTrue(collection.getErrorsForType(type).isEmpty());
		}
	}

	@Test
	public void testInfoDict() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.INFORMATION_DICTIONARY);
		assertTrue(treeNodes.contains(TestNodeGenerator.getInfDictNode()));
	}

	@Test
	public void testMetadata() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.METADATA);
		assertTrue(treeNodes.contains(TestNodeGenerator.getMetadataNode()));
	}

	@Test
	public void testLowLvlInfo() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.LOW_LEVEL_INFO);
		assertTrue(treeNodes.contains(TestNodeGenerator.getLowLvlInfo()));
	}

	@Test
	public void testEmbeddedFiles() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.EMBEDDED_FILE);
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file1", "1.txt", "", "text/plain",
				"FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z",
				"D41D8CD98F00B204E9800998ECF8427E", "0")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file2", "Arist.jpg", "", "image/jpeg",
				"FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z",
				"F9803872918B24974BE596EA6C986D7D", "26862")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file3", "XMP - 8.xml", "", "text/xml",
				"FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z",
				"0605BCE41755770D87A93EF1380E6ED4", "876")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file4", "fox_1.jpg",
				"Some Description for embedded file", "image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00",
				"2014-09-08T12:01:07.000Z", "CBD3FE566607E760BA95E56B153F410D", "67142")));
	}

	@Test
	public void testICCProfiles() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.ICCPROFILE);
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir19", "2.1", "ADBE", "RGB ",
				"ADBE", "2000-08-11T19:52:24.000Z", "Perceptual", "Copyright 2000 Adobe Systems Incorporated",
				"Apple RGB", null, null, "none",
				TestNodeGenerator.getMetadataBytesFromFile("/iccprofile19_metadata_bytes.txt"))));
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir81", "2.1", "ADBE", "RGB ",
				"ADBE", "2000-08-11T19:54:18.000Z", "Perceptual", "Copyright 2000 Adobe Systems Incorporated",
				"PAL/SECAM", null, null, "none", null)));
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir84", "2.2", "appl", "RGB ",
				"appl", "2000-08-13T16:06:07.000Z", "Media-Relative Colorimetric",
				"Copyright 1998 - 2003 Apple Computer Inc., all rights reserved.", "sRGB Profile", null, null, "appl",
				null)));
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir85", "4.2", "ADBE", "RGB ",
				"ADBE", "2007-10-24T00:00:00.000Z", "Media-Relative Colorimetric",
				"Copyright 2007 Adobe Systems Incorporated", "HDTV (Rec. 709)",
				"t\u001C$ﾦ\u0012\u0017ﾉHQﾃ\uFFEFￋ￨<\uFFE7,", null, null, null)));
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir77", "2.1", "ADBE", "GRAY",
				"ADBE", "1999-06-03T00:00:00.000Z", "Perceptual", "Copyright 1999 Adobe Systems Incorporated",
				"Dot Gain 20%", null, null, "none", null)));
	}

	@Test
	public void testOutputIntent() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.OUTPUTINTENT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getOutputIntent()));
	}

	@Test
	public void testOutlines() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.OUTLINES);
		assertTrue(treeNodes.contains(TestNodeGenerator.getOutlines()));
	}

	@Test
	public void testAnnotations() throws FeatureParsingException {
		Set<String> xobj37 = new HashSet<>();
		xobj37.add("xobjIndir28");
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.ANNOTATION);
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir37", "Text", "368.092", "423.522",
				"386.092", "441.522", "Annotation with pop-up window", "d48d8e43-b22c-41ce-8cfa-28c1ca955d97",
				"D:20150822140440+03'00'", xobj37, "annotIndir38", "1.0", "1.0", "0.0", null, "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir38", "Popup", "370.08", "265.547",
				"550.081", "385.984", null, null, null, null, null, null, null, null, null, "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj13 = new HashSet<>();
		xobj13.add("xobjIndir22");
		xobj13.add("xobjIndir21");
		xobj13.add("xobjIndir23");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir13", "Widget", "112.562", "398.933",
				"161.251", "450.764", null, null, null, xobj13, null, null, null, null, null, "false", "false", "true",
				"false", "false", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir42", "Popup", "499.977", "350.004",
				"679.977", "470.004", null, null, null, null, null, null, null, null, null, "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir40", "Popup", "499.977", "322.78",
				"679.977", "442.78", null, null, null, null, null, null, null, null, null, "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj41 = new HashSet<>();
		xobj41.add("xobjIndir27");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir41", "Text", "338.339", "452.004",
				"356.339", "470.004", "annotation with CMYK colorspace\r", "a21bf4d8-e9fe-4e29-89a0-26e416fc8ca7",
				"D:20150831140530+03'00'", xobj41, "annotIndir42", "1.0", "0.0", "0.0", "0.0", "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj39 = new HashSet<>();
		xobj39.add("xobjIndir27");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir39", "Text", "307.974", "424.78",
				"325.974", "442.78", "annotation with gray colorspace\r", "85f36ad6-ae92-479e-9b24-ba07c8702837",
				"D:20150831140515+03'00'", xobj39, "annotIndir40", "1.0", null, null, null, "false", "false", "true",
				"true", "true", "false", "false", "false", "false", "false")));
	}

	@Test
	public void testPage() {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.PAGE);
		// TODO: return this when image colorspace obtaining will be fixed
		// assertTrue(treeNodes.contains(TestNodeGenerator.getPage()));
	}

	@Test
	public void testGraphicsState() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.EXT_G_STATE);
		assertTrue(treeNodes.contains(
				TestNodeGenerator.getGraphicsState("exGStIndir93", "true", "false", "false", "false", "fntIndir89")));
		assertTrue(treeNodes
				.contains(TestNodeGenerator.getGraphicsState("exGStDir4", "true", "false", "false", "false", null)));
		assertTrue(treeNodes
				.contains(TestNodeGenerator.getGraphicsState("exGStDir25", "true", "false", "false", "false", null)));
		assertTrue(treeNodes.contains(
				TestNodeGenerator.getGraphicsState("exGStIndir47", "false", "false", "false", "true", "fntIndir88")));
	}

	@Test
	public void testPropertiesDicts() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.PROPERTIES);
		assertTrue(treeNodes.contains(TestNodeGenerator.getProperties()));
	}

	@Test
	public void testFailedXObjects() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.FAILED_XOBJECT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getFailedXObject("xobjIndir53", "error0")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFailedXObject("xobjIndir54", "error1")));
	}

	@Test
	public void testShadings() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.SHADING);
		assertTrue(treeNodes.contains(TestNodeGenerator.getShading()));
	}

	@Test
	public void testPatterns() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.PATTERN);
		assertTrue(treeNodes.contains(TestNodeGenerator.getShadingPattern()));
		// TODO: return this when image colorspace obtaining will be fixed
		// assertTrue(treeNodes.contains(TestNodeGenerator.getTilingPattern()));
	}

	@Test
	public void testErrors() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.ERROR);
		assertTrue(treeNodes.contains(TestNodeGenerator.getErrorNode("error0", "Invalid XObject Subtype: Custom")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getErrorNode("error1",
				"Unexpected object type: org.apache.pdfbox.cos.COSDictionary")));
	}

	@Test
	public void testFonts() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeatureObjectType.FONT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont0()));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont1()));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont2()));
	}

	@Test
	public void testImageXObjects() {
		// TODO: Finish this
	}

	@Test
	public void testFormXObjects() {
		// TODO: Finish this
	}

	@Test
	public void testColorSpaces() {
		// TODO: Finish this
	}
}
