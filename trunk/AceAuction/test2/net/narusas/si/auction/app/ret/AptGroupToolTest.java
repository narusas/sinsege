package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.app.ret.AddressTool.Entity;
import net.narusas.si.auction.model.단지;
import net.narusas.si.auction.model.단지평형;
import net.narusas.si.auction.model.단지평형시세추이;

import org.apache.commons.httpclient.HttpException;

public class AptGroupToolTest extends TestCase {
	// public void testSimple() throws HttpException, IOException {
	// RetFetcher f = new RetFetcher();
	// AptGroupTool tool = new AptGroupTool(f);
	// Entity DO = new Entity("서울", "11");
	// Entity CITY = new Entity("강남구", "68");
	// Entity DONG = new Entity("개포동", "0103");
	//
	// List<단지> groups = tool.fetch(DO, CITY, DONG);
	// // System.out.println(groups);
	// // {RA_PRICE_M2=366, APTS_NAME=LG개포자이, COMPLT_MONTH=2004-06,
	// // SA_PRICE_M2=1053, TOWN_APT_CODE=249405, APTS_TOT_DONG=4,
	// // APTS_H_NUM=212, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=134, SP_USEAREA_MAX=170, APTS_CODE=18371,
	// // DANJI_AREA=4개동 212세대, JUSO=서울 강남구 개포동 12-2}
	// // {RA_PRICE_M2=370, APTS_NAME=경남1차, COMPLT_MONTH=1984-03,
	// // SA_PRICE_M2=1129, TOWN_APT_CODE=1441, APTS_TOT_DONG=6,
	// // APTS_H_NUM=408, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=84, SP_USEAREA_MAX=154, APTS_CODE=457, DANJI_AREA=6개동
	// // 408세대, JUSO=서울 강남구 개포동 649}
	// // {RA_PRICE_M2=338, APTS_NAME=경남2차, COMPLT_MONTH=1984-11,
	// // SA_PRICE_M2=1029, TOWN_APT_CODE=1442, APTS_TOT_DONG=3,
	// // APTS_H_NUM=270, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=84, SP_USEAREA_MAX=171, APTS_CODE=458, DANJI_AREA=3개동
	// // 270세대, JUSO=서울 강남구 개포동 649}
	// // {RA_PRICE_M2=356, APTS_NAME=대청, COMPLT_MONTH=1992-10,
	// // SA_PRICE_M2=1034, TOWN_APT_CODE=1443, APTS_TOT_DONG=6,
	// // APTS_H_NUM=822, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=39, SP_USEAREA_MAX=60, APTS_CODE=445, DANJI_AREA=6개동
	// // 822세대, JUSO=서울 강남구 개포동 12}
	// // {RA_PRICE_M2=361, APTS_NAME=대치2단지, COMPLT_MONTH=1991-11,
	// // SA_PRICE_M2=1042, TOWN_APT_CODE=1444, APTS_TOT_DONG=11,
	// // APTS_H_NUM=1753, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=33, SP_USEAREA_MAX=49, APTS_CODE=446, DANJI_AREA=11개동
	// // 1753세대, JUSO=서울 강남구 개포동 12}
	// // {RA_PRICE_M2=166, APTS_NAME=시영, COMPLT_MONTH=1984-02,
	// // SA_PRICE_M2=1832, TOWN_APT_CODE=1445, APTS_TOT_DONG=30,
	// // APTS_H_NUM=1970, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=28, SP_USEAREA_MAX=57, APTS_CODE=459, DANJI_AREA=30개동
	// // 1970세대, JUSO=서울 강남구 개포동 656}
	// // {RA_PRICE_M2=345, APTS_NAME=우성3차, COMPLT_MONTH=1984-12,
	// // SA_PRICE_M2=1013, TOWN_APT_CODE=1447, APTS_TOT_DONG=5,
	// // APTS_H_NUM=405, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=84, SP_USEAREA_MAX=161, APTS_CODE=461, DANJI_AREA=5개동
	// // 405세대, JUSO=서울 강남구 개포동 652}
	// // {RA_PRICE_M2=314, APTS_NAME=우성6차, COMPLT_MONTH=1987-11,
	// // SA_PRICE_M2=1087, TOWN_APT_CODE=1448, APTS_TOT_DONG=8,
	// // APTS_H_NUM=270, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=54, SP_USEAREA_MAX=79, APTS_CODE=462, DANJI_AREA=8개동
	// // 270세대, JUSO=서울 강남구 개포동 658-1}
	// // {RA_PRICE_M2=360, APTS_NAME=우성8차, COMPLT_MONTH=1987-09,
	// // SA_PRICE_M2=1016, TOWN_APT_CODE=1449, APTS_TOT_DONG=3,
	// // APTS_H_NUM=261, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=78, SP_USEAREA_MAX=79, APTS_CODE=463, DANJI_AREA=3개동
	// // 261세대, JUSO=서울 강남구 개포동 179}
	// // {RA_PRICE_M2=400, APTS_NAME=우성9차, COMPLT_MONTH=1991-01,
	// // SA_PRICE_M2=1010, TOWN_APT_CODE=1450, APTS_TOT_DONG=2,
	// // APTS_H_NUM=232, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=81, SP_USEAREA_MAX=84, APTS_CODE=464, DANJI_AREA=2개동
	// // 232세대, JUSO=서울 강남구 개포동 651-1}
	// // {RA_PRICE_M2=174, APTS_NAME=주공1단지, COMPLT_MONTH=1982-06,
	// // SA_PRICE_M2=1976, TOWN_APT_CODE=256470, APTS_TOT_DONG=124,
	// // APTS_H_NUM=5040, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=35, SP_USEAREA_MAX=61, APTS_CODE=465, DANJI_AREA=124개동
	// // 5040세대, JUSO=서울 강남구 개포동 141}
	// // {RA_PRICE_M2=244, APTS_NAME=주공2단지, COMPLT_MONTH=1982-11,
	// // SA_PRICE_M2=1850, TOWN_APT_CODE=1452, APTS_TOT_DONG=32,
	// // APTS_H_NUM=1400, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=25, SP_USEAREA_MAX=80, APTS_CODE=466, DANJI_AREA=32개동
	// // 1400세대, JUSO=서울 강남구 개포동 140}
	// // {RA_PRICE_M2=195, APTS_NAME=주공3단지, COMPLT_MONTH=1982-11,
	// // SA_PRICE_M2=2108, TOWN_APT_CODE=1453, APTS_TOT_DONG=25,
	// // APTS_H_NUM=1160, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=35, SP_USEAREA_MAX=50, APTS_CODE=467, DANJI_AREA=25개동
	// // 1160세대, JUSO=서울 강남구 개포동 138}
	// // {RA_PRICE_M2=196, APTS_NAME=주공4단지, COMPLT_MONTH=1982-11,
	// // SA_PRICE_M2=1867, TOWN_APT_CODE=1454, APTS_TOT_DONG=58,
	// // APTS_H_NUM=2840, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=35, SP_USEAREA_MAX=50, APTS_CODE=468, DANJI_AREA=58개동
	// // 2840세대, JUSO=서울 강남구 개포동 189}
	// // {RA_PRICE_M2=384, APTS_NAME=주공5단지, COMPLT_MONTH=1983-10,
	// // SA_PRICE_M2=1208, TOWN_APT_CODE=50421, APTS_TOT_DONG=6,
	// // APTS_H_NUM=940, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=53, SP_USEAREA_MAX=83, APTS_CODE=469, DANJI_AREA=6개동
	// // 940세대, JUSO=서울 강남구 개포동 187}
	// // {RA_PRICE_M2=389, APTS_NAME=주공6단지, COMPLT_MONTH=1983-10,
	// // SA_PRICE_M2=1225, TOWN_APT_CODE=1455, APTS_TOT_DONG=9,
	// // APTS_H_NUM=1060, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=53, SP_USEAREA_MAX=83, APTS_CODE=470, DANJI_AREA=9개동
	// // 1060세대, JUSO=서울 강남구 개포동 185}
	// // {RA_PRICE_M2=389, APTS_NAME=주공7단지, COMPLT_MONTH=1983-10,
	// // SA_PRICE_M2=1169, TOWN_APT_CODE=256608, APTS_TOT_DONG=8,
	// // APTS_H_NUM=900, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=53, SP_USEAREA_MAX=83, APTS_CODE=471, DANJI_AREA=8개동
	// // 900세대, JUSO=서울 강남구 개포동 185}
	// // {RA_PRICE_M2=344, APTS_NAME=현대1차, COMPLT_MONTH=1984-04,
	// // SA_PRICE_M2=1066, TOWN_APT_CODE=1457, APTS_TOT_DONG=6,
	// // APTS_H_NUM=416, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=83, SP_USEAREA_MAX=166, APTS_CODE=472, DANJI_AREA=6개동
	// // 416세대, JUSO=서울 강남구 개포동 653}
	// // {RA_PRICE_M2=364, APTS_NAME=현대2차, COMPLT_MONTH=1986-01,
	// // SA_PRICE_M2=1178, TOWN_APT_CODE=1458, APTS_TOT_DONG=13,
	// // APTS_H_NUM=558, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=83, SP_USEAREA_MAX=165, APTS_CODE=473, DANJI_AREA=13개동
	// // 558세대, JUSO=서울 강남구 개포동 654}
	// // {RA_PRICE_M2=356, APTS_NAME=현대3차, COMPLT_MONTH=1986-03,
	// // SA_PRICE_M2=1227, TOWN_APT_CODE=1459, APTS_TOT_DONG=5,
	// // APTS_H_NUM=198, APTS_SHT_NAME=null, APTP_DATE=20100405,
	// // SP_USEAREA_MIN=84, SP_USEAREA_MAX=163, APTS_CODE=474, DANJI_AREA=5개동
	// // 198세대, JUSO=서울 강남구 개포동 177}
	//
	// // 단지명 마을명 단지규모 사용승인월 면적분포 매매 전세
	// // LG개포자이 4개동 212세대 2004-06 134 ~ 170㎡ 1,053 366
	// // 경남1차
	// // 6개동 408세대 1984-03 84 ~ 154㎡ 1,129 370
	// // 경남2차
	// // 3개동 270세대 1984-11 84 ~ 171㎡ 1,029 338
	// // 대청
	// // 6개동 822세대 1992-10 39 ~ 60㎡ 1,034 356
	// // 대치2단지
	// // 11개동 1753세대 1991-11 33 ~ 49㎡ 1,042 361
	// // 시영
	// // 30개동 1970세대 1984-02 28 ~ 57㎡ 1,832 166
	// // 우성3차
	// // 5개동 405세대 1984-12 84 ~ 161㎡ 1,013 345
	// // 우성6차
	// // 8개동 270세대
	//
	// }

