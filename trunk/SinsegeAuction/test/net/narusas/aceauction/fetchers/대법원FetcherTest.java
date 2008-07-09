package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class 대법원FetcherTest extends TestCase {

	public void testFetch() throws HttpException, IOException {
//		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
//		System.out
//				.println(fetcher
//						.fetch("/au/SuperServlet?target_command=au.command.auc.C110ListCommand&browser=2&check_msg=&bub_cd=000211"));
	}

	public void test2() throws IOException {
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		fetcher.prepare();
		
//		fetcher.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&bub_cd=000210&search_flg=2&mae_giil=2007.03.27&jp_cd=1003&dam_nm=%B0%E6%B8%C53%B0%E8&browser=2&check_msg=");
//		fetcher.fetch("/au/SuperServlet?target_command=au.command.auc.C311ListCommand&sa_no=20050130009723&bub_cd=000210&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&mae_giil=2007.03.27&jp_cd=1003&dam_nm=%B0%E6%B8%C53%B0%E8&allbub=000210&browser=2&check_msg=");
		System.out.println(fetcher.fetch("/au/SuperServlet?target_command=au.command.auc.C311ListCommand&sa_no=20050130007062&bub_cd=000210&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&mae_giil=2007.03.27&jp_cd=1003&dam_nm=%B0%E6%B8%C53%B0%E8&allbub=000210&browser=2&check_msg="));
	}
}
