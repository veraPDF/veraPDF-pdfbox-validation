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
package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosBM;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosBM;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.pd.functions.PBoxPDFunction;
import org.verapdf.model.pdlayer.PDExtGState;
import org.verapdf.model.pdlayer.PDHalftone;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class represent extended graphic state
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDExtGState extends PBoxPDResource implements PDExtGState {

	public static final String EXT_G_STATE_TYPE = "PDExtGState";

	public static final String CUSTOM_FUNCTIONS = "customFunctions";
    public static final String RI = "RI";
    public static final String FONT_SIZE = "fontSize";
	public static final String HALFTONE = "HT";
	public static final String LINK_BM = "bm";

	private final Double ca;
	private final Double CA;
	
	private final PDFAFlavour flavour;

    public PBoxPDExtGState(PDExtendedGraphicsState state, PDFAFlavour flavour) {
        super(state, EXT_G_STATE_TYPE);
		this.ca = PBoxPDExtGState.getDoubleProperty(state, COSName.CA_NS);
		this.CA = PBoxPDExtGState.getDoubleProperty(state, COSName.CA);
		this.flavour = flavour;
    }

    @Override
    public Double getca() {
        return this.ca;
    }

    @Override
    public Double getCA() {
        return this.CA;
    }

	@Override
	public String getTR2NameValue() {
		return getNameKeyStringValue((COSDictionary)simplePDObject.getCOSObject(), COSName.getPDFName("TR2"));
	}

	@Override
	public Boolean getcontainsTR() {
		return ((COSDictionary)simplePDObject.getCOSObject()).containsKey(COSName.TR);
	}

	@Override
	public Boolean getcontainsTR2() {
		return ((COSDictionary)simplePDObject.getCOSObject()).containsKey(COSName.getPDFName("TR2"));
	}

	@Override
	public Boolean getcontainsHTP() {
		return ((COSDictionary)simplePDObject.getCOSObject()).containsKey(COSName.getPDFName("HTP"));
	}

	@Override
	public Boolean getcontainsHTO() {
		return ((COSDictionary)simplePDObject.getCOSObject()).containsKey(COSName.getPDFName("HTO"));
	}

	@Override
	public String getSMaskNameValue() {
		return getNameKeyStringValue((COSDictionary)simplePDObject.getCOSObject(), COSName.SMASK);
	}

	@Override
	public Boolean getcontainsSMask() {
		return ((COSDictionary)simplePDObject.getCOSObject()).containsKey(COSName.SMASK);
	}

	@Override
	public String getBMNameValue() {
		return getNameKeyStringValue((COSDictionary)simplePDObject.getCOSObject(), COSName.BM);
	}

	private static String getNameKeyStringValue(COSDictionary dictionary, COSName key) {
		COSBase base = dictionary.getDictionaryObject(key);
		return base instanceof COSName ? ((COSName) base).getName() : null;
    }

	private static Double getDoubleProperty(PDExtendedGraphicsState state, COSName key) {
		COSBase base = state.getCOSObject().getDictionaryObject(key);
		return !(base instanceof COSNumber) ? null :
				Double.valueOf(((COSNumber) base).doubleValue());
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case RI:
				return this.getRI();
			case HALFTONE:
				return this.getHalftone();
			case CUSTOM_FUNCTIONS:
				return this.getCustomFunctions();
			case LINK_BM:
				return this.getLinkBM();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosBM> getLinkBM() {
		COSBase BM = ((COSDictionary)simplePDObject.getCOSObject()).getDictionaryObject(COSName.BM);
		if (BM == null) {
			return Collections.emptyList();
		}
		if (flavour == null || flavour.getPart() != PDFAFlavour.Specification.ISO_19005_4) {
			if (BM instanceof COSArray) {
				COSArray array = (COSArray)BM;
				for (COSBase obj : array) {
					if (obj instanceof COSName) {
						BM = obj;
						break;
					}
				}
			}
		}
		if (BM instanceof COSName) {
			List<CosBM> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosBM((COSName)BM));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<CosRenderingIntent> getRI() {
        RenderingIntent renderingIntent = ((PDExtendedGraphicsState) this.simplePDObject)
                .getRenderingIntent();
        if (renderingIntent != null) {
			List<CosRenderingIntent> renderingIntents = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			COSName pdfName = COSName.getPDFName(renderingIntent.stringValue());
            renderingIntents.add(new PBCosRenderingIntent(pdfName));
			return Collections.unmodifiableList(renderingIntents);
        }
        return Collections.emptyList();
    }

	private List<PDHalftone> getHalftone() {
		COSDictionary dict = ((PDExtendedGraphicsState) this.simplePDObject).getCOSObject();
		COSBase halftone = dict.getDictionaryObject(COSName.getPDFName("HT"));
		boolean isDictionary = halftone instanceof COSDictionary;
		if (isDictionary || halftone instanceof COSName) {
			ArrayList<PDHalftone> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			if (isDictionary) {
				list.add(new PBoxPDHalftone((COSDictionary) halftone));
			} else {
				list.add(new PBoxPDHalftone((COSName) halftone));
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
	//Stub
	private List<PBoxPDFunction> getCustomFunctions() {
		return Collections.emptyList();
	}

}
