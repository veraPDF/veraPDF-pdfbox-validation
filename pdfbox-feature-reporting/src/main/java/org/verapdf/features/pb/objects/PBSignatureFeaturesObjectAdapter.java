/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.verapdf.features.objects.SignatureFeaturesObjectAdapter;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class PBSignatureFeaturesObjectAdapter implements SignatureFeaturesObjectAdapter {

    private PDSignature signature;

    public PBSignatureFeaturesObjectAdapter(PDSignature signature) {
        this.signature = signature;
    }

    @Override
    public String getFilter() {
        if (this.signature != null) {
            return this.signature.getFilter();
        }
        return null;
    }

    @Override
    public String getSubFilter() {
        if (this.signature != null) {
            return this.signature.getSubFilter();
        }
        return null;
    }

    @Override
    public String getHexContents() {
        if (this.signature != null) {
            byte[] cont = signature.getContents();
            if (cont != null) {
                return DatatypeConverter.printHexBinary(cont);
            }
        }
        return null;
    }

    @Override
    public String getName() {
        if (this.signature != null) {
            return this.signature.getName();
        }
        return null;
    }

    @Override
    public Calendar getSignDate() {
        if (this.signature != null) {
            return this.signature.getSignDate();
        }
        return null;
    }

    @Override
    public String getLocation() {
        if (this.signature != null) {
            return this.signature.getLocation();
        }
        return null;
    }

    @Override
    public String getReason() {
        if (this.signature != null) {
            return this.signature.getReason();
        }
        return null;
    }

    @Override
    public String getContactInfo() {
        if (this.signature != null) {
            return this.signature.getContactInfo();
        }
        return null;
    }

    @Override
    public InputStream getData() {
        if (this.signature != null) {
            byte[] contents = signature.getContents();
            return contents == null ? null : new ByteArrayInputStream(contents);
        }
        return null;
    }

    @Override
    public List<String> getErrors() {
        return Collections.emptyList();
    }
}
