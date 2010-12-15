package net.narusas.aceauction.pdf;

import java.util.LinkedList;

public class 등기부등본Builder {

	private LinkedList<SectionPart> parts = new LinkedList<SectionPart>();

	private final PDFSections sections;

	public 등기부등본Builder(PDFSections sections) {
		this.sections = sections;
	}

	public SectionPart getPart(int i) {
		return parts.get(i);
	}

	public void parse() {
		for (PDFSection section : sections.getSections()) {
			SectionPartParser parser = new SectionPartParser(section);
			SectionPart part = parser.parse();
			parts.add(part);
		}

	}

}
