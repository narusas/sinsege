package net.narusas.aceauction.ui2;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.updater.���_���FetchCommand;
import net.narusas.aceauction.model.����;

public class ���_���FetchTask implements Runnable {

	private final BasicListModel sagunListModel;

	private final ���� ����;
	List<String> res = new LinkedList<String>();

	public ���_���FetchTask(BasicListModel sagunListModel, ���� ����) {
		this.sagunListModel = sagunListModel;
		this.���� = ����;
	}

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
