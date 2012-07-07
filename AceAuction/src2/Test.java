import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class Test {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String line = br.readLine();
				if (line == null || "".equals(line.trim())) {
					continue;
				}
				
				try {
					java.awt.Desktop.getDesktop().browse(new URI(line));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
