package net.narusas.si.auction.fetchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoadConverter {

	List<String> keys = new ArrayList<String>(){
		{
			add("+K7aoM/9ubk7w30PpKRodf0CUiPGVtCtGuiI0wtE+g8Jr7OGvXDdtHCk+yI22NSzqcixNP91yv+S+/7O19TiPQ==");
			add("x4BO9buhBOISnS1z8l1u3u/YfDKB+kYRANINBYKIxal7dV9F+DTtCHpHDrKa43LIJ8qVeOQ7UNViPI0YMjmaMw==");
			add("NK5NJ8oeE2xIlB9dey+0LMcMmF31pcdkYS9in9DdonVdj02nXwvGqJx1GzZRkzZBh8wzLySNzZ74AN1KoyhP4w==");
			add("cEqbaNBzDP5ZyYEB0iWlHIzDMw9lFtaGysVYiV6WtQlrLreJS5pzrvh5r8EVHFeCCMAQxoUkKox9kXz7sOoRBw==");
			add("+UkJ2UMS+n+99x6jSpIeHxDNEmnyzLOpS6aC4iNNxnTAG6WvL+pDFotkxCH5Xdj9MTy2Od08AE9QgEtlh9pbkA==");
			add("alJT8gFr63xv5eXYMKb+2W6BVBkQuRW4fQvI5+ABcJHAw2IQQ24QrvyM0DUhLHEOaCdx2vJeUO2lVEPAItT33Q==");
			add("rWLA5MzFnceAX9pFAwmDVCeafVqVg2zaJzFgbdWNKejevTKq9jJWxULMfwXK7GcyJc9priaTnMr0BLv6g23wVQ==");
			add("l/E1Ie7jI8CTm4i/lxNYRKO6XdDEtNMuW802d4WGL8mI5RZd+ocNr4XPvl/c0Ol0S7hJ8cKII8idT3ggbuugXQ==");
			add("rJ2tENbiylyTlQUqf7J2loet7wsLXP5c+3U0nz9Hp8uoYHRzrUgFK/kTKsuClp0uJ6Rn32N4gafwbE8nwxAd7A==");
			add("1tB1fleTQRn05J6Mk0SiPjE90u9/qq0GC4qnGmlyn2nu6Y5tRXmPM6jAbR7/lwN3KPZLv3Wtrz8BKB7qj6a6mg==");
			add("2ISH1fAlyEjaNooN78IsReuaYxa4JedC7gSiSxhArE8nL0bfKPZpVzwRIr4FAuH9VFpFgfqPkRRVjRKW/B2gwg==");			
		}
	};
	int nextKeyIndex = 0;

	public String convert(String prefix, String dong) {
		try {
			String json = null; 
			while(true){
				String key = nextKey();
				if (key == null){
					throw new RuntimeException("No KEY");
				}
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
				json = buf.toString();
				System.out.println();
				System.out.println(json);
				if (json.contains("LIMITED NUMBER OF SERVICE") ==false){
					break;
				}
				limited(key);
				
			}
			
			Matcher m = Pattern.compile("<lnmAdres>([^<]+)</lnmAdres>\\s?<rnAdres>([^<]+)</rnAdres>").matcher(json);
			while (m.find()) {
				String road = m.group(1);
				String rn = m.group(2);
				System.out.println("road:"+road + " RN:" + rn+" Prefix:"+prefix+"  DONG:"+dong);
				if (rn.contains(dong)
						|| (prefix != null && rn.contains(prefix))) {
					return road;
				}
			}

			return null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void limited(String key) {
		keys.remove(key);		
	}

	private String nextKey() {
		String key = keys.get(nextKeyIndex);
		nextKeyIndex++;
		if (nextKeyIndex == keys.size()) {
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
		System.out.println(c.convert("대전 서구", "우고리 465"));

	}

}
