/*
 * 
 */
package net.narusas.aceauction.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import net.narusas.util.lang.NFile;

// TODO: Auto-generated Javadoc
/**
 * The Class �ǹ���Ȳ_����Converter.
 */
public class �ǹ���Ȳ_����Converter {

	/** The props. */
	private static Properties props;
	static {
		props = new Properties();
		try {
			String text = NFile.getText(new File("�ǹ���Ȳ_����.cfg"));
			String[] lines = text.split("\n");
			for (String line : lines) {
				if ("".equals(line.trim())) {
					continue;
				}
				String[] tokens = line.split("=");
				props.put(tokens[0], tokens[1]);
			}
			// props.load(new FileReader("�ǹ���Ȳ_����.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Convert.
	 * 
	 * @param str the str
	 * 
	 * @return the string
	 */
	public static String convert(String str) {
		if (str == null) {
			return "";
		}
		if (props.containsKey(str)) {
			return props.getProperty(str);
		}
		str = str.replaceAll("��", ",");
		String temp;
		if (str.endsWith("��") && str.endsWith("����") == false) {
			temp = str.substring(0, str.length() - 1);
			return temp + "����";
		}
		if (str.endsWith("����") == false) {
			return str + "����";
		}

		return str;
	}
}
