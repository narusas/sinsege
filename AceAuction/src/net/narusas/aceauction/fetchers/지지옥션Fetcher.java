package net.narusas.aceauction.fetchers;

import java.io.IOException;

import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class ��������Fetcher extends PageFetcher {

	public ��������Fetcher() throws HttpException, IOException {
		super("http://www.ggi.co.kr");
	}

	public byte[] fetch�ǹ����ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
				+ "_1-1+D.pdf");

		return chekcPDF(m);
	}

	public byte[] fetch���ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
				+ "_1-1.pdf");
		return chekcPDF(m);
	}

	public byte[] fetch�������ε(���� court, int year, int no) throws HttpException, IOException {
		GetMethod m = get("/P_dungki/" + court.get��������Code() + "/" + year + "/" + year + no
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
