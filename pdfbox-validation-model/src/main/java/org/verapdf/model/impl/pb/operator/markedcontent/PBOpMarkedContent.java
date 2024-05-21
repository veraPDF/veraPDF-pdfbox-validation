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
package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosActualText;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosLang;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosActualText;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.cos.PBCosLang;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpMarkedContent;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for marked content operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpMarkedContent extends PBOperator implements
        OpMarkedContent {

	private final PDDocument document;
	private final PDFAFlavour flavour;

	/** Name of link to the tag name */
    public static final String TAG = "tag";
	/** Name of link to the properties dictionary */
    public static final String PROPERTIES = "properties";
	/** Name of link to Lang value from the properties dictionary */
	public static final String LANG = "Lang";
	/** Name of link to ActualText value from the properties dictionary */
	public static final String ACTUAL_TEXT = "actualText";
	public static final String ALT = "alt";

	public PBOpMarkedContent(List<COSBase> arguments, final String opType, PDDocument document, PDFAFlavour flavour) {
        super(arguments, opType);
		this.document = document;
		this.flavour = flavour;
    }

    protected List<CosName> getTag() {
        if (this.arguments.size() > 1) {
			COSBase name = this.arguments
					.get(this.arguments.size() - 2);
			if (name instanceof COSName) {
				List<CosName> list =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosName((COSName) name));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }


	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ACTUAL_TEXT:
				return this.getactualText();
			case ALT:
				return Collections.emptyList();				
			default:
				return super.getLinkedObjects(link);
		}
	}

    protected List<CosDict> getPropertiesDict() {
        if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments
					.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				List<CosDict> list =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosDict((COSDictionary) dict, document, flavour));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }

	protected List<CosLang> getLang() {
		if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments
					.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				COSBase baseLang = ((COSDictionary) dict).getDictionaryObject(COSName.LANG);
				if (baseLang instanceof COSString) {
					List<CosLang> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					list.add(new PBCosLang((COSString) baseLang));
					return Collections.unmodifiableList(list);
				}
			}
		}
		return Collections.emptyList();
	}

	private List<CosActualText> getactualText() {
		if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				COSBase actualText = ((COSDictionary) dict).getDictionaryObject(COSName.ACTUAL_TEXT);
				if (actualText instanceof COSString) {
					List<CosActualText> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					list.add(new PBCosActualText((COSString) actualText));
					return Collections.unmodifiableList(list);
				}
			}
		}
		return Collections.emptyList();
	}
}
