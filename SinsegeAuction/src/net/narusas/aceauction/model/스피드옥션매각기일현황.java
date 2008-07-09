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
 * The Class 스피드옥션매각기일현황.
 */
public class 스피드옥션매각기일현황 {
	
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

		/** The 매수인. */
		private String 매수인;

		/** The 매수금액. */
		private String 매수금액;

		/** The 입찰수. */
		private String 입찰수;

		/** The 금액대비. */
		private String 금액대비;

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
		 * Gets the 매수인.
		 * 
		 * @return the 매수인
		 */
		public String get매수인() {
			return 매수인;
		}

		/**
		 * Sets the 매수인.
		 * 
		 * @param 매수인 the new 매수인
		 */
		public void set매수인(String 매수인) {
			this.매수인 = 매수인;
		}

		/**
		 * Gets the 매수금액.
		 * 
		 * @return the 매수금액
		 */
		public String get매수금액() {
			return 매수금액;
		}

		/**
		 * Sets the 매수금액.
		 * 
		 * @param 매수금액 the new 매수금액
		 */
		public void set매수금액(String 매수금액) {
			this.매수금액 = 매수금액;
		}

		/**
		 * Gets the 입찰수.
		 * 
		 * @return the 입찰수
		 */
		public String get입찰수() {
			return 입찰수;
		}

		/**
		 * Sets the 입찰수.
		 * 
		 * @param 입찰수 the new 입찰수
		 */
		public void set입찰수(String 입찰수) {
			this.입찰수 = 입찰수;
		}

		/**
		 * Gets the 금액대비.
		 * 
		 * @return the 금액대비
		 */
		public String get금액대비() {
			return 금액대비;
		}

		/**
		 * Sets the 금액대비.
		 * 
		 * @param 금액대비 the new 금액대비
		 */
		public void set금액대비(String 금액대비) {
			this.금액대비 = 금액대비;
		}

	}

	/** The items. */
	private List<Item> items;

	/** The table. */
	private final Table table;

	/** The 보증금. */
	private final String 보증금;

	/**
	 * Instantiates a new 스피드옥션매각기일현황.
	 * 
	 * @param table the table
	 * @param 보증금 the 보증금
	 */
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
	 * Gets the 보증금.
	 * 
	 * @return the 보증금
	 */
	public String get보증금() {
		return 보증금;
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
