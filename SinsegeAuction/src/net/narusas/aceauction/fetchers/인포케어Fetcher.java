/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.narusas.aceauction.model.법원;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * The Class 인포케어Fetcher.
 */
public class 인포케어Fetcher extends PageFetcher {

	/**
	 * Instantiates a new 인포케어 fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	인포케어Fetcher() throws HttpException, IOException {
		super("http://image.infocare.co.kr");
	}

	/**
	 * Fetch물건사진.
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
	public List<File> fetch물건사진(법원 court, String year, String no) throws HttpException, IOException {
		no = fixNo(no);
		String path = "/mulgun/" + court.get인포케어Code() + "/" + year + "/" + year + no
				+ "000100010.jpg";
		// System.out.println(path);
		GetMethod m = get(path);

		File f = new File("물건사진.jpg");
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
