/*
 * 
 */
package net.narusas.aceauction.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class ��������Converter.
 */
public class ��������Converter {
	
	/** The props. */
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

	/**
	 * Convert.
	 * 
	 * @param �������� the ��������
	 * 
	 * @return the string
	 */
	public static String convert(String ��������) {
		if (props.containsKey(��������)) {
			return props.getProperty(��������);
		}
		return ��������;
	}

}
