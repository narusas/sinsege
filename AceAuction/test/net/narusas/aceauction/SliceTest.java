package net.narusas.aceauction;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class SliceTest extends TestCase {
	public void testSlice() throws IOException {
		Pattern p1 = Pattern.compile("`([^`]+)`");
		Pattern p2 = Pattern.compile("'([^']+)'");

		// Matcher m1 = p1.matcher(" `judgement_location` varchar(4096) default
		// NULL COMMENT '감정평가서_위치관련',");
		// Matcher m2 = p2.matcher(" `judgement_location` varchar(4096) default
		// NULL COMMENT '감정평가서_위치관련',");
		// assertTrue(m1.find());
		// assertEquals("judgement_location", m1.group(1));
		// assertTrue(m2.find());
		// assertEquals("감정평가서_위치관련", m2.group(1));

		String text = NFile.getText(new File("slice.txt"));
		String[] lines = text.split("\n");
		LinkedList<Pair> list = new LinkedList<Pair>();
		for (String line : lines) {
			Matcher m1 = p1.matcher(line);
			Matcher m2 = p2.matcher(line);
			if (m1.find()) {
				Pair p = new Pair();
				p.str1 = m1.group(1);
				if (m2.find()) {
					p.str2 = m2.group(1);
				}
				list.add(p);
			}
			
		}
		for (Pair pair : list) {
			System.out.println(pair.str1);
		}
		System.out.println("--------");
		for (Pair pair : list) {
			System.out.println(pair.str2);
		}
		System.out.println("--------");
	}
}

class Pair {
	String str1;
	String str2="";
}
