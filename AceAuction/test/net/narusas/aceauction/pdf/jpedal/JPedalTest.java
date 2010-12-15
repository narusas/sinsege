package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

public class JPedalTest extends TestCase {
	List<���ε_����> items = new LinkedList<���ε_����>();

	private Fixture[] fixtures;

	@Override
	protected void setUp() throws Exception {
		items = new LinkedList<���ε_����>();
		fixtures = new Fixture[] {
				new Fixture(1, "1", new FixtureSub[] { new FixtureSub("�����Ǻ���", "1995��4��18��\n��18980ȣ", "",
						new String[] { "������ Ȳ���� 491225-2******\n  ���� ���α� ��ô�� 98-42" }) }),
				new Fixture(1, "2", new FixtureSub[] { new FixtureSub("���з�", "1997��12��9��\n��80037ȣ",
						"1997��12��5��\n��������\n�������������� ���з�\n����(97ī��6564ȣ)",
						new String[] { "û���ݾ� ��40,000,000��\n�Ǹ��� ����\n  ���� ���α� ��ô1�� 98-43" }) }),
				new Fixture(1, "3", new FixtureSub[] { new FixtureSub("���з�", "1997��12��11��\n��80870ȣ",
						"1997��12��9��\n��������\n���� ���������� ���з�\n����(97ī��6693ȣ)",
						new String[] { "û���ݾ� ��33,759,103��\n�Ǹ��� ������\n  ���� ���α� ��ô�� 226" }) }),

				new Fixture(1, "4", new FixtureSub[] { new FixtureSub("���з�", "1998��4��7��\n��17577ȣ",
						"1998��4��4��\n��������\n���� ���з�\n����(98ī��78975)", new String[] {
								"û���ݾ� ��5,485,577��\n�Ǹ��� ���Ѻ�������(��)\n  ���� ���α� ������ 136-74\n  (����������)",
								"�ε�����������Ģ��Ģ ��3�� ��1���� ������ ���Ͽ�\n1�� ���� 4�� ��⸦ 1998�� 04�� 10�� �����̱�" }) }),

				new Fixture(
						1,
						"5",
						new FixtureSub[] { new FixtureSub(
								"���з�",
								"1998��4��30��\n��22186ȣ",
								"1998��4��28��\n���������������������\n���з�\n����(98ī��17462)",
								new String[] { "û���ݾ� ��15,000,000����\nä���� �漭����������� 114936-0000290\n  ���� ��õ�� ������ 977-20\n  (��ô����)" }) }),

				new Fixture(
						1,
						"6",
						new FixtureSub[] { new FixtureSub(
								"���з�",
								"1998��5��26��\n��26751ȣ",
								"1998��5��23��\n��õ��������� ���з�\n����(98ī��28207)",
								new String[] { "û���ݾ� ��5,138,683����\nä���� ���Ѻ��������ֽ�ȸ�� 110111-0099774\n  ���� ���α� ������ 136-74\n  (��õ����)", }) }),
				new Fixture(1, "7", new FixtureSub[] { new FixtureSub("������Ž�û", "1998��6��5��\n��28589ȣ",
						"1998��6��3��\n���������������������\n������Ű��ð��� (98Ÿ\n��26344)",
						new String[] { "ä���� ������ 561201-1******\n  ���� ���α� ��ô�� 226. �Ѿ����հ�����" }) }),

				new Fixture(1, "8", new FixtureSub[] { new FixtureSub("���з�", "1998��7��13��\n��38541ȣ",
						"1998��7��10��\n������������� ���з�\n����(98ī��150816)",
						new String[] { "û���ݾ� ��5,600,790��\nä���� �ֽ�ȸ�籹������\n  ���� �߱� ���빮��2�� 9-1\n  (����������)", }) }),

				new Fixture(1, "9", new FixtureSub[] { new FixtureSub("���з�", "1998��8��24��\n��47158ȣ",
						"1998��8��21��\n���������������������\n���з�\n����(98ī��35577)",
						new String[] { "û���ݾ� ��5,386,500��\nä���� ���������ֽ�ȸ��\n  ���� �������� ���ǵ��� 20", }) }),

				new Fixture(1, "10", new FixtureSub[] { new FixtureSub("�з�", "1998��11��26��\n��70197ȣ",
						"1998��11��26��\n�з�(û�Ұ�67500-2930)", new String[] { "�Ǹ��� ���α�", }) }),
				new Fixture(1, "11", new FixtureSub[] { new FixtureSub("���з�", "1998��12��5��\n��72580ȣ",
						"1998��12��3��\n���������������������\n���з�\n����(98ī��48865)",
						new String[] { "û���ݾ� ��11,057,000��\nä���� �߼ұ������\n  ���� �߱� ������2�� 50\n  (����������)", }) }),
				new Fixture(1, "12", new FixtureSub[] { new FixtureSub("7��������Ž�û��⸻��", "1999��6��3��\n��39827ȣ",
						"1999��5��29��\n����", new String[] {}) }),
				new Fixture(1, "13", new FixtureSub[] { new FixtureSub("�з�", "2001��8��10��\n��64515ȣ",
						"2001��8��9��\n�з�(����13410)", new String[] { "�Ǹ��� ���α�(����������)" }) }),
				new Fixture(1, "14", new FixtureSub[] { new FixtureSub("�з�", "2003��1��29��\n��7137ȣ",
						"2003��1��23��\n�з�(¡��46120-393)", new String[] { "�Ǹ��� ���μ�����" }) }),
				new Fixture(1, "15", new FixtureSub[] { new FixtureSub("������Ű��ð���", "2006��1��18��\n��3792ȣ",
						"2006��1��13��\n���ﳲ�����������\n������Ű��ð���(2006\nŸ��1586)",
						new String[] { "ä���� ����� 600405-1******\n  ���� ���α� ��ô�� 98-43" }) }),
				new Fixture(
						2,
						"1",
						new FixtureSub[] { new FixtureSub(
								"����������",
								"2002��11��28��\n��125933ȣ",
								"2002��11��4��\n���������������������\n�����ǵ����(2002ī\n��2540)",
								new String[] { "���������� ��50,000,000��\n�� �� 2�� ����\n�Ӵ���������� 1996�� 10�� 17��\n�ֹε������ 1996�� 11�� 22��\n������������ 1996�� 11�� 17��\nȮ������ 1996�� 10�� 18��\n�������� ����� 600405-1******\n  ����Ư���� ���α� ��ô�� 98-43" }) }), };

	}
/*
	public void test1() throws Exception {
		PdfDecoder.useTextExtraction();
		PdfDecoder decodePdf = new PdfDecoder(false);
		decodePdf.setExtractionMode(PdfDecoder.TEXT); // extract just
		// text
		decodePdf.init(true);
		PdfGroupingAlgorithms.useUnrotatedCoords = false;
		decodePdf.openPdfFile("fixtures/�ǹ����ε6364.pdf");
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

				if ("��".equals(text) && "��".equals(words.get(i + 5))) {
					stage = 1;
					i = skip(i, words);
				}
				if ("��".equals(text) && "��".equals(words.get(i + 5))) {
					stage = 2;
					i = skip(i, words);
				}
				if ("--".equals(text) && "��".equals(words.get(i + 5)) && "��".equals(words.get(i + 10))
						&& "��".equals(words.get(i + 15)) && "��".equals(words.get(i + 20))) {
					stage = 3;
				}

				if ("��".equals(text) && "281.2105".equals(words.get(i + 1))) {
					// System.out.println("#"+text+" "+words.get(i + 1) +" :
					// "+words.get(i + 2));
					continue;
				}
				//				
				if ("��".equals(text) && "377.21024".equals(words.get(i + 1))) {
					// System.out.println("#"+text+" "+words.get(i + 1) +" :
					// "+words.get(i + 2));
					continue;
				}
				if ("��".equals(text) && "473.20993".equals(words.get(i + 1))) {
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

		assertEquals("���� � (���һ��� ����) - �ǹ�", first.getText());

		assertTextPositionList(priotyNos, new String[] { "1\n(�� 1)", "2\n(�� 2)", "3\n(�� 3)", "4\n(�� 4)", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14", "15", "1" });

		assertTextPositionList(purpose, new String[] { "�����Ǻ���", "���з�", "���з�", "���з�", "���з�", "���з�", "������Ž�û", "���з�",
				"���з�", "�з�", "���з�", "7��������Ž�û��⸻��", "�з�", "�з�", "������Ű��ð���", "����������" });

		assertTextPositionList(acceptDate, new String[] { "1995��4��18��\n��18980ȣ", "1997��12��9��\n��80037ȣ",
				"1997��12��11��\n��80870ȣ", "1998��4��7��\n��17577ȣ", "1998��4��30��\n��22186ȣ", "1998��5��26��\n��26751ȣ",
				"1998��6��5��\n��28589ȣ", "1998��7��13��\n��38541ȣ", "1998��8��24��\n��47158ȣ", "1998��11��26��\n��70197ȣ",
				"1998��12��5��\n��72580ȣ", "1999��6��3��\n��39827ȣ", "2001��8��10��\n��64515ȣ", "2003��1��29��\n��7137ȣ",
				"2006��1��18��\n��3792ȣ", "2002��11��28��\n��125933ȣ" });
		assertTextPositionList(because, new String[] { "1997��12��5��\n��������\n�������������� ���з�\n����(97ī��6564ȣ)",
				"1997��12��9��\n��������", "���� ���������� ���з�\n����(97ī��6693ȣ)", "1998��4��4��\n��������\n���� ���з�\n����(98ī��78975)",
				"1998��4��28��\n���������������������\n���з�\n����(98ī��17462)", "1998��5��23��\n��õ��������� ���з�\n����(98ī��28207)",
				"1998��6��3��\n���������������������\n������Ű��ð��� (98Ÿ\n��26344)", "1998��7��10��\n������������� ���з�\n����(98ī��150816)",
				"1998��8��21��\n���������������������\n���з�\n����(98ī��35577)", "1998��11��26��\n�з�(û�Ұ�67500-2930)",
				"1998��12��3��\n���������������������\n���з�\n����(98ī��48865)", "1999��5��29��\n����", "2001��8��9��\n�з�(����13410)",
				"2003��1��23��\n�з�(¡��46120-393)", "2006��1��13��\n���ﳲ�����������\n������Ű��ð���(2006\nŸ��1586)",
				"2002��11��4��\n���������������������\n�����ǵ����(2002ī\n��2540)" });

		assertTextPositionList(
				right,
				new String[] {
						"������ Ȳ���� 491225-2******\n���� ���α� ��ô�� 98-42",
						"û���ݾ� ��40,000,000��\n�Ǹ��� ����\n���� ���α� ��ô1�� 98-43",
						"û���ݾ� ��33,759,103��\n�Ǹ��� ������",
						"���� ���α� ��ô�� 226",
						"û���ݾ� ��5,485,577��\n�Ǹ��� ���Ѻ�������(��)\n���� ���α� ������ 136-74\n(����������)",
						"�ε�����������Ģ��Ģ ��3�� ��1���� ������ ���Ͽ�\n1�� ���� 4�� ��⸦ 1998�� 04�� 10�� �����̱�",
						"û���ݾ� ��15,000,000����\nä���� �漭����������� 114936-0000290\n���� ��õ�� ������ 977-20\n(��ô����)",
						"û���ݾ� ��5,138,683����\nä���� ���Ѻ��������ֽ�ȸ�� 110111-0099774\n���� ���α� ������ 136-74\n(��õ����)",
						"ä���� ������ 561201-1******\n���� ���α� ��ô�� 226. �Ѿ����հ�����",
						"û���ݾ� ��5,600,790��\nä���� �ֽ�ȸ�籹������\n���� �߱� ���빮��2�� 9-1",
						"(����������)",
						"û���ݾ� ��5,386,500��\nä���� ���������ֽ�ȸ��\n���� �������� ���ǵ��� 20",
						"�Ǹ��� ���α�",
						"û���ݾ� ��11,057,000��\nä���� �߼ұ������\n���� �߱� ������2�� 50\n(����������)",
						"�Ǹ��� ���α�(����������)",
						"�Ǹ��� ���μ�����",
						"ä���� ����� 600405-1******\n���� ���α� ��ô�� 98-43",
						"���������� ��50,000,000��\n�� �� 2�� ����\n�Ӵ���������� 1996�� 10�� 17��\n�ֹε������ 1996�� 11�� 22��\n������������ 1996�� 11�� 17��\nȮ������ 1996�� 10�� 18��\n�������� ����� 600405-1******\n����Ư���� ���α� ��ô�� 98-43"

				});
	}
*/
	private void assertItems(List<���ε_����> it) {
		for (int i = 0; i < fixtures.length; i++) {
			Fixture fixture = fixtures[i];
			���ε_���� item = it.get(i);
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
				if (tp1.isBinding����(tp2,21f)) {
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
				if (tp1.isBinding����(tp2)) {
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
		���ε_���� item = new ���ε_����(startEntity);
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
		while (!words.get(i).equals("��")) {
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
		TextPosition tp1 = new TextPosition("����", 256.0105f, 548.5f, 310.0105f, 530.5f, 0, 1);
		assertEquals(256.0105f, tp1.x);
		assertEquals(548.5f, tp1.y2);
		assertEquals(310.0105f, tp1.x2);
		assertEquals(530.5f, tp1.y);
		assertEquals(310.0105f - 256.0105f, tp1.getWidth(), 0.1f);

		TextPosition tp2 = new TextPosition("�", 319.0105f, 548.5f, 355.0105f, 530.5f, 0, 1);
		TextPosition tp3 = new TextPosition("����Ư����", 115.8398f, 512.4198f, 165.74878f, 502.41998f, 0, 1);
		assertTrue(tp1.isBinding����(tp2, 21f));
		assertFalse(tp1.isBinding����(tp3, 21f));

		TextPosition tp = tp1.add(tp2);
		assertEquals("���� �", tp.getText());
		assertEquals(256.0105f, tp.x);
		assertEquals(530.5f, tp.y);
		assertEquals(355.0105f, tp.x2);
		assertEquals(548.5f, tp.y2);
	}

	public void test���εParser() throws Exception {
		���εParser parser = new ���εParser();
		List<���ε_����> it = parser.parse("fixtures/�ǹ����ε6364.pdf");
		assertItems(it);
		���ε_���� s4 = it.get(3);
		assertEquals("û���ݾ� ��5,485,577��\n�Ǹ��� ���Ѻ�������(��)\n  ���� ���α� ������ 136-74\n  (����������)", s4.getRight());
	}

	public void test���εParser2() throws Exception {
		���εParser parser = new ���εParser();
		List<���ε_����> it = parser.parse("fixtures/�ǹ����ε001.pdf");
		���ε_���� s13 = it.get(12);
		assert����(s13, "13", "����������", "1995��7��21��", "�Ÿ�",
				"������ ���� 1713.24���� 27.867\n ������ 420327-1******\n  ���� ������ ������ 188-23", "���ڹ������Ϻ�����");

		���ε_���� s14 = it.get(13);
		assert����(s14, "14", "����������", "1995��7��21��", "�Ÿ�",
				"������ ���� 1713.24���� 27.867\n ������ 591218-2******\n  ���� ������ ������ 164-12", "���ڹ������Ϻ�����");

		���ε_���� s30 = it.get(30);
		assert����(s30, "30", "����������", "2001��4��23��", "�Ÿ�",
				"������ ���� 1713.24���� 15.9 25\n ����ȣ 501227-1******\n  ���� ������ ������ 104-5 �������� 102ȣ",
				"21����³�����1713.24����161.134 ���Ϻ�(1713.24����15.925)");

		���ε_���� s33_1_1 = it.get(36);
		assert����(s33_1_1, "33-1-1", "33-1�����з����Ǹ�������", "", "", "���Ǹ�������\n2001��12��26��", "33-1�����з����Ǹ�������");

	}

	void assert����(���ε_���� s, String exNo, String exP, String exA, String exB, String exR, String exBigo) {
		assertEquals(exNo, s.getText());
		assertEquals(exP, s.getPurpose());
		assertEquals(exA, s.getAcceptDate());
		assertEquals(exB, s.getBecause());
		assertEquals(exR, s.getRight());
		assertEquals(exBigo, s.getBigo());
	}
}
