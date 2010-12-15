package net.narusas.aceauction.data.builder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.fetchers.����Fetcher;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.htmlparser.util.ParserException;

/**
 * ������ ������ ���� ���� ����� �����ϴ� ����.
 * 
 * @author narusas
 * 
 */
public class ����Builder {

	static Logger logger = Logger.getLogger("log");

	private final Date endDate;

	private final Long[] eventNos;

	private final BuildProgressListener listener;

	private final Date startDate;

	private final ���� ����;

	����Builder(���� ����, BuildProgressListener listener, Date startD, Date endD,
			Long[] eventNos) {
		this.���� = ����;
		this.listener = listener;
		this.startDate = startD;
		this.endDate = endD;
		this.eventNos = eventNos;
	}

	private void build����(���� ����) throws Exception {
		if (����.isInScoop(startDate, endDate) == false) {
			logger.info("���谡 �۾� ���� �����ȿ� ���Ե��� �ʽ��ϴ�." + ����.get�Ű�����());
			listener.progress(BuildProgressListener.LEVEL_����);
			return;
		}
		���Builder db = new ���Builder(listener);
		db.build���s(����);
		����.update();
		db.update���DB(����.getNo(), ����.getSagun(), eventNos);
		listener.progress(BuildProgressListener.LEVEL_����);
	}

	void build����s() throws ParserException, SQLException, TransformerException {
		List<����> list = null;
		try {
			logger.info("���� ����� ���ɴϴ�. ");
			����Fetcher fetcher = ����Fetcher.get����Fetcher_�������(����);
			list = fetcher.fetchCharges();
			listener.update����Size(list.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (list == null) {
			return;
		}
		while (list.size() > 0) {
			try {
				logger.info("���� ������ �� :" + list.size());
				���� charge = list.remove(list.size() - 1);
				build����(charge);
			} catch (Throwable e) {
				logger.log(Level.FINE, "���� ������", e);
				e.printStackTrace();
			}
		}

	}
}
