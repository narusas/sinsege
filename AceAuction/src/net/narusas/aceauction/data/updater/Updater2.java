package net.narusas.aceauction.data.updater;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.�Ű�����UpdaterDB;
import net.narusas.aceauction.fetchers.����Fetcher;
import net.narusas.aceauction.fetchers.��������ϳ���Fetcher_���;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.���ϳ���Item;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class Updater2 extends DB implements Runnable {
	static Logger logger = Logger.getLogger("log");

	private Date end;

	private long sa_no;

	private Date start;

	private ���� ����;

	private ���� ����;

	private List<����> ����s;

	public Updater2(List<����> ����s, Date start, Date end) {
		this.����s = ����s;
		this.start = start;
		this.end = end;
	}

	public Updater2(���� ����) {
		this.���� = ����;
	}

	public Updater2(���� ����, long sa_no) {
		this.���� = ����;
		this.sa_no = sa_no;
	}

	public Updater2(���� ����, Date start, Date end) {
		this.���� = ����;
		this.start = start;
		this.end = end;
	}

	public void run() {
		if (this.���� == null && sa_no == 0 && this.���� == null) {
			logger.log(Level.INFO, "���� �Ǵ� ����� �������� �ʾ����Ƿ� �����մϴ�.");
			return;
		}
		if (sa_no != 0) {
			updateSagun(����, sa_no);
		} else if (���� != null) {
			updateCharge(����);
		} else if (���� != null && ����s == null) {
			updateCourt(����, start, end);
		} else if (����s != null) {
			updateCourts(����s, start, end);
		}
		logger.info("�����۾��� �����մϴ�");
	}

	public List<���ϳ���Item> update���ϳ���(int mulgun_no, Table ���ϳ���) {
		logger.info("���� ������ �����մϴ�");
		List<���ϳ���Item> res = new LinkedList<���ϳ���Item>();
		if (���ϳ��� == null) {
			return res;
		}

		for (Row record : ���ϳ���.getRows()) {
			String no = record.getValue(0);
			String target = "" + mulgun_no;
			// logger.info("���ϳ��� �׸�� ���ǰ� ��ġ�ϴ��� Ȯ���մϴ�. no=" + no + " target=" +
			// target);
			if (no.equals(target)) {
				���ϳ���Item item = new ���ϳ���Item(record);
				// if (res.contains(item)) {
				// continue;
				// }
				res.add(item);
			}
		}
		return res;
	}

	private long getExisting����Id(���� charge) throws Exception {
		���� cc = ����.findByMemoryObject(charge);
		if (cc != null) {
			return cc.getNo();
		}
		return -1;
	}

	private List<Goods> getExisting����IDs(long sagunId) throws Exception {
		logger.log(Level.INFO, "���� ����� �Ǵ� ������ ID�� DB���� ���ɴϴ�.");
		List<Goods> res = new LinkedList<Goods>();

		Statement stmt = createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM ac_goods WHERE event_no="
						+ sagunId + " AND done=0;");
//		logger.info("SQL:" + "SELECT * FROM ac_goods WHERE event_no=" + sagunId
//				+ " AND done=0;");
		// PreparedStatement stmt = prepareStatement("SELECT * FROM ac_goods
		// WHERE event_no=?;");
		// stmt.setLong(1, sagunId);
		// ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			// logger.info("Finded:"+rs.isLast());
//			logger.log(Level.INFO, "���� id=" + rs.getInt("id") + ", ���ǹ�ȣ="
//					+ rs.getInt("no"));
			res.add(new Goods(rs.getInt("id"), rs.getInt("no")));
		}
		return res;
	}

	private long getExisting���id(���� ����, long sagun_no) throws Exception {
		logger.log(Level.INFO, "���� Code=" + ����.get����().getCode() + ", ��ǹ�ȣ="
				+ sagun_no + " �� ����� �˻��մϴ�. ");
		PreparedStatement stmt = prepareStatement("SELECT * FROM ac_event WHERE no=? AND court_code=?;");
		stmt.setLong(1, sagun_no);
		stmt.setLong(2, toLong(����.get����().getCode()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getLong("id");
		}
		return -1;
	}

	private void insert���ϳ���Item(int id, ���ϳ���Item item, int index)
			throws Exception {
		// if ("�Ű���������".equals(item.get��������())) {
		// return;
		// }
		if (isExist(id, index)) {
			return;
		}
		item.setIndex(index);
		item.insert(id);
	}

	private boolean isExist(int id, int index) throws Exception {
		Statement st = createStatement();
		ResultSet rs = st
				.executeQuery("SELECT * FROM ac_appoint_statement WHERE goods_id="
						+ id + " AND no=" + index + ";");
		boolean result = rs != null && rs.next();
		rs.close();
		return result;
	}

	private void removeExisting���ϳ���(long id) throws Exception {
		logger.log(Level.INFO, "����(" + id + ")�� �ش��ϴ� ���ϳ����� �ʱ�ȭ�մϴ�. ");
		Statement stmt = createStatement();
		stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE goods_id="
				+ id + ";");

	}

	private String toGuarantee(String lowest) {
		long price = toPrice(lowest);
		long gurantee = (int) price / 10;

		return "" + ((int) price / 10);
	}

	private void updateCharge(���� ����) {
		logger.log(Level.INFO, "���� ���踦 �����մϴ�. " + ����.get�����̸�() + ":"
				+ ����.get�Ű�����());
		List<String> sa_nos = new LinkedList<String>();
		new ���_���FetchCommand(����, sa_nos).run();
		for (String each_sa_no : sa_nos) {
			updateSagun(����, Long.parseLong(each_sa_no));
		}
	}

	private void updateCourt(���� ����, Date start, Date end) {
		try {
			logger.log(Level.INFO, "���� ������ �����մϴ�. " + ����.getName());
			����Fetcher fetcher = ����Fetcher.get����Fetcher_�Ű����(����);
			logger.log(Level.INFO, "���� ������ Fetch�մϴ�. ");
			List<����> list = fetcher.fetchCharges();
			for (���� ���� : list) {
				if (����.isInScoop(start, end) == false) {
					logger.info("���� �� ���� �����ȿ� ���Ե��� �ʽ��ϴ�." + ����.get�Ű�����());
					continue;
				}
				updateCharge(����);

			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateCourts(List<����> ����s, Date start, Date end) {
		for (���� ���� : ����s) {
			updateCourt(����, start, end);
		}
	}

	private void updateSagun(���� ����, long each_sa_no) {
		logger.log(Level.INFO, "���� ����� �����մϴ�. ");
		logger.log(Level.INFO, "���� �۾��� �����մϴ�. " + ���� + ":" + each_sa_no);
		logger.log(Level.INFO, "���� ���� ������ ���ɴϴ�");
		��������ϳ���Fetcher_��� f2 = new ��������ϳ���Fetcher_���();
		Table t = f2.get���ϳ���Table(//
				����.get����().getCode(),// 
				����.get�����̸�(),//
				����.get�����ڵ�(),// 
				����.get�Ű�����(), //
				each_sa_no);
		logger.log(Level.INFO, "���� ���� ������ ���Խ��ϴ�. ");

		logger.log(Level.INFO, "���� ����� �Ǵ� ������ DB���� ���ɴϴ�.");

		try {
			�Ű�����UpdaterDB dbUpdater = new �Ű�����UpdaterDB(new UpdaterListener() {

				public void log(String msg) {
					logger.log(Level.INFO, msg);
				}

				public void progress(int progress) {
				}

				public void updateWorkSize(int size) {
				}
			});
			reConnect();
			// int charge_no = getExisting����Id(����);
			long saung_id = getExisting���id(����, each_sa_no);
			List<Goods> goods_ids = getExisting����IDs(saung_id);
			logger.info(goods_ids.size() + "���� ������ �����մϴ�. ");
			for (Goods goods : goods_ids) {
				logger.info("����:" + goods.goods_id + " �� ���� ������ �����մϴ�. ");
				removeExisting���ϳ���(goods.goods_id);
				List<���ϳ���Item> data = update���ϳ���(goods.mul_no, t);
				logger.log(Level.INFO, "���ϳ��� �߰��� �����մϴ�. ");
				for (int i = 0; i < data.size(); i++) {
					insert���ϳ���Item(goods.goods_id, data.get(i), i);
				}
				logger.log(Level.INFO, "���ϳ��� �߰��� �Ϸ��߽��ϴ�. ");

				dbUpdater.update(goods.goods_id);
				update������(goods.goods_id);

				logger.log(Level.INFO, "������ ���¸� �Ϸ���·� �����մϴ�(done=1)");
				PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET done=1 WHERE id=?;");
				stmt.setLong(1, goods.goods_id);
				stmt.execute();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Update Fail", e);
			e.printStackTrace();
		}
	}

	private void update������(long id) throws Exception {
		Statement stmt = createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT lowest_price FROM ac_appoint_statement WHERE goods_id="
						+ id + ";");

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
		logger.log(Level.INFO, "������=" + lowest);
		// PreparedStatement stmt2 = prepareStatement("UPDATE ac_goods SET
		// lowest_price=?, guarantee_price=? WHERE id=?;");
		PreparedStatement stmt2 = prepareStatement("UPDATE ac_goods SET lowest_price=? WHERE id=?;");
		stmt2.setString(1, lowest == null ? "" : lowest);
		// stmt2.setString(2, lowest == null ? "" : "10% (?" +
		// toGuarantee(lowest) + ")");
		stmt2.setLong(2, id);

		stmt2.execute();

	}

}

class Goods {

	public final int goods_id;

	public final int mul_no;

	public Goods(int goods_id, int mul_no) {
		this.goods_id = goods_id;
		this.mul_no = mul_no;
	}
}
