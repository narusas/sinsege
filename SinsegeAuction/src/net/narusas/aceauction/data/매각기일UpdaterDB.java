/*
 * 
 */
package net.narusas.aceauction.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.updater.UpdaterListener;
import net.narusas.aceauction.model.���ϳ���Item;

// TODO: Auto-generated Javadoc
/**
 * The Class �Ű�����UpdaterDB.
 */
public class �Ű�����UpdaterDB {
	
	/** The p. */
	static Pattern p = Pattern.compile("\\(([^\\)]+)\\)");

	/** The listener. */
	private final UpdaterListener listener;

	/**
	 * Instantiates a new �Ű����� updater db.
	 * 
	 * @param listener the listener
	 */
	public �Ű�����UpdaterDB(UpdaterListener listener) {
		this.listener = listener;
	}

	/**
	 * Update.
	 * 
	 * @param goodsId the goods id
	 * 
	 * @throws Exception the exception
	 */
	public void update(long goodsId) throws Exception {
		log("���ϳ����� ��� �м��� �����մϴ�. ");

		List<���ϳ���Item> items = ���ϳ���Item.findByGoodsId(goodsId);
//		for (int i = 0; i < items.size(); i++) {
//			System.out.println(items.get(i));
//		}
		if (items == null || items.size() == 0) {
			return;
		}

		if ("�Ű���������".equals(items.get(items.size() - 1).get��������())) {
			items.remove(items.size() - 1);
		}

		int ����count = count����(items);

		if (items.size() == 1) {
			���ϳ���Item item = items.get(0);
			String result = item.get���ϰ��();
			if ("".equals(result) || result == null) {
				update(goodsId, ����count, "�Ű�", item.get����());
			} else {
				update(goodsId, ����count, result, item.get����());
			}
			return;
		}

		���ϳ���Item lastItem = items.get(items.size() - 1);
		String result = lastItem.get���ϰ��();
		if (("".equals(result) || "����".equals(result) || result == null)) {
			���ϳ���Item item = items.get(items.size() - 2);

			String �������ٷ��� = item.get���ϰ��();
			if ("����".equals(�������ٷ���)) {
				update(goodsId, ����count, "����", lastItem.get����());
			} else {
				update(goodsId, ����count, "������", lastItem.get����());
			}
			return;
		}
		update(goodsId, ����count, lastItem.get���ϰ��(), lastItem.get����());
	}

	/**
	 * Count����.
	 * 
	 * @param list the list
	 * 
	 * @return the int
	 */
	private int count����(List<���ϳ���Item> list) {
		int count = 0;
		for (���ϳ���Item item : list) {
			// System.out.println("@@" + str);
			String result = item.get���ϰ��();
			if (result == null) {
				continue;
			}
			if (result.contains("����")) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Fix result.
	 * 
	 * @param goodsId the goods id
	 * 
	 * @throws Exception the exception
	 */
	private void fixResult(long goodsId) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_appoint_statement WHERE goods_id=" + goodsId + ";");
		while (rs.next()) {
			String result = rs.getString("result");
			Matcher m = p.matcher(result);
			if (m.find()) {
				String result_temp = result.split("\\(")[0].trim();
				PreparedStatement stmt2 = DB.prepareStatement("UPDATE ac_appoint_statement SET result=? WHERE id=?;");
				stmt2.setString(1, result_temp);
				stmt2.setLong(2, rs.getLong("id"));
				stmt2.executeUpdate();
			}
		}
	}

	/**
	 * Log.
	 * 
	 * @param msg the msg
	 */
	private void log(String msg) {
		if (listener != null) {
			listener.log(msg);
		}
	}

	/**
	 * Update.
	 * 
	 * @param goodsId the goods id
	 * @param count the count
	 * @param result the result
	 * @param date the date
	 * 
	 * @throws Exception the exception
	 */
	private void update(long goodsId, int count, String result, String date) throws Exception {
		log("������ �Ű������ �����մϴ�. ����ȸ��=" + count + ", ���ǻ���=" + result);

		Matcher m = p.matcher(result);
		PreparedStatement stmt = null;
		if (m.find()) {
			String result_temp = result.split("\\(")[0].trim();
			long last_sell_price = DB.toPrice(m.group(1));

			stmt = createUpdateStatememtHaveLastSellPrice(goodsId, count, date, result_temp, last_sell_price);

		} else {
			stmt = createUpdateStatement(goodsId, count, result, date);

		}
		stmt.executeUpdate();

		fixResult(goodsId);

	}

	/**
	 * Creates the update statememt have last sell price.
	 * 
	 * @param goodsId the goods id
	 * @param count the count
	 * @param date the date
	 * @param result_temp the result_temp
	 * @param last_sell_price the last_sell_price
	 * 
	 * @return the prepared statement
	 * 
	 * @throws Exception the exception
	 * @throws SQLException the SQL exception
	 */
	protected PreparedStatement createUpdateStatememtHaveLastSellPrice(long goodsId, int count, String date,
			String result_temp, long last_sell_price) throws Exception, SQLException {
		PreparedStatement stmt;
		stmt = DB
				.prepareStatement("UPDATE ac_goods SET appoint_count=?, appoint_result=?, appoint_result_date=?, last_sell_price=? WHERE id=?;");
		stmt.setInt(1, count);
		stmt.setString(2, result_temp);
		String temp = date == null ? null : date.replaceAll("\\.", "-");
		stmt.setString(3, temp);
		stmt.setLong(4, last_sell_price);
		stmt.setLong(5, goodsId);
		return stmt;
	}

	/**
	 * Creates the update statement.
	 * 
	 * @param goodsId the goods id
	 * @param count the count
	 * @param result the result
	 * @param date the date
	 * 
	 * @return the prepared statement
	 * 
	 * @throws Exception the exception
	 * @throws SQLException the SQL exception
	 */
	protected PreparedStatement createUpdateStatement(long goodsId, int count, String result, String date)
			throws Exception, SQLException {
		PreparedStatement stmt;
		stmt = DB
				.prepareStatement("UPDATE ac_goods SET appoint_count=?, appoint_result=?, appoint_result_date=? WHERE id=?;");
		stmt.setInt(1, count);
		stmt.setString(2, result);
		String temp = date == null ? null : date.replaceAll("\\.", "-");
		stmt.setString(3, temp);
		stmt.setLong(4, goodsId);
		return stmt;
	}
}
