package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import junit.framework.TestCase;

public class ����������򰡼�_DDDFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {
		String queryString = "bub_cd=000214&sa_no=20070130044821&mae_giil=2008.04.07&sano_HasGam=20070130044821&gam_no=47054";
		����������򰡼�Fetcher fetcher = new ����������򰡼�Fetcher();
		fetcher.fetch(true, queryString);
	}
}
