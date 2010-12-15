package net.narusas.aceauction.pdf.jpedal;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;

public class 등기부등본Parser {
	private PdfDecoder decodePdf;

	List<TextPosition> acceptDate;

	List<TextPosition> because;

	TextPosition first = null;

	List<등기부등본_사항> items = new LinkedList<등기부등본_사항>();

	List<TextPosition> priotyNos;

	List<TextPosition> purpose;

	List<TextPosition> res1 = new LinkedList<TextPosition>();

	List<TextPosition> res2 = new LinkedList<TextPosition>();

	List<TextPosition> res3 = new LinkedList<TextPosition>();

	List<TextPosition> res4 = new LinkedList<TextPosition>();

	List<TextPosition> res5 = new LinkedList<TextPosition>();

	List<TextPosition> right;

	int stage = 0;

	public 등기부등본Parser() {
		PdfDecoder.useTextExtraction();
		decodePdf = new PdfDecoder(false);
		decodePdf.setExtractionMode(PdfDecoder.TEXT); // extract just text
		decodePdf.init(true);
		PdfGroupingAlgorithms.useUnrotatedCoords = false;

	}

	/**
	 * 제일 처음 TextPosition을 반환한다. 여기에는 토지,건물,집합건물 같은 등기부등본의 종류를 나타내는 문장이 포함된다.
	 * 
	 * @return
	 */
	public TextPosition getFirst() {
		return first;
	}

	public List<등기부등본_사항> parse(String pdf) throws Exception {
		decodePdf.openPdfFile(pdf);
		int start = 1, end = decodePdf.getPageCount();

		List<TextPosition> list = new LinkedList<TextPosition>();

		collectContents(decodePdf, start, end, list);

		collectColumnsBasic(list);

		bindColumns();

		collectChilds(priotyNos, sort());

		return items;
	}

	public List<등기부등본_사항> parseAll(String file, String[] owners) throws Exception {
		List<등기부등본_사항> it = this.parse(file);
		// return it;
		사항Fixer.fix(it);
		Filter.filter(it, owners);

		List<등기부등본_사항> ok = new LinkedList<등기부등본_사항>();

		for (등기부등본_사항 s : it) {
			if (s.willDelete() == false) {
				ok.add(s);
			}
		}
		return ok;
	}

	private void bindColumns() {
		priotyNos = bindPriotyNos(binding(res1));
		purpose = bindingPurpose(priotyNos, binding(res2));
		acceptDate = binding(res3);
		because = binding(res4);
		TextPositionFix.fixXPos(res5);
		// for (TextPosition tp : res5) {
		// System.out.println(tp);
		// }
		// System.out.println(res5);
		// right = bindingRightbinding들여쓰기(res5));
		right = binding들여쓰기(res5);
		// print(right);
	}

	private List<TextPosition> bindHorizontal들여쓰기(List<TextPosition> temp) {
		List<TextPosition> res2 = new LinkedList<TextPosition>();
		while (temp.size() != 0) {
			TextPosition tp1 = temp.remove(0);
			for (int i = 0; i < temp.size(); i++) {
				TextPosition tp2 = temp.get(i);
				if (tp1.isBinding수직(tp2)) {
					tp1 = tp1.addVertical들여쓰기(tp2);
					temp.remove(i);
					i--;
				}
			}
			res2.add(tp1);
		}
		return res2;
	}

	/**
	 * @param src
	 * @return
	 */
	private List<TextPosition> binding(List<TextPosition> src) {
		List<TextPosition> temp = bind수평(src, 21f);
		List<TextPosition> res2 = bind수직(temp);
		return res2;
	}

	private List<TextPosition> bindingPurpose(List<TextPosition> parent, List<TextPosition> src) {
		List<TextPosition> purposes = new LinkedList<TextPosition>();

		TextPosition last = null;

		while (src.size() > 0) {
			TextPosition t = src.remove(0);
			if (last == null) {
				last = t;
				continue;
			}

			if (last.getPage() == t.getPage()) {
				purposes.add(last);
				last = t;
				continue;
			}

			boolean find = false;
			for (TextPosition prioty : parent) {
				if (prioty.getPage() == t.getPage() && prioty.getY() == t.getY()) {
					find = true;
				}
			}

			if (find) {
				purposes.add(last);
				last = t;
			} else {
				last = last.addVertical(t, true);
			}
		}
		purposes.add(last);
		return purposes;
	}

	private List<TextPosition> bindingRight(List<TextPosition> src) {
		List<TextPosition> purposes = new LinkedList<TextPosition>();

		TextPosition last = null;

		while (src.size() > 0) {
			TextPosition t = src.remove(0);
			if (last == null) {
				last = t;
				continue;
			}

			if (last.getPage() == t.getPage()) {
				purposes.add(last);
				last = t;
				continue;
			}

			boolean find = false;
			for (TextPosition prioty : priotyNos) {
				if (prioty.getPage() == t.getPage() && prioty.getY() == t.getY()) {
					find = true;
				}
			}

			if (find) {
				purposes.add(last);
				last = t;
			} else {
				last = last.addVertical(t, true);
			}
		}
		purposes.add(last);
		return purposes;
	}

