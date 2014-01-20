package net.narusas.si.auction.fetchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.AddressBuilder.통합주소;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.지역;

public class 주소Builder2 {
	
	final Logger logger = LoggerFactory.getLogger("auction");
	
//	public 도로명주소 parse소재지(String 소재지, 주소 _주소) {
//		도로명주소 _도로명주소 = new 도로명주소();
//		통합주소 _통합주소= _주소.get통합주소();
//		_도로명주소.시도 =_통합주소.시도; 
//		_도로명주소.시군구 =_통합주소.시군구; 
//		_도로명주소..시도 =_통합주소.시도; 
//		_도로명주소.시도 =_통합주소.시도; 
////		소재지 = 소재지.trim();
////		if (소재지.startsWith("(")) {
////			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
////		}
////		String[] tokens = 소재지.split(" ");
////		
////		String target시도 = tokens[0];
////		addr.시도 = tokens[0];
////		
////		String target시군구 = tokens[1];
////		addr.시군구 = tokens[1];
////		
////		String target읍면동 = tokens[2];
////		addr.읍면동  = tokens[2];
////		
////		String target번지이하 = others(tokens, 3);
////		addr.번지이하 = others(tokens, 3);
////		if (target읍면동.endsWith("군") || target읍면동.endsWith("구")) {
////			addr.시군구 = tokens[1] + " " + tokens[2];
////			target시군구 = tokens[1] + " " + tokens[2];
////			target읍면동 = tokens[3];
////			addr.읍면동  = tokens[3];
////			String temp  = tokens[4];
////			
////			target번지이하 = others(tokens, 4);
////			addr.번지이하 = others(tokens, 4);
////		}
////		else {
////			String temp = tokens[3];
////			if ((temp.endsWith("로") || temp.endsWith("길"))) {
////				target번지이하 = others(tokens, 4);
////				addr.번지이하 = target번지이하;
////				
////				addr.도로명 = temp;
////				Pattern 신규주소명_길번호_규칙 = Pattern.compile("([\\d-]+),?.*");
////				Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
////				if (m1.find()) {
////					addr.is도로명주소 = true;
////					System.out.println("#here");
////					String 건물번호 = m1.group(1);
////					if (건물번호.contains("-")) {
////						String[] tokens2 = 건물번호.split("-");
////						addr.건물본번 = Integer.parseInt(tokens2[0].trim());
////						addr.건물부번 = Integer.parseInt(tokens2[1].trim());
////					} else {
////						addr.건물본번 = Integer.parseInt(건물번호);
////					}
////
////					// target읍면동 = target읍면동 + " " + 건물번호;
////					target번지이하 = target번지이하.substring(target번지이하.indexOf(건물번호) + 건물번호.length()).trim();
////					if (target번지이하.startsWith(",")) {
////						target번지이하 = target번지이하.substring(1).trim();
////					}
////				}
////			}
////
////		}
////		
////		
////		System.out.println("### "+target읍면동);
////		System.out.println("### "+target번지이하);
////
////		
////		if ((target읍면동.endsWith("로") || target읍면동.endsWith("길"))) {
////
////			addr.도로명 = target읍면동;
////			Pattern 신규주소명_길번호_규칙 = Pattern.compile("([\\d-]+),?.*");
////			Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
////			if (m1.find()) {
////				addr.is도로명주소 = true;
////				System.out.println("#here");
////				String 건물번호 = m1.group(1);
////				if (건물번호.contains("-")) {
////					String[] tokens2 = 건물번호.split("-");
////					addr.건물본번 = Integer.parseInt(tokens2[0].trim());
////					addr.건물부번 = Integer.parseInt(tokens2[1].trim());
////				} else {
////					addr.건물본번 = Integer.parseInt(건물번호);
////				}
////
////				// target읍면동 = target읍면동 + " " + 건물번호;
////				target번지이하 = target번지이하.substring(target번지이하.indexOf(건물번호) + 건물번호.length()).trim();
////				if (target번지이하.startsWith(",")) {
////					target번지이하 = target번지이하.substring(1).trim();
////				}
////			}
////
////		}
////	
////		return addr;
//
////		return new String[] { target시도, target시군구, target읍면동, target번지이하, type};
//
//	}
//	class  도로명주소 {
//		String   시도;
//		String  시군구;
//		String  읍면동;
//		String  도로명;
//		Integer 건물본번;
//		 Integer  건물부번;
//		 String  번지이하;
//		 boolean is도로명주소 =false;
//		@Override
//		public String toString() {
//			return "도로명주소 [시도=" + 시도 + ", 시군구=" + 시군구 + ", 읍면동=" + 읍면동 + ", 도로명=" + 도로명 + ", 건물본번=" + 건물본번 + ", 건물부번=" + 건물부번 + ", 번지이하="
//					+ 번지이하 + ", is도로명주소=" + is도로명주소 + "]";
//		}
//		 
//	}

