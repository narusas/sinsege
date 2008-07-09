/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.narusas.aceauction.model.����;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * The Class �����ɾ�Fetcher.
 */
public class �����ɾ�Fetcher extends PageFetcher {

	/**
	 * Instantiates a new �����ɾ� fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	�����ɾ�Fetcher() throws HttpException, IOException {
		super("http://image.infocare.co.kr");
	}

	/**
	 * Fetch���ǻ���.
	 * 
	 * @param court the court
	 * @param year the year
	 * @param no the no
	 * 
	 * @return the list< file>
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<File> fetch���ǻ���(���� court, String year, String no) throws HttpException, IOException {
		no = fixNo(no);
		String path = "/mulgun/" + court.get�����ɾ�Code() + "/" + year + "/" + year + no
				+ "000100010.jpg";
		// System.out.println(path);
		GetMethod m = get(path);

		File f = new File("���ǻ���.jpg");
		NFile.write(f, m.getResponseBody());
		return null;
	}

	/**
	 * Fix no.
	 * 
	 * @param no the no
	 * 
	 * @return the string
	 */
	private String fixNo(String no) {
		if (no.length() == 7) {
			return no;
		}

		for (int i = 0; i <= 7 - no.length(); i++) {
			no = "0" + no;
		}
		return no;
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.fetchers.PageFetcher#prepare()
	 */
	@Override
	protected void prepare() throws HttpException, IOException {
	}

}
