package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.fetchers.PageFetcher;
import net.narusas.aceauction.fetchers.TableParser;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;

import org.apache.commons.httpclient.HttpException;

public class BeadangDetailFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {
		PageFetcher fetcher = new PageFetcher(
				"http://www.courtauction.go.kr/au/SuperServlet?target_command=");

		String bub_cd = "000211";
		String bub_name = "서울동부지방법원";
		String sa_no = "20080130005794";
		String jp_cd = "1001";
		String dam_nm = "담당1계";

		getCookie(fetcher, bub_cd, bub_name, jp_cd, dam_nm);

		System.out.println("####"
				+ fetcher.client.getParams().getParameter("Cookie"));

		String query = "au.command.auc.C311ListCommand&bub_cd=" + bub_cd + "&"
				+ "sa_no=" + sa_no + "&" + "jp_cd=" + jp_cd + "&" + "dam_nm="
				+ URLEncoder.encode(dam_nm, "euc-kr")
				+ "search_flg=8&browser=2";
		String page = fetcher.fetch(query);

		// System.out.println(TableParser.getNextTDValueStripped(page, "사건번호"));
		System.out.println(TableParser.getNextTDValueStripped(page, "사건명"));
		System.out.println(TableParser.getNextTDValueStripped(page, "접수일자"));
		System.out.println(TableParser.getNextTDValueStripped(page, "개시결정일자"));
		System.out.println(TableParser.getNextTDValueStripped(page, "담당계"));
		System.out.println(TableParser.getNextTDValueStripped(page, "청구금액"));
		System.out.println(TableParser
				.getNextTDValueStripped(page, "사건항고/정지여부"));
		System.out.println(TableParser.getNextTDValueStripped(page, "종국결과"));
		System.out.println(TableParser.getNextTDValueStripped(page, "종국일자"));

//		parse목록(page);
		
		int startPos = page.indexOf("당사자내역</b>");
		int endPos = page.indexOf("</table>", startPos);
		
		Table table = TableParser.parseTable(page, startPos,endPos );
		System.out.println(table.getHeaders());
		
		List<Row> rows = table.getRows();
		for (Row row : rows) {
			System.out.println("###");
			System.out.println(row.getValue(0));
//			System.out.println(row.getValue(1));
			System.out.println(row.getValue(2));
		}
	}

	private void parse목록(String page) {
		int startPos = page.indexOf("목록내역</b>");
		int endPos = page.indexOf("</table>", startPos);
		
		Table table = TableParser.parseTable(page, startPos,endPos );
		System.out.println(table.getHeaders());
		
		List<Row> rows = table.getRows();
		for (Row row : rows) {
			System.out.println(row);
		}
	}

	private void getCookie(PageFetcher fetcher, String bub_cd, String bub_name,
			String jp_cd, String dam_nm) throws IOException {
		String query = "bub_cd=" + URLEncoder.encode(bub_cd, "euc-kr")//
				+ "&" + "bub_nm=" + URLEncoder.encode(bub_name, "euc-kr")//
				+ "&" + "jp_cd=" + URLEncoder.encode(jp_cd, "euc-kr")//
				+ "&" + "dam_nm=" + URLEncoder.encode(dam_nm, "euc-kr");

		String page = fetcher.fetch("au.command.aua.A312ListCommand&" + query);
	}
}
