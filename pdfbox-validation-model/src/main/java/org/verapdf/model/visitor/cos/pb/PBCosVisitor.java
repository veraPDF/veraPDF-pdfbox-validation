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
package org.verapdf.model.visitor.cos.pb;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.ICOSVisitor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.impl.pb.cos.PBCosBool;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.model.impl.pb.cos.PBCosFileSpecification;
import org.verapdf.model.impl.pb.cos.PBCosIndirect;
import org.verapdf.model.impl.pb.cos.PBCosInteger;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.impl.pb.cos.PBCosNull;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.cos.PBCosString;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * Implementation of {@link ICOSVisitor} which realize Visitor pattern.
 * Current implementation create objects of abstract model implementation for corresponding objects
 * of pdf box. Methods call from {@code <? extends COSBase>} objects using accept() method.
 *
 * @author Evgeniy Muravitskiy
 */
public final class PBCosVisitor implements ICOSVisitor {

    private final PDDocument document;
    private final PDFAFlavour flavour;

    private PBCosVisitor(PDDocument document, PDFAFlavour flavour) {
        this.document = document;
        this.flavour = flavour;
    }

    public static PBCosVisitor getInstance(PDDocument document, PDFAFlavour flavour) {
        return new PBCosVisitor(document, flavour);
    }

    /** {@inheritDoc} Create a PBCosArray for corresponding COSArray.
     * @return PBCosArray object
     * @see PBCosArray
     */
    @Override
    public Object visitFromArray(COSArray obj) {
        return new PBCosArray(obj, document, flavour);
    }

    /** {@inheritDoc} Create a PBCosBool for corresponding COSBoolean.
     * @return PBCosBool object
     * @see PBCosBool
     */
    @Override
    public Object visitFromBoolean(COSBoolean obj) {
        return PBCosBool.valueOf(obj);
    }

    /** {@inheritDoc} Create a PBCosFileSpecification COSDictionary if
	 * value of type key of {@code obj} is file specification. Otherwise
	 * create PBCosDict
     * @return PBCosFileSpecification or PBCosDict
     * @see PBCosDict
	 * @see PBCosFileSpecification
     */
    @Override
    public Object visitFromDictionary(COSDictionary obj) {
		COSName type = obj.getCOSName(COSName.TYPE);
		boolean isFileSpec = type != null && COSName.FILESPEC.equals(type);
		return isFileSpec ? new PBCosFileSpecification(obj, document, flavour) : new PBCosDict(obj, document, flavour);
    }

    /** {@inheritDoc} Create a PBCosDocument for corresponding COSDocument.
     * @return PBCosDocument object
     * @see PBCosDocument
     */
    @Override
    public Object visitFromDocument(COSDocument obj) {
        return new PBCosDocument(obj, flavour);
    }

    /** {@inheritDoc} Create a PBCosReal for corresponding COSFloat.
     * @return PBCosReal object
     * @see PBCosReal
     */
    @Override
    public Object visitFromFloat(COSFloat obj) {
        return new PBCosReal(obj);
    }

    /** {@inheritDoc} Create a PBCosInteger for corresponding COSInteger.
     * @return PBCosInteger object
     * @see PBCosInteger
     */
    @Override
    public Object visitFromInt(COSInteger obj) {
        return new PBCosInteger(obj);
    }

    /** {@inheritDoc} Create a PBCosName for corresponding COSName.
     * @return PBCosName object
     * @see PBCosName
     */
    @Override
    public Object visitFromName(COSName obj) {
        return new PBCosName(obj);
    }

    /** {@inheritDoc} Create a PBCosNull for corresponding COSNull.
     * @return PBCosNull object
     * @see PBCosNull
     */
    @Override
    public Object visitFromNull(COSNull obj) {
        return PBCosNull.getInstance();
    }

    /** {@inheritDoc} Create a PBCosStream for corresponding COSStream.
     * @return PBCosStream object
     * @see PBCosStream
     */
    @Override
    public Object visitFromStream(COSStream obj) {
        return new PBCosStream(obj, document, flavour);
    }

    /** {@inheritDoc} Create a PBCosString for corresponding COSString.
     * @return PBCosString object
     * @see PBCosString
     */
    @Override
    public Object visitFromString(COSString obj) {
        return new PBCosString(obj);
    }

    /** Notification of visiting in indirect object. Create a PBCosIndirect for corresponding
     * COSObject. {@code COSObject#accept(ICOSVisitor)} not accept indirect objects - its get
     * direct content and accepting it.
     * @return {@link PBCosIndirect} object
     * @see PBCosIndirect
     * @see COSObject#accept(ICOSVisitor)
     */
    public static Object visitFromObject(COSObject obj, PDDocument document, PDFAFlavour flavour) {
        return new PBCosIndirect(obj, document, flavour);
    }
}