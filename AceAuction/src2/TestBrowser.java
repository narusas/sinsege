

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TestBrowser {
	public static void main(String[] args) {
		try {
			java.awt.Desktop.getDesktop().browse(new URI("http://naver.com"));
			Thread.sleep(5000L);
			java.awt.Desktop.getDesktop().browse(new URI("http://daum.com"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