	public void testRegx1() {
		String src = "dwr.engine.remote.handleCallback(\"6\",\"0\",{status:\"OK\",result:[{JUSO:\"서울 강동구 고덕동 492\",\"DANJI_AREA\":\"43개동 2500세대\",\"APTS_CODE\":346,\"SP_USEAREA_MAX\":59,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":34,\"APTS_TOT_DONG\":43,\"APTS_H_NUM\":2500,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":1555,\"COMPLT_MONTH\":\"1984-05\",\"SA_PRICE_M2\":1271,\"APTS_NAME\":\"고덕시영\",\"RA_PRICE_M2\":142},{JUSO:\"서울 강동구 고덕동 312\",\"DANJI_AREA\":\"4개동 448세대\",\"APTS_CODE\":345,\"SP_USEAREA_MAX\":84,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":59,\"APTS_TOT_DONG\":4,\"APTS_H_NUM\":448,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":1559,\"COMPLT_MONTH\":\"1995-11\",\"SA_PRICE_M2\":633,\"APTS_NAME\":\"배재현대\",\"RA_PRICE_M2\":244},{JUSO:\"서울 강동구 고덕동 220\",\"DANJI_AREA\":\"2개동 171세대\",\"APTS_CODE\":340,\"SP_USEAREA_MAX\":107,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":66,\"APTS_TOT_DONG\":2,\"APTS_H_NUM\":171,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":1554,\"COMPLT_MONTH\":\"1986-06\",\"SA_PRICE_M2\":713,\"APTS_NAME\":\"삼익그린12차\",\"RA_PRICE_M2\":193},{JUSO:\"서울 강동구 고덕동 486\",\"DANJI_AREA\":\"8개동 807세대\",\"APTS_CODE\":341,\"SP_USEAREA_MAX\":84,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":50,\"APTS_TOT_DONG\":8,\"APTS_H_NUM\":807,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":1556,\"COMPLT_MONTH\":\"1996-03\",\"SA_PRICE_M2\":541,\"APTS_NAME\":\"아남\",\"RA_PRICE_M2\":218},{JUSO:\"서울 강동구 고덕동 212\",\"DANJI_AREA\":\"71개동 2600세대\",\"APTS_CODE\":343,\"SP_USEAREA_MAX\":55,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":32,\"APTS_TOT_DONG\":71,\"APTS_H_NUM\":2600,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":1558,\"COMPLT_MONTH\":\"1982-09\",\"SA_PRICE_M2\":1353,\"APTS_NAME\":\"주공2단지\",\"RA_PRICE_M2\":141}]});";

		Pattern p2 = Pattern.compile("JUSO:\"([^\"]+)\"," + "\"DANJI_AREA\":\"([^\"]+)\"," + "\"APTS_CODE\":([^,]+),"
				+ "\"SP_USEAREA_MAX\":([^,]+)," + "\"APTP_DATE\":\"([^\"]+)\"," + "\"SP_USEAREA_MIN\":([^,]+),"
				+ "\"APTS_TOT_DONG\":([^,]+)," + "\"APTS_H_NUM\":([^,]+)," + "\"APTS_SHT_NAME\":([^,]+),"
				+ "\"TOWN_APT_CODE\":([^,]+)," + "\"COMPLT_MONTH\":\"([^\"]+)\"," + "\"SA_PRICE_M2\":([^,]+),"
				+ "\"APTS_NAME\":\"([^\"]+)\"," + "\"RA_PRICE_M2\":([^,]+)"

		);
		Matcher m = p2.matcher(src);
		while (m.find()) {
			// System.out.println(m.group(1));
			// System.out.println(m.group(2));
			// System.out.println(m.group(3));
			// System.out.println(m.group(4));
			// System.out.println(m.group(5));
			// System.out.println(m.group(6));
			// System.out.println(m.group(7));
			// System.out.println(m.group(8));
			// System.out.println(m.group(9));
			// System.out.println(m.group(10));
			// System.out.println(m.group(11));
			// System.out.println(m.group(12));
			// System.out.println(m.group(13));
		}

		Pattern p = Pattern.compile("\\{(JUSO[^\\}]+)");
		m = p.matcher(src);
		p2 = Pattern.compile("[\"]*([^:\"]+)[\"]*:[\"]*([^,\"]+)[\"}]*");
		while (m.find()) {
			// System.out.println(m.group(1));
			Matcher m2 = p2.matcher(m.group(1));
			while (m2.find()) {
				// System.out.println(m2.group(1)+":"+m2.group(2));
			}

		}
	}

