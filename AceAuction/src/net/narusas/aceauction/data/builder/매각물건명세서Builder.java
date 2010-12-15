package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.�ݾ�Converter;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.�Ű����Ǹ���;
import net.narusas.aceauction.model.����;

public class �Ű����Ǹ���Builder {

	private final int goods_id;

	private final ���� ����;

	public �Ű����Ǹ���Builder(int id, ���� ����) {
		this.goods_id = id;
		this.���� = ����;
	}

	public void update() throws Exception {
		�Ű����Ǹ��� ���� = �Ű����Ǹ���.findByGoodsId(goods_id);
		if (���� != null) {
			����.delete();
		}
		insert����();
	}

	private void insert����() throws Exception {
		DB.reConnect();
		if (���� == null) {// || ����.get������() == null) {
			return;
		}

		update���Ǹ���Comment(����);

		if (����.get������() == null) {
			return;
		}

		List<Row> records = ����.get������().getRows();
		for (Row record : records) {
			insert����(record);
		}
	}

	private void insert����(Row record) throws Exception {

		String first = record.getValue(0);
		if (first.contains("���� ����")) {
			return;
		}
		String name = record.getValue(0); // 1
		String possessory = record.getValue(1); // 2
		String info_source = record.getValue(2); // 3
		String ground_claim = record.getValue(3); // 4
		String period = record.getValue(4); // 5
		String guaranty_price = �ݾ�Converter.convert(record.getValue(5)); // 6
		String chaim = �ݾ�Converter.convert(record.getValue(6)); // 7
		String report_date = record.getValue(7); // 8
		String conclusion_date = record.getValue(8); // 9
		String devidend_req_date = record.getValue(9); // 10

		�Ű����Ǹ��� item = new �Ű����Ǹ���(goods_id, name, possessory, info_source,
				ground_claim, period, guaranty_price, chaim, report_date,
				conclusion_date, devidend_req_date);

		item.insert();

	}

	private void update���Ǹ���Comment(���� ����) throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("UPDATE  ac_goods SET goods_statememt_comment=?,goods_statememt_comment3=?,goods_statememt_comment2=?,goods_statememt_comment4=?  WHERE id=?;");
		stmt.setString(1, ����.get���());
		stmt.setString(2, ����.get����());
		stmt.setString(3, ����.get�Ǹ�());
		stmt.setString(4, ����.get����());
		stmt.setLong(5, goods_id);
		stmt.execute();
	}
}
