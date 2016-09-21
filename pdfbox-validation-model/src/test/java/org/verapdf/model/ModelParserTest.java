package org.verapdf.model;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.core.EncryptedPdfException;
import org.verapdf.core.ModelParsingException;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Evgeniy Muravitskiy
 */
@SuppressWarnings({ "javadoc" })
public class ModelParserTest {

	@Test
	public void testExistingFile() throws URISyntaxException, IOException, ModelParsingException, EncryptedPdfException {
		String path = getSystemIndependentPath("/model/impl/pb/pd/Fonts.pdf");
		try (FileInputStream fis = new FileInputStream(path);
				ModelParser loader = ModelParser.createModelWithFlavour(fis, null)) {
			Assert.assertNotNull(loader.getPDDocument());
			Assert.assertNotNull(loader.getRoot());
		}
	}

	private static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}
