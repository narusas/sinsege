package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import net.narusas.util.lang.NFile;

public class 물건종별Stage1 {
	private static LinkedList<Entry> 물건종별매칭테이블;
	static {
		물건종별매칭테이블 = new LinkedList<Entry>();
		try {
			String text = NFile.getText(new File("cfg/type_match_premium.txt"), "euc-kr");
			String[] lines = text.split("\n");
			for (String line : lines) {
				if ("".equals(line.trim())) {
					continue;
				}

				String[] tokens = line.split("=");
				물건종별매칭테이블.add(new Entry(tokens[0], Integer.parseInt(tokens[1].trim())));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Integer parse물건종류(String 물건종류) {
		if (물건종류 == null) {
			return null;
		}
		for (Entry entry : 물건종별매칭테이블) {
			if (entry.isMatchKey(물건종류)) {
				return entry.getValue();
			}
		}
		return null;
	}

}
