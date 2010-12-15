package net.narusas.aceauction.model;

import java.sql.PreparedStatement;

import net.narusas.aceauction.data.DB;

public class 등기부등본Item {

	private final String accept_date;
	private final long attested_id;
	private final String comment;
	private final String expiration;
	private final long goods_id;
	private final int no;
	private final String right_person;
	private final String right_price;
	private final String right_type;

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
