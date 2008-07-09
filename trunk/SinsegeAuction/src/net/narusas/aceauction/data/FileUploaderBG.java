/*
 * 
 */
package net.narusas.aceauction.data;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUploaderBG.
 */
public class FileUploaderBG extends Thread {
	
	/** The instance. */
	static FileUploaderBG instance = new FileUploaderBG();
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	
	/** The url. */
	static String url;
	
	/** The count. */
	private int count;
	
	/** The executors. */
	private ExecutorService executors;

	/** The list. */
	private LinkedList<BGTask> list;

	/** The listeners. */
	LinkedList<FileUploadListener> listeners = new LinkedList<FileUploadListener>();

	static {
		try {
			url = NFile.getText(new File("fileupload.cfg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new file uploader bg.
	 */
	private FileUploaderBG() {
		super("File Uploader Thread");
		list = new LinkedList<BGTask>();
		setDaemon(true);
		executors = java.util.concurrent.Executors.newFixedThreadPool(5);
	}

	/**
	 * Adds the listener.
	 * 
	 * @param listener the listener
	 */
	public void addListener(FileUploadListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			// System.out.println("################################");
			// System.out.println("#### FileUploadBG:"+list.size());
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

	/**
	 * Upload.
	 * 
	 * @param path the path
	 * @param filename the filename
	 * @param targetFile the target file
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void upload(String path, String filename, File targetFile) throws HttpException,
			IOException {
		logger.info(
				"Upload File Path:" + path + " FileName:" + filename + " SRC:" + targetFile
						+ " TO:" + url);
		HttpClient client = new HttpClient();
		PostMethod filePost = new PostMethod(url);
		Part[] parts = { new FilePart("UPLOADFILE", targetFile), new StringPart("path", path),
				new StringPart("filename", filename) };

		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
		add(new BGTask(path + filename, client, filePost));
		// int status = client.executeMethod(filePost);
		// System.out.println("## FIle Upload:" + status);
		// System.out.println(filePost.getResponseBodyAsString());

	}

	/**
	 * Adds the.
	 * 
	 * @param task the task
	 */
	private void add(BGTask task) {
		count++;
		list.add(task);
		executors.execute(task);
	}

	/**
	 * Notify listeners.
	 * 
	 * @param count the count
	 * @param remains the remains
	 * @param name the name
	 */
	private void notifyListeners(int count, int remains, String name) {
		for (FileUploadListener listener : listeners) {
			listener.update(count, remains, name);
		}
	}

	/**
	 * Skip.
	 * 
	 * @param l the l
	 */
	private void skip(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the single instance of FileUploaderBG.
	 * 
	 * @return single instance of FileUploaderBG
	 */
	public static FileUploaderBG getInstance() {
		return instance;
	}
}

class BGTask implements Runnable {
	static Logger logger = Logger.getLogger("log");
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
			logger.log(Level.WARNING, "Try to upload(Trial " + (i + 1) + "). " + name2);
			try {
				int code = client.executeMethod(filePost);
				page = filePost.getResponseBodyAsString();
				if (code != 200) {
					logger.log(Level.WARNING, "Fail to upload. Response code is not 200." + code
							+ "\n" + page);
					continue;
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "Fail to upload", e + "\n" + page);
				continue;
			}
			logger.log(Level.INFO, "Success to upload " + name2 + "\n" + page);
			break;
		}

		File f = new File(name2);
		if (f.exists()) {
			f.delete();
		}
	}

	String getPath() {
		return name2;
	}
}
