/*
 * 
 */
package net.narusas.aceauction.pdf.gamjung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.builder.�����򰡼�Builder;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungParser.Group;

// TODO: Auto-generated Javadoc
/**
 * The Class GamjungController.
 */
public class GamjungController {
	
	/** The current. */
	private Group current;
	
	/** The groups. */
	private List<Group> groups;
	
	/** The pos. */
	private int pos;
	
	/** The s. */
	private ��� s;
	
	/** The ui. */
	private final GamjungUI ui;

	/**
	 * Instantiates a new gamjung controller.
	 * 
	 * @param ui the ui
	 */
	public GamjungController(GamjungUI ui) {
		this.ui = ui;
		init();
	}

	/**
	 * Sets the gamjungs.
	 * 
	 * @param s the s
	 * @param groups the groups
	 * @param ��� the ���
	 * @param ���� the ����
	 */
	public void setGamjungs(��� s, List<Group> groups, final String ���, final String ����) {
		this.s = s;
		ui.setTitle("��ǹ�ȣ:" + s.get��ǹ�ȣ());
		this.groups = groups;
		pos = 0;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ui.get���().setText(���);
				ui.get����().setText(����);
			}
		});

		updateCount();
		if (groups == null) {
			return;
		}
		if (groups.size() > 0) {
			setup(groups.get(0));
		}
	}

	/**
	 * Clear contents.
	 */
	private void clearContents() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ui.item1TextArea.setText("");
				ui.item2TextArea.setText("");
				ui.item3TextArea.setText("");
				ui.item4TextArea.setText("");
				ui.item5TextArea.setText("");

				ui.tableCountLabel.setText("0/0");
				ui.tableNameLabel.setText("");
			}
		});

	}

	/**
	 * Inits the.
	 */
	private void init() {
		ui.insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				insert();

			}
		});
		ui.nextTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				next();
			}
		});
		ui.prevTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				prev();
			}
		});
		ui.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				ui.dispose();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		clearContents();
	}

	/**
	 * Sets the up.
	 * 
	 * @param group the new up
	 */
	private void setup(final Group group) {
		current = group;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ui.tableNameLabel.setText(current.name);
				ui.item1TextArea.setText(group.get��ġ�ױ���());
				ui.item2TextArea.setText(group.get�̿����());
				ui.item3TextArea.setText(group.get����());
				ui.item4TextArea.setText(group.get���ΰ���());
				ui.item5TextArea.setText(group.get�ó���());
			}
		});
	}

	/**
	 * Update count.
	 */
	private void updateCount() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int size = groups == null ? 0 : groups.size();
				ui.tableCountLabel.setText((pos + 1) + "/" + size);
			}
		});

	}

	/**
	 * Insert.
	 */
	protected void insert() {
		�����򰡼�Builder db = new �����򰡼�Builder();
		String ��ġ�ױ��� = ui.item1TextArea.getText();
		String �̿���� = ui.item2TextArea.getText();
		String ���� = ui.item3TextArea.getText();
		String ���ΰ��� = ui.item4TextArea.getText();
		String �ó��� = ui.item5TextArea.getText();
		String ��� = ui.get���().getText();
		String ���� = ui.get����().getText();
		db.insert(s, ���, ����, ��ġ�ױ���, �̿����, ����, ���ΰ���, �ó���);
	}

	/**
	 * Next.
	 */
	protected void next() {
		pos++;
		if (pos >= groups.size()) {
			pos = 0;
		}
		current = groups.get(pos);
		setup(current);
	}

	/**
	 * Prev.
	 */
	protected void prev() {
		pos--;
		if (pos < 0) {
			pos = groups.size() - 1;
		}
		current = groups.get(pos);
		setup(current);
	}
}
