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
package org.verapdf.features.pb;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.verapdf.features.objects.*;
import org.verapdf.features.pb.objects.*;

import java.util.Set;

/**
 * Creates Feature Objects and report them to Features Reporter
 *
 * @author Maksim Bezrukov
 */
public final class PBFeaturesObjectCreator {

	private PBFeaturesObjectCreator() {
	}

	/**
	 * Creates new PBInfoDictFeaturesObjectAdapter
	 *
	 * @param info PDDocumentInformation class from pdfbox, which represents a document info dictionary for feature report
	 * @return created PBInfoDictFeaturesObjectAdapter
	 */
	public static InfoDictFeaturesObject createInfoDictFeaturesObject(PDDocumentInformation info) {
		PBInfoDictFeaturesObjectAdapter adapter = new PBInfoDictFeaturesObjectAdapter(info);
		return new InfoDictFeaturesObject(adapter);
	}

	/**
	 * Creates new PBMetadataFeaturesObjectAdapter
	 *
	 * @param metadata PDMetadata class from pdfbox, which represents a metadata for feature report
	 * @return created PBMetadataFeaturesObjectAdapter
	 */
	public static MetadataFeaturesObject createMetadataFeaturesObject(PDMetadata metadata) {
		PBMetadataFeaturesObjectAdapter adapter = new PBMetadataFeaturesObjectAdapter(metadata);
		return new MetadataFeaturesObject(adapter);
	}

	/**
	 * Creates new PBDocSecurityFeaturesObjectAdapter
	 *
	 * @param encryption PDEncryption class from pdfbox, which represents an encryption for feature report
	 * @return created PBDocSecurityFeaturesObjectAdapter
	 */
	public static DocSecurityFeaturesObject createDocSecurityFeaturesObject(PDEncryption encryption) {
		PBDocSecurityFeaturesObjectAdapter adapter = new PBDocSecurityFeaturesObjectAdapter(encryption);
		return new DocSecurityFeaturesObject(adapter);
	}

	/**
	 * Creates new PBLowLvlInfoFeaturesObjectAdapter
	 *
	 * @param document COSDocument class from pdfbox, which represents a document for feature report
	 * @return created PBLowLvlInfoFeaturesObjectAdapter
	 */
	public static LowLvlInfoFeaturesObject createLowLvlInfoFeaturesObject(COSDocument document) {
		PBLowLvlInfoFeaturesObjectAdapter adapter = new PBLowLvlInfoFeaturesObjectAdapter(document);
		return new LowLvlInfoFeaturesObject(adapter);
	}

	/**
	 * Creates new PBEmbeddedFileFeaturesObjectAdapter
	 *
	 * @param embFile PDComplexFileSpecification class from pdfbox, which represents a file specification with embedded
	 *                file for feature report
	 * @return created PBEmbeddedFileFeaturesObjectAdapter
	 */
	public static EmbeddedFileFeaturesObject createEmbeddedFileFeaturesObject(PDComplexFileSpecification embFile,
																				int index) {
		PBEmbeddedFileFeaturesObjectAdapter adapter = new PBEmbeddedFileFeaturesObjectAdapter(embFile, index);
		return new EmbeddedFileFeaturesObject(adapter);
	}

	/**
	 * Creates new PBOutputIntentsFeaturesObjectAdapter
	 *
	 * @param outInt       PDOutputIntent class from pdfbox, which represents an outputIntent for feature report
	 * @param iccProfileID id of the icc profile which use in this outputIntent
	 * @return created PBOutputIntentsFeaturesObjectAdapter
	 */
	public static OutputIntentFeaturesObject createOutputIntentFeaturesObject(PDOutputIntent outInt,
																				 String iccProfileID) {
		PBOutputIntentsFeaturesObjectAdapter adapter = new PBOutputIntentsFeaturesObjectAdapter(outInt, iccProfileID);
		return new OutputIntentFeaturesObject(adapter);
	}

