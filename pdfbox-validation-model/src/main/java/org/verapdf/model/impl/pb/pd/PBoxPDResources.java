package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PBoxPDResources extends PBoxPDObject implements org.verapdf.model.pdlayer.PDResources {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDResources.class.getCanonicalName());

	public static final String RESOURCES_TYPE = "PDResources";

	public static final String RESOURCES_NAMES = "resourcesNames";

	public PBoxPDResources(PDResources resources){
		super(resources, RESOURCES_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case RESOURCES_NAMES:
				return this.getResourcesNames();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosUnicodeName> getResourcesNames(){
		List<CosUnicodeName> names = new ArrayList<>();
		PDResources resources = (PDResources) simplePDObject;
		for (COSName fontName : resources.getFontNames()) {
			try {
				PDFont font = resources.getFont(fontName);
				if (font != null) {
					addAsCosUnicodeName(names, font.getName());
				}
			} catch (IOException e) {
				LOGGER.log(Level.WARNING,"There is no font by fontName " + fontName);
			}
		}
		for (COSName colorSpaceName : resources.getColorSpaceNames()) {
			try {
				PDColorSpace colorSpace = resources.getColorSpace(colorSpaceName);
				if (colorSpace != null) {
					if (colorSpace instanceof PDSeparation) {
						addAsCosUnicodeName(names, ((PDSeparation) colorSpace).getColorantName());
					} else if (colorSpace instanceof PDDeviceN) {
						List<String> colorantNames = ((PDDeviceN) colorSpace).getColorantNames();
						for (String colorant : colorantNames) {
							addAsCosUnicodeName(names, colorant);
						}
					}
				}
			} catch (IOException e) {
				LOGGER.log(Level.WARNING,"There is no colorSpace by colorSpaceName " + colorSpaceName);
			}
		}
		return names;
	}

	public void addAsCosUnicodeName(List<CosUnicodeName> names, String name){
		if (name != null) {
			names.add(new PBCosUnicodeName(COSName.getPDFName(name)));
		}
	}
}
