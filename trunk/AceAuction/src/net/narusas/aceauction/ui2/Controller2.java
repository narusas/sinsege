package net.narusas.aceauction.ui2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import net.narusas.aceauction.data.updater.Updater2;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.ui.Command;
import net.narusas.aceauction.ui.법원ListModel;

public class Controller2 {
	static Logger logger = Logger.getLogger("log");

	private DefaultListModel chargeListModel;

	private BasicListModel sagunListModel;

	private final UpdaterUI2 ui;

	public Controller2(UpdaterUI2 ui) {
		this.ui = ui;
	}

	public void init() {
		ui.getCourtList().setModel(new 법원ListModel());
		ui.getCourtList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ui.getCourtList().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							return;
						}
						법원selected(법원.get(ui.getCourtList().getSelectedIndex()));
					}
				});

		ui.getChargeList()
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ui.getChargeList().setModel(getChargeListModel());
		ui.getChargeList().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							return;
						}
						담당계selected(ui.getChargeList().getSelectedIndex());
					}

				});

		ui.getSagunList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ui.getSagunList().setModel(getSagunListModel());
		ui.getSagunList().addListSelectionListener(
				new javax.swing.event.ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							return;
						}
						사건selected(ui.getSagunList().getSelectedIndex());
					}
				});

		ui.getWorkSagunButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				work사건();
			}
		});

		ui.getWorkChargeButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				work담당계();
			}
		});

		ui.getWorkCourtButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				work법원();
			}
		});

		ui.getWorkAllButton().addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				workAll();
			}
		});

		ui.setVisible(true);
		ui.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});

		logger.addHandler(new Handler() {

			@Override
			public void close() throws SecurityException {
			}

			@Override
			public void flush() {
			}

			@Override
			public void publish(LogRecord record) {
				String text = ui.getLogTextArea().getText();
				text += record.getMessage() + "\n";
				ui.getLogTextArea().setText(text);
				ui.getLogTextArea().setCaretPosition(text.length() - 1);
			}
		});

		Date start = new Date(System.currentTimeMillis()
				- (1000 * 60 * 60 * 24 * 20));
		ui.getStartYearTextField().setText(
				String.valueOf(start.getYear() + 1900));
		ui.getStartMonthTextField().setText(
				String.valueOf(start.getMonth() + 1));
		ui.getStartDayTextField().setText(String.valueOf(start.getDate()));

		Date end = new Date(System.currentTimeMillis());
		ui.getEndYearTextField().setText(String.valueOf(end.getYear() + 1900));
		ui.getEndMonthTextField().setText(String.valueOf(end.getMonth() + 1));
		ui.getEndDayTextField().setText(String.valueOf(end.getDate()));

	}

	private DefaultListModel getChargeListModel() {
		if (chargeListModel == null) {
			chargeListModel = new DefaultListModel();
		}
		return chargeListModel;
	}

	private Date getEndDate() {
		JTextField ytf = ui.getEndYearTextField();
		JTextField mtf = ui.getEndMonthTextField();
		JTextField dtf = ui.getEndDayTextField();

		return toDate(ytf, mtf, dtf);
	}

	private BasicListModel getSagunListModel() {
		if (sagunListModel == null) {
			sagunListModel = new BasicListModel();
		}
		return sagunListModel;
	}

	private Date getStartDate() {
		JTextField ytf = ui.getStartYearTextField();
		JTextField mtf = ui.getStartMonthTextField();
		JTextField dtf = ui.getStartDayTextField();

		return toDate(ytf, mtf, dtf);
	}

	private Date toDate(JTextField ytf, JTextField mtf, JTextField dtf) {
		int y = toInt(ytf.getText());
		int m = toInt(mtf.getText());
		int d = toInt(dtf.getText());

		return new Date(y - 1900, m - 1, d);
	}

	protected void workAll() {
		Date start = getStartDate();
		Date end = getEndDate();
		List<법원> list = new LinkedList<법원>();
		for (int i = 0; i < 법원.size(); i++) {
			list.add(법원.get(i));
		}
		Updater2 updater2 = new Updater2(list, start, end);
		new Thread(updater2).start();
	}

	protected void work담당계() {
		Updater2 updater2 = new Updater2((담당계) ui.getChargeList()
				.getSelectedValue());
		new Thread(updater2).start();
	}

	protected void work법원() {
		Date start = getStartDate();
		Date end = getEndDate();
		Updater2 updater2 = new Updater2((법원) ui.getCourtList()
				.getSelectedValue(), start, end);
		new Thread(updater2).start();
	}

	protected void work사건() {
		Updater2 updater2 = new Updater2((담당계) ui.getChargeList()
				.getSelectedValue(), (Long) ui.getSagunList()
				.getSelectedValue());
		new Thread(updater2).start();
	}

	protected void 담당계selected(int selectedIndex) {
		if (selectedIndex == -1) {
			return;
		}
		getSagunListModel().clear();
		Command cmd = new Command(new 사건_결과FetchTask(getSagunListModel(),
				(담당계) getChargeListModel().get(selectedIndex)), 20);
		cmd.start();
		ui.getSagunList().updateUI();
	}

	protected void 법원selected(법원 court) {
		getChargeListModel().clear();
		getSagunListModel().clear();
		Command cmd = new Command(new 담당계_결과FetchTask(court,
				getChargeListModel()), 6);
		cmd.start();
	}

	protected void 사건selected(int selectedIndex) {

	}

	int toInt(String src) {
		return Integer.parseInt(src);
	}

}
