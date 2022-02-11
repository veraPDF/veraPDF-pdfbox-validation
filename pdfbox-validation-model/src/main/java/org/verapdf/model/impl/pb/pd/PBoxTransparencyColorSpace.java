package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.impl.pb.containers.StaticContainers;
import org.verapdf.model.pdlayer.TransparencyColorSpace;

public class PBoxTransparencyColorSpace extends GenericModelObject implements TransparencyColorSpace {

    protected PDColorSpace colorSpace;

    public static final String TRANSPARENCY_COLOR_SPACE_TYPE = "TransparencyColorSpace";

    public PBoxTransparencyColorSpace(PDColorSpace colorSpace, String type) {
        super(type);
        this.colorSpace = colorSpace;
    }

    public PBoxTransparencyColorSpace(PDColorSpace colorSpace) {
        this(colorSpace, TRANSPARENCY_COLOR_SPACE_TYPE);
    }

    @Override
    public String getcolorSpaceType() {
        StaticContainers.setCurrentTransparencyColorSpace(colorSpace);
        if (colorSpace == null) {
            return null;
        }
        return colorSpace.getName();
    }
}
