package net.narusas.aceauction.fetchers;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
import net.narusas.si.auction.builder.present.물건현황;

public class 대법원사건FetcherMergeTest extends TestCase {

	public void testMerge() {
		사건 s1 = new 사건(법원.get(0), new 담당계(법원.get(0), "","","",""));
		사건 s2 = new 사건(법원.get(0), new 담당계(법원.get(0), "","","",""));
		
		s1.사건번호 = 200413000000L;
		s2.사건번호 = 200413000000L;
		물건 m1 = new 물건(1,"",false,"1","comment","price","low");
		물건 m2 = new 물건(1,"",false,"1","comment","price","low");
		
		m1.add(new 물건현황("address1"));
		m2.add(new 물건현황("address2"));
		s1.add(m1);
		s2.add(m2);
		
		ArrayList<사건> src = new ArrayList<사건>();
		src.add(s1);
		src.add(s2);
		
//		System.out.println(s1.items.get(0).물건현황);
//		System.out.println(s2.items.get(0).물건현황);
		
		ArrayList<사건> res = 대법원사건Fetcher.merge(src);
		
		assertEquals(1, res.size());
//		System.out.println(res.get(0).items);
		assertEquals(1, res.get(0).items.size());
//		System.out.println(res.get(0).items.get(0).물건현황);
		assertEquals(2, res.get(0).items.get(0).물건현황.size());
		
		
	}
}
