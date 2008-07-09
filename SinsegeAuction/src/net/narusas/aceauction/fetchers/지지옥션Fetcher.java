/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;

import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * The Class 지지옥션Fetcher.
 */
public class 지지옥션Fetcher extends PageFetcher {

	/**
	 * Instantiates a new 지지옥션 fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public 지지옥션Fetcher() throws HttpException, IOException {
		super("http://www.ggi.co.kr");
	}

	/**
	 * Fetch건물등기부등본.
	 * 
	 * @param court the court
	 * @param year the year
	 * @param no the no
	 * 
	 * @return the byte[]
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] fetch건물등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1+D.pdf");

		return chekcPDF(m);
	}

	/**
	 * Fetch등기부등본.
	 * 
	 * @param court the court
	 * @param year the year
	 * @param no the no
	 * 
	 * @return the byte[]
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] fetch등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1.pdf");
		return chekcPDF(m);
	}

	/**
	 * Fetch토지등기부등본.
	 * 
	 * @param court the court
	 * @param year the year
	 * @param no the no
	 * 
	 * @return the byte[]
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] fetch토지등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1+E.pdf");
		return chekcPDF(m);
	}

	/**
	 * Chekc pdf.
	 * 
	 * @param m the m
	 * 
	 * @return the byte[]
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private byte[] chekcPDF(GetMethod m) throws IOException {
		if (m.getStatusCode() == 200) {
			return m.getResponseBody();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.fetchers.PageFetcher#prepare()
	 */
	@Override
	protected void prepare() throws HttpException, IOException {
	}

}