	private String others(String[] tokens, int i) {
		StringBuffer buf = new StringBuffer();
		for (; i < tokens.length; i++) {
			buf.append(tokens[i]).append(" ");
		}
		return buf.toString().trim();
	}
	AddressBuilder 주소통합Builder =  new AddressBuilder();
	@SuppressWarnings("rawtypes")
	public void update(물건 goods, 주소 addr, String 소재지) {
		logger.info("도로명 분석: "+소재지);
		System.out.println("주소Builder2:"+소재지);
//		도로명주소 res = parse소재지(소재지, addr); // target시도, target시군구, target읍면동, target번지이하, type
		통합주소 res  = addr.get통합주소();//주소통합Builder.parse소재지(소재지);
		logger.info("도로명  결과: "+res);
		
		
		Integer road_area =  null;
		JdbcTemplate template = (JdbcTemplate) App.context.getBean("jdbcTemplate");
		{
//		if (goods.getRoad1() == null && res.도로명 == null){
			logger.info("ROAD1 갱신 작업을 시작합니다");
			String area3token = res.읍면;
			if (area3token == null){
				area3token = res.동리;
			}
			int area1 = addr.get시도().getId();
			
			int area2 = addr.get시군구() != null? addr.get시군구().getId() : addr.get읍면동().getId();
			if (area3token != null){
				
//			int area3 = addr.get읍면동().getId();
				String queryForRoad1 = "select * from aceauction.ac_area_road1 where area2="+area2+" and name='"+area3token+"'";
				System.out.println(queryForRoad1);
				List<Map> AREA = template.queryForList(queryForRoad1);
				if (AREA.size() ==0){
					logger.info(" 도로명 주소를 판별할수 없습니다. ");
					return;
//					if ((area3token.endsWith("동") == false && area3token.endsWith("가") == false&& area3token.endsWith("읍") == false&& area3token.endsWith("면") == false) == false)  {
//						String query = "insert into aceauction.ac_area_road1(area1, area2, name) values ("+area1+", "+area2+", '"+area3token+"')";
//						System.out.println(query);
//						template.update(query);
//						AREA = template.queryForList(queryForRoad1);
//					}
				}
				road_area = (Integer) AREA.get(0).get("id");
				goods.setRoad1(road_area.longValue());
				logger.info("이 물건의  ROAD1 은 "+ road_area+"입니다");
//			String query = "update aceauction.ac_goods set area_road_1="+road_area+" where id="+goods.getId();
//			System.out.println(query);
//			template.update(query);	
			}
//		}
	}
		
		
		if (res == null || res.도로명 == null){
			logger.info("도로명이 없는 주소 입니다. ");
			return;
		}
		
		System.out.println("##:"+res);
		
		지역 area1 = addr.get시도();
		지역 area2 = addr.get시군구() != null? addr.get시군구():addr.get읍면동();
//		지역 area3 = addr.get읍면동();
		//·
		
//		System.out.println("SELECT * FROM aceauction.ac_area_road2 where name1 like '"+res[2]+"' and area1="+area1.getId()+" and area2="+area2.getId());
		System.out.println("select id from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+res.도로명+"' ");
		List<Map> roadIdList = template.queryForList("select id from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+res.도로명+"'");
		if (roadIdList == null || roadIdList.size()==0){
			logger.info(" 도로명을 찾을수 없습니다.  select id  from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+res.도로명+"'");
			
			if (res.도로명.contains("번길") && res.도로명.contains("로")){
				String shot도로명 =res.도로명.substring(0, res.도로명.lastIndexOf("로")+1);
				System.out.println("select id from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+shot도로명+"' ");
				roadIdList = template.queryForList("select id from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+shot도로명+"'");
				if (roadIdList == null || roadIdList.size()==0){
					logger.info(" 도로명을 찾을수 없습니다.  select id  from aceauction.ac_road_road where area1="+area1.getId()+" and name='"+shot도로명+"'");
					return;
				
				}
			}
			else {
				return;
			}
		}
		for(Map idMap:  roadIdList){
			Long roadId  = (Long) idMap.get("id");
			Map map =  null; 
			if (res.건물주번 != null && res.건물부번 != null) {
				String query = "SELECT * FROM aceauction.ac_road_bld where road_no = "+roadId+" and bld_main="+res.건물주번+" and bld_sub="+res.건물부번 +" and area1="+area1.getId()+" and area2="+area2.getId();
				System.out.println(query);
				List<Map> bldList = template.queryForList(query);
				if(bldList != null && bldList.size()!=0){
					map = bldList.get(0);
				}
				else {
					
				}
//				SELECT * FROM aceauction.ac_road_bld where road_no = 116803122001 and bld_main=265;
			}
			else if (res.건물주번 != null && map == null){
				String query = "SELECT * FROM aceauction.ac_road_bld where road_no = "+roadId+" and bld_main="+res.건물주번 +" and area1="+area1.getId()+" and area2="+area2.getId();
				System.out.println(query);
				List<Map> bldList = template.queryForList(query);
				if(bldList != null && bldList.size()!=0){
					map = bldList.get(0);
				}
			}
			
			
			
			if (roadIdList.size()==1 && map== null){
				String query = "SELECT * FROM aceauction.ac_road_bld where road_no = "+roadId+"  and area1="+area1.getId()+" and area2="+area2.getId();
				System.out.println(query);
				List<Map> bldList = template.queryForList(query);
				if(bldList != null && bldList.size()!=0){
					map = bldList.get(0);
				}
			}
			if (map!= null && map.size()>0){
//				logger.info("도로명 주소:"+ map);
				if (road_area != null) {
					goods.setRoad1(road_area.longValue());
				}
				else {
					goods.setRoad1(Long.parseLong(""+map.get("road_area")));
				}
				goods.setRoad2(Long.parseLong(""+ map.get("road_no")));
				goods.setRoad3(new BigDecimal(""+ map.get("id")));
				logger.info("도로명 주소: road1:"+ goods.getRoad1()+" road2:"+goods.getRoad2()+" road3:"+goods.getRoad3());
				return;
			}
		}
		
		logger.info("도로명 주소를 찾을수 없습니다. ");
	}
}
