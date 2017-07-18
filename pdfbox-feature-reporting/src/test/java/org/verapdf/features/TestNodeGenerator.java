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
package org.verapdf.features;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.tools.FeatureTreeNode;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public final class TestNodeGenerator {

	public static final String ENTRY = "entry";
	public static final String ID = "id";
	public static final String METADATA = "metadata";
	public static final String LLX = "llx";
	public static final String LLY = "lly";
	public static final String URX = "urx";
	public static final String URY = "ury";

	public static FeatureTreeNode getFont0() throws FeatureParsingException {
		FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
		font.setAttribute("id", "fntIndir90");
		FeatureTreeNode type = font.addChild("type");
		type.setValue("Type1");
		FeatureTreeNode baseFont = font.addChild("baseFont");
		baseFont.setValue("OLXYQW+MyriadPro-Regular");
		FeatureTreeNode firstChar = font.addChild("firstChar");
		firstChar.setValue("32");
		FeatureTreeNode lastChar = font.addChild("lastChar");
		lastChar.setValue("121");
		FeatureTreeNode encoding = font.addChild("encoding");
		encoding.setValue("WinAnsiEncoding");
		FeatureTreeNode fontDescriptor = font.addChild("fontDescriptor");
		FeatureTreeNode fontName = fontDescriptor.addChild("fontName");
		fontName.setValue("MyriadPro-Regular");
		FeatureTreeNode subset = fontDescriptor.addChild("subset");
		subset.setValue("true");
		FeatureTreeNode fontFamily = fontDescriptor.addChild("fontFamily");
		fontFamily.setValue("Myriad Pro");
		FeatureTreeNode fontStretch = fontDescriptor.addChild("fontStretch");
		fontStretch.setValue("Normal");
		FeatureTreeNode fontWeight = fontDescriptor.addChild("fontWeight");
		fontWeight.setValue("400.000");
		FeatureTreeNode fixedPitch = fontDescriptor.addChild("fixedPitch");
		fixedPitch.setValue("false");
		FeatureTreeNode serif = fontDescriptor.addChild("serif");
		serif.setValue("false");
		FeatureTreeNode symbolic = fontDescriptor.addChild("symbolic");
		symbolic.setValue("false");
		FeatureTreeNode script = fontDescriptor.addChild("script");
		script.setValue("false");
		FeatureTreeNode nonsymbolic = fontDescriptor.addChild("nonsymbolic");
		nonsymbolic.setValue("true");
		FeatureTreeNode italic = fontDescriptor.addChild("italic");
		italic.setValue("false");
		FeatureTreeNode allCap = fontDescriptor.addChild("allCap");
		allCap.setValue("false");
		FeatureTreeNode smallCap = fontDescriptor.addChild("smallCap");
		smallCap.setValue("false");
		FeatureTreeNode forceBold = fontDescriptor.addChild("forceBold");
		forceBold.setValue("false");
		FeatureTreeNode fontBBox = fontDescriptor.addChild("fontBBox");
		fontBBox.setAttribute("lly", "-250.000");
		fontBBox.setAttribute("llx", "-157.000");
		fontBBox.setAttribute("urx", "1126.000");
		fontBBox.setAttribute("ury", "952.000");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.000");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("952.000");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-250.000");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.000");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("674.000");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("484.000");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("88.000");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.000");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.000");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("0.000");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("0.000");
		FeatureTreeNode charSet = fontDescriptor.addChild("charSet");
		charSet.setValue("/space/one/E/T/b/d/e/f/m/n/o/p/t/y");
		FeatureTreeNode embedded = fontDescriptor.addChild("embedded");
		embedded.setValue("true");
		return font;
	}

	public static FeatureTreeNode getFont1() throws FeatureParsingException {
		FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
		font.setAttribute("id", "fntDir32");
		FeatureTreeNode type = font.addChild("type");
		type.setValue("CIDFontType2");
		FeatureTreeNode baseFont = font.addChild("baseFont");
		baseFont.setValue("CBTOEA+ArialMT");
		FeatureTreeNode defaultWidth = font.addChild("defaultWidth");
		defaultWidth.setValue("750");
		FeatureTreeNode cidSystemInfo = font.addChild("cidSystemInfo");
		FeatureTreeNode registry = cidSystemInfo.addChild("registry");
		registry.setValue("Adobe");
		FeatureTreeNode ordering = cidSystemInfo.addChild("ordering");
		ordering.setValue("Identity");
		FeatureTreeNode supplement = cidSystemInfo.addChild("supplement");
		supplement.setValue("0");
		FeatureTreeNode fontDescriptor = font.addChild("fontDescriptor");
		FeatureTreeNode fontName = fontDescriptor.addChild("fontName");
		fontName.setValue("Arial");
		FeatureTreeNode subset = fontDescriptor.addChild("subset");
		subset.setValue("false");
		FeatureTreeNode fixedPitch = fontDescriptor.addChild("fixedPitch");
		fixedPitch.setValue("false");
		FeatureTreeNode serif = fontDescriptor.addChild("serif");
		serif.setValue("false");
		FeatureTreeNode symbolic = fontDescriptor.addChild("symbolic");
		symbolic.setValue("false");
		FeatureTreeNode script = fontDescriptor.addChild("script");
		script.setValue("false");
		FeatureTreeNode nonsymbolic = fontDescriptor.addChild("nonsymbolic");
		nonsymbolic.setValue("true");
		FeatureTreeNode italic = fontDescriptor.addChild("italic");
		italic.setValue("false");
		FeatureTreeNode allCap = fontDescriptor.addChild("allCap");
		allCap.setValue("false");
		FeatureTreeNode smallCap = fontDescriptor.addChild("smallCap");
		smallCap.setValue("false");
		FeatureTreeNode forceBold = fontDescriptor.addChild("forceBold");
		forceBold.setValue("false");
		FeatureTreeNode fontBBox = fontDescriptor.addChild("fontBBox");
		fontBBox.setAttribute("lly", "-325.000");
		fontBBox.setAttribute("llx", "-665.000");
		fontBBox.setAttribute("urx", "2000.000");
		fontBBox.setAttribute("ury", "1006.000");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.000");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("728.000");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-210.000");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.000");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("677.000");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("480.000");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("88.000");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.000");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.000");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("0.000");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("0.000");
		FeatureTreeNode embedded = fontDescriptor.addChild("embedded");
		embedded.setValue("true");
		return font;
	}

	public static FeatureTreeNode getFont2() throws FeatureParsingException {
		FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
		font.setAttribute("id", "fntDir64");
		FeatureTreeNode type = font.addChild("type");
		type.setValue("CIDFontType0");
		FeatureTreeNode baseFont = font.addChild("baseFont");
		baseFont.setValue("IIWNIN+AdobeFanHeitiStd-Bold");
		FeatureTreeNode defaultWidth = font.addChild("defaultWidth");
		defaultWidth.setValue("1000");
		FeatureTreeNode cidSystemInfo = font.addChild("cidSystemInfo");
		FeatureTreeNode registry = cidSystemInfo.addChild("registry");
		registry.setValue("Adobe");
		FeatureTreeNode ordering = cidSystemInfo.addChild("ordering");
		ordering.setValue("CNS1");
		FeatureTreeNode supplement = cidSystemInfo.addChild("supplement");
		supplement.setValue("6");
		FeatureTreeNode fontDescriptor = font.addChild("fontDescriptor");
		FeatureTreeNode fontName = fontDescriptor.addChild("fontName");
		fontName.setValue("AdobeFanHeitiStd-Bold");
		FeatureTreeNode subset = fontDescriptor.addChild("subset");
		subset.setValue("true");
		FeatureTreeNode fontFamily = fontDescriptor.addChild("fontFamily");
		fontFamily.setValue("Adobe Fan Heiti Std B");
		FeatureTreeNode fontStretch = fontDescriptor.addChild("fontStretch");
		fontStretch.setValue("Normal");
		FeatureTreeNode fontWeight = fontDescriptor.addChild("fontWeight");
		fontWeight.setValue("600.000");
		FeatureTreeNode fixedPitch = fontDescriptor.addChild("fixedPitch");
		fixedPitch.setValue("false");
		FeatureTreeNode serif = fontDescriptor.addChild("serif");
		serif.setValue("false");
		FeatureTreeNode symbolic = fontDescriptor.addChild("symbolic");
		symbolic.setValue("true");
		FeatureTreeNode script = fontDescriptor.addChild("script");
		script.setValue("false");
		FeatureTreeNode nonsymbolic = fontDescriptor.addChild("nonsymbolic");
		nonsymbolic.setValue("false");
		FeatureTreeNode italic = fontDescriptor.addChild("italic");
		italic.setValue("false");
		FeatureTreeNode allCap = fontDescriptor.addChild("allCap");
		allCap.setValue("false");
		FeatureTreeNode smallCap = fontDescriptor.addChild("smallCap");
		smallCap.setValue("false");
		FeatureTreeNode forceBold = fontDescriptor.addChild("forceBold");
		forceBold.setValue("false");
		FeatureTreeNode fontBBox = fontDescriptor.addChild("fontBBox");
		fontBBox.setAttribute("lly", "-283.000");
		fontBBox.setAttribute("llx", "-163.000");
		fontBBox.setAttribute("urx", "1087.000");
		fontBBox.setAttribute("ury", "967.000");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.000");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("967.000");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-283.000");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.000");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("766.000");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("551.000");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("116.000");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.000");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.000");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("1000.000");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("800.000");
		FeatureTreeNode embedded = fontDescriptor.addChild("embedded");
		embedded.setValue("true");

		return font;
	}

	public static FeatureTreeNode getErrorNode(String id, String value) throws FeatureParsingException {
		FeatureTreeNode res = FeatureTreeNode.createRootNode("error");
		res.setValue(value);
		res.setAttribute(ID, id);
		return res;
	}

	public static FeatureTreeNode getTilingPattern() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
		root.setAttribute(ID, "ptrnIndir49");
		root.setAttribute("type", "tiling");
		root.addChild("paintType").setValue("2");
		root.addChild("tilingType").setValue("1");
		FeatureTreeNode bbox = root.addChild("bbox");
		bbox.setAttribute(LLX, "0.000");
		bbox.setAttribute(LLY, "0.000");
		bbox.setAttribute(URX, "5.000");
		bbox.setAttribute(URY, "10.000");
		root.addChild("xStep").setValue("5.000");
		root.addChild("yStep").setValue("10.000");
		getStandartMatrix(root);
		FeatureTreeNode resources = root.addChild("resources");
		FeatureTreeNode colorSpaces = resources.addChild("colorSpaces");
		FeatureTreeNode clr2 = colorSpaces.addChild("colorSpace");
		clr2.setAttribute(ID, "clrspDir19");
		FeatureTreeNode clr1 = colorSpaces.addChild("colorSpace");
		clr1.setAttribute(ID, "clrspDir20");
		FeatureTreeNode xobjects = resources.addChild("xobjects");
		FeatureTreeNode xobj2 = xobjects.addChild("xobject");
		xobj2.setAttribute(ID, "xobjIndir60");
		FeatureTreeNode xobj1 = xobjects.addChild("xobject");
		xobj1.setAttribute(ID, "xobjIndir56");
		return root;
	}

	public static FeatureTreeNode getShadingPattern() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
		root.setAttribute(ID, "ptrnIndir50");
		root.setAttribute("type", "shading");
		FeatureTreeNode shading = root.addChild("shading");
		shading.setAttribute(ID, "shdngIndir52");
		getStandartMatrix(root);
		FeatureTreeNode gst = root.addChild("graphicsState");
		gst.setAttribute(ID, "exGStIndir93");
		return root;
	}

	public static void getStandartMatrix(FeatureTreeNode root) throws FeatureParsingException {
		FeatureTreeNode matr = root.addChild("matrix");
		addElement("1", "1.000", matr);
		addElement("2", "0.000", matr);
		addElement("3", "0.000", matr);
		addElement("4", "1.000", matr);
		addElement("5", "0.000", matr);
		addElement("6", "0.000", matr);
	}

	public static void addElement(String index, String value, FeatureTreeNode parent)
			throws FeatureParsingException {
		FeatureTreeNode element = parent.addChild("element");
		element.setAttribute("index", index);
		element.setAttribute("value", value);
	}

	public static FeatureTreeNode getShading() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("shading");
		root.setAttribute(ID, "shdngIndir52");
		root.addChild("shadingType").setValue("2");
		FeatureTreeNode clr = root.addChild("colorSpace");
		clr.setAttribute(ID, "devrgb");
		FeatureTreeNode bbox = root.addChild("bbox");;
		bbox.setAttribute(LLX, "0.000");
		bbox.setAttribute(LLY, "0.000");
		bbox.setAttribute(URX, "400.000");
		bbox.setAttribute(URY, "400.000");
		root.addChild("antiAlias").setValue("false");
		return root;
	}

	public static FeatureTreeNode getFailedXObject(String id, String errorid) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
		root.setAttribute(ID, id);
		root.setAttribute("errorId", errorid);
		root.setAttribute("type", "form");
		return root;
	}

	public static FeatureTreeNode getProperties() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
		root.setAttribute(ID, "propDir84");
		return root;
	}

	public static FeatureTreeNode getGraphicsState(String id, String transparency, String strokeAdjustment,
			String overprintForStroke, String overprintForFill, String fontChild) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("graphicsState");
		root.setAttribute(ID, id);
		root.addChild("transparency").setValue(transparency);
		root.addChild("strokeAdjustment").setValue(strokeAdjustment);
		root.addChild("overprintForStroke").setValue(overprintForStroke);
		root.addChild("overprintForFill").setValue(overprintForFill);

		if (fontChild != null) {
			FeatureTreeNode res = root.addChild("resources");
			FeatureTreeNode fons = res.addChild("fonts");
			FeatureTreeNode fon = fons.addChild("font");
			fon.setAttribute(ID, fontChild);
		}
		return root;
	}

	public static FeatureTreeNode getPage() throws FeatureParsingException, URISyntaxException, FileNotFoundException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("page");
		root.setAttribute(ID, "page1");
		root.setAttribute("orderNumber", "1");

		addBox("mediaBox", root);
		addBox("cropBox", root);
		addBox("trimBox", root);
		addBox("bleedBox", root);
		addBox("artBox", root);
		root.addChild("rotation").setValue("0");
		root.addChild("scaling").setValue("75.000");
		FeatureTreeNode thumb = root.addChild("thumbnail");
		thumb.setAttribute(ID, "xobjIndir126");
		root.addMetadataChild("metadata")
				.setValue(DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/page1_metadata_bytes.txt")));

		List<String> annotations = new ArrayList<>();
		annotations.add("annotIndir13");
		annotations.add("annotIndir42");
		annotations.add("annotIndir37");
		annotations.add("annotIndir38");
		annotations.add("annotIndir39");
		annotations.add("annotIndir41");
		annotations.add("annotIndir40");
		makeList("annotation", annotations, root);

		FeatureTreeNode resources = root.addChild("resources");

		List<String> graphicsStates = new ArrayList<>();
		graphicsStates.add("exGStIndir47");
		makeList("graphicsState", graphicsStates, resources);

		List<String> colorSpaces = new ArrayList<>();

		colorSpaces.add("clrspDir10");
		colorSpaces.add("clrspDir11");
		colorSpaces.add("devcmyk");
		colorSpaces.add("clrspDir18");
		colorSpaces.add("clrspDir4");
		colorSpaces.add("clrspDir5");
		colorSpaces.add("clrspDir6");
		colorSpaces.add("clrspDir17");
		colorSpaces.add("clrspDir14");
		colorSpaces.add("clrspDir15");
		colorSpaces.add("clrspDir12");
		colorSpaces.add("clrspDir2");
		colorSpaces.add("devgray");
		colorSpaces.add("clrspDir13");
		colorSpaces.add("devrgb");
		colorSpaces.add("clrspDir7");
		colorSpaces.add("clrspDir9");

		makeList("colorSpace", colorSpaces, resources);

		List<String> patterns = new ArrayList<>();
		patterns.add("ptrnIndir49");
		patterns.add("ptrnIndir50");
		makeList("pattern", patterns, resources);

		List<String> shadings = new ArrayList<>();
		shadings.add("shdngIndir52");
		makeList("shading", shadings, resources);

		List<String> xobject = new ArrayList<>();
		xobject.add("xobjIndir60");
		xobject.add("xobjIndir58");
		xobject.add("xobjIndir59");
		xobject.add("xobjIndir62");
		xobject.add("xobjIndir56");
		xobject.add("xobjIndir61");
		xobject.add("xobjIndir57");
		xobject.add("xobjIndir63");
		xobject.add("xobjIndir55");
		makeList("xobject", xobject, resources);

		List<String> fonts = new ArrayList<>();
		fonts.add("fntIndir90");
		fonts.add("fntIndir91");
		fonts.add("fntIndir92");
		fonts.add("fntIndir88");
		fonts.add("fntIndir89");
		makeList("font", fonts, resources);

		List<String> propertiesDicts = new ArrayList<>();
		propertiesDicts.add("propDir0");
		makeList("propertiesDict", propertiesDicts, resources);

		return root;
	}

	public static void makeList(String name, List<String> values, FeatureTreeNode parent)
			throws FeatureParsingException {
		FeatureTreeNode head = parent.addChild(name + "s");
		for (String el : values) {
			FeatureTreeNode element = head.addChild(name);
			element.setAttribute(ID, el);
		}
	}

	public static void addBox(String name, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode box = parent.addChild(name);
		box.setAttribute(LLX, "0.000");
		box.setAttribute(LLY, "0.000");
		box.setAttribute(URX, "499.977");
		box.setAttribute(URY, "499.977");
	}

	public static FeatureTreeNode getAnnotation(String id, String subtype, String width, String height, String llx, String lly, String urx,
			String ury, String contents, String annotationName, String modifiedDate, Set<String> xobj, String popup,
			String red, String green, String blue, String kayan, String invisible, String hidden, String print,
			String noZoom, String noRotate, String noView, String readOnly, String locked, String toggleNoView,
			String lockedContents) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("annotation");
		root.setAttribute(ID, id);
		addNotEmptyNode("subType", subtype, root);
		root.addChild("width").setValue(width);
		root.addChild("height").setValue(height);
		FeatureTreeNode rec = root.addChild("rectangle");
		rec.setAttribute(LLX, llx);
		rec.setAttribute(LLY, lly);
		rec.setAttribute(URX, urx);
		rec.setAttribute(URY, ury);
		addNotEmptyNode("contents", contents, root);
		addNotEmptyNode("annotationName", annotationName, root);
		addNotEmptyNode("modifiedDate", modifiedDate, root);

		if (xobj != null && !xobj.isEmpty()) {
			FeatureTreeNode resources = root.addChild("resources");
			for (String objID : xobj) {
				FeatureTreeNode node = resources.addChild("xobject");
				node.setAttribute(ID, objID);
			}
		}
		if (popup != null) {
			FeatureTreeNode pop = root.addChild("popup");
			pop.setAttribute(ID, popup);
		}

		if (red != null && green != null && blue != null && kayan != null) {
			FeatureTreeNode color = root.addChild("color");
			color.setAttribute("cyan", red);
			color.setAttribute("magenta", green);
			color.setAttribute("yellow", blue);
			color.setAttribute("black", blue);
		} else if (red != null && green != null && blue != null) {
			FeatureTreeNode color = root.addChild("color");
			color.setAttribute("red", red);
			color.setAttribute("green", green);
			color.setAttribute("blue", blue);
		} else if (red != null) {
			FeatureTreeNode color = root.addChild("color");
			color.setAttribute("gray", red);
		}

		root.addChild("invisible").setValue(invisible);
		root.addChild("hidden").setValue(hidden);
		root.addChild("print").setValue(print);
		root.addChild("noZoom").setValue(noZoom);
		root.addChild("noRotate").setValue(noRotate);
		root.addChild("noView").setValue(noView);
		root.addChild("readOnly").setValue(readOnly);
		root.addChild("locked").setValue(locked);
		root.addChild("toggleNoView").setValue(toggleNoView);
		root.addChild("lockedContents").setValue(lockedContents);

		return root;
	}

	public static FeatureTreeNode getOutlines() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("outlines");
		FeatureTreeNode out1 = root.addChild("outline");
		makeOutline("1 - COLOR", "1.000000", "0.000000", "0.000000", "false", "false", out1);
		FeatureTreeNode out1_1 = out1.addChild("outline");
		makeOutline("1.1", "0.000000", "0.000000", "1.000000", "false", "false", out1_1);
		FeatureTreeNode out2 = root.addChild("outline");
		makeOutline("2 - ITALIC", "0.000000", "0.000000", "0.000000", "true", "false", out2);
		FeatureTreeNode out2_2 = out2.addChild("outline");
		makeOutline("2.2", "0.000000", "0.000000", "0.000000", "true", "false", out2_2);
		FeatureTreeNode out2_2_1 = out2_2.addChild("outline");
		makeOutline("2.2.1", "0.000000", "0.000000", "0.000000", "true", "false", out2_2_1);
		FeatureTreeNode out2_2_2 = out2_2.addChild("outline");
		makeOutline("2.2.2", "0.000000", "0.000000", "0.000000", "true", "false", out2_2_2);
		FeatureTreeNode out2_2_2_1 = out2_2_2.addChild("outline");
		makeOutline("2.2.2.1", "0.000000", "0.000000", "0.000000", "true", "false", out2_2_2_1);
		FeatureTreeNode out2_1 = out2.addChild("outline");
		makeOutline("2.1", "0.000000", "0.000000", "0.000000", "true", "false", out2_1);
		FeatureTreeNode out3 = root.addChild("outline");
		makeOutline("3 - BOLD", "0.000000", "0.000000", "0.000000", "false", "true", out3);
		FeatureTreeNode out4 = root.addChild("outline");
		makeOutline("4", "0.000000", "0.000000", "0.000000", "false", "false", out4);
		return root;
	}

	public static void makeOutline(String title, String red, String green, String blue, String italic, String bold,
			FeatureTreeNode root) throws FeatureParsingException {
		root.addChild("title").setValue(title);
		FeatureTreeNode color = root.addChild("color");
		color.setAttribute("red", red);
		color.setAttribute("green", green);
		color.setAttribute("blue", blue);
		FeatureTreeNode style = root.addChild("style");
		style.setAttribute("italic", italic);
		style.setAttribute("bold", bold);
	}

	public static FeatureTreeNode getOutputIntent() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("outputIntent");
		root.addChild("subtype").setValue("GTS_PDFA1");
		root.addChild("outputCondition").setValue("SomeOutputCondition");
		root.addChild("outputConditionIdentifier").setValue("Apple RGB");
		root.addChild("registryName").setValue("fxqn:/us/va/reston/cnri/ietf/24/asdf%*.fred");
		root.addChild("info").setValue("RGB");
		FeatureTreeNode dest = root.addChild("destOutputIntent");
		dest.setAttribute(ID, "iccProfileIndir19");
		return root;
	}

	public static FeatureTreeNode getICCProfile(String id, String version, String cmmType, String dataColorSpace,
			String creator, String creationDate, String defaultRenderingIntent, String copyright, String description,
			String profileId, String deviceModel, String deviceManufacturer, byte[] metadata)
			throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("iccProfile");
		root.setAttribute(ID, id);

		addNotEmptyNode("version", version, root);
		addNotEmptyNode("cmmType", cmmType, root);
		addNotEmptyNode("dataColorSpace", dataColorSpace, root);
		addNotEmptyNode("creator", creator, root);
		addNotEmptyNode("creationDate", creationDate, root);
		addNotEmptyNode("defaultRenderingIntent", defaultRenderingIntent, root);
		addNotEmptyNode("copyright", copyright, root);
		addNotEmptyNode("description", description, root);
		addNotEmptyNode("profileId", profileId, root);
		addNotEmptyNode("deviceModel", deviceModel, root);
		addNotEmptyNode("deviceManufacturer", deviceManufacturer, root);
		if (metadata != null) {
			root.addMetadataChild("metadata")
					.setValue(DatatypeConverter.printHexBinary(metadata));
		}

		return root;
	}

	public static FeatureTreeNode getEmbeddedFileNode(String id, String fileName, String description, String subtype,
			String filter, String creationDate, String modDate, String checkSum, String size)
			throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("embeddedFile");
		root.setAttribute(ID, id);
		root.addChild("fileName").setValue(fileName);
		root.addChild("description").setValue(description);
		root.addChild("subtype").setValue(subtype);
		root.addChild("filter").setValue(filter);
		root.addChild("creationDate").setValue(creationDate);
		root.addChild("modDate").setValue(modDate);
		root.addChild("checkSum").setValue(checkSum);
		root.addChild("size").setValue(size);
		return root;
	}

	public static FeatureTreeNode getLowLvlInfo() throws FeatureParsingException {
		FeatureTreeNode lli = FeatureTreeNode.createRootNode("lowLevelInfo");
		lli.addChild("indirectObjectsNumber").setValue("129");
		FeatureTreeNode docID = lli.addChild("documentId");
		docID.setAttribute("modificationId", "295EBB0E08D32644B7E5C1825F15AD3A");
		docID.setAttribute("creationId", "85903F3A2C43B1DA24E486CD15B8154E");
		lli.addChild("tagged").setValue("false");
		FeatureTreeNode filters = lli.addChild("filters");
		addFilter("FlateDecode", filters);
		addFilter("ASCIIHexDecode", filters);
		addFilter("ASCII85Decode", filters);
		addFilter("CCITTFaxDecode", filters);
		addFilter("DCTDecode", filters);
		return lli;
	}

	public static void addFilter(String name, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode filter = parent.addChild("filter");
		filter.setAttribute("name", name);
	}

	public static FeatureTreeNode getInfDictNode() throws FeatureParsingException {
		FeatureTreeNode infDict = FeatureTreeNode.createRootNode("informationDict");
		addEntry("Title", "SomeTitle", infDict);
		addEntry("Author", "SomeAuthor", infDict);
		addEntry("Subject", "SomeSubject", infDict);
		addEntry("Keywords", "Some Keywords", infDict);
		addEntry("Creator", "SomeCreator", infDict);
		addEntry("Producer", "SomeProducer", infDict);
		addEntry("CreationDate", "2015-08-22T14:04:45.000+03:00", infDict);
		addEntry("ModDate", "2015-08-31T14:05:31.000+03:00", infDict);
		addEntry("Trapped", "False", infDict);
		addEntry("CustomEntry", "CustomValue", infDict);
		addEntry("SecondCustomEntry", "SomeCustomValue", infDict);
		return infDict;
	}

	public static FeatureTreeNode getMetadataNode()
			throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		FeatureTreeNode rootMetadataNode = FeatureTreeNode.createRootNode(METADATA);
		FeatureTreeNode xmpNode = rootMetadataNode.addMetadataChild("xmpPackage");
		xmpNode.setValue(DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/metadata_bytes.txt")));
		return rootMetadataNode;
	}

	public static byte[] getMetadataBytesFromFile(String path) throws URISyntaxException, FileNotFoundException {
		try (Scanner scan = new Scanner(new File(getSystemIndependentPath(path)))) {
			int n = scan.nextInt();
			byte[] res = new byte[n];
			for (int i = 0; scan.hasNextInt(); ++i) {
				res[i] = (byte) scan.nextInt();
			}
			return res;
		}
	}

	public static void addEntry(String name, String value, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode entry = parent.addChild(ENTRY);
		entry.setValue(value);
		entry.setAttribute("key", name);
	}

	public static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

	public class FontDescriptorStructure {
		public String fontName = null;
		public String fontFamily = null;
		public String fontStretch = null;
		public String fontWeight = null;
		public String fixedPitch = null;
		public String serif = null;
		public String symbolic = null;
		public String script = null;
		public String nonsymbolic = null;
		public String italic = null;
		public String allCap = null;
		public String smallCap = null;
		public String forceBold = null;
		public String fontBBox_llx = null;
		public String fontBBox_lly = null;
		public String fontBBox_urx = null;
		public String fontBBox_ury = null;
		public String italicAngle = null;
		public String ascent = null;
		public String descent = null;
		public String leading = null;
		public String capHeight = null;
		public String xHeight = null;
		public String stemV = null;
		public String stemH = null;
		public String averageWidth = null;
		public String maxWidth = null;
		public String missingWidth = null;

		public FontDescriptorStructure() {
		}

		public FeatureTreeNode generateNode(FeatureTreeNode parent) throws FeatureParsingException {
			FeatureTreeNode root = parent.addChild("fontDescriptor");

			addNotEmptyNode("fontName", fontName, root);
			addNotEmptyNode("fontFamily", fontFamily, root);
			addNotEmptyNode("fontStretch", fontStretch, root);
			addNotEmptyNode("fontWeight", fontWeight, root);
			addNotEmptyNode("fixedPitch", fixedPitch, root);
			addNotEmptyNode("serif", serif, root);
			addNotEmptyNode("symbolic", symbolic, root);
			addNotEmptyNode("script", script, root);
			addNotEmptyNode("nonsymbolic", nonsymbolic, root);
			addNotEmptyNode("italic", italic, root);
			addNotEmptyNode("allCap", allCap, root);
			addNotEmptyNode("smallCap", smallCap, root);
			addNotEmptyNode("forceBold", forceBold, root);

			FeatureTreeNode bbox = root.addChild("fontBBox");
			bbox.setAttribute(LLX, fontBBox_llx);
			bbox.setAttribute(LLY, fontBBox_lly);
			bbox.setAttribute(URX, fontBBox_urx);
			bbox.setAttribute(URY, fontBBox_ury);

			addNotEmptyNode("italicAngle", italicAngle, root);
			addNotEmptyNode("ascent", ascent, root);
			addNotEmptyNode("descent", descent, root);
			addNotEmptyNode("leading", leading, root);
			addNotEmptyNode("capHeight", capHeight, root);
			addNotEmptyNode("xHeight", xHeight, root);
			addNotEmptyNode("stemV", stemV, root);
			addNotEmptyNode("stemH", stemH, root);
			addNotEmptyNode("averageWidth", averageWidth, root);
			addNotEmptyNode("maxWidth", maxWidth, root);
			addNotEmptyNode("missingWidth", missingWidth, root);
			return parent;
		}
	}

	private static FeatureTreeNode addNotEmptyNode(String name, String value, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode node = parent.addChild(name);
			node.setValue(value);
			return node;
		}
		return null;
	}
}
