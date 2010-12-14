package net.narusas.si.auction.builder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class FileUploaderBG extends Thread {
	static FileUploaderBG instance = new FileUploaderBG();
	static Logger logger = Logger.getLogger("log");
	static String url;
	private int count;
	private ExecutorService executors;
	int size;
	int progress;
	private LinkedList<BGTask> list;

	LinkedList<FileUploadListener> listeners = new LinkedList<FileUploadListener>();

	static {
		try {
			url = NFile.getText(new File("cfg/fileupload.cfg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileUploaderBG() {
		super("File Uploader Thread");
		list = new LinkedList<BGTask>();
		setDaemon(true);
		executors = java.util.concurrent.Executors.newFixedThreadPool(5);
	}

	public void addListener(FileUploadListener listener) {
		listeners.add(listener);
	}

	public void run() {
		while (true) {
			notifyListeners(count, list.size(), "");
			if (list.size() == 0) {

				skip(1000L);
				continue;
			}
			BGTask task = list.remove(0);

			notifyListeners(count, list.size(), task.getPath());
			task.run();

		}
	}

	public void upload(String path, String filename, File targetFile) throws HttpException, IOException {
		logger.info("Upload File Path:" + path + " FileName:" + filename + " SRC:" + targetFile + " TO:" + url);
		HttpClient client = new HttpClient();
		PostMethod filePost = new PostMethod(url);
		Part[] parts = { new FilePart("UPLOADFILE", targetFile), new StringPart("path", path),
				new StringPart("filename", filename) };

		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
		size++;
		EventNotifier.set업로드Size(size);
		add(new BGTask(path + filename, client, filePost));
	}

	private void add(BGTask task) {
		count++;
		list.add(task);
		executors.execute(task);
	}

	private void notifyListeners(int count, int remains, String name) {
		for (FileUploadListener listener : listeners) {
			listener.update(count, remains, name);
		}
	}

	private void skip(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static FileUploaderBG getInstance() {
		return instance;
	}

	class BGTask implements Runnable {
		private final String name2;

		final HttpClient client;

		final PostMethod filePost;

		public BGTask(String name, HttpClient client, PostMethod filePost) {
			name2 = name;
			this.client = client;
			this.filePost = filePost;
		}

		public void run() {
			String page = "";
			for (int i = 0; i < 5; i++) {
//				logger.log(Level.WARNING, "Try to upload(Trial " + (i + 1) + "). " + name2);
				try {
					int code = client.executeMethod(filePost);
					page = filePost.getResponseBodyAsString();
					if (code != 200) {
//						logger.log(Level.WARNING, "Fail to upload. Response code is not 200." + code + "\n" + page);
						continue;
					}
				} catch (Exception e) {
//					logger.log(Level.WARNING, "Fail to upload", e + "\n" + page);
					continue;
				}
//				logger.log(Level.INFO, "Success to upload " + name2 + "\n" + page);
				break;
			}
			progress++;
			EventNotifier.progress업로드Current(progress);

			// File f = new File(name2);
			// if (f.exists()) {
			// f.delete();
			// }
		}

		String getPath() {
			return name2;
		}
	}
}
