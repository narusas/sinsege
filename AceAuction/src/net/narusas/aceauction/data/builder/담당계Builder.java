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

/**
 * 지정된 법원에 대한 담당계 목록을 구성하는 빌더.
 * 
 * @author narusas
 * 
 */
public class 담당계Builder {

	static Logger logger = Logger.getLogger("log");

	private final Date endDate;

	private final Long[] eventNos;

	private final BuildProgressListener listener;

	private final Date startDate;

	private final 법원 법원;

	담당계Builder(법원 법원, BuildProgressListener listener, Date startD, Date endD,
			Long[] eventNos) {
		this.법원 = 법원;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

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
