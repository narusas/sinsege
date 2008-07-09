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
		stripper.getText(PDDocument.load(new File("fixtures/건물등기부등본6364.pdf")));
		// assertEquals(3, stripper.sections.size());

		// for (PDFPage page : stripper.sections.get(0).pages) {
		// System.out.println("################ Page ###################");
		// print("순위번호", page.priotyNo);
		// print("등기목적", page.purpose);
		// print("접수", page.acceptDate);
		// print("등기원인", page.because);
		// print("권리자", page.rightAndEtc);
		//		
		// }
		// SectionPart part = new SectionPart();
		//
		SectionPartParser parser = new SectionPartParser(stripper.sections.get(0));
		// parser.collectAllPages(part);
		SectionPart d = parser.parse();

//		System.out.println("########################");

//		Filter.checkDeleteItem(parser.items);
		// for (등기부등본_사항 s: parser.items) {
		// System.out.println(s);
		// }
		LinkedList<등기부등본_사항> src = parser.items;

		String[][] fixture = {
				{ "1", "소유권보존", "1995년4월18일", "", "소유자  황을년  491225-2******\n서울 구로구 고척동 98-42" },
				{ "2", "가압류", "1997년12월9일", "1997년12월5일\n서울지방\n법원남부지원의 가압류\n결정(97카단6564호)",
						"청구금액  금40,000,000원\n권리자  고영식\n서울 구로구 고척1동 98-43" },
				{ "3", "가압류", "1997년12월11일", "1997년12월9일\n서울지방법원 남부지원의 가압류\n결정(97카합6693호)",
						"청구금액  금33,759,103원\n권리자  정병세\n서울 구로구 고척동 226" } };

		for (int i = 0; i < fixture.length; i++) {
			assertEquals(fixture[i][0], src.get(i).getText());
			assertEquals(fixture[i][1], src.get(i).purposes.get(0).getPurpose());
			assertEquals(fixture[i][2], src.get(i).purposes.get(0).getAcceptDate());
			assertEquals(fixture[i][3], src.get(i).purposes.get(0).getBecause());
			assertEquals(fixture[i][4], src.get(i).purposes.get(0).getRight());
		}

		assertEquals("4", src.get(3).getText());
		assertEquals("가압류", src.get(3).purposes.get(0).getPurpose());
		assertEquals("1998년4월7일", src.get(3).purposes.get(0).getAcceptDate());
		assertEquals("1998년4월4일\n서울지방\n법원 가압류\n결정(98카단78975)", src.get(3).purposes.get(0).getBecause());
		assertEquals("청구금액  금5,485,577원\n권리자  대한보증보험(주)\n서울 종로구 연지동 136-74\n(영등포지점)", src.get(3).purposes.get(0)
				.getRights().get(0));
		assertEquals("부동산등기법시행규칙부칙 제3조 제1항의 규정에 의하여\n1번 내지 4번 등기를 1998년 04월 10일 전산이기", src.get(3).purposes.get(0)
				.getRights().get(1));

		fixture = new String[][] {
				{ "5", "가압류", "1998년4월30일", "1998년4월28일\n서울지방법원남부지원의\n가압류\n결정(98카단17462)",
						"청구금액  금15,000,000원정\n채권자  경서농업협동조합  114936-0000290\n서울 양천구 신정동 977-20\n(고척지소)" },
				{ "6", "가압류", "1998년5월26일", "1998년5월23일\n인천지방법원의 가압류\n결정(98카단28207)",
						"청구금액  금5,138,683원정\n채권자  대한보증보험주식회사  110111-0099774\n서울 종로구 연지동 136-74\n(인천지점)" },
				{ "7", "강제경매신청", "1998년6월5일", "1998년6월3일\n서울지방법원남부지원의\n강제경매개시결정\n(98타 경26344)",
						"채권자\n정병세 561201-1****** 서울 구로구 고척동 226. 한양종합건재상사" },
				{ "8", "가압류", "1998년7월13일", "1998년7월10일\n서울지방법원의 가압류\n결정(98카단150816)",
						"청구금액  금5,600,790원\n채권자  주식회사국민은행\n서울 중구 남대문로2가 9-1\n(오류동지점)" }, 
						{"9", "가압류","1998년8월24일","1998년8월21일\n서울지방법원남부지원의\n가압류\n결정(98카단35577)","청구금액 금5,386,500원\n채권자 엘지산전주식회사\n가압류 서울 영등포구 여의도동 20"}

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
