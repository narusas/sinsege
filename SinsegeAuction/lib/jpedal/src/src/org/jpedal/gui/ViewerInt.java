package org.jpedal.gui;

import org.jpedal.gui.Hotspots;
import org.jpedal.objects.PdfPageData;

import java.util.Map;
import java.io.IOException;

/**
 * provide interface for methods needed for remote printing
 */
public interface ViewerInt {
	void resetPrintData();

	byte[] getPrintData();

	Hotspots getPrintHotspots();

	Map getUserIconsForPrinting();

	void deserializeHotspotData(byte[] hotspotData, boolean b) throws IOException, ClassNotFoundException;

	PdfPageData getPageData();

	Map getFontList();
}
