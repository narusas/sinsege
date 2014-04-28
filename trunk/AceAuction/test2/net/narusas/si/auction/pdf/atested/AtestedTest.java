package net.narusas.si.auction.pdf.atested;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.si.auction.app.attested.parser.AtestedPDFParser;
import net.narusas.si.auction.model.등기부등본Item;

import org.jpedal.exception.PdfException;

public class AtestedTest extends TestCase {

	
	
	public void test071() throws Exception {
		
		
		
		String expectedSrc = "[접수일=null, 접수번호=null, 순번=, 권리자=동국석유주식회사, 권리종류=소유권, 금액=NONE, 대상소유자=단독소유, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2007년4월26일, 접수번호=33103, 순번=8, 권리자=주식회사신한은행, 권리종류=근저당권, 금액=5200000000, 대상소유자=동국석유주식회사, 소멸기준=소멸기준, 비고=null]\n"
				+ "[접수일=2008년5월2일, 접수번호=30446, 순번=12, 권리자=주식회사신한은행, 권리종류=지상권, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년6월18일, 접수번호=42545, 순번=13, 권리자=신용보증기금, 권리종류=근저당권, 금액=1500000000, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년6월24일, 접수번호=44342, 순번=14, 권리자=에스케이에너지주식회사, 권리종류=근저당권, 금액=1000000000, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년8월4일, 접수번호=56313, 순번=12, 권리자=김경섭외 2명, 권리종류=가압류, 금액=36492846, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년8월12일, 접수번호=58330, 순번=16, 권리자=에스케이에너지주식회사, 권리종류=근저당권, 금액=2000000000, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년10월9일, 접수번호=71525, 순번=13, 권리자=고성주, 권리종류=가압류, 금액=3262290, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년11월11일, 접수번호=78580, 순번=14, 권리자=중소기업은행, 권리종류=가압류, 금액=442231710, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년12월4일, 접수번호=83235, 순번=15, 권리자=정용모외 11명, 권리종류=가압류, 금액=152376743, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년2월2일, 접수번호=5552, 순번=16, 권리자=근로복지공단춘천지사, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년2월12일, 접수번호=8056, 순번=17, 권리자=파주시, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년2월17일, 접수번호=8921, 순번=18, 권리자=국민연금공단춘천지사, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년2월25일, 접수번호=11173, 순번=19, 권리자=윤일종합건설 주식회사, 권리종류=가압류, 금액=776600000, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년2월27일, 접수번호=11633, 순번=20, 권리자=강원도춘천시, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년4월9일, 접수번호=22024, 순번=22, 권리자=윤일종합건설 주식회사, 권리종류=가압류, 금액=130900000, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년6월5일, 접수번호=35909, 순번=23, 권리자=국, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년7월30일, 접수번호=51009, 순번=25, 권리자=주식회사 신한은행, 권리종류=임의경매, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년11월24일, 접수번호=81262, 순번=26, 권리자=파주시, 권리종류=압류, 금액=NONE, 대상소유자=동국석유주식회사, 소멸기준=소멸, 비고=null]";

		// assertItem(expectedSrc, "fixture2/071_고양 9-23449.pdf");
	}

	private void assertItem(String expectedSrc, String src) throws PdfException, Exception {
		String[] expected = expectedSrc.split("\n");

		List<등기부등본Item> items = parse(src);

		Iterator<등기부등본Item> it = items.iterator();
		for (String expect : expected) {
			assertEquals(expect, it.next().toString());
		}

	}

	private List<등기부등본Item> parse(String src) throws PdfException, Exception {
		List<등기부등본Item> items = new AtestedPDFParser().parse(new File(src));
//		printItems(items);
		return items;
	}

	private void printItems(List<등기부등본Item> items) {
		for (등기부등본Item item : items) {
			System.out.println(item);
		}
	}

