package net.narusas.aceauction.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 스피드옥션매각기일현황 {
	public static class Item {
		public int size;

		String comment;

		String date;

		String no;

		String price;

		String result;

		private String 매수인;

		private String 매수금액;

		private String 입찰수;

		private String 금액대비;

		public Item(Row r) {
			size = r.size();
			if (r.size() == 1) {

				result = r.getValue(0);
			}
			if (r.size() == 3) {
				no = r.getValue(0);
				date = r.getValue(1);
				result = r.getValue(2);
			} else {
				no = r.getValue(0);
				date = r.getValue(1);
				price = r.getValue(2);
				result = r.getValue(3);
			}
		}

		public String getComment() {
			return comment;
		}

		public Date getDate() {
			String[] tokens = date.split("-");
			return new Date(Integer.parseInt(tokens[0]) - 1900, Integer.parseInt(tokens[1]) - 1, Integer
					.parseInt(tokens[2]));
		}

		public String getNo() {
			return no;
		}

		public String getPrice() {
			return price;
		}

		public String getResult() {
			return result;
		}

		public long getTime() {
			String[] tokens = date.split("-");
			int year = Integer.parseInt(tokens[0]);
			int month = Integer.parseInt(tokens[1]);
			int date = Integer.parseInt(tokens[2]);
			Date d = new java.sql.Date(year - 1900, month - 9, date);
			return d.getTime();
		}

		public void setComment(String value) {
			comment = value;
			parseComment(comment);
		}

		private void parseComment(String comment) {
			String[] tokens = comment.split("/");
			Pattern p = Pattern.compile("매수인(.*)");
			Matcher m = p.matcher(tokens[0]);
			if (m.find()) {
				매수인 = m.group(1);
			}

			p = Pattern.compile("입찰(.*)");
			m = p.matcher(tokens[1]);
			if (m.find()) {
				입찰수 = m.group(1);
			}

			String[] tokens2 = tokens[2].split("\\(");
			p = Pattern.compile("매각(.*)");
			m = p.matcher(tokens2[0]);
			if (m.find()) {
				매수금액 = m.group(1);
				금액대비 = tokens2[1].replaceAll("\\)", "");
			}

		}

		public void setDate(String date) {
			this.date = date;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public void setResult(String result) {
			this.result = result;
		}

		@Override
		public String toString() {
			return "[" + no + "," + date + "," + price + "," + result + "," + comment + "]";
		}

		public String get매수인() {
			return 매수인;
		}

		public void set매수인(String 매수인) {
			this.매수인 = 매수인;
		}

		public String get매수금액() {
			return 매수금액;
		}

		public void set매수금액(String 매수금액) {
			this.매수금액 = 매수금액;
		}

		public String get입찰수() {
			return 입찰수;
		}

		public void set입찰수(String 입찰수) {
			this.입찰수 = 입찰수;
		}

		public String get금액대비() {
			return 금액대비;
		}

		public void set금액대비(String 금액대비) {
			this.금액대비 = 금액대비;
		}

	}

	private List<Item> items;

	private final Table table;

	private final String 보증금;

	public 스피드옥션매각기일현황(Table table, String 보증금) {

		this.table = table;
		this.보증금 = 보증금;
		items = new LinkedList<Item>();
		List<Row> records = table.getRows();
		Item item = null;
		for (int i = 0; i < records.size(); i++) {
			Row r = records.get(i);
			if (r.size() == 4 || r.size() == 3) {
				item = new Item(r);
				items.add(item);
			} else if (item != null) {
				item.setComment(r.getValue(0));
			}
		}
	}

	public Item get(int i) {
		return items.get(i);
	}

	public Item getLast() {
		if (items.size() == 0) {
			return null;
		}
		return items.get(items.size() - 1);
	}

	public String get보증금() {
		return 보증금;
	}

	public int size() {
		return items.size();
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
