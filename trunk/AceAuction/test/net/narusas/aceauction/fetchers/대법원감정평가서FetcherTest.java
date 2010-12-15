package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.감정평가서Link;
import net.narusas.si.auction.fetchers.PageFetcher;
import net.narusas.si.auction.fetchers.대법원Fetcher;

import org.apache.commons.httpclient.NameValuePair;

public class 대법원감정평가서FetcherTest extends TestCase {
	public void test1() throws IOException {
		// <form name=gamForm method=post action="">
		// <input type=hidden name=bub_cd value="">
		// <input type=hidden name=bub_nm value="">
		// <input type=hidden name=sa_no value="">
		// <input type=hidden name=user_mo_sa_no value="">
		// <input type=hidden name=gubun value="02">
		// <input type=hidden name=browser value="">
		// <input type=hidden name=displaySaList_RelSa value="">
		// <input type=hidden name=userSaList_RelSa value="">
		// <input type=hidden name=saList_HasGam value="">
		// <input type=hidden name=userSaList_HasGam value="">
		// <input type=hidden name=sano_HasGam value="">
		// <input type=hidden name=mae_giil value="">
		// <input type=hidden name=gam_no value="">
		// </form>

		// <a href="javascript:loadGam( '000412',
		// '20050130030813',
		// '2005타경30813',
		// '<br>2006타경11192<br>(중복)',
		// '20050130030813#',
		// '2005타경30813#',
		// '20050130030813',
		// '2005타경30813',
		// '',
		// '2007.04.17',
		// '33253'

		String src = "<a href=\"javascript:loadGam( '000210', \n"
				+ "												 '20040130051550',\n"
				+ "												 '2004타경51550', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '2007.04.03',\n"
				+ "												 '53774' \n" + "											    );\" id=\"a1\">";

		감정평가서Link gam = 대법원사건Parser.parse감정평가서(src);

		PageFetcher f = 대법원Fetcher.getInstance();
		String page = f.fetch("/au/auc/C103Frame.jsp?bub_cd=" + gam.getBub_cd()
				+ "&sa_no=" + gam.getSa_no() + "&mae_giil=" + gam.getMae_giil()
				+ "&sano_HasGam=" + gam.getSano_HasGam() + "&gam_no="
				+ gam.getGam_no());

		Pattern p = Pattern.compile("(C103Top.jsp?[^\"]+)");
		Matcher m = p.matcher(page);
		assertTrue(m.find());
		String topUrl = m.group(1);
		// assertEquals(
		// "C103Top.jsp?bub_cd=000412&sa_no=20050130030813&max_ord_hoi=1&ord_hoi=1&mae_giil=2007.04.17",
		// topUrl);

		String topPage = f.fetch("/au/auc/" + topUrl);

		Pattern p2 = Pattern.compile("var bottomUrl = \"([^\"]+)");
		Matcher m2 = p2.matcher(topPage);
		assertTrue(m2.find());
		// assertEquals(
		// "http://210.98.146.20/main.asp?bub_cd=000412&sa_no=20050130030813&mae_giil=2007.04.17&ord_hoi=",
		// m2.group(1));
		// Pattern p3 = Pattern.compile("");
		// Matcher m3 = p3.matcher(m2.group(1));
		// m3.find();
		// assertEquals("http://210.98.146.20/")
		String fPage = f.fetch(m2.group(1) + "1", new NameValuePair[] {
				new NameValuePair("Host", "210.98.146.20"),
				new NameValuePair("Referer", f.getUrl_prefix() + "/au/auc/"
						+ topUrl) });

		Pattern p3 = Pattern.compile("(top_pdf.php?[^\"]+)");
		Matcher m3 = p3.matcher(fPage);
		m3.find();
		String pdfPageURL = "http://210.98.146.20/" + m3.group(1);
		String pdfPage = f.fetch(pdfPageURL, null);

		Pattern p4 = Pattern.compile("parent.aks2.location.href=\"([^\"]+)");
		Matcher m4 = p4.matcher(pdfPage);
		m4.find();

		String pdfURL = "http://210.98.146.20" + m4.group(1);
//		System.out.println(pdfURL);
		byte[] pdf = f.fetchBinary(pdfURL, null);

	}

	public void test대법원감정평가서Fetcher() throws IOException {
		String src = "<a href=\"javascript:loadGam( '000210', \n"
				+ "												 '20040130051550',\n"
				+ "												 '2004타경51550', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '', \n"
				+ "												 '', \n" + "												 '2007.04.03',\n"
				+ "												 '53774' \n" + "											    );\" id=\"a1\">";
		감정평가서Link gam = 대법원사건Parser.parse감정평가서(src);
		대법원감정평가서Fetcher f = new 대법원감정평가서Fetcher();
		byte[] pdfBinary = f.fetch(gam, false);
//		for (byte b : pdfBinary) {
//			System.out.print(" " + Integer.toHexString(b & 0xFF));
//		}
	}

	public void testNoPDF() throws IOException {
		String src = " <a href=\"javascript:loadGam( '000213',\n"
				+ "			 '20050130018108', \n" + "			 '2005타경18108', \n"
				+ "			 '<br>2006타경9279<br>(중복)',\n" + "			 '', \n"
				+ "			 '', \n" + "			 '', \n" + "			 '', \n" + "			 '', \n"
				+ "			 '2007.04.09',\n" + "			 '20341' \n"
				+ "		    );\" id=\"a1\">";
	감정평가서Link gam = 대법원사건Parser.parse감정평가서(src);
	대법원감정평가서Fetcher f = new 대법원감정평가서Fetcher();
	byte[] pdfBinary = f.fetch(gam, false);
	assertNull(pdfBinary);
	}
}
