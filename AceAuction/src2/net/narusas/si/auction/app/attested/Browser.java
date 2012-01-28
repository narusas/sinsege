package net.narusas.si.auction.app.attested;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.swing.JFrame;

import org.openqa.selenium.WebDriver;

public class Browser {
	private WebDriver webBrowser;

	public Browser() {
		JFrame frame = new JFrame("ATESTED Web Browser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		webBrowser = new org.openqa.selenium.firefox.FirefoxDriver();
		webBrowser = new org.openqa.selenium.ie.InternetExplorerDriver();

			webBrowser.get("http://www.iros.go.kr");


	}

	public void setURL(String url) {
			webBrowser.get(url);
	}

	public static void main(String[] args) {
		try {
			
			Browser b = new Browser();
			//b.webBrowser.setURL(new URL("http://www.iros.go.kr/"));
			Thread.sleep(10000);
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