	public void test072() throws PdfException, Exception {
		String expect = "[접수일=null, 접수번호=null, 순번=, 권리자=남동협 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의33058, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=null, 접수번호=null, 순번=, 권리자=박은주 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의33058, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=null, 접수번호=null, 순번=, 권리자=박창주 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의49587, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=null, 접수번호=null, 순번=, 권리자=송순덕 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의116484, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=null, 접수번호=null, 순번=, 권리자=윤미향 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의66116, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=null, 접수번호=null, 순번=, 권리자=장윤봉 , 권리종류=소유권, 금액=NONE, 대상소유자=347890분의49587, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2007년9월20일, 접수번호=88898, 순번=8, 권리자=중소기업은행, 권리종류=근저당권, 금액=70000000엔, 대상소유자=박창주, 소멸기준=소멸기준, 비고=null]\n"
				+ "[접수일=2007년9월20일, 접수번호=88903, 순번=9, 권리자=농업협동조합중앙회, 권리종류=근저당권, 금액=557000000, 대상소유자=윤미향, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2007년10월5일, 접수번호=92503, 순번=10, 권리자=농업협동조합중앙회, 권리종류=근저당권, 금액=70000000, 대상소유자=윤미향, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년3월13일, 접수번호=20010, 순번=16, 권리자=한국자산관리공사, 권리종류=근저당권, 금액=1260000000엔, 대상소유자=송순덕, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년3월13일, 접수번호=20013, 순번=17, 권리자=중소기업은행, 권리종류=근저당권, 금액=1140000000엔, 대상소유자=남동협등, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년4월18일, 접수번호=32106, 순번=18, 권리자=농업협동조합중앙회, 권리종류=근저당권, 금액=312000000, 대상소유자=윤미향, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년5월21일, 접수번호=43048, 순번=21, 권리자=중소기업은행, 권리종류=근저당권, 금액=840000000, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년12월10일, 접수번호=104317, 순번=10, 권리자=신왕식, 권리종류=가압류, 금액=4025400, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2008년12월15일, 접수번호=105126, 순번=11, 권리자=주식회사효성종합건설, 권리종류=가압류, 금액=32500000, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년3월27일, 접수번호=19952, 순번=12, 권리자=국, 권리종류=압류, 금액=NONE, 대상소유자=박창주, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년5월14일, 접수번호=32588, 순번=13, 권리자=국민연금공단, 권리종류=압류, 금액=NONE, 대상소유자=박창주, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년5월14일, 접수번호=32595, 순번=14, 권리자=국민연금공단, 권리종류=압류, 금액=NONE, 대상소유자=송순덕, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년9월14일, 접수번호=70720, 순번=15, 권리자=국민연금공단, 권리종류=압류, 금액=NONE, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년12월1일, 접수번호=90593, 순번=16, 권리자=중소기업은행, 권리종류=임의경매, 금액=NONE, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2009년12월31일, 접수번호=98643, 순번=17, 권리자=중소기업은행, 권리종류=임의경매, 금액=NONE, 대상소유자=송순덕, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2010년1월12일, 접수번호=1525, 순번=18, 권리자=시흥시, 권리종류=압류, 금액=NONE, 대상소유자=송순덕, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2010년1월12일, 접수번호=1533, 순번=19, 권리자=시흥시, 권리종류=압류, 금액=NONE, 대상소유자=장윤봉, 소멸기준=소멸, 비고=null]\n"
				+ "[접수일=2010년1월28일, 접수번호=5054, 순번=20, 권리자=국민건강보험공단, 권리종류=압류, 금액=NONE, 대상소유자=송순덕, 소멸기준=소멸, 비고=null]";

		// assertItem(expect, "fixture2/072_안산9-27165.pdf");

	}

	//
	// public void test073_2010_443() throws PdfException, Exception {
	// System.out.println("fixture2/081_인천11-6870.pdf");
	// List<등기부등본Item> items = parse("fixture2/081_인천11-6870.pdf");
	// System.out.println("#############");
	// // for (등기부등본Item item : items) {
	// // System.out.println(item);
	// // }
	// }

