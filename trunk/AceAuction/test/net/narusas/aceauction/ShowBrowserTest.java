package net.narusas.aceauction;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class ShowBrowserTest extends TestCase {
	public void test1() throws InterruptedException, IOException {
		File f= new File("fixtures/경매계목록.htm");
		Process ps = Runtime.getRuntime().exec(
				"c:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "+f.toURL());
		Thread.sleep(1000 * 30);
	}
}
