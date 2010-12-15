package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import junit.framework.TestCase;

public class 대법원감정평가서_DDDFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {
		String queryString = "bub_cd=000214&sa_no=20070130044821&mae_giil=2008.04.07&sano_HasGam=20070130044821&gam_no=47054";
		대법원감정평가서Fetcher fetcher = new 대법원감정평가서Fetcher();
		fetcher.fetch(true, queryString);
	}
}
