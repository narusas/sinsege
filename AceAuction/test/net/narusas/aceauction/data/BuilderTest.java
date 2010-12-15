package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import net.narusas.aceauction.data.builder.Builder;
import net.narusas.aceauction.model.법원;

import org.htmlparser.util.ParserException;

public class BuilderTest extends TestCase {
	public void test1() throws ParserException, InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException, TransformerException, IOException {
		Builder b = new Builder(법원.findByName("서울중앙지방법원"), null, null, null, null);
		// b.dbConnect();
		// b.clear담당계();
		b.build();
	}

	public void test2() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// Class.forName("com.mysql.jdbc.Driver").newInstance();
		// Connection conn = DriverManager
		// .getConnection("jdbc:mysql://210.109.102.179/test?user=aceauction&password=0921");
		// Statement stmt = conn.createStatement();
		// ResultSet rs = stmt.executeQuery("SELECT * FROM test.ac_charge a
		// WHERE charge_code=1008");
		// while(rs.next()) {
		// System.out.println(rs.getString("name"));
		// }
	}

}
