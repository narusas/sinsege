package net.narusas.aceauction.dwrtest;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class StringTest extends TestCase {
	public void testUnicode() throws IOException {
		String src = NFile.getText(new File("부동산테크_샘플링/RetPricePyong_year_response.txt"));//"dwr.engine.remote.handleCallback(\"0\",\"0\",{status:\"OK\",result:[{\"DO_NAME\":\"\\uC11C\\uC6B8\",\"DO_CODE\":\"11\"},{\"DO_NAME\":\"\\uBD80\\uC0B0\",\"DO_CODE\":\"26\"},{\"DO_NAME\":\"\\uB300\\uAD6C\",\"DO_CODE\":\"27\"},{\"DO_NAME\":\"\\uC778\\uCC9C\",\"DO_CODE\":\"28\"},{\"DO_NAME\":\"\\uAD11\\uC8FC\",\"DO_CODE\":\"29\"},{\"DO_NAME\":\"\\uB300\\uC804\",\"DO_CODE\":\"30\"},{\"DO_NAME\":\"\\uC6B8\\uC0B0\",\"DO_CODE\":\"31\"},{\"DO_NAME\":\"\\uACBD\\uAE30\",\"DO_CODE\":\"41\"},{\"DO_NAME\":\"\\uAC15\\uC6D0\",\"DO_CODE\":\"42\"},{\"DO_NAME\":\"\\uCDA9\\uBD81\",\"DO_CODE\":\"43\"},{\"DO_NAME\":\"\\uCDA9\\uB0A8\",\"DO_CODE\":\"44\"},{\"DO_NAME\":\"\\uC804\\uBD81\",\"DO_CODE\":\"45\"},{\"DO_NAME\":\"\\uC804\\uB0A8\",\"DO_CODE\":\"46\"},{\"DO_NAME\":\"\\uACBD\\uBD81\",\"DO_CODE\":\"47\"},{\"DO_NAME\":\"\\uACBD\\uB0A8\",\"DO_CODE\":\"48\"},{\"DO_NAME\":\"\\uC81C\\uC8FC\",\"DO_CODE\":\"50\"}]});";
		Pattern p = Pattern.compile("\\\\u(....)");
		
		Matcher m = p.matcher(src);
		while(m.find()){
			char ch = (char) (Integer.parseInt(m.group(1), 16));
			src = src.replace("\\u"+m.group(1), ""+ch);
		}
		
		System.out.println(src);
		
		Pattern p2 = Pattern.compile("DO_NAME\":\"([^\"]+)\",\"DO_CODE\":\"([^\"]+)");
		Matcher m2 = p2.matcher(src);
		while(m2.find()){
			System.out.println(m2.group(1)+" : "+m2.group(2));
		}
		/**
		 * 서울 : 11
부산 : 26
대구 : 27
인천 : 28
광주 : 29
대전 : 30
울산 : 31
경기 : 41
강원 : 42
충북 : 43
충남 : 44
전북 : 45
전남 : 46
경북 : 47
경남 : 48
제주 : 50
		 */
	}
}
