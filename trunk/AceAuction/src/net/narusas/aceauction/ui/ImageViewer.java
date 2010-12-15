package net.narusas.aceauction.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class ImageViewer extends JFrame {
	private final Image img;

	public ImageViewer(String url, Image img) {
		super(url);
		this.img = img;
		setSize(img.getWidth(this), img.getHeight(this));
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	public void close() {
		setVisible(false);
		dispose();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}
}
