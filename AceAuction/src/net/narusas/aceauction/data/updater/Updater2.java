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
import net.narusas.aceauction.data.매각기일UpdaterDB;
import net.narusas.aceauction.fetchers.담당계Fetcher;
import net.narusas.aceauction.fetchers.대법원기일내역Fetcher_결과;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.기일내역Item;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class Updater2 extends DB implements Runnable {
	static Logger logger = Logger.getLogger("log");

	private Date end;

	private long sa_no;

	private Date start;

	private 담당계 담당계;

	private 법원 법원;

	private List<법원> 법원s;

	public Updater2(List<법원> 법원s, Date start, Date end) {
		this.법원s = 법원s;
		this.start = start;
		this.end = end;
	}

	public Updater2(담당계 담당계) {
		this.담당계 = 담당계;
	}

	public Updater2(담당계 담당계, long sa_no) {
		this.담당계 = 담당계;
		this.sa_no = sa_no;
	}

	public Updater2(법원 법원, Date start, Date end) {
		this.법원 = 법원;
		this.start = start;
		this.end = end;
	}

	public void run() {
		if (this.법원 == null && sa_no == 0 && this.담당계 == null) {
			logger.log(Level.INFO, "담당계 또는 사건이 지정되지 않았으므로 종료합니다.");
			return;
		}
		if (sa_no != 0) {
			updateSagun(담당계, sa_no);
		} else if (담당계 != null) {
			updateCharge(담당계);
		} else if (법원 != null && 법원s == null) {
			updateCourt(법원, start, end);
		} else if (법원s != null) {
			updateCourts(법원s, start, end);
		}
		logger.info("갱신작업을 종료합니다");
	}

	public List<기일내역Item> update기일내역(int mulgun_no, Table 기일내역) {
		logger.info("기일 내역을 갱신합니다");
		List<기일내역Item> res = new LinkedList<기일내역Item>();
		if (기일내역 == null) {
			return res;
		}

		for (Row record : 기일내역.getRows()) {
			String no = record.getValue(0);
			String target = "" + mulgun_no;
			// logger.info("기일내역 항목과 물건과 일치하는지 확인합니다. no=" + no + " target=" +
			// target);
			if (no.equals(target)) {
				기일내역Item item = new 기일내역Item(record);
				// if (res.contains(item)) {
				// continue;
				// }
				res.add(item);
			}
		}
		return res;
	}

	private long getExisting담당계Id(담당계 charge) throws Exception {
		담당계 cc = 담당계.findByMemoryObject(charge);
		if (cc != null) {
			return cc.getNo();
		}
		return -1;
	}

	private List<Goods> getExisting물건IDs(long sagunId) throws Exception {
		logger.log(Level.INFO, "갱신 대상이 되는 물건의 ID을 DB에서 얻어옵니다.");
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
//			logger.log(Level.INFO, "물건 id=" + rs.getInt("id") + ", 물건번호="
//					+ rs.getInt("no"));
			res.add(new Goods(rs.getInt("id"), rs.getInt("no")));
		}
		return res;
	}

	private long getExisting사건id(담당계 담당계, long sagun_no) throws Exception {
		logger.log(Level.INFO, "법원 Code=" + 담당계.get법원().getCode() + ", 사건번호="
				+ sagun_no + " 인 사건을 검색합니다. ");
		PreparedStatement stmt = prepareStatement("SELECT * FROM ac_event WHERE no=? AND court_code=?;");
		stmt.setLong(1, sagun_no);
		stmt.setLong(2, toLong(담당계.get법원().getCode()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getLong("id");
		}
		return -1;
	}

	private void insert기일내역Item(int id, 기일내역Item item, int index)
			throws Exception {
		// if ("매각결정기일".equals(item.get기일종류())) {
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

	private void removeExisting기일내역(long id) throws Exception {
		logger.log(Level.INFO, "물건(" + id + ")에 해당하는 기일내역을 초기화합니다. ");
		Statement stmt = createStatement();
		stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE goods_id="
				+ id + ";");

	}

	private String toGuarantee(String lowest) {
		long price = toPrice(lowest);
		long gurantee = (int) price / 10;

		return "" + ((int) price / 10);
	}

	private void updateCharge(담당계 담당계) {
		logger.log(Level.INFO, "단일 담당계를 갱신합니다. " + 담당계.get담당계이름() + ":"
				+ 담당계.get매각기일());
		List<String> sa_nos = new LinkedList<String>();
		new 사건_결과FetchCommand(담당계, sa_nos).run();
		for (String each_sa_no : sa_nos) {
			updateSagun(담당계, Long.parseLong(each_sa_no));
		}
	}

	private void updateCourt(법원 법원, Date start, Date end) {
		try {
			logger.log(Level.INFO, "단일 법원를 갱신합니다. " + 법원.getName());
			담당계Fetcher fetcher = 담당계Fetcher.get담당계Fetcher_매각결과(법원);
			logger.log(Level.INFO, "담당계 정보를 Fetch합니다. ");
			List<담당계> list = fetcher.fetchCharges();
			for (담당계 담당계 : list) {
				if (담당계.isInScoop(start, end) == false) {
					logger.info("담당계 가 날자 범위안에 포함되지 않습니다." + 담당계.get매각기일());
					continue;
				}
				updateCharge(담당계);

			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateCourts(List<법원> 법원s, Date start, Date end) {
		for (법원 법원 : 법원s) {
			updateCourt(법원, start, end);
		}
	}

	private void updateSagun(담당계 담당계, long each_sa_no) {
		logger.log(Level.INFO, "단일 사건을 갱신합니다. ");
		logger.log(Level.INFO, "갱신 작업을 시작합니다. " + 담당계 + ":" + each_sa_no);
		logger.log(Level.INFO, "종료 기일 내역을 얻어옵니다");
		대법원기일내역Fetcher_결과 f2 = new 대법원기일내역Fetcher_결과();
		Table t = f2.get기일내역Table(//
				담당계.get법원().getCode(),// 
				담당계.get담당계이름(),//
				담당계.get담당계코드(),// 
				담당계.get매각기일(), //
				each_sa_no);
		logger.log(Level.INFO, "종료 기일 내역을 얻어왔습니다. ");

		logger.log(Level.INFO, "갱신 대상이 되는 물건을 DB에서 얻어옵니다.");

		try {
			매각기일UpdaterDB dbUpdater = new 매각기일UpdaterDB(new UpdaterListener() {

				public void log(String msg) {
					logger.log(Level.INFO, msg);
				}

				public void progress(int progress) {
				}

				public void updateWorkSize(int size) {
				}
			});
			reConnect();
			// int charge_no = getExisting담당계Id(담당계);
			long saung_id = getExisting사건id(담당계, each_sa_no);
			List<Goods> goods_ids = getExisting물건IDs(saung_id);
			logger.info(goods_ids.size() + "개의 물건을 갱신합니다. ");
			for (Goods goods : goods_ids) {
				logger.info("물건:" + goods.goods_id + " 의 기일 내역을 갱신합니다. ");
				removeExisting기일내역(goods.goods_id);
				List<기일내역Item> data = update기일내역(goods.mul_no, t);
				logger.log(Level.INFO, "기일내역 추가를 시작합니다. ");
				for (int i = 0; i < data.size(); i++) {
					insert기일내역Item(goods.goods_id, data.get(i), i);
				}
				logger.log(Level.INFO, "기일내역 추가를 완료했습니다. ");

				dbUpdater.update(goods.goods_id);
				update최저가(goods.goods_id);

				logger.log(Level.INFO, "물건의 상태를 완료상태로 변경합니다(done=1)");
				PreparedStatement stmt = prepareStatement("UPDATE  ac_goods SET done=1 WHERE id=?;");
				stmt.setLong(1, goods.goods_id);
				stmt.execute();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Update Fail", e);
			e.printStackTrace();
		}
	}

	private void update최저가(long id) throws Exception {
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
		logger.log(Level.INFO, "최저가=" + lowest);
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
