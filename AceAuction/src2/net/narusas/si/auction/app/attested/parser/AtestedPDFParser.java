package net.narusas.si.auction.app.attested.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.등기부등본Item;
import net.narusas.si.auction.pdf.attested.TextPosition;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;

public class AtestedPDFParser {
	private static final float TABLE_ROW_GAP = 21f;
	private static final float FONT_WIDTH = 5.035f;
	Collumn[] STAGE_2_COLS = new Collumn[] { new Collumn(84.00f, 130.0f, Align.Left),// 등기명의인
			new Collumn(132.0f, 290.0f, Align.Left),// 등록번호
			new Collumn(291.00f, 385.0f, Align.Left), // 최종지분
			new Collumn(391.00f, 690.0f, Align.Left), // 주소
	};

	Collumn[] STAGE_3_COLS = new Collumn[] { //
	new Collumn(70.0f, 110.0f, Align.Left),// 순위번
			new Collumn(115.0f, 230.0f, Align.Left),// 등기목적
			new Collumn(235.00f, 350.0f, Align.Left), // 접수정보

			new Collumn(368.00f, 690.0f, Align.Left), // 주요등기사항
			new Collumn(692.00f, Float.MAX_VALUE, Align.Right), // 대상소유자
	};
	
	Collumn[] STAGE_4_COLS = STAGE_3_COLS; // 3번과 동일한 규격임 
	//new Collumn[] { //
//			new Collumn(70.0f, 110.0f, Align.Left),// 순위번호
//					new Collumn(115.0f, 230.0f, Align.Left),// 등기목적
//					new Collumn(235.00f, 350.0f, Align.Left), // 접수
//
//					new Collumn(370.00f, 690.0f, Align.Left), // 등기원인
//					new Collumn(692.00f, Float.MAX_VALUE, Align.Right), // 권리자및 기타사항
//			};

	enum Align {
		Left, Center, Right
	};

	public List<등기부등본Item> parse(File f) throws PdfException, Exception {
		if (f.exists() == false) {
			throw new IllegalArgumentException(f + " is not exist");
		}
		List<TextPosition> temp = collectTexts(f);
		List<TextPosition> list = new ArrayList<TextPosition>();

		for (TextPosition t : temp) {
			list.add(t.trim(FONT_WIDTH));
		}
		
		for(TextPosition t: list){
			System.out.println("=="+t.getText()+" X:"+t.getX()+" Y:"+t.getY()+" STG:"+t.getStage());
		}
		
		List<List<TextPosition>> _1_소유지분현황_COLS = splitCollumsAtStage(list, 2, STAGE_2_COLS);
		List<List<TextPosition>> _2_소유지분을제외한_소유권현황_COLS = splitCollumsAtStage(list, 3, STAGE_3_COLS);
		List<List<TextPosition>> _3_저당권및전세권_COLS = splitCollumsAtStage(list, 4, STAGE_4_COLS);
		System.out.println("######### _3_저당권및전세권_COLS:"+_3_저당권및전세권_COLS);
		List<등기부등본Item> items = new ArrayList<등기부등본Item>();

		collectStage2Info(_1_소유지분현황_COLS, items);
		boolean 예고등기무시 = false;
		collectOtherStages(items, _2_소유지분을제외한_소유권현황_COLS, 예고등기무시);
		예고등기무시 = true;
		collectOtherStages(items, _3_저당권및전세권_COLS, 예고등기무시);

		sort(items);
		calc소멸기준(items);
		items.get(0).set소멸기준("소멸");

		

		return items;

	}

	private void calc소멸기준(List<등기부등본Item> items) {
		boolean 소멸시점경과 = false;
		for (등기부등본Item item : items) {
			if (item.get권리종류().equals("소유권")) {
				item.set소멸기준("소멸");
				continue;
			}
			if (item.is예고등기()) {
				item.set소멸기준("인수");
				continue;
			}
			if (소멸시점경과 == false && item.is소멸기준종류() == true) {
				소멸시점경과 = true;
				item.set소멸기준("소멸기준");
				continue;
			}
			if (소멸시점경과 == false) {
				item.set소멸기준("인수");
			} else {
				item.set소멸기준("소멸");
			}
		}
	}

