package net.narusas.aceauction.data;

import java.util.Date;

import junit.framework.TestCase;
import net.narusas.aceauction.data.updater.Updater;

public class UpdaterTest extends TestCase {
	public void testCreation() throws Exception {
//		Updater up = new Updater(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14),
//				new UpdaterListener() {
//					public void updateWorkSize(int size) {
//
//					}
//
//					public void log(String msg) {
//
//					}
//
//					public void progress(int progress) {
//
//					}
//				});

//		up.update();
	}

	public void testEvent() {
		String src = "2004130022122";
		assertEquals("2004", Updater.year(src));
		assertEquals("22122", Updater.no(src));
	}
	
	public void testDate() {
		Date d = new Date(2004-1900, 1-1,2);
		System.out.println(d);
	}
	
	public void test6letter(){
		assertEquals("000111", Updater.letter6("111"));
		assertEquals("214807", Updater.letter6("214807"));
		
	}

}
