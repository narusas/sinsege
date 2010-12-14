/*
 * 
 */
package net.narusas.si.auction.converters;

// TODO: Auto-generated Javadoc
/**
 * The Class 매각대상Converter.
 */
public class 매각대상Converter {

	/**
	 * Convert.
	 * 
	 * @param 매각대상 the 매각대상
	 * 
	 * @return the string
	 */
	public static String convert(String 매각대상) {
		String res = 매각대상.replaceAll("/", " 및 ");
		if (res.equals("토지일괄매각")) {
			return "토지매각";
		}
		if (res.equals("건물일괄매각")) {
			return "토지제외 및 건물매각";
		}
		if (res.equals("토지만매각")) {
			return "건물제외 및 토지매각";
		}
		if (res.equals("건물만매각")) {
			return "토지제외 및 건물매각";
		}
		return res;
	}

}
