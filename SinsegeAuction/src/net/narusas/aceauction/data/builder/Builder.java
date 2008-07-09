/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.법원;

import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * 정보를 얻어와 DB에 입력하는 프로그램인 Builder의 시작 포인트.
 * 
 * @author narusas
 */
public class Builder extends DB {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The dymmu listener. */
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

	/** The end date. */
	private final Date endDate;

	/** The event nos. */
	private final Long[] eventNos;

	/** The listener. */
	private BuildProgressListener listener = dymmuListener;

	/** The start date. */
	private final Date startDate;

	/** The 법원. */
	private final 법원 법원;

	/**
	 * Instantiates a new builder.
	 * 
	 * @param 법원 the 법원
	 * @param listener the listener
	 * @param startD the start d
	 * @param endD the end d
	 * @param eventNos the event nos
	 */
	public Builder(법원 법원, BuildProgressListener listener, Date startD,
			Date endD, Long[] eventNos) {
		this.법원 = 법원;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

	/**
	 * Builds the.
	 * 
	 * @throws ParserException the parser exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SQLException the SQL exception
	 * @throws TransformerException the transformer exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
