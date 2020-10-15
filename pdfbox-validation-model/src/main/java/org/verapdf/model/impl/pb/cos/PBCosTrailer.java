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
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.impl.pb.pd.PBoxPDEncryption;
import org.verapdf.model.pdlayer.PDEncryption;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Trailer of the document.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

    /** Type name for PBCosTrailer */
    public static final String COS_TRAILER_TYPE = "CosTrailer";

    public static final String CATALOG = "Catalog";
    public static final String ENCRYPT = "Encrypt";

    private final boolean isEncrypted;

    /**
     * Default constructor
     * @param dictionary pdfbox COSDictionary
     */
    public PBCosTrailer(COSDictionary dictionary, PDDocument document, PDFAFlavour flavour) {
        super(dictionary, COS_TRAILER_TYPE, document, flavour);
        this.isEncrypted = dictionary.getItem(COSName.ENCRYPT) != null;
    }

    /**
     * @return true if the current document is encrypted
     */
    @Override
    public Boolean getisEncrypted() {
        return Boolean.valueOf(this.isEncrypted);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case CATALOG:
                return this.getCatalog();
            case ENCRYPT:
                return this.getEncrypt();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CosIndirect> getCatalog() {
        List<CosIndirect> catalog = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        COSBase base = ((COSDictionary) this.baseObject)
				.getItem(COSName.ROOT);
        catalog.add(new PBCosIndirect((COSObject) base, this.document, this.flavour));
        return Collections.unmodifiableList(catalog);
    }

    private List<PDEncryption> getEncrypt() {
        COSBase base = ((COSDictionary)this.baseObject).getDictionaryObject(COSName.ENCRYPT);
        if (base != null && base instanceof COSDictionary) {
            List<PDEncryption> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            list.add(new PBoxPDEncryption((COSDictionary)base));
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

}
