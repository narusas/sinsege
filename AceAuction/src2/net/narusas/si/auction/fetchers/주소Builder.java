package net.narusas.si.auction.fetchers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.AddressBuilder.통합주소;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.지역;
import net.narusas.si.auction.model.dao.지역Dao;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class 주소Builder {
	final Logger logger = LoggerFactory.getLogger("auction");
	AddressBuilder 주소통합Builder = new AddressBuilder();
	주소승격Converter 주소승격Converter = new 주소승격Converter();

	void 지역정보갱신() {

		if (지역.최상위지역 == null || 지역.최상위지역.size() == 0) {
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

		통합주소 _통합주소 = 주소통합Builder.parse소재지(소재지);
		logger.info("통합주소 :" + _통합주소);
		주소 addr = new 주소();
		addr.set통합주소(_통합주소);

		if (소재지.trim().startsWith("(")) {
			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
		}
		addr.set주소(소재지);
		지역정보갱신();

		지역Dao dao = (지역Dao) App.context.getBean("지역DAO");

		// 최상위 지역 검색.
		지역 parent = null;
		for (지역 area : 지역.최상위지역) {
			if (area.match지역명(_통합주소.시도)) {
				parent = area;
				logger.info("시도 코드:" + area);
				addr.set시도(area);
				break;
			}
		}

		if (parent == null) {
			logger.info("지역명을 DB에서 검색할수 없습니다");
			return null;
		}

		// 시군구 검색

		if (_통합주소.시군구그룹 != null) {
			logger.info("시군구 그룹을 검색합니다. :" + _통합주소.시군구그룹);
			지역 그룹 = dao.findSub(parent, _통합주소.시군구그룹);
			logger.info("검색된 시군구 그룹 코드:" + (그룹 == null ? "검색실패.  해당 시군구 그룹을 수동으로 추가해주세요" : 그룹.getId()));
			addr.set시군구그룹(그룹);
		}

		지역 res = null;
		// 시군구 검색은 "성남시 분당구"처럼 두개의 단어로 된것 부터 검색한다.
		if (_통합주소.시군구 != null) {
			res = dao.findSub(parent, _통합주소.시군구);
			if (res == null) {
				지역 시군구 = new 지역();
				시군구.set상위지역Code(parent.getId());
				시군구.set지역명(_통합주소.시군구);
				dao.save(시군구);
				res = 시군구;
			}

			if (주소승격Converter.is승격됨(res.getId())) {
				logger.info("승격된  시군구 주소입니다.");
				int 승격된code = 주소승격Converter.지역코드(res.getId());
				String 승격된지명 = 주소승격Converter.지역명(res.getId(), res.get지역명());
				_통합주소.시군구 = 승격된지명;
				res = dao.get(승격된code);
			}

			parent = res;
			logger.info("시군구 코드:" + parent + " " + parent.getId() + "  지역명:" + parent.get지역명());
			addr.set시군구(parent);
		}

		res = null;

		// 읍면동 검색
		if (_통합주소.읍면 != null) {
			res = dao.findSub(parent, _통합주소.읍면);
			if (res == null) {
				res = dao.findSub(parent, _통합주소.읍면);
				logger.info("DB에 해당 (" + _통합주소.읍면 + ") 주소에 대한 읍면동 내용이 없습니다.  해당 읍면을 추가합니다.  ");
				지역 읍면동 = new 지역();
				읍면동.set상위지역Code(parent.getId());
				읍면동.set지역명(_통합주소.읍면);
				dao.save(읍면동);
				res = 읍면동;
			}
		} else {
			if (_통합주소.동리 != null) {
				res = dao.findSub(parent, _통합주소.동리);
				if (res == null) {
					res = dao.findSub(parent, _통합주소.동리);
					logger.info("DB에 해당 (" + _통합주소.동리 + ") 주소에 대한 동리 내용이 없습니다.  해당 동리을 추가합니다.  ");
					지역 읍면동 = new 지역();
					읍면동.set상위지역Code(parent.getId());
					읍면동.set지역명(_통합주소.동리);
					dao.save(읍면동);
					res = 읍면동;
				}
			}

		}

		if (res != null) {
			parent = res;
			logger.info("읍면동리 코드:" + parent);
			addr.set읍면동(parent);
		} else {
			addr.set읍면동(null);
		}

		if (_통합주소.번지 != null) {
			String temp1 = _통합주소.번지 == null ? "" : _통합주소.번지;
			String temp2 = _통합주소.번지이하 == null ? "" : _통합주소.번지이하;
			String 번지이하 = (temp1 + " " + temp2).trim();

			// 최하위 주소 분석.
			logger.info("번지이하 :" + 번지이하);
			addr.set번지이하(번지이하);
		}

		// 세종시의 경우 area2 가 없고 area3 가 들어가기도 함
		// 이경우 area3 를 area2 로 승격 시킴
		if (addr.get시군구() == null) {
			addr.set시군구(addr.get읍면동());
		}

		if (StringUtils.isNotEmpty(_통합주소.도로명그룹)) {
			logger.info("도로명 그룹 :" + _통합주소.도로명그룹 + " 을 처리합니다. ");
			JdbcTemplate template = (JdbcTemplate) App.context.getBean("jdbcTemplate");
			String queryForRoad1Group = "select * from aceauction.ac_road_group where area1=" + addr.get시도().getId() + " and area2="
					+ addr.get시군구().getId() + " and name='" + _통합주소.도로명그룹 + "'";
			System.out.println(queryForRoad1Group);
			List result = template.queryForList(queryForRoad1Group);
			if (result == null || result.size() == 0) {
				String insertQuery ="insert into aceauction.ac_road_group(area1, area2, name) values (" + addr.get시도().getId() + ","
						+ addr.get시군구().getId() + ",'"+ _통합주소.도로명그룹 +"')";
				System.out.println(insertQuery);
				template.execute(insertQuery);
			}
			result = template.queryForList(queryForRoad1Group);
			Map map = (Map) result.get(0);
			_통합주소.도로명그룹id =((Integer) map.get("id")).longValue(); 
			
			logger.info("선택된 도로명 그룹은 "+_통합주소.도로명그룹id+" 입니다. ");
		}

		return addr;
	}

}
