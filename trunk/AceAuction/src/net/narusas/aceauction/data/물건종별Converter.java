package net.narusas.aceauction.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ��������Converter {
	private static Properties props;

	static {
		props = new Properties();
		try {
			props.load(new FileReader("��������.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convert(String ��������) {
		if (props.containsKey(��������)) {
			return props.getProperty(��������);
		}
		return ��������;
	}

}
