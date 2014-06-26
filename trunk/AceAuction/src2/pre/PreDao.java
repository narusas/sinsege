package pre;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.AddressBuilder;
import net.narusas.si.auction.fetchers.AddressBuilder.통합주소;
import net.narusas.si.auction.fetchers.주소Builder;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.주소;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class PreDao {
	JdbcTemplate template;
	net.narusas.si.auction.fetchers.주소Builder2 주소Builder2 = new net.narusas.si.auction.fetchers.주소Builder2();
	public PreDao() {
		try {
			App.context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
//			Class.forName("com.mysql.jdbc.Driver");
//			ComboPooledDataSource ds = new com.mchange.v2.c3p0.ComboPooledDataSource();
//			ds.setDriverClass("com.mysql.jdbc.Driver");
//			ds.setJdbcUrl("jdbc:mysql://58.230.118.122/aceauction?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
//			ds.setUser("acea");
//			ds.setPassword("aceone");
			
//		SingleConnectionDataSource ds = new SingleConnectionDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://58.230.118.122/aceauction?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull", "acea", "aceone", true);
//			template = new JdbcTemplate(ds);
			template = (JdbcTemplate) App.context.getBean("jdbcTemplate");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public void insert기본정보(사건기본정보 info, 사건 sagun) {
		
		
		
		template.update("insert into ac_pre_event "
				+ "      (jiwon,charge, type, 		sano, 			acceptDate, decisionDate, 	price, 	isStop, result,baedangDate,address, area1, 	area2, 	area3, 	road1) "
				+ "values(?,	?,		?,			?,				?,			?,				?,		?,		?,		?,			?,		?,		?,		?, 		?)",
				new Object[]{
						info.jiwon,
						sagun == null ? "" : sagun.담당계,
						info.사건명,
						info.saNo,
						info.접수일자,
						info.개시결정일자,
						info.청구금액,
						info.사건항고정지여부,
						info.종국결과,
						sagun == null ? "" : sagun.배당요구종기일,
						sagun == null ? "" : sagun.소재지,
						sagun == null ? "0" : (sagun.주소 == null ? "0":sagun.주소.get시도().getId()),
						sagun == null ? "0" : (sagun.주소 == null ? "0":sagun.주소.get시군구().getId()),
						sagun == null ? "0" : ((sagun.주소 == null ||sagun.주소.get읍면동()==null)  ? "0":sagun.주소.get읍면동().getId()),
						sagun.road1
				});
		
	}
	public void insert당사자(사건기본정보 info) {
		if (info.당사자목록 == null ||  info.당사자목록.size() ==0){
			return;
		}
		for(당사자 person: info.당사자목록){
			template.update("insert into ac_pre_event_person (jiwon, sano, type, name) values (?,?,?,?)", 
					new Object[]{info.jiwon,info.saNo, person.구분, person.이름});	
		}
		
		
	}
	public boolean isExists기본정보(사건기본정보 info) {
		String jiwon = info.jiwon;
		String saNo = info.saNo;
		long count = template.queryForLong("select count(*) from ac_pre_event where jiwon=? and sano=?", new Object[]{jiwon, saNo});
		return count > 0;
	}
	public void analysis(사건 sagun) {
		if (  sagun == null){
			return;
		}
		
		
		sagun.주소 =new 주소Builder().parse(sagun.소재지);
		
		System.out.println("##########  주소:");
		System.out.println( sagun.주소);
		통합주소 addr = new AddressBuilder().parse소재지(sagun.소재지);
		물건 goods = new  물건();
		주소Builder2.update(goods, sagun.주소, sagun.소재지);
		System.out.println("########## 통합주소:");
		System.out.println("####  물건 road1:"+goods.getRoad1());
		System.out.println(addr);
		sagun.통합주소 = addr;
		sagun.road1 = goods.getRoad1();
		if (addr.도로명그룹 == null || addr.도로명그룹id == null){
			return;
		}
		
		
				
	}
	
}
