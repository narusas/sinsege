package net.narusas.aceauction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import junit.framework.TestCase;

public class TempTest extends TestCase {
	public void test1() throws UnknownHostException, IOException {
//		String request = "<SOAP-ENV:Envelope " + "xmlns:m=\"http://www.hubtea.com/soap/SimpleControl/\" "
//				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
//				+ "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" "
//				+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
//				+ "<SOAP-ENV:Body SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
//				+ "<m:control xmlns=\"urn:Controller\" id=\"o0\" SOAP-ENC:root=\"1\">\n"
//				+ "<caller xsi:type=\"xsd:string\">Wallpad</caller>\n"
//				+ "<target xsi:type=\"xsd:string\">mode</target>\n" + "<newValue xsi:type=\"xsd:int\">1</newValue>\n"
//				+ "</m:control>\n" + "</SOAP-ENV:Body>\n" + "</SOAP-ENV:Envelope>\n";
//		// System.out.println(request);
//		String req = "<SOAP-ENV:Envelope>" + "xmlns:m=\"http://www.hubtea.com/soap/SimpleControl/\" "
//				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
//				+ "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" "
//				+ "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<SOAP-ENV:Body>"
//				+ "<m:control xmlns=\"urn:Controller\" id=\"o0\" SOAP-ENC:root=\"1\">" + "</m:control>"
//				+ "</SOAP-ENV:Body>" + "</SOAP-ENV:Envelope>";
//
//		// HttpClient client = new HttpClient();
//		// HttpState state = new HttpState();
//		// PostMethod m = new PostMethod("http://localhost/soap.php");
//		// m.setRequestBody(new NameValuePair[] { new
//		// NameValuePair("soaprequest", req) });
//		// client.executeMethod(m);
//		//		
//		// System.out.println(m.getResponseBodyAsString());
//		// SoapServer ss = new SoapServer();
//		// ss.start();
//		// Thread.sleep(1000);
//		// URL u = new URL("http://localhost/soap")
//		 Socket s = new Socket("125.177.160.143", 8888);
//		// // Socket s = new Socket("59.6.208.158",8888);
//		// //
//		// // Socket s = new Socket("127.0.0.1",8888);
//		//
//		 OutputStream out = new BufferedOutputStream(s.getOutputStream());
//		//
//		 out.write(request.getBytes("utf-8"));
//		 out.flush();
//		 BufferedReader br = new BufferedReader(new
//		 InputStreamReader(s.getInputStream(), "utf-8"));
//		 while (true) {
//		 int r = br.read();
//		 if (r == -1) {
//		 break;
//		 }
//		 System.out.print((char) r);
//		 }
	}
	
	public void test3() {
		try {
			Process ps = Runtime.getRuntime().exec("cmd");
			OutputStream out = ps.getOutputStream();
			InputStream in = ps.getInputStream();
			while(true) {
				int r= in.read();
				if (r == -1){
					break;
				}
				System.out.print((char) r);
				if (r == '>'){
					out.write("start pdf1.pdf".getBytes());
					out.flush();
				}
			}
			Thread.sleep(100000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
