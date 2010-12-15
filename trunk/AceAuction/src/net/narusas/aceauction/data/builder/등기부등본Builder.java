package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.FileUploader;
import net.narusas.aceauction.data.금액Converter;
import net.narusas.aceauction.model.등기부등본;
import net.narusas.aceauction.model.등기부등본Item;
import net.narusas.aceauction.pdf.itext.등기부등본날자변경;
import net.narusas.aceauction.pdf.jpedal.등기부등본Parser;
import net.narusas.aceauction.pdf.jpedal.등기부등본_사항;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

import com.lowagie.text.DocumentException;

public class 등기부등본Builder {
	static Logger logger = Logger.getLogger("log");

	static Pattern p2 = Pattern.compile("^\\d번");

	private File f;

	private final String filePath;

	private final String[] owners;

	private final int parentID;
	private String type;

	private final String url;

	public 등기부등본Builder(int parentID, String url, String[] owners,
			String filePath) {
		this.parentID = parentID;
		this.url = url;
		this.owners = owners;
		this.filePath = filePath;

	}

	public void execute() throws Exception {
		boolean hadInserted = dbUpdate();
		if (hadInserted){
			uploadPDF();	
		}
		
	}

	private String cutNo(String bigo) {
		Matcher m = p2.matcher(bigo);
		if (m.find()) {
			return bigo.substring(m.end());
		}
		return bigo;
	}

	private boolean dbUpdate() throws MalformedURLException, IOException,
			Exception, SQLException {
		byte[] data = downloadPDFBinary();
		if (data == null || data.length == 0) {
			return false;
		}
		NFile.write(f, data);

		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> items = parser.parseAll(f.getAbsolutePath(), owners);
		if (items == null || items.size() == 0) {
			return false;
		}
		String text = parser.getFirst().getText();
		type = text.substring(text.indexOf("-") + 1).trim();

		등기부등본 등본 = 등기부등본.findByGoodsId(parentID, type);
		if (등본 != null) {
			return false;
		}

		insertRecord(items);
		return true;

	}

	private byte[] downloadPDFBinary() throws MalformedURLException,
			IOException {
		URL u = new URL(url);
		f = File.createTempFile("등기부등본", ".pdf");
		byte[] data = net.narusas.util.lang.NInputStream.readBytes(u
				.openStream());
		return data;
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

	private void insertAttestedStatement(List<등기부등본_사항> items, long attested_id)
			throws Exception {

		for (int i = 0; i < items.size(); i++) {
			insertItem(items, attested_id, i);

		}
	}

	private void insertItem(List<등기부등본_사항> items, long attested_id, int i) {

		try {
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
			long goods_id = parentID;
			int no = i + 1;

			등기부등본Item item = new 등기부등본Item(right_type, accept_date,
					right_person, right_price, expiration, comment, goods_id,
					no, attested_id);
			item.insert();

		} catch (Throwable e) {
			logger.log(Level.WARNING, "Insert item error", e);
			e.printStackTrace();
		}
	}

	private void insertRecord(List<등기부등본_사항> items) throws Exception {
		등기부등본 item = new 등기부등본(parentID, type);
		item.insert();
		insertAttestedStatement(items, item.getId());
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

	private void uploadPDF() throws HttpException, IOException,
			DocumentException {
		FileUploader uploader = FileUploader.getInstance();
		String susffix = "land";
		if (type.contains("건물")) {
			susffix = "building";
		}

		uploader.upload(filePath, "PDF_" + susffix + ".pdf", 등기부등본날자변경
				.convert(f.getAbsolutePath()));
	}

}
