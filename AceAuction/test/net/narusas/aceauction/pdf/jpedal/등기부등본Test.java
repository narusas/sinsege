package net.narusas.aceauction.pdf.jpedal;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class 등기부등본Test extends TestCase {
	public void test권한들여쓰기() throws Exception { 
		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> it = parser.parse("fixtures/2004-47568 서울중앙.pdf");
		for (등기부등본_사항 s : it) {
			s.processRight();
			Set<String> names = s.getRightPeoples();
			
			System.out.println(s);
		}
		
	}
}
