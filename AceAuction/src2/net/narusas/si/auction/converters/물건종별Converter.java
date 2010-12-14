/*
 * 
 */
package net.narusas.si.auction.converters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class 물건종별Converter.
 */
public class 물건종별Converter {
	
	/** The props. */
	private static Properties props;

	static {
		props = new Properties();
		try {
			props.load(new FileReader("cfg/물건종별.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convert.
	 * 
	 * @param 물건종별 the 물건종별
	 * 
	 * @return the string
	 */
	public static String convert(String 물건종별) {
		if (props.containsKey(물건종별)) {
			return props.getProperty(물건종별);
		}
		return 물건종별;
	}

}
