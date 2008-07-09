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
import net.narusas.aceauction.model.����;

import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * ������ ���� DB�� �Է��ϴ� ���α׷��� Builder�� ���� ����Ʈ.
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

		public void update����Size(int chargeSize) {
		}

		public void update����Size(int mulgunSize) {
		}

		public void update���Size(int sagunSize) {
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

	/** The ����. */
	private final ���� ����;

	/**
	 * Instantiates a new builder.
	 * 
	 * @param ���� the ����
	 * @param listener the listener
	 * @param startD the start d
	 * @param endD the end d
	 * @param eventNos the event nos
	 */
	public Builder(���� ����, BuildProgressListener listener, Date startD,
			Date endD, Long[] eventNos) {
		this.���� = ����;
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

		logger.info(����.getName() + "�� ���� ������ �����մϴ�. ");
		����Builder db = new ����Builder(����, listener, startDate, endDate, eventNos);
		db.build����s();
		logger.info(����.getName() + "�� ���� ������ �����մϴ�. ");
		listener.progress(BuildProgressListener.LEVEL_����);
	}
}