	private void sort(List<등기부등본Item> items) {
		Collections.sort(items, new Comparator<등기부등본Item>() {
			@Override
			public int compare(등기부등본Item o1, 등기부등본Item o2) {
				if (o1.get접수일() == null) {
					return -1;
				}
				int comapre = o1.get접수일AsDate().compareTo(o2.get접수일AsDate());
				if (comapre != 0) {
					return comapre;
				}

				return new Long(o1.get접수번호()).compareTo(new Long(o2.get접수번호()));
			}
		});
	}

	private String parse금액(String text) {
		Matcher m = Pattern.compile("([\\d,\\s]+\\s*(원|엔|불))").matcher(text);
		if (m.find()) {
			return 금액Converter.convert(m.group(1));
		}
		return 금액Converter.convert(text);
	}

	private String parse권리종류(String text) {
		return text;
	}

	private void collectStage2Info(List<List<TextPosition>> stage2Cols, List<등기부등본Item> items) {
		List<TextPosition> nameCol = bindVandH(stage2Cols.get(0));
		List<TextPosition> typeCol = bindVandH(stage2Cols.get(2));

		// System.out.println(nameCol.size() + ", " + typeCol.size());

		for (int i = 0; i < nameCol.size(); i++) {
			등기부등본Item firstItem = new 등기부등본Item();
			firstItem.set순번("");
			firstItem.set권리종류("소유권");
			firstItem.set권리순위(0);
			firstItem.set금액("NONE");
			firstItem.set권리자(nameCol.get(i).getText());
			firstItem.set대상소유자(typeCol.get(i).getText());
			items.add(firstItem);
		}
	}

	private void collectOtherStages(List<등기부등본Item> result, List<List<TextPosition>> stageCol, boolean 예고등기무시) {
		List<등기부등본Item> items = collectTextOnly(stageCol);

		List<등기부등본Item> temp = new ArrayList<등기부등본Item>();
		for (등기부등본Item item : items) {
			if (item.is부기등본() && item.is무시종류(예고등기무시)) {
				continue;
			}

			if (item.is변경등기()) {
				등기부등본Item target = temp.get(temp.size() - 1);
				target.updateFrom부기등기(item);
				continue;
			}

			if (item.is부기등본() == true) {
				continue;
			}
			temp.add(item);

		}

		result.addAll(temp);

	}

	private List<등기부등본Item> collectTextOnly(List<List<TextPosition>> stageCol) {
		try {
			return new RowParser(stageCol).parse();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<등기부등본Item>();
		}
	}

	private long extractNumber(String text) {
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(text);
		if (m.find() == false) {
			return 0;
		}
		return Long.parseLong(m.group(1));
	}

	private String parse순번(String text) {
		if (text.contains("\n")) {
			return text.substring(0, text.indexOf('\n')).trim();
		}
		return text;
	}

	private float getNextTPAbsoluteY(List<TextPosition> tps, int current) {

		if (tps.size() - 1 == current) {
			return Float.MAX_VALUE;
		}

		TextPosition nextTP = tps.get(current + 1);

		return nextTP.getAbsoluteY();
	}

	List<TextPosition> bindVandH(List<TextPosition> src) {
		List<TextPosition> horizontalBinded = bind수평(src, TABLE_ROW_GAP);

		return bind수직(horizontalBinded);
	}

	List<TextPosition> bind수평(List<TextPosition> src) {
		return bind수평(src, TABLE_ROW_GAP);
	}

	List<TextPosition> bind수평(List<TextPosition> src, float f) {
		List<TextPosition> temp = new LinkedList<TextPosition>();

		while (src.size() != 0) {
			TextPosition tp1 = src.remove(0);
			for (int i = 0; i < src.size(); i++) {
				TextPosition tp2 = src.get(i);
				if (tp1.isBinding수평(tp2, f)) {
					tp1 = tp1.add(tp2);
					src.remove(i);
					i--;
				}
			}
			temp.add(tp1);

		}
		System.out.println(temp);
		return temp;
	}

	List<TextPosition> bind수직(List<TextPosition> temp) {
		List<TextPosition> res2 = new LinkedList<TextPosition>();
		while (temp.size() != 0) {
			TextPosition tp1 = temp.remove(0);
			for (int i = 0; i < temp.size(); i++) {
				TextPosition tp2 = temp.get(i);
				if (tp1.isBinding수직(tp2)) {
					tp1 = tp1.addVertical(tp2);
					temp.remove(i);
					i--;
				}
			}
			res2.add(tp1);
		}
		return res2;
	}

