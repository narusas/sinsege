/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ���ǵ���ǸŰ�������Ȳ.
 */
public class ���ǵ���ǸŰ�������Ȳ {
	
	/**
	 * The Class Item.
	 */
	public static class Item {
		
		/** The size. */
		public int size;

		/** The comment. */
		String comment;

		/** The date. */
		String date;

		/** The no. */
		String no;

		/** The price. */
		String price;

		/** The result. */
		String result;

		/** The �ż���. */
		private String �ż���;

		/** The �ż��ݾ�. */
		private String �ż��ݾ�;

		/** The ������. */
		private String ������;

		/** The �ݾ״��. */
		private String �ݾ״��;

		/**
		 * Instantiates a new item.
		 * 
		 * @param r the r
		 */
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

		/**
		 * Gets the comment.
		 * 
		 * @return the comment
		 */
		public String getComment() {
			return comment;
		}

		/**
		 * Gets the date.
		 * 
		 * @return the date
		 */
		public Date getDate() {
			String[] tokens = date.split("-");
			return new Date(Integer.parseInt(tokens[0]) - 1900, Integer.parseInt(tokens[1]) - 1, Integer
					.parseInt(tokens[2]));
		}

		/**
		 * Gets the no.
		 * 
		 * @return the no
		 */
		public String getNo() {
			return no;
		}

		/**
		 * Gets the price.
		 * 
		 * @return the price
		 */
		public String getPrice() {
			return price;
		}

		/**
		 * Gets the result.
		 * 
		 * @return the result
		 */
		public String getResult() {
			return result;
		}

		/**
		 * Gets the time.
		 * 
		 * @return the time
		 */
		public long getTime() {
			String[] tokens = date.split("-");
			int year = Integer.parseInt(tokens[0]);
			int month = Integer.parseInt(tokens[1]);
			int date = Integer.parseInt(tokens[2]);
			Date d = new java.sql.Date(year - 1900, month - 9, date);
			return d.getTime();
		}

		/**
		 * Sets the comment.
		 * 
		 * @param value the new comment
		 */
		public void setComment(String value) {
			comment = value;
			parseComment(comment);
		}

		/**
		 * Parses the comment.
		 * 
		 * @param comment the comment
		 */
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

		/**
		 * Sets the date.
		 * 
		 * @param date the new date
		 */
		public void setDate(String date) {
			this.date = date;
		}

		/**
		 * Sets the no.
		 * 
		 * @param no the new no
		 */
		public void setNo(String no) {
			this.no = no;
		}

		/**
		 * Sets the price.
		 * 
		 * @param price the new price
		 */
		public void setPrice(String price) {
			this.price = price;
		}

		/**
		 * Sets the result.
		 * 
		 * @param result the new result
		 */
		public void setResult(String result) {
			this.result = result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "[" + no + "," + date + "," + price + "," + result + "," + comment + "]";
		}

		/**
		 * Gets the �ż���.
		 * 
		 * @return the �ż���
		 */
		public String get�ż���() {
			return �ż���;
		}

		/**
		 * Sets the �ż���.
		 * 
		 * @param �ż��� the new �ż���
		 */
		public void set�ż���(String �ż���) {
			this.�ż��� = �ż���;
		}

		/**
		 * Gets the �ż��ݾ�.
		 * 
		 * @return the �ż��ݾ�
		 */
		public String get�ż��ݾ�() {
			return �ż��ݾ�;
		}

		/**
		 * Sets the �ż��ݾ�.
		 * 
		 * @param �ż��ݾ� the new �ż��ݾ�
		 */
		public void set�ż��ݾ�(String �ż��ݾ�) {
			this.�ż��ݾ� = �ż��ݾ�;
		}

		/**
		 * Gets the ������.
		 * 
		 * @return the ������
		 */
		public String get������() {
			return ������;
		}

		/**
		 * Sets the ������.
		 * 
		 * @param ������ the new ������
		 */
		public void set������(String ������) {
			this.������ = ������;
		}

		/**
		 * Gets the �ݾ״��.
		 * 
		 * @return the �ݾ״��
		 */
		public String get�ݾ״��() {
			return �ݾ״��;
		}

		/**
		 * Sets the �ݾ״��.
		 * 
		 * @param �ݾ״�� the new �ݾ״��
		 */
		public void set�ݾ״��(String �ݾ״��) {
			this.�ݾ״�� = �ݾ״��;
		}

	}

	/** The items. */
	private List<Item> items;

	/** The table. */
	private final Table table;

	/** The ������. */
	private final String ������;

	/**
	 * Instantiates a new ���ǵ���ǸŰ�������Ȳ.
	 * 
	 * @param table the table
	 * @param ������ the ������
	 */
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

	/**
	 * Gets the.
	 * 
	 * @param i the i
	 * 
	 * @return the item
	 */
	public Item get(int i) {
		return items.get(i);
	}

	/**
	 * Gets the last.
	 * 
	 * @return the last
	 */
	public Item getLast() {
		if (items.size() == 0) {
			return null;
		}
		return items.get(items.size() - 1);
	}

	/**
	 * Gets the ������.
	 * 
	 * @return the ������
	 */
	public String get������() {
		return ������;
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public int size() {
		return items.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return items.toString();
	}
}
