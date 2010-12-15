package net.narusas.aceauction.data.builder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.����;

import org.htmlparser.util.ParserException;

/**
 * ������ ���� DB�� �Է��ϴ� ���α׷��� Builder�� ���� ����Ʈ.
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

		public void update����Size(int chargeSize) {
		}

		public void update����Size(int mulgunSize) {
		}

		public void update���Size(int sagunSize) {
		}
	};

	private final Date endDate;

	private final Long[] eventNos;

	private BuildProgressListener listener = dymmuListener;

	private final Date startDate;

	private final ���� ����;

	public Builder(���� ����, BuildProgressListener listener, Date startD,
			Date endD, Long[] eventNos) {
		this.���� = ����;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

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
