package org.jpedal.objects.acroforms;

import java.util.Map;

import org.jpedal.io.PdfObjectReader;

/**generic interface so we can have multiple form streams to decode different types of form*/
public interface FormDecoder {

	void resetItems();

	FormObject createAppearanceString(Map currentField, PdfObjectReader currentPdfFile);

}
