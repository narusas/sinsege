package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.사건;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.evot.util.concurrency.ThreadPool;

public class 사진Downloader extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	private static 사진Downloader instance;
	List<DownloadItem> remains = new LinkedList<DownloadItem>();
	private 대법원Fetcher fetcher;
	int max = 0;
	int current = 0;
	ThreadPool pool = new ThreadPool(10);
	static Object lock = new Object();

	private 사진Downloader() {
		try {
			fetcher = new 대법원Fetcher();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static 사진Downloader getInstance() {
		if (instance == null) {
			instance = new 사진Downloader();
			instance.start();
		}
		return instance;
	}

	public void add(사건 사건, List<String> urls) {
		max += urls.size();
		EventNotifier.set사진목록Size(max);
		synchronized (remains) {
			remains.add(new DownloadItem(사건, urls));
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			synchronized (remains) {

				if (remains.size() == 0) {
					continue;
				}

			}
			DownloadItem item = remains.remove(0);

			download(item);
		}
	}

	private void download(DownloadItem item) {
		for (int i = 0; i < item.urls.size(); i++) {
			String query = item.urls.get(i);
			System.out.println("Downlod :" + query);
			File dir = new File("download/" + item.사건.getPath());
			if (dir.exists() == false) {
				dir.mkdirs();
			}
			File f = new File(dir, "pic_courtauction_" + i + ".jpg");
			pool.assign(new Job(query, item.사건.getPath(), f));

			// fetcher.downloadBinary(query, f);

		}
	}

	class Job implements Runnable {

		private final String query;
		private final File f;
		private final String path;

		public Job(String query, String path, File f) {
			this.query = query;
			this.path = path;
			this.f = f;
		}

		@Override
		public void run() {
			try {
				new 대법원Fetcher().downloadBinary(query, f);
				FileUploaderBG.getInstance().upload(path, f.getName(), f);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			synchronized (lock) {
				current++;
				EventNotifier.progress사진목록Current(current);
			}
		}

	}
}

class DownloadItem {
	사건 사건;
	List<String> urls;

	public DownloadItem(사건 사건, List<String> urls) {
		super();
		this.사건 = 사건;
		this.urls = urls;
	}

}