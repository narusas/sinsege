package net.narusas.si.auction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

public class EventGamjungFetcherTest {
	@Test
	public void 테스트() throws HttpException, IOException {
		사건감정평가서Fetcher f = new 사건감정평가서Fetcher();
		f.fetch("","");
	}
}
