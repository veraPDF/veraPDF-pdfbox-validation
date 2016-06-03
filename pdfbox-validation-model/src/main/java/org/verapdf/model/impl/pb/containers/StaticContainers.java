package org.verapdf.model.impl.pb.containers;

import org.verapdf.model.impl.pb.pd.colors.PBoxPDSeparation;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timur Kamalov
 */
public class StaticContainers {

	//PBoxPDSeparation
	public static Map<String, List<PBoxPDSeparation>> separations = new HashMap<>();
	public static List<String> inconsistentSeparations = new ArrayList<>();

	//ColorSpaceFactory
	//TODO : change key from object reference to something else
	public final static Map<org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace, PDColorSpace> cachedColorSpaces = new HashMap<>();

	public static void clearAllContainers() {
		separations.clear();
		inconsistentSeparations.clear();
		cachedColorSpaces.clear();
	}

}