	private List<List<TextPosition>> splitCollumsAtStage(List<TextPosition> list, int stage, Collumn[] cols) {
		List<List<TextPosition>> res = createCollumsTextPositionHolders(cols);

		for (int i = 0; i < list.size(); i++) {
			TextPosition t = list.get(i);
			if (t.getStage() != stage) {
				continue;
			}

			putMatchColumn(cols, res, t);

		}
		return res;
	}

	private void putMatchColumn(Collumn[] cols, List<List<TextPosition>> res, TextPosition t) {
		for (int pos = 0; pos < cols.length; pos++) {
			if (cols[pos].contains(t)) {
				res.get(pos).add(t);
				break;
			}
		}
	}

	private List<List<TextPosition>> createCollumsTextPositionHolders(Collumn[] cols) {
		List<List<TextPosition>> res = new ArrayList<List<TextPosition>>();
		for (int pos = 0; pos < cols.length; pos++) {
			res.add(new ArrayList<TextPosition>());
		}
		return res;
	}

	private List<TextPosition> collectTexts(File f) throws PdfException, Exception {
		List<TextPosition> list = new LinkedList<TextPosition>();

		PdfDecoder.useTextExtraction();
		PdfDecoder decodePdf = new PdfDecoder(false);
		decodePdf.setExtractionMode(PdfDecoder.TEXT); // extract just text
		decodePdf.init(true);
		PdfGroupingAlgorithms.useUnrotatedCoords = false;
		decodePdf.openPdfFile(f.getAbsolutePath());
		int start = 1;
		int end = decodePdf.getPageCount();
		// assertEquals(7, end);
		int stage = 0;
		for (int page = start; page <= end; page++) {
			decodePdf.decodePage(page);
			PdfGroupingAlgorithms currentGrouping = decodePdf.getGroupingObject();
			PdfPageData currentPageData = decodePdf.getPdfPageData();

			int x1 = currentPageData.getMediaBoxX(page);
			int x2 = currentPageData.getMediaBoxWidth(page) + x1;

			int y2 = currentPageData.getMediaBoxX(page);
			int y1 = currentPageData.getMediaBoxHeight(page) - y2;
			System.out.println(String.format("X1:%d Y1:%d X2:%d Y2:%d", x1,y1,x2,y2));
			Vector<String> words = currentGrouping.extractTextAsWordlist(x1-1000, y1+1000, x2+1000, y2-1000, page, true, true, "");
			
			List<TextPosition> tpList = new ArrayList<TextPosition>();
			
			for (int i = 0; i < words.size(); i += 5) {
				tpList.add(createTextPosition(page, y1, words, 0, i));
			}
			List<Float> skipLines = new ArrayList<Float>();
			
			List<TextPosition> temp = new ArrayList<TextPosition>();
			
			for (TextPosition t : tpList) {
				
				if ("요약한".equals(t.getText())) {// 주요등기사항요약
					stage = 1;
					continue;
				}
				// System.out.println(t);
				if (stage == 0 ) {
					continue;
				}
				
				
				// System.out.println(t);
				if (stage == 1 && "1.".equals(t.getText()) && t.getX() < 100f) {
					stage = 2;
					System.out.println("Stage 2 :" + t.getY());
					skipLines.add(t.getY());
					continue;
				}

				if (stage == 2 && "2.".equals(t.getText()) && t.getX() < 100f) {
					stage = 3;
					System.out.println("Stage 3");
					skipLines.add(t.getY());
					continue;
				}
				if (stage == 3 && ("3.".equals(t.getText())  ) && t.getX() < 100f) {
					stage = 4;
					 System.out.println("Stage 4:"+t.getY());
					 skipLines.add(t.getY());
					continue;
				}
				if (stage == 4 &&  "(근".equals(t.getText())){
					continue;
				}
				if (stage == 4 &&  "근근".equals(t.getText())){
					continue;
				}
				
				if (stage == 4 &&  ".".equals(t.getText())){
					continue;
				}
				
				if (stage == 4 &&  "[".equals(t.getText())){
					break;
				}

				if ("등기명의인".equals(t.getText())) {
					skipLines.add(t.getY());
					continue;
				}
				if ("순위번호".equals(t.getText())) {
					skipLines.add(t.getY());
					continue;
				}
				if ("없음".equals(t.getText())) {
					skipLines.add(t.getY());
					continue;
				}

				if (763.38007f <= t.getY()) {
					skipLines.add(t.getY());
					continue;
				}


				if (stage >= 2) {// 소유지분형황이라면
					t.setStage(stage);
					// System.out.println(t);
					temp.add(t);
				}
			}
			
			for (TextPosition t : temp) {
				boolean isSkipTarget = false;
				for (Float ff : skipLines) {
					if (Math.abs(t.getY()-ff) < 3f){
						isSkipTarget = true;
					}
				}
				if (isSkipTarget == false){
					list.add(t);
					
				}
			}
		}
		
		System.out.println("-------------------------------------------");
		for (TextPosition tp : list) {
			System.out.println(":::"+tp.getText() +" stg:"+tp.getStage());
		}
		System.out.println("-------------------------------------------");
		return list;
	}

