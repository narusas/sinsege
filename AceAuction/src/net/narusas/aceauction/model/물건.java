package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;

public class 물건 {

	static Logger logger = Logger.getLogger("log");

	public boolean has명세서;

	public String 가격;

	public String 명세서번호 = "";

	public int 물건번호;

	public String 물건종류;

	public List<물건현황> 물건현황 = new ArrayList<물건현황>();

	public String 비고;

	public 사건 사건;

	public List<String> 사진 = new ArrayList<String>();

	public String 최저가;

	private 스피드옥션물건상세내역 detail;

	private String 건물등기부등본;

	private LinkedList<기일내역Item> 기일내역 = new LinkedList<기일내역Item>();

	private String 매각물건명세서html;

	private 명세서 명세서;

	private String 배당요구종기일;

	private LinkedList<제시외건물> 제시외건물s = new LinkedList<제시외건물>();

	private String 주소;
	private String 토지등기부등본;

	public 물건() {

	}

	public 물건(int 물건번호, String 물건종류, boolean has명세서, String 명세서번호, String 비고, String 가격, String 최저가) {
		this.물건번호 = 물건번호;
		this.물건종류 = 물건종류;
		this.has명세서 = has명세서;
		this.명세서번호 = 명세서번호;
		this.비고 = 비고;
		this.가격 = 가격;
		this.최저가 = 최저가;
	}

	public void add(물건현황 bld) {
		if (물건현황.size() == 0) {
			set주소(bld.get주소());
		}
		물건현황.add(bld);
	}

	public void add기일내역(Row record) {
		기일내역Item item = new 기일내역Item(record);
		if (기일내역.contains(item)) {
			return;
		}
		기일내역.add(item);
	}

	public void add사진(String url) {
		사진.add(url);
	}

	public void add제시외건물(제시외건물 e) {
		제시외건물s.add(e);
	}

	public void clear제시외건물() {
		제시외건물s.clear();
	}

	public 스피드옥션물건상세내역 getDetail() {
		return detail;
	}

	public boolean getHas명세서() {
		return has명세서;
	}

	public String get가격() {
		return 가격;
	}

	public String get건물등기부등본() {
		return 건물등기부등본;
	}

	public List<기일내역Item> get기일내역() {
		if (기일내역.size() == 0) {
			사건.update기일내역(this);
		}
		return 기일내역;
	}

	public String get등기부등본FilePath() {
		return 사건.getEventYear() + "/" + 사건.court.getCode() + "/" + removeDots(사건.charge.get매각기일().toString()) + "/"
				+ 사건.charge.get담당계코드() + "/" + 사건.get사건번호() + "/" + 물건번호 + "/";
	}

	public String get매각물건명세서html() {
		return 매각물건명세서html;
	}

	public 명세서 get명세서() {
		return 명세서;
	}

	public String get명세서번호() {
		return 명세서번호;
	}

	public String get물건FilePath() {
		return 사건.getEventYear() + "/" + 사건.court.getCode() + "/" + removeDots(사건.charge.get매각기일().toString()) + "/"
				+ 사건.charge.get담당계코드() + "/" + 사건.get사건번호() + "/" + 물건번호 + "/";
	}

	public int get물건번호() {
		return 물건번호;
	}

	public String get물건종류() {
		return 물건종류;
	}

	public List<물건현황> get물건현황() {
		return 물건현황;
	}

	public String get배당요구종기일() {
		사건.update배당요구(this);
		return 배당요구종기일;
	}

	public String get비고() {
		return 비고;
	}

	public 사건 get사건() {
		return 사건;
	}

	public List<String> get사진() {
		return 사진;
	}

	public 물건현황 get상세내역(int index) {
		return (물건현황) 물건현황.get(index);
	}

	public LinkedList<제시외건물> get제시외건물s() {
		return 제시외건물s;
	}

	public String get주소() {
		return 주소;
	}

	public String get최저가() {
		return 최저가;
	}

	public String get토지등기부등본() {
		return 토지등기부등본;
	}

	public boolean has명세서() {
		return has명세서;
	}

