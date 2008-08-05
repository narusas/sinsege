package net.narusas.aceauction.fetchers.motc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.fetchers.PageFetcher;

public class MOCTFetcher {
	public List<Sgg> fetch시군구(Sido sido) throws HttpException, IOException {
		String src = "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=sido&fullAddress=&selSido="
				+ sido.getCode()
				+ "&selSgg=%BC%B1%C5%C3&selUmd=%BC%B1%C5%C3&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=";
		PageFetcher fetcher = new PageFetcher(src);

		String page = fetcher.fetch("");
		String chunk = page.substring(page.indexOf("name=\"selSgg\""));
		chunk = chunk.substring(0, chunk.indexOf("</select"));
		Pattern p = Pattern
				.compile("<option value=\"(\\d+)\" >([^<]*)</option");

		Matcher m = p.matcher(chunk);
		ArrayList<Sgg> sggList = new ArrayList<Sgg>();

		while (m.find()) {
			Sgg sgg = new Sgg(m.group(1), m.group(2));
			sggList.add(sgg);
		}
		return sggList;

	}

	public List<Umd> fetch읍면동(Sido sido, Sgg sgg) throws HttpException,
			IOException {
		String sidoCode = sido.getCode();
		String sggCode = sgg.getCode();

		String src = "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=sgg&fullAddress=&selSido="
				+ sidoCode
				+ "&selSgg="
				+ sggCode
				+ "&selUmd=%BC%B1%C5%C3&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=";

		PageFetcher fetcher = new PageFetcher(src);
		String page = fetcher.fetch("");

		System.out.println(page);
		String chunk = page.substring(page.indexOf("name=\"selUmd\""));
		chunk = chunk.substring(0, chunk.indexOf("</select"));
		Pattern p = Pattern
				.compile("<option value=\"([^\"]+)\" >([^<]*)</option");

		Matcher m = p.matcher(chunk);
		ArrayList<Umd> umdList = new ArrayList<Umd>();

		while (m.find()) {
			Umd umd = new Umd(m.group(1), m.group(2));
			umdList.add(umd);
		}
		return umdList;
	}

	public List<Ri> fetch리(Sido sido, Sgg sgg, Umd umd) throws HttpException,
			IOException {
		String sidoCode = sido.getCode();
		String sggCode = sgg.getCode();
		String umdCode = umd.getCode();

		String src = "http://luris.moct.go.kr/web/actreg/arservice/ArLandUsePrintFrame.jsp?pnu=&mode=&selGbn=umd&fullAddress=&selSido="
				+ sidoCode
				+ "&selSgg="
				+ sggCode
				+ "&selUmd="
				+ URLEncoder.encode(umdCode, "euc-kr")
				+ "&selRi=%BC%B1%C5%C3&landGbn=1&bobn=&bubn=";

		PageFetcher fetcher = new PageFetcher(src);
		String page = fetcher.fetch("");

		System.out.println(page);
		String chunk = page.substring(page.indexOf("name=\"selRi\""));
		chunk = chunk.substring(0, chunk.indexOf("</select"));
		Pattern p = Pattern
				.compile("<option value=\"([^\"]+)\" >([^<]*)</option");

		Matcher m = p.matcher(chunk);
		ArrayList<Ri> riList = new ArrayList<Ri>();

		while (m.find()) {
			Ri ri = new Ri(m.group(1), m.group(2));
			riList.add(ri);
		}

		return riList;
	}

	public String createURL(Sido sido, Sgg sgg, Umd umd, Ri ri,
			boolean isMountain, String bobn, String bubn)
			throws UnsupportedEncodingException {
		String sidoCode = sido.getCode();
		String sggCode = sgg.getCode();
		String umdCode = umd.getCode();
		String riCode = ri.getCode();
		String landGubun = isMountain ? "2" : "1";

		String pnu = createPNU(bobn, bubn, sidoCode, sggCode, umdCode, riCode);
		String full = createFullAddress(sido, sgg, umd, ri, bobn, bubn);

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
		return src;
	}

	private String createFullAddress(Sido sido, Sgg sgg, Umd umd, Ri ri,
			String bobn, String bubn) {
		if (ri != null) {
			return sido.getName() + "+" + sgg.getName() + "+" + "+"
					+ umd.getName() + "+" + ri.getName() + "+" + bobn + "-"
					+ bubn;
		} else {
			return sido.getName() + "+" + sgg.getName() + "+" + "+"
					+ umd.getName() + "+" + bobn + "-" + bubn;
		}

	}

	private String createPNU(String bobn, String bubn, String sidoCode,
			String sggCode, String umdCode, String riCode) {
		return sidoCode + sggCode + splitNo(umdCode) + threeLength(riCode)
				+ fourLength(bobn) + fourLength(bubn);
	}

	private String splitNo(String umdCode) {
		return umdCode.split("_")[0];
	}

	private String fourLength(String bubn) {
		for (int i = bubn.length(); i < 4; i++) {
			bubn = "0" + bubn;
		}
		return bubn;
	}

	private String threeLength(String bubn) {
		for (int i = bubn.length(); i < 3; i++) {
			bubn = "0" + bubn;
		}
		return bubn;
	}

}