	private List<TextPosition> binding들여쓰기(List<TextPosition> src) {
		List<TextPosition> temp = bind수평(src, 40f);
		List<TextPosition> res2 = bindHorizontal들여쓰기(temp);
		return res2;
	}

	/**
	 * 순위번호에 해당하는 TextPosition들을 묶음단위로 만든다.
	 * 
	 * @param temp1
	 * @return
	 */
	private List<TextPosition> bindPriotyNos(List<TextPosition> temp1) {
		List<TextPosition> priotyNos = new LinkedList<TextPosition>();

		TextPosition last = null;
		while (temp1.size() > 0) {
			TextPosition t = temp1.remove(0);
			if (last == null) {
				last = t;
				continue;
			}

			if (last.getPage() == t.getPage()) {
				priotyNos.add(last);
				last = t;
				continue;
			}

			// System.out.println("% Last:"+last+" \nNow:"+t+"\n&");
			if (t.getText().startsWith("(전")) {
				last = last.addVertical(t, true);
			} else {
				priotyNos.add(last);
				last = t;
			}
		}
		priotyNos.add(last);
		return priotyNos;
	}

	/**
	 * @param temp
	 * @return
	 */
	private List<TextPosition> bind수직(List<TextPosition> temp) {
		List<TextPosition> res2 = new LinkedList<TextPosition>();
		while (temp.size() != 0) {
			TextPosition tp1 = temp.remove(0);
			for (int i = 0; i < temp.size(); i++) {
				TextPosition tp2 = temp.get(i);
				if (tp1.isBinding수직(tp2)) {
					tp1 = tp1.addVertical(tp2);
					temp.remove(i);
					i--;
				}
			}
			res2.add(tp1);
		}
		return res2;
	}

	/**
	 * @param src
	 * @param f
	 * @return
	 */
	private List<TextPosition> bind수평(List<TextPosition> src, float f) {
		List<TextPosition> temp = new LinkedList<TextPosition>();
		while (src.size() != 0) {
			TextPosition tp1 = src.remove(0);
			for (int i = 0; i < src.size(); i++) {
				TextPosition tp2 = src.get(i);
				if (tp1.isBinding수평(tp2, f)) {
					tp1 = tp1.add(tp2);
					src.remove(i);
					i--;
				}
			}
			temp.add(tp1);
		}
		return temp;
	}

	private void collectChilds(List<TextPosition> priotyNos, LinkedList<TextPosition> buf) {
		Part part = new Part(priotyNos, purpose, acceptDate, because, right);
		int start = -1;

		TextPosition startEntity = null;
		for (int i = 0; i < buf.size(); i++) {
			TextPosition e = buf.get(i);
			if (priotyNos.contains(e)) {
				if (start == -1) {
					startEntity = e;
					start = i;
					continue;
				}

				// System.out.println("----------------------------");
				List<TextPosition> res = new LinkedList<TextPosition>();
				for (int k = start + 1; k < i; k++) {
					res.add(buf.get(k));
					// System.out.println("###### :"+buf.get(k));
				}

				collectChilds(part, startEntity, res);

				start = i;
				startEntity = e;
			}
		}

		// for (int j = start + 1; j < list.size(); j++) {
		// System.out.println(list.get(j));
		// }

		List<TextPosition> res = new LinkedList<TextPosition>();
		for (int j = start + 1; j < buf.size(); j++) {
			res.add(buf.get(j));
		}
		collectChilds(part, startEntity, res);
	}

	private void collectChilds(Part part, TextPosition startEntity, List<TextPosition> childs) {
		등기부등본_사항 item = new 등기부등본_사항(startEntity);
		for (TextPosition entity : childs) {
			if (part.purpose.contains(entity)) {
				item.addPurpose(entity);
			} else if (part.acceptDate.contains(entity)) {
				item.addAcceptDate(entity);
			} else if (part.because.contains(entity)) {
				item.addBecause(entity);
			} else if (part.right.contains(entity)) {
				item.addRight(entity);
			}
		}
		items.add(item);
	}

	private void collectColumnsBasic(List<TextPosition> list) {
		List<TextPosition> buf1 = new LinkedList<TextPosition>(list);
		while (buf1.size() != 0) {
			TextPosition tp1 = buf1.remove(0);
			if (tp1.getX2() >= 80 && tp1.getX2() < 130 && isInContentsHeight(tp1)) {
				res1.add(tp1);
			} else if (tp1.getX2() >= 130 && tp1.getX2() < 280 && isInContentsHeight(tp1)) {
				res2.add(tp1);
			} else if (tp1.getX2() >= 280 && tp1.getX2() < 370 && isInContentsHeight(tp1)) {
				res3.add(tp1);
			} else if (tp1.getX2() >= 370 && tp1.getX2() < 500 && isInContentsHeight(tp1)) {
				res4.add(tp1);
			} else if (tp1.getX2() >= 500 && isInContentsHeight(tp1)) {
				res5.add(tp1);
			}
		}
	}

