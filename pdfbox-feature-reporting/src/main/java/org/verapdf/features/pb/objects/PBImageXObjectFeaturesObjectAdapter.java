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
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.verapdf.features.objects.ImageXObjectFeaturesObjectAdapter;
import org.verapdf.features.pb.tools.PBAdapterHelper;

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
    private List<String> errors;
    private List<String> filterNames;

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
        this.filterNames = new ArrayList<>();
        init();
    }

    private void init() {
        try {
            List<COSName> filters = imageXObject.getStream().getFilters();
            if(filters != null) {
                for (COSName filter : filters) {
                    this.filterNames.add(filter.getName());
                }
            }
        } catch (IOException e) {
            this.errors.add("Can't get image stream filters");
            LOGGER.info(e);
        }
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public Long getWidth() {
        return imageXObject == null ? null : Long.valueOf(imageXObject.getWidth());
    }

    @Override
    public Long getHeight() {
        return imageXObject == null ? null : Long.valueOf(imageXObject.getHeight());
    }

    @Override
    public String getColorSpaceChild() {
        return this.colorSpaceChild;
    }

    @Override
    public Long getBitsPerComponent() {
        return imageXObject == null ? null : Long.valueOf(imageXObject.getBitsPerComponent());
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
        return Collections.unmodifiableSet(this.alternatesChild);
    }

    @Override
    public String getSMaskChild() {
        return this.sMaskChild;
    }

    @Override
    public Long getStructParent() {
        return imageXObject == null ? null : Long.valueOf(imageXObject.getStructParent());
    }

    @Override
    public List<String> getFilters() {
        return Collections.unmodifiableList(filterNames);
    }

    @Override
    public InputStream getMetadata() {
        if (imageXObject != null) {
            return PBAdapterHelper.getMetadataStream(imageXObject.getMetadata());
        }
        return null;
    }

    @Override
    public InputStream getRawStreamData() {
        if (imageXObject != null) {
            try {
                if (imageXObject.getStream() != null && imageXObject.getStream().getStream() != null) {
                    return imageXObject.getStream().getStream().getFilteredStream();
                }
            } catch (IOException e) {
                LOGGER.info(e);
            }
        }
        return null;
    }

    @Override
    public List<String> getErrors() {
        return Collections.emptyList();
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

            return Collections.unmodifiableList(res);
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
            COSBase k = base.getDictionaryObject(COSName.K);
            return k instanceof COSInteger ? ((COSInteger) k).longValue() : null;
        }

        @Override
        public boolean getCCITTEndOfLine() {
            COSBase colors = base.getDictionaryObject(COSName.COLORS);
            return colors instanceof COSBoolean ? ((COSBoolean) colors).getValue() : false;
        }

        @Override
        public boolean getCCITTEncodedByteAlign() {
            COSBase bitsPerComponent = base.getDictionaryObject(COSName.BITS_PER_COMPONENT);
            return bitsPerComponent instanceof COSBoolean ? ((COSBoolean) bitsPerComponent).getValue() :
                    false;
        }

        @Override
        public Long getCCITTColumns() {
            COSBase columns = base.getDictionaryObject(COSName.COLUMNS);
            return columns instanceof COSInteger ? ((COSInteger) columns).longValue() : null;
        }

        @Override
        public Long getCCITTRows() {
            COSBase rows = base.getDictionaryObject(COSName.ROWS);
            return rows instanceof COSInteger ? ((COSInteger) rows).longValue() : null;
        }

        @Override
        public boolean getCCITTEndOfBlock() {
            COSBase endOfBlock = base.getDictionaryObject(COSName.getPDFName("EndOfBlock"));
            return endOfBlock instanceof COSBoolean ? ((COSBoolean) endOfBlock).getValue() : true;
        }

        @Override
        public boolean getCCITTBlackIs1() {
            COSBase blackIs1 = base.getDictionaryObject(COSName.BLACK_IS_1);
            return blackIs1 instanceof COSBoolean ? ((COSBoolean) blackIs1).getValue() : false;
        }

        @Override
        public Long getCCITTDamagedRowsBeforeError() {
            COSBase damgedRowsBeforeError =
                    base.getDictionaryObject(COSName.getPDFName("DamagedRowsBeforeError"));
            return damgedRowsBeforeError instanceof COSInteger ? ((COSInteger) damgedRowsBeforeError).longValue() :
                    null;
        }

        @Override
        public Long getDCTColorTransform() {
            COSBase colorTransform = base.getDictionaryObject(COSName.getPDFName("ColorTransform"));
            return colorTransform instanceof COSInteger ? ((COSInteger) colorTransform).longValue() : null;
        }

        @Override
        public Long getLZWEarlyChange() {
            COSBase earlyChange = base.getDictionaryObject(COSName.EARLY_CHANGE);
            return earlyChange instanceof COSInteger ? ((COSInteger) earlyChange).longValue() : null;
        }

        @Override
        public Long getFlatePredictor() {
            COSBase predictor = base.getDictionaryObject(COSName.PREDICTOR);
            return predictor instanceof COSInteger ? ((COSInteger) predictor).longValue() : null;
        }

        @Override
        public Long getFlateColors() {
            COSBase colors = base.getDictionaryObject(COSName.COLORS);
            return colors instanceof COSInteger ? ((COSInteger) colors).longValue() : null;
        }

        @Override
        public Long getFlateBitsPerComponent() {
            COSBase bitsPerComponent = base.getDictionaryObject(COSName.BITS_PER_COMPONENT);
            return bitsPerComponent instanceof COSInteger ? ((COSInteger) bitsPerComponent).longValue() : null;
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
