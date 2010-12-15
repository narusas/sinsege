package net.narusas.aceauction;

import java.io.UnsupportedEncodingException;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HTMLParserTest {
	public static void main(String[] args) throws ParserException,
			UnsupportedEncodingException {
		Parser parser = new Parser();
		parser.setFeedback(Parser.STDOUT);
		Parser.getConnectionManager().setMonitor(parser);
		Parser.getConnectionManager().setRedirectionProcessingEnabled(true);
		Parser.getConnectionManager().setCookieProcessingEnabled(true);
		parser.setInputHTML("<tag>abc</tag>");
//		parser.setResource("hangul.html");
		NodeList ns = parser.parse(null);
		for (int i = 0; i < ns.size(); i++) {
			Node n = ns.elementAt(i);
			System.out.println(new String(n.toPlainTextString().getBytes(
					"8859_1"), "euc-kr"));
		}
		// System.out.println (ns);
	}

}
