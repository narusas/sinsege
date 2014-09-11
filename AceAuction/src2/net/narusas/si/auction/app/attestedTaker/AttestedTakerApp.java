package net.narusas.si.auction.app.attestedTaker;

import javax.swing.JFrame;

import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JButton;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.attested.parser.AtestedPDFParser;
import net.narusas.si.auction.app.ui.담당계ListModel;
import net.narusas.si.auction.app.ui.법원ListModel;
import net.narusas.si.auction.app.ui.사건ListModel;
import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.fetchers.PageFetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.등기부등본Item;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.담당계Dao;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jpedal.exception.PdfException;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class AttestedTakerApp extends JFrame {
	public JPanel controlPanel;
	public JPanel contentPanel;
	public JScrollPane scrollPane;
	public JScrollPane scrollPane_1;
	public JScrollPane scrollPane_2;
	public JScrollPane scrollPane_3;
	public JList 법원list;
	public JList 담당계list;
	public JList 사건list;
	public JList list_3;
	public JLabel lblNewLabel;
	public JLabel lblNewLabel_1;
	public JLabel lblNewLabel_2;
	public JLabel lblNewLabel_3;
	public JButton executeButton;
	public AttestedTakerInitiator initiator;
	public JTextField yearField;
	public JTextField noField;
	public JTextField mulNoField;
	public JLabel label;
	public JLabel lblNewLabel_4;
	public JLabel lblNewLabel_5;
	public JPanel panel;
	public JPanel panel_1;
	public JScrollPane scrollPane_4;
	public JTextArea logTextArea;
	public JPanel panel_2;
	public JPanel panel_3;
	public JPanel panel_4;
	public JPanel panel_5;
	public JButton executeChargeButton;
	public JButton executeEventButton;

	public AttestedTakerApp() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		controlPanel = new JPanel();
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		controlPanel.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		controlPanel.add(panel, BorderLayout.NORTH);

		label = new JLabel("연도");
		panel.add(label);

		yearField = new JTextField();
		panel.add(yearField);
		yearField.setColumns(10);

		lblNewLabel_4 = new JLabel("사건번호");
		panel.add(lblNewLabel_4);

		noField = new JTextField();
		panel.add(noField);
		noField.setColumns(10);

		lblNewLabel_5 = new JLabel("물건번호");
		panel.add(lblNewLabel_5);

		mulNoField = new JTextField();
		panel.add(mulNoField);
		mulNoField.setColumns(10);

		executeButton = new JButton("단일 실행");
		panel.add(executeButton);

		panel_1 = new JPanel();
		controlPanel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		logTextArea = new JTextArea();
		logTextArea.setRows(10);
		logTextArea.setText("log");

		scrollPane_4 = new JScrollPane();
		scrollPane_4.setViewportView(logTextArea);
		panel_1.add(scrollPane_4, BorderLayout.SOUTH);
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				download();
			}
		});

		contentPanel = new JPanel();
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));

		panel_2 = new JPanel();
		contentPanel.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_2.add(scrollPane);

		법원list = new JList();
		법원list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		법원list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				법원 court = (법원) 법원list.getSelectedValue();
				System.out.println(court);
				courtSelected(court);

			}
		});
		scrollPane.setViewportView(법원list);

		lblNewLabel = new JLabel("법원");
		scrollPane.setColumnHeaderView(lblNewLabel);

		panel_3 = new JPanel();
		contentPanel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));

		scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1);

		담당계list = new JList();
		담당계list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (event.getValueIsAdjusting()) {
					return;
				}
				담당계 charge = (담당계) 담당계list.getSelectedValue();
				담당계Selected(charge);

			}
		});
		scrollPane_1.setViewportView(담당계list);

		lblNewLabel_1 = new JLabel("담당계");
		scrollPane_1.setColumnHeaderView(lblNewLabel_1);

		executeChargeButton = new JButton("담당계 실행");
		executeChargeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				담당계실행();
			}
		});
		panel_3.add(executeChargeButton, BorderLayout.SOUTH);

		panel_4 = new JPanel();
		contentPanel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		scrollPane_2 = new JScrollPane();
		panel_4.add(scrollPane_2);

		사건list = new JList();
		사건list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}

			}
		});
		scrollPane_2.setViewportView(사건list);

		lblNewLabel_2 = new JLabel("사건");
		scrollPane_2.setColumnHeaderView(lblNewLabel_2);

		executeEventButton = new JButton("사건 실행");
		executeEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final 사건 event = (사건) 사건list.getSelectedValue();
				new Thread() {
					public void run() {
						try {
							download사건(event);
						} catch (HttpException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}.start();

			}
		});
		panel_4.add(executeEventButton, BorderLayout.SOUTH);

		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panel_5);

		scrollPane_3 = new JScrollPane();
		panel_5.add(scrollPane_3);

		list_3 = new JList();
		scrollPane_3.setViewportView(list_3);

		lblNewLabel_3 = new JLabel("New label");
		scrollPane_3.setColumnHeaderView(lblNewLabel_3);
	}
	private void download사건(final 사건 event) throws HttpException, IOException {
		log(" 사건을 다운로드 합니다. : "+event.get사건번호());
		String courtName = event.get법원().get법원경매_법원명() == null ? event.get법원().get법원명() : event.get법원().get법원경매_법원명();
		downloadEVent(event.get법원(), courtName, String.valueOf(event.getEventYear()),
				String.valueOf(event.getEventNo()), "1");
	}

	protected void 담당계실행() {
		 new Thread(){
			 public void run() {
				 ListModel model = 사건list.getModel();
				 for(int i=0; i< model.getSize(); i++){
					  사건  event = (사건) model.getElementAt(i);
					  try {
						download사건(event);
					} catch (HttpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 };
		 }.start();
	}

	protected void 담당계Selected(final 담당계 charge) {
		new Thread() {
			public void run() {
				사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
				log("" + charge + " 의 사건을 가져오는 중입니다. 잠시만 기다려주세요");
				List<사건> eventList = eventDao.findBy(charge);
				log("사건의 갯수:" + eventList.size());
				System.out.println(eventList);
				final 사건ListModel model = new 사건ListModel();
				사건list.setModel(model);
				for (사건 사건 : eventList) {
					log(" 사건:" + 사건.get사건번호() + "  이 신건인지  확인합니다. ");
					if (isIs신건(사건)) {
						log(" 신건입니다 ");
						model.addElement(사건);
					}
					else {
						log(" 신건이 아닙니다. ");
					}
				}
				log( "확인이 완료 되었습니다. 신건의 갯수:" + model.getSize());
			}
			
		}.start();
	}

	protected boolean isIs신건(사건 사건) {
		물건Dao goodsDAO = (물건Dao) App.context.getBean("물건DAO");

		물건 item = goodsDAO.find(사건.get법원(), 사건.get담당계(), 사건, 1);
		if(item == null){
			log("1번 물건이 없습니다. ");
			return false;
		}
		return "신건".equals(item.get기일결과()) || item.get유찰수() == 0;
	}

	protected void courtSelected(법원 court) {
		담당계Dao chargeDAO = (담당계Dao) App.context.getBean("담당계DAO");
		System.out.println(chargeDAO);
		
		Date base = new Date(System.currentTimeMillis());
		Date start = new Date(base.getYear(), base.getMonth(), base.getDate() );
		start = DateUtils.addDays(start, -1);
		Date end = DateUtils.addDays(start, 60);
		List<담당계> chargeList = chargeDAO.find(court
				,start
				,end
					);
		System.out.println(chargeList);
		담당계ListModel listModel = new 담당계ListModel();
		for (담당계 담당계 : chargeList) {
			if (StringUtils.isEmpty(담당계.getTime())){
				continue;
			}
			listModel.addElement(담당계);
		}

		담당계list.setModel(listModel);
	}

	protected void download() {
		try {

			final 법원 court = (법원) 법원list.getSelectedValue();
			final String courtName = court.get법원경매_법원명() == null ? court.get법원명() : court.get법원경매_법원명();

			final String eventYear = yearField.getText();
			final String eventNo = noField.getText();
			final String mulNo = mulNoField.getText();

			downloadEVent(court, courtName, eventYear, eventNo, mulNo);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void downloadEVent(final 법원 court, final String courtName, final String eventYear, final String eventNo, final String mulNo)
			throws HttpException, IOException {
		PageFetcher f = new PageFetcher("");
		f.post("http://www.courtauction.co.kr/lib/loginprocess.asp", new NameValuePair[] { new NameValuePair("type", "login"),
				new NameValuePair("loginid", "radio"), new NameValuePair("loginpw", "kch8888"),

		});

		String url = "http://www.courtauction.co.kr/maemul/maemul_view.asp?bubwon=" + HTMLUtils.encodeUrl(courtName, "EUC-KR") + "&num1="
				+ eventYear + "&num2=" + eventNo + "&num3=" + mulNo;
		log("작업  대상  URL:" + url);
		String html = f.fetch(url);
		Pattern p = Pattern.compile("name=\"dungki\\d+\" value=\"([^\"]*)", Pattern.MULTILINE);
		Matcher m = p.matcher(html);

		while (m.find()) {
			final String pdfUrl = m.group(1);
			if (StringUtils.isEmpty(pdfUrl)) {
				continue;
			}
			String type = "건물";
			System.out.println(m.group(0));
			System.out.println(pdfUrl);
			if (m.group(0).contains("dungki1")) {
				type = "토지";
			} else if (m.group(0).contains("dungki2")) {
				type = "건물";
			} else {
				continue;
			}
			final String temp = type;
			log("종류:" + temp + " URL" + pdfUrl);
			new Thread() {
				public void run() {
					try {
						downloadPdf(court, eventYear, eventNo, mulNo, pdfUrl, temp);
					} catch (PdfException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};
			}.start();
		}
	}

	private void downloadPdf(법원 court, String eventYear, String eventNo, String mulNo, String pdfUrl, String type) throws PdfException,
			Exception {
		// log("등기부등본 PDF URL:" + pdfUrl);
		사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
		사건 event = new 사건();
		event.setEventYear(Integer.parseInt(eventYear));
		event.setEventNo(Integer.parseInt(eventNo));
		사건 savedEvent = eventDao.find(court, Long.parseLong(event.to사건번호Full()));
		download(court, mulNo, pdfUrl, savedEvent, type);
	}

	private void download(법원 court, String mulNo, String pdfUrl, 사건 savedEvent, String type) throws MalformedURLException, IOException,
			PdfException, Exception {
		if (pdfUrl.contains("Dungki") == false) {
			return;
		}
		물건Dao goodsDAO = (물건Dao) App.context.getBean("물건DAO");
		물건 savedMul = goodsDAO.find(court, savedEvent.get담당계(), savedEvent, Integer.parseInt(mulNo));
		System.out.println(savedMul);

		File pdfFile = downloadPDFBinary(pdfUrl);
		log("저장된  PDF 파일:" + pdfFile.getAbsolutePath());

		List<등기부등본Item> items = new AtestedPDFParser().parse(pdfFile);
		for (등기부등본Item item : items) {
			System.out.println(item.toString());
		}
		등기부등본 atested = new 등기부등본(savedMul, type);
		등기부등본Dao dao = (등기부등본Dao) App.context.getBean("등기부등본DAO");
		atested.setItems(items);
		Collection<등기부등본> 기존 = dao.get(savedMul);
		if (기존 == null || 기존.size() == 0) {
			log("######################  갱신합니다. ");
			dao.saveOrUpdate(atested);
			for (등기부등본Item item : items) {
				log(item.toString());
			}
		} else {
			log("######################  이미 등기부등본이 있습니다. ");
		}
	}

	private File downloadPDFBinary(String url) throws MalformedURLException, IOException {
		URL u = new URL(url);
		File f = File.createTempFile("atested", ".pdf");
		FileOutputStream out = new FileOutputStream(f);
		out.write(net.narusas.util.lang.NInputStream.readBytes(u.openStream()));
		out.flush();
		out.close();
		return f;
	}

	public void setup() {
		법원list.setModel(new 법원ListModel(법원.법원목록));
	}

	void log(final String msg) {
		System.out.println(msg);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				logTextArea.setText(logTextArea.getText() + "\n" + msg);
			}
		});

	}
}
