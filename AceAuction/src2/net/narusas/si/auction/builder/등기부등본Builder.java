package net.narusas.si.auction.builder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.등기부등본Item;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.pdf.attested.등기부등본Parser;
import net.narusas.si.auction.pdf.attested.등기부등본_사항;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.DocumentException;

public class 등기부등본Builder {
	final Logger logger = LoggerFactory.getLogger("auction");

	static Pattern p2 = Pattern.compile("^\\d번");

	private File fileToUpload;

	private final List<String> owners;

	private String type;

	private final String url;

	private final 물건 물건;

	public 등기부등본Builder(물건 물건, String url, List<String> get소유자, String type) {
		this.물건 = 물건;
		this.url = url;
		owners = get소유자;
		this.type = type;
	}

	public void execute() throws Exception {
		boolean hadInserted = dbUpdate();
		// if (hadInserted) {
//		uploadPDF();
		// }

	}

	private String cutNo(String bigo) {
		Matcher m = p2.matcher(bigo);
		if (m.find()) {
			return bigo.substring(m.end());
		}
		return bigo;
	}

	private boolean dbUpdate() throws MalformedURLException, IOException, Exception, SQLException {
		logger.info("\n\n물건:" + 물건);
		

		
		byte[] data = downloadPDFBinary();
		if (data == null || data.length == 0) {
			return false;
		}

		File parent = new File("download/" + 물건.getPath());
		if (parent.exists() == false) {
			parent.mkdirs();
		}
		fileToUpload = new File(parent, "PDF_" + type + ".pdf");
		NFile.write(fileToUpload, data);

		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> items = parser.parseAll(fileToUpload.getAbsolutePath(), owners.toArray(new String[] {}));
		if (items == null || items.size() == 0) {
			return false;
		}
		String text = parser.getFirst().getText();
		type = text.substring(text.indexOf("-") + 1).trim();

		//File renF = new File(parent, "등기부등본_" + type + ".pdf");
		//fileToUpload.renameTo(renF);
		//fileToUpload = renF;
		
//		등기부등본Dao dao = (등기부등본Dao) App.context.getBean("등기부등본DAO");
//		등기부등본 등본 = dao.get(물건, type);
//		if (등본 != null) {
//			
//			return false;
//		}
		
		
		
		// 임시로 파일업로드만 가능하게 수정. 
		
		insertRecord(items);
		return true;

	}

	private byte[] downloadPDFBinary() throws MalformedURLException, IOException {
		logger.info("PDF 파일을 다운로드합니다 :" + url);
		URL u = new URL(url);

		return net.narusas.util.lang.NInputStream.readBytes(u.openStream());
	}

	private String formatRightPeople(등기부등본_사항 item) {

		Set<String> names = item.getRightPeoples();
		String res = names.toString();
		if (res == null || "".equals(res) || "[]".equals(res)) {
			res = item.getRightPeople();

		} else {
			res = res.split(",")[0];
		}
		res = res.replaceAll("\\[", "");
		res = res.replaceAll("\\]", "");
		res = res.replaceAll("주식회사", "");
		res = res.replaceAll("\\(주\\)", "");
		if ("지분".equals(res)) {
			res = "";
		}
		return res;
	}

	private Collection<등기부등본Item> create등기부등본Item(등기부등본 등기부등본, List<등기부등본_사항> items) throws Exception {
		Collection<등기부등본Item> res = new LinkedList<등기부등본Item>();
		for (int i = 0; i < items.size(); i++) {
			res.add(insertItem(등기부등본, items, i));
		}

		return res;
	}

	private 등기부등본Item insertItem(등기부등본 등기부등본, List<등기부등본_사항> items, int i) {

		등기부등본_사항 entity = items.get(i);
		entity.processRight();
		String purpose = entity.getPurpose();
		String bigo = parseBigo(entity, purpose);
		String right_type = purpose;
		String accept_date = entity.getAcceptDate();
		String right_person = formatRightPeople(entity);
		String right_price = 금액Converter.convert(entity.getRightPrice());
		String expiration = entity.getFlagString();
		String comment = bigo;
		int no = i + 1;
		logger.info(no + "  " + right_type + "  " + accept_date + "  " + right_person + "  " + right_price + "  "
				+ expiration + "  " + comment);

		등기부등본Item item = new 등기부등본Item(물건, 등기부등본, right_type, accept_date, right_person, right_price, expiration,
				comment, no);
		return item;

	}

	private void insertRecord(List<등기부등본_사항> items) throws Exception {
		등기부등본 등기부등본 = new 등기부등본(물건, type);
		등기부등본.setItems(create등기부등본Item(등기부등본, items));
		등기부등본Dao dao = (등기부등본Dao) App.context.getBean("등기부등본DAO");
		dao.save(등기부등본);
	}

	private boolean isNo(String bigo) {
		if (bigo.startsWith("갑구") || bigo.startsWith("을구")) {
			return true;
		}
		Pattern p = Pattern.compile("\\(\\d+\\)$");
		Matcher m = p.matcher(bigo);
		return m.find();
	}

	private boolean isStartNo(String bigo) {
		Matcher m = p2.matcher(bigo);
		return m.find();
	}

	private String parseBigo(등기부등본_사항 entity, String purpose) {
		String bigo = entity.getBigo().trim();

		if ("소유권".equals(bigo)) {
			bigo = "";
		} else if (purpose.trim().equals(bigo)) {
			bigo = "";
		} else if (isNo(bigo)) {
			bigo = "";
		} else if (isStartNo(bigo)) {
			bigo = cutNo(bigo);
		}
		bigo = removeDuplicated(bigo, ",");
		return bigo;
	}

	private String removeDuplicated(String bigo, String separator) {
		if (bigo.contains(separator) == false) {
			return bigo;
		}
		String[] tokens = bigo.split(separator);
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < tokens.length; i++) {
			boolean isDupplicated = false;
			for (int k = i - 1; k >= 0; k--) {
				if (tokens[i].equals(tokens[k])) {
					isDupplicated = true;
				}
			}

			if (isDupplicated) {
				continue;
			}
			res.append(tokens[i]).append(" ");

		}
		return res.toString();
	}

	private void uploadPDF() throws HttpException, IOException, DocumentException {
		if (fileToUpload == null)
			return;
		FileUploaderBG uploader = FileUploaderBG.getInstance();
		String susffix = "land";
		if (type.contains("건물")) {
			susffix = "building";
		}
		logger.info("PDF_" + susffix + ".pdf 을 "+물건.getPath()+"에 업로드 합니다. ");
		FileUploaderBG.getInstance().upload(물건.getPath(), "PDF_" + susffix + ".pdf", fileToUpload);

	}

}
