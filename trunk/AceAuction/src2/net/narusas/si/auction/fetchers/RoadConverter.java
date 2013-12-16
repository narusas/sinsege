package net.narusas.si.auction.fetchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoadConverter {

	String[] keys = { 
			"+K7aoM/9ubk7w30PpKRodf0CUiPGVtCtGuiI0wtE+g8Jr7OGvXDdtHCk+yI22NSzqcixNP91yv+S+/7O19TiPQ==",
			"x4BO9buhBOISnS1z8l1u3u/YfDKB+kYRANINBYKIxal7dV9F+DTtCHpHDrKa43LIJ8qVeOQ7UNViPI0YMjmaMw==",
			"NK5NJ8oeE2xIlB9dey+0LMcMmF31pcdkYS9in9DdonVdj02nXwvGqJx1GzZRkzZBh8wzLySNzZ74AN1KoyhP4w==",
			"cEqbaNBzDP5ZyYEB0iWlHIzDMw9lFtaGysVYiV6WtQlrLreJS5pzrvh5r8EVHFeCCMAQxoUkKox9kXz7sOoRBw",
			"+UkJ2UMS+n+99x6jSpIeHxDNEmnyzLOpS6aC4iNNxnTAG6WvL+pDFotkxCH5Xdj9MTy2Od08AE9QgEtlh9pbkA==",
			"alJT8gFr63xv5eXYMKb+2W6BVBkQuRW4fQvI5+ABcJHAw2IQQ24QrvyM0DUhLHEOaCdx2vJeUO2lVEPAItT33Q==",
			"rWLA5MzFnceAX9pFAwmDVCeafVqVg2zaJzFgbdWNKejevTKq9jJWxULMfwXK7GcyJc9priaTnMr0BLv6g23wVQ=="
	
	};
	int nextKeyIndex = 0;

	public String convert(String dong) {
		try {
			String key = nextKey();
			String query = "http://openapi.epost.go.kr/postal/retrieveNewAdressService/retrieveNewAdressService/getNewAddressList?searchSe=dong&serviceKey="
					+ URLEncoder.encode(key, "UTF-8") + "&srchwrd=" + URLEncoder.encode(dong, "UTF-8");
			System.out.println(query);
			URL u = new URL(query);
			InputStream in = u.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer buf = new StringBuffer();
			while (true) {
				String line = br.readLine();

				if (line == null) {
					break;
				}
				buf.append(line);

			}
			br.close();
			String json = buf.toString();
			System.out.println(json);
			Matcher m = Pattern.compile("<lnmAdres>([^<]+)</lnmAdres>\\s?<rnAdres>([^<]+)</rnAdres>").matcher(json);
			while (m.find()) {
				String road = m.group(1);
				String rn = m.group(2);
				System.out.println(road + ":" + rn);
				if (rn.contains(dong)) {
					return road;
				}
			}

			return null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String nextKey() {
		String key = keys[nextKeyIndex];
		nextKeyIndex++;
		if (nextKeyIndex == keys.length) {
			nextKeyIndex = 0;
		}
		return key;
	}

	public static void main(String[] args) throws IOException {
		RoadConverter c = new RoadConverter();
		// 가장동 35-3
		// 경북 상주시 가장동 386
		// 대전 서구 가장동 35-3
		String target = "가장동 35-3";
		System.out.println(c.convert("우고리 465"));

	}

}
