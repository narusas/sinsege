package net.narusas.aceauction.fetchers;

import java.io.IOException;

import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class 지지옥션Fetcher extends PageFetcher {

	public 지지옥션Fetcher() throws HttpException, IOException {
		super("http://www.ggi.co.kr");
	}

	public byte[] fetch건물등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1+D.pdf");

		return chekcPDF(m);
	}

	public byte[] fetch등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1.pdf");
		return chekcPDF(m);
	}

	public byte[] fetch토지등기부등본(법원 court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get지지옥션Code() + "/" + year + "/" + year + no
				+ "_1-1+E.pdf");
		return chekcPDF(m);
	}

	private byte[] chekcPDF(GetMethod m) throws IOException {
		if (m.getStatusCode() == 200) {
			return m.getResponseBody();
		}
		return null;
	}

	@Override
	protected void prepare() throws HttpException, IOException {
	}

}
