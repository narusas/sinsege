package net.narusas.aceauction.pdf.jpedal;

import java.util.List;

public class Part {

	final List<TextPosition> acceptDate;

	final List<TextPosition> because;

	final List<TextPosition> priotyNos;

	final List<TextPosition> purpose;

	final List<TextPosition> right;

	public Part(List<TextPosition> priotyNos, List<TextPosition> purpose,
			List<TextPosition> acceptDate, List<TextPosition> because, List<TextPosition> right) {
		this.priotyNos = priotyNos;
		this.purpose = purpose;
		this.acceptDate = acceptDate;
		this.because = because;
		this.right = right;
	}

}