	public void testInfo() throws HttpException, IOException {
		// /
		// RetFetcher f = new RetFetcher();
		// AptGroupTool tool = new AptGroupTool(f);
		//
		// 단지 group = tool
		// .parse단지("JUSO:\"서울 강남구 개포동 12-2\",\"DANJI_AREA\":\"4개동 212세대\",\"APTS_CODE\":18371,\"SP_USEAREA_MAX\":170,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":134,\"APTS_TOT_DONG\":4,\"APTS_H_NUM\":212,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":249405,\"COMPLT_MONTH\":\"2004-06\",\"SA_PRICE_M2\":1053,\"APTS_NAME\":\"LG개포자이\",\"RA_PRICE_M2\":366");
		// System.out.println(group);
		// System.out.println(group.getCode());
		// // code = 18371
		// group.setCode("457");
		// tool.updateInfo(group);
		// System.out.println(group);
	}

	public void testAreaList() throws HttpException, IOException {
//		RetFetcher f = new RetFetcher();
//		AptGroupTool tool = new AptGroupTool(f);
//
//		단지 group = tool
//				.parse단지("JUSO:\"서울 강남구 개포동 12-2\",\"DANJI_AREA\":\"4개동 212세대\",\"APTS_CODE\":18371,\"SP_USEAREA_MAX\":170,\"APTP_DATE\":\"20100405\",\"SP_USEAREA_MIN\":134,\"APTS_TOT_DONG\":4,\"APTS_H_NUM\":212,\"APTS_SHT_NAME\":null,\"TOWN_APT_CODE\":249405,\"COMPLT_MONTH\":\"2004-06\",\"SA_PRICE_M2\":1053,\"APTS_NAME\":\"LG개포자이\",\"RA_PRICE_M2\":366");
//		List<단지평형> type = tool.fetch평형종류(group);
//		System.out.println(type);
		/**
		 * 
{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=48, SU_PRICE=139,000, RA_PRICE=47,500, SA_PRICE=133,500, RU_PRICE=50,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=84, RL_PRICE=45,000, SP_USEAREA=135, SL_PRICE=128,000}
{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=55, SU_PRICE=174,000, RA_PRICE=57,500, SA_PRICE=163,500, RU_PRICE=60,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=44, RL_PRICE=55,000, SP_USEAREA=154, SL_PRICE=153,000}
{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=61, SU_PRICE=190,000, RA_PRICE=62,500, SA_PRICE=182,500, RU_PRICE=65,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=84, RL_PRICE=60,000, SP_USEAREA=171, SL_PRICE=175,000}
[48[{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=48, SU_PRICE=139,000, RA_PRICE=47,500, SA_PRICE=133,500, RU_PRICE=50,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=84, RL_PRICE=45,000, SP_USEAREA=135, SL_PRICE=128,000}], 55[{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=55, SU_PRICE=174,000, RA_PRICE=57,500, SA_PRICE=163,500, RU_PRICE=60,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=44, RL_PRICE=55,000, SP_USEAREA=154, SL_PRICE=153,000}], 61[{APTP_DATE=20100405, APTS_CODE=18371, PYN_TYPE=61, SU_PRICE=190,000, RA_PRICE=62,500, SA_PRICE=182,500, RU_PRICE=65,000, APTS_NAME=LG개포자이, SP_TOT_HOUSE=84, RL_PRICE=60,000, SP_USEAREA=171, SL_PRICE=175,000}]]

		 */
	}
	
