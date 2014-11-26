package pre;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.narusas.si.auction.fetchers.주소Builder;
import net.narusas.si.auction.model.주소;

import org.apache.commons.lang.StringUtils;

public class PreApp {

	private JFrame frame;
	JComboBox jiwonComboBox;
	private PreFetcher fetcher;
	PreDao dao = new PreDao();
	JComboBox chargeComboBox;
	private List<담당계> chargeList;
	JButton button;
	private JTextArea logTextArea;
	private List<사건> sagunList;
	private JTextField sanoTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreApp window = new PreApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PreApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		fetcher = new PreFetcher();
		frame = new JFrame();
		frame.setBounds(100, 100, 535, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		jiwonComboBox = new JComboBox();
		jiwonComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("-----".equals(jiwonComboBox.getSelectedItem())) {
					return;
				}
				jiwonSelected(jiwonComboBox.getSelectedItem());
			}
		});
		jiwonComboBox.setModel(new DefaultComboBoxModel(new String[] { "-----", "서울중앙지방법원", "서울동부지방법원", "서울서부지방법원", "서울남부지방법원", "서울북부지방법원",
				"의정부지방법원", "고양지원", "인천지방법원", "부천지원", "수원지방법원", "성남지원", "여주지원", "평택지원", "안산지원", "안양지원", "춘천지방법원", "강릉지원", "원주지원", "속초지원",
				"영월지원", "청주지방법원", "충주지원", "제천지원", "영동지원", "대전지방법원", "홍성지원", "논산지원", "천안지원", "공주지원", "서산지원", "대구지방법원", "안동지원", "경주지원",
				"김천지원", "상주지원", "의성지원", "영덕지원", "포항지원", "대구서부지원", "부산지방법원", "부산동부지원", "울산지방법원", "창원지방법원", "마산지원", "진주지원", "통영지원", "밀양지원",
				"거창지원", "광주지방법원", "목포지원", "장흥지원", "순천지원", "해남지원", "전주지방법원", "군산지원", "정읍지원", "남원지원", "제주지방법원" }));
		jiwonComboBox.setBounds(6, 6, 172, 27);
		frame.getContentPane().add(jiwonComboBox);

		chargeComboBox = new JComboBox();
		chargeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("-----".equals(chargeComboBox.getSelectedItem())) {
					return;
				}
				try {
					chargeSelected(chargeComboBox.getSelectedIndex());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		chargeComboBox.setModel(new DefaultComboBoxModel(new String[] { "----" }));
		chargeComboBox.setBounds(194, 6, 172, 27);
		frame.getContentPane().add(chargeComboBox);

		button = new JButton("실행");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeChargeSaguns();
			}
		});
		button.setBounds(384, 5, 117, 29);
		frame.getContentPane().add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 116, 523, 224);
		frame.getContentPane().add(scrollPane);
		
		logTextArea = new JTextArea();
		scrollPane.setViewportView(logTextArea);
		
		sanoTextField = new JTextField();
		sanoTextField.setBounds(6, 45, 172, 28);
		frame.getContentPane().add(sanoTextField);
		sanoTextField.setColumns(10);
		
		JButton button_1 = new JButton("단일실행");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
					executeSingle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(204, 45, 117, 29);
		frame.getContentPane().add(button_1);
		
		JButton button_2 = new JButton("법원 실행");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				법원목록();
			}
		});
		button_2.setBounds(384, 45, 117, 29);
		frame.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("전체 실행");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				전체실행();
			}
		});
		button_3.setBounds(384, 75, 117, 29);
		frame.getContentPane().add(button_3);
	}

	protected void 전체실행() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Thread() {
					public void run() {
						for(int i=1; i<jiwonComboBox.getModel().getSize();i++){
							String jiwon = (String) jiwonComboBox.getModel().getElementAt(i);
							log(" 법원  "+ jiwon+" 를 실행합니다. ");
							chargeList = fetcher.fetch담당계(jiwon);
							for (담당계 charge : chargeList) {
								log(" 담당계: "+ charge+" 를 실행합니다. ");
								try {
									sagunList = fetcher.fetch사건목록(jiwon, charge.id, charge.name);
									for (사건 sagun : sagunList) {
										String saNo = sagun.eventNo;
										try {
											handleInfoReal(jiwon, saNo, sagun);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}	
							}
						}
					};
				}.start();
				
				
			}});		
	}

	protected void 법원목록() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				chargeComboBox.setModel(new DefaultComboBoxModel(new String[] { "----" }));
				final String jiwon = (String) jiwonComboBox.getSelectedItem();
				System.out.println("Selected Jiwon: " + jiwon);
				chargeList = fetcher.fetch담당계(jiwon);
				upateChargeList(chargeList);
				new Thread() {
					public void run() {
						for (담당계 charge : chargeList) {
							log(" 담당계: "+ charge+" 를 실행합니다. ");
							try {
								sagunList = fetcher.fetch사건목록(jiwon, charge.id, charge.name);
								for (사건 sagun : sagunList) {
									String saNo = sagun.eventNo;
									try {
										handleInfoReal(jiwon, saNo, sagun);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}	
						}
					}
				}.start();
				
			}
		});
	}

	protected void executeSingle() throws Exception {
		String jiwon = (String) jiwonComboBox.getSelectedItem();
		String saNo = sanoTextField.getText();
		
		handleInfo(jiwon, saNo);
		
	}

	private void handleInfo(final String jiwon, final String saNo) throws Exception {
		new Thread(){
			public void run() {
				handleInfoReal(jiwon, saNo, new 사건());
			}

			;
		}.start();
	}
	private void handleInfoReal(final String jiwon, final String saNo, 사건 sagun) {
		try {
			사건기본정보 info = fetcher.fetch사건기본정보(jiwon, saNo);
			if (info == null) {
				log("정보가 없습니다");
				return;
			}
			
			
			dao.analysis(sagun);
			
			
			log("작업대상:"+info.saNo);
			log("DB 에 이미 입력되어 있는지 확인합니다.") ;
			if (dao.isExists기본정보(info)){
				log("이미 입력되어 있습니다. 다음으로 넘어갑니다. ");
				return;
			}
			 System.out.println("### SAGUN:"+sagun);
			log("DB 에 존재하지 않습니다.  DB 에 입력합니다. ");
			dao.insert기본정보( info, sagun);
			dao.insert당사자(info);
			log("DB 에 입력되었습니다. ");
			System.out.println("##############################################################");
			System.out.println(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void executeChargeSaguns() {
		new Thread(){
			public void run() {
				String jiwon = (String) jiwonComboBox.getSelectedItem();
				for (사건 sagun : sagunList) {
					String saNo = sagun.eventNo;
					
					try {
						handleInfoReal(jiwon, saNo, sagun);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void log(final String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				logTextArea.setText(logTextArea.getText()+"\n"+msg+"\n");		
				
			}
		});
	}

	protected void chargeSelected(int selectedIndex) throws Exception {
		담당계 charge = (담당계) chargeList.get(selectedIndex - 1);
		System.out.println("Selected charge: " + charge);
		sagunList = fetcher.fetch사건목록(getSelectedJiwon(), getSelectedChargeCode(),getSelectedChargeName());
		log(sagunList.toString());
		System.out.println("####################");
		System.out.println(sagunList);
		
	}

	private String getSelectedChargeCode() {
		담당계 charge = (담당계) chargeList.get(chargeComboBox.getSelectedIndex() - 1);
		return charge.id;
	}
	
	private String getSelectedChargeName() {
		담당계 charge = (담당계) chargeList.get(chargeComboBox.getSelectedIndex() - 1);
		return charge.name;
	}

	private String getSelectedJiwon() {
		return (String) jiwonComboBox.getSelectedItem();
	}

	protected void jiwonSelected(final Object selectedItem) {
		if (selectedItem == null) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				chargeComboBox.setModel(new DefaultComboBoxModel(new String[] { "----" }));
				String jiwon = (String) selectedItem;
				System.out.println("Selected Jiwon: " + jiwon);
				chargeList = fetcher.fetch담당계(jiwon);
				upateChargeList(chargeList);
			}
		});
	}

	private void upateChargeList(List<담당계> chargeList) {
		Vector<String> names = new Vector<String>();
		names.add("---- 완료");
		for (담당계 담당계 : chargeList) {
			names.add(담당계.name);
		}
		chargeComboBox.setModel(new DefaultComboBoxModel(names));
	}
}
