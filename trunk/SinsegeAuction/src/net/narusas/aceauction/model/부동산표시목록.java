/*
 * 
 */
package net.narusas.aceauction.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class 부동산표시목록.
 */
public class 부동산표시목록 {

	/** The res. */
	private LinkedList<부동산표시목록Item> res = new LinkedList<부동산표시목록Item>();
	
	/** The logger. */
	Logger logger = Logger.getLogger("log");

	/**
	 * Instantiates a new 부동산표시목록.
	 */
	public 부동산표시목록() {
	}

	/**
	 * Instantiates a new 부동산표시목록.
	 * 
	 * @param xml the xml
	 * 
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public 부동산표시목록(String xml) throws ParserConfigurationException,
			SAXException, IOException {
//		logger.info(xml);
		parse(xml);
	}

	/**
	 * Gets the item.
	 * 
	 * @param 물건 the 물건
	 * 
	 * @return the item
	 */
	public List<부동산표시목록Item> getItem(물건 물건) {

		List<부동산표시목록Item> result = new LinkedList<부동산표시목록Item>();
		HashSet<부동산표시목록Item> temp = new HashSet<부동산표시목록Item>();

		List<물건현황> statuses = 물건.물건현황;
		for (물건현황 status : statuses) {
//			logger.info("getItem " + status.get주소());

			String address = removeAllSpaces(status.get주소());
			address = removeBracedContents(address);

			for (부동산표시목록Item item : res) {
				String addr4 = removeAllSpaces(item.getAddress().addr4);
//				logger.info("Check address addr3=" + item.getAddress().addr3
//						+ ",addr4=" + item.getAddress().addr4);
				// 물건 주소가 표시목록의 "번지이하"로 끝나는 것만 추가한다.
				if (address.endsWith(addr4)) {
					temp.add(item);
				}
			}
		}
		if (result.size() == 0) {
//			logger.info("getItem " + 물건.get주소());
			String address = removeAllSpaces(물건.get주소());

			for (부동산표시목록Item item : res) {
				String addr4 = removeAllSpaces(item.getAddress().addr4);
//				logger.info("Check address addr3=" + item.getAddress().addr3
//						+ ",addr4=" + item.getAddress().addr4);

				// 물건 주소가 표시목록의 "번지이하"로 끝나는 것만 추가한다.
				if (address.endsWith(addr4)) {
					logger.info("부동산 표시 목록을 물건에 추가합니다.");
					temp.add(item);
				}
			}
		}
		result.addAll(temp);
		return result;
	}

	/**
	 * Gets the results.
	 * 
	 * @return the results
	 */
	public List<부동산표시목록Item> getResults() {
		return res;
	}

	/**
	 * Find node.
	 * 
	 * @param childNodes the child nodes
	 * @param target the target
	 * 
	 * @return the node
	 */
	private Node findNode(NodeList childNodes, String target) {
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node n = childNodes.item(i);
			// System.out.println(n.getNodeName());
			if (n.getNodeName().equals(target)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Gets the node value.
	 * 
	 * @param findNode the find node
	 * 
	 * @return the node value
	 */
	private String getNodeValue(Node findNode) {
		if (findNode.getChildNodes().getLength() < 1) {
			return "";
		}
		return findNode.getChildNodes().item(0).getNodeValue();
	}

	/**
	 * Parses the.
	 * 
	 * @param xml the xml
	 * 
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void parse(String xml) throws ParserConfigurationException,
			SAXException, IOException {
		Document document;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		Node root = document.getChildNodes().item(1);

		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			Node infoNode = root.getChildNodes().item(i);
			if ("부동산정보".equals(infoNode.getNodeName())) {
				String serialNo = infoNode.getAttributes().getNamedItem("일련번호")
						.getNodeValue();
				부동산표시목록주소 address = parseAddress(infoNode);
				String detail = parseDetail(infoNode);
				부동산표시목록Item item = new 부동산표시목록Item(serialNo, address, detail);
//				logger.info("Add item=" + item);
				res.add(item);
			}
		}
	}

	/**
	 * Parses the address.
	 * 
	 * @param infoNode the info node
	 * 
	 * @return the 부동산표시목록주소
	 */
	private 부동산표시목록주소 parseAddress(Node infoNode) {
		NodeList childs = infoNode.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node item = childs.item(i);

			if ("주소".equals(item.getNodeName())) {
				String addr1 = getNodeValue(findNode(item.getChildNodes(), "시도"));
				String addr2 = getNodeValue(findNode(item.getChildNodes(),
						"시군구"));
				String addr3 = getNodeValue(findNode(item.getChildNodes(),
						"읍면동"));
				String addr4 = getNodeValue(findNode(item.getChildNodes(),
						"번지이하"));
				return new 부동산표시목록주소(addr1, addr2, addr3, addr4);
			}
		}
		return null;
	}

	/**
	 * Parses the detail.
	 * 
	 * @param infoNode the info node
	 * 
	 * @return the string
	 */
	private String parseDetail(Node infoNode) {
		NodeList childs = infoNode.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node item = childs.item(i);
			if ("용도구조면적".equals(item.getNodeName())) {
				return getNodeValue(item);// .getChildNodes().item(0).getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Prints the.
	 * 
	 * @param chs the chs
	 */
	private void print(NodeList chs) {
		for (int i = 0; i < chs.getLength(); i++) {
			Node n = chs.item(i);
			System.out.println(n.getNodeName());
		}
	}

	/**
	 * Removes the braced contents.
	 * 
	 * @param address the address
	 * 
	 * @return the string
	 */
	private String removeBracedContents(String address) {
		int startPos = address.indexOf("(");
		if (startPos == -1) {
			return address;
		}
		int endPos = address.indexOf(")");

		return address.substring(0, startPos) + address.substring(endPos + 1);
	}

	/**
	 * Removes the all spaces.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	String removeAllSpaces(String src) {
		return src.replaceAll("\\s+", "");
	}

}
