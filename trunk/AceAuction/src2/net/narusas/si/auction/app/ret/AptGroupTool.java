package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.app.ret.AddressTool.Entity;
import net.narusas.si.auction.model.단지;
import net.narusas.si.auction.model.단지평형;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

public class AptGroupTool {

	private final RetFetcher fetcher;

	public AptGroupTool(RetFetcher fetcher) {
		this.fetcher = fetcher;
	}

	public List<단지> fetch(Entity DO, Entity CITY, Entity DONG) {
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:RETPRICE_APT.getRetPriceAptList"),
							new NameValuePair("c0-e1", "string:" + DO.getCode() + CITY.getCode() + DONG.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{addr_code:reference:c0-e1}"), }));

			return parseCity(html);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	Pattern p = Pattern.compile("\\{(JUSO[^\\}]+)");
	
	private List<단지> parseCity(String html) {
		List<단지> res = new ArrayList<단지>();
		Matcher m = p.matcher(html);
		while (m.find()) {
			res.add(parse단지(m.group(1)));
		}
		return res;
	}

	public 단지 parse단지(String tmp) {
		// System.out.println(tmp);
		HashMap<String, String> pair = parsePairs(tmp);
		// System.out.println(pair);
		단지 group = new 단지(pair);
		return group;
	}
	
	Pattern p2 = Pattern.compile("((?i)[\"][^\"]+[\"]|[^:]+):((?i)[\"][^\"]+[\"]|[^,]+)[,]*");
	private HashMap<String, String> parsePairs(String tmp) {
		Matcher m2 = p2.matcher(tmp);
		HashMap<String, String> pair = new HashMap<String, String>();
		while (m2.find()) {
			pair.put(trim(m2.group(1)), trim(m2.group(2).trim()));
		}
		return pair;
	}

	private String trim(String src) {
		return src.replace('\"', ' ').trim();
	}

	public void updateInfo(단지 group) {
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:APT.getAptInfo"),
							new NameValuePair("c0-e1", "string:" + group.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"), }));
//			System.out.println(html);
			group.updateInfo(parsePairs(html));

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<단지평형> fetch평형종류(단지 group) {
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_Area"),
							new NameValuePair("c0-e1", "string:" + group.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"), }));

			return parseAreaType(group, html);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	Pattern p3 = Pattern.compile("\\{(\"SL_PRICE[^\\}]+)");
	public List<단지평형> parseAreaType(단지 단지, String html) {
//		System.out.println(html);
		Matcher m = p3.matcher(html);
		List<단지평형> res = new ArrayList<단지평형>();
		while(m.find()){
			res.add(new  단지평형(단지, parsePairs(m.group(1))));
		}
		return res;
	}
	
	public void update단지평형(단지 group,  List<단지평형> areas  ){
		try {
			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
							new NameValuePair("c0-methodName", "queryForList"),
							new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_info2"),
							new NameValuePair("c0-e1", "string:" + group.getCode()),
							new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"), }));

			updateAreaAdditionalData(html, group, areas);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Pattern p4 = Pattern.compile("\\{(\"SP_LIVING[^\\}]+)");
	
	private void updateAreaAdditionalData(String html, 단지 group, List<단지평형> areas) {
//		System.out.println(html);
		Matcher m = p4.matcher(html);
		while(m.find()){
			HashMap<String, String> pairs = parsePairs(m.group(1));
			String pyn_type = pairs.get("PYN_TYPE");
			for (단지평형 area : areas) {
				if (pyn_type.equals(area.getPYN_TYPE())){
					area.update(pairs);
				}
			}
		}
	}
	
	
//	public List<단지평형시세추이> fetch시세추이(단지 group, 단지평형  type) {
//		System.out.println("#########:"+group.getCode()+"       "+type.getCode());
//		try {
//			String html = fetcher.fetch("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", Arrays
//					.asList(new NameValuePair[] { new NameValuePair("c0-scriptName", "KabnetService"),
//							new NameValuePair("c0-methodName", "queryForList"),
//							new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_info"),
//							new NameValuePair("c0-e1", "string:" + group.getCode()),
//							new NameValuePair("c0-e2", "number:" + type.getCode()),
//							new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1, pyn_type:reference:c0-e2}"), 
//			 }));
//
//			return parse시세추이(html);
//
//		} catch (HttpException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	Pattern p4 = Pattern.compile("\\{(\"RU_COLOR[^\\}]+)");
//	
//	private List<단지평형시세추이> parse시세추이(String html) {
//		System.out.println(html);
//		Matcher m = p4.matcher(html);
//		List<단지평형시세추이> res = new ArrayList<단지평형시세추이>();
//		while(m.find()){
//			res.add(new  단지평형시세추이(parsePairs(m.group(1))));
//		}
//		return res;
//	}

}
