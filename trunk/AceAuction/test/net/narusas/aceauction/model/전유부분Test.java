package net.narusas.aceauction.model;

import net.narusas.si.auction.builder.present.전유부분;
import junit.framework.TestCase;

public class 전유부분Test extends TestCase {
	public void test면적() {
		전유부분 item = new 전유부분("전유부분의 건물의 표시\n"//
				+ "    건물의번호 : 2층 45호\n"//
				+ "    구      조 : 철근콩크리트조\n"//
				+ "    면      적 : 점포\n"//
				+ "                 9.72㎡");
		assertEquals("철근콩크리트조", item.get구조());
		assertEquals("9.72㎡", item.get면적());
		
	}
	
	public void test면적2() {
		전유부분 item = new 전유부분("전유부분의 건물의 표시\n"//
				+ "    건물의번호 : 2층 201호\n"//
				+ "    구      조 : 철근콘크리트조 63.85㎡ 아프트"//
				);
		assertEquals("철근콘크리트조", item.get구조());
		assertEquals("63.85㎡", item.get면적());
		
	}
}
