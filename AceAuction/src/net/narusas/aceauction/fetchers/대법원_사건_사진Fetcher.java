package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

public class 대법원_사건_사진Fetcher {
	public List<File> fetch(String path, String bub_cd, long sa_no) throws HttpException,
			IOException {
		List<File> res = new LinkedList<File>();

		PageFetcher f = 대법원Fetcher.getInstance();

		int count = 0;
		for (int i = 1; i < 4; i++) {
			byte[] jpg = f.fetchBinary("http://www.courtauction.go.kr/au/hh100/hhCc00.jsp?bub_cd="
					+ bub_cd + "&sa_no=" + sa_no + "&ord_hoi=" + i + "&img_type=1&ser_no=1", null);
			if (jpg == null || jpg.length == 0) {
				continue;
			}
			File file = new File(path, "pic_" + count + ".jpg");
			NFile.write(file, jpg);
			res.add(file);
			count++;
		}
		return res;
	}

}
