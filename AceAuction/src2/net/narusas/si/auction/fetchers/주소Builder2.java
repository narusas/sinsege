package net.narusas.si.auction.fetchers;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.지역;

public class 주소Builder2 {
	public String[] parse소재지(String 소재지) {
		소재지 = 소재지.trim();
		if (소재지.startsWith("(")) {
			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
		}
		String[] tokens = 소재지.split(" ");

		String target시도 = tokens[0];
		String target시군구 = tokens[1];
		String target읍면동 = tokens[2];
		String target번지이하 = others(tokens, 3);
		if (target읍면동.endsWith("군") || target읍면동.endsWith("구")) {
			target시군구 = tokens[1] + " " + tokens[2];
			target읍면동 = tokens[3];
			target번지이하 = others(tokens, 4);
	 	}

		return new String[] { target시도, target시군구, target읍면동, target번지이하 };

	}

	private String others(String[] tokens, int i) {
		StringBuffer buf = new StringBuffer();
		for (; i < tokens.length; i++) {
			buf.append(tokens[i]).append(" ");
		}
		return buf.toString().trim();
	}

	@SuppressWarnings("rawtypes")
	public void update(물건 goods, 주소 addr, String 소재지) {
		System.out.println(소재지);
		String[] res = parse소재지(소재지);
		JdbcTemplate template = (JdbcTemplate) App.context.getBean("jdbcTemplate");
		지역 area1 = addr.get시도();
		지역 area2 = addr.get시군구();
//		지역 area3 = addr.get읍면동();
		System.out.println("SELECT * FROM aceauction.ac_area_road2 where name1 like '"+res[2]+"' and area1="+area1.getId()+" and area2="+area2.getId());
		List road2  = template.queryForList("SELECT * FROM aceauction.ac_area_road2 where name1 like '"+res[2]+"' and area1="+area1.getId()+" and area2="+area2.getId());
		if (road2 == null || road2.size() == 0) {
			System.out.println("SELECT * FROM aceauction.ac_area_road1 where name like '"+res[2]+"' and area1="+area1.getId()+" and area2="+area2.getId());
			List road1 = template.queryForList("SELECT * FROM aceauction.ac_area_road1 where name like '"+res[2]+"' and area1="+area1.getId()+" and area2="+area2.getId());
			if (road1 == null || road1.size()==0){
				System.out.println("##  신규 주소를 찾을수 없습니다.");
				return;
			}
			System.out.println(road1);
			Map map = (Map) road1.get(0);
			goods.setRoad1((Integer) map.get("id"));
				
			return;
		}
		Map map = (Map) road2.get(0);
		
		goods.setRoad1((Integer) map.get("road1"));
		goods.setRoad2((Integer) map.get("id"));
		
		
		
	}
}
