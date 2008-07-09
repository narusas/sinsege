/*
 * 
 */
package net.narusas.aceauction.ui2;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.updater.사건_결과FetchCommand;
import net.narusas.aceauction.model.담당계;

// TODO: Auto-generated Javadoc
/**
 * The Class 사건_결과FetchTask.
 */
public class 사건_결과FetchTask implements Runnable {

	/** The sagun list model. */
	private final BasicListModel sagunListModel;

	/** The 담당계. */
	private final 담당계 담당계;
	
	/** The res. */
	List<String> res = new LinkedList<String>();

	/**
	 * Instantiates a new 사건_결과 fetch task.
	 * 
	 * @param sagunListModel the sagun list model
	 * @param 담당계 the 담당계
	 */
	public 사건_결과FetchTask(BasicListModel sagunListModel, 담당계 담당계) {
		this.sagunListModel = sagunListModel;
		this.담당계 = 담당계;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			new 사건_결과FetchCommand(담당계, res).run();

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
