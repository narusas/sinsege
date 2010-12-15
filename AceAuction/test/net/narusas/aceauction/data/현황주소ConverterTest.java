package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class 泅炔林家ConverterTest extends TestCase {
	public void test1() {
		assertEquals("林家", 泅炔林家Converter.convert("林家 寇 3 鞘瘤"));
		assertEquals("林家", 泅炔林家Converter.convert("林家 寇3鞘瘤"));
		assertEquals("林家", 泅炔林家Converter.convert("林家寇3鞘瘤"));
	}
}