	public void testParseArea() throws HttpException, IOException {
//		String src = "[{\"SL_PRICE\":\"                43,000\",\"SP_USEAREA\":35,\"RL_PRICE\":\"                 4,000\",\"SP_TOT_HOUSE\":800,\"APTS_NAME\":\"고덕시영\",\"RU_PRICE\":\"                 5,000\",\"SA_PRICE\":\"                44,000\",\"RA_PRICE\":\"                 4,500\",\"SU_PRICE\":\"                45,000\",\"PYN_TYPE\":13,\"APTS_CODE\":346,\"APTP_DATE\":\"20100405\"},{\"SL_PRICE\":\"                54,000\",\"SP_USEAREA\":44,\"RL_PRICE\":\"                 5,500\",\"SP_TOT_HOUSE\":1000,\"APTS_NAME\":\"고덕시영\",\"RU_PRICE\":\"                 6,000\",\"SA_PRICE\":\"                54,500\",\"RA_PRICE\":\"                 5,750\",\"SU_PRICE\":\"                55,000\",\"PYN_TYPE\":17,\"APTS_CODE\":346,\"APTP_DATE\":\"20100405\"},{\"SL_PRICE\":\"                62,000\",\"SP_USEAREA\":50,\"RL_PRICE\":\"                 7,000\",\"SP_TOT_HOUSE\":500,\"APTS_NAME\":\"고덕시영\",\"RU_PRICE\":\"                 8,000\",\"SA_PRICE\":\"                63,000\",\"RA_PRICE\":\"                 7,500\",\"SU_PRICE\":\"                64,000\",\"PYN_TYPE\":19,\"APTS_CODE\":346,\"APTP_DATE\":\"20100405\"},{\"SL_PRICE\":\"                72,000\",\"SP_USEAREA\":59,\"RL_PRICE\":\"                 9,000\",\"SP_TOT_HOUSE\":200,\"APTS_NAME\":\"고덕시영\",\"RU_PRICE\":\"                10,500\",\"SA_PRICE\":\"                73,000\",\"RA_PRICE\":\"                 9,750\",\"SU_PRICE\":\"                74,000\",\"PYN_TYPE\":22,\"APTS_CODE\":346,\"APTP_DATE\":\"20100405\"}]";
//		RetFetcher f = new RetFetcher();
//		AptGroupTool tool = new AptGroupTool(f);
//		System.out.println(tool.parseAreaType(src));
	}
	
	
	public void testSellTrend() throws HttpException, IOException{
		RetFetcher fetcher = new RetFetcher();
		AddressTool addrTool = new AddressTool(fetcher);
		
		List<Entity> citys = addrTool.getCityList(AddressTool.doList[0]);
		List<Entity> dongs = addrTool.getDongList(AddressTool.doList[0], citys.get(0));
		
		Entity DO = AddressTool.doList[0];
		Entity CITY = citys.get(0);
		Entity DONG = dongs.get(0);
		
		
		
		
		
		AptGroupTool groupTool = new AptGroupTool(fetcher);
		List<단지> groups = groupTool.fetch(DO, CITY, DONG);
//		System.out.println(groups.get(0));
		단지 group = groups.get(0);
		List<단지평형> list = groupTool.fetch평형종류(group);
		groupTool.update단지평형(group, list);
//		단지평형 type = new 단지평형();
//		type.setCode(55L);
//		List<단지평형시세추이> trend = tool.fetch시세추이(group, type);
		/**
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100320","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100220","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},
		 * {"RU_COLOR":"up","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100120","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":3000,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20091220","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               163,500","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":1000,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20091120","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"up","RU_PRICE":"                57,000","SA_PRICE":"               163,500","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":3000,"SU_PRICE":"               173,000","APTS_CODE":18371,"APTP_DATE":"20091020","SL_PRICE":"               152,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"up","RU_PRICE":"                57,000","SA_PRICE":"               162,500","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090920","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090820","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090720","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090620","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090520","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090420","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090320","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"}]});

null

		 */
//		System.out.println(trend);
	}

}
