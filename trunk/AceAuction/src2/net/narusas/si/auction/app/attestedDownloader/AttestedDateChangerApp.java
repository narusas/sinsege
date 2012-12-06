package net.narusas.si.auction.app.attestedDownloader;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;

public class AttestedDateChangerApp extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		setupTZ();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AttestedDateChangerApp frame = new AttestedDateChangerApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void setupTZ() {
		Calendar.getInstance().setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
	}

	/**
	 * Create the frame.
	 */
	public AttestedDateChangerApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openPDFDialog();
			}
		});
		panel.add(btnNewButton);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(100);
		contentPane.add(splitPane, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		JList list = new JList();
		scrollPane.setViewportView(list);

		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		JTextArea textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
	}

	protected void openPDFDialog() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = fc.getSelectedFile();
		changeAttested(file);
	}

	private void changeAttested(File file) {
		File path = file.getParentFile();
		String saveFileName = file.getName().substring(0, file.getName().lastIndexOf(".")) + "_converted.pdf";
		File saveFile = new File(path, saveFileName);

		AttestedDateChanger changer = new AttestedDateChanger(file);
		changer.convertTo(saveFile);
	}

}
