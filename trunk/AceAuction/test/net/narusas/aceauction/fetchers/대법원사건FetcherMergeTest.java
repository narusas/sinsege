package net.narusas.aceauction.fetchers;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.si.auction.builder.present.������Ȳ;

public class ��������FetcherMergeTest extends TestCase {

	public void testMerge() {
		��� s1 = new ���(����.get(0), new ����(����.get(0), "","","",""));
		��� s2 = new ���(����.get(0), new ����(����.get(0), "","","",""));
		
		s1.��ǹ�ȣ = 200413000000L;
		s2.��ǹ�ȣ = 200413000000L;
		���� m1 = new ����(1,"",false,"1","comment","price","low");
		���� m2 = new ����(1,"",false,"1","comment","price","low");
		
		m1.add(new ������Ȳ("address1"));
		m2.add(new ������Ȳ("address2"));
		s1.add(m1);
		s2.add(m2);
		
		ArrayList<���> src = new ArrayList<���>();
		src.add(s1);
		src.add(s2);
		
//		System.out.println(s1.items.get(0).������Ȳ);
//		System.out.println(s2.items.get(0).������Ȳ);
		
		ArrayList<���> res = ��������Fetcher.merge(src);
		
		assertEquals(1, res.size());
//		System.out.println(res.get(0).items);
		assertEquals(1, res.get(0).items.size());
//		System.out.println(res.get(0).items.get(0).������Ȳ);
		assertEquals(2, res.get(0).items.get(0).������Ȳ.size());
		
		
	}
}
