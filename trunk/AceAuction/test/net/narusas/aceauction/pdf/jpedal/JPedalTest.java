package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

public class JPedalTest extends TestCase {
	List<등기부등본_사항> items = new LinkedList<등기부등본_사항>();

	private Fixture[] fixtures;

	@Override
	protected void setUp() throws Exception {
		items = new LinkedList<등기부등본_사항>();
		fixtures = new Fixture[] {
				new Fixture(1, "1", new FixtureSub[] { new FixtureSub("소유권보존", "1995년4월18일\n제18980호", "",
						new String[] { "소유자 황을년 491225-2******\n  서울 구로구 고척동 98-42" }) }),
				new Fixture(1, "2", new FixtureSub[] { new FixtureSub("가압류", "1997년12월9일\n제80037호",
						"1997년12월5일\n서울지방\n법원남부지원의 가압류\n결정(97카단6564호)",
						new String[] { "청구금액 금40,000,000원\n권리자 고영식\n  서울 구로구 고척1동 98-43" }) }),
				new Fixture(1, "3", new FixtureSub[] { new FixtureSub("가압류", "1997년12월11일\n제80870호",
						"1997년12월9일\n서울지방\n법원 남부지원의 가압류\n결정(97카합6693호)",
						new String[] { "청구금액 금33,759,103원\n권리자 정병세\n  서울 구로구 고척동 226" }) }),

				new Fixture(1, "4", new FixtureSub[] { new FixtureSub("가압류", "1998년4월7일\n제17577호",
						"1998년4월4일\n서울지방\n법원 가압류\n결정(98카단78975)", new String[] {
								"청구금액 금5,485,577원\n권리자 대한보증보험(주)\n  서울 종로구 연지동 136-74\n  (영등포지점)",
								"부동산등기법시행규칙부칙 제3조 제1항의 규정에 의하여\n1번 내지 4번 등기를 1998년 04월 10일 전산이기" }) }),

				new Fixture(
						1,
						"5",
						new FixtureSub[] { new FixtureSub(
								"가압류",
								"1998년4월30일\n제22186호",
								"1998년4월28일\n서울지방법원남부지원의\n가압류\n결정(98카단17462)",
								new String[] { "청구금액 금15,000,000원정\n채권자 경서농업협동조합 114936-0000290\n  서울 양천구 신정동 977-20\n  (고척지소)" }) }),

				new Fixture(
						1,
						"6",
						new FixtureSub[] { new FixtureSub(
								"가압류",
								"1998년5월26일\n제26751호",
								"1998년5월23일\n인천지방법원의 가압류\n결정(98카단28207)",
								new String[] { "청구금액 금5,138,683원정\n채권자 대한보증보험주식회사 110111-0099774\n  서울 종로구 연지동 136-74\n  (인천지점)", }) }),
				new Fixture(1, "7", new FixtureSub[] { new FixtureSub("강제경매신청", "1998년6월5일\n제28589호",
						"1998년6월3일\n서울지방법원남부지원의\n강제경매개시결정 (98타\n경26344)",
						new String[] { "채권자 정병세 561201-1******\n  서울 구로구 고척동 226. 한양종합건재상사" }) }),

				new Fixture(1, "8", new FixtureSub[] { new FixtureSub("가압류", "1998년7월13일\n제38541호",
						"1998년7월10일\n서울지방법원의 가압류\n결정(98카단150816)",
						new String[] { "청구금액 금5,600,790원\n채권자 주식회사국민은행\n  서울 중구 남대문로2가 9-1\n  (오류동지점)", }) }),

				new Fixture(1, "9", new FixtureSub[] { new FixtureSub("가압류", "1998년8월24일\n제47158호",
						"1998년8월21일\n서울지방법원남부지원의\n가압류\n결정(98카단35577)",
						new String[] { "청구금액 금5,386,500원\n채권자 엘지산전주식회사\n  서울 영등포구 여의도동 20", }) }),

				new Fixture(1, "10", new FixtureSub[] { new FixtureSub("압류", "1998년11월26일\n제70197호",
						"1998년11월26일\n압류(청소과67500-2930)", new String[] { "권리자 구로구", }) }),
				new Fixture(1, "11", new FixtureSub[] { new FixtureSub("가압류", "1998년12월5일\n제72580호",
						"1998년12월3일\n서울지방법원남부지원의\n가압류\n결정(98카단48865)",
						new String[] { "청구금액 금11,057,000원\n채권자 중소기업은행\n  서울 중구 을지로2가 50\n  (오류동지점)", }) }),
				new Fixture(1, "12", new FixtureSub[] { new FixtureSub("7번강제경매신청등기말소", "1999년6월3일\n제39827호",
						"1999년5월29일\n취하", new String[] {}) }),
				new Fixture(1, "13", new FixtureSub[] { new FixtureSub("압류", "2001년8월10일\n제64515호",
						"2001년8월9일\n압류(세관13410)", new String[] { "권리자 구로구(세무관리과)" }) }),
				new Fixture(1, "14", new FixtureSub[] { new FixtureSub("압류", "2003년1월29일\n제7137호",
						"2003년1월23일\n압류(징세46120-393)", new String[] { "권리자 구로세무서" }) }),
				new Fixture(1, "15", new FixtureSub[] { new FixtureSub("강제경매개시결정", "2006년1월18일\n제3792호",
						"2006년1월13일\n서울남부지방법원의\n강제경매개시결정(2006\n타경1586)",
						new String[] { "채권자 김원일 600405-1******\n  서울 구로구 고척동 98-43" }) }),
				new Fixture(
						2,
						"1",
						new FixtureSub[] { new FixtureSub(
								"주택임차권",
								"2002년11월28일\n제125933호",
								"2002년11월4일\n서울지방법원남부지원의\n임차권등기명령(2002카\n기2540)",
								new String[] { "임차보증금 금50,000,000원\n범 위 2층 전부\n임대차계약일자 1996년 10월 17일\n주민등록일자 1996년 11월 22일\n점유개시일자 1996년 11월 17일\n확정일자 1996년 10월 18일\n임차권자 김원일 600405-1******\n  서울특별시 구로구 고척동 98-43" }) }), };

	}
/*
	public void test1() throws Exception {
		PdfDecoder.useTextExtraction();
		PdfDecoder decodePdf = new PdfDecoder(false);
		decodePdf.setExtractionMode(PdfDecoder.TEXT); // extract just
		// text
		decodePdf.init(true);
		PdfGroupingAlgorithms.useUnrotatedCoords = false;
		decodePdf.openPdfFile("fixtures/건물등기부등본6364.pdf");
		int start = 1, end = decodePdf.getPageCount();

		// System.out.println(start + ":" + end);
		TextPosition first = null;
		List<TextPosition> list = new LinkedList<TextPosition>();
		int stage = 0;
		for (int page = start; page < end + 1; page++) {
			decodePdf.decodePage(page);
			PdfGroupingAlgorithms currentGrouping = decodePdf.getGroupingObject();
			PdfPageData currentPageData = decodePdf.getPdfPageData();

			int x1 = currentPageData.getMediaBoxX(page);
			int x2 = currentPageData.getMediaBoxWidth(page) + x1;

			int y2 = currentPageData.getMediaBoxX(page);
			int y1 = currentPageData.getMediaBoxHeight(page) - y2;

			// System.out.println("Page " + page
			// + " Extracting text from rectangle (" + x1 + "," + y1 + " "
			// + x2 + "," + y2 + ")");
			Vector<String> words = currentGrouping.extractTextAsWordlist(x1, y1, x2, y2, page, true, true, "");

			for (int i = 0; i < words.size(); i += 5) {
				String text = words.get(i);
				if (stage == 0 && page == 1) {
					TextPosition t = new TextPosition(words.get(i), floatValue(words.get(i + 1)), y1
							- floatValue(words.get(i + 2)), floatValue(words.get(i + 3)), y1
							- floatValue(words.get(i + 4)), page, stage);
					if (first == null) {
						first = t;
						continue;
					}

					if (t.getY() == first.getY()) {
						first = first.add(t);
					}
				}

				if ("【".equals(text) && "갑".equals(words.get(i + 5))) {
					stage = 1;
					i = skip(i, words);
				}
				if ("【".equals(text) && "을".equals(words.get(i + 5))) {
					stage = 2;
					i = skip(i, words);
				}
				if ("--".equals(text) && "이".equals(words.get(i + 5)) && "하".equals(words.get(i + 10))
						&& "여".equals(words.get(i + 15)) && "백".equals(words.get(i + 20))) {
					stage = 3;
				}

				if ("열".equals(text) && "281.2105".equals(words.get(i + 1))) {
					// System.out.println("#"+text+" "+words.get(i + 1) +" :
					// "+words.get(i + 2));
					continue;
				}
				//				
				if ("람".equals(text) && "377.21024".equals(words.get(i + 1))) {
					// System.out.println("#"+text+" "+words.get(i + 1) +" :
					// "+words.get(i + 2));
					continue;
				}
				if ("용".equals(text) && "473.20993".equals(words.get(i + 1))) {
					// System.out.println("#"+text+" "+words.get(i + 1) +" :
					// "+words.get(i + 2));
					continue;
				}
				if (stage > 0 && stage <= 2) {
					TextPosition t = new TextPosition(words.get(i), floatValue(words.get(i + 1)), y1
							- floatValue(words.get(i + 2)), floatValue(words.get(i + 3)), y1
							- floatValue(words.get(i + 4)), page, stage);

					if (isInContentsHeight(t)) {
						list.add(t);
					}
				}
			}

			// java.util.Collections.sort(list, new TextPositionComparator());
		}

		List<TextPosition> temp = new LinkedList<TextPosition>(list);
		Collections.sort(temp, new Comparator<TextPosition>() {

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

		List<TextPosition> res1 = new LinkedList<TextPosition>();
		List<TextPosition> res2 = new LinkedList<TextPosition>();
		List<TextPosition> res3 = new LinkedList<TextPosition>();
		List<TextPosition> res4 = new LinkedList<TextPosition>();
		List<TextPosition> res5 = new LinkedList<TextPosition>();
		//
		List<TextPosition> buf1 = new LinkedList<TextPosition>(list);
		//
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
		//
		List<TextPosition> priotyNos = binding(res1);
		List<TextPosition> purpose = binding(res2);
		List<TextPosition> acceptDate = binding(res3);
		List<TextPosition> because = binding(res4);
		List<TextPosition> right = binding(res5);

		assertEquals("등기부 등본 (말소사항 포함) - 건물", first.getText());

		assertTextPositionList(priotyNos, new String[] { "1\n(전 1)", "2\n(전 2)", "3\n(전 3)", "4\n(전 4)", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14", "15", "1" });

		assertTextPositionList(purpose, new String[] { "소유권보존", "가압류", "가압류", "가압류", "가압류", "가압류", "강제경매신청", "가압류",
				"가압류", "압류", "가압류", "7번강제경매신청등기말소", "압류", "압류", "강제경매개시결정", "주택임차권" });

		assertTextPositionList(acceptDate, new String[] { "1995년4월18일\n제18980호", "1997년12월9일\n제80037호",
				"1997년12월11일\n제80870호", "1998년4월7일\n제17577호", "1998년4월30일\n제22186호", "1998년5월26일\n제26751호",
				"1998년6월5일\n제28589호", "1998년7월13일\n제38541호", "1998년8월24일\n제47158호", "1998년11월26일\n제70197호",
				"1998년12월5일\n제72580호", "1999년6월3일\n제39827호", "2001년8월10일\n제64515호", "2003년1월29일\n제7137호",
				"2006년1월18일\n제3792호", "2002년11월28일\n제125933호" });
		assertTextPositionList(because, new String[] { "1997년12월5일\n서울지방\n법원남부지원의 가압류\n결정(97카단6564호)",
				"1997년12월9일\n서울지방", "법원 남부지원의 가압류\n결정(97카합6693호)", "1998년4월4일\n서울지방\n법원 가압류\n결정(98카단78975)",
				"1998년4월28일\n서울지방법원남부지원의\n가압류\n결정(98카단17462)", "1998년5월23일\n인천지방법원의 가압류\n결정(98카단28207)",
				"1998년6월3일\n서울지방법원남부지원의\n강제경매개시결정 (98타\n경26344)", "1998년7월10일\n서울지방법원의 가압류\n결정(98카단150816)",
				"1998년8월21일\n서울지방법원남부지원의\n가압류\n결정(98카단35577)", "1998년11월26일\n압류(청소과67500-2930)",
				"1998년12월3일\n서울지방법원남부지원의\n가압류\n결정(98카단48865)", "1999년5월29일\n취하", "2001년8월9일\n압류(세관13410)",
				"2003년1월23일\n압류(징세46120-393)", "2006년1월13일\n서울남부지방법원의\n강제경매개시결정(2006\n타경1586)",
				"2002년11월4일\n서울지방법원남부지원의\n임차권등기명령(2002카\n기2540)" });

		assertTextPositionList(
				right,
				new String[] {
						"소유자 황을년 491225-2******\n서울 구로구 고척동 98-42",
						"청구금액 금40,000,000원\n권리자 고영식\n서울 구로구 고척1동 98-43",
						"청구금액 금33,759,103원\n권리자 정병세",
						"서울 구로구 고척동 226",
						"청구금액 금5,485,577원\n권리자 대한보증보험(주)\n서울 종로구 연지동 136-74\n(영등포지점)",
						"부동산등기법시행규칙부칙 제3조 제1항의 규정에 의하여\n1번 내지 4번 등기를 1998년 04월 10일 전산이기",
						"청구금액 금15,000,000원정\n채권자 경서농업협동조합 114936-0000290\n서울 양천구 신정동 977-20\n(고척지소)",
						"청구금액 금5,138,683원정\n채권자 대한보증보험주식회사 110111-0099774\n서울 종로구 연지동 136-74\n(인천지점)",
						"채권자 정병세 561201-1******\n서울 구로구 고척동 226. 한양종합건재상사",
						"청구금액 금5,600,790원\n채권자 주식회사국민은행\n서울 중구 남대문로2가 9-1",
						"(오류동지점)",
						"청구금액 금5,386,500원\n채권자 엘지산전주식회사\n서울 영등포구 여의도동 20",
						"권리자 구로구",
						"청구금액 금11,057,000원\n채권자 중소기업은행\n서울 중구 을지로2가 50\n(오류동지점)",
						"권리자 구로구(세무관리과)",
						"권리자 구로세무서",
						"채권자 김원일 600405-1******\n서울 구로구 고척동 98-43",
						"임차보증금 금50,000,000원\n범 위 2층 전부\n임대차계약일자 1996년 10월 17일\n주민등록일자 1996년 11월 22일\n점유개시일자 1996년 11월 17일\n확정일자 1996년 10월 18일\n임차권자 김원일 600405-1******\n서울특별시 구로구 고척동 98-43"

				});
	}
*/
	private void assertItems(List<등기부등본_사항> it) {
		for (int i = 0; i < fixtures.length; i++) {
			Fixture fixture = fixtures[i];
			등기부등본_사항 item = it.get(i);
			assertEquals(fixture.stage, item.getStage());
			assertEquals(fixture.no, item.getText());
			assertEquals(fixture.subs.length, item.purposes.size());
			for (int k = 0; k < fixture.subs.length; k++) {
				assertEquals("Purpose " + i + ":" + k, fixture.subs[k].purpose, item.purposes.get(k).getPurpose());
				assertEquals("Accept " + i + ":" + k, fixture.subs[k].acceptDate, item.purposes.get(k).getAcceptDate());
				assertEquals("Because " + i + ":" + k, fixture.subs[k].because, item.purposes.get(k).getBecause());

				assertEquals("Right length " + i + ":" + k, fixture.subs[k].right.length, item.purposes.get(k)
						.getRight().size());
				for (int n = 0; n < fixture.subs[k].right.length; n++) {
					assertEquals("Right " + i + ":" + k + ":" + n, fixture.subs[k].right[n], item.purposes.get(k)
							.getRight().get(n).getText());
				}
			}
		}
	}