	public void test_083_2010_24925근저당못읽음() throws PdfException, Exception {
		String[] expected = new String[] { "[접수일=null, 접수번호=null, 순번=, 권리자=정숙희 , 권리종류=소유권, 금액=NONE, 대상소유자=단독소유, 소멸기준=소멸, 비고=null]",
				"[접수일=2003년5월29일, 접수번호=35427, 순번=7, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=520000000, 대상소유자=정숙희, 소멸기준=소멸기준, 비고=null]",
				"[접수일=2003년11월14일, 접수번호=91576, 순번=8, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=130000000, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2004년1월14일, 접수번호=3683, 순번=9, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=39000000, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2006년6월30일, 접수번호=52431, 순번=10, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=260000000, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2007년8월9일, 접수번호=48928, 순번=11, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=104000000, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2008년5월21일, 접수번호=26840, 순번=12, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=130000000, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년5월22일, 접수번호=40528, 순번=13, 권리자=중화인민공화국인 김요, 권리종류=근저당권, 금액=300000불, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년5월22일, 접수번호=40529, 순번=14, 권리자=중화인민공화국인 김영, 권리종류=근저당권, 금액=300000불, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년10월7일, 접수번호=72460, 순번=2, 권리자=국, 권리종류=압류, 금액=NONE, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년12월9일, 접수번호=87923, 순번=3, 권리자=주식회사국민은행, 권리종류=임의경매, 금액=NONE, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2011년1월11일, 접수번호=1386, 순번=4, 권리자=성남시분당구, 권리종류=압류, 금액=NONE, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]",
				"[접수일=2011년11월22일, 접수번호=75244, 순번=5, 권리자=서울특별시강남구, 권리종류=압류, 금액=NONE, 대상소유자=정숙희, 소멸기준=소멸, 비고=null]", };
		List<등기부등본Item> items = parse("fixture2/083_2010-24925근저당못읽음.pdf");
		assertItems(expected, items);

	}

