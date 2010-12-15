package net.narusas.aceauction.data;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class FileUploadTest extends TestCase {
	public void testUpload() throws HttpException, IOException {
		HttpClient client = new HttpClient();
		HttpState state = new HttpState();
		PostMethod filePost = new PostMethod("http://localhost/upload.php");
		File targetFile = new File("fixtures/건물등기부등본6364.pdf");
		Part[] parts = { new FilePart("UPLOADFILE", targetFile), new StringPart("path", "abc/a/"),
				new StringPart("filename", "abc.pdf") };

		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
		int status = client.executeMethod(filePost);
		assertEquals(HttpStatus.SC_OK, status);
		System.out.println(filePost.getResponseBodyAsString());
	}
}
