package net.narusas.si.auction.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.주소Builder;
import net.narusas.si.auction.model.*;
import net.narusas.si.auction.model.dao.담당계DaoHibernate;
import net.narusas.si.auction.model.dao.당사자DaoHibernate;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.법원DaoHibernate;
import net.narusas.si.auction.model.dao.사건DaoHibernate;
import net.narusas.si.auction.model.dao.지역DaoHibernate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringTest extends TestCase {
	private ApplicationContext context;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
		App.context = context;
	}

	public void testFetch() {
		// (대지) 서울특별시 도봉구 창동 38 주공아파트 1709동 4층 409호
		// "(오피스텔) 경상북도 경주시 서부동 4-6"
		주소Builder b = new 주소Builder();
		주소 addr = b.parse("(기타) 경산시 진량읍 신상리 1194-1 토지, 1194-1 에이동 건물 및 1194-1 비이동, 시이동 건물");
		System.out.println(addr);
		System.out.println(addr.get시도());
		System.out.println(addr.get시군구());
		System.out.println(addr.get읍면동());
		System.out.println(addr.get번지이하());
		System.out.println(addr.toSlimAddress());
		
		// 법원 court = ((법원DaoHibernate) context.getBean("법원DAO")).get(210);
		// System.out.println(court);
		// //
		// 사건DaoHibernate dao = (사건DaoHibernate) context.getBean("사건DAO");
		// 사건 event = dao.find(court, 20070130032994L);
		// System.out.println(event);
		// System.out.println(event.get담당계());
		// System.out.println(event.get담당계Id());
		//		
		// System.out.println(dao.get(20080130009264L));

		// 담당계 charge = ((담당계DaoHibernate)
		// context.getBean("담당계DAO")).get(9035L);
		// System.out.println("Charge:" + charge.get매각기일().getTime());
		// Date date = new Date(2009 - 1900, 3 - 1, 3);
		// 담당계 charge = ((담당계DaoHibernate) context.getBean("담당계DAO")).get(court,
		// 1023, date);

		// System.out.println("Charge:" + charge);
		// System.out.println("Charge:" + charge.get매각기일());
		// 법원 court = ((법원DaoHibernate) context.getBean("법원DAO")).get(210);
		// System.out.println(court);
		// 담당계 charge = ((담당계DaoHibernate) context.getBean("담당계DAO")).get(1L);
		// System.out.println(charge);
		// 물건Dao dao = ((물건Dao) context.getBean("물건DAO"));
		// 물건 goods = dao.get(37811);
		// System.out.println(goods);
		// System.out.println(goods.get기일내역());
		// System.out.println(goods.get지역_도());
		// System.out.println(goods.get지역_시군구());
		// System.out.println(goods.get지역_동읍면());
		// System.out.println(goods.get물건종별());
		//	
		// 사건DaoHibernate dao = (사건DaoHibernate) context.getBean("사건DAO");
		//
		// List<사건> events = dao.findBy(charge);
		// 사건 event = events.get(0);x
		//
		// 당사자DaoHibernate dao2 = (당사자DaoHibernate) context.getBean("당사자DAO");
		// List<당사자> participants = dao2.findBy(event);
		// System.out.println(participants);

		// 지역DaoHibernate dao3 = (지역DaoHibernate) context.getBean("지역DAO");
		// 지역 area = dao3.get(2);
		// System.out.println(area);
		// System.out.println(area.get하위지역());
		// 지역 area2 = area.get하위지역().iterator().next();
		// System.out.println(area2);
		// System.out.println(area2.get하위지역());
		// String src = "서울특별시 구로구 개봉동 478 개봉한진아파트 상가1동 1층";
		// System.out.println(new 주소Builder().parse(src));

		// 물건Dao dao = ((물건Dao) context.getBean("물건DAO"));
		// 물건 물건 = dao.get(174291);
		//		
		// 등기부등본Dao dao2 = (등기부등본Dao) App.context.getBean("등기부등본DAO");
		// 등기부등본 attested = dao2.get(물건,"건물");
		// System.out.println(attested);
		// System.out.println(attested.get물건());
		// System.out.println(attested.getType());

	}

}
