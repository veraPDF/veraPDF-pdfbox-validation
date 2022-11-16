/**
 * This file is part of veraPDF Validation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Validation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Validation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Validation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.OutputIntents;
import org.verapdf.model.pdlayer.PDOutputIntent;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBoxOutputIntents extends GenericModelObject implements OutputIntents {

    public static final String OUTPUT_INTENTS_TYPE = "OutputIntents";

    public static final String OUTPUT_INTENTS = "outputIntents";

    List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> outInts = null;
    List<PDOutputIntent> pbOutInts = null;

    private final PDDocument document;
    private final PDFAFlavour flavour;

    public PBoxOutputIntents(List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> outInts, PDDocument document, PDFAFlavour flavour) {
        super(OUTPUT_INTENTS_TYPE);
        this.outInts = outInts;
        this.document = document;
        this.flavour = flavour;
    }

    @Override
    public Boolean getsameOutputProfileIndirect() {
        if (pbOutInts == null) {
            pbOutInts = parseOutputIntents();
        }
        String destOutputProfileIndirect = null;
        for (PDOutputIntent outputIntent : pbOutInts) {
            String currentOutputProfile = outputIntent.getdestOutputProfileIndirect();
            if (destOutputProfileIndirect != null && currentOutputProfile != null &&
                    !destOutputProfileIndirect.equals(currentOutputProfile)) {
                return false;
            }
            destOutputProfileIndirect = destOutputProfileIndirect == null ? currentOutputProfile : destOutputProfileIndirect;
        }
        return true;
    }

    @Override
    public String getoutputProfileIndirects() {
        return null;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case OUTPUT_INTENTS:
                return this.getOutputIntents();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<PDOutputIntent> parseOutputIntents() {
        List<PDOutputIntent> res = new ArrayList<>(outInts.size());
        for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent outInt : outInts) {
            res.add(new PBoxPDOutputIntent(outInt));
        }
        return res;
    }

    private List<PDOutputIntent> getOutputIntents() {
        if (pbOutInts == null) {
            pbOutInts = parseOutputIntents();
        }
        return pbOutInts;
    }

    public String getColorSpace() {
        if (pbOutInts == null) {
            pbOutInts = parseOutputIntents();
        }
        for (PDOutputIntent outputIntent : pbOutInts) {
            String colorSpace = ((PBoxPDOutputIntent)outputIntent).getColorSpace();
            if (colorSpace != null) {
                return colorSpace;
            }
        }
        return null;
    }
}
