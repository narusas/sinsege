/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.fetchers.담당계Fetcher;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * 지정된 법원에 대한 담당계 목록을 구성하는 빌더.
 * 
 * @author narusas
 */
public class 담당계Builder {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The end date. */
	private final Date endDate;

	/** The event nos. */
	private final Long[] eventNos;

	/** The listener. */
	private final BuildProgressListener listener;

	/** The start date. */
	private final Date startDate;

	/** The 법원. */
	private final 법원 법원;

	/**
	 * Instantiates a new 담당계 builder.
	 * 
	 * @param 법원 the 법원
	 * @param listener the listener
	 * @param startD the start d
	 * @param endD the end d
	 * @param eventNos the event nos
	 */
	담당계Builder(법원 법원, BuildProgressListener listener, Date startD, Date endD,
			Long[] eventNos) {
		this.법원 = 법원;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

	/**
	 * Build담당계.
	 * 
	 * @param 담당계 the 담당계
	 * 
	 * @throws Exception the exception
	 */
	private void build담당계(담당계 담당계) throws Exception {
		if (담당계.isInScoop(startDate, endDate) == false) {
			logger.info("담당계가 작업 날자 범위안에 포함되지 않습니다." + 담당계.get매각기일());
			listener.progress(BuildProgressListener.LEVEL_담당계);
			return;
		}
		사건Builder db = new 사건Builder(listener);
		db.build사건s(담당계);
		담당계.update();
		db.update사건DB(담당계.getNo(), 담당계.getSagun(), eventNos);
		listener.progress(BuildProgressListener.LEVEL_담당계);
	}

	/**
	 * Build담당계s.
	 * 
	 * @throws ParserException the parser exception
	 * @throws SQLException the SQL exception
	 * @throws TransformerException the transformer exception
	 */
	void build담당계s() throws ParserException, SQLException, TransformerException {
		List<담당계> list = null;
		try {
			logger.info("담당계 목록을 얻어옵니다. ");
			담당계Fetcher fetcher = 담당계Fetcher.get담당계Fetcher_경매진행(법원);
			list = fetcher.fetchCharges();
			listener.update담당계Size(list.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (list == null) {
			return;
		}
		while (list.size() > 0) {
			try {
				logger.info("남은 담당계의 수 :" + list.size());
				담당계 charge = list.remove(list.size() - 1);
				build담당계(charge);
			} catch (Throwable e) {
				logger.log(Level.FINE, "담당계 갱신중", e);
				e.printStackTrace();
			}
		}

	}
}
