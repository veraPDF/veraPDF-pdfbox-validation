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
package org.verapdf.model.impl.pb.pd.actions;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDMediaClip;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Maxim Plushchov
 */
public class PBoxPDMediaClip extends PBoxPDObject implements PDMediaClip {

    public static final String MEDIA_CLIP_TYPE = "PDMediaClip";

    public static final String CONTENT_TYPE = "CT";

    public PBoxPDMediaClip(COSDictionary simplePDObject) {
        super(simplePDObject, MEDIA_CLIP_TYPE);
    }

    @Override
    public String getCT() {
        return ((COSDictionary)simplePDObject).getNameAsString(COSName.getPDFName(CONTENT_TYPE));
    }

    @Override
    public String getAlt() {
        COSBase object = ((COSDictionary)simplePDObject).getDictionaryObject(COSName.ALT);
        List<String> list = new LinkedList<>();
        if (object instanceof COSArray) {
            for (COSBase elem : (COSArray)object) {
                if (elem instanceof COSString) {
                    list.add(((COSString) elem).getString());
                }
            }
        }
        return list.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(""));
    }

    @Override
    public Boolean gethasCorrectAlt() {
        COSBase object = ((COSDictionary)simplePDObject).getDictionaryObject(COSName.ALT);
        if (!(object instanceof COSArray)) {
            return false;
        }
        COSArray array = (COSArray)object;
        if (array.size() % 2 != 0) {
            return false;
        }
        for (int i = 0; i < array.size(); i++) {
            COSBase elem = array.get(i);
            if (!(elem instanceof COSString) || (i % 2 == 1 && ((COSString)elem).getString().isEmpty())) {
                return false;
            }
        }
        return true;
    }
}