	private TextPosition createTextPosition(int page, int y1, Vector<String> words, int stage, int i) {
		return new TextPosition(words.get(i),// string
				floatValue(words.get(i + 1)), // x1
				y1 - floatValue(words.get(i + 2)), // y1
				floatValue(words.get(i + 3)),// x2
				y1 - floatValue(words.get(i + 4)), page, // y2
				stage);
	}

	private int skipLine(TextPosition t, Vector<String> words, int i, int page, int stage, int y1) {
		while (true) {
			if (i + 5 >= words.size() - 1) {
				return words.size() - 1;
			}
			TextPosition temp = createTextPosition(page, y1, words, stage, i);

			// bold를 표현하려고 좌표를 살짝 바뀌어 2번 찍는 경우가 있음.
			// 그것도 같이 처리하기 위해 범위 계산을 하게함.
			if (Math.abs(temp.getY() - t.getY()) > 2f) {
				return i - 5;
			}
			i += 5;
		}
	}

	float floatValue(Object object) {
		return Float.parseFloat((String) object);
	}

	boolean isInContentsHeight(TextPosition position) {
		return position.getY() > 340 && position.getY() < 750;
	}

	int skipWord(int i) {
		return i + 5;
	}

	class RowParser {
		int noIndex = 0;
		int dateIndex = 0;
		int detailIndex = 0;
		int ownerIndex = 0;
		private ArrayList<등기부등본Item> items;
		private List<TextPosition> noCol;
		private List<TextPosition> typeCol;
		private List<TextPosition> dateCol;
		private List<TextPosition> detailCol;
		private List<TextPosition> ownerCol;
		String 접수일 = null;
		long 접수번호 = 0;
		String detail;
		String owner;

		public RowParser(List<List<TextPosition>> stageCol) {
			items = new ArrayList<등기부등본Item>();

			List<TextPosition> tmp = bind수평(stageCol.get(0));
			noCol = new ArrayList<TextPosition>();
			for (TextPosition tp : tmp) {
				if (tp.getText() != null && tp.getText().trim().startsWith("(") == false) {
					noCol.add(tp);
				}
			}
			typeCol = bind수평(stageCol.get(1));
			if (typeCol == null || typeCol.size() == 0) {
				throw new IllegalArgumentException("No Data");
			}
			dateCol = bind수평(stageCol.get(2));
			detailCol = bind수평(stageCol.get(3));
			ownerCol = bind수평(stageCol.get(4));

			System.out.println("################### typeCol START");
			for (TextPosition textPosition : typeCol) {
				System.out.println(textPosition);
			}
			
			System.out.println("################### dateCol START");
			for (TextPosition textPosition : dateCol) {
				System.out.println(textPosition);
			}
			
			System.out.println("################### detailCol START");
			for (TextPosition textPosition : detailCol) {
				System.out.println(textPosition);
			}
			
			System.out.println("################### ownerCol START");
			for (TextPosition textPosition : ownerCol) {
				System.out.println(textPosition);
			}
			

		}

