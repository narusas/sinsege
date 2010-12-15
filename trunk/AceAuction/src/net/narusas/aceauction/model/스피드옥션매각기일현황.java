package net.narusas.aceauction.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ���ǵ���ǸŰ�������Ȳ {
	public static class Item {
		public int size;

		String comment;

		String date;

		String no;

		String price;

		String result;

		private String �ż���;

		private String �ż��ݾ�;

		private String ������;

		private String �ݾ״��;

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
			Pattern p = Pattern.compile("�ż���(.*)");
			Matcher m = p.matcher(tokens[0]);
			if (m.find()) {
				�ż��� = m.group(1);
			}

			p = Pattern.compile("����(.*)");
			m = p.matcher(tokens[1]);
			if (m.find()) {
				������ = m.group(1);
			}

			String[] tokens2 = tokens[2].split("\\(");
			p = Pattern.compile("�Ű�(.*)");
			m = p.matcher(tokens2[0]);
			if (m.find()) {
				�ż��ݾ� = m.group(1);
				�ݾ״�� = tokens2[1].replaceAll("\\)", "");
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

		public String get�ż���() {
			return �ż���;
		}

		public void set�ż���(String �ż���) {
			this.�ż��� = �ż���;
		}

		public String get�ż��ݾ�() {
			return �ż��ݾ�;
		}

		public void set�ż��ݾ�(String �ż��ݾ�) {
			this.�ż��ݾ� = �ż��ݾ�;
		}

		public String get������() {
			return ������;
		}

		public void set������(String ������) {
			this.������ = ������;
		}

		public String get�ݾ״��() {
			return �ݾ״��;
		}

		public void set�ݾ״��(String �ݾ״��) {
			this.�ݾ״�� = �ݾ״��;
		}

	}

	private List<Item> items;

	private final Table table;

	private final String ������;

	public ���ǵ���ǸŰ�������Ȳ(Table table, String ������) {

		this.table = table;
		this.������ = ������;
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

	public String get������() {
		return ������;
	}

	public int size() {
		return items.size();
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
