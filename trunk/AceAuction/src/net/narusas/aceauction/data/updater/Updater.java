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
import net.narusas.aceauction.data.�ݾ�Converter;
import net.narusas.aceauction.data.�Ű�����UpdaterDB;
import net.narusas.aceauction.fetchers.���ǵ���ǹ��ǻ󼼳���Fetcher;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���ǵ���ǸŰ�������Ȳ;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;
import net.narusas.aceauction.model.���ǵ���ǸŰ�������Ȳ.Item;

public class Updater extends DB {

	static Logger logger = Logger.getLogger("log");

	static Pattern p = Pattern.compile("�Ű�(.*)��");

	private final UpdaterListener listener;

	private final Date timePoint;

	public Updater(Date pointTime, UpdaterListener listener) {
		this.timePoint = pointTime;
		this.listener = listener;
	}

	public void update() throws Exception {
		listener.log("DB�� �����մϴ�");
		dbConnect();
		try {
			updateGoods(timePoint);
		} catch (Throwable t) {
			t.printStackTrace();
			logger.log(Level.FINE, "�۾��߿���", t);
		}
	}

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

	private ���ǵ���ǸŰ�������Ȳ fetch���ǵ����_�Ű�������Ȳ(����Item target) throws Exception {
		listener.log("���ǵ���ǿ��� �Ű�������Ȳ�� ���ɴϴ�. .");
		���ǵ���ǹ��ǻ󼼳���Fetcher fetcher = new ���ǵ���ǹ��ǻ󼼳���Fetcher();
		String id = target.getId();
		String court = target.getCourt();
		String charge = target.getCharge();
		String event_id = target.getEvent_no();
		String no = target.getNo();

		String evetNo = getEventNo(event_id);
		String page = fetcher.getPage(getCourt(court), chargeCode(charge), year(evetNo), no(evetNo), no);
		// listener.log(page);
		// System.out.println(page);

		if (page.contains("���� ������ �ƴϰų� ���񽺰� ����Ǿ����ϴ�")) {
			JOptionPane.showMessageDialog(null, "���ǵ���� ������ ����Ǿ����ϴ�. ������ �����Ͻð� ���α׷��� �ٽ� �����Ͽ� �ֽʽÿ�");
			System.exit(-1);
		}
		���ǵ���ǹ��ǻ󼼳��� item = fetcher.parse(page);

		return new ���ǵ���ǸŰ�������Ȳ(item.get�Ű�������Ȳ(), item.get������());
	}

	private String getCourt(String court) {
		return ����.findByCode(letter6(court)).get���ǵ����Code();
	}

