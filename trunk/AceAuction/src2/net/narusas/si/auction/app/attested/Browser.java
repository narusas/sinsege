package net.narusas.si.auction.app.attested;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.jdic.browser.WebBrowser;
import org.jdesktop.jdic.browser.WebBrowserEvent;
import org.jdesktop.jdic.browser.WebBrowserListener;

public class Browser {
	private WebBrowser webBrowser;

	public Browser() {
		JFrame frame = new JFrame("ATESTED Web Browser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		webBrowser = new WebBrowser();

		// Use below code to check the status of the navigation process,
		// or register a listener for the notification events.
		webBrowser.addWebBrowserListener(new WebBrowserListener() {
			public void downloadStarted(WebBrowserEvent event) {
				;
			}

			public void downloadCompleted(WebBrowserEvent event) {
				;
			}

			public void downloadProgress(WebBrowserEvent event) {
				;
			}

			public void downloadError(WebBrowserEvent event) {
				;
			}

			public void documentCompleted(WebBrowserEvent event) {
				System.out.println("documentCompleted");
				System.out.println(webBrowser.getURL());
			}

			public void titleChange(WebBrowserEvent event) {
				System.out.println("titleChange:"+event);
			}

			public void statusTextChange(WebBrowserEvent event) {
//				System.out.println("statusTextChange:" + event);
			}

			@Override
			public void windowClose(WebBrowserEvent arg0) {
				
			}


		});

		try {
			webBrowser.setURL(new URL("http://www.iros.go.kr"));
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			return;
		}

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(1024, 768));
		panel.add(webBrowser, BorderLayout.CENTER);

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);

	}

	public void setURL(String url) {
		try {
			webBrowser.setURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			
			Browser b = new Browser();
			//b.webBrowser.setURL(new URL("http://www.iros.go.kr/"));
			Thread.sleep(10000);
			System.out.println(b.webBrowser.getURL());
			BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String line = br.readLine();
				if (line == null ||"".equals(line)){
					continue;
				}
				b.setURL(line);
				//b.setURL(line);
			}
			//b.webBrowser.setURL(new URL("javascript:"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
