/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.금액Converter;
import net.narusas.aceauction.data.매각기일UpdaterDB;
import net.narusas.aceauction.fetchers.스피드옥션물건상세내용Fetcher;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.스피드옥션매각기일현황;
import net.narusas.aceauction.model.스피드옥션물건상세내역;
import net.narusas.aceauction.model.스피드옥션매각기일현황.Item;

// TODO: Auto-generated Javadoc
/**
 * The Class Updater.
 */
public class Updater extends DB {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p. */
	static Pattern p = Pattern.compile("매각(.*)원");

	/** The listener. */
	private final UpdaterListener listener;

	/** The time point. */
	private final Date timePoint;

	/**
	 * Instantiates a new updater.
	 * 
	 * @param pointTime the point time
	 * @param listener the listener
	 */
	public Updater(Date pointTime, UpdaterListener listener) {
		this.timePoint = pointTime;
		this.listener = listener;
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		listener.log("DB에 연결합니다");
		dbConnect();
		try {
			updateGoods(timePoint);
		} catch (Throwable t) {
			t.printStackTrace();
			logger.log(Level.FINE, "작업중에러", t);
		}
	}

	/**
	 * Charge code.
	 * 
	 * @param charge the charge
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	private String chargeCode(String charge) throws Exception {

		PreparedStatement stmt = prepareStatement("SELECT charge_code FROM ac_charge WHERE no=?;");
		stmt.setInt(1, Integer.parseInt(charge));
		ResultSet rs = stmt.executeQuery();
		try {
			if (rs.next()) {
				return rs.getString(1);
			}
			throw new IllegalArgumentException("No such charge " + charge);
		} finally {
			rs.close();
		}
	}

	/**
	 * Fetch스피드옥션_매각기일현황.
	 * 
	 * @param target the target
	 * 
	 * @return the 스피드옥션매각기일현황
	 * 
	 * @throws Exception the exception
	 */
	private 스피드옥션매각기일현황 fetch스피드옥션_매각기일현황(물건Item target) throws Exception {
		listener.log("스피드옥션에서 매각기일현황을 얻어옵니다. .");
		스피드옥션물건상세내용Fetcher fetcher = new 스피드옥션물건상세내용Fetcher();
		String id = target.getId();
		String court = target.getCourt();
		String charge = target.getCharge();
		String event_id = target.getEvent_no();
		String no = target.getNo();

		String evetNo = getEventNo(event_id);
		String page = fetcher.getPage(getCourt(court), chargeCode(charge), year(evetNo), no(evetNo), no);
		// listener.log(page);
		// System.out.println(page);

		if (page.contains("서비스 지역이 아니거나 서비스가 만료되었습니다")) {
			JOptionPane.showMessageDialog(null, "스피드옥션 계정이 종료되었습니다. 계정을 갱신하시고 프로그램을 다시 실행하여 주십시요");
			System.exit(-1);
		}
		스피드옥션물건상세내역 item = fetcher.parse(page);

		return new 스피드옥션매각기일현황(item.get매각기일현황(), item.get보증금());
	}

	/**
	 * Gets the court.
	 * 
	 * @param court the court
	 * 
	 * @return the court
	 */
	private String getCourt(String court) {
		return 법원.findByCode(letter6(court)).get스피드옥션Code();
	}

