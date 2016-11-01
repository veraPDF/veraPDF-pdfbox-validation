package org.verapdf.metadata.fixer;

import org.verapdf.metadata.fixer.MetadataFixerImpl;

/**
 * @author Evgeniy Muravitskiy
 */
public enum MetadataFixerEnum {

	BOX_INSTANCE(new PBoxMetadataFixerImpl());

	private final transient MetadataFixerImpl instance;

	MetadataFixerEnum(MetadataFixerImpl instance) {
		this.instance = instance;
	}

	public MetadataFixerImpl getInstance() {
		return this.instance;
	}
}
