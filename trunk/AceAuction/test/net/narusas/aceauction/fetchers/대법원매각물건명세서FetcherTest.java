package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;

public class ������Ű����Ǹ���FetcherTest extends TestCase {
	public void test1() throws IOException {
		// http://www.courtauction.go.kr/au/SuperServlet?target_command=au.command.auc.C101MyungCommand&search_flg=2&bub_cd=000210&sa_no=20040130010146&maemul_no=1&browser=2&check_msg=
		������Ű����Ǹ���Fetcher fetcher = new ������Ű����Ǹ���Fetcher();
//		 System.out.println(fetcher.getPage("000210", "20040130051826", "1"));
		���� m = new ����();
		System.out.println(fetcher.parse(fetcher.getPage("000210", 20040130051826L, "1"), m));
		System.out.println(m.get�Ű����Ǹ���html());
	}
}
