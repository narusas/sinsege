/*
 * 
 */
package net.narusas.aceauction.ui;

import java.io.IOException;
import java.util.Vector;

import net.narusas.aceauction.fetchers.���ǵ����_����Updater;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * The Class ����Task.
 */
public class ����Task implements Runnable {

	/** The info table model. */
	private final BeansTableModel infoTableModel;

	/** The ����. */
	private final ���� ����;

	/** The �������� model. */
	private final ��������ListModel ��������Model;

	/**
	 * Instantiates a new ���� task.
	 * 
	 * @param ���� the ����
	 * @param infoTableModel the info table model
	 * @param listModel the list model
	 */
	public ����Task(���� ����, BeansTableModel infoTableModel, ��������ListModel listModel) {
		this.���� = ����;
		this.infoTableModel = infoTableModel;
		this.��������Model = listModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		��������Model.clear();
		infoTableModel.clear();
		if (���� == null) {
			return;
		}
		try {

			if (����.getDetail() == null) {
				���ǵ����_����Updater.updateDetail(����, null);
			}

			infoTableModel.setBeans(����);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Vector<String> cmds = new Vector<String>();
		���ǵ���ǹ��ǻ󼼳��� detail = ����.getDetail();
		cmds.add("������");

		if (detail != null) {
			if (detail.get�ǹ���Ȳ() != null) {
				cmds.add("�ǹ���Ȳ");
			}

			if (detail.get��������Ȳ() != null) {
				cmds.add("��������Ȳ");
			}
			if (detail.get�Ű�������Ȳ() != null) {
				cmds.add("�Ű�������Ȳ");
			}
			if (detail.get������Ȳ() != null) {
				cmds.add("������Ȳ");
			}
			if (detail.get���ÿܰǹ���Ȳ() != null) {
				cmds.add("���ÿܰǹ���Ȳ");
			}
			if (detail.get���ⱸ() != null) {
				cmds.add("���ⱸ");
			}
		}

		if (����.get���ϳ���() != null) {
			cmds.add("����� ���ϳ���");
		}
		if (����.getHas����()) {
			cmds.add("�Ű����Ǹ���");
		}

		if (����.get����() != null && ����.get����().size() != 0) {
			cmds.add("����");
		}

		if (����.get������Ȳ() != null && ����.get������Ȳ().size() != 0) {
			cmds.add("�����������Ȳ");
		}

		if (����.get���ÿܰǹ�s() != null && ����.get���ÿܰǹ�s().size() != 0) {
			cmds.add("��������ÿܰǹ�");
		}
		��������Model.update(cmds);
	}

}
