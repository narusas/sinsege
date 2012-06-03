package net.narusas.si.auction.fetchers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import biz.evot.util.lang.NInputStream;

public class 스피드옥션물건상세내용FetcherTest {

	@Test
	public void test() throws IOException {
		String raw = NInputStream.getText(getClass().getResourceAsStream("/net/narusas/si/auction/fetchers/fixture001.txt"));
		String chunk = raw.substring(raw.indexOf("<h3>임대차관계조사서</h3>"));
		chunk = chunk.substring(chunk.indexOf("<h4>2. 기타 </h4>")+"<h4>2. 기타 </h4>".length());
		chunk = chunk.replaceAll("<[^>]+>", "");
		chunk = chunk.replaceAll("\n\n", "\n");
		chunk = chunk.replaceAll("\n\n", "\n");
		chunk = chunk.replaceAll("\n\n", "\n");
		chunk = chunk.replaceAll("\n\n", "\n");
		String[] lines = chunk.split("\n");
		for (int i=0;i<lines.length;i++) {
			lines[i]  = lines[i].trim();
			
		}
		chunk  = StringUtils.join(lines,"\n").trim();
		System.out.println(chunk);
//		사건현황조사서Fetcher f = new 사건현황조사서Fetcher();
//		f.parse임대차관계내역(raw);
	}

}
