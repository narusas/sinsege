package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class 대법원사건Fetcher_결과Test extends TestCase {
	public void testFetchPage() throws HttpException, IOException {
		대법원사건Fetcher_결과 f = new 대법원사건Fetcher_결과(법원.findByCode("000210"), 
				new 담당계(법원.findByCode("000210"), "2007.08.07",null, "1006", "경매6계" ));
		System.out.println(f.getPages().length);
	}
}
