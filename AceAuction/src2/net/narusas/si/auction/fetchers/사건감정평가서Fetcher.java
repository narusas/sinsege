package net.narusas.si.auction.fetchers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

	public String fetchMainPage(사건 사건) throws IOException {
		return fetchMainPage(사건.get법원().get법원명(), String.valueOf(사건.get사건번호()));
	}

	String fetchMainPage(String 법원명, String 사건번호) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstSaGamEvalSeo.laf" //
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018" //
		, HTMLUtils.encodeUrl(법원명), 사건번호);
		Exception e = null;
		for(int i=0;i<3;i++){
			
			try {
				return 대법원Fetcher.getInstance().fetch(query);
			} catch (Exception ex) {
				e = ex;
			}
		}
		throw new IOException(e);
		
	}

	public String parsePDFPageURL(String html) throws IOException {
		Pattern p = Pattern.compile("<frame style=[^>]* src=\"([^\"]+)\"[^>]+");
		Matcher m = p.matcher(html);
		if (m.find() == false) {
			return null;
		}
		return  HTMLUtils.converHTMLSpecialChars(m.group(1));
	}
	
	static Pattern hostPattern = Pattern.compile("http://([^/$]+)");
	String fetch(String prefix, String path) throws HttpException, IOException {
		if (fetcher == null) {
			fetcher = new PageFetcher(prefix);
		}
		
		
//		Host: ca.kapanet.or.kr
//		User-Agent: Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; ko; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//		Accept-Language: ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3
//		Accept-Encoding: gzip,deflate
//		Accept-Charset: EUC-KR,utf-8;q=0.7,*;q=0.7
//		Keep-Alive: 115
//		Connection: keep-alive
//		Referer: http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf

		Matcher m = hostPattern.matcher(prefix);
		if (m.find()){
			logger.info("감정평가서 서버:"+m.group(1) +"   "+path);
			
			
			try {
				Socket soc = new Socket(m.group(1),80);
				OutputStream out = soc.getOutputStream();
				InputStream in = soc.getInputStream();
				out.write(("GET "+path+" HTTP/1.0\r\n").getBytes());
				String request = "Host: ca.kapanet.or.kr\r\n"+
"User-Agent: Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; ko; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15\r\n"+
"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n"+
"Accept-Language: ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3\r\n"+
"Accept-Encoding: gzip,deflate\r\n"+
"Accept-Charset: EUC-KR,utf-8;q=0.7,*;q=0.7\r\n"+
"Connection: close\r\n"+
"Referer: http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf\r\n\r\n";
				out.write(request.getBytes());
				out.flush();
				logger.info("OUTPUT");
				int len = 0;
				while (true) {
					int r = in.read();
					System.out.print((char) r);
					if (r == -1) {
						break;
					}
					if (len == 1 && r == '\n') {
						break;
					}
					len++;
					if (r == '\n'){
						len = 0;
					}
				}
				logger.info("HEADER readed");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				while(true){
					int r = in.read();
					if (r == -1){
						break;
					}
					bout.write(r);
				}
				logger.info("All readed");
				return new String(bout.toByteArray());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			return fetcher.fetch(path, new NameValuePair[] {//
					//
					new NameValuePair("Host", m.group(1)), //
					new NameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; ko; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15"), //
					new NameValuePair("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"), //
					new NameValuePair("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3"), //
					new NameValuePair("Accept-Encoding", "gzip,deflate"), //
					new NameValuePair("Accept-Charset", "EUC-KR,utf-8;q=0.7,*;q=0.7"), //
					new NameValuePair("Connection", "close"), //
					new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf"), //
////					new NameValuePair("Host", "ca.kapanet.or.kr"), //
//					//http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf
//					new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf"), //
//					new NameValuePair("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"), //
//					new NameValuePair("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3"), //
//					new NameValuePair("Accept-Charset", "EUC-KR,utf-8;q=0.7,*;q=0.7"), //
//					new NameValuePair("Accept-Encoding", "gzip, deflate"), //
//					new NameValuePair("Connection", "keep-alive"), //
//					new NameValuePair("Keep-Alive", "115"), //
//
//					new NameValuePair("User-Agent", "Mozilla/5.0  Firefox/3.6.15"), //
//					new NameValuePair("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5"), //
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
	
	public String fetchTopFrame(String 법원명,  String  사건번호) throws IOException{
		String query = MessageFormat.format("/RetrieveRealEstSaGamEvalSeoTop.laf" //
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018" //
				, HTMLUtils.encodeUrl(법원명), 사건번호);
		String topHtml = 대법원Fetcher.getInstance().fetch(query);
		return topHtml;
	}


	public List<File> download(사건 사건) throws IOException {
		List<File>  감정평가서_파일목록 =  new ArrayList<File>();
//		if (true){
//			return null;
//		}
		logger.info("감정평가서를 다운로드를 위한 작업을 시작 합니다");
		{
			String html = fetchMainPage(사건);
			String url = parsePDFPageURL(html);

		}
		
		
		String topHtml = fetchTopFrame( 사건.get법원().get법원명(), String.valueOf(사건.get사건번호()));
		
		System.out.println(topHtml);
		List<String> 사건번호목록 = 사건번호추출(topHtml);
		if ( 사건번호목록 ==  null || 사건번호목록.size() ==0){
			  return  download단일사건( 사건);
		}
		logger.info("이 사건에 관련된 감정평가서의 사건번호 목록:"+  사건번호목록);
		int count = 0;
		for (String  사건번호 : 사건번호목록) {
			String  mainHtml = fetchMainPage( 사건.get법원().get법원명(), 사건번호);
			String url = parsePDFPageURL(mainHtml);
			if (url == null || "".equals(url.trim())){
				logger.info("감정평가서가 없는 사건입니다.");
				continue;
			}
			Pattern p = Pattern.compile("(http://[^/]+)(/.*)");
			Matcher m = p.matcher(url);
			if (m.find() == false) {
				logger.info("감정평가서 PDF URL Page를 얻어 오지 못했습니다. ");
				continue;
			}
			String prefix = m.group(1);
			String path = m.group(2);
			logger.info("감정평가서 다운로드 사전작업 Phase -2 start :"+path);
			String html = fetch(prefix, path);
			logger.info("감정평가서 다운로드 사전작업 Phase -2 end");
			
//			url = parsePDFPageURL(html);
	//
//			html = fetch(prefix, "/" + url);

//			Pattern p2 = Pattern.compile("parent\\.aks2\\.location\\.href=\"([^\"]+)\"");
			Pattern p2 = Pattern.compile("document.[^\\.]+.location\\s*=\\s*'([^']+)");
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
			
			File file = new File(parent, count == 0? "PDF_Judgement.pdf":"PDF_Judgement_"+count+".pdf");
			
			logger.info("감정평가서 다운로드를 시작합니다.( "+prefix+url+ " ) to "+file.getPath());
			downloadPDF(prefix+url, file);
			감정평가서_파일목록.add( file);
			count++;
		}
		
		return 감정평가서_파일목록;
	}

	private List<File> download단일사건(사건 사건) throws HttpException, IOException {
		logger.info("감정평가서 다운로드 사전작업 Phase -1 start");
		String html = fetchMainPage(사건);
		logger.info("감정평가서 다운로드 사전작업 Phase -1 end");
		
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
		logger.info("감정평가서 다운로드 사전작업 Phase -2 start :"+path);
		html = fetch(prefix, path);
		logger.info("감정평가서 다운로드 사전작업 Phase -2 end");
		
//		url = parsePDFPageURL(html);
//
//		html = fetch(prefix, "/" + url);

//		Pattern p2 = Pattern.compile("parent\\.aks2\\.location\\.href=\"([^\"]+)\"");
		Pattern p2 = Pattern.compile("document.[^\\.]+.location\\s*=\\s*'([^']+)");
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
		return  Arrays.asList(file);
	}

	private List<String> 사건번호추출(String topHtml) {
		Pattern optionPattern = Pattern.compile("option value=\"(\\d+),(\\d+)\"");
		Matcher optionMatcher = optionPattern.matcher(topHtml);
		List<String> 사건번호목록 =  new ArrayList<String>();
		while(optionMatcher.find()){
			사건번호목록.add(optionMatcher.group(1));
		}
		return 사건번호목록;
	}

}
