package net.narusas.si.auction.app.onbid;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ONBIDApp extends JFrame {
	public ONBIDApp() {
		super("OnBid");
		getContentPane().setLayout(new BorderLayout());
		JButton builderButton = new JButton("Builder");
		builderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				new ONBIDBuilderApp();
				
			}
		});
		JButton updaterButton = new JButton("Updater");
		updaterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new ONBIDUpdaterApp();				
			}
		});
		getContentPane().add(builderButton, BorderLayout.NORTH);
		getContentPane().add(updaterButton, BorderLayout.SOUTH);
		setSize(300,100);
		setVisible(true);
	}

	public static void main(String[] args) {
		new ONBIDApp();
	}
}
