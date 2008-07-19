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
		String bub_name = "���ﵿ���������";
		String sa_no = "20080130005794";
		String jp_cd = "1001";
		String dam_nm = "���1��";

		getCookie(fetcher, bub_cd, bub_name, jp_cd, dam_nm);

		System.out.println("####"
				+ fetcher.client.getParams().getParameter("Cookie"));

		String query = "au.command.auc.C311ListCommand&bub_cd=" + bub_cd + "&"
				+ "sa_no=" + sa_no + "&" + "jp_cd=" + jp_cd + "&" + "dam_nm="
				+ URLEncoder.encode(dam_nm, "euc-kr")
				+ "search_flg=8&browser=2";
		String page = fetcher.fetch(query);

		// System.out.println(TableParser.getNextTDValueStripped(page, "��ǹ�ȣ"));
		System.out.println(TableParser.getNextTDValueStripped(page, "��Ǹ�"));
		System.out.println(TableParser.getNextTDValueStripped(page, "��������"));
		System.out.println(TableParser.getNextTDValueStripped(page, "���ð�������"));
		System.out.println(TableParser.getNextTDValueStripped(page, "����"));
		System.out.println(TableParser.getNextTDValueStripped(page, "û���ݾ�"));
		System.out.println(TableParser
				.getNextTDValueStripped(page, "����װ�/��������"));
		System.out.println(TableParser.getNextTDValueStripped(page, "�������"));
		System.out.println(TableParser.getNextTDValueStripped(page, "��������"));

//		parse���(page);
		
		int startPos = page.indexOf("����ڳ���</b>");
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

	private void parse���(String page) {
		int startPos = page.indexOf("��ϳ���</b>");
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
