package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 주소통합Builder {
	RoadConverter roadConverter;
	
	public static class 통합주소 {
		public String 시도;
		public String 시군구;
		public String 읍면;
		public String  동리;
		public String 번지;
		public String 번지이하;
		public String  도로명;
		public Integer 건물주번;
		public Integer 건물부번;
		@Override
		public String toString() {
			return "주소 [시도=" + 시도 + ", 시군구=" + 시군구 + ", 읍면=" + 읍면 + ", 동리=" + 동리 + ", 번지=" + 번지 + ", 번지이하=" + 번지이하 + ", 도로명=" + 도로명	+ ", 건물주번=" + 건물주번 + ", 건물부번=" + 건물부번 + "]";
		}
	}
	
	
	public 주소통합Builder() {
		roadConverter =  new RoadConverter();
	}
	
	public 통합주소 parse소재지(String  address) {
		통합주소 _주소 = parseAddress(address);
		if (_주소.도로명 ==  null){
			String dong =_주소.동리+" "+_주소.번지;
			System.out.println("## DONG:"+dong);
			String roadAddress = roadConverter.convert(dong);
			System.out.println("## ROAD:"+roadAddress);
			if(roadAddress != null){
				통합주소 addr = parseAddress(roadAddress);
				 _주소.도로명 =  addr.도로명;
				 _주소.건물주번=  addr.건물주번;
				 _주소.건물부번=  addr.건물부번;
				 
				 
			}
		}
			
		return _주소;
	}

	private 통합주소 parseAddress(String address) {
		if (address.trim().startsWith("(")) {
			address = address.substring(address.indexOf(")") + 1).trim();
		}
		String temp = null;
		통합주소 _주소 = new 통합주소();
		{
			Matcher m = Pattern.compile("([^\\s]+)\\s+(.*)").matcher(address);
			m.find();
			_주소.시도 = m.group(1);
			temp = m.group(2);
		}
		{
			Matcher m = Pattern.compile("(.+(구|군))\\s+(.*)").matcher(temp);
			if (m.find()){
				_주소.시군구 = m.group(1);
				temp = m.group(3);
			}
			else {
				m = Pattern.compile("([^\\s]+시)\\s+(.*)").matcher(temp);
				m.find();
				_주소.시군구 = m.group(1);
				temp = m.group(2);
			}
		}
		
		
		
		{
			Matcher m = Pattern.compile("([^\\s]+(로|길|거리))\\s+([^,]*)").matcher(temp);
			if (m.find()){
				_주소.도로명 = m.group(1);
				String ttt = m.group(3);
				Matcher numStrM = Pattern.compile("^([\\d-]+)").matcher(ttt);
				if  (numStrM.find()){
					String numStr = numStrM.group(1);
					Matcher numM = Pattern.compile("([\\d]+)").matcher(ttt);
					if (numM.find()){
						_주소.건물주번 = Integer.parseInt(numM.group(1));
					}
					if (numM.find()){
						_주소.건물부번  = Integer.parseInt(numM.group(1));
					}
					temp = temp.replace(_주소.도로명+" "+numStrM.group(0), "");
				}
				else {
					temp = temp.replace(_주소.도로명, "");
				}
			}
		}
		String saved = null;
		String etc = null;
		{
			temp = temp.trim();
			if (temp.startsWith(",")){
				temp = temp.substring(1).trim();
			}
			System.out.println("#"+temp+"#");
			
			if (temp.contains("(")){
				saved = temp;
				temp = saved.substring(0, saved.indexOf("(")).trim();
				etc = saved.substring(saved.indexOf("(")).trim();
			}
			
			System.out.println("##########"+temp);
			Matcher m = Pattern.compile("([^\\s\\(\\d]+(읍|면))\\s?(.*)").matcher(temp);
			if (m.find()) {
				System.out.println("#----"+_주소.도로명);
				_주소.읍면 = m.group(1);
				String ttt = "".equals(m.group(3)) ? null : m.group(3).trim();
				System.out.println("###1 "+ttt);
				if (ttt != null && ttt.startsWith(",")){
					ttt = ttt.substring(1).trim();
				}
				temp  = temp.replace(_주소.읍면, "").trim();
			}
		}
		{
			temp = temp.trim();
			if (temp.startsWith(",")){
				temp = temp.substring(1).trim();
			}
			System.out.println("############"+temp);
			Matcher m = Pattern.compile("([^\\s\\(\\d]+(동|리))\\s?(.*)").matcher(temp);
			if (m.find()) {
				System.out.println("#----"+_주소.도로명);
				_주소.동리 = m.group(1);
				String ttt = "".equals(m.group(3)) ? null : m.group(3).trim();
				System.out.println("###1 "+ttt);
				if (ttt != null && ttt.startsWith(",")){
					ttt = ttt.substring(1).trim();
				}
				System.out.println("###2 "+ttt);
				
				if (_주소.도로명 == null){
					if (ttt != null){
						Matcher m2 = Pattern.compile("^([\\d-]+)\\s?(.*)").matcher(ttt);
						if (m2.find()){
							_주소.번지 = m2.group(1);
							_주소.번지이하  = "".equals(m2.group(2).trim()) ? null :  m2.group(2).trim();
						}
						else {
							_주소.번지이하 =ttt; 
						}
					}
									
					if (etc != null){
						_주소.번지이하 +=" "+etc;
					}
					temp = m.group(3);
				}
				else {
					_주소.번지이하  = ttt;
					if (etc != null){
						_주소.번지이하 +=" "+etc;
					}
				}
			}
			else {
				_주소.번지이하  = "".equals(temp) ? null : temp;
				if (etc != null){
					_주소.번지이하 +=" "+etc;
				}	

			}
			
		}
		return _주소;
	}
}
