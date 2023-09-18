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
package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFileSpecification;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.model.impl.pb.containers.StaticContainers;
import org.verapdf.model.impl.pb.external.PBoxEmbeddedFile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent a specific type of Dictionary - File Specification Dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosDict implements CosFileSpecification {

	/** Type name for PBCosFileSpecification */
	public static final String COS_FILE_SPECIFICATION_TYPE = "CosFileSpecification";

	public static final String EF = "EF";

	private final String f;
	private final String uf;
	private final String afrelationship;

	/**
	 * Default constructor
	 * 
	 * @param dictionary
	 *            pdfbox COSDictionary
	 */
	public PBCosFileSpecification(COSDictionary dictionary, PDDocument document, PDFAFlavour flavour) {
		super(dictionary, COS_FILE_SPECIFICATION_TYPE, document, flavour);
		this.f = this.getStringValue(COSName.F);
		this.uf = this.getStringValue(COSName.UF);
		this.afrelationship = this.getNameValue(COSName.getPDFName("AFRelationship"));
	}

	@Override
	public String getF() {
		return this.f;
	}

	@Override
	public String getUF() {
		return this.uf;
	}

	@Override
	public String getAFRelationship() {
		return this.afrelationship;
	}

	@Override
	public Boolean getisAssociatedFile() {
		return Boolean.valueOf(this.baseObject != null
				&& StaticContainers.getFileSpecificationKeys().contains(this.baseObject.getKey()));
	}

	@Override
	public Boolean getcontainsEF() {
		return this.baseObject != null && this.baseObject instanceof COSDictionary &&
				((COSDictionary) this.baseObject).containsKey(COSName.EF);
	}

	@Override
	public Boolean getcontainsDesc() {
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (EF.equals(link)) {
			return this.getEFFile();
		}
		return super.getLinkedObjects(link);
	}

	private List<EmbeddedFile> getEFFile() {
		COSBase efDictionary = ((COSDictionary) this.baseObject).getDictionaryObject(COSName.EF);
		if (efDictionary instanceof COSDictionary) {
			List<EmbeddedFile> list = new ArrayList<>();
			COSDictionary dict = (COSDictionary) efDictionary;
			addEFFile(list, dict, COSName.F);
			addEFFile(list, dict, COSName.UF);
			addEFFile(list, dict, COSName.DOS);
			addEFFile(list, dict, COSName.MAC);
			addEFFile(list, dict, COSName.UNIX);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private void addEFFile(List<EmbeddedFile> list, COSDictionary dict, COSName name) {
		COSBase base = dict.getDictionaryObject(name);
		if (base != null) {
			list.add(new PBoxEmbeddedFile(base));
		}
	}

	private String getStringValue(COSName key) {
		COSBase value = ((COSDictionary) this.baseObject).getDictionaryObject(key);
		return value instanceof COSString ? ((COSString) value).getString() : null;
	}

	private String getNameValue(COSName key) {
		COSBase value = ((COSDictionary) this.baseObject).getDictionaryObject(key);
		return value instanceof COSName ? ((COSName) value).getName() : null;
	}
}