	class Fixture {
		String no;

		FixtureSub[] subs;

		int stage;

		public Fixture(int stage, String no, FixtureSub[] subs) {
			this.stage = stage;
			this.no = no;
			this.subs = subs;
		}

	}

	class FixtureSub {
		String purpose;

		String acceptDate;

		String because;

		String[] right;

		public FixtureSub(String purpose, String acceptDate, String because, String[] right) {
			this.purpose = purpose;
			this.acceptDate = acceptDate;
			this.because = because;
			this.right = right;
		}
	}

	private void assertTextPositionList(List<TextPosition> values, String[] fixtures) {
		assertEquals(fixtures.length, values.size());
		for (int i = 0; i < fixtures.length; i++) {
			assertEquals("" + i, fixtures[i], values.get(i).getText());
		}
	}

	/**
	 * @param src
	 * @return
	 */
	private List<TextPosition> binding(List<TextPosition> src) {
		List<TextPosition> temp = new LinkedList<TextPosition>();

		while (src.size() != 0) {
			TextPosition tp1 = src.remove(0);
			for (int i = 0; i < src.size(); i++) {
				TextPosition tp2 = src.get(i);
				if (tp1.isBinding수평(tp2,21f)) {
					tp1 = tp1.add(tp2);
					src.remove(i);
					i--;
				}
			}
			temp.add(tp1);
		}

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

	private int skip(int i, Vector<String> words) {
		while (!words.get(i).equals("항")) {
			i++;
		}
		return i + 5;
	}

	private float floatValue(Object object) {
		return Float.parseFloat((String) object);
	}

	boolean isInContentsHeight(TextPosition position) {
		return position.getY() > 357 && position.getY() < 750;
	}

	public void testTextPosition() {
		TextPosition tp1 = new TextPosition("등기부", 256.0105f, 548.5f, 310.0105f, 530.5f, 0, 1);
		assertEquals(256.0105f, tp1.x);
		assertEquals(548.5f, tp1.y2);
		assertEquals(310.0105f, tp1.x2);
		assertEquals(530.5f, tp1.y);
		assertEquals(310.0105f - 256.0105f, tp1.getWidth(), 0.1f);

		TextPosition tp2 = new TextPosition("등본", 319.0105f, 548.5f, 355.0105f, 530.5f, 0, 1);
		TextPosition tp3 = new TextPosition("서울특별시", 115.8398f, 512.4198f, 165.74878f, 502.41998f, 0, 1);
		assertTrue(tp1.isBinding수평(tp2, 21f));
		assertFalse(tp1.isBinding수평(tp3, 21f));

		TextPosition tp = tp1.add(tp2);
		assertEquals("등기부 등본", tp.getText());
		assertEquals(256.0105f, tp.x);
		assertEquals(530.5f, tp.y);
		assertEquals(355.0105f, tp.x2);
		assertEquals(548.5f, tp.y2);
	}

	public void test등기부등본Parser() throws Exception {
		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> it = parser.parse("fixtures/건물등기부등본6364.pdf");
		assertItems(it);
		등기부등본_사항 s4 = it.get(3);
		assertEquals("청구금액 금5,485,577원\n권리자 대한보증보험(주)\n  서울 종로구 연지동 136-74\n  (영등포지점)", s4.getRight());
	}

	public void test등기부등본Parser2() throws Exception {
		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> it = parser.parse("fixtures/건물등기부등본001.pdf");
		등기부등본_사항 s13 = it.get(12);
		assert사항(s13, "13", "소유권이전", "1995년7월21일", "매매",
				"공유자 지분 1713.24분의 27.867\n 조지성 420327-1******\n  서울 강남구 논현동 188-23", "김자범지분일부이전");

		등기부등본_사항 s14 = it.get(13);
		assert사항(s14, "14", "소유권이전", "1995년7월21일", "매매",
				"공유자 지분 1713.24분의 27.867\n 윤순자 591218-2******\n  서울 강남구 논현동 164-12", "김자범지분일부이전");

		등기부등본_사항 s30 = it.get(30);
		assert사항(s30, "30", "소유권이전", "2001년4월23일", "매매",
				"공유자 지분 1713.24분의 15.9 25\n 강지호 501227-1******\n  서울 강남구 논현동 104-5 동현빌라 102호",
				"21번김승남지분1713.24분의161.134 중일부(1713.24분의15.925)");

		등기부등본_사항 s33_1_1 = it.get(36);
		assert사항(s33_1_1, "33-1-1", "33-1번가압류직권말소통지", "", "", "직권말소통지\n2001년12월26일", "33-1번가압류직권말소통지");

	}

	void assert사항(등기부등본_사항 s, String exNo, String exP, String exA, String exB, String exR, String exBigo) {
		assertEquals(exNo, s.getText());
		assertEquals(exP, s.getPurpose());
		assertEquals(exA, s.getAcceptDate());
		assertEquals(exB, s.getBecause());
		assertEquals(exR, s.getRight());
		assertEquals(exBigo, s.getBigo());
	}
}
