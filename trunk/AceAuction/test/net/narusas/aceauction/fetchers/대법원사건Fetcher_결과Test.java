package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class ��������Fetcher_���Test extends TestCase {
	public void testFetchPage() throws HttpException, IOException {
		��������Fetcher_��� f = new ��������Fetcher_���(����.findByCode("000210"), 
				new ����(����.findByCode("000210"), "2007.08.07",null, "1006", "���6��" ));
		System.out.println(f.getPages().length);
	}
}
