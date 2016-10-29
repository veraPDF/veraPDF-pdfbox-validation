package org.verapdf.features.pb;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.documentinterchange.markedcontent.PDPropertyList;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.PDPostScriptXObject;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.color.PDICCBased;
import org.apache.pdfbox.pdmodel.graphics.color.PDIndexed;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesExtractor;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.config.FeaturesConfig;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Parses PDFBox PDDocument to generate features collection
 *
 * @author Maksim Bezrukov
 */
public final class PBFeatureParser {
	private static final EnumSet<FeatureObjectType> XOBJECTS = EnumSet.of(FeatureObjectType.FORM_XOBJECT,
			FeatureObjectType.IMAGE_XOBJECT, FeatureObjectType.POSTSCRIPT_XOBJECT, FeatureObjectType.FAILED_XOBJECT);
	private static final Logger LOGGER = Logger.getLogger(PBFeatureParser.class);
	private static final String ID = "id";
	private static final String DEVICEGRAY_ID = "devgray";
	private static final String DEVICERGB_ID = "devrgb";
	private static final String DEVICECMYK_ID = "devcmyk";

	private FeaturesReporter reporter;
	private FeaturesConfig config;
	private Set<String> processedIDs;

	private PBFeatureParser(FeaturesReporter reporter, FeaturesConfig config) {
		this.reporter = reporter;
		this.config = config;
		this.processedIDs = new HashSet<>();
	}

	/**
	 * Parses the document and returns Feature collection by using given
	 * Features Reporter
	 *
	 * @param document
	 *            the document for parsing
	 * @return FeaturesCollection class with information about all featurereport
	 */
	public static FeaturesCollection getFeaturesCollection(final PDDocument document, final FeaturesConfig config) {

		FeaturesReporter reporter = new FeaturesReporter(config);
		return getFeatures(document, reporter, config);
	}

	/**
	 * Parses the document and returns Feature collection by using given
	 * Features Reporter
	 *
	 * @param document
	 *            the document for parsing
	 * @return FeaturesCollection class with information about all featurereport
	 */
	public static FeaturesCollection getFeaturesCollection(final PDDocument document,
			final List<FeaturesExtractor> extractors, final FeaturesConfig config) {

		FeaturesReporter reporter = new FeaturesReporter(config, extractors);
		return getFeatures(document, reporter, config);
	}

	private static FeaturesCollection getFeatures(PDDocument document, FeaturesReporter reporter,
			FeaturesConfig config) {
		if (config == null) {
			throw new IllegalArgumentException("Features config can not be null");
		}
		if (document != null) {
			PBFeatureParser parser = new PBFeatureParser(reporter, config);
			parser.parseDocumentFeatures(document);
		}

		return reporter.getCollection();
	}

	private void parseDocumentFeatures(PDDocument document) {
		reporter.report(PBFeaturesObjectCreator.createInfoDictFeaturesObject(document.getDocumentInformation()));
		reporter.report(PBFeaturesObjectCreator.createDocSecurityFeaturesObject(document.getEncryption()));

		PDDocumentCatalog catalog = document.getDocumentCatalog();
		if (catalog != null) {
			getCatalogFeatures(catalog);
		}

		reporter.report(PBFeaturesObjectCreator.createLowLvlInfoFeaturesObject(document.getDocument()));

	}

	private void getCatalogFeatures(PDDocumentCatalog catalog) {
		reporter.report(PBFeaturesObjectCreator.createMetadataFeaturesObject(catalog.getMetadata()));
		reporter.report(PBFeaturesObjectCreator.createOutlinesFeaturesObject(catalog.getDocumentOutline()));

		PDAcroForm acroForm = catalog.getAcroForm();
		if (acroForm != null) {
			getAcroFormFeatures(acroForm);
		}

		if (catalog.getNames() != null && catalog.getNames().getEmbeddedFiles() != null) {
			reportEmbeddedFiles(catalog);
		}

		if (catalog.getOutputIntents() != null) {
			for (PDOutputIntent outInt : catalog.getOutputIntents()) {
				String iccProfileID = addICCProfileFromOutputIntent(outInt);
				if (!config.isFeatureEnabled(FeatureObjectType.ICCPROFILE)) {
					iccProfileID = null;
				}
				reporter.report(PBFeaturesObjectCreator.createOutputIntentFeaturesObject(outInt, iccProfileID));
			}
		}

		PDPageTree pageTree = catalog.getPages();
		if (pageTree != null) {
			getPageTreeFeatures(pageTree);
		}
	}

	private void getAcroFormFeatures(PDAcroForm acroForm) {
		List<PDField> fields = acroForm.getFields();
		if (fields != null) {
			for (PDField field : fields) {
				getFieldFeatures(field);
			}
		}
	}

