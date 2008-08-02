package net.narusas.aceauction.fetchers.motc;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.fetchers.PageFetcher;
import junit.framework.TestCase;

public class MOTCTest extends TestCase {
	// public void test1() throws HttpException, IOException {
	//
	// for (Sido sido : Sido.sidos) {
	//
	// String src =
	// "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=sido&fullAddress=&selSido="
	// + sido.getCode()
	// +
	// "&selSgg=%BC%B1%C5%C3&selUmd=%BC%B1%C5%C3&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn="
	// ;
	// PageFetcher fetcher = new PageFetcher(src);
	//
	// String page = fetcher.fetch("");
	// String chunk = page.substring(page.indexOf("name=\"selSgg\""));
	// chunk = chunk.substring(0, chunk.indexOf("</select"));
	// Pattern p = Pattern
	// .compile("<option value=\"(\\d+)\" >([^<]*)</option");
	//
	// Matcher m = p.matcher(chunk);
	// ArrayList<Sgg> sggList = new ArrayList<Sgg>();
	//
	// while (m.find()) {
	// Sgg sgg = new Sgg(m.group(1), m.group(2));
	// sggList.add(sgg);
	// }
	// System.out.println(sggList);
	// }
	// }

	// public void test2() throws HttpException, IOException {
	// String sidoCode = "11";
	// String sggCode = "680";
	//
	// String src =
	// "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=sgg&fullAddress=&selSido="
	// + sidoCode
	// + "&selSgg="
	// + sggCode
	// + "&selUmd=%BC%B1%C5%C3&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=";
	//
	// PageFetcher fetcher = new PageFetcher(src);
	// String page = fetcher.fetch("");
	//
	// System.out.println(page);
	// String chunk = page.substring(page.indexOf("name=\"selUmd\""));
	// chunk = chunk.substring(0, chunk.indexOf("</select"));
	// Pattern p = Pattern
	// .compile("<option value=\"([^\"]+)\" >([^<]*)</option");
	//
	// Matcher m = p.matcher(chunk);
	// ArrayList<Umd> umdList = new ArrayList<Umd>();
	//
	// while (m.find()) {
	// Umd umd = new Umd(m.group(1), m.group(2));
	// umdList.add(umd);
	// }
	// System.out.println(umdList);
	// }

	// public void test3() throws HttpException, IOException {
	//
	// // http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?
	// // pnu
	// // =&mode=&selGbn=umd&fullAddress=&selSido=44&selSgg=150&selUmd=360_%C0
	// // %C7%B4%E7%B8%E9&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=
	// String sidoCode = "44";
	// String sggCode = "150";
	// String umdCode = "360_�Ǵ��";
	//
	// String src =
	// "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=umd&fullAddress=&selSido="
	// + sidoCode
	// + "&selSgg="
	// + sggCode
	// + "&selUmd="
	// + URLEncoder.encode(umdCode, "euc-kr")
	// + "&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=";
	//
	// // http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?
	// // pnu
	// // =&mode=&selGbn=umd&fullAddress=&selSido=11&selSgg=680&selUmd=103_%B0
	// // %B3%C6%F7%B5%BF&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=
	// PageFetcher fetcher = new PageFetcher(src);
	// String page = fetcher.fetch("");
	//
	// System.out.println(page);
	// String chunk = page.substring(page.indexOf("name=\"selRi\""));
	// chunk = chunk.substring(0, chunk.indexOf("</select"));
	// Pattern p = Pattern
	// .compile("<option value=\"([^\"]+)\" >([^<]*)</option");
	//
	// Matcher m = p.matcher(chunk);
	// ArrayList<Ri> umdList = new ArrayList<Ri>();
	//
	// while (m.find()) {
	// Ri ri = new Ri(m.group(1), m.group(2));
	// umdList.add(ri);
	// }
	// System.out.println(umdList);
	// }

	public void test4() throws IOException, URISyntaxException {
		String sidoCode = "11";
		String sggCode = "350";
		String umdCode = "105_��赿";
		String riCode = "������";
		String landGubun = "1";
		String bobn = "136";
		String bubn = "1";

		String pnu = sidoCode + sggCode + "105" + "001" + fourLength(bobn)
				+ fourLength(bubn);
		String full = "�����+�����+��赿+136-1";
		// http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?
		// pnu
		// =&mode=&selGbn=umd&fullAddress=&selSido=11&selSgg=350&selUmd=105_%BB
		// %F3%B0%E8%B5%BF&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=

		// http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?
		// pnu
		// =1135010500101360001&mode=search&selGbn=umd&fullAddress=%B3%EB%BF%F8
		// %B1
		// %B8+%BB%F3%B0%E8%B5%BF+136-1&selSido=11&selSgg=350&selUmd=105_%BB%F3
		// %B0%E8%B5%BF&selRi=00&landGbn=1&bobn=136&bubn=1

		String src = "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu="
				+ pnu
				+ "&mode=search&selGbn=umd&fullAddress="
				+ URLEncoder.encode(full, "euc-kr")
				+ "&selSido="
				+ sidoCode
				+ "&selSgg="
				+ sggCode
				+ "&selUmd="
				+ URLEncoder.encode(umdCode, "euc-kr")
				+ "&selRi="
				+ URLEncoder.encode(riCode, "euc-kr")
				+ "&landGbn="
				+ landGubun
				+ "&bobn=" + bobn + "&bubn=" + bubn;
		//
		// PageFetcher fetcher = new PageFetcher(src);
		// String page = fetcher.fetch("");
		// System.out.println(page);//

		Desktop.getDesktop().browse(new URI(src));
	}

	private String fourLength(String bubn) {
		for (int i = bubn.length(); i < 4; i++) {
			bubn = "0" + bubn;
		}
		return bubn;
	}

	public void testRegx() {
		Pattern p = Pattern
				.compile("<option value=\"(\\d+)\" >([^<]*)</option");
		Matcher m = p.matcher("		<option value=\"440\" >������</option");
		assertTrue(m.find());
		assertEquals("440", m.group(1));
		assertEquals("������", m.group(2));

	}

}

class Sgg {
	String name;

	String code;

	public Sgg(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name + "(" + code + ")";
	}
}

class Umd extends Sgg {

	public Umd(String code, String name) {
		super(code, name);
	}

}

class Ri extends Sgg {

	public Ri(String code, String name) {
		super(code, name);
	}

}

class Sido {
	static ArrayList<Sido> sidos = new ArrayList<Sido>();
	String name;

	String code;

	public Sido(String name, String code) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name + "(" + code + ")";
	}

	static {

		sidos.add(new Sido("����Ư����", "11"));
		sidos.add(new Sido("�λ걤����", "26"));
		sidos.add(new Sido("�뱸������", "27"));
		sidos.add(new Sido("��õ������", "28"));
		sidos.add(new Sido("���ֱ�����", "29"));
		sidos.add(new Sido("����������", "30"));
		sidos.add(new Sido("��걤����", "31"));
		sidos.add(new Sido("��⵵", "41"));
		sidos.add(new Sido("������", "41"));
		sidos.add(new Sido("��û�ϵ�", "43"));
		sidos.add(new Sido("��û����", "44"));
		sidos.add(new Sido("����ϵ�", "45"));
		sidos.add(new Sido("���󳲵�", "46"));
		sidos.add(new Sido("���ϵ�", "47"));
		sidos.add(new Sido("��󳲵�", "48"));
		sidos.add(new Sido("����Ư����ġ��", "50"));

	}
}
