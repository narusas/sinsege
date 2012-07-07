package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.HttpException;

public class 공매일정Fetcher {
	List<공매일정> fetch() throws HttpException, IOException {
		PageFetcher f = new PageFetcher("http://www.onbid.co.kr");
		String page = f.fetch("/frontup/portal/announce/control/announce/getAucSchedule.do");
		page = page.substring(page.indexOf("<!-- 목록 반복 시작 -->"));
		page = page.substring(0, page.indexOf("<!-- 목록반복 끝 -->"));
		String[] chuncks = page.split("<\\!-- 목록 반복 시작 -->");

		공매일정 last = null;

		List<공매일정> goodsList = new ArrayList<공매일정>();
		for (String chunk : chuncks) {
			chunk = chunk.replaceAll("<br>", " ");
			chunk = chunk.replaceAll("&nbsp;", " ");
			chunk = chunk.replaceAll("<font[^>]*>", "");
			chunk = chunk.replaceAll("</font[^>]*>", "");
			chunk = chunk.replaceAll("\\s+", " ");

			Pattern p = Pattern.compile("<tr [^>]+>\\s*" + //
					"<td[^>]*>\\s*([^<]+)</td>" + // 자산구분
					"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
					"<td>\\s*([^<]*)</td>" + // 담당부점
					"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
					"<td[^>]*>\\s*([^<]*)</td>\\s*" + // 회차차수
					"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
					"<td[^>]*>\\s*([^<]*)</td>\\s*" + // 입찰기간
					"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄
					"<td[^>]*>\\s*([^<]*)</td>\\s*" + // 개찰일시
					"\\s*<td[^>]*>\\s*</td>\\s*" + // 줄

					"");

			Matcher m = p.matcher(chunk);
			if (m.find() == false) {
				continue;
			}
			String 자산구분 = m.group(1).trim();
			String 담당부점 = m.group(2).trim();
			String 회차차수 = m.group(3).trim();
			String 입찰기간 = m.group(4).trim();
			String 개찰일시 = m.group(5).trim();

			Pattern p2 = Pattern.compile("javascript:goodsURL\\('([^']*)','([^']*)','([^']*)','([^']*)','([^']*)'");
			Matcher m2 = p2.matcher(chunk);
			m2.find();

			공매일정 goods = new 공매일정();
			goods.set자산구분(자산구분);
			goods.set담당부점(담당부점);
			goods.set회차차수(회차차수);
			goods.set입찰기간(입찰기간);
			goods.set개찰일시(개찰일시);
			goods.setLinkInfo(m2.group(1), m2.group(2), m2.group(3), m2.group(4), m2.group(5));
			goods.copyFromLast(last);
			last = goods;
			goodsList.add(goods);
			System.out.println(goods);
			System.out.println(goods.getUrl());
		}
		return goodsList;
	}
}
