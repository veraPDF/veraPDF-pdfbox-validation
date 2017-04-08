/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.features.objects.ImageXObjectFeaturesObjectAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Features object for image xobject
 *
 * @author Maksim Bezrukov
 */
public class PBImageXObjectFeaturesObjectAdapter implements ImageXObjectFeaturesObjectAdapter {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
            .getLogger(PBImageXObjectFeaturesObjectAdapter.class);

    private PDImageXObjectProxy imageXObject;
    private String id;
    private String colorSpaceChild;
    private String maskChild;
    private String sMaskChild;
    private Set<String> alternatesChild;
    private InputStream metadata;
    private InputStream imageStream;
    private List<String> errors;

    /**
     * Constructs new shading features object
     *
     * @param imageXObject    PDImageXObject which represents image xobject for feature
     *                        report
     * @param id              id of the object
     * @param colorSpaceChild colorSpace id which contains in this image xobject
     * @param maskChild       image xobject id which contains in this image xobject as it's
     *                        mask
     * @param sMaskChild      image xobject id which contains in this image xobject as it's
     *                        smask
     * @param alternatesChild set of image xobject ids which contains in this image xobject
     *                        as alternates
     */
    public PBImageXObjectFeaturesObjectAdapter(PDImageXObjectProxy imageXObject, String id, String colorSpaceChild,
                                               String maskChild, String sMaskChild, Set<String> alternatesChild) {
        this.imageXObject = imageXObject;
        this.id = id;
        this.colorSpaceChild = colorSpaceChild;
        this.maskChild = maskChild;
        this.sMaskChild = sMaskChild;
        this.alternatesChild = alternatesChild;
        this.errors = new ArrayList<>();

        try {
            if (imageXObject != null) {
                PDMetadata pdMetadata = imageXObject.getMetadata();
                this.metadata = pdMetadata == null ? null : pdMetadata.getStream().getFilteredStream();
            }
        } catch (IOException e) {
            this.errors.add("Can't decode metadata stream");
        }
    }

