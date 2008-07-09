/*
 * 
 */
package net.narusas.aceauction.ui2;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.updater.���_���FetchCommand;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class ���_���FetchTask.
 */
public class ���_���FetchTask implements Runnable {

	/** The sagun list model. */
	private final BasicListModel sagunListModel;

	/** The ����. */
	private final ���� ����;
	
	/** The res. */
	List<String> res = new LinkedList<String>();

	/**
	 * Instantiates a new ���_��� fetch task.
	 * 
	 * @param sagunListModel the sagun list model
	 * @param ���� the ����
	 */
	public ���_���FetchTask(BasicListModel sagunListModel, ���� ����) {
		this.sagunListModel = sagunListModel;
		this.���� = ����;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			new ���_���FetchCommand(����, res).run();

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (String sa_no : res) {
						sagunListModel.addElement(sa_no);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
