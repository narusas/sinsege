import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.pdfbox.util.StringUtil;


public class Test2 {
	public static void main(String[] args) {
		try {
			byte[] data  = new byte[]{66,67,0,66,67};
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
			System.out.println(new String(br.readLine()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
