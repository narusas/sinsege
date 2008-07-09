/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.logging.Logger;

import net.narusas.aceauction.model.물건;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * The Class 스피드옥션_물건Updater.
 */
public class 스피드옥션_물건Updater {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * Update detail.
	 * 
	 * @param item the item
	 * @param gamjungInfo the gamjung info
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void updateDetail(물건 item, String[] gamjungInfo)
			throws HttpException, IOException {
		// if (items == null) {
		// return;
		// }
		// List<부동산표시목록Item> item = items.getItem(this);

		스피드옥션물건상세내용Fetcher fetcher = new 스피드옥션물건상세내용Fetcher();
		String src = fetcher.getPage(item.사건.court.get스피드옥션Code(),
				item.사건.charge.get담당계이름(), item.사건.getEventYear(), item.사건
						.getEventNo(), String.valueOf(item.물건번호));
		item.setDetail(fetcher.parse(src));

		logger.info("등기부등본  URL을 얻어올 준비를 합니다.  ");
		try {
			스피드옥션ZoomPageFetcher 사진Fetcher = new 스피드옥션ZoomPageFetcher();
			사진Fetcher.init(src);
			// 사진Fetcher.fetch사진(this, 사진Fetcher.get사진page());
			사진Fetcher.fetch등기부등본(item, src);

		} catch (Exception ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		}
		logger.info("등기부등본  URL을 얻어왔습니다.");
	}
}