	private void collectContents(PdfDecoder decodePdf, int start, int end, List<TextPosition> list)
			throws Exception, PdfException {
		for (int page = start; page < end + 1; page++) {
			decodePdf.decodePage(page);
			PdfGroupingAlgorithms currentGrouping = decodePdf.getGroupingObject();
			PdfPageData currentPageData = decodePdf.getPdfPageData();

			int x1 = currentPageData.getMediaBoxX(page);
			int x2 = currentPageData.getMediaBoxWidth(page) + x1;

			int y2 = currentPageData.getMediaBoxX(page);
			int y1 = currentPageData.getMediaBoxHeight(page) - y2;

			Vector<String> words = currentGrouping.extractTextAsWordlist(x1, y1, x2, y2, page,
					true, true, "");

			// for (int i = 0; i < words.size(); i += 5) {
			// System.out.format("%30s %10s %10s %10s
			// %10s\n",words.get(i),words.get(i+1),words.get(i+2),words.get(i+3),words.get(i+4));
			// }
			// System.out.println("-----------------------------------");
			for (int i = 0; i < words.size(); i += 5) {
				String text = words.get(i);
				if (stage == 0 && page == 1) {
					TextPosition t = new TextPosition(words.get(i), floatValue(words.get(i + 1)),
							y1 - floatValue(words.get(i + 2)), floatValue(words.get(i + 3)), y1
									- floatValue(words.get(i + 4)), page, stage);

					if (first == null) {
						first = t;
						continue;
					}

					if (t.getY() == first.getY()) {
						first = first.add(t);
					}
				}

				if ("【".equals(text)) {
					if ("【".equals(text) && "갑".equals(words.get(i + 5))) {
						stage = 1;
						i = skip(i, words);
					} else if ("【".equals(text) && "을".equals(words.get(i + 5))) {
						stage = 2;
						i = skip(i, words);
					} else {
						stage = 3;
						// i = skip(i, words);
					}
				}

				if ("--".equals(text) /*
										 * && "이".equals(words.get(i + 5))/* &&
										 * "하".equals(words.get(i + 10)) &&
										 * "여".equals(words.get(i + 15)) &&
										 * "백".equals(words.get(i + 20))
										 */) {
					stage = 3;
				}

				if ("열".equals(text) && "281.2105".equals(words.get(i + 1))) {
					continue;
				}
				//				
				if ("람".equals(text) && "377.21024".equals(words.get(i + 1))) {
					continue;
				}
				if ("용".equals(text) && "473.20993".equals(words.get(i + 1))) {
					continue;
				}
				if (stage > 0 && stage <= 2) {
					TextPosition t = new TextPosition(words.get(i), floatValue(words.get(i + 1)),
							y1 - floatValue(words.get(i + 2)), floatValue(words.get(i + 3)), y1
									- floatValue(words.get(i + 4)), page, stage);
					// System.out.println("$$"+t);
					if (isInContentsHeight(t)) {
						list.add(t);
					}
				}
			}

			// java.util.Collections.sort(list, new TextPositionComparator());
		}
	}

	private float floatValue(Object object) {
		return Float.parseFloat((String) object);
	}

	private void print(List<TextPosition> src) {
		System.out.println("###################");
		for (TextPosition position : src) {
			System.out.println("#" + position);
		}
	}

	private int skip(int i, Vector<String> words) {
		while (!words.get(i).equals("항")) {
			i++;
		}
		return i + 5;
	}

	private LinkedList<TextPosition> sort() {
		LinkedList<TextPosition> buf = new LinkedList<TextPosition>();
		buf.addAll(priotyNos);
		buf.addAll(purpose);
		buf.addAll(acceptDate);
		buf.addAll(because);
		buf.addAll(right);
		Collections.sort(buf, new Comparator<TextPosition>() {

			public int compare(TextPosition o1, TextPosition o2) {
				if (o1.getPage() < o2.getPage()) {
					return -1;
				}
				if (o1.getPage() > o2.getPage()) {
					return 1;
				}

				if (o1.getY() < o2.getY()) {
					return -1;
				}
				if (o1.getY() > o2.getY()) {
					return 1;
				}

				if (o1.getX() < o2.getX()) {
					return -1;
				}
				if (o1.getX() > o2.getX()) {
					return 1;
				}
				return 0;
			}
		});
		return buf;
	}

	boolean isInContentsHeight(TextPosition position) {
		return position.getY() > 340 && position.getY() < 750;
	}
}
