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
package org.verapdf.model.impl.pb.operator.color;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.operator.OpColor;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for pdf color operators like CS, cs, SCN,
 * scn, G, g, RG, rg, K, k
 *
 * @author Timur Kamalov
 */
public class PBOpColor extends PBOpSetColor implements OpColor {

	/** Type name for {@code PBOpColor} */
    public static final String OP_COLOR_TYPE = "OpColor";
    public static final String COLOR_SPACE = "colorSpace";
    public static final String PATTERN_NAME = "patternName";

    private PDColorSpace colorSpace;

    public PBOpColor(List<COSBase> arguments, PDColorSpace colorSpace) {
        super(arguments, OP_COLOR_TYPE);
        this.colorSpace = colorSpace;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case COLOR_SPACE:
                return getColorSpace();
            case PATTERN_NAME:
                return getPatternName();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<CosName> getPatternName() {
        int size = this.arguments.size();
        if (size > 0) {
            COSBase cosBase = this.arguments.get(size - 1);
            if (cosBase instanceof COSName) {
                List<CosName> res = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
                res.add(new PBCosName((COSName) cosBase));
                return Collections.unmodifiableList(res);
            }
        }
        return Collections.emptyList();
    }

    private List<PDColorSpace> getColorSpace() {
        if (this.colorSpace != null) {
            List<PDColorSpace> res = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            res.add(colorSpace);
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }
}
