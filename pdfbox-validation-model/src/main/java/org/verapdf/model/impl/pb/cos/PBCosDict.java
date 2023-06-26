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
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.impl.pb.pd.PBoxPDMetadata;
import org.verapdf.model.pdlayer.PDMetadata;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Current class is representation of CosDict interface of abstract model. This
 * class is analogue of COSDictionary in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosDict extends PBCosObject implements CosDict {

    public static final String COS_DICT_TYPE = "CosDict";

    public static final String KEYS = "keys";
    public static final String VALUES = "values";
    public static final String METADATA = "metadata";

    private final int size;

    protected final PDDocument document;
    protected final PDFAFlavour flavour;

    /**
     * Default constructor
     *
     * @param dictionary pdfbox COSDictionary
     */
    public PBCosDict(COSDictionary dictionary, PDDocument document, PDFAFlavour flavour) {
        this(dictionary, COS_DICT_TYPE, document, flavour);
    }

    /**
     * Constructor used by child classes
     *
     * @param dictionary pdfbox COSDictionary
     * @param type       type of child class
     */
    protected PBCosDict(COSDictionary dictionary, final String type, final PDDocument document, final PDFAFlavour flavour) {
        super(dictionary, type);
        this.size = dictionary.size();
        this.document = document;
        this.flavour = flavour;
    }

    /**
     * Get number of key/value pairs in the dictionary
     */
    @Override
    public Long getsize() {
        return Long.valueOf(this.size);
    }

    @Override
    public String getkeysString() {
        return ((COSDictionary)this.baseObject).keySet().stream()
                .map(COSName::getName)
                .collect(Collectors.joining("&"));
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        switch (link) {
            case KEYS:
                return this.getKeys();
            case VALUES:
                return this.getValues();
            case METADATA:
                return this.getMetadata();
            default:
                return super.getLinkedObjects(link);
        }
    }

    /**
     * Get all keys of the dictionary
     */
    private List<CosName> getKeys() {
        COSDictionary dictionary = (COSDictionary) this.baseObject;
        List<CosName> list = new ArrayList<>(dictionary.size());
        for (COSName key : dictionary.keySet()) {
            if (key != null) {
                list.add((CosName) getFromValue(key, this.document, this.flavour));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Get all values of the dictionary
     */
    private List<CosObject> getValues() {
        COSDictionary dictionary = (COSDictionary) this.baseObject;
        List<CosObject> list = new ArrayList<>(dictionary.size());
        for (COSBase value : dictionary.getValues()) {
            if (value != null) {
                list.add(getFromValue(value, this.document, this.flavour));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Get XMP metadata if it is present
     */
    private List<PDMetadata> getMetadata() {

        COSDictionary dictionary = (COSDictionary) this.baseObject;
        COSBase meta = dictionary.getDictionaryObject(COSName.METADATA);
        COSName type = dictionary.getCOSName(COSName.TYPE);
        if (PBoxPDMetadata.isMetadataObject(meta)
                && type != COSName.CATALOG) {
            ArrayList<PDMetadata> pdMetadatas = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            org.apache.pdfbox.pdmodel.common.PDMetadata md = new org.apache.pdfbox.pdmodel.common.PDMetadata(
                    (COSStream) meta);
            pdMetadatas.add(new PBoxPDMetadata(md, Boolean.FALSE, document, flavour));
            return pdMetadatas;
        }

        return Collections.emptyList();
    }
}
