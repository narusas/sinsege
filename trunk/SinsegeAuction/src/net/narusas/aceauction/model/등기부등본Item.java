/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.PreparedStatement;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * The Class 등기부등본Item.
 */
public class 등기부등본Item {

	/** The accept_date. */
	private final String accept_date;
	
	/** The attested_id. */
	private final long attested_id;
	
	/** The comment. */
	private final String comment;
	
	/** The expiration. */
	private final String expiration;
	
	/** The goods_id. */
	private final long goods_id;
	
	/** The no. */
	private final int no;
	
	/** The right_person. */
	private final String right_person;
	
	/** The right_price. */
	private final String right_price;
	
	/** The right_type. */
	private final String right_type;

	/**
	 * Instantiates a new 등기부등본 item.
	 * 
	 * @param right_type the right_type
	 * @param accept_date the accept_date
	 * @param right_person the right_person
	 * @param right_price the right_price
	 * @param expiration the expiration
	 * @param comment the comment
	 * @param goods_id the goods_id
	 * @param no the no
	 * @param attested_id the attested_id
	 */
	public 등기부등본Item(String right_type, String accept_date,
			String right_person, String right_price, String expiration,
			String comment, long goods_id, int no, long attested_id) {
		this.right_type = right_type;
		this.accept_date = accept_date;
		this.right_person = right_person;
		this.right_price = right_price;
		this.expiration = expiration;
		this.comment = comment;
		this.goods_id = goods_id;
		this.no = no;
		this.attested_id = attested_id;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
	public void insert() throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("INSERT INTO ac_attested_statement " // 
						+ "("// 
						+ "right_type, "// 1
						+ "accept_date,"// 2
						+ "right_person,"// 3
						+ "right_price,"// 4
						+ "expiration,"// 5
						+ "comment,"// 6
						+ "goods_id,"// 7
						+ "no,"// 8
						+ "attested_id"// 9
						+ ") "// 
						+ "VALUES(?,?,?,?,?,?,?,?,?);"); // ;

		stmt.setString(1, right_type);
		stmt.setString(2, accept_date);
		stmt.setString(3, right_person);
		stmt.setString(4, right_price);
		stmt.setString(5, expiration);
		stmt.setString(6, comment);
		stmt.setLong(7, goods_id);
		stmt.setInt(8, no);
		stmt.setLong(9, attested_id);
		stmt.execute();
	}

}