		public List<등기부등본Item> parse() {
			System.out.println("------------------------------------------------------------");
			System.out.println(typeCol);
			System.out.println(noCol);
			System.out.println("TypeCol:"+typeCol.size()+" noCol:"+noCol.size());
			
			List<TextPosition> temp = new ArrayList<TextPosition>();
			
			for (int row = 0; row < typeCol.size(); row++) {
				TextPosition typeTP = typeCol.get(row);
				remove괄호숫자(typeTP);
				if (typeTP.getText().trim().startsWith("(")) {
					continue;
				}
				float nextTPY = getNextTPAbsoluteY(typeCol, row);
				parse접수(typeTP);
				detail = parseDetail(typeTP, nextTPY);
				owner = parse권리자(typeTP, nextTPY, detail);
				등기부등본Item item = createItem(row, typeTP, detail);
				items.add(item);
			}
			return items;
		}

		private void remove괄호숫자(TextPosition typeTP) {
			
			if (typeTP.getText() != null && typeTP.getText().trim().matches("\\(\\d+\\).*")){
				Pattern p = Pattern.compile("\\(\\d+\\)(.*)");
				Matcher m = p.matcher(typeTP.getText().trim());
				if(m.find()) {
					typeTP.setText(m.group(1));
				}
			}
		}

		private 등기부등본Item createItem(int row, TextPosition typeTP, String detail) {
			등기부등본Item firstItem = new 등기부등본Item();
			firstItem.set순번(parse순번(noCol.get(row).getText()));
			firstItem.set권리종류(parse권리종류(typeTP.getText()));
			firstItem.set접수일(접수일);
			firstItem.set접수번호(접수번호);
			firstItem.set권리순위(0);
			firstItem.set금액("NONE");

			// firstItem.set비고(detail);
			firstItem.set대상소유자(owner);

			analysisDetail(detail, firstItem);
			return firstItem;
		}

		private void analysisDetail(String detail, 등기부등본Item firstItem) {
			if (contain사람(detail)) {
				change사람(detail, firstItem);
			}
			if (금액Converter.contain금액(detail)) {
				change금액(detail, firstItem);
			}

			// if (detail.trim().equals("") == false) {
			// if (detail.contains("\n")) {
			// String firstLine = detail.substring(0,
			// detail.indexOf("\n")).trim();
			// parseFirstLine(firstItem, firstLine);
			//
			// String remains = detail.substring(detail.indexOf("\n")).trim();
			// parse권리자(firstItem, remains);
			// } else {
			// parseFirstLine(firstItem, detail);
			// }
			//
			// }
		}

		
		private void change금액(String str, 등기부등본Item item) {
			String temp = parse금액(str);
			Pattern moneyPattern = Pattern.compile("(\\d+[엔|불]?)");
			Matcher m = moneyPattern.matcher(temp);
			if (m.find()) {
				item.set금액(m.group(1));
			}
		}

		final String[] 권리자종류 = new String[] { "권리자", "채권자", "근저당권자", "지상권자", "전세권자", "가등기권자", "임차권자" };

		private boolean contain사람(String str) {
			if (str == null) {
				return false;
			}
			for (String type : 권리자종류) {
				if (str.contains(type)) {
					return true;
				}
			}
			return false;
		}

		private void change사람(String str, 등기부등본Item item) {
			for (String type : 권리자종류) {
				Pattern p = Pattern.compile(type);
				Matcher m = p.matcher(str);
				int pos = 0;
				while(m.find()){
					pos = m.end()+1;
				}
				if (pos > 0 && str.length()-1 >= pos){
					item.set권리자(str.substring(pos).trim());
					return;
				}
				
				
//				Pattern p2 = Pattern.compile(type);
//				Matcher m2 = p.matcher(str);
//				int pos2 = 0;
//				while(m2.find()){
//					pos2 = m2.end()+1;
//				}
//				
//				if (pos2 != 0){
//					item.set권리자(str.substring(pos2).trim());
//					return;
//				}
			}
			
//			for (String type : 권리자종류) {
//				Pattern p = Pattern.compile("\\s+" + type);
//				// str = str.replaceAll("^\\s*", "^");
//				Matcher m = p.matcher(str);
//				if (m.find()) {
//					item.set권리자(str.substring(m.end() + 1).trim());
//				} else {
//					m = Pattern.compile(type).matcher(str);
//					if (m.find()) {
//						item.set권리자(str.substring(m.end() + 1).trim());
//					}
//				}
//
//			}
		}

