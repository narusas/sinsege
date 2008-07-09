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
import net.narusas.aceauction.model.기일내역Item;

// TODO: Auto-generated Javadoc
/**
 * The Class 매각기일UpdaterDB.
 */
public class 매각기일UpdaterDB {
	
	/** The p. */
	static Pattern p = Pattern.compile("\\(([^\\)]+)\\)");

	/** The listener. */
	private final UpdaterListener listener;

	/**
	 * Instantiates a new 매각기일 updater db.
	 * 
	 * @param listener the listener
	 */
	public 매각기일UpdaterDB(UpdaterListener listener) {
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
		log("기일내역의 결과 분석을 시작합니다. ");

		List<기일내역Item> items = 기일내역Item.findByGoodsId(goodsId);
//		for (int i = 0; i < items.size(); i++) {
//			System.out.println(items.get(i));
//		}
		if (items == null || items.size() == 0) {
			return;
		}

		if ("매각결정기일".equals(items.get(items.size() - 1).get기일종류())) {
			items.remove(items.size() - 1);
		}

		int 유찰count = count유찰(items);

		if (items.size() == 1) {
			기일내역Item item = items.get(0);
			String result = item.get기일결과();
			if ("".equals(result) || result == null) {
				update(goodsId, 유찰count, "신건", item.get기일());
			} else {
				update(goodsId, 유찰count, result, item.get기일());
			}
			return;
		}

		기일내역Item lastItem = items.get(items.size() - 1);
		String result = lastItem.get기일결과();
		if (("".equals(result) || "예정".equals(result) || result == null)) {
			기일내역Item item = items.get(items.size() - 2);

			String 마지막바로전 = item.get기일결과();
			if ("유찰".equals(마지막바로전)) {
				update(goodsId, 유찰count, "유찰", lastItem.get기일());
			} else {
				update(goodsId, 유찰count, "재진행", lastItem.get기일());
			}
			return;
		}
		update(goodsId, 유찰count, lastItem.get기일결과(), lastItem.get기일());
	}

	/**
	 * Count유찰.
	 * 
	 * @param list the list
	 * 
	 * @return the int
	 */
	private int count유찰(List<기일내역Item> list) {
		int count = 0;
		for (기일내역Item item : list) {
			// System.out.println("@@" + str);
			String result = item.get기일결과();
			if (result == null) {
				continue;
			}
			if (result.contains("유찰")) {
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
		log("물건의 매각결과를 갱신합니다. 유찰회수=" + count + ", 물건상태=" + result);

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
