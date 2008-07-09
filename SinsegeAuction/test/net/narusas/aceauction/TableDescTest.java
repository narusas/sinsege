package net.narusas.aceauction;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;
import junit.framework.TestCase;

public class TableDescTest extends TestCase {

	public void test1() throws IOException {
		String text = NFile.getText(new File("doc/TableCreationQuery.txt"));

		String[] chunks = text.split("DROP");
		for (String chunk : chunks) {
			if (chunk.contains("CREATE") == false) {
				continue;
			}
			chunk = chunk.substring(chunk.indexOf("CREATE"));
			Pattern p = Pattern.compile("(ac_[^`]+)");
			Matcher m = p.matcher(chunk);
			m.find();
			String tableName = m.group(1);
			Pattern p5 = Pattern.compile("COMMENT='([^`]+)'", Pattern.MULTILINE);
			Matcher m5 = p5.matcher(chunk);
			String tableComment = "";
			if (m5.find()){
				tableComment = m5.group(1);
			}
			
			
			System.out.println(tableName+"\n"+tableComment);

			String[] lines = chunk.split("\n");
			int count = 0;
			for (String line : lines) {
				Pattern p2 = Pattern.compile("^\\s+`([^`]+)`\\s+([^\\s]+)");
				Matcher m2 = p2.matcher(line);
				if (m2.find()) {
					String attrName = m2.group(1);
					String attrType = m2.group(2);
					
					boolean notNull = line.contains("NOT NULL");
					boolean def = line.contains("default");
					Pattern p3 = Pattern.compile("COMMENT '([^']+)");
					Matcher m3 = p3.matcher(line);
//					System.out.println(line);
					String comment = "";
					if (m3.find()){
						comment = m3.group(1);
					}
					System.out.format("%d\t%s\t%s\t%s\t\t%s\t%s\n", count,attrName, comment, attrType,notNull, def );	
				}
				
				
				
				count++;
				
			}

		}
	}
}
