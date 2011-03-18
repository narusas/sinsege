package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.당사자;
import net.narusas.si.auction.model.목록;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.배당요구종기내역;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.담당계Dao;
import net.narusas.si.auction.model.dao.담당계DaoHibernate;
import net.narusas.util.lang.Closure;
import net.narusas.util.lang.NCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건내역Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	public boolean update(사건 s) throws IOException {
		return update(s, true);
	}

	public boolean update(사건 s, boolean sagunDetailOnly) throws IOException {
		Exception ex = null;
		for (int trial = 1; trial <= 3; trial++) {
			try {
				logger.info("사건 페이지를 얻어옵니다(Trial#" + trial + ": " + s.get사건번호());
				String html = fetch(s);
				logger.info("사건 페이지를 분석합니다");
				parseHTML(s, html);

				List<물건> goodsList = parse물건(s, html);
				logger.info("물건 목록:" + goodsList);
				s.set물건목록(goodsList);

				if (sagunDetailOnly) {

					List<당사자> peoples = parse당사자(s, html);
					logger.info("당사자목록:" + peoples);
					s.set당사자목록(peoples);

					logger.info("사진 페이지를 분석합니다");
					사건사진Fetcher pictureFetcher = new 사건사진Fetcher();
					List<String> urls = pictureFetcher.fetchAllImageURLs(s);
					사진Downloader.getInstance().add(s, urls);

					logger.info("기일내역 페이지를 분석합니다");
					사건기일내역Fetcher 기일내역Fetcher = new 사건기일내역Fetcher();
					기일내역Fetcher.update(s);
				}
				return true;
			} catch (Exception e) {
				ex = e;
			}
		}
		throw new IOException(ex);
	}

	public String fetch(사건 s) throws IOException {
		Exception ee = null;
		for (int i = 0; i < 3; i++) {
			try {
				String query = MessageFormat.format("/RetrieveRealEstDetailInqSaList.laf" //
						+ "?jiwonNm={0}&saNo={1}&_SRCH_SRNID=PNO102005" //
				, HTMLUtils.encodeUrl(s.get법원().get법원명()), String.valueOf(s.get사건번호()));
				return 대법원Fetcher.getInstance().fetch(query);
			} catch (Exception e) {
				ee = e;
			}
		}
		throw new RuntimeException(ee);
	}

	public void parseHTML(사건 s, String html) {
		String 담당계 = HTMLUtils.findTHAndNextValue(html, "담당계");
		if (담당계 != null && 담당계.contains("전화")) {
			Pattern p = Pattern.compile("전화 : (.*)");
			Matcher m = p.matcher(담당계);
			if (m.find()) {
				String 전화 = m.group(1);
				if (s.get담당계().get전화번호() == null) {
					s.get담당계().set전화번호(전화);
					update담당계(s.get담당계());
				}
			}

		}
		s.set사건명(HTMLUtils.findTHAndNextValue(html, "사건명"));
		s.set개시결정일자(HTMLUtils.toDate(HTMLUtils.findTHAndNextValue(html, "개시결정일자")));
		s.set접수일자(HTMLUtils.toDate(HTMLUtils.findTHAndNextValue(html, "접수일자")));
		s.set청구금액(toLong(금액Converter.convert(HTMLUtils.findTHAndNextValue(html, "청구금액"))));
		s.set사건항고정지여부(HTMLUtils.findTHAndNextValue(html, "사건항고/정지여부"));
		// s.set종국결과(HTMLUtils.findTHAndNextValue(html, "종국결과"));
		// s.set종국일자(HTMLUtils.findTHAndNextValue(html, "종국일자"));

		handl병합(s, html);

		logger.info("사건명:" + s.get사건명());
		logger.info("개시결정일자:" + s.get개시결정일자());
		logger.info("접수일자:" + s.get접수일자());
		logger.info("청구금액:" + s.get청구금액());
		logger.info("사건항고정지여부:" + s.get사건항고정지여부());
		logger.info("병합:" + s.get병합());
	}

	private void update담당계(담당계 담당계) {
		담당계Dao 담당계dao = (담당계DaoHibernate) App.context.getBean("담당계DAO");
		담당계dao.update(담당계);
	}

	private void handl병합(사건 s, String html) {
		String chunk = HTMLUtils.findTHAndNextValueAsComplex(html, "중복/병합/이송");
		if (chunk != null) {
			List<String> anchors = HTMLUtils.findAnchors(chunk);
			List res = NCollection.collect(anchors, new Closure() {
				@Override
				public Object collect(Object object) {
					return HTMLUtils.strip((String) object);
				}
			});
			s.set병합(net.narusas.util.TextUtil.join(res.toArray(), ","));
		} else {
			s.set병합("");
		}
	}

	private Long toLong(String v) {
		try {
			return Long.parseLong(v);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	public 배당요구종기내역 parse배당요구종기내역(String html) {
		Sheet sheet = Sheet.parse(html, "<caption>배당요구종기내역</caption>");
		return new 배당요구종기내역(sheet);
	}

	public List<당사자> parse당사자(사건 event, String html) {
		Sheet sheet = Sheet.parse(html, "<caption>당사자내역</caption>");
		List<당사자> res = new LinkedList<당사자>();
		for (int i = 0; i < sheet.rowSize(); i++) {
			add당사자(event, res, sheet.valueAt(i, 0), sheet.valueAt(i, 1));
			add당사자(event, res, sheet.valueAt(i, 3), sheet.valueAt(i, 4));

		}
		return res;
	}

	private void add당사자(사건 event, List<당사자> res, String 당사자구분, String 당사자명) {
		// if ("".equals(당사자구분.trim()) == false && "".equals(당사자명.trim()) ==
		// false && isAcceptable당사자구분(당사자구분)) {
		if ("".equals(당사자구분.trim()) == false && "".equals(당사자명.trim()) == false) {
			res.add(new 당사자(event, 당사자구분, 당사자명));
		}
	}

	static String[] acceptables = { "채권자", "채무자", "소유자", "채무자겸소유자" };

	// static {
	// try {
	// String temp = NFile.getText(new File("conf/당사자.cfg"), "utf-8");
	// acceptables = temp.split(",");
	// // System.out.println(acceptables);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	//
	private boolean isAcceptable당사자구분(String 당사자구분) {
		if (당사자구분 == null) {
			return false;
		}
		for (String acceptable : acceptables) {
			if (acceptable.equals(당사자구분.trim())) {
				return true;
			}
		}
		return false;
	}

	public List<물건> parse물건(사건 event, String html) {
		Pattern p = Pattern.compile("<th[^>]*>물건번호</th>\\s*<td[^>]*>\\s*(\\d+)&nbsp;\\s+<a", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		List<물건> res = new LinkedList<물건>();

		while (m.find()) {
			물건 goods = new 물건();
			goods.set물건번호(Integer.parseInt(m.group(1).trim()));
			goods.set사건(event);
			goods.set법원(event.get법원());
			goods.set담당계(event.get담당계());
			goods.set사건(event);

			parse목록(html.substring(m.start(), html.indexOf("</table>", m.start())), goods);

			res.add(goods);
		}
		event.set물건목록(res);
		return res;
	}

	private void parse목록(String chunk, 물건 goods) {
		Pattern noPattern = Pattern.compile("<th>목록\\s*(\\d+)</th>\\s*<td[^>]+>([^<]+)");

		Matcher m = noPattern.matcher(chunk);
		while (m.find()) {

			String no = m.group(1);
			String address = m.group(2).trim();
			String type = null;
			String comment = null;

			String rest = chunk.substring(m.end());

			Pattern popupPattern = Pattern.compile("regiBU\\('(\\d+)");
			Matcher m2 = popupPattern.matcher(rest);
			if (m2.find() == false) {
				return;
			}

			String buNo = m2.group(1);
			Pattern typePattern = Pattern.compile("<th[^>]*>목록구분</th>\\s*<td[^>]+>([^<]+)</td>");
			Matcher m3 = typePattern.matcher(rest);
			if (m3.find()) {
				type = m3.group(1);
			}

			// Pattern commentPattern =
			// Pattern.compile("<th[^>]*>비고</th>\\s*<td[^>]+>");
			Pattern commentPattern = Pattern.compile("<th[^>]*>비고</th>\\s*<td[^>]*>");
			Matcher m4 = commentPattern.matcher(rest);
			if (m4.find()) {
				comment = HTMLUtils.strip(rest.substring(m4.end(), rest.indexOf("</td", m4.end())));
			}
			System.out.println(no + " " + address + " " + type + " " + comment);

			goods.add목록(new 목록(goods, no, address, buNo, type, comment));
		}
	}

	public String fetch기일결과(사건 사건) throws IOException {
		String html = fetch(사건);
		return HTMLUtils.findTHAndNextValue(html, "종국결과");
	}
}
