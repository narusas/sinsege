package net.narusas.aceauction.model;

import net.narusas.si.auction.builder.present.�����κ�;
import junit.framework.TestCase;

public class �����κ�Test extends TestCase {
	public void test����() {
		�����κ� item = new �����κ�("�����κ��� �ǹ��� ǥ��\n"//
				+ "    �ǹ��ǹ�ȣ : 2�� 45ȣ\n"//
				+ "    ��      �� : ö����ũ��Ʈ��\n"//
				+ "    ��      �� : ����\n"//
				+ "                 9.72��");
		assertEquals("ö����ũ��Ʈ��", item.get����());
		assertEquals("9.72��", item.get����());
		
	}
	
	public void test����2() {
		�����κ� item = new �����κ�("�����κ��� �ǹ��� ǥ��\n"//
				+ "    �ǹ��ǹ�ȣ : 2�� 201ȣ\n"//
				+ "    ��      �� : ö����ũ��Ʈ�� 63.85�� ����Ʈ"//
				);
		assertEquals("ö����ũ��Ʈ��", item.get����());
		assertEquals("63.85��", item.get����());
		
	}
}
