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
import org.verapdf.features.pb.tools.PBAdapterHelper;
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
		FeatureTreeNode widths = font.addChild("widths");
		String[] chars = new String[] { "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
				"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61",
				"62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78",
				"79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95",
				"96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110",
				"111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121" };
		String[] values = new String[] { "212", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
				"0", "0", "513", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
				"0", "0", "492", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "497", "0", "0",
				"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "569", "0", "564", "501", "292", "0", "0", "0",
				"0", "0", "0", "834", "555", "549", "569", "0", "0", "0", "331", "0", "0", "0", "0", "471" };
		for (int i = 0; i < values.length; ++i) {
			FeatureTreeNode width = widths.addChild("width");
			width.setValue(values[i]);
			width.setAttribute("char", chars[i]);
		}
		FeatureTreeNode encoding = font.addChild("encoding");
		encoding.setValue("WinAnsiEncoding");
		FeatureTreeNode fontDescriptor = font.addChild("fontDescriptor");
		FeatureTreeNode fontName = fontDescriptor.addChild("fontName");
		fontName.setValue("OLXYQW+MyriadPro-Regular");
		FeatureTreeNode fontFamily = fontDescriptor.addChild("fontFamily");
		fontFamily.setValue("Myriad Pro");
		FeatureTreeNode fontStretch = fontDescriptor.addChild("fontStretch");
		fontStretch.setValue("Normal");
		FeatureTreeNode fontWeight = fontDescriptor.addChild("fontWeight");
		fontWeight.setValue("400.0");
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
		fontBBox.setAttribute("lly", "-250.0");
		fontBBox.setAttribute("llx", "-157.0");
		fontBBox.setAttribute("urx", "1126.0");
		fontBBox.setAttribute("ury", "952.0");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.0");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("952.0");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-250.0");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.0");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("674.0");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("484.0");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("88.0");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.0");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.0");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("0.0");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("0.0");
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
		fontBBox.setAttribute("lly", "-325.0");
		fontBBox.setAttribute("llx", "-665.0");
		fontBBox.setAttribute("urx", "2000.0");
		fontBBox.setAttribute("ury", "1006.0");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.0");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("728.0");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-210.0");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.0");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("677.0");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("480.0");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("88.0");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.0");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.0");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("0.0");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("0.0");
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
		fontName.setValue("IIWNIN+AdobeFanHeitiStd-Bold");
		FeatureTreeNode fontFamily = fontDescriptor.addChild("fontFamily");
		fontFamily.setValue("Adobe Fan Heiti Std B");
		FeatureTreeNode fontStretch = fontDescriptor.addChild("fontStretch");
		fontStretch.setValue("Normal");
		FeatureTreeNode fontWeight = fontDescriptor.addChild("fontWeight");
		fontWeight.setValue("600.0");
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
		fontBBox.setAttribute("lly", "-283.0");
		fontBBox.setAttribute("llx", "-163.0");
		fontBBox.setAttribute("urx", "1087.0");
		fontBBox.setAttribute("ury", "967.0");
		FeatureTreeNode italicAngle = fontDescriptor.addChild("italicAngle");
		italicAngle.setValue("0.0");
		FeatureTreeNode ascent = fontDescriptor.addChild("ascent");
		ascent.setValue("967.0");
		FeatureTreeNode descent = fontDescriptor.addChild("descent");
		descent.setValue("-283.0");
		FeatureTreeNode leading = fontDescriptor.addChild("leading");
		leading.setValue("0.0");
		FeatureTreeNode capHeight = fontDescriptor.addChild("capHeight");
		capHeight.setValue("766.0");
		FeatureTreeNode xHeight = fontDescriptor.addChild("xHeight");
		xHeight.setValue("551.0");
		FeatureTreeNode stemV = fontDescriptor.addChild("stemV");
		stemV.setValue("116.0");
		FeatureTreeNode stemH = fontDescriptor.addChild("stemH");
		stemH.setValue("0.0");
		FeatureTreeNode averageWidth = fontDescriptor.addChild("averageWidth");
		averageWidth.setValue("0.0");
		FeatureTreeNode maxWidth = fontDescriptor.addChild("maxWidth");
		maxWidth.setValue("1000.0");
		FeatureTreeNode missingWidth = fontDescriptor.addChild("missingWidth");
		missingWidth.setValue("800.0");
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
		bbox.setAttribute(LLX, "0.0");
		bbox.setAttribute(LLY, "0.0");
		bbox.setAttribute(URX, "5.0");
		bbox.setAttribute(URY, "10.0");
		root.addChild("xStep").setValue("5.0");
		root.addChild("yStep").setValue("10.0");
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
		addElement("1", "1.0", matr);
		addElement("2", "0.0", matr);
		addElement("3", "0.0", matr);
		addElement("4", "1.0", matr);
		addElement("5", "0.0", matr);
		addElement("6", "0.0", matr);
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
		bbox.setAttribute(LLX, "0.0");
		bbox.setAttribute(LLY, "0.0");
		bbox.setAttribute(URX, "400.0");
		bbox.setAttribute(URY, "400.0");
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
		root.addChild("scaling").setValue("75.0");
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
		box.setAttribute(LLX, "0.0");
		box.setAttribute(LLY, "0.0");
		box.setAttribute(URX, "499.977");
		box.setAttribute(URY, "499.977");
	}

	public static FeatureTreeNode getAnnotation(String id, String subtype, String llx, String lly, String urx,
			String ury, String contents, String annotationName, String modifiedDate, Set<String> xobj, String popup,
			String red, String green, String blue, String kayan, String invisible, String hidden, String print,
			String noZoom, String noRotate, String noView, String readOnly, String locked, String toggleNoView,
			String lockedContents) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("annotation");
		root.setAttribute(ID, id);
		PBAdapterHelper.addNotEmptyNode("subType", subtype, root);
		FeatureTreeNode rec = root.addChild("rectangle");
		rec.setAttribute(LLX, llx);
		rec.setAttribute(LLY, lly);
		rec.setAttribute(URX, urx);
		rec.setAttribute(URY, ury);
		PBAdapterHelper.addNotEmptyNode("contents", contents, root);
		PBAdapterHelper.addNotEmptyNode("annotationName", annotationName, root);
		PBAdapterHelper.addNotEmptyNode("modifiedDate", modifiedDate, root);

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

		PBAdapterHelper.addNotEmptyNode("version", version, root);
		PBAdapterHelper.addNotEmptyNode("cmmType", cmmType, root);
		PBAdapterHelper.addNotEmptyNode("dataColorSpace", dataColorSpace, root);
		PBAdapterHelper.addNotEmptyNode("creator", creator, root);
		PBAdapterHelper.addNotEmptyNode("creationDate", creationDate, root);
		PBAdapterHelper.addNotEmptyNode("defaultRenderingIntent", defaultRenderingIntent, root);
		PBAdapterHelper.addNotEmptyNode("copyright", copyright, root);
		PBAdapterHelper.addNotEmptyNode("description", description, root);
		PBAdapterHelper.addNotEmptyNode("profileId", profileId, root);
		PBAdapterHelper.addNotEmptyNode("deviceModel", deviceModel, root);
		PBAdapterHelper.addNotEmptyNode("deviceManufacturer", deviceManufacturer, root);
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

			PBAdapterHelper.addNotEmptyNode("fontName", fontName, root);
			PBAdapterHelper.addNotEmptyNode("fontFamily", fontFamily, root);
			PBAdapterHelper.addNotEmptyNode("fontStretch", fontStretch, root);
			PBAdapterHelper.addNotEmptyNode("fontWeight", fontWeight, root);
			PBAdapterHelper.addNotEmptyNode("fixedPitch", fixedPitch, root);
			PBAdapterHelper.addNotEmptyNode("serif", serif, root);
			PBAdapterHelper.addNotEmptyNode("symbolic", symbolic, root);
			PBAdapterHelper.addNotEmptyNode("script", script, root);
			PBAdapterHelper.addNotEmptyNode("nonsymbolic", nonsymbolic, root);
			PBAdapterHelper.addNotEmptyNode("italic", italic, root);
			PBAdapterHelper.addNotEmptyNode("allCap", allCap, root);
			PBAdapterHelper.addNotEmptyNode("smallCap", smallCap, root);
			PBAdapterHelper.addNotEmptyNode("forceBold", forceBold, root);

			FeatureTreeNode bbox = root.addChild("fontBBox");
			bbox.setAttribute(LLX, fontBBox_llx);
			bbox.setAttribute(LLY, fontBBox_lly);
			bbox.setAttribute(URX, fontBBox_urx);
			bbox.setAttribute(URY, fontBBox_ury);

			PBAdapterHelper.addNotEmptyNode("italicAngle", italicAngle, root);
			PBAdapterHelper.addNotEmptyNode("ascent", ascent, root);
			PBAdapterHelper.addNotEmptyNode("descent", descent, root);
			PBAdapterHelper.addNotEmptyNode("leading", leading, root);
			PBAdapterHelper.addNotEmptyNode("capHeight", capHeight, root);
			PBAdapterHelper.addNotEmptyNode("xHeight", xHeight, root);
			PBAdapterHelper.addNotEmptyNode("stemV", stemV, root);
			PBAdapterHelper.addNotEmptyNode("stemH", stemH, root);
			PBAdapterHelper.addNotEmptyNode("averageWidth", averageWidth, root);
			PBAdapterHelper.addNotEmptyNode("maxWidth", maxWidth, root);
			PBAdapterHelper.addNotEmptyNode("missingWidth", missingWidth, root);
			return parent;
		}
	}
}
