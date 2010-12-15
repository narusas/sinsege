package net.narusas.aceauction.data.builder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.기일내역Item;
import net.narusas.aceauction.model.물건;

/**
 * 기일내역에 관한 DB를 처리하는 클래스.
 * 
 * @author narusas
 * 
 */
public class 기일내역Builder {

	private final int id;

	private final 물건 물건;
	static Logger logger = Logger.getLogger("log");

	public 기일내역Builder(int id, 물건 물건) {
		this.id = id;
		this.물건 = 물건;
	}

	public void update() throws Exception {
		기일내역Item.removeForGoodsId(id);
		insert기일내역(id, 물건);
	}

	private void insert기일내역(int id, 물건 mul) throws Exception {
		logger.info("기일 내역을 입력합니다");
		DB.reConnect();
		List<기일내역Item> list = mul.get기일내역();
		Date bid_start = null;
		Date bid_end = null;
		for (int i = 0; i < list.size(); i++) {
			기일내역Item item = list.get(i);
			insert기일내역Item(id, item, i);
			if (item.get기간입찰_시작() != null) {
				bid_start = item.get기간입찰_시작();
				bid_end = item.get기간입찰_종료();
			}
		}

		logger.info("기일내역의 입찰기간 정보를 ac_goods에 입력합니다. 입찰시작=" + bid_start + ", 입찰종료=" + bid_end);

		if (bid_start == null) {
			return;
		}
		PreparedStatement stmt = null;

		try {
			stmt = DB.prepareStatement("UPDATE ac_goods SET bid_start=?,bid_end=? WHERE id=?;");
			stmt.setDate(1, bid_start);
			stmt.setDate(2, bid_end);
			stmt.setLong(3, id);

			stmt.executeUpdate();
		} finally {
			DB.cleanup(stmt);
		}
	}

	private void insert기일내역Item(int id, 기일내역Item item, int index) throws Exception {
		item.setIndex(index);
		item.insert(id);

	}
}
