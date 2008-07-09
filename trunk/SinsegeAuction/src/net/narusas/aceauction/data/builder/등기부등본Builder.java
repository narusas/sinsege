/*
 * 
 */
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
import net.narusas.aceauction.data.�ݾ�Converter;
import net.narusas.aceauction.model.���ε;
import net.narusas.aceauction.model.���εItem;
import net.narusas.aceauction.pdf.itext.���ε���ں���;
import net.narusas.aceauction.pdf.jpedal.���εParser;
import net.narusas.aceauction.pdf.jpedal.���ε_����;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

import com.lowagie.text.DocumentException;

// TODO: Auto-generated Javadoc
/**
 * The Class ���εBuilder.
 */
public class ���εBuilder {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p2. */
	static Pattern p2 = Pattern.compile("^\\d��");

	/** The f. */
	private File f;

	/** The file path. */
	private final String filePath;

	/** The owners. */
	private final String[] owners;

	/** The parent id. */
	private final int parentID;
	
	/** The type. */
	private String type;

	/** The url. */
	private final String url;

	/**
	 * Instantiates a new ���ε builder.
	 * 
	 * @param parentID the parent id
	 * @param url the url
	 * @param owners the owners
	 * @param filePath the file path
	 */
	public ���εBuilder(int parentID, String url, String[] owners,
			String filePath) {
		this.parentID = parentID;
		this.url = url;
		this.owners = owners;
		this.filePath = filePath;

	}

	/**
	 * Execute.
	 * 
	 * @throws Exception the exception
	 */
	public void execute() throws Exception {
		boolean hadInserted = dbUpdate();
		if (hadInserted){
			uploadPDF();	
		}
		
	}

	/**
	 * Cut no.
	 * 
	 * @param bigo the bigo
	 * 
	 * @return the string
	 */
	private String cutNo(String bigo) {
		Matcher m = p2.matcher(bigo);
		if (m.find()) {
			return bigo.substring(m.end());
		}
		return bigo;
	}

	/**
	 * Db update.
	 * 
	 * @return true, if successful
	 * 
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception the exception
	 * @throws SQLException the SQL exception
	 */
	private boolean dbUpdate() throws MalformedURLException, IOException,
			Exception, SQLException {
		byte[] data = downloadPDFBinary();
		if (data == null || data.length == 0) {
			return false;
		}
		NFile.write(f, data);

		���εParser parser = new ���εParser();
		List<���ε_����> items = parser.parseAll(f.getAbsolutePath(), owners);
		if (items == null || items.size() == 0) {
			return false;
		}
		String text = parser.getFirst().getText();
		type = text.substring(text.indexOf("-") + 1).trim();

		���ε � = ���ε.findByGoodsId(parentID, type);
		if (� != null) {
			return false;
		}

		insertRecord(items);
		return true;

	}

	/**
	 * Download pdf binary.
	 * 
	 * @return the byte[]
	 * 
	 * @throws MalformedURLException the malformed url exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private byte[] downloadPDFBinary() throws MalformedURLException,
			IOException {
		URL u = new URL(url);
		f = File.createTempFile("���ε", ".pdf");
		byte[] data = net.narusas.util.lang.NInputStream.readBytes(u
				.openStream());
		return data;
	}

	/**
	 * Format right people.
	 * 
	 * @param item the item
	 * 
	 * @return the string
	 */
	private String formatRightPeople(���ε_���� item) {

		Set<String> names = item.getRightPeoples();
		String res = names.toString();
		if (res == null || "".equals(res) || "[]".equals(res)) {
			res = item.getRightPeople();

		} else {
			res = res.split(",")[0];
		}
		res = res.replaceAll("\\[", "");
		res = res.replaceAll("\\]", "");
		res = res.replaceAll("�ֽ�ȸ��", "");
		res = res.replaceAll("\\(��\\)", "");
		if ("����".equals(res)) {
			res = "";
		}
		return res;
	}

	/**
	 * Insert attested statement.
	 * 
	 * @param items the items
	 * @param attested_id the attested_id
	 * 
	 * @throws Exception the exception
	 */
	private void insertAttestedStatement(List<���ε_����> items, long attested_id)
			throws Exception {

		for (int i = 0; i < items.size(); i++) {
			insertItem(items, attested_id, i);

		}
	}

	/**
	 * Insert item.
	 * 
	 * @param items the items
	 * @param attested_id the attested_id
	 * @param i the i
	 */
	private void insertItem(List<���ε_����> items, long attested_id, int i) {

		try {
			���ε_���� entity = items.get(i);
			entity.processRight();
			String purpose = entity.getPurpose();
			String bigo = parseBigo(entity, purpose);
			String right_type = purpose;
			String accept_date = entity.getAcceptDate();
			String right_person = formatRightPeople(entity);
			String right_price = �ݾ�Converter.convert(entity.getRightPrice());
			String expiration = entity.getFlagString();
			String comment = bigo;
			long goods_id = parentID;
			int no = i + 1;

			���εItem item = new ���εItem(right_type, accept_date,
					right_person, right_price, expiration, comment, goods_id,
					no, attested_id);
			item.insert();

		} catch (Throwable e) {
			logger.log(Level.WARNING, "Insert item error", e);
			e.printStackTrace();
		}
	}

	/**
	 * Insert record.
	 * 
	 * @param items the items
	 * 
	 * @throws Exception the exception
	 */
	private void insertRecord(List<���ε_����> items) throws Exception {
		���ε item = new ���ε(parentID, type);
		item.insert();
		insertAttestedStatement(items, item.getId());
	}

	/**
	 * Checks if is no.
	 * 
	 * @param bigo the bigo
	 * 
	 * @return true, if is no
	 */
	private boolean isNo(String bigo) {
		if (bigo.startsWith("����") || bigo.startsWith("����")) {
			return true;
		}
		Pattern p = Pattern.compile("\\(\\d+\\)$");
		Matcher m = p.matcher(bigo);
		return m.find();
	}

	/**
	 * Checks if is start no.
	 * 
	 * @param bigo the bigo
	 * 
	 * @return true, if is start no
	 */
	private boolean isStartNo(String bigo) {
		Matcher m = p2.matcher(bigo);
		return m.find();
	}

	/**
	 * Parses the bigo.
	 * 
	 * @param entity the entity
	 * @param purpose the purpose
	 * 
	 * @return the string
	 */
	private String parseBigo(���ε_���� entity, String purpose) {
		String bigo = entity.getBigo().trim();

		if ("������".equals(bigo)) {
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

	/**
	 * Removes the duplicated.
	 * 
	 * @param bigo the bigo
	 * @param separator the separator
	 * 
	 * @return the string
	 */
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

	/**
	 * Upload pdf.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DocumentException the document exception
	 */
	private void uploadPDF() throws HttpException, IOException,
			DocumentException {
		FileUploader uploader = FileUploader.getInstance();
		String susffix = "land";
		if (type.contains("�ǹ�")) {
			susffix = "building";
		}

		uploader.upload(filePath, "PDF_" + susffix + ".pdf", ���ε���ں���
				.convert(f.getAbsolutePath()));
	}

}
