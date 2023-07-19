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
package org.verapdf.metadata.fixer.impl.pb.model;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.verapdf.metadata.fixer.entity.InfoDictionary;
import org.verapdf.metadata.fixer.utils.DateConverter;

/**
 * @author Evgeniy Muravitskiy
 */
public class InfoDictionaryImpl implements InfoDictionary {

	private final PDDocumentInformation info;

	public InfoDictionaryImpl(PDDocumentInformation info) {
		if (info == null) {
			throw new IllegalArgumentException("Info dictionary representation can not be null");
		}
		this.info = info;
	}

	@Override
	public String getTitle() {
		return this.info.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.info.setTitle(title);
	}

	@Override
	public String getSubject() {
		return this.info.getSubject();
	}

	@Override
	public void setSubject(String subject) {
		this.info.setSubject(subject);
	}

	@Override
	public String getAuthor() {
		return this.info.getAuthor();
	}

	@Override
	public int getAuthorSize() {
		return this.info.getAuthor() != null ? 1 : 0;
	}

	@Override
	public void setAuthor(String author) {
		this.info.setAuthor(author);
	}

	@Override
	public String getProducer() {
		return this.info.getProducer();
	}

	@Override
	public void setProducer(String producer) {
		this.info.setProducer(producer);
	}

	@Override
	public String getKeywords() {
		return this.info.getKeywords();
	}

	@Override
	public void setKeywords(String keywords) {
		this.info.setKeywords(keywords);
	}

	@Override
	public String getCreator() {
		return this.info.getCreator();
	}

	@Override
	public void setCreator(String creator) {
		this.info.setCreator(creator);
	}

	@Override
	public String getCreationDate() {
		COSBase modDate = this.info.getCOSObject().getDictionaryObject(COSName.CREATION_DATE);
		return modDate instanceof COSString ? ((COSString) modDate).getString() : null;
	}

	@Override
	public void setCreationDate(String creationDate) {
		this.info.getCOSObject().setString(COSName.CREATION_DATE, DateConverter.toPDFFormat(creationDate));
	}

	@Override
	public String getModificationDate() {
		COSBase modDate = this.info.getCOSObject().getDictionaryObject(COSName.MOD_DATE);
		return modDate instanceof COSString ? ((COSString) modDate).getString() : null;
	}

	@Override
	public void setModificationDate(String modificationDate) {
		this.info.getCOSObject().setString(COSName.MOD_DATE, DateConverter.toPDFFormat(modificationDate));
	}

	@Override
	public boolean isNeedToBeUpdated() {
		return this.info.getCOSObject().isNeedToBeUpdated();
	}

	@Override
	public void setNeedToBeUpdated(boolean needToBeUpdated) {
		this.info.getCOSObject().setNeedToBeUpdated(needToBeUpdated);
	}

}
