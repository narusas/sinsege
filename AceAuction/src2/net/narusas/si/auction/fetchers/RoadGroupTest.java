package net.narusas.si.auction.fetchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class RoadGroupTest {

	@Test
	public void test() {
		AddressBuilder builder = new AddressBuilder();
		assertNull(builder.roadGroup("아차산로"));
		assertNull(builder.roadGroup("3·15대로"));
		assertEquals("용정길", builder.roadGroup("용정길46번길"));
		assertEquals("군포로", builder.roadGroup("군포로490번길"));
		assertEquals("조치원길", builder.roadGroup("조치원5길"));
		assertEquals("청마로", builder.roadGroup("청마로148번길"));
		assertEquals("새천년대로", builder.roadGroup("새천년대로1075번길"));
		assertEquals("당동로", builder.roadGroup("당동1로"));
		
	}


}