	/**
	 * Gets the event no.
	 * 
	 * @param event_no the event_no
	 * 
	 * @return the event no
	 * 
	 * @throws Exception the exception
	 */
	private String getEventNo(String event_no) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs2 = stmt.executeQuery("SELECT no FROM ac_event WHERE id=" + event_no + ";");
		rs2.next();
		String evetNo = rs2.getString(1);
		rs2.close();
		return evetNo;
	}

	/**
	 * Gets the last.
	 * 
	 * @param items the items
	 * 
	 * @return the last
	 */
	private 매각기일Item getLast(List<매각기일Item> items) {
		if (items.size() == 0) {
			return null;
		}
		매각기일Item item = items.get(items.size() - 1);
		if ("매각결정기일".equals(item.type)) {
			item = items.get(items.size() - 2);
		}
		return item;
	}

	/**
	 * Gets the max no.
	 * 
	 * @param id the id
	 * 
	 * @return the max no
	 * 
	 * @throws Exception the exception
	 */
	private int getMaxNo(long id) throws Exception {
		PreparedStatement stmt = prepareStatement("SELECT max(no) FROM ac_appoint_statement WHERE goods_id=?;");
		stmt.setLong(1, id);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		return rs.getInt(1);
	}

	/**
	 * Gets the 매각기일현황.
	 * 
	 * @param goodsId the goods id
	 * 
	 * @return the 매각기일현황
	 * 
	 * @throws Exception the exception
	 */
	private List<매각기일Item> get매각기일현황(long goodsId) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, no, fixed_date, lowest_price, result, type "
				+ "FROM ac_appoint_statement " + "WHERE goods_id=" + goodsId + ";");
		LinkedList<매각기일Item> list = new LinkedList<매각기일Item>();
		while (rs.next()) {
			list.add(new 매각기일Item(rs));
		}
		rs.close();
		return list;
	}

	/**
	 * Insert매각기일 item.
	 * 
	 * @param id the id
	 * @param item the item
	 * @param no the no
	 * 
	 * @throws Exception the exception
	 */
	private void insert매각기일Item(long id, Item item, int no) throws Exception {

		// 매각결과는 tabel의 컬럼수가 1이다.
		if (item.getComment() != null) {
			Matcher m = p.matcher(item.getComment());
			if (m.find()) {
				update물건매각가격(id, 금액Converter.convert(m.group(1)), item.getResult());
			}
		}

		listener.log("매각기일을 추가한다. no=" + no + ", date=" + item.getDate() + ", price=" + item.getPrice() + ", result="
				+ item.getResult() + ", comment=" + item.getComment());

		PreparedStatement stmt = prepareStatement("INSERT INTO ac_appoint_statement "//
				+ "(" //
				+ "no, "// 1
				+ "fixed_date, "// 2
				+ "lowest_price, "// 3
				+ "result, "// 4
				+ "goods_id"// 5
				+ ") " + "VALUES (?,?,?,?,?);");

		stmt.setInt(1, no);
		stmt.setDate(2, item.getDate());
		stmt.setString(3, 금액Converter.convert(item.getPrice()));
		stmt.setString(4, item.getResult());
		stmt.setLong(5, id);
		stmt.execute();

	}

	/**
	 * Update goods.
	 * 
	 * @param timePoint the time point
	 * 
	 * @throws Exception the exception
	 */
	private void updateGoods(Date timePoint) throws Exception {
		listener.log("갱신 대상이 되는 물건을 DB에서 얻어옵니다.");

		PreparedStatement stmt = prepareStatement("SELECT no FROM ac_charge WHERE fixed_date=?;");
		stmt.setDate(1, new java.sql.Date(timePoint.getTime()));
		ResultSet rs = stmt.executeQuery();
		LinkedList<담당계Item> res = new LinkedList<담당계Item>();
		while (rs.next()) {
			res.add(new 담당계Item(rs));
		}
		rs.close();
		listener.updateWorkSize(res.size());
		listener.log("총 작업량 :" + res.size());
		for (담당계Item item : res) {
			stmt = prepareStatement("SELECT id, court_code, charge_id, event_no, no " + "FROM ac_goods "
					+ "WHERE charge_id=" + item.getChargeId() + " AND done=0;");
			rs = stmt.executeQuery();
			update매각기일현황s(rs);
			rs.close();
			listener.progress(1);
		}

	}

	/**
	 * Update매각기일 item.
	 * 
	 * @param id the id
	 * @param lastDBItem the last db item
	 * @param item the item
	 * 
	 * @throws Exception the exception
	 */
	private void update매각기일Item(long id, 매각기일Item lastDBItem, Item item) throws Exception {
		listener.log("기 존재하는 매각기일을 갱신합니다." + item);

		if (item.getComment() != null) {
			Matcher m = p.matcher(item.getComment());
			if (m.find()) {
				update물건매각가격(id, 금액Converter.convert(m.group(1)), item.getResult());
			}
		}

		PreparedStatement stmt = prepareStatement("UPDATE  ac_appoint_statement SET result=? WHERE id=?;");
		stmt.setString(1, item.getResult());
		stmt.setLong(2, lastDBItem.id);
		stmt.executeUpdate();
	}

	/**
	 * Update매각기일현황.
	 * 
	 * @param idStr the id str
	 * @param state the state
	 * 
	 * @throws Exception the exception
	 */
	private void update매각기일현황(String idStr, 스피드옥션매각기일현황 state) throws Exception {

		long id = Long.parseLong(idStr);
		List<매각기일Item> items = get매각기일현황(id);

		listener.log("물건의 매각기일현황을 갱신합니다. ");

		int maxNo = getMaxNo(id);
		매각기일Item lastDBItem = getLast(items);

		listener.log("기존에 기록된 마지막 매각물현 현황의 날자:" + lastDBItem.date);
		Item commentItem = null;
		for (int i = 0; i < state.size(); i++) {
			Item item = state.get(i);
			if (item.get매수인() != null) {
				commentItem = item;
			}
			Date lastDBTime = lastDBItem.date;
			Date itemTime = item.getDate();
			listener.log("스피드옥션의 매각물현 현황의 날자:" + itemTime);

			if (lastDBTime.compareTo(itemTime) == 0) {
				update매각기일Item(id, lastDBItem, item);
			} else if (lastDBTime.compareTo(itemTime) < 0) { // 대법원에서 읽어들인
				// 매각기일 목록보다 최신인
				// 정보가 스피드옥션에
				// 있다면 추가한다.
				insert매각기일Item(id, item, ++maxNo);
			}
		}
		listener.log("매각기일현황 비고:" + commentItem);
		PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET done=1, buyer=?, bid_count=?, buy_price=?, buy_price_ratio=? WHERE id=?;");
		// stmt.setString(1, state.get보증금());
		
		stmt.setString(1, commentItem == null ? null : commentItem.get매수인());
		stmt.setString(2, commentItem == null ? null : commentItem.get입찰수());
		stmt.setString(3, commentItem == null ? null : commentItem.get매수금액());
		stmt.setString(4, commentItem == null ? null : commentItem.get금액대비());
		stmt.setLong(5, id);
		stmt.execute();

		매각기일UpdaterDB ap = new 매각기일UpdaterDB(listener);
		ap.update(id);

		// try {
		// conn.commit();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		update최저가(id);
	}

	/**
	 * Update매각기일현황s.
	 * 
	 * @param rs the rs
	 * 
	 * @throws Exception the exception
	 */
	private void update매각기일현황s(ResultSet rs) throws Exception {
		listener.log("물건의 매각기일현황을 갱신합니다.");

		LinkedList<물건Item> list = new LinkedList<물건Item>();
		while (rs.next()) {
			String id = rs.getString(1);
			String court = rs.getString(2);
			String charge = rs.getString(3);
			String event_no = rs.getString(4);
			String no = rs.getString(5);

			물건Item target = new 물건Item(id, court, charge, event_no, no);
			list.add(target);

		}

		// conn.setAutoCommit(false);
		for (물건Item target : list) {
			try {

				listener.log("갱신 대상 물건:" + target);
				스피드옥션매각기일현황 state = fetch스피드옥션_매각기일현황(target);
				update매각기일현황(target.getId(), state);
				// conn.commit();
				// listener.progress(1);
			} catch (Exception e) {
				// conn.rollback();
				e.printStackTrace();
				logger.log(Level.FINE, "업데이트중 에러 ", e);
			}
		}
		// conn.setAutoCommit(true);
		rs.close();
	}

	/**
	 * Update물건매각가격.
	 * 
	 * @param id the id
	 * @param price the price
	 * @param result the result
	 * 
	 * @throws Exception the exception
	 */
	private void update물건매각가격(long id, String price, String result) throws Exception {
		listener.log("물건의 정보를 갱신한다. 매각가격=" + price + ", 매각결과=" + result);
		PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET last_sell_price=?, last_sell_result=? WHERE id=?;");
		stmt.setString(1, price);
		stmt.setString(2, result);
		stmt.setLong(3, id);

		stmt.executeUpdate();
	}

	/**
	 * Update최저가.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	private void update최저가(long id) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT lowest_price FROM ac_appoint_statement WHERE goods_id=" + id + ";");

		LinkedList<String> list = new LinkedList<String>();
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		rs.close();

		// System.out.println(list);
		String lowest = "";
		for (String value : list) {
			if (value == null || "".equals(value)) {
				continue;
			}
			lowest = value;
		}
		// System.out.println(lowest);
		PreparedStatement stmt2 = prepareStatement("UPDATE ac_goods SET lowest_price=? WHERE id=?;");
		stmt2.setString(1, lowest == null ? "" : lowest);
		stmt2.setLong(2, id);

		stmt2.execute();

	}

	/**
	 * Letter6.
	 * 
	 * @param text the text
	 * 
	 * @return the string
	 */
	public static String letter6(String text) {
		String res = "";
		for (int i = text.length(); i < 6; i++) {
			res += "0";
		}
		return res + text;
	}

	/**
	 * No.
	 * 
	 * @param event_no the event_no
	 * 
	 * @return the int
	 */
	public static int no(String event_no) {
		return Integer.parseInt(event_no.substring(7));
	}

	/**
	 * Year.
	 * 
	 * @param event_no the event_no
	 * 
	 * @return the int
	 */
	public static int year(String event_no) {
		return Integer.parseInt(event_no.substring(0, 4));
	}
}
