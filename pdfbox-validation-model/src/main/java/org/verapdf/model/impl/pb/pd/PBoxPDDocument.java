/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;
import org.apache.pdfbox.pdmodel.interactive.action.PDDocumentCatalogAdditionalActions;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosLang;
import org.verapdf.model.impl.pb.cos.PBCosLang;
import org.verapdf.model.impl.pb.pd.signatures.PBoxPDPerms;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.OutlinesHelper;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * High-level representation of pdf document.
 * Implemented by Apache PDFBox
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocument extends PBoxPDObject implements PDDocument {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDDocument.class);

	/**
	 * Type name for {@code PBoxPDDocument}
	 */
	public static final String PD_DOCUMENT_TYPE = "PDDocument";

	/**
	 * Link name for pages
	 */
	public static final String PAGES = "pages";
	/**
	 * Link name for main metadata of document
	 */
	public static final String METADATA = "metadata";
	/**
	 * Link name for all output intents
	 */
	public static final String OUTPUT_INTENTS = "outputIntents";
	/**
	 * Link name for acro forms
	 */
	public static final String ACRO_FORMS = "AcroForm";
	/**
	 * Link name for additional actions of document
	 */
	public static final String ACTIONS = "AA";
	/**
	 * Link name for open action of document
	 */
	public static final String OPEN_ACTION = "OpenAction";
	/**
	 * Link name for all outlines of document
	 */
	public static final String OUTLINES = "Outlines";
	/**
	 * Link name for annotations structure tree root of document
	 */
	public static final String STRUCTURE_TREE_ROOT = "StructTreeRoot";
	/**
	 * Link name for alternate presentation of names tree of document
	 */
	public static final String ALTERNATE_PRESENTATIONS = "AlternatePresentations";
	/**
	 * Link name for optional content properties of the document
	 */
	public static final String OC_PROPERTIES = "OCProperties";
	/**
	 * Name of link to Lang value from the document catalog dictionary
	 */
	public static final String LANG = "Lang";
	/**
	 * Name of link to Perms value
	 */
	public static final String PERMS = "Perms";

	/**
	 * Maximal number of additional actions for AA key
	 */
	public static final int MAX_NUMBER_OF_ACTIONS = 5;

	private final PDDocumentCatalog catalog;
	private final PDFAFlavour flavour;

	/**
	 * Default constructor
	 *
	 * @param document high level document representation
	 */
	public PBoxPDDocument(org.apache.pdfbox.pdmodel.PDDocument document, PDFAFlavour flavour) {
		super(document, PD_DOCUMENT_TYPE);
		this.catalog = this.document.getDocumentCatalog();
		this.flavour = flavour;
	}

	@Override
	public Boolean getcontainsAA() {
		return this.catalog != null && this.catalog.getCOSObject().containsKey(COSName.AA);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case OUTLINES:
				return this.getOutlines();
			case OPEN_ACTION:
				return this.getOpenAction();
			case ACTIONS:
				return this.getActions();
			case PAGES:
				return this.getPages();
			case METADATA:
				return this.getMetadata();
			case OUTPUT_INTENTS:
				return this.getOutputIntents();
			case ACRO_FORMS:
				return this.getAcroForms();
			case STRUCTURE_TREE_ROOT:
				return this.getStructureTreeRoot();
			case OC_PROPERTIES:
				return this.getOCProperties();
			case LANG:
				return this.getLang();
			case PERMS:
				return this.getPerms();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDOutline> getOutlines() {
		return OutlinesHelper.getOutlines(this.catalog);
	}

	private List<PDAction> getOpenAction() {
		if (this.catalog != null) {
			try {
				PDDestinationOrAction openAction = this.catalog.getOpenAction();
				if (openAction instanceof org.apache.pdfbox.pdmodel.interactive.action.PDAction) {
					List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					this.addAction(actions,
							(org.apache.pdfbox.pdmodel.interactive.action.PDAction) openAction);
					return Collections.unmodifiableList(actions);
				}
			} catch (IOException e) {
				LOGGER.debug(
						"Problems with open action obtaining. " + e.getMessage(), e);
			}
		}
		return Collections.emptyList();
	}

	private List<PDAction> getActions() {
		PDDocumentCatalogAdditionalActions pbActions = this.getAdditionalAction();
		if (pbActions != null) {
			List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);

			org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

			buffer = pbActions.getDP();
			this.addAction(actions, buffer);

			buffer = pbActions.getDS();
			this.addAction(actions, buffer);

			buffer = pbActions.getWP();
			this.addAction(actions, buffer);

			buffer = pbActions.getWS();
			this.addAction(actions, buffer);

			buffer = pbActions.getWC();
			this.addAction(actions, buffer);

			return Collections.unmodifiableList(actions);
		}
		return Collections.emptyList();
	}

	private PDDocumentCatalogAdditionalActions getAdditionalAction() {
		if (this.catalog != null) {
			COSDictionary catalogLocal = this.catalog.getCOSObject();
			COSBase aaDictionary = catalogLocal.getDictionaryObject(COSName.AA);
			if (aaDictionary instanceof COSDictionary) {
				return new PDDocumentCatalogAdditionalActions((COSDictionary) aaDictionary);
			}
		}
		return null;
	}

	private List<PDPage> getPages() {
		PDPageTree pageTree = this.document.getPages();
		List<PDPage> pages = new ArrayList<>(pageTree.getCount());
		for (org.apache.pdfbox.pdmodel.PDPage page : pageTree) {
			pages.add(new PBoxPDPage(page, this.document, this.flavour));
		}
		return Collections.unmodifiableList(pages);
	}

	private List<PDMetadata> getMetadata() {
		if (this.catalog != null) {
			org.apache.pdfbox.pdmodel.common.PDMetadata meta = this.catalog.getMetadata();
			if (meta != null && PBoxPDMetadata.isMetadataObject(meta.getCOSObject())) {
				List<PDMetadata> metadata = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				metadata.add(new PBoxPDMetadata(meta, Boolean.TRUE, this.document, this.flavour));
				return Collections.unmodifiableList(metadata);
			}
		}
		return Collections.emptyList();
	}

	private List<PDOutputIntent> getOutputIntents() {
		if (this.catalog != null) {
			List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
					this.catalog.getOutputIntents();
			List<PDOutputIntent> outputIntents = new ArrayList<>(pdfboxOutputIntents.size());
			for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent : pdfboxOutputIntents) {
				outputIntents.add(new PBoxPDOutputIntent(intent, this.document, this.flavour));
			}
			return Collections.unmodifiableList(outputIntents);
		}
		return Collections.emptyList();
	}

	private List<PDAcroForm> getAcroForms() {
		if (this.catalog != null) {
			org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form =
					this.catalog.getAcroForm();
			if (form != null) {
				List<PDAcroForm> forms = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				forms.add(new PBoxPDAcroForm(form, this.document, this.flavour));
				return Collections.unmodifiableList(forms);
			}
		}
		return Collections.emptyList();
	}

	private List<PDStructTreeRoot> getStructureTreeRoot() {
		if (this.catalog != null) {
			PDStructureTreeRoot root = this.catalog.getStructureTreeRoot();
			if (root != null) {
				List<PDStructTreeRoot> treeRoot = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				treeRoot.add(new PBoxPDStructTreeRoot(root, this.flavour));
				return Collections.unmodifiableList(treeRoot);
			}
		}
		return Collections.emptyList();
	}

	private List<PDPerms> getPerms() {
		if(this.catalog != null) {
			COSDictionary perms = (COSDictionary)
					this.catalog.getCOSObject().getDictionaryObject(COSName.PERMS);
			if (perms != null) {
				List<PDPerms> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBoxPDPerms(perms));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

	@Override
	public Boolean getcontainsAlternatePresentations() {
		if (this.catalog != null) {
			COSDictionary rawCatalog = this.catalog.getCOSObject();

			COSDictionary namesDictionary = (COSDictionary) rawCatalog.getDictionaryObject(COSName.NAMES);
			if (namesDictionary != null) {

				COSBase alternatePresentations = namesDictionary.getDictionaryObject(COSName.getPDFName(ALTERNATE_PRESENTATIONS));

				if (alternatePresentations != null) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean getvalidPDF() {
		// TODO: implement me
		return Boolean.TRUE;
	}

	private List<PDOCProperties> getOCProperties() {
		if (this.catalog != null) {
			PDOptionalContentProperties pBoxOCProperties = this.catalog.getOCProperties();
			if (pBoxOCProperties != null) {
				List<PDOCProperties> result = new ArrayList<>();

				PDOCProperties ocProperties = new PBoxPDOCProperties(pBoxOCProperties);
				result.add(ocProperties);

				return result;
			}
		}
		return Collections.emptyList();
	}

	private List<CosLang> getLang() {
		if (this.catalog != null) {
			COSBase baseLang = catalog.getCOSObject().getDictionaryObject(COSName.LANG);
			if (baseLang instanceof COSString) {
				List<CosLang> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosLang((COSString) baseLang));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}

}
