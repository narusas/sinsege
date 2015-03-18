package pre;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.narusas.si.auction.fetchers.AddressBuilder.통합주소;
import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.fetchers.사건문건송달내역Fetcher;
import net.narusas.si.auction.model.문건처리내역;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.송달내역;
import net.narusas.si.auction.model.주소;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PreFetcher {
	HttpClient client = new HttpClient();
	HttpState state = new HttpState();

	public PreFetcher() {
		client.setState(state);
	}

	public List<담당계> fetch담당계(String 법원) {
		try {
			String url = "https://www.courtauction.go.kr/RetrieveJpDeptNonAllCodeList.ajax?query=" + URLEncoder.encode(법원, "UTF-8")
					+ "&tid=idJpDeptCode";

			PostMethod m = new PostMethod(url);

			m.setRequestHeader("Host", "www.courtauction.go.kr");
			m.setRequestHeader("Referer", "https://www.courtauction.go.kr/InitMulSrch.laf");
			client.executeMethod(m);
			ArrayList<담당계> list = new ArrayList<담당계>();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(new ByteArrayInputStream(m.getResponseBody()));
			NodeList root = doc.getChildNodes();
			Node xsync = root.item(0);
			NodeList select = xsync.getChildNodes().item(1).getChildNodes();

			for (int i = 0; i < select.getLength(); i++) {
				Node child = select.item(i);
				if ("option".equals(child.getNodeName())) {
					담당계 _담당계 = new 담당계();
					_담당계.id = child.getAttributes().getNamedItem("value").getTextContent();
					_담당계.name = child.getTextContent().trim();
					list.add(_담당계);
				}
			}
			return list;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (HttpException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	public List<사건> fetch사건목록(String jiwon, String chargeCode, String chargeName) throws Exception {// "&_SRCH_SRNID=PNO101005"
		System.out.println("## Fetch :" + jiwon + " " + chargeCode);
		// https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=1001
		// https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm=%EC%84%9C%EC%9A%B8%EC%A4%91%EC%95%99%EC%A7%80%EB%B0%A9%EB%B2%95%EC%9B%90&jpDeptCd=1001

		String url = "https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm="
				+ URLEncoder.encode(jiwon, "EUC-KR") + "&jpDeptCd=" + chargeCode;

		// https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=1001&saYear=2014
		System.out.println(url);
		PostMethod m = new PostMethod(url);

		m.setRequestHeader("Host", "www.courtauction.go.kr");
		m.setRequestHeader("Referer", "https://www.courtauction.go.kr/InitMulSrch.laf");
		client.executeMethod(m);

		String text = m.getResponseBodyAsString();
		// System.out.println(text);
		List<사건> sagunList = parse(chargeName, text);

		return sagunList;
	}

	List<사건> parse(String chargeName, String text) {
		List<사건> sagunList = new ArrayList<사건>();
		if (text.contains("<h3>배당요구종기공고</h3>") == false) {
			return sagunList;
		}
		String chunk = text.substring(text.indexOf("<h3>배당요구종기공고</h3>"));
		chunk = text.substring(text.indexOf("<tbody>") + 7);
		chunk = chunk.substring(0, chunk.indexOf("</table>"));
		Pattern p1 = Pattern.compile("<a ([^>]+)>", Pattern.MULTILINE);
		Matcher m1 = p1.matcher(chunk);

		while (m1.find()) {
			String temp = m1.group(1);
			if (temp.contains("showSaDetail") == false) {
				continue;
			}
			temp = temp.replaceAll("\\s+", " ");

			Pattern p2 = Pattern.compile("\\('[^']+',\\s*'([^']+)'\\s*,\\s*'([^']+)'\\s*,\\s*'([^']+)'\\s*,\\s*(\\d+)", Pattern.MULTILINE
					| Pattern.DOTALL);
			Matcher m2 = p2.matcher(temp);
			m2.find();

			사건 e = new 사건();
			e.법원 = m2.group(1);
			e.담당계 = chargeName;
			e.eventNo = m2.group(2);
			e.no1 = m2.group(3);
			e.no2 = m2.group(4);
			{
				int start =  m1.start();
				int end = chunk.indexOf("</tr", start);
				String ttt = chunk.substring(start, end);
				List<String> tdContentsList = HTMLUtils.findTDs(ttt);
				e.소재지 = tdContentsList.get(0).replaceAll("\n","").replaceAll("\\s+"," ");
			}
			{
				int start = chunk.indexOf("<tr", m1.start());
				int end = chunk.indexOf("</tr", start);
				String ttt = chunk.substring(start, end);
				List<String> tdContentsList = HTMLUtils.findTDs(ttt);
				e.배당요구종기일 = tdContentsList.get(2);
			}
			sagunList.add(e);
		}
		return sagunList;
	}

	public 사건기본정보 fetch사건기본정보(String jiwon, String saNo) throws Exception {
		System.out.println("################## jiwon:"+jiwon);
		String url = "https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?"
				+ "jiwonNm="+URLEncoder.encode(jiwon, "EUC-KR")
//				+ "jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8"
				// + "&saNo=20140130004508"
				+ "&saNo=" + saNo + "&srnID=PNO101005" + "&_SRCH_SRNID=PNO101005"
		// 
		//
		// + "&srnID=PNO101005"
		// + "&_SRCH_SRNID=PNO101005"
		;
		// https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?
		// jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8
		// &saNo=20140130004508
		// &srnID=PNO101005
		// &_SRCH_SRNID=PNO101005

		System.out.println(url);
		PostMethod m = new PostMethod(url);
		m.setRequestHeader("Host", "www.courtauction.go.kr");
		m.setRequestHeader("Referer", "https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf");
		client.executeMethod(m);
		String text = m.getResponseBodyAsString();
		System.out.println("########################################################");
		
		//201401300022460
		//https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&saNo=20140130002246&srnID=PNO101005&_SRCH_SRNID=PNO101005&ordGyuljGbncd=&bdygjgiDay=&_C_jiwonNm=%BC%AD%BF%EF%B5%BF%BA%CE%C1%F6%B9%E6%B9%FD%BF%F8
		
		
		//https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?jiwonNm=%BC%AD%BF%EF%B5%BF%BA%CE%C1%F6%B9%E6%B9%FD%BF%F8&_SRCH_SRNID=PNO101005&saNo=20140130002246&srnID=PNO101005
		//https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&_SRCH_SRNID=PNO101005&saNo=20140130002246&srnID=PNO101005

		
		
		//RetrieveRealEstDetailInqSaList.laf
		//jiwonNm	%BC%AD%BF%EF%B5%BF%BA%CE%C1%F6%B9%E6%B9%FD%BF%F8
		//saNo	20140130002246
		//ordGyuljGbncd	
		//bdygjgiDay	
		//srnID	PNO101005
		//_NAVI_CMD	InitMulSrch.laf
		//_NAVI_SRNID	PNO101005
		
		//saNo=20140130002246
		//
		//jiwonNm	%BC%AD%BF%EF%B5%BF%BA%CE%C1%F6%B9%E6%B9%FD%BF%F8
//		System.out.println(text);
		Thread.sleep(1000l);
		if (text.indexOf("table_title") == -1) {
			return null;
		}
//		System.out.println("##");
		String chunk = text.substring(text.indexOf("table_title"));
		chunk = chunk.substring(0, chunk.indexOf("footer_container"));
		사건기본정보 info = new 사건기본정보();
		info.jiwon = jiwon;
		info.saNo = saNo;
		info.사건번호 = HTMLUtils.findTHAndNextValue(chunk, "사건번호");
		info.사건명 = HTMLUtils.findTHAndNextValue(chunk, "사건명");
		info.접수일자 = HTMLUtils.findTHAndNextValue(chunk, "접수일자");
		info.개시결정일자 = HTMLUtils.findTHAndNextValue(chunk, "개시결정일자");
		info.청구금액 = HTMLUtils.findTHAndNextValue(chunk, "청구금액");
		info.사건항고정지여부 = HTMLUtils.findTHAndNextValue(chunk, "사건항고/정지여부");
		info.종국결과 = HTMLUtils.findTHAndNextValue(chunk, "종국결과");

		chunk = chunk.substring(chunk.indexOf("<caption>당사자내역</caption>"));
		chunk = chunk.substring(chunk.indexOf("<tbody>"));
		chunk = chunk.substring(0, chunk.indexOf("</tbody>"));

		// System.out.println(chunk);
		Pattern p = Pattern.compile("<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>\\s*<td>([^<]*)</td>",
				Pattern.MULTILINE);
		Matcher mm = p.matcher(chunk);
		List<당사자> list = new ArrayList<당사자>();
		while (mm.find()) {
			당사자 당사자 = new 당사자();
			당사자.구분 = mm.group(1).trim();
			당사자.이름 = mm.group(2).trim();
			if (당사자.isValid()) {
				list.add(당사자);
			}
			당사자 = new 당사자();
			당사자.구분 = mm.group(4).trim();
			당사자.이름 = mm.group(5).trim();
			if (당사자.isValid()) {
				list.add(당사자);
			}
		}
		// System.out.println(list);
		info.당사자목록 = list;
		
//		Pre사건문건송달내역Fetcher  f = new Pre사건문건송달내역Fetcher();
//		f.download(info);
//		
		 
		return info;
	}

}

class 담당계 {
	String id;
	String name;

	@Override
	public String toString() {
		return "담당계 [id=" + id + ", name=" + name + "]";
	}

}

class 사건 {
	String 법원;
	public String 담당계;
	String eventNo;
	String no1;
	String no2;
	public String 소재지;
	public String 배당요구종기일;
	public 주소 주소;
	public 통합주소 통합주소;
	public Long road1;
	@Override
	public String toString() {
		return "사건 [법원=" + 법원 + ", 담당계=" + 담당계 + ", eventNo=" + eventNo + ", no1=" + no1 + ", no2=" + no2 + ", 주소=" + 소재지 + ", 배당요구종기일="
				+ 배당요구종기일 + "]";
	}

}

class 사건기본정보 {
	사건 사건;
	public String saNo;
	public String jiwon;
	public String 사건번호;
	public String 사건명;
	public String 접수일자;
	public String 개시결정일자;
	public String 청구금액;
	public String 사건항고정지여부;
	public String 종국결과;
	public List<당사자> 당사자목록;
	public List<송달내역>   송달내역;
	public List<문건처리내역>  문건처리내역;
	

	@Override
	public String toString() {
		return "사건기본정보 [사건번호=" + 사건번호 + ", 사건명=" + 사건명 + ", 접수일자=" + 접수일자 + ", 개시결정일자=" + 개시결정일자 + ", 청구금액=" + 청구금액 + ", 사건항고정지여부="
				+ 사건항고정지여부 + ", 종국결과=" + 종국결과 + ", 당사자목록=" + 당사자목록 + "]";
	}

	public String getPath() {
		return HTMLUtils.encodeUrl(jiwon)+"/"+HTMLUtils.encodeUrl(saNo);
	}

	
}

class 당사자 {
	String 구분;
	String 이름;

	public boolean isValid() {
		return StringUtils.isNotEmpty(구분) && StringUtils.isNotEmpty(이름);
	}

	@Override
	public String toString() {
		return "당사자 [구분=" + 구분 + ", 이름=" + 이름 + "]";
	}

}