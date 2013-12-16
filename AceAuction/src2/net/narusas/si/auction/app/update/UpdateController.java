package net.narusas.si.auction.app.update;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.narusas.si.auction.app.build.BuildController;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.updater.물건Batch;
import net.narusas.si.auction.updater.법원Updater;
import net.narusas.si.auction.updater.사건Updater;

import org.apache.commons.lang.StringUtils;

public class UpdateController extends  BuildController {

	private JTextField startYear;
	private JTextField startMonth;
	private JTextField startDay;
	private JTextField endYear;
	private JTextField endMonth;
	private JTextField endDay;
	private JCheckBox doneCheckbox;
	private JComboBox typeCombo;
	private JButton 지정물건실행Button;

	public void enableControl(boolean b) {
		super.enableControl(b);
		startYear.setEditable(b);
		startMonth.setEditable(b);
		startDay.setEditable(b);
		endYear.setEditable(b);
		endMonth.setEditable(b);
		endDay.setEditable(b);
	}
	
	public void set지정물건실행Button(JButton btn) {

		this.지정물건실행Button = btn;
		this.지정물건실행Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("#######");
				final JFrame frame = new JFrame();
				frame.setSize(200, 400);
				frame.getContentPane().setLayout(new BorderLayout());
				final JTextArea textArea = new JTextArea();
				JScrollPane scrolPane = new JScrollPane(textArea);
				frame.getContentPane().add(scrolPane, BorderLayout.CENTER);
				JButton btn = new JButton("실행");
				frame.getContentPane().add(btn, BorderLayout.SOUTH);
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						frame.setVisible(false);
						String t = textArea.getText();
						System.out.println(t);
						if (t != null) {
							t = t.trim();
						}
						String[] tokens = t.split("\n");
						final List<Integer> ids = new ArrayList<Integer>();
						for (String token : tokens) {
							token = token.trim();
							if (StringUtils.isEmpty(token)){
								continue;
							}
							ids.add(Integer.parseInt(token.trim()));
						}
						new Thread() {
							public void run() {
								 물건Batch batch = new 물건Batch (ids);
								 batch.execute();
							}
						}.start();
						System.out.println("gogogo");
					}
				});
				frame.setVisible(true);
			}
		});
	}

	@Override
	public void set단일실행Button(JButton 단일실행Btn) {
		this.단일실행Btn = 단일실행Btn;
		단일실행Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				사건단일실행();
			}
		});
	}

	@Override
	public void set선택실행Button(JButton 선택실행Btn) {
		this.선택실행Btn = 선택실행Btn;
		선택실행Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				법원선택실행();
			}
		});
	}

	protected Date parseDate(JTextField yearF, JTextField monthF, JTextField dayF) {
		if (isNull(yearF) || isNull(monthF) | isNull(dayF)) {
			return null;
		}
		int year = Integer.parseInt(yearF.getText().trim());
		int month = Integer.parseInt(monthF.getText().trim());
		int day = Integer.parseInt(dayF.getText().trim());
		return new Date(year - 1900, month - 1, day);
	}

	private boolean isNull(JTextField textField) {
		return textField == null || textField.getText() == null || textField.getText().trim().length() == 0;
	}

	@Override
	public void set전체실행Button(JButton 전체실행Btn) {
		this.전체실행Btn = 전체실행Btn;
	}

	@Override
	public void set기간TextFields(JTextField startYear, JTextField startMonth, JTextField startDay, JTextField endYear,
			JTextField endMonth, JTextField endDay) {
		this.startYear = startYear;
		this.startMonth = startMonth;
		this.startDay = startDay;
		this.endYear = endYear;
		this.endMonth = endMonth;
		this.endDay = endDay;
	}

	private void 법원선택실행() {
		new Thread() {
			@Override
			public void run() {
				Object[] values = 법원List.getSelectedValues();
				if (values == null || values.length == 0) {
					logger.info("선택된 법원이 없습니다. ");
					return;
				}
				Date start = null, end = null;
				try {
					start = parseDate(startYear, startMonth, startDay);
					end = parseDate(endYear, endMonth, endDay);
				} catch (Exception ex) {
					logger.info("날자가 잘못 입력되었습니다.");
					return;
				}

				for (Object obj : values) {

					try {
						new 법원Updater((법원) obj, start, end, doneCheckbox.isSelected(), getType(), false).run();
					} catch (Exception e) {
					}
				}
			}
		}.start();

	}

	private void 사건단일실행() {
		Object[] values = 법원List.getSelectedValues();
		if (values == null || values.length == 0) {
			logger.info("선택된 법원이 없습니다. ");
			return;
		}
		법원 court = (법원) values[0];
		String eventYear = 단일사건YearTextField.getText();
		String eventNo = 단일사건NoTextField.getText();
		if (eventNo == null || "".equals(eventNo.trim()) || eventYear == null || "".equals(eventYear.trim())) {
			return;
		}
		int eventYearValue = Integer.parseInt(eventYear);
		int eventNoValue = Integer.parseInt(eventNo);

		final long 사건번호 = Long.parseLong(MessageFormat.format("{0,number,0000}013{1,number,0000000}", eventYearValue,
				eventNoValue));
		new 사건Updater(court, 사건번호, doneCheckbox.isSelected(), getType()).start();
	}
	
	String getType() {
		return (String) typeCombo.getSelectedItem();
	}

	public void setDoneCheckbox(JCheckBox doneCheckbox) {
		this.doneCheckbox = doneCheckbox;
		doneCheckbox.setSelected(true);

	}
	
	public void set종류Combo(JComboBox typeCombo) {
		this.typeCombo = typeCombo;
		typeCombo.setModel(new DefaultComboBoxModel(new Object[]{"전체", "유찰","변경"}));
	}

}
