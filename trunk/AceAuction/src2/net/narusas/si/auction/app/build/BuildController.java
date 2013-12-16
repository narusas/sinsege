package net.narusas.si.auction.app.build;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.narusas.si.auction.app.Controller;
import net.narusas.si.auction.app.ui.담당계ListModel;
import net.narusas.si.auction.app.ui.법원ListModel;
import net.narusas.si.auction.builder.Mode;
import net.narusas.si.auction.builder.경매공매등기부등본Batch;
import net.narusas.si.auction.builder.담당계Batch;
import net.narusas.si.auction.builder.담당계목록Batch;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildController implements Controller {
	public final Logger logger = LoggerFactory.getLogger("auction");
	protected JList 법원List;
	protected JList 담당계List;
	protected 담당계ListModel a담당계ListModel;
	protected JButton 전체실행Btn;
	protected JButton 선택실행Btn;
	protected JButton 단일실행Btn;
	protected JTextField 단일사건YearTextField;
	protected JButton clearLogBtn;
	protected JTextArea logTextArea;
	protected JTextField 단일사건NoTextField;
	protected JRadioButton 신건RadioBtn;
	protected JRadioButton 매각물건명세서RadioBtn;
	protected JRadioButton 경매결과RadioBtn;
	private JRadioButton 등기부등본RadioBtn;
	private JTextField 여기부터YearFilter;
	private JTextField 여기부터NoFilter;
	private JButton 지정물건실행Button;

	public void set법원List(JList list) {
		this.법원List = list;
		법원List.setModel(new 법원ListModel(법원.법원목록));
		법원List.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		법원List.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected법원s = 법원List.getSelectedValues();
				if (selected법원s == null || selected법원s.length == 0) {
					a담당계ListModel.clear();
					return;
				}

				new Thread() {
					@Override
					public void run() {
						if (a담당계ListModel == null) {
							return;
						}
						a담당계ListModel.clear();
						ArrayList<담당계> temp = new ArrayList<담당계>();
						for (Object object : selected법원s) {
							법원 court = (법원) object;
							List<담당계> charges = court.getWorkSet();
							if (charges == null) {
								continue;
							}
							temp.addAll(charges);
						}
						sort(temp);
						for (담당계 c : temp) {
							add(c);
						}

					}

					private void add(final 담당계 charge) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								a담당계ListModel.addElement(charge);
							}
						});
					}
				}.start();

				System.out.println();
			}
		});
	}

	private void sort(List<담당계> charges) {
		Collections.sort(charges, new Comparator<담당계>() {
			@Override
			public int compare(담당계 o1, 담당계 o2) {
				return o1.get매각기일().compareTo(o2.get매각기일());
			}
		});
	}

	public void set담당계List(JList list) {
		담당계List = list;
		a담당계ListModel = new 담당계ListModel();
		담당계List.setModel(a담당계ListModel);
	}

	public void set전체실행Button(JButton btn) {
		전체실행Btn = btn;
	}

	public void set선택실행Button(JButton btn) {
		선택실행Btn = btn;
		선택실행Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] values = 담당계List.getSelectedValues();
				if (values.length == 0) {
					return;
				}

				final List<담당계> workset = new LinkedList<담당계>();
				synchronized (a담당계ListModel) {
					for (Object object : values) {
						workset.add((담당계) object);
					}
				}
				if (BuildApp.mode == Mode.등기부등본) {

					start경매공매등기부등본(workset);
					return;
				}

				new Thread() {
					@Override
					public void run() {
						String eventYear = 여기부터YearFilter.getText();
						String eventNo = 여기부터NoFilter.getText();
						if (eventNo == null || "".equals(eventNo.trim()) || eventYear == null || "".equals(eventYear.trim())) {
							new 담당계목록Batch(workset).execute();
							return;
						}
						int eventYearValue = Integer.parseInt(eventYear);
						int eventNoValue = Integer.parseInt(eventNo);

						final long 여기부터사건번호 = Long.parseLong(MessageFormat.format("{0,number,0000}013{1,number,0000000}", eventYearValue,
								eventNoValue));

						new 담당계목록Batch(workset, 여기부터사건번호).execute();
					}
				}.start();

			}

		});
	}

	public void set단일실행Button(JButton btn) {
		단일실행Btn = btn;
		단일실행Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object[] values = 담당계List.getSelectedValues();
				if (values.length == 0) {
					return;
				}
				String eventYear = 단일사건YearTextField.getText();
				String eventNo = 단일사건NoTextField.getText();
				if (eventNo == null || "".equals(eventNo.trim()) || eventYear == null || "".equals(eventYear.trim())) {
					return;
				}
				int eventYearValue = Integer.parseInt(eventYear);
				int eventNoValue = Integer.parseInt(eventNo);

				final long 사건번호 = Long.parseLong(MessageFormat.format("{0,number,0000}013{1,number,0000000}", eventYearValue, eventNoValue));

				final 담당계 charge = (담당계) values[0];
				new Thread() {
					@Override
					public void run() {
						new 담당계Batch(charge, 사건번호).execute();
					}
				}.start();

			}
		});
	}

	public void set단일사건TextField(JTextField yearField, JTextField noField) {
		단일사건YearTextField = yearField;
		단일사건NoTextField = noField;
	}

	public void set여기부터TextField(JTextField 여기부터YearFilter, JTextField 여기부터NoFilter) {
		this.여기부터YearFilter = 여기부터YearFilter;
		this.여기부터NoFilter = 여기부터NoFilter;

	}

	public void enableControl(boolean b) {
		전체실행Btn.setEnabled(b);
		선택실행Btn.setEnabled(b);
		단일실행Btn.setEnabled(b);
		단일사건YearTextField.setEnabled(b);
		단일사건NoTextField.setEnabled(b);
	}

	public void setClearLogButton(JButton clearLogBtn) {
		this.clearLogBtn = clearLogBtn;
		clearLogBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (logTextArea == null) {
					return;
				}
				logTextArea.setText("");
			}
		});

	}

	public void setLogTetArea(JTextArea logTextArea) {
		this.logTextArea = logTextArea;
	}

	public void set신건RadioBtn(JRadioButton 신건RadioBtn) {
		this.신건RadioBtn = 신건RadioBtn;
		this.신건RadioBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BuildApp.mode = Mode.신건;
			}
		});
		신건RadioBtn.setSelected(true);
	}

	public void set매각물건명세서RadioBtn(JRadioButton 매각물건명세서RadioBtn) {
		this.매각물건명세서RadioBtn = 매각물건명세서RadioBtn;
		this.매각물건명세서RadioBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BuildApp.mode = Mode.매각물건명세서;
			}
		});
	}

	public void set기간TextFields(JTextField startYear, JTextField startMonth, JTextField startDay, JTextField endYear, JTextField endMonth,
			JTextField endDay) {

	}

	public void set등기부등본RadioBtn(JRadioButton 등기부등본RadioBtn) {
		this.등기부등본RadioBtn = 등기부등본RadioBtn;
		등기부등본RadioBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BuildApp.mode = Mode.등기부등본;
			}
		});
	}

	private void start경매공매등기부등본(List<담당계> workset) {
		final Object[] selected법원s = 법원List.getSelectedValues();
		if (selected법원s == null || selected법원s.length == 0) {
			return;
		}

		new 경매공매등기부등본Batch((법원) selected법원s[0], workset).start();
	}

	

}
