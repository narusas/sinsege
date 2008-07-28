package net.narusas.aceauction.ui_pregoods;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import net.narusas.aceauction.data.builder.BuildProgressListener;
import net.narusas.aceauction.ui.commons.법원ListModel;

public class MainFrame extends JFrame implements BuildProgressListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4045790311889882176L;
	private JProgressBar courtProgress;
	private JProgressBar chargeProgress;
	private JProgressBar sagunProgress;
	private JProgressBar goodsProgress;

	public MainFrame() {
		super("Pregoods");
		init();

	}

	private void init() {
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JList list = new JList();
		list.setModel(new 법원ListModel());

		JScrollPane scrolPanel = new JScrollPane();
		scrolPanel.getViewport().add(list);
		panel.add(scrolPanel, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));

		JButton workButton = new JButton("작업");
		JButton workAllButton = new JButton("전체 작업");

		controlPanel.add(workButton);
		controlPanel.add(workAllButton);
		panel.add(controlPanel, BorderLayout.SOUTH);

		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new GridLayout(4, 1));

		courtProgress = new JProgressBar();

		chargeProgress = new JProgressBar();
		sagunProgress = new JProgressBar();
		goodsProgress = new JProgressBar();

		setupProgress(progressPanel, new JLabel("법원"), courtProgress);
		setupProgress(progressPanel, new JLabel("담당계"), chargeProgress);
		setupProgress(progressPanel, new JLabel("사건"), sagunProgress);
		setupProgress(progressPanel, new JLabel("물건"), goodsProgress);

		add(panel, BorderLayout.WEST);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());

		contentPanel.add(progressPanel, BorderLayout.NORTH);
		add(contentPanel, BorderLayout.CENTER);

		setSize(800, 640);
		setVisible(true);
	}

	private void setupProgress(JPanel progressPanel, JLabel label,
			JProgressBar progress) {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		label.setPreferredSize(new Dimension(50, 20));
		p.add(label, BorderLayout.WEST);
		p.add(progress, BorderLayout.CENTER);
		progressPanel.add(p);
	}

	public static void main(String[] args) {
		MainFrame f = new MainFrame();
	}

	@Override
	public void progress(int level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update담당계Size(int chargeSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update물건Size(int mulgunSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update사건Size(int sagunSize) {
		// TODO Auto-generated method stub

	}
}
