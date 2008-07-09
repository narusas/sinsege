package net.narusas.aceauction.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.data.builder.제시외건물현황Builder;
import net.narusas.aceauction.model.제시외건물;

public class 제시외건물현황DBTest extends TestCase {
	public void test1() throws Exception {
		List<제시외건물> src =new LinkedList<제시외건물>();
		src.add(new 제시외건물(1, "보일러실", "샷시조경사슬래브지붕지하1층부합", "0.6㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "보일러실", "샷시조경사슬래브지붕지하1층부합", "1.3㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "다용도실", "샷시조샷시지붕지하1층부합", "1.8㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "창고", "샷시조샷시지붕지하1층부합", "1.8㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "다용도실", "샷시조슬래브지붕1층부합", "3.2㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "다용도실", "샷시조슬래브지붕2층부합", "4.8㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "발코니", "샷시조슬래브지붕2층부합", "3㎡","신림동 652-82",1) );
		src.add(new 제시외건물(1, "다용도실겸 화장실", "벽돌조슬래브지붕옥탑소재", "8.4㎡","신림동 652-82",1) );
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection("jdbc:mysql://210.109.102.179/test?user=aceauction&password=0921");
		제시외건물현황Builder db = new 제시외건물현황Builder(3155, src);
		db.update();
	}
}