    private void init() {
        try {
            if (imageXObject != null) {
                PDMetadata pdMetadata = imageXObject.getMetadata();
                this.metadata = pdMetadata == null ? null : pdMetadata.getStream().getFilteredStream();
            }
        } catch (IOException e) {
            this.errors.add("Can't decode metadata stream");
        }
        try {
            if (imageXObject != null) {
                this.imageStream = imageXObject.getStream().getStream().getUnfilteredStream();
            }
        } catch (IOException e) {
            this.errors.add("Can't get image stream");
        }
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public Long getWidth() {
        return imageXObject == null ? null : new Long(imageXObject.getWidth());
    }

    @Override
    public Long getHeight() {
        return imageXObject == null ? null : new Long(imageXObject.getHeight());
    }

    @Override
    public String getColorSpaceChild() {
        return this.colorSpaceChild;
    }

    @Override
    public Long getBitsPerComponent() {
        return imageXObject == null ? null : new Long(imageXObject.getBitsPerComponent());
    }

    @Override
    public boolean getImageMask() {
        return imageXObject == null ? false : imageXObject.isStencil();
    }

    @Override
    public String getMaskChild() {
        return this.maskChild;
    }

    @Override
    public boolean isInterpolate() {
        return imageXObject == null ? false : imageXObject.getInterpolate();
    }

    @Override
    public Set<String> getAlternatesChild() {
        return this.alternatesChild;
    }

    @Override
    public String getSMaskChild() {
        return this.sMaskChild;
    }

    @Override
    public Long getStructParent() {
        return imageXObject == null ? null : new Long(imageXObject.getStructParent());
    }

    @Override
    public List<String> getFilters() {
        try {
            List<COSName> filters = imageXObject.getStream().getFilters();
            List<String> res = new ArrayList<>(filters.size());
            for (COSName filter : filters) {
                res.add(filter.getName());
            }
            return res;
        } catch (IOException e) {
            LOGGER.info(e);
            return Collections.emptyList();
        }
    }

    @Override
    public InputStream getMetadata() {
        return this.metadata;
    }

    @Override
    public InputStream getRawStreamData() {
        return this.imageStream;
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }

    @Override
    public List<StreamFilterAdapter> getFilterAdapters() {
        if (imageXObject != null && imageXObject.getCOSObject() != null) {
            COSBase base = imageXObject.getCOSStream().getDictionaryObject(COSName.DECODE_PARMS);
            List<StreamFilterAdapter> res = new ArrayList<>();

            if (base instanceof COSDictionary) {
                res.add(new PBStreamFilterAdapter(base));
            } else if (base instanceof COSArray) {
                for (COSBase baseElem : (COSArray) base) {
                    if (baseElem instanceof COSDictionary) {
                        res.add(new PBStreamFilterAdapter(baseElem));
                    } else {
                        res.add(null);
                    }
                }
            }

            return res;
        }
        return Collections.emptyList();
    }

    class PBStreamFilterAdapter implements StreamFilterAdapter {

        COSDictionary base;

        public PBStreamFilterAdapter(COSBase base) {
            this.base = base == null ? new COSDictionary() : (COSDictionary) base;
        }

        @Override
        public Long getCCITTK() {
            return ((COSInteger) base.getDictionaryObject(COSName.K)).longValue();
        }

        @Override
        public boolean getCCITTEndOfLine() {
            return ((COSBoolean) base.getDictionaryObject(COSName.COLORS)).getValue();
        }

        @Override
        public boolean getCCITTEncodedByteAlign() {
            return ((COSBoolean) base.getDictionaryObject(COSName.BITS_PER_COMPONENT)).getValue();
        }

        @Override
        public Long getCCITTColumns() {
            return ((COSInteger) base.getDictionaryObject(COSName.COLUMNS)).longValue();
        }

        @Override
        public Long getCCITTRows() {
            return ((COSInteger) base.getDictionaryObject(COSName.ROWS)).longValue();
        }

        @Override
        public boolean getCCITTEndOfBlock() {
            return ((COSBoolean) base.getDictionaryObject(COSName.getPDFName("EndOfBlock"))).getValue();
        }

        @Override
        public boolean getCCITTBlackIs1() {
            return ((COSBoolean) base.getDictionaryObject(COSName.BLACK_IS_1)).getValue();
        }

        @Override
        public Long getCCITTDamagedRowsBeforeError() {
            return ((COSInteger) base.getDictionaryObject(COSName.getPDFName("DamagedRowsBeforeError"))).longValue();
        }

        @Override
        public Long getDCTColorTransform() {
            return ((COSInteger) base.getDictionaryObject(COSName.getPDFName("ColorTransform"))).longValue();
        }

        @Override
        public Long getLZWEarlyChange() {
            return ((COSInteger) base.getDictionaryObject(COSName.EARLY_CHANGE)).longValue();
        }

        @Override
        public Long getFlatePredictor() {
            return ((COSInteger) base.getDictionaryObject(COSName.PREDICTOR)).longValue();
        }

        @Override
        public Long getFlateColors() {
            return ((COSInteger) base.getDictionaryObject(COSName.COLORS)).longValue();
        }

        @Override
        public Long getFlateBitsPerComponent() {
            return ((COSInteger) base.getDictionaryObject(COSName.BITS_PER_COMPONENT)).longValue();
        }

        @Override
        public Long getFlateColumns() {
            return ((COSInteger) base.getDictionaryObject(COSName.COLUMNS)).longValue();
        }

        @Override
        public InputStream getJBIG2Global() {
            try {
                if (base.getDictionaryObject(COSName.JBIG2_GLOBALS) instanceof COSStream) {
                    return ((COSStream) base.getDictionaryObject(COSName.JBIG2_GLOBALS)).getUnfilteredStream();
                }
            } catch (IOException e) {
                LOGGER.info(e);
            }
            return null;
        }

        @Override
        public boolean hasCryptFilter() {
            return !COSName.IDENTITY.equals(base.getCOSName(COSName.NAME));
        }
    }
}
