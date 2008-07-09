/*
 * 
 */
package net.narusas.aceauction.model;


// TODO: Auto-generated Javadoc
/**
 * The Class 명세서.
 */
public class 명세서 {

	/** The 개요. */
	private final String 개요;

	/** The 권리. */
	private final String 권리;

	/** The 비고. */
	private final String 비고;

	/** The 비고란. */
	private final String 비고란;

	/** The 점유자. */
	private final Table 점유자;

	/**
	 * Instantiates a new 명세서.
	 * 
	 * @param table the table
	 * @param 비고 the 비고
	 * @param 권리 the 권리
	 * @param 개요 the 개요
	 * @param 비고란 the 비고란
	 */
	public 명세서(Table table, String 비고, String 권리, String 개요, String 비고란) {
		this.점유자 = table;
		this.비고 = 비고;
		this.권리 = 권리;
		this.개요 = 개요;
		this.비고란 = 비고란;

	}

	/**
	 * Gets the 개요.
	 * 
	 * @return the 개요
	 */
	public String get개요() {
		return 개요;
	}

	/**
	 * Gets the 권리.
	 * 
	 * @return the 권리
	 */
	public String get권리() {
		return 권리;
	}

	/**
	 * Gets the 비고.
	 * 
	 * @return the 비고
	 */
	public String get비고() {
		return 비고;
	}

	/**
	 * Gets the 비고란.
	 * 
	 * @return the 비고란
	 */
	public String get비고란() {
		return 비고란;
	}

	/**
	 * Gets the 점유자.
	 * 
	 * @return the 점유자
	 */
	public Table get점유자() {
		return 점유자;
	}

}
