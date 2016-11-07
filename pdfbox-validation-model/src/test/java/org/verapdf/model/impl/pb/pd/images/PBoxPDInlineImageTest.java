package org.verapdf.model.impl.pb.pd.images;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIIFilter;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImageTest extends PBoxPDXImageTest {

	@BeforeClass
	public static void setUp() throws IOException {
		expectedType = TYPES.contains(PBoxPDInlineImage.INLINE_IMAGE_TYPE) ?
				PBoxPDInlineImage.INLINE_IMAGE_TYPE : null;
		expectedID = null;

		final COSDictionary parameters = getParameters();
		PDInlineImage image = new PDInlineImage(parameters, new byte[0], new PDResources(new COSDictionary()));
		actual = new PBoxPDInlineImage(image, document, null);
	}

	private static COSDictionary getParameters() {
		COSDictionary dictionary = new COSDictionary();
		dictionary.setItem(COSName.CS, COSName.RGB);
		dictionary.setItem(COSName.F, COSName.LZW_DECODE_ABBREVIATION);
		return dictionary;
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertNull(((org.verapdf.model.pdlayer.PDInlineImage) actual).getSubtype());
	}

	@Test
	public void testFMethod() {
		List<? extends Object> actualFilters = ((PBoxPDInlineImage) actual).getLinkedObjects(PBoxPDInlineImage.F);
		Assert.assertEquals(1, actualFilters.size());
		Object filter = actualFilters.get(0);
		if (filter instanceof CosIIFilter) {
			Assert.assertEquals("LZW", ((CosIIFilter) filter).getinternalRepresentation());
		} else {
			Assert.fail();
		}
	}

	@AfterClass
	public static void tearDown() {
		expectedType = null;
		expectedID = null;
		actual = null;
	}

}
