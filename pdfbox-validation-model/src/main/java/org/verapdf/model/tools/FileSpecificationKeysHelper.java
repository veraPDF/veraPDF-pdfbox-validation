package org.verapdf.model.tools;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureNode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.PDFontSetting;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObjectProxy;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.verapdf.model.impl.pb.containers.StaticContainers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public class FileSpecificationKeysHelper {

    private static final Logger LOGGER = Logger
            .getLogger(FileSpecificationKeysHelper.class);

    private static Set<COSObjectKey> visitedKeys = new HashSet<>();

    public static void registerFileSpecificationKeys(PDDocument document) {
        PDDocumentCatalog catalog = document.getDocumentCatalog();
        if (catalog != null) {
            registerDictionaryAFKeys(catalog.getCOSObject());
            processStructElements(catalog.getStructureTreeRoot());
        }
        PDPageTree pageTree = document.getPages();
        if (pageTree != null) {
            registerDictionaryAFKeys(pageTree.getCOSObject());
            for (PDPage page : pageTree) {
                processPage(page);
            }
        }
        visitedKeys.clear();
    }

    private static void processStructElements(PDStructureNode structureNode) {
        if (structureNode != null) {
            for (Object obj : structureNode.getKids()) {
                if (obj instanceof PDStructureElement) {
                    PDStructureElement element = (PDStructureElement) obj;
                    registerDictionaryAFKeys(element.getCOSObject());
                    processStructElements(element);
                }
            }
        }
    }

    private static void processPage(PDPage page) {
        if (page != null) {
            registerDictionaryAFKeys(page.getCOSObject());
            try {
                for (PDAnnotation annotation : page.getAnnotations()) {
                    if (annotation != null) {
                        registerDictionaryAFKeys(annotation.getCOSObject());
                        for (PDAppearanceStream stream : getAllAppearances(annotation)) {
                            processXObject(stream);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Can not get page annotations", e);
            }
            parseResources(page.getResources());
        }
    }

    private static void processXObject(PDXObject xObject) {
        if (xObject == null || isKeyVisited(xObject.getCOSObject().getKey())) {
            return;
        }
        registerDictionaryAFKeys((COSDictionary) xObject.getCOSObject());
        if (xObject instanceof PDFormXObject) {
            parseResources(((PDFormXObject) xObject).getResources());
        } else if (xObject instanceof PDImageXObjectProxy) {
            try {
                processXObject(((PDImageXObjectProxy) xObject).getMask());
            } catch (IOException e) {
                LOGGER.error("Can not obtain Image xobject Mask", e);
            }
            try {
                processXObject(((PDImageXObjectProxy) xObject).getMask());
            } catch (IOException e) {
                LOGGER.error("Can not obtain Image xobject SMask", e);
            }
            processImageAlternates((PDImageXObjectProxy) xObject);
        }

    }

    private static void processImageAlternates(PDImageXObjectProxy xObject) {
        COSBase alternates = ((COSDictionary) xObject.getCOSObject()).getDictionaryObject(COSName.getPDFName("Alternates"));
        if (alternates instanceof COSArray) {
            for (COSBase obj : (COSArray) alternates) {
                if (obj instanceof COSDictionary) {
                    COSBase image = ((COSDictionary) obj).getDictionaryObject(COSName.IMAGE);
                    if (image instanceof COSStream) {
                        processXObject(new PDImageXObjectProxy(new PDStream((COSStream) image), null));
                    }
                }
            }
        }
    }

    private static List<PDAppearanceStream> getAllAppearances(PDAnnotation annotation) {
        List<PDAppearanceStream> res = new ArrayList<>();
        if (annotation != null) {
            PDAppearanceDictionary appearance = annotation.getAppearance();
            if (appearance != null) {
                addAllAppearances(appearance.getNormalAppearance(), res);
                addAllAppearances(appearance.getDownAppearance(), res);
                addAllAppearances(appearance.getRolloverAppearance(), res);
            }
        }
        return res;
    }

    private static void addAllAppearances(PDAppearanceEntry appearance, List<PDAppearanceStream> list) {
        if (appearance == null) {
            return;
        }
        if (appearance.isStream()) {
            PDAppearanceStream appearanceStream = appearance.getAppearanceStream();
            if (appearanceStream != null) {
                list.add(appearanceStream);
            }
        } else {
            for (PDAppearanceStream appearanceStream : appearance.getSubDictionary().values()) {
                if (appearanceStream != null) {
                    list.add(appearanceStream);
                }
            }
        }
    }

    private static void registerDictionaryAFKeys(COSDictionary dictionary) {
        if (dictionary == null) {
            return;
        }
        COSBase af = dictionary.getDictionaryObject(COSName.getPDFName("AF"));
        if (af instanceof COSArray) {
            for (COSBase element : (COSArray) af) {
                addElementKey(element);
            }
        }
    }

    private static void processExtGState(PDExtendedGraphicsState extGState) {
        if (extGState == null || isKeyVisited(extGState.getCOSObject().getKey())) {
            return;
        }
        PDFontSetting fontSetting = extGState.getFontSetting();
        if (fontSetting != null) {
            try {
                processFont(fontSetting.getFont());
            } catch (IOException e) {
                LOGGER.error("Can not obtain font from extGState's font settings", e);
            }
        }
    }

    private static void processFont(PDFont font) {
        if (font instanceof PDType3Font && !isKeyVisited(font.getCOSObject().getKey())) {
            parseResources(((PDType3Font) font).getResources());
        }
    }

    private static void processPattern(PDAbstractPattern pattern) {
        if (pattern == null || isKeyVisited(pattern.getCOSObject().getKey())) {
            return;
        }
        if (pattern instanceof PDTilingPattern) {
            parseResources(((PDTilingPattern) pattern).getResources());
        } else if (pattern instanceof PDShadingPattern) {
            processExtGState(((PDShadingPattern) pattern).getExtendedGraphicsState());
        }
    }

    private static void parseResources(PDResources resources) {
        if (resources != null && !isKeyVisited(resources.getCOSObject().getKey())) {
            parseResourcesXObjects(resources);
            parseResourcesExtGState(resources);
            parseResourcesPatterns(resources);
            parseResourcesFonts(resources);
        }
    }

    private static void parseResourcesPatterns(PDResources resources) {
        for (COSName name : resources.getPatternNames()) {
            try {
                PDAbstractPattern pattern = resources.getPattern(name);
                processPattern(pattern);
            } catch (IOException e) {
                LOGGER.error("Can not obtain pattern from resources", e);
            }
        }
    }

    private static void parseResourcesExtGState(PDResources resources) {
        for (COSName name : resources.getExtGStateNames()) {
            PDExtendedGraphicsState extGState = resources.getExtGState(name);
            processExtGState(extGState);
        }
    }

    private static void parseResourcesXObjects(PDResources resources) {
        for (COSName name : resources.getXObjectNames()) {
            try {
                PDXObject xObject = resources.getXObject(name);
                processXObject(xObject);
            } catch (IOException e) {
                LOGGER.error("Can not obtain xobject from resources", e);
            }
        }
    }

    private static void parseResourcesFonts(PDResources resources) {
        for (COSName name : resources.getFontNames()) {
            try {
                PDFont font = resources.getFont(name);
                processFont(font);
            } catch (IOException e) {
                LOGGER.error("Can not obtain font from resources", e);
            }
        }
    }

    private static void addElementKey(COSBase element) {
        COSBase base = element;
        while (base instanceof COSObject) {
            base = ((COSObject) base).getObject();
        }
        if (base != null) {
            COSObjectKey key = base.getKey();
            if (key != null) {
                StaticContainers.fileSpecificationKeys.add(key);
            }
        }
    }

    private static boolean isKeyVisited(COSObjectKey key) {
        if (visitedKeys.contains(key)) {
            return true;
        }
        visitedKeys.add(key);
        return false;
    }

}
