package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.si.auction.app.ret.AddressTool.Entity;

import org.apache.commons.httpclient.HttpException;

public class AddressToolTest extends TestCase {
	public void testSimple() throws HttpException, IOException {
		RetFetcher f = new RetFetcher();
//		f.prepare();
		AddressTool tool = new AddressTool(f);
		List<Entity> cityList = tool.getCityList(tool.doList[0]);
		System.out.println(cityList);
		//[강남구:68, 강동구:74, 강북구:30, 강서구:50, 관악구:62, 광진구:21, 구로구:53, 금천구:54, 노원구:35, 도봉구:32, 동대문구:23, 동작구:59, 마포구:44, 서대문구:41, 서초구:65, 성동구:20, 성북구:29, 송파구:71, 양천구:47, 영등포구:56, 용산구:17, 은평구:38, 종로구:11, 중구:14, 중랑구:26]
		List<Entity> dongList = tool.getDongList(tool.doList[0], cityList.get(0));	
		System.out.println(dongList);
		//[개포동:0103, 논현동:0108, 대치동:0106, 도곡동:0118, 삼성동:0105, 수서동:0115, 신사동:0107, 압구정동:0110, 역삼동:0101, 일원동:0114, 청담동:0104]
	}
}
