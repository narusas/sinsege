package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.감정평가서;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 대법원에서 감정평가서를 다운받기 위한 Fetcher이다.
 * 
 * @author narusas
 * 
 */
public class 대법원감정평가서Fetcher {

	private boolean launchUI;

	static Set<String> imgPageNos = new HashSet<String>();
	static {
		File f = new File("image_nos.txt");
		String text;
		try {
			text = NFile.getText(f);
			String[] nos = text.split(",");
			for (String no : nos) {
				String trimed = no.trim();
				if ("".equals(trimed)) {
					continue;
				}
				imgPageNos.add(trimed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		imgPageNos.add("85");
	}

	public byte[] fetch(감정평가서 gam, boolean launchUI) throws IOException {
		String queryString = gam.toQueryString();
		return fetch(launchUI, queryString);
	}

	public byte[] fetch(boolean launchUI, String queryString)
			throws IOException, HttpException {
		this.launchUI = launchUI;
		System.out.println(queryString);
		PageFetcher f = 대법원Fetcher.getInstance();
		String page = f.fetch("/au/auc/C103Frame.jsp?" + queryString);
		// System.out.println(page);
		Pattern p = Pattern.compile("(C103Top.jsp?[^\"]+)");
		Matcher m = p.matcher(page);
		if (!m.find()) {
			return null;
		}

		String topUrl = m.group(1);
		// System.out.println(topUrl);
		String topPage = f.fetch("/au/auc/" + topUrl);

		Pattern p2 = Pattern.compile("var bottomUrl = \"([^\"]+)");
		Matcher m2 = p2.matcher(topPage);
		// System.out.println(topPage);
		m2.find();
		// System.out.println("------------------------------");

		Pattern optionPattern = Pattern.compile("option value=\"([^\"]+)");
		Matcher optionMatcher = optionPattern.matcher(topPage);
		String value = "1";
		while (optionMatcher.find()) {
			value = optionMatcher.group(1);
		}
		String nURL = m2.group(1) + value;

		// System.out.println(nURL);

		// 실제 감정평가서를 다운받는 곳은 다른 서버(210.98.146.20)로 연결되어 있다.
		String fPage = f.fetch(m2.group(1) + value, new NameValuePair[] {
				new NameValuePair("Host", "210.98.146.20"),
				new NameValuePair("Referer",
						"http://courtauction.go.kr/au/auc/" + topUrl) });

		Pattern p3 = Pattern.compile("(top_pdf.php?[^\"]+)");
		Matcher m3 = p3.matcher(fPage);
		// System.out.println("------------------------------");
		if (m3.find() == false) {
			return fetchMultiplePDFFIle(fPage);
		}
		byte[] pdf = fetchSinglePDFFile(f, m3);
		return pdf;
	}

	private byte[] fetchMultiplePDFFIle(String page) throws IOException {
		// System.out.println(page);
		Pattern p = Pattern.compile("ssid=([^\"]+)");
		Matcher m = p.matcher(page);
		m.find();
		String url = "http://210.98.146.41/menu.php?ssid=" + m.group(1);
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		String pdfPage = fetcher.fetch(url, null);
		// System.out.println(pdfPage);

		return fetchPDF(pdfPage);
	}

	private byte[] fetchPDF(String page) throws IOException {
		Pattern p = Pattern.compile("detail.php\\?ssid=([^&]+)&gubun=([^\"]+)");
		Matcher m2 = p.matcher(page);
		HashSet urls = new HashSet();
		byte[] result = null;
		while (m2.find()) {
			String ssid = m2.group(1);
			String gubun = m2.group(2);
			String url = "http://210.98.146.41/detail.php?ssid=" + ssid
					+ "&gubun=" + gubun;
			urls.add(url);

			if (imgPageNos.contains(gubun)) {
				HttpClient client = new HttpClient();
				GetMethod method = new GetMethod(url);
				client.executeMethod(method);
				String detailPage = method.getResponseBodyAsString();
				System.out.println(detailPage);

				Pattern fsizeP = Pattern.compile("fsize\" value=\"([^\"]+)",
						Pattern.MULTILINE);
				Matcher m = fsizeP.matcher(detailPage);
				m.find();
				String fsize = m.group(1);

				Pattern gubunP = Pattern.compile("gubun\" value=\"([^\"]+)",
						Pattern.MULTILINE);
				m = gubunP.matcher(detailPage);
				m.find();

				String g = m.group(1);

				result = downMultiPdf(ssid, fsize, g);
			}

		}
		if (launchUI) {
			for (Iterator it = urls.iterator(); it.hasNext();) {
				String url = (String) it.next();
				Runtime.getRuntime().exec(
						"C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "
								+ url);
			}
		}

		return result;

	}

	private byte[] downMultiPdf(String ssid, String fsize, String type)
			throws HttpException, IOException {
		String url = "http://210.98.146.41/down.asp?ssid=" + ssid + "&ftype="
				+ type + "&fsize=" + fsize + "&rmk=";
		System.out.println(url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("Host", "210.98.146.41");
		method
				.setRequestHeader("User-Agent",
						"Mozilla/3.0 (compatible;  KAPA)");
		method.setRequestHeader("Accept", "text/html,*/*");
		method.setRequestHeader("Connection", "keep-alive");

		client.executeMethod(method);

		byte[] res = method.getResponseBody();
		NFile.write(new File(type + ".ddd"), res);
		return res;
	}

	private byte[] fetchSinglePDFFile(PageFetcher f, Matcher m3)
			throws IOException, HttpException {
		String pdfPageURL = "http://210.98.146.20/" + m3.group(1);
		String pdfPage = f.fetch(pdfPageURL, null);

		Pattern p4 = Pattern.compile("parent.aks2.location.href=\"([^\"]+)");
		Matcher m4 = p4.matcher(pdfPage);
		if (m4.find() == false) {
			return null;
		}
		// m4.find();

		String pdfURL = "http://210.98.146.20" + m4.group(1);
		// System.out.println(pdfURL);
		byte[] pdf = f.fetchBinary(pdfURL, null);
		return pdf;
	}
}

interface PictureSource {

}