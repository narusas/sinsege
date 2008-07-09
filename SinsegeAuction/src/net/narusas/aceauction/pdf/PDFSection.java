/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

// TODO: Auto-generated Javadoc
/**
 * The Class PDFSection.
 */
public class PDFSection {
	
	/** The page. */
	PDFPage page = new PDFPage();

	/** The pages. */
	LinkedList<PDFPage> pages = new LinkedList<PDFPage>();

	/**
	 * Adds the accept date.
	 * 
	 * @param position the position
	 */
	public void addAcceptDate(TextPosition position) {
		page.acceptDate.add(position);
	}

	/**
	 * Adds the because.
	 * 
	 * @param position the position
	 */
	public void addBecause(TextPosition position) {
		page.because.add(position);
	}

	/**
	 * Adds the prioty no.
	 * 
	 * @param position the position
	 */
	public void addPriotyNo(TextPosition position) {
		page.priotyNo.add(position);
	}

	/**
	 * Adds the purpose.
	 * 
	 * @param position the position
	 */
	public void addPurpose(TextPosition position) {
		page.purpose.add(position);
	}

	/**
	 * Adds the right and etc.
	 * 
	 * @param position the position
	 */
	public void addRightAndEtc(TextPosition position) {
		page.rightAndEtc.add(position);
	}

	/**
	 * New page.
	 */
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
