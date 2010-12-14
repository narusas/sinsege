package net.narusas.si.auction.model;

import java.util.LinkedList;
import java.util.List;

import net.narusas.si.auction.fetchers.Sheet;

public class 배당요구종기내역 {
	사건 사건;
	Sheet sheet;
	private LinkedList<배당요구종기내역Item> list;

	public 배당요구종기내역() {
	}

	public 배당요구종기내역(Sheet sheet2) {
		setSheet(sheet2);
	}

	public 사건 get사건() {
		return 사건;
	}

	public void set사건(사건 사건) {
		this.사건 = 사건;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
		list = new LinkedList<배당요구종기내역Item>();
		for (int i = 0; i < sheet.rowSize(); i++) {
			String 목록번호 = sheet.valueAt(i, 0);
			String 소재지 = sheet.valueAt(i, 1);
			String 배당요구종기일 = sheet.valueAt(i, 2);

			list.add(new 배당요구종기내역Item(목록번호, 소재지, 배당요구종기일));
		}
	}

	public int size() {
		return sheet.rowSize();
	}

	public List<배당요구종기내역Item> get배당요구종기내역Items() {
		return list;
	}

}
