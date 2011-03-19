package net.narusas.si.auction.model;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 사건 {
	Long id;
	private Long 사건번호;
	String 사건명;

	private 담당계 담당계;
	private Long 담당계Id;
	private 법원 법원;
	private int eventYear;
	private int eventNo;

	String 병합;

	String 사건항고정지여부;

	Date 접수일자;
	Date 개시결정일자;

	String 종국결과;
	Date 종국일자;

	Long 청구금액;
	String 감정평가요항RawText;

	사건감정평가서 감정평가서;
	private List<물건> 물건목록;
	private Collection<당사자> 당사자목록;
	private String 부동산의현황;

	private 사건종류 종류 = 사건종류.부동산; // 기본값을 부동산으로 함.
	private boolean is신건;
	private List<점유관계> 부동산점유관계;

	// transient List<물건> workset;

	public void set사건번호(Long 사건번호) {
		this.사건번호 = 사건번호;
		if (사건번호 == null) {
			return;
		}

		Pattern p = Pattern.compile("^(\\d\\d\\d\\d)0130(\\d+)");
		Matcher m = p.matcher(String.valueOf(사건번호));
		if (m.find() == false) {
			return;
		}

		this.eventYear = Integer.parseInt(m.group(1));
		this.eventNo = Integer.parseInt(m.group(2));
	}

	public Long get담당계Id() {
		return 담당계Id;
	}

	public void set담당계Id(Long 담당계Id) {
		this.담당계Id = 담당계Id;
	}

	public void set담당계(담당계 담당계) {
		this.담당계 = 담당계;
		if (담당계 != null) {
			담당계Id = 담당계.getId();
		}
	}

	public void set법원(법원 법원) {
		this.법원 = 법원;
	}

	public long get사건번호() {
		return 사건번호;
	}

	public 담당계 get담당계() {
		return 담당계;
	}

	public 법원 get법원() {
		return 법원;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getEventYear() {
		return eventYear;
	}

	public void setEventYear(int eventYear) {
		this.eventYear = eventYear;
	}

	public int getEventNo() {
		return eventNo;
	}

	public void setEventNo(int eventNo) {
		this.eventNo = eventNo;
	}

	public String get사건명() {
		return 사건명;
	}

	public void set사건명(String 사건명) {
		this.사건명 = 사건명;
		this.종류 = parse사건종류(사건명);
	}

	public 사건종류 get종류() {
		return parse사건종류(get사건명());
	}

	public void set종류(사건종류 사건종류) {
		this.종류 = 사건종류;
	}

	사건종류 parse사건종류(사건 s) {
		return parse사건종류(s.get사건명());
	}

	private 사건종류 parse사건종류(String a사건명) {
		사건종류 type = 사건종류.부동산;
		if (a사건명==null){
			return type;
		}
		if (a사건명.contains("자동차")) {
			type = 사건종류.자동차;
		} else if (a사건명.contains("자동차")) {
			type = 사건종류.자동차;
		} else if (a사건명.contains("선박") || a사건명.contains("어선")) {
			type = 사건종류.선박;
		} else if (a사건명.contains("중장비") || a사건명.contains("중기") || a사건명.contains("건설기계")) {
			type = 사건종류.중장비;
		}
		return type;
	}

	public String get병합() {
		return 병합;
	}

	public void set병합(String 병합) {
		this.병합 = 병합;
	}

	public String get사건항고정지여부() {
		return 사건항고정지여부;
	}

	public void set사건항고정지여부(String 사건항고정지여부) {
		this.사건항고정지여부 = 사건항고정지여부;
	}

	public Date get접수일자() {
		return 접수일자;
	}

	public void set접수일자(Date 접수일자) {
		this.접수일자 = 접수일자;
	}

	public Date get개시결정일자() {
		return 개시결정일자;
	}

	public void set개시결정일자(Date 개시결정일자) {
		this.개시결정일자 = 개시결정일자;
	}

	public String get종국결과() {
		return 종국결과;
	}

	public void set종국결과(String 종국결과) {
		this.종국결과 = 종국결과;
	}

	public Date get종국일자() {
		return 종국일자;
	}

	public void set종국일자(Date 종국일자) {
		this.종국일자 = 종국일자;
	}

	public Long get청구금액() {
		return 청구금액;
	}

	public void set청구금액(Long 청구금액) {
		this.청구금액 = 청구금액;
	}

	public 사건감정평가서 get감정평가서() {
		return 감정평가서;
	}

	public void set감정평가서(사건감정평가서 감정평가서) {
		this.감정평가서 = 감정평가서;
	}

	public void set물건목록(List<물건> 물건목록) {
		this.물건목록 = 물건목록;
	}

	public List<물건> get물건목록() {
		return 물건목록;
	}

	public Collection<당사자> get당사자목록() {
		return 당사자목록;
	}

	public void set당사자목록(Collection<당사자> 당사자목록) {
		if (당사자목록 != null) {
			for (당사자 당사자 : 당사자목록) {
				당사자.set사건(this);
			}
		}
		this.당사자목록 = 당사자목록;
	}

	public 물건 get물건(int index) {
		if (물건목록 == null) {
			return null;
		}
		return 물건목록.get(index);
	}

	public 물건 get물건By물건번호(int no) {
		if (물건목록 == null) {
			return null;
		}
		for (int i = 0; i < 물건목록.size(); i++) {
			물건 goods = 물건목록.get(i);
			if (no == goods.get물건번호().intValue()) {
				return goods;
			}
		}
		return null;
	}

	public String get감정평가요항RawText() {
		return 감정평가요항RawText;
	}

	public void set감정평가요항RawText(String rawText) {
		감정평가요항RawText = rawText;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventNo;
		result = prime * result + eventYear;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((감정평가서 == null) ? 0 : 감정평가서.hashCode());
		result = prime * result + ((개시결정일자 == null) ? 0 : 개시결정일자.hashCode());
		result = prime * result + ((담당계 == null) ? 0 : 담당계.hashCode());
		result = prime * result + ((당사자목록 == null) ? 0 : 당사자목록.hashCode());
		result = prime * result + ((물건목록 == null) ? 0 : 물건목록.hashCode());
		result = prime * result + ((법원 == null) ? 0 : 법원.hashCode());
		result = prime * result + ((병합 == null) ? 0 : 병합.hashCode());
		result = prime * result + ((사건명 == null) ? 0 : 사건명.hashCode());
		result = prime * result + ((사건번호 == null) ? 0 : 사건번호.hashCode());
		result = prime * result + ((사건항고정지여부 == null) ? 0 : 사건항고정지여부.hashCode());
		result = prime * result + ((접수일자 == null) ? 0 : 접수일자.hashCode());
		result = prime * result + ((종국결과 == null) ? 0 : 종국결과.hashCode());
		result = prime * result + ((종국일자 == null) ? 0 : 종국일자.hashCode());
		result = prime * result + ((청구금액 == null) ? 0 : 청구금액.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		사건 other = (사건) obj;
		if (eventNo != other.eventNo)
			return false;
		if (eventYear != other.eventYear)
			return false;
		if (id != other.id)
			return false;
		if (감정평가서 == null) {
			if (other.감정평가서 != null)
				return false;
		} else if (!감정평가서.equals(other.감정평가서))
			return false;
		if (개시결정일자 == null) {
			if (other.개시결정일자 != null)
				return false;
		} else if (!개시결정일자.equals(other.개시결정일자))
			return false;
		if (담당계 == null) {
			if (other.담당계 != null)
				return false;
		} else if (!담당계.equals(other.담당계))
			return false;
		if (당사자목록 == null) {
			if (other.당사자목록 != null)
				return false;
		} else if (!당사자목록.equals(other.당사자목록))
			return false;
		if (물건목록 == null) {
			if (other.물건목록 != null)
				return false;
		} else if (!물건목록.equals(other.물건목록))
			return false;
		if (법원 == null) {
			if (other.법원 != null)
				return false;
		} else if (!법원.equals(other.법원))
			return false;
		if (병합 == null) {
			if (other.병합 != null)
				return false;
		} else if (!병합.equals(other.병합))
			return false;
		if (사건명 == null) {
			if (other.사건명 != null)
				return false;
		} else if (!사건명.equals(other.사건명))
			return false;
		if (사건번호 == null) {
			if (other.사건번호 != null)
				return false;
		} else if (!사건번호.equals(other.사건번호))
			return false;
		if (사건항고정지여부 == null) {
			if (other.사건항고정지여부 != null)
				return false;
		} else if (!사건항고정지여부.equals(other.사건항고정지여부))
			return false;
		if (접수일자 == null) {
			if (other.접수일자 != null)
				return false;
		} else if (!접수일자.equals(other.접수일자))
			return false;
		if (종국결과 == null) {
			if (other.종국결과 != null)
				return false;
		} else if (!종국결과.equals(other.종국결과))
			return false;
		if (종국일자 == null) {
			if (other.종국일자 != null)
				return false;
		} else if (!종국일자.equals(other.종국일자))
			return false;
		if (청구금액 == null) {
			if (other.청구금액 != null)
				return false;
		} else if (!청구금액.equals(other.청구금액))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(사건번호);
	}

	public String getPath() {
		StringBuffer buf = new StringBuffer(String.valueOf(get법원().get법원코드()));
		for (int i = buf.length(); i < 6; i++) {
			buf.insert(0, '0');
		}
		String courtCode = buf.toString();
		return getEventYear() + "/" + courtCode + "/" + removeDots(get담당계().get매각기일().toString()) + "/"
				+ get담당계().get담당계코드() + "/" + get사건번호() + "/";
	}

	private String removeDots(String src) {
		return src.replaceAll(".", "").replace("-", "");
	}

	public List<String> get소유자() {
		LinkedList<String> res = new LinkedList<String>();
		for (당사자 people : 당사자목록) {
			if (people.get당사자구분().contains("소유")) {
				res.add(people.get당사자명());
			}
		}
		return res;
	}

	public void merge(사건 event) {
		set병합(event.병합);
		set접수일자(event.get접수일자());
		set종국일자(event.종국일자);
		set청구금액(event.청구금액);
		set사건명(event.사건명);

	}

	public void set부동산의현황(String 부동산의현황) {
		this.부동산의현황 = 부동산의현황;
	}

	public String get부동산의현황() {
		return 부동산의현황;
	}

	public void set신건(boolean 신건) {
		this.is신건 = 신건;
	}

	public boolean isIs신건() {
		return is신건;
	}

	

	public List<점유관계> get부동산점유관계() {
		return 부동산점유관계;
	}

	public void set부동산점유관계(List<점유관계> 부동산점유관계) {
		this.부동산점유관계 = 부동산점유관계;
	}

}
