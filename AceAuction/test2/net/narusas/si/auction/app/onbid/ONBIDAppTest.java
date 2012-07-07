package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class ONBIDAppTest extends TestCase {
	// public void testFetch() throws IOException {
	//
	// String temp = "<tr align=center>\n"
	// + "		<td height=25>\n"
	// + "		국유재산<br>&nbsp;&nbsp;(유가증권)\n"
	// + "		</td>\n"
	// + "		<td bgcolor=#d6dbe1 width=1></td>\n"
	// + "		<td>\n"
	// + "		국유증권실\n"
	// + "		</td>\n"
	// + "		<td bgcolor=#d6dbe1 width=1></td>\n"
	// + "		<td>018-001</td>\n"
	// + "		<td bgcolor=#d6dbe1 width=1></td>\n"
	// +
	// "		<td><font color=#000000>2010/09/13 10:00 ~ 2010/09/14 18:00</font></td>\n"
	// + "		<td bgcolor=#d6dbe1 width=1></td>\n"
	// + "		<td><font color=#000000>2010/09/15 10:00</font></td>\n"
	// + "		<td bgcolor=#d6dbe1 width=1></td>\n"
	// + "		<td>\n"
	// + "			<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
	// + "				<tr><td height=3></td></tr>\n"
	// + "				<tr>\n"
	// + "					<td><img src=/images_up/icon/icon_left_03.gif border=0></td>\n"
	// +
	// "					<td background=/images_up/icon/icon_bg_03.gif class=btn4><A href=\"javascript:goodsURL('158247','018','174732','001','5');\" class=\"btn4\">보기</a></td>\n"
	// + "					<td><img src=/images_up/icon/icon_right_03.gif border=0></td>\n"
	// + "				</tr>\n"
	// + "				<tr><td height=3></td></tr>\n" + "			</table>\n" + "		</td>\n" +
	// "	</tr>\n"
	// + "	<tr><td bgcolor=#d6dbe1 height=1 colspan=11></td></tr>\n";
	// PageFetcher f = new PageFetcher("http://www.onbid.co.kr");
	// String page =
	// f.fetch("/frontup/portal/announce/control/announce/getAucSchedule.do");
	// page = page.substring(page.indexOf("<!-- 목록 반복 시작 -->"));
	// page = page.substring(0, page.indexOf("<!-- 목록반복 끝 -->"));
	// String[] chuncks = page.split("<\\!-- 목록 반복 시작 -->");
	//
	// 공매물건 last = null;
	//
	// List<공매물건> goodsList = new ArrayList<공매물건>();
	// for (String chunk : chuncks) {
	// chunk = chunk.replaceAll("<br>", " ");
	// chunk = chunk.replaceAll("&nbsp;", " ");
	// chunk = chunk.replaceAll("<font[^>]*>", "");
	// chunk = chunk.replaceAll("</font[^>]*>", "");
	// chunk = chunk.replaceAll("\\s+", " ");
	//
	// Pattern p = Pattern.compile("<tr [^>]+>\\s*" + //
	// "<td[^>]*>\\s*([^<]+)</td>" + // 자산구분
	// "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
	// "<td>\\s*([^<]*)</td>" + // 담당부점
	// "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
	// "<td[^>]*>\\s*([^<]*)</td>\\s*" + // 회차차수
	// "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
	// "<td[^>]*>\\s*([^<]*)</td>\\s*" + // 입찰기간
	// "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
	// "<td[^>]*>\\s*([^<]*)</td>\\s*" + // 개찰일시
	// "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
	//
	// "");
	//
	// Matcher m = p.matcher(chunk);
	// if (m.find() == false) {
	// continue;
	// }
	// String 자산구분 = m.group(1).trim();
	// String 담당부점 = m.group(2).trim();
	// String 회차차수 = m.group(3).trim();
	// String 입찰기간 = m.group(4).trim();
	// String 개찰일시 = m.group(5).trim();
	//
	// Pattern p2 =
	// Pattern.compile("javascript:goodsURL\\('([^']*)','([^']*)','([^']*)','([^']*)','([^']*)'");
	// Matcher m2 = p2.matcher(chunk);
	// m2.find();
	//
	// 공매물건 goods = new 공매물건();
	// goods.set자산구분(자산구분);
	// goods.set담당부점(담당부점);
	// goods.set회차차수(회차차수);
	// goods.set입찰기간(입찰기간);
	// goods.set개찰일시(개찰일시);
	// goods.setLinkInfo(m2.group(1), m2.group(2), m2.group(3), m2.group(4),
	// m2.group(5));
	// goods.copyFromLast(last);
	// last = goods;
	// goodsList.add(goods);
	// System.out.println(goods);
	// System.out.println(goods.getUrl());
	// }
	// }

	public void testPage2() throws HttpException, IOException {
		// PageFetcher f = new PageFetcher("http://www.onbid.co.kr");
		// String page =
		// f.fetch("/frontup/portal/announce/control/announce/getAnnounceAuctionView.do?ANNOUNCE_NO=153572&AUCTION_SQ=040&C_CODE=040&D_CODE=001&AUCTION_SUB_SQ=001&AUCTION_NO=169500&BUSINESS_TYPE=2&MENU_CODE=m0303000100");
		// page = page.substring(page.indexOf("<!-- 목록 반복 시작 -->"));
		// //// page = page.substring(0, page.indexOf("<!-- 목록반복 끝 -->"));
		// String[] chunks = page.split("<\\!-- 목록 반복 시작 -->");
		// String chunk = chunks[1];
		String chunk = "<!-- 물건비교 값 넘기기 -->\n"
				+ "   <tr align=center>\n"
				+ "	<td rowspan=3 width=30></td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1 rowspan=3></td>\n"
				+ "	<td rowspan=3  width=100 style=\"word-break:break-all\">토지/\n"
				+ "기타토지\n"
				+ "(2005-13909-004)</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1 rowspan=3></td>\n"
				+ "	<td align=left style=\"padding-left:10px\">\n"
				+ "		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
				+ "			<tr><td height=7></td></tr>\n"
				+ "			<tr><td class=\"b_green1\"> <a href=\"javascript:detailSearch('156094','169500','605904','2200513909004','공고중','','kamco','1');\"  class=\"b_green1\"><span  id=\"a0\"  onMouseOver=\"overColor('a0')\"  OnMouseOut=\"outColor('a0')\" style=\"color:#00a32d\">전북 익산시 낭산면 용기리 1352-6, 1352-12</span> <!--  --></a></td></tr>\n"
				+ "			<tr><td height=5></td></tr>\n"
				+ "			<tr><td>[물건관리번호] 2005-13909-004</td></tr>\n"
				+ "			<tr><td height=5></td></tr>\n"
				+ "			<tr><td>[소재지] 전북 익산시 낭산면 용기리 1352-6, 1352-12</td></tr>\n"
				+ "			<tr><td height=7></td></tr>\n"
				+ "		</table>\n"
				+ "	</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1></td>\n"
				+ "	<td width=80>매각/\n"
				+ "일반경쟁</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1></td>\n"
				+ "	<td width=110>2,485,750\n"
				+ "<b>2,238,000</b>\n"
				+ "(90%)</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1></td>\n"
				+ "	<td width=100>10/10/11 10:00 \n"
				+ "~\n"
				+ "10/10/13 17:00</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1></td>\n"
				+ "	<td width=80>공고중\n"
				+ "유찰0회</td>\n"
				+ "	<td bgcolor=#d6dbe1 width=1></td>\n"
				+ "	<td width=40>32</td>\n"
				+ "	</tr>\n"
				+ "\n"
				+ "	<tr><td bgcolor=#d6dbe1 height=1 colspan=13></td></tr>\n"
				+ "	<tr align=center bgcolor=#f8f8f8>\n"
				+ "		<td align=left style=\"padding-left:10px\" colspan=11>\n"
				+ "			<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
				+ "				<tr><td height=7></td></tr>\n"
				+ "				<tr><td style=\"padding-left:10px\">대지 54.<U>839</U>㎡  지분(총면적 367㎡), 전 107.<U>137</U>㎡  지분(총면적 717㎡)\n"
				+ "				</td></tr>\n" + "				<tr><td height=7></td></tr>\n" + "			</table>\n" + "		</td>\n" + "	</tr>\n"
				+ "\n" + "	<tr><td bgcolor=#d6dbe1 height=1 colspan=15></td></tr>\n";
		// chunk = chunk.replaceAll("<[bB][rR]>", "\n");
		// chunk = chunk.replaceAll("&nbsp;", " ");
		// chunk = chunk.replaceAll("<font[^>]*>", "");
		// chunk = chunk.replaceAll("</font[^>]*>", "");
		// // chunk = chunk.replaceAll("\\s+", " ");
		// chunk = chunk.replaceAll("(<img[^>]*>)", "");
		// chunk = chunk.replaceAll("(<input[^>]*>)", "");
		// // chunk = chunk.replaceAll("<td([^>]*)>", "");
		// System.out.println(chunk);

		String t = "	<td bgcolor=#d6dbe1 width=1 rowspan=3></td>\n"
				+ "	<td align=left style=\"padding-left:10px\">\n"
				+ "		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
				+ "			<tr><td height=7></td></tr>\n"
				+ "			<tr><td class=\"b_green1\"> <a href=\"javascript:detailSearch('156094','169500','605904','2200513909004','공고중','','kamco','1');\"  class=\"b_green1\"><span  id=\"a0\"  onMouseOver=\"overColor('a0')\"  OnMouseOut=\"outColor('a0')\" style=\"color:#00a32d\">전북 익산시 낭산면 용기리 1352-6, 1352-12</span> <!--  --></a></td></tr>\n"
				+ "			<tr><td height=5></td></tr>\n" + "			<tr><td>[물건관리번호] 2005-13909-004</td></tr>\n"
				+ "			<tr><td height=5></td></tr>\n" + "			<tr><td>[소재지] 전북 익산시 낭산면 용기리 1352-6, 1352-12</td></tr>\n"
				+ "			<tr><td height=7></td></tr>\n" + "		</table>\n" + "	</td>\n";

		Pattern p1 = Pattern.compile("<tr[^>]*>" + "\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
				"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
				"<td[^>]*>\\s*([^<]+)</td>" + // 용도, 입찰번호
				"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
				"\\s*<td[^>]*>\\s*<table[^>]*>\\s*" +

				"");
		Matcher m1 = p1.matcher(chunk);
		assertTrue(m1.find());
		assertEquals("토지/\n기타토지\n(2005-13909-004)", m1.group(1));

	}

	public void testFetch() throws HttpException, IOException {
		// 공매물건Fetcher f = new 공매물건Fetcher();
		// List<공매물건> list = f.fetch();
		// for (공매물건 공매물건 : list) {
		// System.out.println(공매물건);
		// }
	}

	public void testFetchDetailList() throws HttpException, IOException {
//		PageFetcher f = new PageFetcher("http://www.onbid.co.kr");
//		String page = f.fetch("/frontup/portal/announce/control/announce/getAnnounceAuctionView.do" + "?pageSize=10"
//				+ "&pageIndex=4" + "&orderName=" + "&orderType=DESC"
//				+
//				//
//				"&ANNOUNCE_NO=153503" + "&AUCTION_SQ=040" + "&C_CODE=040" + "&D_CODE=001" + "&AUCTION_SUB_SQ=001"
//				+ "&AUCTION_NO=169431" + "&BUSINESS_TYPE=2" + "&MENU_CODE=m0303000100");
//		// "http://www.onbid.co.kr" +
//		// "/frontup/portal/announce/control/announce/getAnnounceAuctionView.do"
//		// +
//		// "?pageSize=10" +
//		// "&pageIndex=4" +
//		// "&orderName=" +
//		// "&orderType=DESC" +
//		// "&ANNOUNCE_NO=153503" +
//		// "&AUCTION_SQ=040" +
//		// "&BUSINESS_TYPE=2" +
//		// "&AUCTION_SUB_SQ=001" +
//		// "&AUCTION_NO=169431" +
//		// "&C_CODE=40" +
//		// "&D_CODE=1" +
//		// "&GOODS_STATE=" +
//		// "&MENU_CODE=m0303000100" +
//		// "&FLAG=" +
//		// "&SEARCH_FIELD=" +
//		// "&SEARCH_TEXT="
//		// System.out.println(page);
//		page = page.substring(page.indexOf("<!-- 목록 반복 시작 -->") + 10);
//		Pattern totalPattern = Pattern.compile("Total : (\\d+)");
//		Matcher m = totalPattern.matcher(page);
//		assertTrue(m.find());
//		assertEquals("97", m.group(1));
//		while (true) {
//			공매물건 item = new 공매물건();
//			int pos = page.indexOf("<!-- 목록 반복 시작 -->");
//			if (pos == -1) {
//				pos = page.length() - 2;
//				page = page.substring(pos + 1);
//				break;
//			}
//			String chunk = page.substring(0, pos);
//			chunk = chunk.substring(chunk.indexOf("<tr"));
//			chunk = chunk.substring(chunk.indexOf("<td") + 1);
//			chunk = chunk.substring(chunk.indexOf("<td") + 1);
//			chunk = chunk.substring(chunk.indexOf("<td"));
//
//			chunk = chunk.replaceAll("<[bB][rR]>", "\n");
//			chunk = chunk.replaceAll("&nbsp;", " ");
//			chunk = chunk.replaceAll("<font[^>]*>", "");
//			chunk = chunk.replaceAll("<b>", "");
//			chunk = chunk.replaceAll("</b>", "");
//
//			chunk = chunk.replaceAll("</font[^>]*>", "");
//			chunk = chunk.replaceAll("(<img[^>]*>)", "");
//			chunk = chunk.replaceAll("(<input[^>]*>)", "");
//
//			Pattern infoPattern = Pattern.compile("<td[^>]+>([^/]+)/([^\\(]+)\\(([^\\)]+)\\)</td>");
//			Matcher m2 = infoPattern.matcher(chunk);
//			assertTrue(m2.find());
//			item.set상위용도(m2.group(1).trim());
//			item.set하위용도(m2.group(2).trim());
//			item.set입찰번호(m2.group(3).trim());
//
//			Pattern infoPattern2 = Pattern.compile("<span[^>]+>([^<]+)</span>");
//			Matcher m3 = infoPattern2.matcher(chunk);
//			m3.find();
//			item.set소재지(m3.group(1));
//
//			Pattern pattern = Pattern.compile("\\[물건관리번호\\]([^<]+)<");
//			Matcher matcher = pattern.matcher(chunk);
//			matcher.find();
//			item.set물건관리번호(matcher.group(1));
//
//			chunk = chunk.substring(matcher.end());
//			chunk = chunk.substring(chunk.indexOf("</table"));
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			matcher = Pattern.compile("<td[^>]+><td[^>]+>").matcher(chunk);
//			m.find();
//
//			matcher = Pattern.compile("<td[^>]+>([^/]+)/([^<]+)").matcher(chunk);
//			matcher.find();
//			item.set처분정보1(matcher.group(1).trim());
//			item.set처분정보2(matcher.group(2).trim());
//
//			chunk = chunk.substring(matcher.end());
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//
//			matcher = Pattern.compile("<td[^>]+>([^\\n]+)\\n([^\\n]+)\\n\\(([^\\)]+)").matcher(chunk);
//			matcher.find();
//			item.set감정가(matcher.group(1));
//			item.set최초예정가액(matcher.group(2));
//			item.set최저입찰가(matcher.group(3));
//			chunk = chunk.substring(matcher.end());
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//			chunk = chunk.substring(chunk.indexOf("</td>") + 1);
//
//			matcher = Pattern.compile("<td[^>]+>([^\\n]+)\\n([^<]+)").matcher(chunk);
//			matcher.find();
//			item.set물건상태(matcher.group(1).trim());
//			item.set유찰회수(matcher.group(2).trim());
//
//			chunk = chunk.substring(matcher.end());
//			chunk = chunk.substring(chunk.indexOf("<table") + 1);
//			chunk = chunk.substring(chunk.indexOf("<tr") + 1);
//			chunk = chunk.substring(chunk.indexOf("<tr") + 1);
//			matcher = Pattern.compile("<td[^>]+>([^<]+)").matcher(chunk);
//			matcher.find();
//			item.set내역(matcher.group(1).trim());
//			// System.out.println(item);
//			page = page.substring(pos + 1);
//			// System.out
//			// .println("##########################################################################################");
//		}

	}

	public void testScheduleFetcher() throws HttpException, IOException {
		공매일정Fetcher f = new 공매일정Fetcher();
		List<공매일정> res = f.fetch();
		공매물건Fetcher f2 = new 공매물건Fetcher();
		List<공매물건> res2 = f2.fetch(res.get(0), new FetcherCallback() {
			
			@Override
			public void setTotal(int total) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void progress(int progress) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void log(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		System.out.println(res2);
	}
}
