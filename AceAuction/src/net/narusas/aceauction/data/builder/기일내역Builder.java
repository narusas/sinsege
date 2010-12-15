package net.narusas.aceauction.data.builder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.���ϳ���Item;
import net.narusas.aceauction.model.����;

/**
 * ���ϳ����� ���� DB�� ó���ϴ� Ŭ����.
 * 
 * @author narusas
 * 
 */
public class ���ϳ���Builder {

	private final int id;

	private final ���� ����;
	static Logger logger = Logger.getLogger("log");

	public ���ϳ���Builder(int id, ���� ����) {
		this.id = id;
		this.���� = ����;
	}

	public void update() throws Exception {
		���ϳ���Item.removeForGoodsId(id);
		insert���ϳ���(id, ����);
	}

	private void insert���ϳ���(int id, ���� mul) throws Exception {
		logger.info("���� ������ �Է��մϴ�");
		DB.reConnect();
		List<���ϳ���Item> list = mul.get���ϳ���();
		Date bid_start = null;
		Date bid_end = null;
		for (int i = 0; i < list.size(); i++) {
			���ϳ���Item item = list.get(i);
			insert���ϳ���Item(id, item, i);
			if (item.get�Ⱓ����_����() != null) {
				bid_start = item.get�Ⱓ����_����();
				bid_end = item.get�Ⱓ����_����();
			}
		}

		logger.info("���ϳ����� �����Ⱓ ������ ac_goods�� �Է��մϴ�. ��������=" + bid_start + ", ��������=" + bid_end);

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

	private void insert���ϳ���Item(int id, ���ϳ���Item item, int index) throws Exception {
		item.setIndex(index);
		item.insert(id);

	}
}
