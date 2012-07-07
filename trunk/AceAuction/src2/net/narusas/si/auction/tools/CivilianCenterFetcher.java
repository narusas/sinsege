package net.narusas.si.auction.tools;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.HttpException;

public class CivilianCenterFetcher {
	public static void main(String[] args) {
		try {
			int count = 0;
			for (int pageIndex = 1; pageIndex <= 500; pageIndex++) {

				// http://map.naver.com/local/search.nhn?query=주민센터&queryRank=1&type=SITE_1&siteOrder=1949473541&page=3
				PageFetcher f = new PageFetcher("http://map.naver.com/local/search.nhn?query="
						+ URLEncoder.encode("주민센터", "utf-8") + "&queryRank=1&type=SITE_1&siteOrder=1949473541&page="
						+ pageIndex);
				f.setEncoding("utf-8");
				String page = f.fetch("");
				String[] chunks = page.split("enter_detail");
				for (int i = 1; i < chunks.length; i++) {
					String name = null;
					String homepage = null;
					String address = null;
					String tel = null;
					String chunk = chunks[i];

					String txt = chunk.substring(chunk.indexOf("title_text"));
					{
						Pattern p = Pattern.compile("<a[^>]+>(.+)</a>");
						Matcher m = p.matcher(txt);
						m.find();
						name = m.group(1);
						chunk = txt.substring(m.end());
					}

					{
						Pattern p2 = Pattern.compile("<a href=\"([^\"]+)");
						Matcher m2 = p2.matcher(chunk);
						m2.find();
						homepage = m2.group(1);

					}

					{
						Pattern p3 = Pattern.compile("무료통화\\(KT제공\\)\">(.*)</a>");
						Matcher m3 = p3.matcher(chunk);
						if (m3.find()) {
							tel = m3.group(1);
						}
					}
					{
						Pattern p3 = Pattern.compile("<span class=\"meta_address\">(.*)</span>");
						Matcher m3 = p3.matcher(chunk);
						if (m3.find()) {
							address = m3.group(1);
						}
					}
					if (tel == null || address == null){
						continue;
					}
					System.out.println( "INSERT INTO ac_center ( name, homepage, tel, address) VALUES (\""+name + "\",\"" + homepage + "\",\"" + tel + "\",\"" + address+"\")");
				}
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