	/**
	 * Creates new PBOutlinesFeaturesObjectAdapter
	 *
	 * @param outlines PDPage class from pdfbox, which represents a page for feature report
	 * @return created PBOutlinesFeaturesObjectAdapter
	 */
	public static OutlinesFeaturesObject createOutlinesFeaturesObject(PDDocumentOutline outlines) {
		PBOutlinesFeaturesObjectAdapter adapter = new PBOutlinesFeaturesObjectAdapter(outlines);
		return new OutlinesFeaturesObject(adapter);
	}

	/**
	 * Creates new PBAnnotationFeaturesObjectAdapter
	 *
	 * @param annot        PDAnnotation class from pdfbox, which represents an annotation for feature report
	 * @param id           page id
	 * @param popupId      id of the popup annotation for this annotation
	 * @param formXObjects set of id of the form XObjects which used in appearance stream of this annotation
	 * @return created PBAnnotationFeaturesObjectAdapter
	 */
	public static AnnotationFeaturesObject createAnnotFeaturesObject(PDAnnotation annot,
																	 String id,
																	 String popupId,
																	 Set<String> formXObjects) {
		PBAnnotationFeaturesObjectAdapter adapter = new PBAnnotationFeaturesObjectAdapter(annot, id, popupId, formXObjects);
		return new AnnotationFeaturesObject(adapter);
	}

	/**
	 * Creates new PBPageFeaturesObjectAdapter
	 *
	 * @param page            pdfbox class represents page object
	 * @param thumb           thumbnail image id
	 * @param annotsId        set of annotations id which contains in this page
	 * @param extGStateChild  set of extGState id which contains in resource dictionary of this page
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this page
	 * @param patternChild    set of pattern id which contains in resource dictionary of this page
	 * @param shadingChild    set of shading id which contains in resource dictionary of this page
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this page
	 * @param fontChild       set of font id which contains in resource dictionary of this page
	 * @param propertiesChild set of properties id which contains in resource dictionary of this page
	 * @param index           page index
	 * @return created PBPageFeaturesObjectAdapter
	 */
	public static PageFeaturesObject createPageFeaturesObject(PDPage page,
																String thumb,
																Set<String> annotsId,
																Set<String> extGStateChild,
																Set<String> colorSpaceChild,
																Set<String> patternChild,
																Set<String> shadingChild,
																Set<String> xobjectChild,
																Set<String> fontChild,
																Set<String> propertiesChild,
																int index) {
		PBPageFeaturesObjectAdapter adapter = new PBPageFeaturesObjectAdapter(page, thumb, annotsId, extGStateChild,
				colorSpaceChild, patternChild, shadingChild, xobjectChild,
				fontChild, propertiesChild, index);
		return new PageFeaturesObject(adapter);
	}

	/**
	 * Creates new PBICCProfileFeaturesObjectAdapter
	 *
	 * @param profile   COSStream which represents the icc profile for feature report
	 * @param id        id of the profile
	 * @return created PBICCProfileFeaturesObjectAdapter
	 */
	public static ICCProfileFeaturesObject createICCProfileFeaturesObject(COSStream profile, String id) {
		PBICCProfileFeaturesObjectAdapter adapter = new PBICCProfileFeaturesObjectAdapter(profile, id);
		return new ICCProfileFeaturesObject(adapter);
	}

