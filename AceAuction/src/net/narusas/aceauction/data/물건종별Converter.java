package net.narusas.aceauction.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class 물건종별Converter {
	private static Properties props;

	static {
		props = new Properties();
		try {
			props.load(new FileReader("물건종별.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convert(String 물건종별) {
		if (props.containsKey(물건종별)) {
			return props.getProperty(물건종별);
		}
		return 물건종별;
	}

}
