package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class 대법원_매각결과_담당계FetcherTest extends TestCase {
	public void test() throws HttpException, IOException {
		담당계Fetcher f = 담당계Fetcher.get담당계Fetcher_매각결과(법원.findByCode("000210"));
		// System.out.println(f.fetchPage());
		List<담당계> list = f.fetchCharges();
		for (담당계 담당계 : list) {
			System.out.println(담당계+":"+담당계.get담당계코드());
		}
	}
}
