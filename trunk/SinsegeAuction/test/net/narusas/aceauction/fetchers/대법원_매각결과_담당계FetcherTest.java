package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class �����_�Ű����_����FetcherTest extends TestCase {
	public void test() throws HttpException, IOException {
		����Fetcher f = ����Fetcher.get����Fetcher_�Ű����(����.findByCode("000210"));
		// System.out.println(f.fetchPage());
		List<����> list = f.fetchCharges();
		for (���� ���� : list) {
			System.out.println(����+":"+����.get�����ڵ�());
		}
	}
}
