package net.narusas.si.auction.fetchers;

import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.선박;

public class 선박표시목록Builder {

	public void build(String html, Sheet sheet, 물건 goods) {
		for (int i = 0; i < sheet.rowSize(); i++) {
			String 목록번호 = sheet.valueAt(i, 0);
			String 목록구분 = sheet.valueAt(i, 1);
			String 상세내역 = sheet.valueAt(i, 2);
			try {

				선박 선박 = new 선박(goods, 목록번호, 상세내역);
				goods.add선박(선박);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