	public void merge(물건 m2) {
		this.물건현황.addAll(m2.물건현황);

	}

	public void setDetail(스피드옥션물건상세내역 detail) {
		this.detail = detail;
	}

	public void setHas명세서(boolean has명세서) {
		this.has명세서 = has명세서;
	}

	public void setParent(사건 사건) {
		this.사건 = 사건;
	}

	public void set가격(String 가격) {
		this.가격 = 가격;
	}

	public void set건물등기부등본(String 건물등기부등본) {
		this.건물등기부등본 = 건물등기부등본;

	}

	public void set매각물건명세서html(String 매각물건명세서html) {
		this.매각물건명세서html = 매각물건명세서html;
	}

	public void set매각물건명세서HTML(String 매각물건명세서HTML) {
		매각물건명세서html = 매각물건명세서HTML;

	}

	public void set명세서(명세서 명세서) {
		this.명세서 = 명세서;
	}

	public void set명세서번호(String 명세서번호) {
		this.명세서번호 = 명세서번호;
	}

	public void set물건번호(int 물건번호) {
		this.물건번호 = 물건번호;
	}

	public void set물건종류(String 물건종류) {
		this.물건종류 = 물건종류;
	}

	public void set배당요구종기일(String 배당요구종기일) {
		this.배당요구종기일 = 배당요구종기일;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public void set사건(사건 사건) {
		this.사건 = 사건;
	}

	public void set최저가(String 최저가) {
		this.최저가 = 최저가;
	}

	public void set토지등기부등본(String 토지등기부등본) {
		this.토지등기부등본 = 토지등기부등본;

	}

	@Override
	public String toString() {
		return "물건{번호=" + 물건번호 + "}";// ;,종류=" + 물건종류 + ",명세서보유여부=" + has명세서;
		// + ", 명세서번호=" + 명세서번호 + ", 가격=[" + 가격 + "], 최저가=[" + 최저가
		// + "],비고=[" + 비고 + "]}";
	}

	private String removeDots(String src) {
		return src.replaceAll(".", "").replace("-", "");
	}

	private void set주소(String 주소) {
		this.주소 = 주소;
	}

	public static String[] getSpecialCaseSource(long id) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String[] result = new String[2];
			stmt = DB.createStatement();
			rs = stmt.executeQuery("SELECT goods_statememt_comment4,comment FROM ac_goods WHERE id=" + +id + ";");
			if (rs != null && rs.next()) {
				result[0] = rs.getString("goods_statememt_comment4");
				result[1] = rs.getString("comment");
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(rs, stmt);
		}
		return null;
	}

	public static String updateSpecialCase(String[] src, long id) {
		if (src == null) {
			return null;
		}
		String res = "";
		if ((src[0] != null && src[0].contains("법정지상권")) || (src[1] != null && src[1].contains("법정지상권"))) {
			res += "법정지상권,";
		}
		if ((src[0] != null && src[0].contains("분묘")) || (src[1] != null && src[1].contains("분묘"))) {
			res += "분묘,";
		}
		if ((src[0] != null && src[0].contains("유치권")) || (src[1] != null && src[1].contains("유치권"))) {
			res += "유치권,";
		}
		if ((src[0] != null && src[0].contains("별도등기")) || (src[1] != null && src[1].contains("별도등기"))) {
			res += "별도등기,";
		}
		if ((src[0] != null && src[0].contains("예고등기 ")) || (src[1] != null && src[1].contains("예고등기 "))) {
			res += "예고등기,";
		}
		PreparedStatement stmt = null;

		try {
			stmt = DB.prepareStatement("UPDATE ac_goods SET special_case=? WHERE id=?;");
			stmt.setString(1, res);
			stmt.setLong(2, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}
		return res;
	}

	public static void main(String[] args) {
		try {
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id FROM ac_goods;");
			while (rs.next()) {
				long id = rs.getLong("id");
				System.out.println("Try ID=" + id + ", res=" + 물건.updateSpecialCase(물건.getSpecialCaseSource(id), id));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
