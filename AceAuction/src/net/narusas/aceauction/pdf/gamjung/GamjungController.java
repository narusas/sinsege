package net.narusas.aceauction.pdf.gamjung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.builder.감정평가서Builder;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.gamjung.GamjungParser.Group;

public class GamjungController {
	private Group current;
	private List<Group> groups;
	private int pos;
	private 사건 s;
	private final GamjungUI ui;

	public GamjungController(GamjungUI ui) {
		this.ui = ui;
		init();
	}

	public void setGamjungs(사건 s, List<Group> groups, final String 기관, final String 시점) {
		this.s = s;
		ui.setTitle("사건번호:" + s.get사건번호());
		this.groups = groups;
		pos = 0;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ui.get기관().setText(기관);
				ui.get시점().setText(시점);
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

	private void setup(final Group group) {
		current = group;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ui.tableNameLabel.setText(current.name);
				ui.item1TextArea.setText(group.get위치및교통());
				ui.item2TextArea.setText(group.get이용상태());
				ui.item3TextArea.setText(group.get토지());
				ui.item4TextArea.setText(group.get도로관련());
				ui.item5TextArea.setText(group.get냉난방());
			}
		});
	}

	private void updateCount() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int size = groups == null ? 0 : groups.size();
				ui.tableCountLabel.setText((pos + 1) + "/" + size);
			}
		});

	}

	protected void insert() {
		감정평가서Builder db = new 감정평가서Builder();
		String 위치및교통 = ui.item1TextArea.getText();
		String 이용상태 = ui.item2TextArea.getText();
		String 토지 = ui.item3TextArea.getText();
		String 도로관련 = ui.item4TextArea.getText();
		String 냉난방 = ui.item5TextArea.getText();
		String 기관 = ui.get기관().getText();
		String 시점 = ui.get시점().getText();
		db.insert(s, 기관, 시점, 위치및교통, 이용상태, 토지, 도로관련, 냉난방);
	}

	protected void next() {
		pos++;
		if (pos >= groups.size()) {
			pos = 0;
		}
		current = groups.get(pos);
		setup(current);
	}

	protected void prev() {
		pos--;
		if (pos < 0) {
			pos = groups.size() - 1;
		}
		current = groups.get(pos);
		setup(current);
	}
}
