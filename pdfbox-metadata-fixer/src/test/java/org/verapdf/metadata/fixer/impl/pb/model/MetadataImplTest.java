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

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.MetadataFixerResultImpl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
@SuppressWarnings({"javadoc"})
@RunWith(Parameterized.class)
public class MetadataImplTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"test1.pdf",  Integer.valueOf(1), "B"}, {"test2.pdf", Integer.valueOf(1), "B"}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public Integer filePart;

    @Parameterized.Parameter(value = 2)
    public String fileConformance;

    @Test
    public void addPDFIdentificationSchemaTest() throws IOException, XMPException {
        try (PDDocument doc = PDDocument.load(getClass().getClassLoader().getResourceAsStream(filePath), false, true)) {
            PDMetadata meta = doc.getDocumentCatalog().getMetadata();
            try (COSStream cosStream = meta.getStream()) {
                VeraPDFMeta xmp = VeraPDFMeta.parse(cosStream
                        .getUnfilteredStream());
                MetadataImpl impl = new MetadataImpl(xmp, cosStream);
                MetadataFixerResultImpl.Builder builder = new MetadataFixerResultImpl.Builder();
                impl.addPDFIdentificationSchema(builder, PDFAFlavour.PDFA_1_B);

                assertEquals(filePart, xmp.getIdentificationPart());
                assertEquals(fileConformance, xmp.getIdentificationConformance());
            }
        }
    }
}
