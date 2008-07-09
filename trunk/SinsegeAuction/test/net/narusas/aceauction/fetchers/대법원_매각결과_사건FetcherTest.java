package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class 대법원_매각결과_사건FetcherTest extends TestCase {
	public void test1() throws HttpException, IOException {
//		대법원사건Fetcher_결과 f = new 대법원사건Fetcher_결과(법원.findByCode("000210"), "2007.08.07", "1006", "경매6계");
//
//		String[] pages = f.getPages();
//		System.out.println(pages.length);
//		// System.out.println(pages[0]);
//		List<String> sa_nos = f.parse(pages[0]);
//
//		// http://www.courtauction.go.kr/au/SuperServlet?target_command=au.command.auc.C313ListCommand&search_flg=6&bub_cd=000210&sa_no=20070130004692&browser=2&check_msg=&jong_day=&page=2&giil=2007.08.07&jp_cd=1006&dam_nm=%B0%E6%B8%C56%B0%E8
//
//		대법원기일내역Fetcher_결과 f2 = new 대법원기일내역Fetcher_결과();
//		Table t = f2.get기일내역Table("000210", "경매6계", "1006",  "2007.08.07", sa_nos.get(0));
//		System.out.println(t);
	}
}
