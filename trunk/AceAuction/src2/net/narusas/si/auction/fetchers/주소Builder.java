package net.narusas.si.auction.fetchers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.지역;
import net.narusas.si.auction.model.dao.지역Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 주소Builder {
	final Logger logger = LoggerFactory.getLogger("auction");

	void 지역정보갱신() {
		
		if (지역.최상위지역 == null || 지역.최상위지역.size()==0) {
			logger.info("지역 DB를 읽어옵니다. ");
			지역Dao dao = (지역Dao) App.context.getBean("지역DAO");
			List<지역> 최상위지역 = new LinkedList<지역>();
			for (int i = 2; i <= 18; i++) {
				최상위지역.add(dao.get(i));
			}
			지역.최상위지역 = 최상위지역;
		}
	}

	public 주소 parse(String 소재지) {
		logger.info("주소지를 분석합니다 :" + 소재지);
		
		
		주소 addr = new 주소();
		addr.set주소(소재지);
		
		
		if (소재지.trim().startsWith("(")) {
			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
		}
		지역정보갱신();
		

		String[] tokens = 소재지.split(" ");

		String target시도 = tokens[0];
		String target시군구 = tokens[1];
		String target읍면동 = tokens[2];
		String target번지이하 = "";
		if (tokens.length >= 4) {
			target번지이하 = tokens[3];
		}

		지역Dao dao = (지역Dao) App.context.getBean("지역DAO");

		// 최상위 지역 검색.
		지역 parent = null;
		for (지역 area : 지역.최상위지역) {
			if (area.match지역명(target시도)) {
				parent = area;
				logger.info("시도 코드:" + area);
				addr.set시도(area);
				break;
			}
		}

		if (parent == null) {
			target시군구 = target시도;
			String token = tokens[1];
			if (token.endsWith("군") || token.endsWith("구")) {
				target시군구 = tokens[0] + " " + tokens[1];
			}

			for (지역 area : 지역.최상위지역) {
				지역 subArea = dao.findSub(area, target시군구);
				if (subArea == null) {
					continue;
				}
				String[] temp = new String[tokens.length + 1];
				System.arraycopy(tokens, 0, temp, 1, tokens.length);
				temp[0] = area.get지역명();
				tokens = temp;
				target시군구 = tokens[1];
				target읍면동 = tokens[2];

				logger.info("시도 코드:" + area);
				parent = area;
				addr.set시도(area);

				break;
			}
		}

		if (parent == null) {
			parent = dao.match(target시도);
		}

		if (parent == null) {
			logger.info("지역명을 DB에서 검색할수 없습니다");
			return null;
		}

		// 시군구 검색

		// 시군구 검색은 "성남시 분당구"처럼 두개의 단어로 된것 부터 검색한다.

		String token = tokens[2];
		if (token.endsWith("군") || token.endsWith("구")) {
			target시군구 = tokens[1] + " " + tokens[2];
			target읍면동 = tokens[3];
			target번지이하 = tokens[4];
		}

		Pattern 신규주소명_길번호_규칙 = Pattern.compile("(\\d+),?.*");
		Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
		if ((target읍면동.endsWith("로") || target읍면동.endsWith("길")) && m1.find()) {
			target읍면동 = target읍면동 + " " + m1.group(1);
		}

		System.out.println("## " + target시군구);
		지역 res = dao.findSub(parent, target시군구);
		if (res == null) {
			지역 시군구 = new 지역();
			시군구.set상위지역Code(parent.getId());
			시군구.set지역명(target시군구);
			dao.save(시군구);
			res = 시군구;
		}
		parent = res;
		logger.info("시군구 코드:" + parent + " " + parent.getId());
		addr.set시군구(parent);

		// 읍면동 검색
		res = dao.findSub(parent, target읍면동);
		if (res == null) {
			logger.info("DB에 해당 (" + target읍면동 + ") 주소에 대한 읍면동 내용이 없습니다. ");
			지역 읍면동 = new 지역();
			읍면동.set상위지역Code(parent.getId());
			읍면동.set지역명(target읍면동);
			dao.save(읍면동);
			res = 읍면동;
		}
		parent = res;
		logger.info("읍면동 코드:" + parent);
		addr.set읍면동(parent);

		// 최하위 주소 분석.
		String 번지이하 = 소재지.substring(소재지.indexOf(target읍면동) + target읍면동.length()).trim();
		if (번지이하.startsWith(",")) {
			번지이하 = 번지이하.substring(1).trim();
		}
		logger.info("번지이하 :" + 번지이하);
		addr.set번지이하(번지이하);

		
		return addr;
	}

}
