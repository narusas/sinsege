package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.사건;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건감정평가서Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");
	PageFetcher fetcher;

	String fetchMainPage(사건 사건) throws IOException {
		return fetchMainPage(사건.get법원().get법원명(), String.valueOf(사건.get사건번호()));
	}

	String fetchMainPage(String 법원명, String 사건번호) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstSaGamEvalSeo.laf" //
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018" //
		, HTMLUtils.encodeUrl(법원명), 사건번호);

		return 대법원Fetcher.getInstance().fetch(query);
	}

	String parsePDFPageURL(String html) {
		Pattern p = Pattern.compile("<frame style=[^>]* src=\"([^\"]+)\"[^>]+");
		Matcher m = p.matcher(html);
		if (m.find() == false) {
			return null;
		}
		return HTMLUtils.converHTMLSpecialChars(m.group(1));
	}
	
	Pattern hostPattern = Pattern.compile("http://([^/$]+)");
	String fetch(String prefix, String path) throws HttpException, IOException {
		if (fetcher == null) {
			fetcher = new PageFetcher(prefix);
		}
		Matcher m = hostPattern.matcher(prefix);
		if (m.find()){
			return fetcher.fetch(path, new NameValuePair[] {//
					new NameValuePair("Host", m.group(1)), //
					//http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf
					new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf"), //
					new NameValuePair("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"), //
					new NameValuePair("Accept-Language", "ko-kr"), //
					new NameValuePair("Accept-Encoding", "gzip, deflate"), //
					new NameValuePair("Connection", "keep-alive"), //
					new NameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; ko-kr) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4"), //
					new NameValuePair("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"), //
					});
		}
//		return fetcher.fetch(path);
		return fetcher.fetch(path, new NameValuePair[] {//
				
				new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf") //
				});
	}
	
	String fetch(String url) throws HttpException, IOException {
		if (fetcher == null) {
			fetcher = new PageFetcher("");
		}

		return fetcher.fetch(url, new NameValuePair[] {//
				new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf") //
				});
	}

	String parsePDFUrl(String html) {
		Pattern p = Pattern.compile("(top_pdf.php[^\"]+)");
		Matcher m = p.matcher(html);
		if (m.find() == false) {
			return null;
		}
		return m.group(1);
	}

	void downloadPDF(String url, File f) throws HttpException, IOException {
		url = URLDecoder.decode(url,"euc-kr");
		String fileName = url.substring(url.lastIndexOf('/')+1);
		String pre = url.substring(0, url.lastIndexOf('/')+1);
		String encodedFileName = URLEncoder.encode(fileName, "euc-kr");
		
		
		String u = pre+encodedFileName;
//		u = urlEncode(u);
		u = u.replace("+","%20");
		logger.info("PDF URL :"+ u );
		PageFetcher ff = new PageFetcher("");
		
		ff.downloadBinary(u, f);
	}
	
	private String urlEncode(String url) throws UnsupportedEncodingException {
		String head = url.substring(0, url.lastIndexOf("/"));
		String tail = url.substring(url.lastIndexOf("/")+1);
		
		return head+"/"+URLEncoder.encode(tail, "UTF-8");
	}


	public File download(사건 사건) throws IOException {
//		if (true){
//			return null;
//		}
		logger.info("감정평가서를 다운로드를 위한 작업을 시작 합니다");
		String html = fetchMainPage(사건);
		System.out.println(html);
		String url = parsePDFPageURL(html);
		if (url == null || "".equals(url.trim())){
			logger.info("감정평가서가 없는 사건입니다.");
			return null;
		}
		
		Pattern p = Pattern.compile("(http://[^/]+)(/.*)");
		Matcher m = p.matcher(url);
		if (m.find() == false) {
			logger.info("감정평가서 PDF URL Page를 얻어 오지 못했습니다. ");
			return null;
		}
		
		String prefix = m.group(1);
		String path = m.group(2);

		html = fetch(prefix, path);
//		url = parsePDFPageURL(html);
//
//		html = fetch(prefix, "/" + url);

//		Pattern p2 = Pattern.compile("parent\\.aks2\\.location\\.href=\"([^\"]+)\"");
		Pattern p2 = Pattern.compile("document.MainFrame.location\\s*=\\s*'([^']+)");
		Matcher m2 = p2.matcher(html);
		if (m2.find() == false) {
			logger.info("감정평가서 PDF URL이 없습니다.");
			return null;
		}

		url = m2.group(1);
		if (url.startsWith("/") == false){
			url = "/"+url;
		}
		File parent = new File("download/"+사건.getPath());
		if (parent.exists() == false) {
			parent.mkdirs();
		}
		File file = new File(parent, "PDF_Judgement.pdf");
		
		logger.info("감정평가서 다운로드를 시작합니다.( "+prefix+url+ " ) to "+file.getPath());
		downloadPDF(prefix+url, file);
		return file;
	}

}