	private void getFieldFeatures(PDField field) {
		if (config.isFeatureEnabled(FeatureObjectType.SIGNATURE) && field instanceof PDSignatureField) {
			PDSignature signature = ((PDSignatureField) field).getSignature();
			if (signature != null) {
				reporter.report(PBFeaturesObjectCreator.createSignatureFeaturesObject(signature));
			}
		}
	}

	private void getPageTreeFeatures(PDPageTree pageTree) {
		for (PDPage page : pageTree) {
			Set<String> annotsId = addAnnotsDependencies(page);
			annotsId = config.isFeatureEnabled(FeatureObjectType.ANNOTATION) ? annotsId : null;

			String thumbID = null;
			if (page.getCOSObject().getDictionaryObject(COSName.getPDFName("Thumb")) != null) {
				COSBase baseThumb = page.getCOSObject().getItem(COSName.getPDFName("Thumb"));
				thumbID = getId(baseThumb, FeatureObjectType.IMAGE_XOBJECT);
				if (checkIDBeforeProcess(thumbID)) {
					COSBase base = getBase(baseThumb);
					if (base instanceof COSStream) {
						PDImageXObjectProxy img = new PDImageXObjectProxy(new PDStream((COSStream) base), null);
						parseImageXObject(img, thumbID);
					} else {
						xobjectCreationProblem(thumbID, "Thumb is not a stream");
					}
				}
			}
			thumbID = config.isAnyFeatureEnabled(XOBJECTS) ? thumbID : null;

			PDResources resources = page.getResources();
			Set<String> extGStateChild = config.isFeatureEnabled(FeatureObjectType.EXT_G_STATE)
					? parseExGStateFromResource(resources) : null;
			Set<String> colorSpaceChild = config.isFeatureEnabled(FeatureObjectType.COLORSPACE)
					? parseColorSpaceFromResources(resources) : null;
			Set<String> patternChild = config.isFeatureEnabled(FeatureObjectType.PATTERN)
					? parsePatternFromResource(resources) : null;
			Set<String> shadingChild = config.isFeatureEnabled(FeatureObjectType.SHADING)
					? parseShadingFromResource(resources) : null;
			Set<String> xobjectChild = config.isAnyFeatureEnabled(XOBJECTS) ? parseXObjectFromResources(resources)
					: null;
			Set<String> fontChild = config.isFeatureEnabled(FeatureObjectType.FONT) ? parseFontFromResources(resources)
					: null;
			Set<String> propertiesChild = config.isFeatureEnabled(FeatureObjectType.PROPERTIES)
					? parsePropertiesFromResources(resources) : null;

			reporter.report(PBFeaturesObjectCreator.createPageFeaturesObject(page, thumbID, annotsId, extGStateChild,
					colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild,
					pageTree.indexOf(page) + 1));
		}
	}

	private Set<String> addAnnotsDependencies(PDPage page) {

		COSArray annotsArray = (COSArray) page.getCOSObject().getDictionaryObject(COSName.ANNOTS);

		if (annotsArray == null) {
			return Collections.emptySet();
		}
		Set<String> annotsId = new HashSet<>();

		for (int i = 0; i < annotsArray.size(); ++i) {
			COSBase item = annotsArray.get(i);
			if (item != null) {
				String id = getId(item, FeatureObjectType.ANNOTATION);
				annotsId.add(id);
				if (checkIDBeforeProcess(id)) {
					COSBase base = getBase(item);
					try {
						PDAnnotation annotation = PDAnnotation.createAnnotation(base);
						COSBase pop = annotation.getCOSObject().getItem(COSName.getPDFName("Popup"));

						String popupID = null;
						if (pop != null) {
							popupID = addPopup(pop);
						}

						Set<String> formsIDs = getAnnotationResourcesDependencies(annotation);
						popupID = config.isFeatureEnabled(FeatureObjectType.ANNOTATION) ? popupID : null;
						formsIDs = config.isAnyFeatureEnabled(XOBJECTS) ? formsIDs : null;
						reporter.report(
								PBFeaturesObjectCreator.createAnnotFeaturesObject(annotation, id, popupID, formsIDs));
					} catch (IOException e) {
						LOGGER.debug("Unknown annotation type detected.", e);
						generateUnknownAnnotation(id);
					}
				}
			}
		}

		return annotsId;
	}

	private String addPopup(COSBase item) {
		String id = getId(item, FeatureObjectType.ANNOTATION);

		if (checkIDBeforeProcess(id)) {
			COSBase base = getBase(item);
			try {
				PDAnnotation annotation = PDAnnotation.createAnnotation(base);
				reporter.report(PBFeaturesObjectCreator.createAnnotFeaturesObject(annotation, id, null, null));
			} catch (IOException e) {
				LOGGER.debug("Unknown annotation type detected.", e);
				generateUnknownAnnotation(id);
			}
		}
		return id;
	}

