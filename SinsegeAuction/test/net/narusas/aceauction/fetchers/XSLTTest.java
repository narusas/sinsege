package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class XSLTTest extends TestCase {
	public void test1() throws UnsupportedEncodingException,
			FileNotFoundException {
		// File xmlFile = new File("fixtures/부동산표시목록.xml");
		// File xsltFile = new File("부동산표시목록_view.xsl");
		// FileOutputStream fout = new FileOutputStream(new File("xmltest"));
		// javax.xml.transform.Source xmlSource = new
		// javax.xml.transform.stream.StreamSource(new InputStreamReader(new
		// FileInputStream(xmlFile), "euc-kr"));
		// javax.xml.transform.Source xsltSource = new
		// javax.xml.transform.stream.StreamSource(new InputStreamReader(new
		// FileInputStream(xsltFile), "euc-kr"));
		// javax.xml.transform.Result result = new
		// javax.xml.transform.stream.StreamResult(
		// fout);
		//
		// // create an instance of TransformerFactory
		// javax.xml.transform.TransformerFactory transFact =
		// javax.xml.transform.TransformerFactory
		// .newInstance();
		//
		// javax.xml.transform.Transformer trans;
		// try {
		// trans = transFact.newTransformer(xsltSource);
		// trans.transform(xmlSource, result);
		// } catch (TransformerConfigurationException e) {
		// e.printStackTrace();
		// } catch (TransformerException e) {
		// e.printStackTrace();
		// }
	}

	public void test2() throws IOException, TransformerException {
		XSLTParser parser = new XSLTParser("/xslt/부동산표시목록_view.xsl");
		String res = parser.parse(NFile
				.getText(new File("fixtures/부동산표시목록.xml")));
		// System.out.println(res);
	}
	
}