	private String getEventNo(String event_no) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs2 = stmt.executeQuery("SELECT no FROM ac_event WHERE id=" + event_no + ";");
		rs2.next();
		String evetNo = rs2.getString(1);
		rs2.close();
		return evetNo;
	}

	private �Ű�����Item getLast(List<�Ű�����Item> items) {
		if (items.size() == 0) {
			return null;
		}
		�Ű�����Item item = items.get(items.size() - 1);
		if ("�Ű���������".equals(item.type)) {
			item = items.get(items.size() - 2);
		}
		return item;
	}

	private int getMaxNo(long id) throws Exception {
		PreparedStatement stmt = prepareStatement("SELECT max(no) FROM ac_appoint_statement WHERE goods_id=?;");
		stmt.setLong(1, id);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		return rs.getInt(1);
	}

	private List<�Ű�����Item> get�Ű�������Ȳ(long goodsId) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, no, fixed_date, lowest_price, result, type "
				+ "FROM ac_appoint_statement " + "WHERE goods_id=" + goodsId + ";");
		LinkedList<�Ű�����Item> list = new LinkedList<�Ű�����Item>();
		while (rs.next()) {
			list.add(new �Ű�����Item(rs));
		}
		rs.close();
		return list;
	}

	private void insert�Ű�����Item(long id, Item item, int no) throws Exception {

		// �Ű������ tabel�� �÷����� 1�̴�.
		if (item.getComment() != null) {
			Matcher m = p.matcher(item.getComment());
			if (m.find()) {
				update���ǸŰ�����(id, �ݾ�Converter.convert(m.group(1)), item.getResult());
			}
		}

		listener.log("�Ű������� �߰��Ѵ�. no=" + no + ", date=" + item.getDate() + ", price=" + item.getPrice() + ", result="
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
		stmt.setString(3, �ݾ�Converter.convert(item.getPrice()));
		stmt.setString(4, item.getResult());
		stmt.setLong(5, id);
		stmt.execute();

	}

	private void updateGoods(Date timePoint) throws Exception {
		listener.log("���� ����� �Ǵ� ������ DB���� ���ɴϴ�.");

		PreparedStatement stmt = prepareStatement("SELECT no FROM ac_charge WHERE fixed_date=?;");
		stmt.setDate(1, new java.sql.Date(timePoint.getTime()));
		ResultSet rs = stmt.executeQuery();
		LinkedList<����Item> res = new LinkedList<����Item>();
		while (rs.next()) {
			res.add(new ����Item(rs));
		}
		rs.close();
		listener.updateWorkSize(res.size());
		listener.log("�� �۾��� :" + res.size());
		for (����Item item : res) {
			stmt = prepareStatement("SELECT id, court_code, charge_id, event_no, no " + "FROM ac_goods "
					+ "WHERE charge_id=" + item.getChargeId() + " AND done=0;");
			rs = stmt.executeQuery();
			update�Ű�������Ȳs(rs);
			rs.close();
			listener.progress(1);
		}

	}

	private void update�Ű�����Item(long id, �Ű�����Item lastDBItem, Item item) throws Exception {
		listener.log("�� �����ϴ� �Ű������� �����մϴ�." + item);

		if (item.getComment() != null) {
			Matcher m = p.matcher(item.getComment());
			if (m.find()) {
				update���ǸŰ�����(id, �ݾ�Converter.convert(m.group(1)), item.getResult());
			}
		}

		PreparedStatement stmt = prepareStatement("UPDATE  ac_appoint_statement SET result=? WHERE id=?;");
		stmt.setString(1, item.getResult());
		stmt.setLong(2, lastDBItem.id);
		stmt.executeUpdate();
	}

	private void update�Ű�������Ȳ(String idStr, ���ǵ���ǸŰ�������Ȳ state) throws Exception {

		long id = Long.parseLong(idStr);
		List<�Ű�����Item> items = get�Ű�������Ȳ(id);

		listener.log("������ �Ű�������Ȳ�� �����մϴ�. ");

		int maxNo = getMaxNo(id);
		�Ű�����Item lastDBItem = getLast(items);

		listener.log("������ ��ϵ� ������ �Ű����� ��Ȳ�� ����:" + lastDBItem.date);
		Item commentItem = null;
		for (int i = 0; i < state.size(); i++) {
			Item item = state.get(i);
			if (item.get�ż���() != null) {
				commentItem = item;
			}
			Date lastDBTime = lastDBItem.date;
			Date itemTime = item.getDate();
			listener.log("���ǵ������ �Ű����� ��Ȳ�� ����:" + itemTime);

			if (lastDBTime.compareTo(itemTime) == 0) {
				update�Ű�����Item(id, lastDBItem, item);
			} else if (lastDBTime.compareTo(itemTime) < 0) { // ��������� �о����
				// �Ű����� ��Ϻ��� �ֽ���
				// ������ ���ǵ���ǿ�
				// �ִٸ� �߰��Ѵ�.
				insert�Ű�����Item(id, item, ++maxNo);
			}
		}
		listener.log("�Ű�������Ȳ ���:" + commentItem);
		PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET done=1, buyer=?, bid_count=?, buy_price=?, buy_price_ratio=? WHERE id=?;");
		// stmt.setString(1, state.get������());
		
		stmt.setString(1, commentItem == null ? null : commentItem.get�ż���());
		stmt.setString(2, commentItem == null ? null : commentItem.get������());
		stmt.setString(3, commentItem == null ? null : commentItem.get�ż��ݾ�());
		stmt.setString(4, commentItem == null ? null : commentItem.get�ݾ״��());
		stmt.setLong(5, id);
		stmt.execute();

		�Ű�����UpdaterDB ap = new �Ű�����UpdaterDB(listener);
		ap.update(id);

		// try {
		// conn.commit();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		update������(id);
	}

	private void update�Ű�������Ȳs(ResultSet rs) throws Exception {
		listener.log("������ �Ű�������Ȳ�� �����մϴ�.");

		LinkedList<����Item> list = new LinkedList<����Item>();
		while (rs.next()) {
			String id = rs.getString(1);
			String court = rs.getString(2);
			String charge = rs.getString(3);
			String event_no = rs.getString(4);
			String no = rs.getString(5);

			����Item target = new ����Item(id, court, charge, event_no, no);
			list.add(target);

		}

		// conn.setAutoCommit(false);
		for (����Item target : list) {
			try {

				listener.log("���� ��� ����:" + target);
				���ǵ���ǸŰ�������Ȳ state = fetch���ǵ����_�Ű�������Ȳ(target);
				update�Ű�������Ȳ(target.getId(), state);
				// conn.commit();
				// listener.progress(1);
			} catch (Exception e) {
				// conn.rollback();
				e.printStackTrace();
				logger.log(Level.FINE, "������Ʈ�� ���� ", e);
			}
		}
		// conn.setAutoCommit(true);
		rs.close();
	}

	private void update���ǸŰ�����(long id, String price, String result) throws Exception {
		listener.log("������ ������ �����Ѵ�. �Ű�����=" + price + ", �Ű����=" + result);
		PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET last_sell_price=?, last_sell_result=? WHERE id=?;");
		stmt.setString(1, price);
		stmt.setString(2, result);
		stmt.setLong(3, id);

		stmt.executeUpdate();
	}

	private void update������(long id) throws Exception {
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

	public static String letter6(String text) {
		String res = "";
		for (int i = text.length(); i < 6; i++) {
			res += "0";
		}
		return res + text;
	}

	public static int no(String event_no) {
		return Integer.parseInt(event_no.substring(7));
	}

	public static int year(String event_no) {
		return Integer.parseInt(event_no.substring(0, 4));
	}
}