	/**
	 * Creates new PBExtGStateFeaturesObjectAdapter
	 *
	 * @param exGState         PDExtendedGraphicsState which represents extended graphics state for feature report
	 * @param id               id of the object
	 * @param fontChildID      id of the font child
	 * @return created PBExtGStateFeaturesObjectAdapter
	 */
	public static ExtGStateFeaturesObject createExtGStateFeaturesObject(PDExtendedGraphicsState exGState,
																		  String id,
																		  String fontChildID) {
		PBExtGStateFeaturesObjectAdapter adapter = new PBExtGStateFeaturesObjectAdapter(exGState, id, fontChildID);
		return new ExtGStateFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBColorSpaceFeaturesObjectAdapter
	 *
	 * @param colorSpace        PDColorSpace which represents colorspace for feature report
	 * @param id                id of the object
	 * @param iccProfileChild   id of the iccprofile child
	 * @param colorSpaceChild   id of the colorspace child
	 * @return created PBColorSpaceFeaturesObjectAdapter
	 */
	public static ColorSpaceFeaturesObject createColorSpaceFeaturesObject(PDColorSpace colorSpace,
																			String id,
																			String iccProfileChild,
																			String colorSpaceChild) {
		PBColorSpaceFeaturesObjectAdapter adapter = new PBColorSpaceFeaturesObjectAdapter(colorSpace, id, iccProfileChild, colorSpaceChild);
		return new ColorSpaceFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBTilingPatternFeaturesObjectAdapter
	 *
	 * @param tilingPattern   PDTilingPattern which represents tilling pattern for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this pattern
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this pattern
	 * @param patternChild    set of pattern id which contains in resource dictionary of this pattern
	 * @param shadingChild    set of shading id which contains in resource dictionary of this pattern
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this pattern
	 * @param fontChild       set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild set of properties id which contains in resource dictionary of this pattern
	 * @return created PBTilingPatternFeaturesObjectAdapter
	 */
	public static TilingPatternFeaturesObject createTilingPatternFeaturesObject(PDTilingPattern tilingPattern,
																				  String id,
																				  Set<String> extGStateChild,
																				  Set<String> colorSpaceChild,
																				  Set<String> patternChild,
																				  Set<String> shadingChild,
																				  Set<String> xobjectChild,
																				  Set<String> fontChild,
																				  Set<String> propertiesChild) {
		PBTilingPatternFeaturesObjectAdapter adapter = new PBTilingPatternFeaturesObjectAdapter(tilingPattern, id, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild);
		return new TilingPatternFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBShadingPatternFeaturesObjectAdapter
	 *
	 * @param shadingPattern PDShadingPattern which represents shading pattern for feature report
	 * @param id             id of the object
	 * @param extGStateChild external graphics state id which contains in this shading pattern
	 * @param shadingChild   shading id which contains in this shading pattern
	 * @return created PBShadingPatternFeaturesObjectAdapter
	 */
	public static ShadingPatternFeaturesObject createShadingPatternFeaturesObject(PDShadingPattern shadingPattern,
																					String id,
																					String shadingChild,
																					String extGStateChild) {
		PBShadingPatternFeaturesObjectAdapter adapter = new PBShadingPatternFeaturesObjectAdapter(shadingPattern, id, shadingChild, extGStateChild);
		return new ShadingPatternFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBShadingFeaturesObjectAdapter
	 *
	 * @param shading         PDShading which represents shading for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this shading pattern
	 * @return created PBShadingFeaturesObjectAdapter
	 */
	public static ShadingFeaturesObject createShadingFeaturesObject(PDShading shading,
																	  String id,
																	  String colorSpaceChild) {
		PBShadingFeaturesObjectAdapter adapter = new PBShadingFeaturesObjectAdapter(shading, id, colorSpaceChild);
		return new ShadingFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBImageXObjectFeaturesObject
	 *
	 * @param imageXObject    PDImageXObject which represents image xobject for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this image xobject
	 * @param maskChild       image xobject id which contains in this image xobject as it's mask
	 * @param sMaskChild      image xobject id which contains in this image xobject as it's smask
	 * @param alternatesChild set of image xobject ids which contains in this image xobject as alternates
	 * @return created PBImageXObjectFeaturesObject
	 */
	public static ImageXObjectFeaturesObject createImageXObjectFeaturesObject(PDImageXObjectProxy imageXObject,
																				String id,
																				String colorSpaceChild,
																				String maskChild,
																				String sMaskChild,
																				Set<String> alternatesChild) {
		PBImageXObjectFeaturesObjectAdapter adapter = new PBImageXObjectFeaturesObjectAdapter(imageXObject, id, colorSpaceChild, maskChild, sMaskChild, alternatesChild);
		return new ImageXObjectFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBFormXObjectFeaturesObjectAdapter
	 *
	 * @param formXObject      PDFormXObject which represents form xobject for feature report
	 * @param id               id of the object
	 * @param groupChild       id of the group xobject which contains in the given form xobject
	 * @param extGStateChild   set of external graphics state id which contains in resource dictionary of this xobject
	 * @param colorSpaceChild  set of ColorSpace id which contains in resource dictionary of this xobject
	 * @param patternChild     set of pattern id which contains in resource dictionary of this xobject
	 * @param shadingChild     set of shading id which contains in resource dictionary of this xobject
	 * @param xobjectChild     set of XObject id which contains in resource dictionary of this xobject
	 * @param fontChild        set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild  set of properties id which contains in resource dictionary of this xobject
	 * @return created PBFormXObjectFeaturesObjectAdapter
	 */
	public static FormXObjectFeaturesObject createFormXObjectFeaturesObject(PDFormXObject formXObject,
																			  String id,
																			  String groupChild,
																			  Set<String> extGStateChild,
																			  Set<String> colorSpaceChild,
																			  Set<String> patternChild,
																			  Set<String> shadingChild,
																			  Set<String> xobjectChild,
																			  Set<String> fontChild,
																			  Set<String> propertiesChild) {
		PBFormXObjectFeaturesObjectAdapter adapter = new PBFormXObjectFeaturesObjectAdapter(formXObject, id, groupChild, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild);
		return new FormXObjectFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBFontFeaturesObjectAdapter
	 *
	 * @param fontLike        PDFontLike which represents font for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this font
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this font
	 * @param patternChild    set of pattern id which contains in resource dictionary of this font
	 * @param shadingChild    set of shading id which contains in resource dictionary of this font
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this font
	 * @param fontChild       set of font id which contains in resource dictionary of this font
	 * @param propertiesChild set of properties id which contains in resource dictionary of this font
	 * @return created PBFontFeaturesObjectAdapter
	 */
	public static FontFeaturesObject createFontFeaturesObject(PDFontLike fontLike,
																String id,
																Set<String> extGStateChild,
																Set<String> colorSpaceChild,
																Set<String> patternChild,
																Set<String> shadingChild,
																Set<String> xobjectChild,
																Set<String> fontChild,
																Set<String> propertiesChild) {
		PBFontFeaturesObjectAdapter adapter = new PBFontFeaturesObjectAdapter(fontLike, id, extGStateChild, colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild);
		return new FontFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBPropertiesDictFeaturesObjectAdapter
	 *
	 * @param properties    COSDictionary which represents properties for feature report
	 * @param id            id of the object
	 * @return created PBPropertiesDictFeaturesObjectAdapter
	 */
	public static PropertiesDictFeaturesObject createPropertiesDictFeaturesObject(COSDictionary properties,
																					String id) {
		PBPropertiesDictFeaturesObjectAdapter adapter = new PBPropertiesDictFeaturesObjectAdapter(properties, id);
		return new PropertiesDictFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBPostScriptXObjectFeaturesObjectAdapter
	 *
	 * @param id            id of the object
	 * @return created PBPostScriptXObjectFeaturesObjectAdapter
	 */
	public static PostScriptFeaturesObject createPostScriptXObjectFeaturesObject(String id) {
		PBPostScriptXObjectFeaturesObjectAdapter adapter = new PBPostScriptXObjectFeaturesObjectAdapter(id);
		return new PostScriptFeaturesObject(adapter);
	}

	/**
	 * Constructs new PBSignatureFeaturesObjectAdapter
	 *
	 * @param signature pdfbox signature object
	 * @return created PBSignatureFeaturesObjectAdapter
     */
	public static SignatureFeaturesObject createSignatureFeaturesObject(PDSignature signature) {
		PBSignatureFeaturesObjectAdapter adapter = new PBSignatureFeaturesObjectAdapter(signature);
		return new SignatureFeaturesObject(adapter);
	}
}
