package net.narusas.si.auction.fetchers;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class 통합주소Converter {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		ComboPooledDataSource ds = new ComboPooledDataSource();//new com.mysql.jdbc.Driver(), "jdbc:mysql://58.230.118.122/aceauction?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull", "acea", "aceone"
		ds.setDriverClass("com.mysql.jdbc.Driver");
		ds.setJdbcUrl("jdbc:mysql://58.230.118.122/aceauction?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
		ds.setUser("acea");
		ds.setPassword("aceone");
		JdbcTemplate template = new JdbcTemplate(ds) ;
		boolean willGo = true;
		int progress = 0;
		while(willGo){
			List<Map> goodsList = template.queryForList("select * from aceauction.ac_goods where id >= ${startId} order by id asc limit "+progress+", 1000");
			
			
		}
	}
}
