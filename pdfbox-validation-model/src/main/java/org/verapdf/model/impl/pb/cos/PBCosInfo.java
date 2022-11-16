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

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.coslayer.CosInfo;
import org.verapdf.model.tools.XMPChecker;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class PBCosInfo extends PBCosDict implements CosInfo {

    public static final String INFORMATION_TYPE = "CosInfo";

    private static final String timeRegex = "(D:)?(\\d\\d){2,7}((([+-](\\d\\d[']))(\\d\\d['])?)?|[Z])";

    private VeraPDFMeta meta;

    public PBCosInfo(COSDictionary dictionary, PDDocument document, PDFAFlavour flavour) {
        super(dictionary, INFORMATION_TYPE, document, flavour);
        this.meta = parseMetadata(document.getDocument());
    }

    private VeraPDFMeta parseMetadata(COSDocument document) {
        try (COSStream metadataStream = XMPChecker.getMetadataDictionary(document)) {
            if (metadataStream != null) {
                return VeraPDFMeta.parse(metadataStream.getUnfilteredStream());
            }
        } catch (IOException | XMPException ignored) {
        }
        return null;
    }

    @Override
    public String getModDate() {
        return ((COSDictionary)this.baseObject).getString(COSName.MOD_DATE);
    }

    @Override
    public String getCreationDate() {
        return ((COSDictionary)this.baseObject).getString(COSName.CREATION_DATE);
    }

    @Override
    public String getTitle() {
        return getStringProperty(COSName.TITLE);
    }

    @Override
    public String getXMPTitle() {
        if (meta != null) {
            try {
                return meta.getTitle();
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getAuthor() {
        return getStringProperty(COSName.AUTHOR);
    }

    @Override
    public String getXMPCreator() {
        if (meta != null) {
            try {
                List<String> creator = meta.getCreator();
                if (creator != null) {
                    return String.join(",", creator);
                }
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getSubject() {
        return getStringProperty(COSName.SUBJECT);
    }

    @Override
    public String getProducer() {
        return getStringProperty(COSName.PRODUCER);
    }

    @Override
    public String getCreator() {
        return getStringProperty(COSName.CREATOR);
    }

    @Override
    public String getKeywords() {
        return getStringProperty(COSName.KEYWORDS);
    }

    @Override
    public String getXMPProducer() {
        if (meta != null) {
            try {
                return meta.getProducer();
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getXMPCreatorTool() {
        if (meta != null) {
            try {
                return meta.getCreatorTool();
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getXMPKeywords() {
        if (meta != null) {
            try {
                return meta.getKeywords();
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public String getXMPDescription() {
        if (meta != null) {
            try {
                return meta.getDescription();
            } catch (XMPException ignored) {
            }
        }
        return null;
    }

    @Override
    public Boolean getdoCreationDatesMatch() {
        Calendar xmpCreateDate = null;
        if (meta != null) {
            try {
                xmpCreateDate = meta.getCreateDate();
            } catch (XMPException ignored) {
            }
        }
        COSBase creationDate = ((COSDictionary)this.baseObject).getItem(COSName.CREATION_DATE);
        if (xmpCreateDate != null && creationDate != null) {
            Calendar creationDateCalendar = getCalendar(creationDate);
            return creationDateCalendar != null && xmpCreateDate.compareTo(creationDateCalendar) == 0;
        }
        return null;
    }

    @Override
    public Boolean getdoModDatesMatch() {
        Calendar xmpModifyDate = null;
        if (meta != null) {
            try {
                xmpModifyDate = meta.getModifyDate();
            } catch (XMPException ignored) {
            }
        }
        COSBase modDate = ((COSDictionary)this.baseObject).getItem(COSName.MOD_DATE);
        if (xmpModifyDate != null && modDate != null) {
            Calendar modDateCalendar = getCalendar(modDate);
            return modDateCalendar != null && xmpModifyDate.compareTo(modDateCalendar) == 0;
        }
        return null;
    }

    private Calendar getCalendar(COSBase object) {
        if (object instanceof COSString) {
            String ascii = ((COSString)object).getASCII();
            if (ascii.matches(timeRegex)) {
                return XMPChecker.getCalendar(ascii);
            }
        }
        return null;
    }

    @Override
    public String getXMPCreateDate() {
        return null;
    }

    @Override
    public String getXMPModifyDate() {
        return null;
    }

    private String getStringProperty(COSName name) {
        String value = ((COSDictionary)this.baseObject).getString(name);
        if (value != null) {
            return XMPChecker.getStringWithoutTrailingZero(value);
        }
        return null;
    }

}