	private Set<String> getAnnotationResourcesDependencies(PDAnnotation annot) {
		PDAppearanceDictionary dic = annot.getAppearance();
		Set<String> appearances = new HashSet<>();

		if (dic != null) {
			COSBase baseNormal = dic.getCOSObject().getItem(COSName.N);
			if (baseNormal != null) {
				appearances.addAll(getAppearanceEntryDependencies(dic.getNormalAppearance(), baseNormal));
			}

			COSBase baseRollover = dic.getCOSObject().getItem(COSName.R);
			if (baseRollover != null) {
				appearances.addAll(getAppearanceEntryDependencies(dic.getRolloverAppearance(), baseRollover));
			}

			COSBase baseDown = dic.getCOSObject().getItem(COSName.D);
			if (baseDown != null) {
				appearances.addAll(getAppearanceEntryDependencies(dic.getDownAppearance(), baseDown));
			}
		}
		return appearances;
	}

	private Set<String> getAppearanceEntryDependencies(PDAppearanceEntry entry, COSBase entryLink) {
		Set<String> res = new HashSet<>();
		if (entry.isStream()) {
			res.add(getAppearanceStreamDependencies(entry.getAppearanceStream(), entryLink));
		} else {
			for (Map.Entry<COSName, PDAppearanceStream> mapEntry : entry.getSubDictionary().entrySet()) {
				res.add(getAppearanceStreamDependencies(mapEntry.getValue(),
						((COSDictionary) entry.getCOSObject()).getItem(mapEntry.getKey())));
			}
		}
		return res;
	}

	private String getAppearanceStreamDependencies(PDAppearanceStream stream, COSBase entryLink) {
		String id = getId(entryLink, FeatureObjectType.FORM_XOBJECT);
		if (checkIDBeforeProcess(id)) {
			parseFormXObject(stream, id);
		}
		return id;
	}

	private void generateUnknownAnnotation(String id) {
		if (config.isFeatureEnabled(FeatureObjectType.ANNOTATION)) {
			FeatureTreeNode annot = FeatureTreeNode.createRootNode(FeatureObjectType.ANNOTATION.getNodeName());
			annot.setAttribute(ID, id);
			ErrorsHelper.addErrorIntoCollection(reporter.getCollection(), annot, "Unknown annotation type");
			reporter.getCollection().addNewFeatureTree(FeatureObjectType.ANNOTATION, annot);
		}
	}

	private void reportEmbeddedFiles(PDDocumentCatalog catalog) {
		int index = 0;
		PDEmbeddedFilesNameTreeNode efTree = catalog.getNames().getEmbeddedFiles();

		try {
			if (config.isFeatureEnabled(FeatureObjectType.EMBEDDED_FILE) && efTree.getNames() != null) {
				for (PDComplexFileSpecification file : efTree.getNames().values()) {
					reporter.report(PBFeaturesObjectCreator.createEmbeddedFileFeaturesObject(file, ++index));
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Error creating PDFBox SubType.", e);
			handleSubtypeCreationProblem(e.getMessage());
		}

		if (efTree.getKids() != null) {
			for (PDNameTreeNode<PDComplexFileSpecification> tree : efTree.getKids()) {
				if (tree != null) {
					index = reportEmbeddedFileNode(tree, index);
				}
			}
		}
	}

	private int reportEmbeddedFileNode(final PDNameTreeNode<PDComplexFileSpecification> node, final int index) {
		int res = index;

		try {
			if (config.isFeatureEnabled(FeatureObjectType.EMBEDDED_FILE) && node.getNames() != null) {
				for (PDComplexFileSpecification file : node.getNames().values()) {
					if (file != null) {
						reporter.report(PBFeaturesObjectCreator.createEmbeddedFileFeaturesObject(file, ++res));
					}
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Subtype creation exception caught", e);
			handleSubtypeCreationProblem(e.getMessage());
		}

		if (node.getKids() != null) {
			for (PDNameTreeNode<PDComplexFileSpecification> tree : node.getKids()) {
				res = reportEmbeddedFileNode(tree, res);
			}
		}

		return res;
	}

	private String addICCProfileFromOutputIntent(PDOutputIntent outInt) {
		COSBase outIntBase = outInt.getCOSObject();

		if (outIntBase instanceof COSDictionary) {
			COSDictionary outIntDict = (COSDictionary) outIntBase;
			String iccProfileID = getId(outIntDict.getItem(COSName.DEST_OUTPUT_PROFILE), FeatureObjectType.ICCPROFILE);
			if (checkIDBeforeProcess(iccProfileID)) {
				reporter.report(PBFeaturesObjectCreator.createICCProfileFeaturesObject(outInt.getDestOutputIntent(),
						iccProfileID));
			}
			return iccProfileID;
		}
		return null;
	}

	private void handleSubtypeCreationProblem(String errorMessage) {
		creationProblem(null, errorMessage, FeatureObjectType.EMBEDDED_FILE,
				"PBFeatureParser.reportEmbeddedFileNode logic failure.", true);
	}

	private void fontCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(nodeID, errorMessage, FeatureObjectType.FONT,
				"PBFeatureParser.fontCreationProblem logic failure.", false);
	}

	private void patternCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(nodeID, errorMessage, FeatureObjectType.PATTERN,
				"PBFeatureParser.patternCreationProblem logic failure.", false);
	}

	private void colorSpaceCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(nodeID, errorMessage, FeatureObjectType.COLORSPACE,
				"PBFeatureParser.colorSpaceCreationProblem logic failure.", false);
	}

	private void shadingCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(nodeID, errorMessage, FeatureObjectType.SHADING,
				"PBFeatureParser.shadingCreationProblem logic failure.", false);
	}

