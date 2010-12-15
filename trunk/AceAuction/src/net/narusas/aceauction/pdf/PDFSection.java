package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

public class PDFSection {
	PDFPage page = new PDFPage();

	LinkedList<PDFPage> pages = new LinkedList<PDFPage>();

	public void addAcceptDate(TextPosition position) {
		page.acceptDate.add(position);
	}

	public void addBecause(TextPosition position) {
		page.because.add(position);
	}

	public void addPriotyNo(TextPosition position) {
		page.priotyNo.add(position);
	}

	public void addPurpose(TextPosition position) {
		page.purpose.add(position);
	}

	public void addRightAndEtc(TextPosition position) {
		page.rightAndEtc.add(position);
	}

	public void newPage() {
		page = new PDFPage();
		pages.add(page);
	}
}

class PDFPage {
	List<TextPosition> acceptDate = new LinkedList<TextPosition>();

	List<TextPosition> because = new LinkedList<TextPosition>();

	List<TextPosition> priotyNo = new LinkedList<TextPosition>();

	List<TextPosition> purpose = new LinkedList<TextPosition>();

	List<TextPosition> rightAndEtc = new LinkedList<TextPosition>();

	@Override
	public String toString() {
		return priotyNo + "\n" + purpose + "\n" + acceptDate + "\n" + because + "\n" + rightAndEtc
				+ "\n";
	}
}
