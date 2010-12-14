package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class UpdateItemFetcherTest extends TestCase {
	public void test1() throws HttpException, IOException {
		공매결과물건Fetcher f = new 공매결과물건Fetcher();
		공매결과일정 schedule = new  공매결과일정();
		schedule.setLinkInof("175055", "business_type", "up_pageSize", "up_pageIndex", "","","");
		List<공매물건> list = f.fetch(schedule);
		for (공매물건 공매물건 : list) {
			System.out.println(공매물건);
		}
	}
}
