/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.xml.transform.TransformerException;

// TODO: Auto-generated Javadoc
/**
 * XML������ XLST �ļ��� �̿��Ͽ� �ٸ� ������ ���� ��ȯ��Ų��.
 * 
 * @author narusas
 */
public class XSLTParser {

	/** The url. */
	private URL url;

	/**
	 * ����� XLST�� ��ġ.
	 * 
	 * @param xlstURL the xlst url
	 */
	public XSLTParser(String xlstURL) {
		url = getClass().getResource(xlstURL);
	}

	/**
	 * XML�� ����ȯ ��Ų��. �Ϲ������� HTML�� ����ȯ ��Ų��.
	 * 
	 * @param text the text
	 * 
	 * @return the string
	 * 
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TransformerException the transformer exception
	 */
	public String parse(String text) throws UnsupportedEncodingException,
			IOException, TransformerException {
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(
				new StringReader(text));
		javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(
				new InputStreamReader(url.openStream(), "euc-kr"));
		StringWriter sw = new StringWriter();
		javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(
				sw);

		javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory
				.newInstance();

		javax.xml.transform.Transformer trans;
		trans = transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, result);
		return sw.getBuffer().toString();
	}
}
