package net.narusas.si.auction.fetchers;

import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.자동차;

public class 자동차표시목록Builder {

	public void build(String html, Sheet sheet, 물건 goods) {
		String 목록번호 = HTMLUtils.findTHAndNextValue(html, "목록번호");
		String 감정평가액 = HTMLUtils.findTHAndNextValue(html, "감정평가액");
		String 차명 = HTMLUtils.findTHAndNextValue(html, "차명");
		String 차종 = HTMLUtils.findTHAndNextValue(html, "차종");
		String 등록번호 = HTMLUtils.findTHAndNextValue(html, "등록번호");
		String 연식 = HTMLUtils.findTHAndNextValue(html, "연식");
		String 제조사 = HTMLUtils.findTHAndNextValue(html, "제조사");
		String 연료종류 = HTMLUtils.findTHAndNextValue(html, "연료종류");
		String 변속기 = HTMLUtils.findTHAndNextValue(html, "변속기");
		String 원동기형식 = HTMLUtils.findTHAndNextValue(html, "원동기형식");
		String 승인번호 = HTMLUtils.findTHAndNextValue(html, "승인번호");
		String 차대번호 = HTMLUtils.findTHAndNextValue(html, "차대번호");
		String 배기량 = HTMLUtils.findTHAndNextValue(html, "배기량");
		String 주행거리 = HTMLUtils.findTHAndNextValue(html, "주행거리");
		String 보관장소 = HTMLUtils.findTHAndNextValueAsComplex(html, "보관장소");
		String 상세내역 = HTMLUtils.findTHAndNextValue(html, "상세내역");
		자동차 자동차 = new 자동차(goods, 목록번호, 감정평가액, 차명, 차종, 등록번호, 연식, 제조사, 연료종류, 변속기, 원동기형식, 승인번호, 차대번호, 배기량, 주행거리, 상세내역,
				보관장소);
		goods.add자동차(자동차) ;
	}

}
