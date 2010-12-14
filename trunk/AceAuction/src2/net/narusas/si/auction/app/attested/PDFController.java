package net.narusas.si.auction.app.attested;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.Controller;
import net.narusas.si.auction.app.ui.담당계ListModel;
import net.narusas.si.auction.app.ui.목록ListModel;
import net.narusas.si.auction.app.ui.물건ListModel;
import net.narusas.si.auction.app.ui.법원ListModel;
import net.narusas.si.auction.app.ui.사건ListModel;
import net.narusas.si.auction.builder.물건목록Batch;
import net.narusas.si.auction.fetchers.사건내역Fetcher;
import net.narusas.si.auction.fetchers.사건목록Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.목록;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.사건Dao;

public class PDFController implements Controller {

	private JList 법원List;
	private JList 담당계List;
	private 담당계ListModel 담당계ListModel;
	private JList 사건List;
	private 사건ListModel 사건ListModel;
	protected final Logger logger = LoggerFactory.getLogger("auction");
	private JList 물건List;
	private JList 목록List;
	private 물건ListModel 물건ListModel;
	private 목록ListModel 목록ListModel;
	private 목록 selected목록;
	private JLabel 목록구분Label;
	private JLabel 목록주소Label;
	private JLabel 목록비고Label;
	private JButton 실행버튼;
	private JLabel 상태Label;

	AtestedMonitor monitor = new AtestedMonitor();
	private JFrame frame;
	Browser browser = new Browser();

	@Override
	public void enableControl(boolean b) {

	}

