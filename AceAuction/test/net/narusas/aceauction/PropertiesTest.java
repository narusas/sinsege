package net.narusas.aceauction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import junit.framework.TestCase;

public class PropertiesTest extends TestCase {
	public void test1() {
		Properties p = new Properties();
		p.setProperty("pdfreader", "c:\\Program Files\\Adobe\\Reader 8.0\\Reader\\AcroRd32.exe");
		try {
			p.save(new FileOutputStream(new File("cfg.properties")),"Config");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}