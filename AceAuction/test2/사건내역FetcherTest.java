
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class 사건내역FetcherTest {

	@Test
	public void test() throws IOException {
		String html = (read("096_사건내역.html"));
		Pattern p = Pattern.compile("<th[^>]*>물건번호</th>\\s*<td[^>]*>\\s*(\\d+)&nbsp;\\s+", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		
		
	}
	
	static String read(String fileName) throws IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				"fixture2/" + fileName), "EUC-KR");
		char[] buf = new char[4096];
		StringBuffer res = new StringBuffer();
		while (true) {
			int r = reader.read(buf, 0, 4096);
			if (r == -1) {
				break;
			}
			res.append(buf, 0, r);
		}
		reader.close();
		return res.toString();
	}

}
