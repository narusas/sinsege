package net.narusas.si.auction.fetchers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.법원;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 경매공매사이트Fetcher extends PageFetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	public 경매공매사이트Fetcher() throws HttpException, IOException {
		super("http://www.kyungmaeguide.co.kr/");
	}

	@SuppressWarnings("unused")
	public void login() {
		try {
			PostMethod res = post("/member/loginproc.asp", new NameValuePair[] { new NameValuePair("userid", "radio"),
					new NameValuePair("pwd", "kch7777"), new NameValuePair("x", "10"), new NameValuePair("y", "28"),

			}, new String[][] { { "Origin", "http://www.kyungmaeguide.co.kr" }, { "Host", "www.kyungmaeguide.co.kr" },
					{ "Referer", "http://www.kyungmaeguide.co.kr/main/main.asp" },

			});

			System.out.println(res.getResponseBodyAsString());

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<사건> fetch사건목록(법원 c) throws IOException {

		String html = fetchFirstPage(c);
		logger.info(html);
		int lastPage = parseLatPageNo(html);
		logger.info("마지막 페이지는 " + lastPage + "입니다.");
		List<사건> res = new ArrayList<사건>();
		for (int i = 1; i <= lastPage; i++) {
			logger.info(i + " 페이지를 읽어 옵니다. ");
			html = fetchPage(c, i);
			System.out.println(html);
			Pattern p = Pattern.compile("\\&nbsp;(\\d+)계(.*?)javascript:DetailView\\('([^']+)',(\\d+),(\\d+),(\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
			Matcher m = p.matcher(html);
			while (m.find()) {
				사건 s = new 사건();
				s.setCourt(c);
				s.set담당계(m.group(1));
				s.set법원명(m.group(3));
				s.setYear(Integer.parseInt(m.group(4)));
				s.setNo(Integer.parseInt(m.group(5)));
				s.setSeq(Integer.parseInt(m.group(6)));
				res.add(s);
				logger.info("사건번호:" + s.get사건번호()+"   "+s);
			}
		}

		return res;
	}

	@SuppressWarnings("deprecation")
	private String fetchPage(법원 c, int page) throws UnsupportedEncodingException, IOException {
		PostMethod m = new PostMethod(getRealURL("/auction/SearchList.asp"));
		m.addRequestHeader("Host", "www.kyungmaeguide.co.kr");
		m.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/536.26.17 (KHTML, like Gecko) Version/6.0.2 Safari/536.26.17");
		m.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		m.addRequestHeader("Origin", "http://www.kyungmaeguide.co.kr");
		m.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		m.addRequestHeader("Referer", "http://www.kyungmaeguide.co.kr/auction/SearchList.asp");
		m.addRequestHeader("Accept-Language", "ko-kr");
		m.addRequestHeader("Accept-Encoding", "gzip, deflate");
		m.addRequestHeader("Pragma", "no-cache");
		m.addRequestHeader("Connection", "keep-alive");

		m.setRequestBody(String.format(
				"bubwonlist=%s" + "&rg1_list=" + "&rg2_list=" + "&sort=1" + "&page=" + page + "&searchtype=search" + "&auc_dev="
						+ "&bubwontitle=%s" + "&yongdolist=" + "&law=%s" + "&bubwon=%s" + "&num1=" + "&num2=" + "&state=%s" + "&gambound="
						+ "&sDate=" + "&eDate=" + "&lowbound=" + "&sido=" + "&gugun=" + "&dong=" + "&addr=" + "&addr1=" + "&addr2=",
				new Object[] { c.get경매공매_법원코드(), URLEncoder.encode(c.get법원명().replaceAll("지방법원", "지법"), "EUC-KR"), c.get경매공매_법원코드(),
						c.get경매공매_법원코드(), "%BD%C5%B0%C7" }));
		client.executeMethod(m);
		return new String(m.getResponseBody(), "EUC-KR");
	}

	private int parseLatPageNo(String html) {
		Pattern p = Pattern.compile("gotoPage\\((\\d+)");
		Matcher m = p.matcher(html);
		int lastPage = 1;
		while (m.find()) {
			lastPage = Integer.parseInt(m.group(1));
		}
		return lastPage;
	}

	private String fetchFirstPage(법원 c) throws IOException {
		return fetchPage(c, 1);
	}

	public String fetch사건상세(사건 s) throws HttpException, IOException {
		PostMethod m = new PostMethod(getRealURL("/auction/Detail_Info.asp"));
		m.addRequestHeader("Host", "www.kyungmaeguide.co.kr");
		m.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		m.addRequestHeader("Origin", "http://www.kyungmaeguide.co.kr");
		m.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		m.addRequestHeader("Referer", "http://www.kyungmaeguide.co.kr/auction/SearchList.asp");
		m.addRequestHeader("Accept-Language", "ko-kr");
		m.addRequestHeader("Accept-Encoding", "gzip, deflate");
		m.addRequestHeader("Pragma", "no-cache");
		m.addRequestHeader("Connection", "keep-alive");

		m.setRequestBody(String.format("bubwon=%s&num1=%s&num2=%s&num3=%s",
				new Object[] { URLEncoder.encode(s.get법원명(), "EUC-KR"), s.getYear(), s.getNo(), s.getSeq() }));
		client.executeMethod(m);
		return new String(m.getResponseBody(), "EUC-KR");
	}

	public static void main(String[] args) {
		try {
			경매공매사이트Fetcher f = new 경매공매사이트Fetcher();
			f.login();

			// System.out.println("########");
			사건 s = new 사건();
			s.set법원명("서울중앙지법");
			s.setYear(2011);
			s.setNo(35860);
			s.setSeq(2);
			// s.set토지등기부등본("JJ01/1102/2011/035/1102-2011035860-0002-A.pdf");
			// s.set건물등기부등본("JJ01/1102/2011/035/1102-2011035860-0002-B.pdf");
			// File file = f.download등기부등본(s.get토지등기부등본());

			String html = f.fetch사건상세(s);
			System.out.println(html);
			// System.out.println(html);
			// f.parse등기부등본(s, html);
			// System.out.println(s.get토지등기부등본());
			// System.out.println(s.get건물등기부등본());

			// 법원 c = new 법원();
			// c.set법원명("서울중앙지법");
			// c.set경매공매_법원코드(1102);
			// List<사건> 사건목록 = f.fetch사건목록(c);
			// System.out.println(사건목록);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File download등기부등본(String path) throws HttpException, IOException {
		GetMethod method = new GetMethod("http://img.ch24.co.kr/files/certify/" + path);
		client.executeMethod(method);
		InputStream in = new BufferedInputStream(method.getResponseBodyAsStream());
		File f = File.createTempFile("certify", ".pdf");
		FileOutputStream fout = new FileOutputStream(f);
		System.out.println(f.getAbsolutePath());
		byte[] buf = new byte[4096];
		while (true) {
			int r = in.read(buf);
			if (r == -1) {
				break;
			}
			fout.write(buf, 0, r);
		}
		fout.close();
		in.close();
		return f;
	}

	public void parse등기부등본(사건 s, String html) {
		Pattern p1 = Pattern.compile("name='land_certify'\\s+value='([^']+)");//
		Pattern p2 = Pattern.compile("name='build_certify'\\s+value='([^']+)");
		Matcher m1 = p1.matcher(html);
		if (m1.find()) {
			s.set토지등기부등본(m1.group(1));
		}
		Matcher m2 = p2.matcher(html);
		if (m2.find()) {
			s.set건물등기부등본(m2.group(1));
		}

	}

	public static class 사건 {
		법원 court;
		String 법원명;
		int year;
		int no;
		int seq;
		String 토지등기부등본;
		String 건물등기부등본;
		private String 담당계;

		public String get법원명() {
			return 법원명;
		}

		public void set담당계(String 담당계) {
			this.담당계 = 담당계;

		}

		public String get담당계() {
			return 담당계;
		}

		public void set법원명(String 법원명) {
			this.법원명 = 법원명;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}

		public 법원 getCourt() {
			return court;
		}

		public void setCourt(법원 court) {
			this.court = court;
		}

		@Override
		public String toString() {
			return "사건 [법원명=" + 법원명 + ",담당계="+담당계+", year=" + year + ", no=" + no + ", seq=" + seq + "]";
		}

		public String get토지등기부등본() {
			return 토지등기부등본;
		}

		public void set토지등기부등본(String 토지등기부등본) {
			this.토지등기부등본 = 토지등기부등본;
		}

		public String get건물등기부등본() {
			return 건물등기부등본;
		}

		public void set건물등기부등본(String 건물등기부등본) {
			this.건물등기부등본 = 건물등기부등본;
		}

		public long get사건번호() {
			return Long.parseLong("" + year + "0130" + String.format("%06d", no));
		}
	}
}
