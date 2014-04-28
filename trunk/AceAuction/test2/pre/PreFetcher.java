package pre;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PreFetcher {
	HttpClient client = new HttpClient();
	HttpState state = new HttpState();

	public PreFetcher() {
		client.setState(state);
	}

	public List<담당계> fetch담당계(String 법원) {
		try {
			PostMethod m = new PostMethod("https://www.courtauction.go.kr/RetrieveJpDeptNonAllCodeList.ajax?query="
					+ URLEncoder.encode(법원, "UTF-8") + "&tid=idJpDeptCode");

			m.setRequestHeader("Host", "www.courtauction.go.kr");
			m.setRequestHeader("Referer", "https://www.courtauction.go.kr/InitMulSrch.laf");
			client.executeMethod(m);
			ArrayList<담당계> list = new ArrayList<담당계>();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(new ByteArrayInputStream(m.getResponseBody()));
			NodeList root = doc.getChildNodes();
			Node xsync = root.item(0);
			NodeList select = xsync.getChildNodes().item(1).getChildNodes();

			for (int i = 0; i < select.getLength(); i++) {
				Node child = select.item(i);
				if ("option".equals(child.getNodeName())) {
					담당계 _담당계 = new 담당계();
					_담당계.id = child.getAttributes().getNamedItem("value").getTextContent();
					_담당계.name = child.getTextContent().trim();
					list.add(_담당계);
				}
			}
			return list;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (HttpException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}
}

class 담당계 {
	String id;
	String name;

	@Override
	public String toString() {
		return "담당계 [id=" + id + ", name=" + name + "]";
	}

}