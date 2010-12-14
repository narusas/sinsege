package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

public class AddressTool {
	public static class Entity {
		String code;
		String name;

		public Entity(String name, String code) {
			super();
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
		@Override
		public String toString() {
			return name+":"+code;
		}
	}

	private final RetFetcher fetcher;
	static Entity[] doList = new Entity[] { new Entity("서울", "11"), new Entity("부산", "26"), new Entity("대구", "27"),
			new Entity("인천", "28"), new Entity("광주", "29"), new Entity("대전", "30"), new Entity("울산", "31"),
			new Entity("경기", "41"), new Entity("강원", "42"), new Entity("충북", "43"), new Entity("충남", "44"),
			new Entity("전북", "45"), new Entity("전남", "46"), new Entity("경북", "47"), new Entity("경남", "48"),
			new Entity("제주", "50"), };

	public AddressTool(RetFetcher fetcher) {
		this.fetcher = fetcher;
	}

	public List<Entity> getCityList(Entity DO) {
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:ADDR.getCityList"),
							new NameValuePair("c0-e1", "string:" + DO.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{do_code:reference:c0-e1}"), }));

			return parseCity(html);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	Pattern p2 = Pattern.compile("CITY_NAME\":\"([^\"]+)\",\"CITY_CODE\":\"([^\"]+)");

	private List<Entity> parseCity(String html) {
		List<Entity> res = new ArrayList<Entity>();
		Matcher m2 = p2.matcher(html);
		while (m2.find()) {
			res.add(new Entity(m2.group(1), m2.group(2)));
		}
		return res;
	}

	public List<Entity> getDongList(Entity DO, Entity CITY) {
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:ADDR.getDongList"),
							new NameValuePair("c0-e1", "string:"+DO.getCode()),
							new NameValuePair("c0-e2", "string:"+CITY.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{do_code:reference:c0-e1,city_code:reference:c0-e2}"),
					}));

			return parseDong(html);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	Pattern p3 = Pattern.compile("DONG_NAME\":\"([^\"]+)\",\"DONG_CODE\":\"([^\"]+)");

	private List<Entity> parseDong(String html) {
		List<Entity> res = new ArrayList<Entity>();
		Matcher m2 = p3.matcher(html);
		while (m2.find()) {
			res.add(new Entity(m2.group(1), m2.group(2)));
		}
		return res;
	}


}
