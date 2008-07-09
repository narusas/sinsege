package net.narusas.aceauction;

import java.io.File;

import junit.framework.TestCase;

public class FileTest extends TestCase {
	public void test1(){
		File f= new File("C:\\Program Files\\Adobe\\Acrobat 6.0\\Reader");
		File[] list = f.listFiles();
		for (File file : list) {
			System.out.println(file);
		}
		
		
	}
}
