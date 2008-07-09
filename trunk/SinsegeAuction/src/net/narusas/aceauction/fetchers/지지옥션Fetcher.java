/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;

import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * The Class ��������Fetcher.
 */
public class ��������Fetcher extends PageFetcher {

	/**
	 * Instantiates a new �������� fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ��������Fetcher() throws HttpException, IOException {
		super("http://www.ggi.co.kr");
	}

	/**
	 * Fetch�ǹ����ε.
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
	public byte[] fetch�ǹ����ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
				+ "_1-1+D.pdf");

		return chekcPDF(m);
	}

	/**
	 * Fetch���ε.
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
	public byte[] fetch���ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
				+ "_1-1.pdf");
		return chekcPDF(m);
	}

	/**
	 * Fetch�������ε.
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
	public byte[] fetch�������ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
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
