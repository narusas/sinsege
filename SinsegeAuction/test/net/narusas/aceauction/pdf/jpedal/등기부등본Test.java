package net.narusas.aceauction.pdf.jpedal;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class ���εTest extends TestCase {
	public void test���ѵ鿩����() throws Exception { 
		���εParser parser = new ���εParser();
		List<���ε_����> it = parser.parse("fixtures/2004-47568 �����߾�.pdf");
		for (���ε_���� s : it) {
			s.processRight();
			Set<String> names = s.getRightPeoples();
			
			System.out.println(s);
		}
		
	}
}
