package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.ret.AddressTool.Entity;
import net.narusas.si.auction.fetchers.주소Builder;
import net.narusas.si.auction.model.단지;
import net.narusas.si.auction.model.단지평형;
import net.narusas.si.auction.model.단지평형시세추이;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.지역;
import net.narusas.si.auction.model.dao.단지Dao;
import net.narusas.si.auction.model.dao.단지평형Dao;
import net.narusas.si.auction.model.dao.지역Dao;

import org.apache.commons.httpclient.HttpException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RetApp {
	public static void main(String[] args) throws HttpException, IOException {
		App.context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
		단지Dao groupDao =  (단지Dao) App.context.getBean("단지DAO");
		단지평형Dao areaDao =  (단지평형Dao) App.context.getBean("단지평형DAO");
		지역Dao addrDao =  (지역Dao) App.context.getBean("지역DAO");
		주소Builder addrBuilder = new 주소Builder();
//		
		RetFetcher fetcher = new RetFetcher();
		AddressTool addrTool = new AddressTool(fetcher);
		AptGroupTool groupTool = new AptGroupTool(fetcher);
		for (Entity DO : AddressTool.doList) {
			List<Entity> citys = addrTool.getCityList(DO);
			for (Entity CITY : citys) {
				List<Entity> dongs = addrTool.getDongList(DO, CITY);
				for (Entity DONG : dongs) {
					주소 addr = addrBuilder.parse(DO.getName()+" "+CITY.getName()+" "+DONG.getName());
					
					List<단지> groups = groupTool.fetch(DO, CITY, DONG);
					for (단지 group : groups) {
						group.set지역_도(addr.get시도());
						group.set지역_시군구(addr.get시군구());
						group.set지역_동읍면(addr.get읍면동());
						
						groupTool.updateInfo(group);
						groupDao.saveOrUpdate(group);
						List<단지평형> 종류s = groupTool.fetch평형종류(group);
						groupTool.update단지평형(group, 종류s);
						for (단지평형 종류 : 종류s) {
							종류.set단지(group);
							System.out.println(종류);
							areaDao.saveOrUpdate(종류);
						}
						
					}
				}
			}
			
		}
//		Entity DO = AddressTool.doList[0];
		
		
		
		
		
		
		
		
//		종류.add시세추이(trends.get(0));
		
//		App.context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
//		단지Dao groupDao =  (단지Dao) App.context.getBean("단지DAO");
//		
//		
//		단지평형Dao areaDao =  (단지평형Dao) App.context.getBean("단지평형DAO");
		
	}
}
