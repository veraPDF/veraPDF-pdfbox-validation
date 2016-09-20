package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;
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

	private static final Logger LOGGER = Logger.getLogger(PBoxPDOCProperties.class);

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
		List<PDOCConfig> result = new ArrayList<>();

		COSBase contentProperties = this.simplePDObject.getCOSObject();
		if (contentProperties instanceof COSDictionary) {
			COSDictionary defaultConfig = (COSDictionary) ((COSDictionary) contentProperties)
					.getDictionaryObject(COSName.D);

			String[] groupNames = ((PDOptionalContentProperties) this.simplePDObject).getGroupNames();
			List<String> groupNamesList = Arrays.asList(groupNames);

			PDOCConfig pdConfig = new PBoxPDOCConfig(defaultConfig, groupNamesList, false);

			result.add(pdConfig);
			return result;
		}
		LOGGER.debug("Invalid object type of the default optional configuration dictionary. Returning empty config.");
		PDOCConfig config = new PBoxPDOCConfig(new COSDictionary());

		result.add(config);
		return result;
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
					PDOCConfig pdConfig = new PBoxPDOCConfig(config, groupNamesList,
							names.contains(((COSDictionary) config).getString(COSName.NAME)));
					result.add(pdConfig);
				} else {
					LOGGER.debug("Invalid object type of the configuration dictionary. Ignoring config.");
				}
			}
			return result;
		}
		return Collections.emptyList();
	}

	private static List<String> getAllNames(final COSDictionary contentProperties) {
		List<String> result = new ArrayList<>();

		COSBase defaultConfig = contentProperties.getDictionaryObject(COSName.D);
		if (defaultConfig instanceof COSDictionary) {
			String name = ((COSDictionary) defaultConfig).getString(COSName.NAME);
			if (name != null) {
				result.add(name);
			}
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
				}
			}
		}

		return result;
	}

}