	private void assertItems(String[] expected, List<등기부등본Item> items) {
		assertEquals(expected.length, items.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], items.get(i).toString());
		}
	}

	public void test084() {
		
	}

	public void testPrint() throws PdfException, Exception {
		String[] expecteds = new String[]{"[접수일=null, 접수번호=null, 순번=, 권리자=이종대 , 권리종류=소유권, 금액=NONE, 대상소유자=4분의1, 소멸기준=소멸, 비고=null]",
				"[접수일=null, 접수번호=null, 순번=, 권리자=이종대 , 권리종류=소유권, 금액=NONE, 대상소유자=4분의1, 소멸기준=소멸, 비고=null]",
				"[접수일=null, 접수번호=null, 순번=, 권리자=이종해 , 권리종류=소유권, 금액=NONE, 대상소유자=4분의1, 소멸기준=소멸, 비고=null]",
				"[접수일=null, 접수번호=null, 순번=, 권리자=이종해 , 권리종류=소유권, 금액=NONE, 대상소유자=4분의1, 소멸기준=소멸, 비고=null]",
				"[접수일=1977년12월24일, 접수번호=142968, 순번=1, 권리자=국민은행, 권리종류=근저당권, 금액=312000000, 대상소유자=이종대등, 소멸기준=소멸기준, 비고=null]",
				"[접수일=1980년5월31일, 접수번호=27778, 순번=2, 권리자=국민은행, 권리종류=근저당권, 금액=338000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1982년6월25일, 접수번호=21888, 순번=3, 권리자=국민은행, 권리종류=근저당권, 금액=195000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1984년6월30일, 접수번호=25135, 순번=4, 권리자=국민은행, 권리종류=근저당권, 금액=254000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1987년10월15일, 접수번호=44070, 순번=5, 권리자=국민은행, 권리종류=근저당권, 금액=84500000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1989년9월23일, 접수번호=43180, 순번=6, 권리자=국민은행, 권리종류=근저당권, 금액=143000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1996년12월30일, 접수번호=52179, 순번=7, 권리자=국민은행, 권리종류=근저당권, 금액=819000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=1997년8월12일, 접수번호=32315, 순번=8, 권리자=국민은행, 권리종류=근저당권, 금액=1412000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2001년7월11일, 접수번호=31743, 순번=9, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=325000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2001년7월11일, 접수번호=31744, 순번=10, 권리자=주식회사국민은행, 권리종류=근저당권, 금액=975000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2007년5월31일, 접수번호=25580, 순번=11, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=750000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2008년5월27일, 접수번호=25406, 순번=12, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=225000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2008년10월30일, 접수번호=52903, 순번=13, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=1500000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13791, 순번=14, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=4725000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13792, 순번=15, 권리자=주식회사명보정보통신, 권리종류=근저당권, 금액=3000000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13795, 순번=16, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=700000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13800, 순번=17, 권리자=주식회사명보정보통신, 권리종류=근저당권, 금액=750000000, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13802, 순번=18, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=750000000, 대상소유자=이종해등, 소멸기준=소멸, 비고=null]",
				"[접수일=2009년4월8일, 접수번호=13803, 순번=19, 권리자=주식회사명동정보통신, 권리종류=근저당권, 금액=225000000, 대상소유자=이종해등, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년1월18일, 접수번호=1727, 순번=13, 권리자=서울특별시영등포구, 권리종류=압류, 금액=NONE, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년1월29일, 접수번호=3472, 순번=14, 권리자=서울특별시영등포구, 권리종류=압류, 금액=NONE, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년12월17일, 접수번호=49859, 순번=15, 권리자=주식회사푸른이저축은행, 권리종류=임의경매, 금액=NONE, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",
				"[접수일=2011년6월10일, 접수번호=24456, 순번=16, 권리자=주식회사국민은행, 권리종류=임의경매, 금액=NONE, 대상소유자=이종대등, 소멸기준=소멸, 비고=null]",};
		 List<등기부등본Item> items =		 parse("fixture2/084_2010-27523_등기권리자_못읽음.pdf");
		 assertItems(expecteds, items);

	}
	
	
	// public void testTarget() {
	// String src = "채권자 (선정당사자)한현주";
	// final String[] 권리자종류 = new String[] { "권리자", "채권자", "근저당권자", "지상권자",
	// "전세권자", "가등기권자", "임차권자" };
	// for (String type : 권리자종류) {
	// Pattern p = Pattern.compile(type);
	// Matcher m = p.matcher(src);
	// if (m.find()){
	// String temp = src.substring(m.end()+1).trim();
	// if (temp.contains("(")) {
	// temp = temp.replaceAll("(\\([^\\)]+\\))", "");
	// System.out.println(temp);
	// }
	// }
	//
	// }
	// }
	
	public void test085_2011_10912_등기목적오류() throws PdfException, Exception{
		String[] expecteds = new String[]{"[접수일=null, 접수번호=null, 순번=, 권리자=박서린 , 권리종류=소유권, 금액=NONE, 대상소유자=2274분의431, 소멸기준=소멸, 비고=null]",
				"[접수일=null, 접수번호=null, 순번=, 권리자=송창호 , 권리종류=소유권, 금액=NONE, 대상소유자=2274분의431, 소멸기준=소멸, 비고=null]",
				"[접수일=null, 접수번호=null, 순번=, 권리자=오태근 , 권리종류=소유권, 금액=NONE, 대상소유자=2274분의1412, 소멸기준=소멸, 비고=null]",
				"[접수일=2006년1월18일, 접수번호=1981, 순번=3, 권리자=한국자산관리공사, 권리종류=근저당권, 금액=110000000, 대상소유자=오태근, 소멸기준=소멸기준, 비고=null]",
				"[접수일=2006년12월28일, 접수번호=48531, 순번=4, 권리자=주식회사 전일상호저축은행, 권리종류=근저당권, 금액=1000000000, 대상소유자=송창호, 소멸기준=소멸, 비고=null]",
				"[접수일=2008년12월22일, 접수번호=55086, 순번=5, 권리자=주식회사전일상호저축은행, 권리종류=근저당권, 금액=3198000000, 대상소유자=오태근, 소멸기준=소멸, 비고=null]",
				"[접수일=2011년8월9일, 접수번호=32794, 순번=9, 권리자=파산자주식회사전일상호저축은행의파산관재인예금보험공사, 권리종류=임의경매, 금액=NONE, 대상소유자=송창호, 소멸기준=소멸, 비고=null]",
				"[접수일=2011년8월9일, 접수번호=32794, 순번=9, 권리자=파산자주식회사전일상호저축은행의파산관재인예금보험공사, 권리종류=임의경매, 금액=NONE, 대상소유자=오태근, 소멸기준=소멸, 비고=null]",};
		 List<등기부등본Item> items =		 parse("fixture2/085_2011_10912_등기목적오류.pdf");
		 assertItems(expecteds, items);
	}
	
	public void test경매공매사이트_등기부등본_001() throws PdfException, Exception {
		String[] expecteds = new String[]{
				"[접수일=null, 접수번호=null, 순번=, 권리자=박용진 , 권리종류=소유권, 금액=NONE, 대상소유자=단독소유, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년8월25일, 접수번호=113458, 순번=2, 권리자=최선기, 권리종류=근저당권, 금액=19500000, 대상소유자=박용진, 소멸기준=소멸기준, 비고=null]",
						"[접수일=2011년11월21일, 접수번호=156343, 순번=3, 권리자=디에스오일주식회사, 권리종류=근저당권, 금액=50000000, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
						"[접수일=2012년5월24일, 접수번호=66727, 순번=13, 권리자=주식회사 국민은행, 권리종류=가압류, 금액=7011690, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
						"[접수일=2012년8월29일, 접수번호=111198, 순번=14, 권리자=최선기, 권리종류=임의경매, 금액=NONE, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
		};
		List<등기부등본Item> items =		 parse("fixture2/kk_atested001.pdf");
		System.out.println("#------------ Here");
		for (등기부등본Item item : items) {
			System.out.println(item);
		}
		assertItems(expecteds, items);
	}
	
	public void test경매공매사이트_등기부등본_002() throws PdfException, Exception {
		String[] expecteds = new String[]{
				"[접수일=null, 접수번호=null, 순번=, 권리자=박용진 , 권리종류=소유권, 금액=NONE, 대상소유자=단독소유, 소멸기준=소멸, 비고=null]",
				"[접수일=2010년8월25일, 접수번호=113458, 순번=2, 권리자=최선기, 권리종류=근저당권, 금액=19500000, 대상소유자=박용진, 소멸기준=소멸기준, 비고=null]",
						"[접수일=2011년11월21일, 접수번호=156343, 순번=3, 권리자=디에스오일주식회사, 권리종류=근저당권, 금액=50000000, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
						"[접수일=2012년5월24일, 접수번호=66727, 순번=13, 권리자=주식회사 국민은행, 권리종류=가압류, 금액=7011690, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
						"[접수일=2012년8월29일, 접수번호=111198, 순번=14, 권리자=최선기, 권리종류=임의경매, 금액=NONE, 대상소유자=박용진, 소멸기준=소멸, 비고=null]",
		};
		List<등기부등본Item> items =		 parse("fixture2/093_제주2012-9357 경매공매.pdf");
		System.out.println("#------------ Here");
		for (등기부등본Item item : items) {
			System.out.println(item);
		}
//		assertItems(expecteds, items);
	}
}