	private void xobjectCreationProblem(final String nodeID, String errorMessage) {
		creationProblem(nodeID, errorMessage, FeatureObjectType.FAILED_XOBJECT,
				"PBFeatureParser.xobjectCreationProblem logic failure.", false);
	}

	private void creationProblem(final String nodeID, final String errorMessage, final FeatureObjectType type,
			final String loggerMessage, final boolean isTypeError) {
		if (config.isFeatureEnabled(type)) {
			if (!isTypeError) {
				FeatureTreeNode node = FeatureTreeNode.createRootNode(type.getNodeName());
				if (nodeID != null) {
					node.setAttribute(ID, nodeID);
				}
				reporter.getCollection().addNewFeatureTree(type, node);
				ErrorsHelper.addErrorIntoCollection(reporter.getCollection(), node, errorMessage);
			} else {
				String id = ErrorsHelper.addErrorIntoCollection(reporter.getCollection(), null, errorMessage);
				reporter.getCollection().addNewError(type, id);

			}
		}
	}

	private Set<String> parseColorSpaceFromResources(PDResources resources) {
		if (resources == null || resources.getXObjectNames() == null) {
			return null;
		}

		Set<String> colorSpaceIDs = new HashSet<>();
		for (COSName name : resources.getColorSpaceNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.COLORSPACE);
			COSBase base = dict.getItem(name);
			String id = getId(base, FeatureObjectType.COLORSPACE);
			try {
				PDColorSpace colorSpace = resources.getColorSpace(name);
				id = checkColorSpaceID(id, colorSpace);
				colorSpaceIDs.add(id);

				if (checkIDBeforeProcess(id)) {
					parseColorSpace(colorSpace, id);
				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(id, e.getMessage());
			}
		}
		return colorSpaceIDs;
	}

