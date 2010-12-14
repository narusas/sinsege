package net.narusas.si.auction.app.onbid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.jeta.forms.components.panel.FormPanel;

public class ONBIDBuilderApp extends JFrame {
	FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
	private AbstractListModel groupListModel;
	private List<공매일정> 공매일정List;
	private JList groupList;
	private JTextArea logArea;
	protected List<공매물건> 물건목록;
	private JLabel totalLabel;
	private JLabel progressLabel;

	public ONBIDBuilderApp() {
		super("Onbid");

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
				fetch((공매일정) groupList.getSelectedValue());

			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 700);
		center();
		setVisible(true);
	}

	공매일정 last = null;

	protected void fetch( 공매일정 schedule) {
		logg(schedule.toString());
		final 공매일정DaoHibernate dao = (공매일정DaoHibernate) context.getBean("공매DAO");
		final 공매물건DaoHibernate dao2 = (공매물건DaoHibernate) context.getBean("공매물건DAO");
		List<공매일정> olds = dao.getAll();
		
		final 공매일정[] temp = new 공매일정[1];
		

//		for (공매일정 old : olds) {
//			System.out.println("#---------" + old.toString());
//			if (old.toString().equals(schedule.toString())) {
//
//				if (last != null && last.toString().equals(schedule.toString())) {
//					logg("기존의 작업을 지우고 다시 작업합니다.  " + schedule);
//					old.set공매물건List(null);
//					dao.saveOrUpdate(old);
//					dao.remove(old);
//					temp[0] = old;
//				} else {
//					logg(schedule.toString());
//					logg("이미 작업한 일정입니다. 기존의 작업을 지우고 다시 작업하시고 싶으시면 한번더 실행버튼을 눌러주세요.  ");
//					last = schedule;
//					return;
//				}
//
//			}
//		}
//		if (temp[0]==null){
//			logg("DB 에 없는 신규 작업입니다. ") ;
//		}
//		if (temp[0]!=null){
//			schedule = temp[0];
//		}
		final 공매일정 work = schedule;

		last = schedule;

		logg("작업을 시작합니다:" + schedule);
		final 공매물건Fetcher f2 = new 공매물건Fetcher();
		new Thread() {

			public void run() {
				try {
					물건목록 = f2.fetch(work, new FetcherCallback() {

						@Override
						public void setTotal(final int total) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									totalLabel.setText(String.valueOf(total));
								}
							});
						}

						@Override
						public void progress(final int progress) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									progressLabel.setText(String.valueOf(progress));
									logg(progress + " 페이지를 얻어옵니다");
								}
							});

						}

						@Override
						public void log(final String msg) {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									logg(msg);
								}
							});

						}
					});

					try {

						List<공매물건> filtered = filterUpdate(물건목록, dao2);
						logg("수집된 목록을  DB 에 입력합니다. ");
						work.set공매물건List(filtered);
						logg("목록을  DB 에 입력했습니다. ");
						dao.save(work);
					} catch (Exception e) {
						e.printStackTrace();
						logg(" 문제가 발생했습니다. " + e.getMessage());
					}
					// for (공매물건 item : 물건목록) {
					//
					// }
				} catch (HttpException e) {
					logg(" 온비드 사이트에 접속할수 없습니다. 잠시후에 다시 시도해주십시요. ") ;
					e.printStackTrace();
				} catch (IOException e) {
					logg(" 온비드 사이트에 접속할수 없습니다. 잠시후에 다시 시도해주십시요. ") ;
					e.printStackTrace();
				}
				catch (Exception e) {
					logg(" 온비드 사이트에 접속할수 없습니다. 잠시후에 다시 시도해주십시요. ") ;
					e.printStackTrace();
				}
			};
		}.start();

	}

	protected List<공매물건> filterUpdate(List<공매물건> fetched, 공매물건DaoHibernate dao) {
		logg(" 얻어온  물건을  DB 에 입력된 물건과 비교합니다. ");
		ArrayList<공매물건> res = new ArrayList<공매물건>();
		 System.out.println(fetched);
		for (공매물건 item : fetched) {
			공매물건 old = dao.find(item);
			logg("Old:" + old);
			if (old != null) {
				logg("이미  DB 에 입력된 물건입니다. 정보를 통합합니다. ");
				old.merge(item);
				res.add(old);
			} else {
				res.add(item);
			}
		}
		return res;
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
			공매일정Fetcher f = new 공매일정Fetcher();
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
		new ONBIDBuilderApp();
		// try {
		//
		// 공매일정Fetcher f = new 공매일정Fetcher();
		// List<공매일정> res = f.fetch();
		// for (공매일정 schedule : res) {
		// System.out.println(schedule);
		// 공매물건Fetcher f2 = new 공매물건Fetcher();
		// List<공매물건> res2 = f2.fetch(schedule);
		// schedule.set공매물건List(res2);
		// dao.saveOrUpdate(schedule);
		// }
		// } catch (HttpException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