	public void set법원List(JList list) {
		this.법원List = list;
		법원List.setModel(new 법원ListModel(법원.법원목록));
		법원List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		법원List.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected법원s = 법원List.getSelectedValues();
				if (selected법원s == null || selected법원s.length == 0) {
					담당계ListModel.clear();
					return;
				}

				new Thread() {
					@Override
					public void run() {
						if (담당계ListModel == null) {
							return;
						}

						담당계ListModel.clear();
						사건ListModel.clear();
						물건ListModel.clear();
						목록ListModel.clear();
						select목록(null);
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

					private void add(final 담당계 sagun) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								담당계ListModel.addElement(sagun);
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
		담당계ListModel = new 담당계ListModel();
		담당계List.setModel(담당계ListModel);
		담당계List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		담당계List.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected담당계s = 담당계List.getSelectedValues();
				if (selected담당계s == null || selected담당계s.length == 0) {
					담당계ListModel.clear();
					return;
				}

				new Thread() {
					@Override
					public void run() {
						if (담당계ListModel == null) {
							return;
						}
						select목록(null);
						목록ListModel.clear();
						물건ListModel.clear();
						사건ListModel.clear();

						담당계 charge = (담당계) selected담당계s[0];
						logger.info(charge.get담당계이름()
								+ "가 선택되었습니다. 사건목록을 얻어옵니다. 잠시만 기다리세요. ");
						사건목록Fetcher fetcher = new 사건목록Fetcher();
						사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
						try {
							List<사건> 사건목록 = fetcher.fetchAll(charge);
							List<사건> stored사건목록 = eventDao.findBy(charge);
							System.out.println("#### 신건목록.");
							System.out.println(사건목록);
							for (사건 event : 사건목록) {
								if (event.isIs신건() == false) {
									continue;
								}
								for (사건 storedEvent : stored사건목록) {
									if (event.get사건번호() == storedEvent
											.get사건번호()) {

										add(storedEvent);
										continue;
									}
								}
							}
							logger.info("사건목록을 얻어왔습니다. 다음 작업을 진행하세요. ");

						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					private void add(final 사건 sagun) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								사건ListModel.addElement(sagun);
							}
						});
					}

				}.start();
			}
		});
	}

	public void set사건List(JList list) {
		사건List = list;
		사건ListModel = new 사건ListModel();
		사건List.setModel(사건ListModel);
		사건List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		사건List.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected사건s = 사건List.getSelectedValues();
				if (selected사건s == null || selected사건s.length == 0) {
					사건ListModel.clear();
					return;
				}
				new Thread() {
					@Override
					public void run() {
						if (사건ListModel == null) {
							return;
						}
						물건ListModel.clear();
						목록ListModel.clear();
						select목록(null);
						logger.info("사건상세를 얻어옵니다. 잠시만 기다리세요.");
						사건 사건 = (사건) selected사건s[0];
						사건내역Fetcher f1 = new 사건내역Fetcher();
						try {
							f1.update(사건, false);
							List<물건> 물건s = 사건.get물건목록();
							for (final 물건 물건 : 물건s) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										물건ListModel.addElement(물건);
									}
								});
							}
							logger.info("사건상세를 얻어왔습니다. 다음 작업을 진행하세요. ");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}

		});

	}

	public void set물건List(JList list) {
		물건List = list;
		물건ListModel = new 물건ListModel();
		물건List.setModel(물건ListModel);
		물건List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		물건List.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected물건s = 물건List.getSelectedValues();
				if (selected물건s == null || selected물건s.length == 0) {
					return;
				}
				new Thread() {
					@Override
					public void run() {
						if (목록ListModel == null) {
							return;
						}
						목록ListModel.clear();
						select목록(null);
						물건 물건 = (물건) selected물건s[0];

						ArrayList<목록> 목록s = 물건.get목록s();
						if (목록s == null) {
							return;
						}
						for (final 목록 목록 : 목록s) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									목록ListModel.addElement(목록);
								}
							});
						}

					}
				}.start();
			}
		});

	}

	public void set목록List(JList list) {
		목록List = list;
		목록ListModel = new 목록ListModel();
		목록List.setModel(목록ListModel);
		목록List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		목록List.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				final Object[] selected목록s = 목록List.getSelectedValues();
				if (selected목록s == null || selected목록s.length == 0) {
					select목록(null);
					return;
				}
				목록 목록 = (목록) selected목록s[0];
				select목록(목록);

			}
		});
	}

	protected void select목록(목록 목록) {
		selected목록 = 목록;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (selected목록 == null) {
					목록주소Label.setText("");
					목록구분Label.setText("");
					목록비고Label.setText("");
					return;
				}
				목록주소Label.setText(selected목록.getAddress());
				목록구분Label.setText(selected목록.getType());
				목록비고Label.setText(selected목록.getComment());
			}
		});

	}

	public void set목록주소Label(JLabel label) {
		목록주소Label = label;
	}

	public void set목록구분Label(JLabel label) {
		목록구분Label = label;
	}

	public void set목록비고Label(JLabel label) {
		목록비고Label = label;
	}

	public void set실행Button(JButton button) {
		실행버튼 = button;
		실행버튼.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected목록 == null) {
					return;
				}
				if (alreadyDownloaded(selected목록)) {
					int n = JOptionPane.showConfirmDialog(frame,
							"이미 다운로드한 물건입니다. 계속 진행하시겠습니까?",
							"An Inane Question", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						browse();
						startMonitor();
					} else if (n == JOptionPane.NO_OPTION) {
					}
					return;
				} else {
					browse();
					startMonitor();
				}

			}
		});
	}

	protected boolean alreadyDownloaded(목록 item) {
		return new AtestedMonitor().alreadyDownloaded(item);
	}

	protected void startMonitor() {
		if (monitor != null && monitor.isAlive()) {
			try {
				monitor.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("새로운 모니터를 띄움닙다");
		monitor = new AtestedMonitor();
		monitor.setTarget(selected목록);
		monitor.setStatus(상태Label);
		monitor.start();
	}

	public void set상태Label(JLabel label) {
		상태Label = label;
		상태Label.setText("대기");
	}

	private void browse() {
		/**
		 * inpSvcCls=on &selkindcls= &e001admin_regn1= &e001admin_regn3=
		 * &a312lot_no= &a301buld_name= &a301buld_no_buld= &a301buld_no_room=
		 * &pin=11611996016086 &regt_no=1161 &svc_cls=VW &fromjunja=Y
		 * 
		 * 
		 * 
		 * inpSvcCls=on &selkindcls= &e001admin_regn1= &e001admin_regn3=
		 * &a312lot_no= &a301buld_name= &a301buld_no_buld= &a301buld_no_room=
		 * &pin=11611996011155 &regt_no=1161 &svc_cls=VW &fromjunja=Y
		 */
		try {
			String uriString = "http://www.iros.go.kr/iris/index.jsp?" + //
					"inpSvcCls=on" + //
					"&selkindcls=" + //
					"&e001admin_regn1=" + //
					"&e001admin_regn3=" + //
					"&a312lot_no=" + //
					"&a301buld_name=" + //
					"&a301buld_no_buld=" + //
					"&a301buld_no_room=" + //
					"&pin=" + selected목록.getBuNo() + //
					"&regt_no=" + selected목록.getBuNo().substring(0, 4) + //
					"&svc_cls=VW" + //
					"&fromjunja=Y";
			System.out.println(uriString);
//			 java.awt.Desktop.getDesktop().browse(new URI(uriString));
			browser.setURL(uriString);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void setFrame(JFrame jFrame) {
		this.frame = jFrame;

	}

}