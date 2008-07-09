/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.��������Fetcher_���;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class ���_���FetchCommand.
 */
public class ���_���FetchCommand implements Runnable {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The sa_nos. */
	private final List<String> sa_nos;

	/** The ����. */
	private final ���� ����;

	/**
	 * Instantiates a new ���_��� fetch command.
	 * 
	 * @param ���� the ����
	 * @param sa_nos the sa_nos
	 */
	public ���_���FetchCommand(���� ����, List<String> sa_nos) {
		this.���� = ����;
		this.sa_nos = sa_nos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			logger.info("��� Fetcher�� �����մϴ�. ");
			��������Fetcher_��� fetcher = new ��������Fetcher_���(����.get����(), ����);
			logger.info("����� Fetcher�մϴ�.  ");
			String[] pages = fetcher.getPages();
			logger.info("������ Page���� ����� �м��մϴ�. ");
			for (String page : pages) {
				List<String> temp = fetcher.parse(page);
				for (String sa_no : temp) {
					sa_nos.add(sa_no);
				}
			}

		} catch (Exception ex) {
			logger.log(Level.INFO, "����� �������µ� �����߽��ϴ�", ex);
			ex.printStackTrace();

		}
	}

}
