package net.narusas.si.auction.fetchers;

import junit.framework.TestCase;

public class MessageFormatTest extends TestCase {
	public void test담당계CodeToPath() {
		StringBuffer buf = new StringBuffer(String.valueOf(210));
		for(int i=buf.length();i<6;i++){
			buf.insert(0, '0');
		}
	
		System.out.println(buf);
	}
}
