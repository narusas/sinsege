package net.narusas.si.auction.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class 물건 {

	Integer id;
	법원 법원;
	담당계 담당계;
	사건 사건;
	Integer 물건번호;
	기일내역 기일내역;
	String 감정평가액;
	String 물건비고;
	String 소재지;
	boolean 완료여부;
	Integer 물건종별Code;
	String 매각대상;
	Long 감정가;
	Long 최저가;
	Date 사건접수일;
	Date 경매개시결정일;
	Date 배당요구종기일;
	Date 입찰시작일;
	Date 입찰종료일;
	Integer 조회수 = 0;
	Integer 유찰수;
	Integer 보증율;
	String 비고;
	기계기구 기계기구;
	매각물건명세서비고 매각물건명세서비고;
	건물현황 건물현황;
	대지권현황 대지권현황;
	물건감정평가서 감정평가서;

	매각기일결과 매각기일결과;

	지역 지역_도;
	지역 지역_시군구;
	지역 지역_동읍면;
	Integer road1;
	Integer road2;
	Integer road3;
	



	public Integer getRoad1() {
		return road1;
	}

	public void setRoad1(Integer road1) {
		this.road1 = road1;
	}

	public Integer getRoad2() {
		return road2;
	}

	public void setRoad2(Integer road2) {
		this.road2 = road2;
	}

	public Integer getRoad3() {
		return road3;
	}

	public void setRoad3(Integer road3) {
		this.road3 = road3;
	}

	String 사진1, 사진2;

	private Collection<부동산표시> 부동산표시목록;
	private Collection<토지> 토지목록;
	private Collection<건물> 건물목록;
	private Collection<대지권> 대지권목록;
	private Collection<제시외건물> 제시외건물목록;
	private Collection<매각물건명세서> 매각물건명세목록;

	private List<자동차> 자동차목록;
	private Collection<선박> 선박목록;

	private String 기일결과;
	private String 기일날자;

	String 기일Start, 기일End;
	private String 매각가격;
	private Collection<물건인근매각통계Item> 인근매각통계;
	private ArrayList<목록> 목록s;
	private String 최선순위설정일자;

	public 물건() {
		건물현황 = new 건물현황();
		대지권현황 = new 대지권현황();
		부동산표시목록 = new LinkedList<부동산표시>();
		토지목록 = new LinkedList<토지>();
		매각기일결과 = new 매각기일결과();
		감정평가서 = new 물건감정평가서();
		기계기구 = new 기계기구();
		// 물건종별 = new 물건종별();
	}

	public 법원 get법원() {
		return 법원;
	}

	public void set법원(법원 법원) {
		this.법원 = 법원;
	}

	public 담당계 get담당계() {
		return 담당계;
	}

	public void set담당계(담당계 담당계) {
		this.담당계 = 담당계;
	}

	public 사건 get사건() {
		return 사건;
	}

	public void set사건(사건 사건) {
		this.사건 = 사건;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void set감정평가액(String 감정평가액) {
		this.감정평가액 = 감정평가액;
	}

	public String get감정평가액() {
		return 감정평가액;
	}

	public void add기일내역(기일 기일내역Item) {
		if (기일내역 == null) {
			기일내역 = new 기일내역();
		}
		기일내역Item.set물건번호(get물건번호());
		기일내역.add(기일내역Item);
	}

	public 기일내역 get기일내역() {
		return 기일내역;
	}

	public void set기일내역(기일내역 기일내역) {
		this.기일내역 = 기일내역;
		if (기일내역 == null) {
			return;
		}
		for (기일 it : 기일내역.기일목록) {
			it.set물건(this);
		}
	}

	public String get물건비고() {
		return 물건비고;
	}

	public void set물건비고(String 물건비고) {
		this.물건비고 = 물건비고;
	}

	public String get소재지() {
		return 소재지;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	public boolean is완료여부() {
		return 완료여부;
	}

	public void set완료여부(boolean 완료여부) {
		this.완료여부 = 완료여부;
	}

	public Integer get물건종별() {
		return 물건종별Code;
	}

	public void set물건종별(Integer 물건종별Code) {
		this.물건종별Code = 물건종별Code;
	}

	public String get매각대상() {
		return 매각대상;
	}

	public void set매각대상(String 매각대상) {
		this.매각대상 = 매각대상;
	}

	public Long get감정가() {
		return 감정가;
	}

	public void set감정가(Long 감정가) {
		this.감정가 = 감정가;
	}

	public Long get최저가() {
		return 최저가;
	}

	public void set최저가(Long 최저가) {
		this.최저가 = 최저가;
	}

	public Date get사건접수일() {
		return 사건접수일;
	}

	public void set사건접수일(Date 사건접수일) {
		this.사건접수일 = 사건접수일;
	}

	public Date get경매개시결정일() {
		return 경매개시결정일;
	}

	public void set경매개시결정일(Date 경매개시결정일) {
		this.경매개시결정일 = 경매개시결정일;
	}

	public Date get배당요구종기일() {
		return 배당요구종기일;
	}

	public void set배당요구종기일(Date 배당요구종기일) {
		this.배당요구종기일 = 배당요구종기일;
	}

	public Date get입찰시작일() {
		return 입찰시작일;
	}

	public void set입찰시작일(Date 입찰시작일) {
		this.입찰시작일 = 입찰시작일;
	}

	public Date get입찰종료일() {
		return 입찰종료일;
	}

	public void set입찰종료일(Date 입찰종료일) {
		this.입찰종료일 = 입찰종료일;
	}

	public Integer get조회수() {
		return 조회수;
	}

	public void set조회수(Integer 조회수) {
		this.조회수 = 조회수;
	}

	public Integer get유찰수() {
		return 유찰수;
	}

	public void set유찰수(Integer 유찰수) {
		this.유찰수 = 유찰수;
	}

	public String get비고() {
		return 비고;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public 기계기구 get기계기구() {
		return 기계기구;
	}

	public void set기계기구(기계기구 기계기구) {
		this.기계기구 = 기계기구;
	}

	public 건물현황 get건물현황() {
		return 건물현황;
	}

	public void set건물현황(건물현황 건물현황) {
		this.건물현황 = 건물현황;
	}

	public 대지권현황 get대지권현황() {
		return 대지권현황;
	}

	public void set대지권현황(대지권현황 대지권현황) {
		this.대지권현황 = 대지권현황;
	}

	public 물건감정평가서 get감정평가서() {
		return 감정평가서;
	}

	public void set감정평가서(물건감정평가서 감정평가서) {
		this.감정평가서 = 감정평가서;
	}

	public 매각기일결과 get매각기일결과() {
		return 매각기일결과;
	}

	public void set매각기일결과(매각기일결과 매각기일결과) {
		this.매각기일결과 = 매각기일결과;
	}

	public Integer get물건번호() {
		return 물건번호;
	}

	public void set물건번호(Integer 물건번호) {
		this.물건번호 = 물건번호;
	}

	public String get사진1() {
		return 사진1;
	}

	public void set사진1(String 사진1) {
		this.사진1 = 사진1;
	}

	public String get사진2() {
		return 사진2;
	}

	public void set사진2(String 사진2) {
		this.사진2 = 사진2;
	}

	public 지역 get지역_도() {
		return 지역_도;
	}

	public void set지역_도(지역 지역_도) {
		this.지역_도 = 지역_도;
	}

	public 지역 get지역_시군구() {
		return 지역_시군구;
	}

	public void set지역_시군구(지역 지역_시군구) {
		this.지역_시군구 = 지역_시군구;
	}

	public 지역 get지역_동읍면() {
		return 지역_동읍면;
	}

	public void set지역_동읍면(지역 지역_동읍면) {
		this.지역_동읍면 = 지역_동읍면;
	}

	public String get기일Start() {
		return 기일Start;
	}

	public void set기일Start(String start) {
		기일Start = start;
	}

	public String get기일End() {
		return 기일End;
	}

	public void set기일End(String end) {
		기일End = end;
	}

	@Override
	public String toString() {
		return ""+get물건번호();
	}

	public void add부동산표시(부동산표시 _표시) {
		if (부동산표시목록 == null) {
			부동산표시목록 = new LinkedList<부동산표시>();
		}
		for (부동산표시 표시 : 부동산표시목록) {
			if (_표시.get목록번호() == 표시.get목록번호()) {
				return;
			}
		}
		부동산표시목록.add(_표시);
	}
	
	public void add부동산표시(int no, 주소 주소, String 공시지가) {
		if (부동산표시목록 == null) {
			부동산표시목록 = new LinkedList<부동산표시>();
		}
		for (부동산표시 표시 : 부동산표시목록) {
			if (no == 표시.get목록번호()) {
				return;
			}
		}
		부동산표시 표시 = new 부동산표시();
		표시.set목록번호(no);
		표시.set주소(주소);
		표시.set공시지가(공시지가);
		부동산표시목록.add(표시);
	}


	public Collection<부동산표시> get부동산표시목록() {
		return 부동산표시목록;
	}

	public void set부동산표시목록(LinkedList<부동산표시> 부동산표시목록) {
		this.부동산표시목록 = 부동산표시목록;
	}

	public 부동산표시 get부동산표시(int 목록번호) {
		for (부동산표시 표시 : 부동산표시목록) {
			if (목록번호 == 표시.get목록번호()) {
				return 표시;
			}
		}
		return null;
	}

	public void add토지(토지 토지) {
		if (토지목록 == null) {
			토지목록 = new LinkedList<토지>();
		}
		토지목록.add(토지);

	}

	public Collection<토지> get토지목록() {
		return 토지목록;
	}

	public void set토지목록(Collection<토지> 토지목록) {
		this.토지목록 = 토지목록;
	}

	public Collection<건물> get건물목록() {
		return 건물목록;
	}

	public void set건물목록(Collection<건물> 건물목록) {
		this.건물목록 = 건물목록;
	}

	public void add건물(건물 bld) {
		if (건물목록 == null) {
			건물목록 = new LinkedList<건물>();
		}
		bld.set물건(this);
		건물목록.add(bld);
	}

	public void add대지권(대지권 bld) {
		if (대지권목록 == null) {
			대지권목록 = new LinkedList<대지권>();
		}
		bld.set물건(this);
		대지권목록.add(bld);
	}

	public Collection<대지권> get대지권목록() {
		return 대지권목록;
	}

	public void set대지권목록(Collection<대지권> 대지권목록) {
		this.대지권목록 = 대지권목록;
	}

	public void add제시외건물(제시외건물 제시외건물) {
		if (제시외건물목록 == null) {
			제시외건물목록 = new LinkedList<제시외건물>();
		}
		제시외건물목록.add(제시외건물);
	}

	public void add자동차(자동차 자동차) {
		if (자동차목록 == null) {
			자동차목록 = new LinkedList<자동차>();
		}
		Iterator<자동차> it = 자동차목록.iterator();
		while (it.hasNext()) {
			자동차 자동차2 = (자동차) it.next();
			if (자동차2.get목록번호() != null && 자동차2.get목록번호().equals(자동차.get목록번호())) {
				return;
			}
		}
		자동차.set물건(this);
		자동차목록.add(자동차);
	}

	public List<자동차> get자동차목록() {
		return 자동차목록;
	}

	public void set자동차목록(List<자동차> 자동차목록) {
		this.자동차목록 = 자동차목록;
	}

	public void add선박(선박 선박) {
		if (선박목록 == null) {
			선박목록 = new LinkedList<선박>();
		}
		Iterator<선박> it = 선박목록.iterator();
		while (it.hasNext()) {
			선박 선박2 = (선박) it.next();
			if (선박2.get목록번호().equals(선박.get목록번호())) {
				return;
			}
		}
		선박목록.add(선박);
	}

	public Collection<선박> get선박목록() {
		return 선박목록;
	}

	public void set선박목록(Collection<선박> 선박목록) {
		this.선박목록 = 선박목록;
	}

	public Collection<제시외건물> get제시외건물목록() {
		return 제시외건물목록;
	}

	public void set제시외건물목록(Collection<제시외건물> 제시외건물목록) {
		this.제시외건물목록 = 제시외건물목록;
	}

	public void set매각물건명세목록(Collection<매각물건명세서> 매각물건명세목록) {
		this.매각물건명세목록 = 매각물건명세목록;
		for (매각물건명세서 매각물건명세서 : 매각물건명세목록) {
			매각물건명세서.set물건(this);
		}
	}

	public 매각물건명세서비고 get매각물건명세서비고() {
		return 매각물건명세서비고;
	}

	public void set매각물건명세서비고(매각물건명세서비고 매각물건명세서비고) {
		this.매각물건명세서비고 = 매각물건명세서비고;
	}

	public Collection<매각물건명세서> get매각물건명세목록() {
		return 매각물건명세목록;
	}

	public void set부동산표시목록(Collection<부동산표시> 부동산표시목록) {
		this.부동산표시목록 = 부동산표시목록;
	}

	public void set기일결과(String 기일결과) {
		this.기일결과 = 기일결과;

	}

	public String get기일결과() {
		return 기일결과;
	}

	public Integer get보증율() {
		return 보증율;
	}

	public void set보증율(Integer 보증율) {
		this.보증율 = 보증율;
	}

	public void set기일날자(String 기일날자) {
		this.기일날자 = 기일날자;
	}

	public String get기일날자() {
		return 기일날자;
	}

	public void set매각가격(String 매각가격) {
		this.매각가격 = 매각가격;
	}

	public String get매각가격() {
		return 매각가격;
	}

	public void merge(물건 goods) {
		// get사건().set종류(goods.get사건().get종류());
		if (goods.get기일내역() != null) {
			set기일내역(goods.get기일내역());
		}
		if (goods.get사건() != null && goods.get사건().get감정평가요항RawText() != null) {
			get사건().set감정평가요항RawText(goods.get사건().get감정평가요항RawText());
		}
		if (goods.get기일날자() != null) {
			set기일날자(goods.get기일날자());
		}

		if (goods.get매각가격() != null) {
			set매각가격(goods.get매각가격());
		}

		if (goods.get기일Start() != null) {
			set기일Start(goods.get기일Start());
		}
		if (goods.get기일End() != null) {
			set기일End(goods.get기일End());
		}
		if (goods.get기일결과() != null) {
			set기일결과(goods.get기일결과());
		}
		if (goods.get최저가() != null) {
			set최저가(goods.get최저가());
		}
		if (goods.get유찰수() != null) {
			set유찰수(goods.get유찰수());
		}
		if (goods.get비고() != null) {
			set비고(goods.get비고());
		}
		if (goods.get보증율() != null) {
			set보증율(goods.get보증율());
		}
		if (goods.get자동차목록() != null) {
			Iterator<자동차> it = goods.get자동차목록().iterator();
			while (it.hasNext()) {
				자동차 자동차 = (자동차) it.next();
				add자동차(자동차);
			}
		}

		if (goods.get선박목록() != null) {
			Iterator<선박> it = goods.get선박목록().iterator();
			while (it.hasNext()) {
				선박 선박 = (선박) it.next();
				add선박(선박);
			}
		}

		if (goods.get인근매각통계() != null) {
			Iterator<물건인근매각통계Item> it = goods.인근매각통계.iterator();
			while (it.hasNext()) {
				물건인근매각통계Item 물건인근매각통계Item = (물건인근매각통계Item) it.next();
				물건인근매각통계Item.set물건(this);
				add인근매각통계(물건인근매각통계Item);
			}
		}
		
		if (goods.getRoad1()!= null){
			this.road1 =goods.getRoad1(); 
		}
		if (goods.getRoad2()!= null){
			this.road2 =goods.getRoad2(); 
		}
		if (goods.getRoad3()!= null){
			this.road3 =goods.getRoad3(); 
		}
	}

	public String getPath() {
		return 사건.getPath() + 물건번호 + "/";
	}

	public void add인근매각통계(물건인근매각통계Item item) {

		if (인근매각통계 == null) {
			인근매각통계 = new ArrayList<물건인근매각통계Item>();
		}

		// 이미 DB에 들어가 있는것이 있다면 합친다.
		for (물건인근매각통계Item old : 인근매각통계) {
			if (old.get기간().equals(item.get기간())) {
				old.merge(item);
				return;
			}
		}

		인근매각통계.add(item);
	}

	public Collection<물건인근매각통계Item> get인근매각통계() {
		return 인근매각통계;
	}

	public void set인근매각통계(Collection<물건인근매각통계Item> 인근매각통계) {
		this.인근매각통계 = 인근매각통계;
	}

	public void add목록(목록 목록) {
		if (목록s == null) {
			목록s = new ArrayList<목록>();
		}
		목록s.add(목록);
		

	}

	public ArrayList<목록> get목록s() {
		return 목록s;
	}

	public void set최선순위설정일자(String 최선순위설정일자) {
		this.최선순위설정일자 = 최선순위설정일자;
		
	}

	public String get최선순위설정일자() {
		return 최선순위설정일자;
	}

}
