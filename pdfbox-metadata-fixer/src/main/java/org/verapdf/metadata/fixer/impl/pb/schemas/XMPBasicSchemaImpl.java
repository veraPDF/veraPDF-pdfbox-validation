/**
 * This file is part of veraPDF Metadata Fixer, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Metadata Fixer is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Metadata Fixer as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Metadata Fixer as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.impl.pb.schemas;

import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.impl.VeraPDFMeta;
import org.apache.log4j.Logger;
import org.verapdf.metadata.fixer.entity.Metadata;
import org.verapdf.metadata.fixer.schemas.XMPBasic;
import org.verapdf.metadata.fixer.utils.DateConverter;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMPBasicSchemaImpl extends BasicSchemaImpl implements XMPBasic {

	private static final Logger LOGGER = Logger.getLogger(XMPBasicSchemaImpl.class);

	public XMPBasicSchemaImpl(VeraPDFMeta meta, Metadata metadata) {
		super(meta, metadata);
	}

	@Override
	public String getCreator() {
		try {
			return this.meta.getCreatorTool();
		} catch (XMPException e) {
			LOGGER.debug("Can not get creator tool.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setCreator(String creatorTool) {
		try {
			this.meta.setCreatorTool(creatorTool);
		} catch (XMPException e) {
			LOGGER.debug("Can not set creator tool.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getCreationDate() {
		try {
			return DateConverter.toUTCString(this.meta.getCreateDate());
		} catch (XMPException e) {
			LOGGER.debug("Can not get creation date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setCreationDate(String creationDate) {
		try {
			this.meta.setCreateDate(DateConverter.toCalendar(creationDate));
		} catch (XMPException e) {
			LOGGER.debug("Can not set creation date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getModificationDate() {
		try {
			return DateConverter.toUTCString(this.meta.getModifyDate());
		} catch (XMPException e) {
			LOGGER.debug("Can not get modification date.", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void setModificationDate(String modificationDate) {
		try {
			this.meta.setModifyDate(DateConverter.toCalendar(modificationDate));
		} catch (XMPException e) {
			LOGGER.debug("Can not set modification date.", e);
			throw new IllegalStateException(e);
		}
	}

}
