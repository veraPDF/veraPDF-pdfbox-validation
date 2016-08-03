package org.verapdf.model.tools;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.impl.pb.pd.PBoxPDStructElem;
import org.verapdf.model.pdlayer.PDStructElem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is utility class for tagged pdf
 *
 * @author Evgeniy Muravitskiy
 */
public class TaggedPDFHelper {

	private static final int MAX_NUMBER_OF_ELEMENTS = 1;

	private TaggedPDFHelper() {
		// disable default constructor
	}

	public static List<PDStructElem> getStructTreeRootChildren(COSDictionary parent,
															   TaggedPDFRoleMapHelper roleMapHelper) {
		return getChildren(parent, roleMapHelper, false);
	}

	public static List<PDStructElem> getStructElemChildren(COSDictionary parent,
														   TaggedPDFRoleMapHelper roleMapHelper) {
		return getChildren(parent, roleMapHelper, true);
	}

	/**
	 * Get all structure elements for current dictionary
	 *
	 * @param parent parent dictionary
	 * @return list of structure elements
	 */
	private static List<PDStructElem> getChildren(COSDictionary parent, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		COSBase children = parent.getDictionaryObject(COSName.K);
		if (children != null) {
			if (children instanceof COSDictionary && isStructElem((COSDictionary) children, checkType)) {
				List<PDStructElem> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBoxPDStructElem((COSDictionary) children, roleMapHelper));
				return Collections.unmodifiableList(list);
			} else if (children instanceof COSArray) {
				return getChildrenFromArray((COSArray) children, roleMapHelper, checkType);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Transform array of dictionaries to list of structure elements
	 *
	 * @param children array of children structure elements
	 * @return list of structure elements
	 */
	private static List<PDStructElem> getChildrenFromArray(COSArray children, TaggedPDFRoleMapHelper roleMapHelper, boolean checkType) {
		if (children.size() > 0) {
			List<PDStructElem> list = new ArrayList<>();
			for (COSBase element : children) {
				COSBase directElem = element;
				if (directElem instanceof COSObject) {
					directElem = ((COSObject) directElem).getObject();
				}
				if (directElem instanceof COSDictionary && isStructElem((COSDictionary) directElem, checkType)) {
					list.add(new PBoxPDStructElem((COSDictionary) directElem, roleMapHelper));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private static boolean isStructElem(COSDictionary dictionary, boolean checkType) {
		if (dictionary == null) {
			return false;
		}
		COSName type = dictionary.getCOSName(COSName.TYPE);
		return !checkType || type == null || type.equals(COSName.getPDFName("StructElem"));
	}
}
