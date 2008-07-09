package net.narusas.aceauction.pdf.gamjung;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.narusas.util.lang.NFile;

import junit.framework.TestCase;

public class ImageParsingTest extends TestCase {
	public void testLoadImg() throws FileNotFoundException, IOException {
		File f = new File("85.pdf");
		assertTrue(f.exists());
		byte[] data = NFile.readBytes(f);

		int[] startTarget = { 0xFF, 0xD8 };
		int[] endTarget = { 0xFF, 0xD9 };

		int start = 0;
		int end = 0;
		int count = 0;
		for (int i = 0; i < data.length - 1; i++) {
			if ((data[i]& 0xFF) == startTarget[0] && (data[i+1] & 0xFF) == startTarget[1] ){
				System.out.println("Start:"+i);
				start = i;
			}
			
			if ((data[i]& 0xFF) == endTarget[0] && (data[i+1] & 0xFF) == endTarget[1] ){
				System.out.println("End:"+i);
				end = i+1;
				int len = end-start;
				byte[] buf = new byte[len];
				System.arraycopy(data, start, buf, 0, len);
				File img = new File(count+".jpg");
				NFile.write(img, buf);
				count++;
			}
		}
	}
}
