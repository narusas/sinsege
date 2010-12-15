package net.narusas.aceauction.pdf;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.TextPosition;

public class PDFTest extends TestCase {

	public void test2() throws IOException {
		PDFStripper stripper = new PDFStripper();
		stripper.getText(PDDocument.load(new File("fixtures/�ǹ����ε6364.pdf")));
		// assertEquals(3, stripper.sections.size());

		// for (PDFPage page : stripper.sections.get(0).pages) {
		// System.out.println("################ Page ###################");
		// print("������ȣ", page.priotyNo);
		// print("������", page.purpose);
		// print("����", page.acceptDate);
		// print("������", page.because);
		// print("�Ǹ���", page.rightAndEtc);
		//		
		// }
		// SectionPart part = new SectionPart();
		//
		SectionPartParser parser = new SectionPartParser(stripper.sections.get(0));
		// parser.collectAllPages(part);
		SectionPart d = parser.parse();

//		System.out.println("########################");

//		Filter.checkDeleteItem(parser.items);
		// for (���ε_���� s: parser.items) {
		// System.out.println(s);
		// }
		LinkedList<���ε_����> src = parser.items;

		String[][] fixture = {
				{ "1", "�����Ǻ���", "1995��4��18��", "", "������  Ȳ����  491225-2******\n���� ���α� ��ô�� 98-42" },
				{ "2", "���з�", "1997��12��9��", "1997��12��5��\n��������\n�������������� ���з�\n����(97ī��6564ȣ)",
						"û���ݾ�  ��40,000,000��\n�Ǹ���  ����\n���� ���α� ��ô1�� 98-43" },
				{ "3", "���з�", "1997��12��11��", "1997��12��9��\n����������� ���������� ���з�\n����(97ī��6693ȣ)",
						"û���ݾ�  ��33,759,103��\n�Ǹ���  ������\n���� ���α� ��ô�� 226" } };

		for (int i = 0; i < fixture.length; i++) {
			assertEquals(fixture[i][0], src.get(i).getText());
			assertEquals(fixture[i][1], src.get(i).purposes.get(0).getPurpose());
			assertEquals(fixture[i][2], src.get(i).purposes.get(0).getAcceptDate());
			assertEquals(fixture[i][3], src.get(i).purposes.get(0).getBecause());
			assertEquals(fixture[i][4], src.get(i).purposes.get(0).getRight());
		}

		assertEquals("4", src.get(3).getText());
		assertEquals("���з�", src.get(3).purposes.get(0).getPurpose());
		assertEquals("1998��4��7��", src.get(3).purposes.get(0).getAcceptDate());
		assertEquals("1998��4��4��\n��������\n���� ���з�\n����(98ī��78975)", src.get(3).purposes.get(0).getBecause());
		assertEquals("û���ݾ�  ��5,485,577��\n�Ǹ���  ���Ѻ�������(��)\n���� ���α� ������ 136-74\n(����������)", src.get(3).purposes.get(0)
				.getRights().get(0));
		assertEquals("�ε�����������Ģ��Ģ ��3�� ��1���� ������ ���Ͽ�\n1�� ���� 4�� ��⸦ 1998�� 04�� 10�� �����̱�", src.get(3).purposes.get(0)
				.getRights().get(1));

		fixture = new String[][] {
				{ "5", "���з�", "1998��4��30��", "1998��4��28��\n���������������������\n���з�\n����(98ī��17462)",
						"û���ݾ�  ��15,000,000����\nä����  �漭�����������  114936-0000290\n���� ��õ�� ������ 977-20\n(��ô����)" },
				{ "6", "���з�", "1998��5��26��", "1998��5��23��\n��õ��������� ���з�\n����(98ī��28207)",
						"û���ݾ�  ��5,138,683����\nä����  ���Ѻ��������ֽ�ȸ��  110111-0099774\n���� ���α� ������ 136-74\n(��õ����)" },
				{ "7", "������Ž�û", "1998��6��5��", "1998��6��3��\n���������������������\n������Ű��ð���\n(98Ÿ ��26344)",
						"ä����\n������ 561201-1****** ���� ���α� ��ô�� 226. �Ѿ����հ�����" },
				{ "8", "���з�", "1998��7��13��", "1998��7��10��\n������������� ���з�\n����(98ī��150816)",
						"û���ݾ�  ��5,600,790��\nä����  �ֽ�ȸ�籹������\n���� �߱� ���빮��2�� 9-1\n(����������)" }, 
						{"9", "���з�","1998��8��24��","1998��8��21��\n���������������������\n���з�\n����(98ī��35577)","û���ݾ� ��5,386,500��\nä���� ���������ֽ�ȸ��\n���з� ���� �������� ���ǵ��� 20"}

		};

		for (int i = 0; i < fixture.length; i++) {
			assertEquals("" + i, fixture[i][0], src.get(i + 4).getText());
			assertEquals("" + i, fixture[i][1], src.get(i + 4).purposes.get(0).getPurpose());
			assertEquals("" + i, fixture[i][2], src.get(i + 4).purposes.get(0).getAcceptDate());
			assertEquals("" + i, fixture[i][3], src.get(i + 4).purposes.get(0).getBecause());
			assertEquals("" + i, fixture[i][4], src.get(i + 4).purposes.get(0).getRight());
		}
	}

	/**
	 * s
	 * 
	 * @param name
	 */
	private void print(String title, List<TextPosition> name) {
		System.out.println("-----------------" + title + "------------------");
		int count = 1;
		for (TextPosition position : name) {
			System.out.println(count + "     " + position.getCharacter() + " Y:" + position.getY());
			count++;
		}
	}
}
