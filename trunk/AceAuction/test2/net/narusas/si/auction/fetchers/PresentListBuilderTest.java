package net.narusas.si.auction.fetchers;

import java.io.File;

import junit.framework.TestCase;
import net.narusas.si.auction.model.건물현황;
import net.narusas.si.auction.model.대지권현황;
import net.narusas.si.auction.model.물건;
import net.narusas.util.lang.NFile;

public class PresentListBuilderTest extends TestCase {
	private String html;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		html = NFile.getText(new File("fixture2/009_물건내역.html"));

	}

	public void testSimple() {
		물건 goods = new 물건();
		goods.set건물현황(new 건물현황());
		goods.set대지권현황(new 대지권현황());
//		goods.add부동산표시(1, new 주소(){
//			@Override
//			public String toString() {
//				return "서울특별시 종로구 숭인동 1479";
//			}
//			 @Override
//			public String get번지이하() {
//				return "1479";
//			}
//		});
//		부동산표시목록Builder.build(goods, 1, "", "1동의 건물의 표시\n"+
//				"    서울특별시 종로구 숭인동 1479\n"+
//				"    철근콘크리트 및 별돌조\n"+
//				"    평스라브지붕 다세대주택\n"+
//				"    1층 73.92㎡\n"+
//				"    2층 83.58㎡\n"+
//				"    3층 70.00㎡\n"+
//				"    4층 48.00㎡\n"+
//				"    지층 89.32㎡\n"+
//				"    대한빌라\n"+
//				"\n"+
//				"전유부분의 건물의 표시\n"+
//				"    건물의 번호 : 지1층 비1호\n"+
//				"    구          조 : 철근콘크리트 및 벽돌조 82.12㎡\n"+
//				"\n"+
//				"대지권의 목적인 토지의 표시\n"+
//				"    토 지 의  표시 : 1. 서울특별시종로구숭인동1479\n"+
//				"                            대 159㎡\n"+
//				"    대지권의 종류 : 1. 소유권\n"+
//				"    대지권의 비율 : 1. 159 분의 53.33"+
//				"");
//		System.out.println(goods.get건물현황().get건물목록());
//		System.out.println(goods.get대지권현황().get대지권목록());
	}
}
