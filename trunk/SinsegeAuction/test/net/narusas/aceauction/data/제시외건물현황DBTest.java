package net.narusas.aceauction.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.data.builder.���ÿܰǹ���ȲBuilder;
import net.narusas.aceauction.model.���ÿܰǹ�;

public class ���ÿܰǹ���ȲDBTest extends TestCase {
	public void test1() throws Exception {
		List<���ÿܰǹ�> src =new LinkedList<���ÿܰǹ�>();
		src.add(new ���ÿܰǹ�(1, "���Ϸ���", "��������罽������������1������", "0.6��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "���Ϸ���", "��������罽������������1������", "1.3��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "�ٿ뵵��", "������������������1������", "1.8��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "â��", "������������������1������", "1.8��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "�ٿ뵵��", "����������������1������", "3.2��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "�ٿ뵵��", "����������������2������", "4.8��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "���ڴ�", "����������������2������", "3��","�Ÿ��� 652-82",1) );
		src.add(new ���ÿܰǹ�(1, "�ٿ뵵�ǰ� ȭ���", "���������������ؿ�ž����", "8.4��","�Ÿ��� 652-82",1) );
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection("jdbc:mysql://210.109.102.179/test?user=aceauction&password=0921");
		���ÿܰǹ���ȲBuilder db = new ���ÿܰǹ���ȲBuilder(3155, src);
		db.update();
	}
}
