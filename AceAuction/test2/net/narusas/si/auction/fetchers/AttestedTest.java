package net.narusas.si.auction.fetchers;

import java.util.LinkedList;

import junit.framework.TestCase;
import net.narusas.si.auction.app.App;
import net.narusas.si.auction.builder.등기부등본Builder;
import net.narusas.si.auction.fetchers.스피드옥션ZoomPageFetcher.등기부등본Links;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.dao.물건Dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AttestedTest extends TestCase {
	public void test1() throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
		App.context = context;

		// 사건Dao eventDao = (사건Dao) context.getBean("사건DAO");
		// 사건 event = eventDao.get(2L);
		//		
		// System.out.println("E:"+event);

		물건Dao dao = (물건Dao) context.getBean("물건DAO");
		물건 goods = dao.get(1);

		스피드옥션ZoomPageFetcher f = new 스피드옥션ZoomPageFetcher();
		등기부등본Links links = f.fetch(goods);
		System.out.println(links);
		System.out.println(links.get건물등기부등본PDFLink());
		System.out.println(links.get토지등기부등본PDFLink());
		등기부등본Builder b = new 등기부등본Builder(goods, links.get건물등기부등본PDFLink(), new LinkedList<String>(), "");
		b.execute();
	}
}
