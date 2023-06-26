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
package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosTrailer;
import org.verapdf.model.coslayer.CosXRef;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.verapdf.model.impl.pb.cos.PBCosDocument.COS_DOCUMENT_TYPE;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosDocumentTest extends BaseTest {

    public static final String FILE_RELATIVE_PATH = "model/impl/pb/cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

    private static final Long expectedNumberOfIndirects = Long.valueOf(17);
	private static final double expectedDocumentVersion = 1.4;
    private static final String expectedIDS = "D6CF927DCF82444068EB69A5914F8070A2A7539F7C71DEBB6A4A6B418235962D";


    @BeforeClass
    public static void setUp() throws URISyntaxException, IOException {
        expectedType = TYPES.contains(COS_DOCUMENT_TYPE) ? COS_DOCUMENT_TYPE : null;
        expectedID = null;

        try (PDDocument doc = PDDocument.load(PBCosDocumentTest.class.getClassLoader().getResourceAsStream(FILE_RELATIVE_PATH), false, true)) {
            actual = new PBCosDocument(doc, PDFAFlavour.PDFA_1_B);
        }
    }

    @Test
    public void testNumberOfIndirectsMethod() {
        Assert.assertEquals(expectedNumberOfIndirects, ((CosDocument) actual).getnrIndirects());
    }

	@Test
	public void testHeaderVersionMethod() {
		double actualVersion = ((CosDocument) actual).getheaderVersion().doubleValue();
		Assert.assertEquals(expectedDocumentVersion, actualVersion, 0.01);
	}

	@Test
	public void testHeaderOffset() {
		Long actualOffset = ((CosDocument) BaseTest.actual).getheaderOffset();
		Assert.assertEquals(Long.valueOf(0), actualOffset);
	}

	@Test
	public void testHeader() {
		String actualHeader = ((CosDocument) actual).getheader();
		Assert.assertEquals("%PDF-1.4", actualHeader);
	}

	@Test
	public void testHeaderByte1() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte1();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte2() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte2();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte3() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte3();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

	@Test
	public void testHeaderByte4() {
		Long actualHeaderByte = ((CosDocument) BaseTest.actual).getheaderByte4();
		Assert.assertEquals(Long.valueOf(-1), actualHeaderByte);
	}

    @Test
    public void testOptionalContentMethod() {
        Assert.assertFalse(((CosDocument) actual).getisOptionalContentPresent().booleanValue());
    }

    @Test
    public void testPostEOFDate() {
		Assert.assertEquals(Long.valueOf(0), ((CosDocument) actual).getpostEOFDataSize());
    }

    @Test
    public void testFirstPageTrailer() {
        String ids = ((CosDocument) actual).getfirstPageID();
        Assert.assertEquals(getExpectedID(), ids);
    }

    @Test
    public void testLastTrailer() {
        String ids = ((CosDocument) actual).getlastID();
        Assert.assertEquals(getExpectedID(), ids);
    }

    // problems with code symbols
    private static String getExpectedID() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < expectedIDS.length(); i += 2) {
            builder.append((char) Integer.parseInt(expectedIDS.substring(i, i + 2), 16));
        }
        return builder.toString();
    }

    @Test
    public void testIsLinearized() {
        Assert.assertFalse(((CosDocument) actual).getisLinearized().booleanValue());
    }

    @Test
    public void testInfoMatchXMP() {
        Assert.assertTrue(((CosDocument) actual).getdoesInfoMatchXMP().booleanValue());
    }

    @Test
    public void testTrailerLink() {
        List<? extends Object> trailer = actual.getLinkedObjects(PBCosDocument.TRAILER);
        Assert.assertEquals(1, trailer.size());
        Assert.assertTrue(trailer.get(0) instanceof CosTrailer);
    }

    @Test
    public void testIndirectObjectsLink() {
        List<? extends Object> indirects = actual.getLinkedObjects(PBCosDocument.INDIRECT_OBJECTS);
        Assert.assertEquals(expectedNumberOfIndirects.intValue(), indirects.size());
        for (Object indirect : indirects) {
            Assert.assertTrue(indirect instanceof PBCosIndirect);
        }
    }

    @Test
    public void testDocumentLink() {
        List<? extends Object> doc = actual.getLinkedObjects(PBCosDocument.DOCUMENT);
        Assert.assertEquals(1, doc.size());
        Assert.assertTrue(doc.get(0) instanceof org.verapdf.model.pdlayer.PDDocument);
    }

    @Test
    public void testXREFLink() {
        List<? extends Object> xref = actual.getLinkedObjects(PBCosDocument.XREF);
        Assert.assertEquals(1, xref.size());
        Assert.assertTrue(xref.get(0) != null);
        Assert.assertTrue(xref.get(0) instanceof CosXRef);
    }

    @Test
    public void testEmbeddedFilesLink() {
        List<? extends Object> embeddedFiles = actual.getLinkedObjects(PBCosDocument.EMBEDDED_FILES);
        Assert.assertEquals(0, embeddedFiles.size());
    }

}
