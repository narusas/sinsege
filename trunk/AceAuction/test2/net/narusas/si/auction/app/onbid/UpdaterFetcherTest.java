package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class UpdaterFetcherTest extends TestCase {
	public void test1() throws HttpException, IOException {
		List<공매결과일정>res = new 공매결과일정Fetcher().fetch();
		for (공매결과일정 공매결과일정 : res) {
			System.out.println(공매결과일정);
		}
	}
	/**
	 * function goTo(auction_no, business_type, up_pageSize, up_pageIndex, up_orderfield, up_flag, strOpenState){ // 상세보기
		if(strOpenState=="0003"){	//03.29 bj.park
			location.href="/frontup/portal/result/control/result/getAuctionInfoKamco.do?AUCTION_NO="+auction_no+"&BUSINESS_TYPE="+business_type+
			"&up_pageSize="+up_pageSize+ "&up_pageIndex="+up_pageIndex+ "&up_orderfield="+up_orderfield+ "&up_flag="+up_flag;
		}else{
			alert("집행중입니다.");
		}
	}
	 */
}
