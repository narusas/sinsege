package net.narusas.si.auction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import net.narusas.si.auction.model.법원;
import junit.framework.TestCase;

public class FixedDateChargeFetcherTest extends TestCase {
	public void testSample() throws HttpException, IOException, Exception {
		법원 청주지방법원 = new 법원();
		청주지방법원.set법원명("청주지방법원");
		청주지방법원.set법원코드(270);
		
		기일입찰목록Fetcher f = new 기일입찰목록Fetcher();
		System.out.println(f.fetch(청주지방법원));
	}
}
