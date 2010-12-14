package net.narusas.si.auction.app.onbid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.BeansException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.jeta.forms.components.panel.FormPanel;

public class ONBIDUpdaterApp extends JFrame {
	FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
	private AbstractListModel groupListModel;
	private List<공매결과일정> 공매일정List;
	private JList groupList;
	private JTextArea logArea;
	protected List<공매물건> 물건목록;
	private JLabel totalLabel;
	private JLabel progressLabel;

	public ONBIDUpdaterApp() {
		super("Updater");
		getContentPane().setLayout(new BorderLayout());

		JPanel modelSelectionPanel = new JPanel();
		modelSelectionPanel.setLayout(new BorderLayout());

		FormPanel modelPanel = new FormPanel("onbid.jfrm");
		modelSelectionPanel.add(modelPanel, BorderLayout.CENTER);
		getContentPane().add(modelSelectionPanel, BorderLayout.CENTER);

		logArea = (JTextArea) modelPanel.getComponentByName("logTextArea");
		totalLabel = (JLabel) modelPanel.getComponentByName("totalLabel");
		progressLabel = (JLabel) modelPanel.getComponentByName("progressLabel");
		initGroupList((JList) modelPanel.getComponentByName("groupList"));

		JButton runButton = (JButton) modelPanel.getComponentByName("runButton");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (groupList.getSelectedValue() == null) {
					return;
				}
				new Thread() {
					public void run() {
						fetch((공매결과일정) groupList.getSelectedValue());
					};
				}.start();

			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700);
		center();
		setVisible(true);

	}

	protected void fetch(공매결과일정 schedule) {
		try {
			logg(schedule.toString());
			final 공매물건DaoHibernate dao2 = (공매물건DaoHibernate) context.getBean("공매물건DAO");
			logg("물건을 얻어옵니다. ");
			List<공매물건> list = new 공매결과물건Fetcher().fetch(schedule);
			for (공매물건 공매물건 : list) {
				logg("작업 물건:" + 공매물건.get물건관리번호() + " 개찰결과:" + 공매물건.get개찰결과() + " 낙찰가:" + 공매물건.get낙찰가());
				logg(공매물건.get물건관리번호() + "이 이미 저장되었는지 확인합니다. ");
				공매물건 old = dao2.find(공매물건);
				if (old == null) {
					logg(공매물건.get물건관리번호() + "은 DB에 저장되어 있지 않습니다. ");
					continue;
				}
				logg(공매물건.get물건관리번호() + "의 정보를 최신 정보로 갱신합니다. ");
				old.set개찰결과(공매물건.get개찰결과());
				old.set낙찰가(공매물건.get낙찰가());
				dao2.saveOrUpdate(old);
				logg(공매물건.get물건관리번호() + "에 대한 작업을 완료했습니다. ");
			}
			logg("공매결과일정에 대한 작업을 완료합니다.");
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void logg(final String msg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (logArea.getText() != null && logArea.getText().length() > 20000) {
					logArea.setText(logArea.getText().substring(9000));
				}
				logArea.setText("" + logArea.getText() + "\n" + msg);
			}
		});

	}

	private void initGroupList(JList groupList) {
		this.groupList = groupList;
		try {
			공매결과일정Fetcher f = new 공매결과일정Fetcher();
			공매일정List = f.fetch();

			groupListModel = new AbstractListModel() {
				@Override
				public int getSize() {
					return 공매일정List.size();
				}

				@Override
				public Object getElementAt(int index) {
					return 공매일정List.get(index);
				}
			};
			groupList.setModel(groupListModel);

		} catch (Exception ex) {

		}
	}

	void center() {
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		this.setLocation(x, y);
	}

	public static void main(String[] args) {
		new ONBIDUpdaterApp();
	}
}
