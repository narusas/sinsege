/*
 * 
 */
package net.narusas.aceauction.data;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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
 * The Class FileUploader.
 */
public class FileUploader {
	
	/** The instance. */
	static FileUploader instance = new FileUploader();
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	
	/** The url. */
	static String url;
	
	/** The list. */
	private LinkedList<Task> list;

	static {
		try {
			url = NFile.getText(new File("fileupload.cfg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new file uploader.
	 */
	public FileUploader() {
		// super("File Uploader Thread");
		list = new LinkedList<Task>();
		// setDaemon(true);
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
		// add(new Task(client, filePost));
		int status = client.executeMethod(filePost);
		// System.out.println("## FIle Upload:" + status);
		// System.out.println(filePost.getResponseBodyAsString());
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}

	}

	/**
	 * Wait done.
	 */
	public void waitDone() {
		for (Task task : list) {
			try {
				task.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		list.clear();
	}

	/**
	 * Adds the.
	 * 
	 * @param task the task
	 */
	private void add(Task task) {
		list.add(task);
		task.start();
	}

	// public void run() {
	// while (true) {
	// if (list.size() == 0) {
	// skip(1000L);
	// continue;
	// }
	// Task task = list.remove(0);
	// task.start();
	// running.add(task;)
	// }
	// }

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
	 * Gets the single instance of FileUploader.
	 * 
	 * @return single instance of FileUploader
	 */
	public static FileUploader getInstance() {
		return instance;
	}
}

class Task extends Thread {

	final HttpClient client;

	final PostMethod filePost;

	public Task(HttpClient client, PostMethod filePost) {
		this.client = client;
		this.filePost = filePost;
	}

	public void run() {
		try {
			client.executeMethod(filePost);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
