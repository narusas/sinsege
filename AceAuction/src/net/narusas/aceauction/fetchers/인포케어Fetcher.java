package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.narusas.aceauction.model.����;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class �����ɾ�Fetcher extends PageFetcher {

	�����ɾ�Fetcher() throws HttpException, IOException {
		super("http://image.infocare.co.kr");
	}

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

	private String fixNo(String no) {
		if (no.length() == 7) {
			return no;
		}

		for (int i = 0; i <= 7 - no.length(); i++) {
			no = "0" + no;
		}
		return no;
	}

	@Override
	protected void prepare() throws HttpException, IOException {
	}

}
