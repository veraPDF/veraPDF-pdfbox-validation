package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDOCConfig;
import org.verapdf.model.pdlayer.PDOCProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDOCProperties extends PBoxPDObject implements PDOCProperties {

	public static final String OC_PROPERTIES_TYPE = "PDOCProperties";

	public static final String D = "D";
	public static final String CONFIGS = "Configs";

	public PBoxPDOCProperties(COSObjectable simplePDObject) {
		super(simplePDObject, OC_PROPERTIES_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case D:
				return this.getD();
			case CONFIGS:
				return this.getConfigs();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDOCConfig> getD() {
		COSBase contentProperties = this.simplePDObject.getCOSObject();
		if (contentProperties instanceof COSDictionary) {
			COSDictionary defaultConfig = (COSDictionary) ((COSDictionary) contentProperties).getDictionaryObject(COSName.D);

			String[] groupNames = ((PDOptionalContentProperties) this.simplePDObject).getGroupNames();
			List<String> groupNamesList = Arrays.asList(groupNames);

			PDOCConfig pdConfig = new PBoxPDOCConfig(defaultConfig, groupNamesList, false);

			List<PDOCConfig> result = new ArrayList<>();
			result.add(pdConfig);
			return result;
		} else {
			//TODO : log error
		}
		return Collections.emptyList();
	}

	private List<PDOCConfig> getConfigs() {

		COSDictionary contentProperties = (COSDictionary) this.simplePDObject.getCOSObject();

		List<String> names = getAllNames(contentProperties);
		String[] groupNames = ((PDOptionalContentProperties) this.simplePDObject).getGroupNames();
		List<String> groupNamesList = Arrays.asList(groupNames);

		COSArray configs = (COSArray) contentProperties.getDictionaryObject(CONFIGS);

		if (configs != null) {
			List<PDOCConfig> result = new ArrayList<>();
			for (int i = 0; i < configs.size(); i++) {
				COSBase config = configs.get(i);
				if (config instanceof COSDictionary) {
					PDOCConfig pdConfig = new PBoxPDOCConfig(config, groupNamesList, names.contains(((COSDictionary) config).getString(COSName.NAME)));
					result.add(pdConfig);
				} else {
					//TODO : log error
				}
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	private List<String> getAllNames(final COSDictionary contentProperties) {
		List<String> result = new ArrayList<>();

		COSBase defaultConfig = contentProperties.getDictionaryObject(COSName.D);
		if (defaultConfig instanceof COSDictionary) {
			String name = ((COSDictionary) defaultConfig).getString(COSName.NAME);
			if (name != null) {
				result.add(name);
			}
		} else {
			//TODO : log error
		}

		COSArray configs = (COSArray) contentProperties.getDictionaryObject(CONFIGS);
		if (configs != null) {
			for (int i = 0; i < configs.size(); i++) {
				COSBase config = configs.get(i);
				if (config instanceof COSDictionary) {
					String name = ((COSDictionary) config).getString(COSName.NAME);
					if (name != null) {
						result.add(name);
					}
				} else {
					//TODO : log error
				}
			}
		}

		return result;
	}

}
