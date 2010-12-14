/*
 * 
 */
package net.narusas.si.auction.pdf;

import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

// TODO: Auto-generated Javadoc
/**
 * The Class PDFSections.
 */
public class PDFSections extends PDFSection {

	/** The last right. */
	private TextPosition lastRight;

	/** The sections. */
	private LinkedList<PDFSection> sections = new LinkedList<PDFSection>();

	/** The section. */
	PDFSection section;

	/**
	 * Instantiates a new pDF sections.
	 */
	public PDFSections() {
		// newSection();
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.pdf.PDFSection#addAcceptDate(org.pdfbox.util.TextPosition)
	 */
	@Override
	public void addAcceptDate(TextPosition position) {
		section.addAcceptDate(position);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.pdf.PDFSection#addBecause(org.pdfbox.util.TextPosition)
	 */
	@Override
	public void addBecause(TextPosition position) {
		section.addBecause(position);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.pdf.PDFSection#addPriotyNo(org.pdfbox.util.TextPosition)
	 */
	@Override
	public void addPriotyNo(TextPosition position) {
		section.addPriotyNo(position);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.pdf.PDFSection#addPurpose(org.pdfbox.util.TextPosition)
	 */
	@Override
	public void addPurpose(TextPosition position) {
		section.addPurpose(position);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.pdf.PDFSection#addRightAndEtc(org.pdfbox.util.TextPosition)
	 */
	@Override
	public void addRightAndEtc(TextPosition position) {
		section.addRightAndEtc(position);
	}

	/**
	 * Gets the.
	 * 
	 * @param i the i
	 * 
	 * @return the pDF section
	 */
	public PDFSection get(int i) {
		return sections.get(i);
	}

	/**
	 * Gets the sections.
	 * 
	 * @return the sections
	 */
	public List<PDFSection> getSections() {
		return java.util.Collections.unmodifiableList(sections);
	}

	/**
	 * New section.
	 */
	public void newSection() {
		section = new PDFSection();
		sections.add(section);
		section.newPage();
	}

	/**
	 * Next page.
	 */
	public void nextPage() {
		if (section == null) {
			return;
		}
		section.newPage();
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public int size() {
		return sections.size();
	}
}
