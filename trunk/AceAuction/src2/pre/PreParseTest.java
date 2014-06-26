package pre;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.util.lang.NFile;

import org.junit.Test;

public class PreParseTest {

	@Test
	public void test() throws Exception {
		String text = NFile.getText(new File("fixture2/pre001.html"), "UTF-8");
		PreFetcher f= new PreFetcher();
		List<사건> list = f.parse("담당계1",    text);
		for (사건 사건 : list) {
			System.out.println(사건);
		}
		
		
//		String chunk = text.substring(text.indexOf("<h3>배당요구종기공고</h3>"));
//		chunk = text.substring(text.indexOf("<tbody>")+7);
//		chunk = chunk.substring(0, chunk.indexOf("</table>"));
////		System.out.println(chunk);
//		
//		Pattern p1 = Pattern.compile("<a ([^>]+)>", Pattern.MULTILINE);
//		Matcher m1 = p1.matcher(chunk);
////		System.out.println(chunk);
//		while(m1.find()){
//			String temp = m1.group(1);
//			if (temp.contains("showSaDetail") == false){
//				continue;
//			}
////			System.out.println("########");
//			temp = temp.replaceAll("\\s+"," ");
////			System.out.println(temp);
////			char[] ch = temp.toCharArray();
////			for (char c : ch) {
////				System.out.println(c+":"+(int)c);
////			}
//			
//			
//			
//			Pattern p2 = Pattern.compile("\\('[^']+',\\s*'([^']+)'\\s*,\\s*'([^']+)'\\s*,\\s*'([^']+)'\\s*,\\s*(\\d+)", Pattern.MULTILINE|Pattern.DOTALL);
//			Matcher m2 = p2.matcher(temp);
//			m2.find();
//			System.out.println(m2.group(1));
//			System.out.println(m2.group(2));
//			System.out.println(m2.group(3));
//			System.out.println(m2.group(4));
//			int start = chunk.indexOf("<tr",m1.start());
//			int end = chunk.indexOf("</tr",start);
//			String ttt = chunk.substring(start, end);
////			System.out.println(ttt);
//			List<String> tdContentsList = HTMLUtils.findTDs(ttt);
//			System.out.println(tdContentsList);
//			
//		}
		
	}
	
	@Test
	public void test2() throws Exception {
		String text = NFile.getText(new File("fixture2/pre002.html"), "UTF-8");
		String chunk = text.substring(text.indexOf("table_title"));
		chunk = chunk.substring(0, chunk.indexOf("footer_container"));
		사건기본정보 info = new 사건기본정보();
		info.사건번호 = HTMLUtils.findTHAndNextValue(chunk, "사건번호");
		info.사건명 = HTMLUtils.findTHAndNextValue(chunk, "사건명");
		info.접수일자 = HTMLUtils.findTHAndNextValue(chunk, "접수일자");
		info.개시결정일자 = HTMLUtils.findTHAndNextValue(chunk, "개시결정일자");
		info.청구금액 = HTMLUtils.findTHAndNextValue(chunk, "청구금액");
		info.사건항고정지여부 = HTMLUtils.findTHAndNextValue(chunk, "사건항고/정지여부");
		info.종국결과 = HTMLUtils.findTHAndNextValue(chunk, "종국결과");
		System.out.println( info);
		
		chunk = chunk.substring(chunk.indexOf("<caption>당사자내역</caption>"));
		chunk = chunk.substring(chunk.indexOf("<tbody>"));
		chunk = chunk.substring(0, chunk.indexOf("</tbody>"));
		
		
		System.out.println(chunk);
		Pattern p = Pattern.compile("<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>", Pattern.MULTILINE);
		Matcher m = p.matcher(chunk);
		List<당사자> list = new ArrayList< 당사자>();
		while(m.find()){
			당사자 당사자 =  new 당사자();
			당사자.구분 = m.group(1).trim();
			당사자.이름 = m.group(2).trim();
			if (당사자.isValid()){
				list.add(당사자);
			}
			당사자 =  new 당사자();
			당사자.구분 = m.group(4).trim();
			당사자.이름 = m.group(5).trim();
			if (당사자.isValid()){
				list.add(당사자);
			}
		}
		System.out.println(list);
	}
}
