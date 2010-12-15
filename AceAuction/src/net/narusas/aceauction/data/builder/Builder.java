package net.narusas.aceauction.data.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.법원;

import org.htmlparser.util.ParserException;

/**
 * 정보를 얻어와 DB에 입력하는 프로그램인 Builder의 시작 포인트.
 * 
 * @author narusas
 * 
 */
public class Builder extends DB {
	static Logger logger = Logger.getLogger("log");

	private BuildProgressListener dymmuListener = new BuildProgressListener() {

		public void log(String message) {
		}

		public void progress(int level) {
		}

		public void update담당계Size(int chargeSize) {
		}

		public void update물건Size(int mulgunSize) {
		}

		public void update사건Size(int sagunSize) {
		}
	};

	private final Date endDate;

	private final Long[] eventNos;

	private BuildProgressListener listener = dymmuListener;

	private final Date startDate;

	private final 법원 법원;

	public Builder(법원 법원, BuildProgressListener listener, Date startD,
			Date endD, Long[] eventNos) {
		this.법원 = 법원;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

	public void build() throws ParserException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			TransformerException, IOException {
		DB.dbConnect();

		logger.info(법원.getName() + "에 대해 빌딩을 시작합니다. ");
		담당계Builder db = new 담당계Builder(법원, listener, startDate, endDate, eventNos);
		db.build담당계s();
		logger.info(법원.getName() + "에 대한 빌딩을 종료합니다. ");
		listener.progress(BuildProgressListener.LEVEL_법원);
	}
}
