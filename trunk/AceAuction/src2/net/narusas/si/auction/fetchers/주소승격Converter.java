package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.narusas.util.lang.Closure;
import net.narusas.util.lang.NFile;

public class 주소승격Converter {
	static Map<Integer, 주소승격> translateMap = new HashMap<Integer, 주소승격>();
	static {
		try {
			NFile.eachLine(new File("cfg/area_translate.txt"), "UTF-8", new Closure() {
				@Override
				public void doWith(String line) {
					if (StringUtils.isEmpty(line)) {
						return;
					}
					String[] tokens = line.split(",");
					주소승격 addr = new 주소승격();
					addr.originCode = Integer.parseInt(tokens[0].trim());
					addr.originName = tokens[1].trim();
					addr.translatedCode = Integer.parseInt(tokens[2].trim());
					addr.translatedName = tokens[3].trim();
					System.out.println(addr);
					translateMap.put(addr.originCode, addr);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Integer 지역코드(Integer originCode) {
		if (is승격됨(originCode)) {
			return translateMap.get(originCode).getTranslatedCode();
		}
		return originCode;
	}

	public boolean is승격됨(Integer originCode) {
		return translateMap.containsKey(originCode);
	}
	
	public String 지역명(Integer originCode, String originName) {
		if (is승격됨(originCode)) {
			return translateMap.get(originCode).getTranslatedName();
		}
		return originName;
	}
	
	
	

	public static void main(String[] args) {
		new 주소승격Converter();
	}

}

class 주소승격 {
	@Override
	public String toString() {
		return "주소승격 [originCode=" + originCode + ", originName=" + originName + ", translatedCode=" + translatedCode + ", translatedName="
				+ translatedName + "]";
	}

	int originCode;
	String originName;
	int translatedCode;
	String translatedName;

	public int getOriginCode() {
		return originCode;
	}

	public void setOriginCode(int originCode) {
		this.originCode = originCode;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public int getTranslatedCode() {
		return translatedCode;
	}

	public void setTranslatedCode(int translatedCode) {
		this.translatedCode = translatedCode;
	}

	public String getTranslatedName() {
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

}
