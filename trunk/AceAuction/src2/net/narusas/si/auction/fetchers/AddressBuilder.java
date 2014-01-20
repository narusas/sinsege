package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressBuilder {
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
		public String 동주소() {
			if ( 번지 ==  null){
				return 동리;
			}
			return 동리+" "+번지;
		}
		public String 도로명주소() {
			StringBuffer buf = new StringBuffer(도로명);
			if (건물주번 != null) {
				buf.append(" ").append(건물주번);
				if (건물부번!=null){
					buf.append("-").append(건물부번);
				}
			}
			
			return buf.toString();
		}
	}
	
	
	public AddressBuilder() {
		roadConverter =  new RoadConverter();
	}
	
	public 통합주소 parse소재지(String  address) {
		통합주소 _주소 = parseAddress(address);
		if (_주소.도로명 ==  null && _주소.번지 != null){
			
			String dong =_주소.동리+" "+_주소.번지;
			System.out.println("## DONG:"+dong);
			String roadAddress = roadConverter.convert(_주소.시군구, dong);
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
		String etcFirst = null;
		if (address.contains(", ")){
			etcFirst = address.substring(address.indexOf(", "));
			address = address.substring(0,address.indexOf(", "));		
		}
		{
			Matcher m = Pattern.compile("([^\\s]+)\\s+(.*)").matcher(address);
			m.find();
			String  시도 = m.group(1);
			if( is시도(시도)){
				_주소.시도 = m.group(1);
				temp = m.group(2);
			}
			else {
				temp = address;
			}
		}
		{
			Matcher m = Pattern.compile("(.+(구|군))\\s+(.*)").matcher(temp);
			if (m.find()){
				_주소.시군구 = m.group(1);
				temp = m.group(3);
			}
			else {
				m = Pattern.compile("([^\\s]+시)\\s+(.*)").matcher(temp);
				if (m.find()) {
					_주소.시군구 = m.group(1);
					temp = m.group(2);	
				}
				
			}
		}
		
		
		
		{
			Matcher m = Pattern.compile("([^\\s]+로\\s?[\\d]+번길)\\s?([\\d-]*)").matcher(temp);
			if (m.find()){
				_주소.도로명 = m.group(1).replaceAll("\\s+", "");
				temp = temp.replace(m.group(1), "");
				String ttt = m.group(2);
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
					temp = temp.replace(numStrM.group(0), "");
				}
				else {
				}
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
				
				if (_주소.도로명.contains(".")){
					_주소.도로명 = _주소.도로명.replace('.', '·');
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
			System.out.println("#-"+temp+"-#");
			
			if (temp.contains("(")){
				saved = temp;
				temp = saved.substring(0, saved.indexOf("(")).trim();
				etc = saved.substring(saved.indexOf("(")).trim();
			}
			
			System.out.println("##########"+temp+"#");
			Matcher m = Pattern.compile("([^\\s\\(\\d]*(읍|면))[\\s]+(.*)").matcher(temp);
			if (m.find()) {
//				System.out.println("#----"+_주소.도로명);
				_주소.읍면 = m.group(1);
				String ttt = "".equals(m.group(3)) ? null : m.group(3).trim();
//				System.out.println("###1 "+ttt);
				if (ttt != null && ttt.startsWith(",")){
					ttt = ttt.substring(1).trim();
				}
				temp  = temp.replace(_주소.읍면, "").trim();
			}
			else {
				Matcher m2 = Pattern.compile("([^\\s\\(\\d]*(읍|면))$").matcher(temp);
				if (m2.find()){
					_주소.읍면 = m2.group(1);
					temp  = temp.replace(_주소.읍면, "").trim();
				}
			}
		}
		{
			temp = temp.trim();
			if (temp.startsWith(",")){
				temp = temp.substring(1).trim();
			}
//			System.out.println("############"+temp);
			Matcher m = Pattern.compile("([^\\s\\(\\d]+[\\d]*(동|리|가))[\\s$]+(.*)").matcher(temp);
			if (m.find()) {
//				System.out.println("#----"+_주소.도로명);
				_주소.동리 = m.group(1);
				String ttt = "".equals(m.group(3)) ? null : m.group(3).trim();
//				System.out.println("###1 "+ttt);
				if (ttt.contains(_주소.동리)){
					ttt = ttt.replace(_주소.동리, "").trim();
				}
				if (ttt != null && ttt.startsWith(",")){
					ttt = ttt.substring(1).trim();
				}
//				System.out.println("###2 "+ttt);
				
				if (_주소.도로명 == null){
					if (ttt != null){
						Matcher m2 = Pattern.compile("^([산]?[\\d-]+)\\s?(.*)").matcher(ttt);
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
				if (etcFirst != null){
					_주소.번지이하 =etcFirst.substring(2);
				}

			}
			
		}
		return _주소;
	}

	private boolean is시도(String 시도) {
		return 시도.contains("서울") 
				||시도.contains("경기")
				||시도.contains("경상")
				||시도.contains("전라")
				||시도.contains("충청")
				||시도.contains("강원")
				||시도.contains("제주")
				
				||시도.contains("광주")
				||시도.contains("대구")
				||시도.contains("대전")
				||시도.contains("부산")
				||시도.contains("울산")
				||시도.contains("인천")
				||시도.contains("세종")
				;  
	}
}