		private void parse접수(TextPosition typeTP) {
			TextPosition dateTP = null;
			// System.out.println(typeTP);

			for (int i = 0; i < dateCol.size()-1; i++) {
				dateTP = dateCol.get(i);
				if (dateTP.isSameRow(typeTP)) {
					접수일 = dateTP.getText();
					접수번호 = extractNumber(dateCol.get(i + 1).getText());
					break;
				}
			}
			// do {
			// if (dateIndex==dateCol.size() ){
			// break;
			// }
			// dateTP = dateCol.get(dateIndex);
			// 접수일 = dateTP.getText();
			// dateIndex++;
			// // if (dateIndex==dateCol.size() ){
			// // break;
			// // }
			// 접수번호 = extractNumber(dateCol.get(dateIndex).getText());
			// } while (!dateTP.isSameRow(typeTP));
		}

		private String parseDetail(TextPosition typeTP, float nextTPY) {
			String detail = "";

			while (true) {
				if (detailCol.size() == detailIndex) {
					break;
				}
				TextPosition tempDetail = detailCol.get(detailIndex);
				detailIndex++;
				if (tempDetail.getAbsoluteY() == typeTP.getAbsoluteY()) {
					detail = tempDetail.getText();
				}
				if (tempDetail.getAbsoluteY() > typeTP.getAbsoluteY() && tempDetail.getAbsoluteY() < nextTPY) {
					detail = detail + "\n" + tempDetail.getText();
				}
				if (tempDetail.getAbsoluteY() >= nextTPY) {
					detailIndex--;
					break;
				}
			}
			return detail;
		}

		private String parse권리자(TextPosition typeTP, float nextTPY, String detail) {
			String owner = "";

			while (true) {
				if (ownerCol.size() == ownerIndex) {
					break;
				}
				TextPosition tempOwner = ownerCol.get(ownerIndex);
				ownerIndex++;
				if (tempOwner.getAbsoluteY() == typeTP.getAbsoluteY()) {
					owner = tempOwner.getText();
				}
				if (tempOwner.getAbsoluteY() > typeTP.getAbsoluteY() && tempOwner.getAbsoluteY() < nextTPY) {
					owner = owner + "\n" + tempOwner.getText();
				}
				if (tempOwner.getAbsoluteY() >= nextTPY) {
					ownerIndex--;
					break;
				}
			}
			return owner;
		}

		private void parseFirstLine(등기부등본Item firstItem, String firstLine) {
			if (firstLine.endsWith("원") || firstLine.endsWith("원정") || firstLine.endsWith("채권최고액")) {
				String 금액 = parse금액(firstLine.substring(firstLine.indexOf(" ")).trim());
				firstItem.set금액(금액);
			} else {
				parse권리자(firstItem, firstLine);
			}
		}

		final String[] 권리자_종류 = { "근저당권자", "저당권자", "소유자", "공유자", "합유자", "수탁자", "권리자", "지상권자", "임차권자", "처분청", "전세권자",
				"채권자", "가등기권자" };

		private void parse권리자(등기부등본Item firstItem, String remains) {

			if (remains.equals("") == false) {

				if (remains.contains("\n")) {
					String line = remains.substring(0, remains.indexOf("\n")).trim();
					if (line.contains(" ")) {
						String people = remains.substring(remains.indexOf(" ")).trim();
						firstItem.set권리자(people);
					} else {
						String people = remains.substring(remains.indexOf("\n")).trim();
						firstItem.set권리자(people);
					}
				} else {
					if (remains.contains(" ")) {
						String[] tokens = remains.split(" ");
						String key = remains.substring(0, remains.indexOf(" ")).trim();
						String value = remains.substring(remains.indexOf(" ")).trim();
						for (String 권리자 : 권리자_종류) {
//							if (key.contains(권리자)) {
							Matcher m = Pattern.compile("^"+권리자+"\\s*([^\n]*)").matcher(remains);
							String name = null;
							while(m.find()){
								
							}
							if (m.find()){
								name = m.group(1);
								while(m.find())
								// String people =
								// key.substring(key.lastIndexOf(권리자) +
								// 권리자.length()).trim();
//								firstItem.set권리자(value.trim());
								firstItem.set권리자(m.group(1));
								return;
							}
						}

						// 금액이라고 가정함.

						// String people =
						// remains.substring(remains.indexOf(" ")).trim();
						if (remains.endsWith("원") || remains.endsWith("원정") || remains.endsWith("채권최고액")) {

							firstItem.set금액(parse금액(value));
						}
					}
				}
			}
		}

	}

}