	private Set<String> parseXObjectFromResources(PDResources resources) {
		if (resources == null || resources.getXObjectNames() == null) {
			return null;
		}

		Set<String> xobjectsIDs = new HashSet<>();
		for (COSName name : resources.getXObjectNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.XOBJECT);
			COSBase base = dict.getItem(name);

			String id = getId(base, FeatureObjectType.IMAGE_XOBJECT);
			xobjectsIDs.add(id);
			if (checkIDBeforeProcess(id)) {
				try {
					PDXObject xobj = resources.getXObject(name);

					if (xobj instanceof PDImageXObjectProxy) {
						parseImageXObject((PDImageXObjectProxy) xobj, id);
					} else if (xobj instanceof PDFormXObject) {
						parseFormXObject((PDFormXObject) xobj, id);
					} else if (xobj instanceof PDPostScriptXObject) {
						reporter.report(PBFeaturesObjectCreator.createPostScriptXObjectFeaturesObject(id));
					}
				} catch (IOException e) {
					LOGGER.info(e);
					xobjectCreationProblem(id, e.getMessage());
				}
			}
		}
		return xobjectsIDs;
	}

	private Set<String> parsePropertiesFromResources(PDResources resources) {
		if (resources == null || resources.getPropertiesNames() == null) {
			return null;
		}

		Set<String> propertiesIDs = new HashSet<>();
		for (COSName name : resources.getPropertiesNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.PROPERTIES);
			COSBase base = dict.getItem(name);
			String id = getId(base, FeatureObjectType.PROPERTIES);
			propertiesIDs.add(id);

			if (checkIDBeforeProcess(id)) {
				PDPropertyList property = resources.getProperties(name);
				reporter.report(
						PBFeaturesObjectCreator.createPropertiesDictFeaturesObject(property.getCOSObject(), id));
			}
		}
		return propertiesIDs;
	}

	private Set<String> parseFontFromResources(PDResources resources) {
		if (resources == null || resources.getFontNames() == null) {
			return null;
		}

		Set<String> fontIDs = new HashSet<>();
		for (COSName name : resources.getFontNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.FONT);
			COSBase base = dict.getItem(name);
			String id = getId(base, FeatureObjectType.FONT);
			fontIDs.add(id);

			if (checkIDBeforeProcess(id)) {
				try {
					PDFont font = resources.getFont(name);
					parseFont(font, id);
				} catch (IOException e) {
					LOGGER.info(e);
					fontCreationProblem(id, e.getMessage());
				}

			}
		}
		return fontIDs;
	}

	private Set<String> parseExGStateFromResource(PDResources resources) {
		if (resources == null || resources.getExtGStateNames() == null) {
			return null;
		}

		Set<String> gStatesIDs = new HashSet<>();
		for (COSName name : resources.getExtGStateNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.EXT_G_STATE);
			COSBase base = dict.getItem(name);
			String id = getId(base, FeatureObjectType.EXT_G_STATE);
			gStatesIDs.add(id);

			if (checkIDBeforeProcess(id)) {
				PDExtendedGraphicsState exGState = resources.getExtGState(name);
				parseExGState(exGState, id);
			}
		}
		return gStatesIDs;
	}

	private Set<String> parsePatternFromResource(PDResources resources) {
		if (resources == null || resources.getPatternNames() == null) {
			return null;
		}

		Set<String> patternIDs = new HashSet<>();
		for (COSName name : resources.getPatternNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.PATTERN);
			COSBase base = dict.getItem(name);

			String id = getId(base, FeatureObjectType.PATTERN);
			patternIDs.add(id);

			if (checkIDBeforeProcess(id)) {
				try {
					PDAbstractPattern pattern = resources.getPattern(name);
					parsePattern(pattern, id);
				} catch (IOException e) {
					LOGGER.info(e);
					patternCreationProblem(id, e.getMessage());
				}
			}
		}
		return patternIDs;
	}

	private Set<String> parseShadingFromResource(PDResources resources) {
		if (resources == null || resources.getShadingNames() == null) {
			return null;
		}

		Set<String> shadingIDs = new HashSet<>();
		for (COSName name : resources.getShadingNames()) {
			COSDictionary dict = (COSDictionary) resources.getCOSObject().getDictionaryObject(COSName.SHADING);
			COSBase base = dict.getItem(name);
			String id = getId(base, FeatureObjectType.SHADING);
			shadingIDs.add(id);

			if (checkIDBeforeProcess(id)) {
				try {
					PDShading shading = resources.getShading(name);
					parseShading(shading, id);
				} catch (IOException e) {
					LOGGER.info(e);
					shadingCreationProblem(id, e.getMessage());
				}
			}
		}
		return shadingIDs;
	}

	private void parseImageXObject(PDImageXObjectProxy xobj, String id) {
		COSBase baseColorSpace = ((COSStream) xobj.getCOSObject()).getItem(COSName.CS);
		if (baseColorSpace == null) {
			baseColorSpace = ((COSStream) xobj.getCOSObject()).getItem(COSName.COLORSPACE);
		}
		String idColorSpace = getId(baseColorSpace, FeatureObjectType.COLORSPACE);
		try {
			PDColorSpace colorSpace = xobj.getColorSpace();
			idColorSpace = checkColorSpaceID(idColorSpace, colorSpace);
			if (checkIDBeforeProcess(idColorSpace)) {
				parseColorSpace(colorSpace, idColorSpace);
			}
		} catch (IOException e) {
			LOGGER.info(e);
			colorSpaceCreationProblem(idColorSpace, e.getMessage());
		}

		String idMask = null;
		COSBase mask = xobj.getCOSStream().getDictionaryObject(COSName.MASK);
		if (mask instanceof COSStream) {
			COSBase maskBase = ((COSStream) xobj.getCOSObject()).getItem(COSName.MASK);
			idMask = getId(maskBase, FeatureObjectType.IMAGE_XOBJECT);
			if (checkIDBeforeProcess(idMask)) {
				try {
					PDImageXObjectProxy imxobj = xobj.getMask();
					parseImageXObject(imxobj, idMask);
				} catch (IOException e) {
					LOGGER.info(e);
					xobjectCreationProblem(idMask, e.getMessage());
				}
			}
		}

		String idSMask = null;
		COSBase sMask = xobj.getCOSStream().getDictionaryObject(COSName.SMASK);
		if (sMask instanceof COSStream) {
			COSBase sMaskBase = ((COSStream) xobj.getCOSObject()).getItem(COSName.SMASK);
			idSMask = getId(sMaskBase, FeatureObjectType.IMAGE_XOBJECT);
			if (checkIDBeforeProcess(idSMask)) {
				try {
					PDImageXObjectProxy imxobj = xobj.getSoftMask();
					parseImageXObject(imxobj, idSMask);
				} catch (IOException e) {
					LOGGER.info(e);
					xobjectCreationProblem(idSMask, e.getMessage());
				}
			}
		}

		COSBase alternates = xobj.getCOSStream().getDictionaryObject(COSName.getPDFName("Alternates"));
		alternates = getBase(alternates);
		Set<String> alternatesIDs = new HashSet<>();
		if (alternates instanceof COSArray) {
			COSArray alternatesArray = (COSArray) alternates;
			for (COSBase entry : alternatesArray) {
				COSBase base = getBase(entry);
				if (base instanceof COSDictionary) {
					COSDictionary altDict = (COSDictionary) base;
					COSBase baseImage = altDict.getItem(COSName.IMAGE);
					String idImage = getId(baseImage, FeatureObjectType.IMAGE_XOBJECT);
					baseImage = getBase(baseImage);
					if (baseImage instanceof COSStream) {
						alternatesIDs.add(idImage);
						if (checkIDBeforeProcess(idImage)) {
							PDImageXObjectProxy im = new PDImageXObjectProxy(new PDStream((COSStream) baseImage), null);
							parseImageXObject(im, idImage);
						}
					}
				}
			}
		}

		idColorSpace = config.isFeatureEnabled(FeatureObjectType.COLORSPACE) ? idColorSpace : null;
		if (!config.isAnyFeatureEnabled(XOBJECTS)) {
			idMask = null;
			idSMask = null;
			alternatesIDs = null;
		}

		reporter.report(PBFeaturesObjectCreator.createImageXObjectFeaturesObject(xobj, id, idColorSpace, idMask,
				idSMask, alternatesIDs));
	}

	private void parseFormXObject(PDFormXObject xobj, String id) {

		PDGroup group = xobj.getGroup();
		String idColorSpace = null;
		if (group != null && COSName.TRANSPARENCY.equals(group.getSubType())) {
			COSBase baseColorSpace = group.getCOSObject().getItem(COSName.CS);
			idColorSpace = getId(baseColorSpace, FeatureObjectType.COLORSPACE);
			try {
				PDColorSpace colorSpace = group.getColorSpace();
				idColorSpace = checkColorSpaceID(idColorSpace, colorSpace);
				if (checkIDBeforeProcess(idColorSpace)) {
					parseColorSpace(colorSpace, idColorSpace);
				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(idColorSpace, e.getMessage());
			}
		}

		PDResources resources = xobj.getResources();
		Set<String> extGStateChild = parseExGStateFromResource(resources);
		extGStateChild = config.isFeatureEnabled(FeatureObjectType.EXT_G_STATE) ? extGStateChild : null;
		Set<String> colorSpaceChild = parseColorSpaceFromResources(resources);
		if (!config.isFeatureEnabled(FeatureObjectType.COLORSPACE)) {
			idColorSpace = null;
			colorSpaceChild = null;
		}
		Set<String> patternChild = config.isFeatureEnabled(FeatureObjectType.PATTERN)
				? parsePatternFromResource(resources) : null;
		Set<String> shadingChild = config.isFeatureEnabled(FeatureObjectType.SHADING)
				? parseShadingFromResource(resources) : null;
		Set<String> xobjectChild = config.isAnyFeatureEnabled(XOBJECTS) ? parseXObjectFromResources(resources) : null;
		Set<String> fontChild = config.isFeatureEnabled(FeatureObjectType.FONT) ? parseFontFromResources(resources)
				: null;
		Set<String> propertiesChild = config.isFeatureEnabled(FeatureObjectType.PROPERTIES)
				? parsePropertiesFromResources(resources) : null;

		reporter.report(PBFeaturesObjectCreator.createFormXObjectFeaturesObject(xobj, id, idColorSpace, extGStateChild,
				colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild));

	}

	private void parseExGState(PDExtendedGraphicsState exGState, String id) {
		String childFontID = null;
		if (exGState.getFontSetting() != null && exGState.getFontSetting().getCOSObject() instanceof COSArray) {
			childFontID = getId(((COSArray) exGState.getFontSetting().getCOSObject()).get(0), FeatureObjectType.FONT);
			if (checkIDBeforeProcess(childFontID)) {
				try {
					PDFont font = exGState.getFontSetting().getFont();
					parseFont(font, childFontID);
				} catch (IOException e) {
					LOGGER.info(e);
					fontCreationProblem(childFontID, e.getMessage());
				}
			}
		}

		childFontID = config.isFeatureEnabled(FeatureObjectType.FONT) ? childFontID : null;
		reporter.report(PBFeaturesObjectCreator.createExtGStateFeaturesObject(exGState, id, childFontID));
	}

	private void parsePattern(PDAbstractPattern pattern, String id) throws IOException {
		if (pattern instanceof PDTilingPattern) {
			PDTilingPattern tilingPattern = (PDTilingPattern) pattern;
			PDResources resources = tilingPattern.getResources();
			Set<String> extGStateChild = config.isFeatureEnabled(FeatureObjectType.EXT_G_STATE)
					? parseExGStateFromResource(resources) : null;
			Set<String> colorSpaceChild = config.isFeatureEnabled(FeatureObjectType.COLORSPACE)
					? parseColorSpaceFromResources(resources) : null;
			Set<String> patternChild = config.isFeatureEnabled(FeatureObjectType.PATTERN)
					? parsePatternFromResource(resources) : null;
			Set<String> shadingChild = config.isFeatureEnabled(FeatureObjectType.SHADING)
					? parseShadingFromResource(resources) : null;
			Set<String> xobjectChild = config.isAnyFeatureEnabled(XOBJECTS) ? parseXObjectFromResources(resources)
					: null;
			Set<String> fontChild = config.isFeatureEnabled(FeatureObjectType.FONT) ? parseFontFromResources(resources)
					: null;
			Set<String> propertiesChild = config.isFeatureEnabled(FeatureObjectType.PROPERTIES)
					? parsePropertiesFromResources(resources) : null;

			reporter.report(PBFeaturesObjectCreator.createTilingPatternFeaturesObject(tilingPattern, id, extGStateChild,
					colorSpaceChild, patternChild, shadingChild, xobjectChild, fontChild, propertiesChild));
		} else {
			PDShadingPattern shadingPattern = (PDShadingPattern) pattern;
			COSBase baseShading = shadingPattern.getCOSObject().getItem(COSName.SHADING);
			String shadingID = getId(baseShading, FeatureObjectType.SHADING);

			if (checkIDBeforeProcess(shadingID) && shadingPattern.getShading() != null) {
				parseShading(shadingPattern.getShading(), shadingID);
			}

			COSBase baseExGState = shadingPattern.getCOSObject().getItem(COSName.EXT_G_STATE);
			String exGStateID = getId(baseExGState, FeatureObjectType.EXT_G_STATE);

			if (checkIDBeforeProcess(exGStateID) && shadingPattern.getExtendedGraphicsState() != null) {
				parseExGState(shadingPattern.getExtendedGraphicsState(), exGStateID);
			}

			shadingID = config.isFeatureEnabled(FeatureObjectType.SHADING) ? shadingID : null;
			exGStateID = config.isFeatureEnabled(FeatureObjectType.EXT_G_STATE) ? exGStateID : null;
			reporter.report(PBFeaturesObjectCreator.createShadingPatternFeaturesObject(shadingPattern, id, shadingID,
					exGStateID));
		}
	}

	private void parseShading(PDShading shading, String id) {
		COSBase base = shading.getCOSObject().getItem(COSName.CS);
		if (base == null) {
			base = shading.getCOSObject().getItem(COSName.COLORSPACE);
		}
		String colorspaceID = getId(base, FeatureObjectType.COLORSPACE);
		try {
			PDColorSpace colorSpace = shading.getColorSpace();

			colorspaceID = checkColorSpaceID(colorspaceID, colorSpace);
			if (checkIDBeforeProcess(colorspaceID)) {
				parseColorSpace(colorSpace, colorspaceID);
			}
		} catch (IOException e) {
			LOGGER.info(e);
			colorSpaceCreationProblem(colorspaceID, e.getMessage());
		}
		colorspaceID = config.isFeatureEnabled(FeatureObjectType.COLORSPACE) ? colorspaceID : null;
		reporter.report(PBFeaturesObjectCreator.createShadingFeaturesObject(shading, id, colorspaceID));
	}

	private void parseFont(PDFontLike font, String id) {
		if (font instanceof PDType3Font) {
			PDResources resources = ((PDType3Font) font).getResources();
			Set<String> extGStateChild = config.isFeatureEnabled(FeatureObjectType.EXT_G_STATE)
					? parseExGStateFromResource(resources) : null;
			Set<String> colorSpaceChild = config.isFeatureEnabled(FeatureObjectType.COLORSPACE)
					? parseColorSpaceFromResources(resources) : null;
			Set<String> patternChild = config.isFeatureEnabled(FeatureObjectType.PATTERN)
					? parsePatternFromResource(resources) : null;
			Set<String> shadingChild = config.isFeatureEnabled(FeatureObjectType.SHADING)
					? parseShadingFromResource(resources) : null;
			Set<String> xobjectChild = config.isAnyFeatureEnabled(XOBJECTS) ? parseXObjectFromResources(resources)
					: null;
			Set<String> fontChild = config.isFeatureEnabled(FeatureObjectType.FONT) ? parseFontFromResources(resources)
					: null;
			Set<String> propertiesChild = config.isFeatureEnabled(FeatureObjectType.PROPERTIES)
					? parsePropertiesFromResources(resources) : null;

			reporter.report(PBFeaturesObjectCreator.createFontFeaturesObject(font, id, extGStateChild, colorSpaceChild,
					patternChild, shadingChild, xobjectChild, fontChild, propertiesChild));
		} else if (font instanceof PDType0Font) {
			PDType0Font type0 = (PDType0Font) font;

			COSBase descendantFontsBase = type0.getCOSObject().getDictionaryObject(COSName.DESCENDANT_FONTS);
			if (descendantFontsBase instanceof COSArray) {
				COSBase descendantFontDictionaryBase = ((COSArray) descendantFontsBase).getObject(0);
				String descendantID = getId(descendantFontDictionaryBase, FeatureObjectType.FONT);
				if (checkIDBeforeProcess(descendantID)) {
					parseFont(type0.getDescendantFont(), descendantID);
				}
				Set<String> descendant = null;
				if (config.isFeatureEnabled(FeatureObjectType.FONT)) {
					descendant = new HashSet<>();
					descendant.add(descendantID);
				}
				reporter.report(PBFeaturesObjectCreator.createFontFeaturesObject(font, id, null, null, null, null, null,
						descendant, null));
			}
		} else {
			reporter.report(PBFeaturesObjectCreator.createFontFeaturesObject(font, id, null, null, null, null, null,
					null, null));
		}
	}

	private void parseColorSpace(PDColorSpace colorSpace, String id) {
		String iccProfileID = null;
		String idAlt = null;
		if (colorSpace instanceof PDICCBased) {
			PDICCBased iccBased = (PDICCBased) colorSpace;
			COSArray array = (COSArray) iccBased.getCOSObject();
			COSBase base = array.get(1);
			iccProfileID = getId(base, FeatureObjectType.ICCPROFILE);

			if (checkIDBeforeProcess(iccProfileID)) {
				reporter.report(PBFeaturesObjectCreator
						.createICCProfileFeaturesObject(iccBased.getPDStream().getStream(), iccProfileID));
			}

			COSBase baseAlt = iccBased.getPDStream().getStream().getItem(COSName.ALTERNATE);
			idAlt = getId(baseAlt, FeatureObjectType.COLORSPACE);

			try {
				PDColorSpace altclr = iccBased.getAlternateColorSpace();
				idAlt = checkColorSpaceID(idAlt, altclr);
				if (checkIDBeforeProcess(idAlt)) {
					parseColorSpace(iccBased.getAlternateColorSpace(), idAlt);

				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(idAlt, e.getMessage());
			}
		} else if (colorSpace instanceof PDIndexed || colorSpace instanceof PDSeparation
				|| colorSpace instanceof PDDeviceN) {

			int number = (colorSpace instanceof PDIndexed) ? 1 : 2;

			COSArray array = (COSArray) colorSpace.getCOSObject();
			COSBase base = array.get(number);
			idAlt = getId(base, FeatureObjectType.COLORSPACE);

			try {
				PDColorSpace alt;
				if (colorSpace instanceof PDIndexed) {
					alt = ((PDIndexed) colorSpace).getBaseColorSpace();
				} else if (colorSpace instanceof PDSeparation) {
					alt = ((PDSeparation) colorSpace).getAlternateColorSpace();
				} else {
					alt = ((PDDeviceN) colorSpace).getAlternateColorSpace();
				}

				idAlt = checkColorSpaceID(idAlt, alt);

				if (checkIDBeforeProcess(idAlt)) {
					parseColorSpace(alt, idAlt);
				}
			} catch (IOException e) {
				LOGGER.info(e);
				colorSpaceCreationProblem(idAlt, e.getMessage());
			}
		}
		iccProfileID = config.isFeatureEnabled(FeatureObjectType.ICCPROFILE) ? iccProfileID : null;
		idAlt = config.isFeatureEnabled(FeatureObjectType.COLORSPACE) ? idAlt : null;
		reporter.report(PBFeaturesObjectCreator.createColorSpaceFeaturesObject(colorSpace, id, iccProfileID, idAlt));
	}

	private static String checkColorSpaceID(String prevID, PDColorSpace colorSpace) {
		String id = prevID;
		if (colorSpace instanceof PDDeviceGray) {
			id = DEVICEGRAY_ID;
		} else if (colorSpace instanceof PDDeviceRGB) {
			id = DEVICERGB_ID;
		} else if (colorSpace instanceof PDDeviceCMYK) {
			id = DEVICECMYK_ID;
		}
		return id;
	}

	private static COSBase getBase(final COSBase base) {
		COSBase item = base;

		while (item instanceof COSObject) {
			item = ((COSObject) item).getObject();
		}

		return item;
	}

	private String getId(final COSBase base, final FeatureObjectType objType) {
		if (base == null) {
			return null;
		}
		long numb = this.processedIDs.size();
		COSBase item = base;
		String type = "Dir";

		while (item instanceof COSObject) {
			numb = ((COSObject) item).getObjectNumber();
			type = "Indir";
			item = ((COSObject) item).getObject();
		}

		return objType.getIdPrefix() + type + numb;
	}

	private boolean checkIDBeforeProcess(String id) {
		if (id == null || this.processedIDs.contains(id)) {
			return false;
		}
		this.processedIDs.add(id);
		return true;
	}
}
