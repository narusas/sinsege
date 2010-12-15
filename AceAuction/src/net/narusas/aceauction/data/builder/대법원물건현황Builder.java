package net.narusas.aceauction.data.builder;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.물건현황;

/**
 * 대법원 사이트에서 얻어온 물건 현황을 처리하는 클래스.
 * 
 * @author narusas
 * 
 */
public class 대법원물건현황Builder {

	static Logger logger = Logger.getLogger("log");

	private final int id;

	private final List<물건현황> 물건현황s;

	public 대법원물건현황Builder(int id, List<물건현황> 물건현황) {
		this.id = id;
		this.물건현황s = 물건현황;
	}

	public void update() throws Exception {
		if (물건현황s == null || 물건현황s.size() == 0) {
			return;
		}
		insert물건현황s();
	}

	private void insert물건현황(int no, 물건현황 item, List<물건현황> db현황)
			throws Exception {
		if (물건현황.exitIn(db현황, no)) {
			return;
		}

		item.setGoodsId(id);
		item.setNo(no);

		item.insert();
	}

	private void insert물건현황s() throws Exception {
		DB.reConnect();
		List<물건현황> db현황 = 물건현황.findByGoodsId(id);
		for (int i = 0; i < 물건현황s.size(); i++) {
			int no = i + 1;// 번호는 1부터 시작한다.
			insert물건현황(no, 물건현황s.get(i), db현황);
		}
	}
}
