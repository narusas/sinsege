package net.narusas.si.auction.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class 토지Test {

	@Test
	public void test() {
		 토지  land = new  토지();
		 
		 // 10
		 //   A - 30%
		 // 5/1
		 land.set매각지분("1000");
		 assertEquals("500", land.calc매각지분("100분의 50 이정희 지분 전부"));
		 assertEquals("500", land.calc매각지분("2분의1 이정희 지분 전부"));
		 assertEquals("500", land.calc매각지분("12분의 6 이정희 지분 전부"));
		 assertEquals("500", land.calc매각지분("1/2 이정희 지분 전부"));
		 assertEquals("500", land.calc매각지분("2분지1 이정희 지분 전부"));
		 assertEquals("500", land.calc매각지분("2분1 이정희 지분 전부"));
		 
		 assertEquals("750", land.calc매각지분("12분의 3 이정희 지분 전부, 6분의 3 홍길동 지분 전부"));
	}

}
