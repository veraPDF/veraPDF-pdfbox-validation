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
package org.verapdf.model.impl.pb.pd.signatures;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdfparser.SignatureParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.PKCSDataObject;
import org.verapdf.model.impl.pb.external.PBoxPKCSDataObject;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDSigRef;
import org.verapdf.model.pdlayer.PDSignature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPDSignature extends PBoxPDObject implements PDSignature {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDSignature.class);

    /**
     * Type name for {@code PBoxPDSignature}
     */
    public static final String SIGNATURE_TYPE = "PDSignature";

    public static final String CONTENTS = "Contents";
    public static final String REFERENCE = "Reference";

    protected static byte[] contents;
    protected long signatureOffset;

    /**
     * @param pdSignature {@link org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature}
     *                    object.
     * @param document    {@link PDDocument} containing representation of initial PDF file.
     */
    public PBoxPDSignature(org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature pdSignature,
                           PDDocument document, COSObject signatureReference) {
        super(pdSignature, SIGNATURE_TYPE);
        this.document = document;
        COSObjectKey key = new COSObjectKey(signatureReference);
        signatureOffset = (this.document.getDocument().getXrefTable().get(key)).longValue();
        contents = ((org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature)
                this.simplePDObject).getContents();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case CONTENTS:
                return getContents();
            case REFERENCE:
                return getSigRefs();
            default:
                return super.getLinkedObjects(link);
        }
    }

    /**
     * @return true if byte range covers entire document except for Contents
     * entry in signature dictionary
     */
    @Override
    public Boolean getdoesByteRangeCoverEntireDocument() {
        try {
            SignatureParser parser = new SignatureParser(this.document.getPdfSource(),
                    this.document.getDocument());
            long[] actualByteRange =
                    parser.getByteRangeBySignatureOffset(signatureOffset);
            int[] byteRange = ((org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature)
                    this.simplePDObject).getByteRange();
            for (int i = 0; i < 3; ++i) {
                if (byteRange[i] != actualByteRange[i]) {
                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        } catch (IOException ex) {
            LOGGER.debug("Can't create parser to process digital signature", ex);
            return Boolean.FALSE;
        }
    }

    private static List<PKCSDataObject> getContents() {
        if (contents != null) {
            List<PKCSDataObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            list.add(new PBoxPKCSDataObject(new COSString(contents)));
            return Collections.unmodifiableList(list);
        }
        return Collections.emptyList();
    }

    private List<PDSigRef> getSigRefs() {
        COSArray reference = (COSArray)
                ((org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature)
                        this.simplePDObject).getCOSObject().getDictionaryObject(REFERENCE);
        if (reference == null || reference.size() == 0) {
            return Collections.emptyList();
        }
        List<PDSigRef> list = new ArrayList<>();
        for (COSBase sigRef : reference) {
            list.add(new PBoxPDSigRef((COSDictionary) sigRef, this.document));
        }
        return Collections.unmodifiableList(list);
    }
}
