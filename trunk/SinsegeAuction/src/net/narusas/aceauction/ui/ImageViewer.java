/*
 * 
 */
package net.narusas.aceauction.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageViewer.
 */
public class ImageViewer extends JFrame {
	
	/** The img. */
	private final Image img;

	/**
	 * Instantiates a new image viewer.
	 * 
	 * @param url the url
	 * @param img the img
	 */
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

	/**
	 * Close.
	 */
	public void close() {
		setVisible(false);
		dispose();
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
	}
}
