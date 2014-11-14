package net.narusas.si.auction.builder;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.fetchers.대법원Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

public class 현장조사서이미지Batch implements ModeStrategy {
	final Logger logger = LoggerFactory.getLogger("auction");
	private 법원 법원;
	private String 사건번호Str;
	private String merged사건번호Strs;
	private 담당계 담당계;
	private String year;

	public 현장조사서이미지Batch(법원 법원, 담당계 담당계, String year, String 사건번호Str, String merged사건번호Strs) {
		this.법원 = 법원;
		this.담당계 = 담당계;
		this.year = year;
		this.사건번호Str = 사건번호Str;
		this.merged사건번호Strs = merged사건번호Strs;
	}

	@Override
	public boolean execute() {
		
		try {
			String target사건번호 =사건번호Str; 
			if ( StringUtils.isNotEmpty(merged사건번호Strs)){
				target사건번호  = merged사건번호Strs;
			}
			int index = 0;
			HashSet<String> urls = new HashSet<String>();
			for(int targetRow=1 ;targetRow<=5; targetRow++){
				logger.info(" 사진 페이지 "+ targetRow+" 에 접속합니다. ");
				String path = "/RetrieveSaPhotoInfo.laf?jiwonNm="+법원.get법원명UrlEncoded()+"&saNo="+target사건번호+"&targetRow="+targetRow+"&chcCd=000162&sourcOrdHoi=1&ordHoi=";
				String url = "https://www.courtauction.go.kr"+path;
				logger.info("현장조사서 사진  URL 에 접속합니다. "+ url);
				
				대법원Fetcher f = new 대법원Fetcher();
				String html = f.fetch(path);
				String imgUrl = "";
				Pattern imgPattern = Pattern.compile("(/DownFront[^\"]+)",Pattern.MULTILINE);
				if (html.contains("/DownFront")  == false) {
					logger.info(" 사진을 찾을수 없습니다. ");
					 return false;
//					//   이미지 경로가 있
//					//  사진의 출처가 다른 법원인 경우가 있음
//					Pattern p = Pattern.compile("name=\"jiwonNm\" value=\"([^\"]+)",Pattern.MULTILINE);
//					Matcher m = p.matcher(html);
//					if(m.find()==false) {
//						finded = false;
//						
//						 continue;
//					}
//					
//					String  법원명 = m.group(1);
//					logger.info(" 해당 사진은 "+법원명+" 에 속한 사진입니다. 다시 조회를 시도합니다.");
//					String  법원명Encoded =  HTMLUtils.encodeUrl(법원명);
//					path = "/RetrieveSaPhotoInfo.laf?jiwonNm="+법원명Encoded+"&saNo="+사건번호Str+"&targetRow=1&chcCd=000162&sourcOrdHoi=1&ordHoi=";
//					url = "https://www.courtauction.go.kr"+path;
//					logger.info("현장조사서 사진  URL 에 다시 접속합니다. "+ url);
//					html = f.fetch(path);
//					if (html.contains("/DownFront")  == false){ 
//						logger.info(" 사진을 찾을수 없습니다. ");
//						 continue;
//					}
//					
//					m = imgPattern.matcher(html);
//					m.find();
//					imgUrl = m.group(1);
				} 
				Matcher m = imgPattern.matcher(html);
				m.find();
				imgUrl = m.group(1);
				imgUrl = HTMLUtils.converHTMLSpecialChars(imgUrl);
				
				
				logger.info(" 다운로드 할 이미지의  URL 은 https://www.courtauction.go.kr"+  imgUrl+"  입니다." );
				if (urls.contains(imgUrl)){
					logger.info(" 이미 처리한 이미지  URL  입니다." );
					 continue;
				}
				urls.add(imgUrl);
				
				Random r = new Random(System.currentTimeMillis());
				File file = new File(Math.abs(r.nextInt())+".jpg");
				logger.info("다음 파일로 이미지를 다운로드 합니다. "+ file.getAbsolutePath());
				f.downloadBinary(imgUrl, file);
				
				StringBuffer buf = new StringBuffer(String.valueOf(법원.get법원코드()));
				for (int i = buf.length(); i < 6; i++) {
					buf.insert(0, '0');
				}
				String courtCode = buf.toString();
				String filePath = year + "/" + courtCode + "/" + removeDots(담당계.get매각기일().toString()) + "/"
						+ 담당계.get담당계코드() + "/" + 사건번호Str + "/";
				
				logger.info("이미지 업로드 경로:"+filePath);
				String uploadFileName = "pic_courtauction_"+ (index)+".jpg";
				index++;
//				String uploadFileName = "pic_courtauction_0.jpg";
				logger.info("이미지 파일명: "+uploadFileName);
				FileUploaderBG.getInstance().upload(filePath, uploadFileName, file);
				System.out.println(html);
			}
			
			
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	private String removeDots(String src) {
		return src.replaceAll(".", "").replace("-", "");
	}
}
