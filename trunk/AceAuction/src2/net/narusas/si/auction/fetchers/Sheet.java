package net.narusas.si.auction.fetchers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class Sheet {
	List<String> headers = new ArrayList<String>();
	List<List<String>> rows = new ArrayList<List<String>>();

	public void setHeader(Node node) {
		NodeList childs = node.getChildren();
		for (int i = 0; i < childs.size(); i++) {
			Node n = childs.elementAt(i);
			if (n.getText().startsWith("th")) {
				if (n.getChildren() == null) {
					headers.add("");
				} else {

					headers.add(n.getChildren().elementAt(0).getText().trim());
				}
			}
		}
	}

	public void addRow(Node node, boolean useComplexTD) {
		LinkedList<String> row = new LinkedList<String>();
		NodeList childs = node.getChildren();
		for (int i = 0; i < childs.size(); i++) {
			Node n = childs.elementAt(i);
			if (n.getText().startsWith("td")) {
				if (n.getChildren() == null) {
					row.add("");
				} else {
					if (useComplexTD) {

						StringBuffer buf = new StringBuffer();
						NodeList txtChilds = n.getChildren();
						for (int k = 0; k < txtChilds.size(); k++) {
							buf.append(txtChilds.elementAt(k).getText().trim());
						}
						String temp = buf.toString();
						temp = HTMLUtils.stripCRLF(temp);
						temp = temp.replaceAll("br /", "\n");

						String[] lines = temp.split("\n");
						StringBuffer buf2 = new StringBuffer();
						for (int k = 0; k < lines.length; k++) {
							if (k >= 1) {
								buf2.append("\n");
							}
							String ttt = HTMLUtils.stripWhitespaces(lines[k]);
//							ttt = ttt.replaceAll(" ", "");
							buf2.append(HTMLUtils.converHTMLSpecialChars(ttt));
						}

						row.add(buf2.toString());
					} else {
						row.add(HTMLUtils.strip(HTMLUtils.stripCRLF(n.getChildren().elementAt(0).getText()
								.trim())));
					}

				}
			}
		}

		rows.add(row);

	}

	public String getHeader(int i) {
		return headers.get(i);
	}

	public int headerSize() {
		return headers.size();
	}

	public int rowSize() {
		return rows.size();
	}

	public String valueAt(int row, int column) {
		return rows.get(row).get(column);
	}

	public static Sheet parse(String html, String targetCaption) {
		return parse(html, targetCaption, true);
	}

	public static Sheet parse(String html, String targetCaption, boolean haveHeader) {
		return parse(html, targetCaption, haveHeader, false);
	}

	public int rowColumnSize(int row) {
		return rows.get(row).size();
	}

	public static Sheet parse(String html, String targetCaption, boolean haveHeader, boolean useComplexTD) {
		try {
			int start = html.indexOf(targetCaption);

			String chunk = html.substring(start, html.indexOf("</tbody>", start) + 8);
			Parser parser = new Parser();
			parser.setFeedback(Parser.STDOUT);
			Parser.getConnectionManager().setMonitor(parser);
			Parser.getConnectionManager().setRedirectionProcessingEnabled(true);
			Parser.getConnectionManager().setCookieProcessingEnabled(true);
			parser.setInputHTML(chunk);
			NodeList ns = parser.parse(null);
			List<Node> trNodes = new ArrayList<Node>();

			for (int i = 0; i < ns.size(); i++) {
				Node n = ns.elementAt(i);
				if (n.getText().startsWith("tr")) {
					trNodes.add(n);
				} else if (n.getText().startsWith("thead")) {
					NodeList childs = n.getChildren();
					if (childs == null) {
						continue;
					}
					for (int k = 0; k < childs.size(); k++) {
						Node child = ns.elementAt(i);
						if (child.getText().startsWith("tr")) {
							trNodes.add(child);
						}
					}
					break;
				}
			}
			Sheet sheet = new Sheet();
			int trStart = 0;
			if (haveHeader) {
				trStart = 1;
				sheet.setHeader(trNodes.get(0));
			}
			for (int i = trStart; i < trNodes.size(); i++) {
				sheet.addRow(trNodes.get(i), useComplexTD);
			}
			return sheet;
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

	}

}
