/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class 등기부등본Builder.
 */
public class 등기부등본Builder {

	/** The parts. */
	private LinkedList<SectionPart> parts = new LinkedList<SectionPart>();

	/** The sections. */
	private final PDFSections sections;

	/**
	 * Instantiates a new 등기부등본 builder.
	 * 
	 * @param sections the sections
	 */
	public 등기부등본Builder(PDFSections sections) {
		this.sections = sections;
	}

	/**
	 * Gets the part.
	 * 
	 * @param i the i
	 * 
	 * @return the part
	 */
	public SectionPart getPart(int i) {
		return parts.get(i);
	}

	/**
	 * Parses the.
	 */
	public void parse() {
		for (PDFSection section : sections.getSections()) {
			SectionPartParser parser = new SectionPartParser(section);
			SectionPart part = parser.parse();
			parts.add(part);
		}

	}

}
