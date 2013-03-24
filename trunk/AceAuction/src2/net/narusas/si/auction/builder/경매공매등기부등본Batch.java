package net.narusas.si.auction.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.attested.parser.AtestedPDFParser;
import net.narusas.si.auction.fetchers.경매공매사이트Fetcher;
import net.narusas.si.auction.fetchers.경매공매사이트Fetcher.사건;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.등기부등본Item;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

public class 경매공매등기부등본Batch extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	List<법원> courtList = new ArrayList<법원>();
	private 경매공매사이트Fetcher f;
	private 법원 선택된_법원;
	private List<담당계> 선택된_담담계목록;

	public 경매공매등기부등본Batch(Object[] selected법원s) {
		for (Object c : selected법원s) {
			courtList.add((법원) c);
		}
		try {
			f = new 경매공매사이트Fetcher();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public 경매공매등기부등본Batch(법원 court, List<담당계> chargeList) {
		this.선택된_법원 = court;
		this.선택된_담담계목록 = chargeList;

		try {
			f = new 경매공매사이트Fetcher();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			f.fetch("/main/main.asp");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		f.login();
		사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
		물건Dao dao = (물건Dao) App.context.getBean("물건DAO");
		등기부등본Dao dao2 = (등기부등본Dao) App.context.getBean("등기부등본DAO");
		법원 court = 선택된_법원;
		// for (법원 court : courtList) {
		try {
			logger.info("경매공매 사이트에서 사건 목록을 가져 옵니다." + court.get법원명());
			f.login();
			f.login();

			List<경매공매사이트Fetcher.사건> eventList = f.fetch사건목록(court);
			logger.info("" + eventList.size() + "개의 사건을 가져 왔습니다.");
			List<경매공매사이트Fetcher.사건> filteredEventList = new ArrayList<경매공매사이트Fetcher.사건>();
			
			for (경매공매사이트Fetcher.사건 event : eventList) {
				if (대상_담당계에_속한_사건인가(event) == false) {
					continue;
				}
				filteredEventList.add(event);
			}
			
			logger.info("" + eventList.size() + "개의 사건이 선택된 담당계에 속한 사건입니다.");
			
			for (경매공매사이트Fetcher.사건 event : eventList) {
				if (대상_담당계에_속한_사건인가(event) == false) {
					continue;
				}
				logger.info("" + event.get사건번호() + " 사건 "+event.getSeq()+"에 대한 작업을 시작합니다.");
				net.narusas.si.auction.model.사건 s = eventDao.find(court, event.get사건번호());
				String html = f.fetch사건상세(event);
				System.out.println(html);
				f.parse등기부등본(event, html);
				logger.info("건물 등기부등본:" + event.get건물등기부등본());
				logger.info("토지 등기부등본:" + event.get토지등기부등본());
				List<물건> goodsList = dao.get(s);

				/**
				 * 3. 물건번호가 2개이상있는경우(여러개 있는경우) 등기부 내역을 읽는게 오류입니다.. 물건 하나 읽을때 마다
				 * 
				 * 물건 모두 읽고 또 하나 읽을때 물건 모두 읽고..db에 확인하면 물건 하나에 내역이 똑같은게 여러번
				 * 들어가있어요
				 * 
				 * 물건번호1번만 읽게 처리해주세요
				 */
				if (goodsList == null || goodsList.size() == 0) {
					logger.info("해당 물건이 선처리 되지 않았습니다. ");
					continue;
				}
				물건 물건 =null;
				for (물건 item : goodsList) {
					if (item.get물건번호() == event.getSeq()){
						물건 = item;
					}
				}
				if (물건 == null){
					logger.info("해당 물건이 선처리 되지 않았습니다. ");
					continue;
				}

				/**
				 * 4. ac_attested안에 들어가는 타입종류가 기존에는 한글로 집합건물,토지,건물 이렇게 되어 있는데
				 * 경매공매가이드를 돌리면 영어로 land,building 이렇게 들어갑니다..똑같이 맞쳐주세요
				 */

				if (event.get건물등기부등본() != null) {
					update등기부등본(물건, "http://img.ch24.co.kr/files/certify/" + event.get건물등기부등본(), "건물");
				}

				if (event.get토지등기부등본() != null) {
					update등기부등본(물건, "http://img.ch24.co.kr/files/certify/" + event.get토지등기부등본(), "토지");

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean 대상_담당계에_속한_사건인가(사건 event) {
		if (선택된_담담계목록 == null || 선택된_담담계목록.size() == 0){
			return true;
		}
		Pattern p = Pattern.compile("(\\d+)");
		for(담당계 charge:선택된_담담계목록 ){
			Matcher m = p.matcher(charge.get담당계이름());
			m.find();
			String chargeNo = m.group(1);
			if (event.get담당계().equals(chargeNo)){
				return true;
			}
		}
		return false;
	}

	void update등기부등본(물건 물건, String url, String type) {
		if (url == null || "".equals(url) || url.startsWith("http") == false) {
			return;
		}
		try {
			File downloaded = downloadPDFBinary(url);
			logger.info("다운 받은 파일 위치:" + downloaded.getAbsolutePath());

			logger.info("등기부등본 분석을 시작합니다.");
			List<등기부등본Item> items = new AtestedPDFParser().parse(downloaded);
			for (등기부등본Item item : items) {
				logger.info(item.toString());
			}
			물건Dao goodsDAO = (물건Dao) App.context.getBean("물건DAO");
			등기부등본 atested = new 등기부등본(물건, type);
			atested.setItems(items);

			등기부등본Dao dao = (등기부등본Dao) App.context.getBean("등기부등본DAO");

			// 2. db안에 등기부내역이 있으면 안들어가게..지금은 내역이 있어도 또 들어가고 또들어가고 하네요..

			Collection<등기부등본> 기존 = dao.get(물건);
			if (기존 == null || 기존.size() == 0) {
				dao.saveOrUpdate(atested);
			}
			else {
				logger.info("기존에 처리된 물건입니다.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private File downloadPDFBinary(String url) throws MalformedURLException, IOException {
		logger.info("PDF 파일을 다운로드합니다 :" + url);
		URL u = new URL(url);
		File f = File.createTempFile("atested", ".pdf");
		FileOutputStream out = new FileOutputStream(f);
		out.write(net.narusas.util.lang.NInputStream.readBytes(u.openStream()));
		out.flush();
		out.close();
		return f;
	}
}
