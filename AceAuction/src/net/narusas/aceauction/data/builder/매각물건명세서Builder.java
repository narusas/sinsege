package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.금액Converter;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.매각물건명세서;
import net.narusas.aceauction.model.명세서;

public class 매각물건명세서Builder {

	private final int goods_id;

	private final 명세서 명세서;

	public 매각물건명세서Builder(int id, 명세서 명세서) {
		this.goods_id = id;
		this.명세서 = 명세서;
	}

	public void update() throws Exception {
		매각물건명세서 명세서 = 매각물건명세서.findByGoodsId(goods_id);
		if (명세서 != null) {
			명세서.delete();
		}
		insert명세서();
	}

	private void insert명세서() throws Exception {
		DB.reConnect();
		if (명세서 == null) {// || 명세서.get점유자() == null) {
			return;
		}

		update물건명세서Comment(명세서);

		if (명세서.get점유자() == null) {
			return;
		}

		List<Row> records = 명세서.get점유자().getRows();
		for (Row record : records) {
			insert명세서(record);
		}
	}

	private void insert명세서(Row record) throws Exception {

		String first = record.getValue(0);
		if (first.contains("내역 없음")) {
			return;
		}
		String name = record.getValue(0); // 1
		String possessory = record.getValue(1); // 2
		String info_source = record.getValue(2); // 3
		String ground_claim = record.getValue(3); // 4
		String period = record.getValue(4); // 5
		String guaranty_price = 금액Converter.convert(record.getValue(5)); // 6
		String chaim = 금액Converter.convert(record.getValue(6)); // 7
		String report_date = record.getValue(7); // 8
		String conclusion_date = record.getValue(8); // 9
		String devidend_req_date = record.getValue(9); // 10

		매각물건명세서 item = new 매각물건명세서(goods_id, name, possessory, info_source,
				ground_claim, period, guaranty_price, chaim, report_date,
				conclusion_date, devidend_req_date);

		item.insert();

	}

	private void update물건명세서Comment(명세서 명세서) throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("UPDATE  ac_goods SET goods_statememt_comment=?,goods_statememt_comment3=?,goods_statememt_comment2=?,goods_statememt_comment4=?  WHERE id=?;");
		stmt.setString(1, 명세서.get비고());
		stmt.setString(2, 명세서.get개요());
		stmt.setString(3, 명세서.get권리());
		stmt.setString(4, 명세서.get비고란());
		stmt.setLong(5, goods_id);
		stmt.execute();
	}
}
