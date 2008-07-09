package net.narusas.aceauction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class FileUploadTest extends TestCase {
	public void testHTTPClient() throws FileNotFoundException {
		HttpClient client =  new HttpClient();
		PostMethod post = new PostMethod();
		post.setRequestBody(new FileInputStream(new File("fixtures/건물등기부등본6364.pdf")));
	}
}
