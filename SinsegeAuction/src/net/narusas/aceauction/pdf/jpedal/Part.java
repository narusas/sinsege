/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Part.
 */
public class Part {

	/** The accept date. */
	final List<TextPosition> acceptDate;

	/** The because. */
	final List<TextPosition> because;

	/** The prioty nos. */
	final List<TextPosition> priotyNos;

	/** The purpose. */
	final List<TextPosition> purpose;

	/** The right. */
	final List<TextPosition> right;

	/**
	 * Instantiates a new part.
	 * 
	 * @param priotyNos the prioty nos
	 * @param purpose the purpose
	 * @param acceptDate the accept date
	 * @param because the because
	 * @param right the right
	 */
	public Part(List<TextPosition> priotyNos, List<TextPosition> purpose,
			List<TextPosition> acceptDate, List<TextPosition> because, List<TextPosition> right) {
		this.priotyNos = priotyNos;
		this.purpose = purpose;
		this.acceptDate = acceptDate;
		this.because = because;
		this.right = right;
	}

}
