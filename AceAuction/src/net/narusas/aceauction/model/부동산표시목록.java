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

public class �ε���ǥ�ø�� {

	private LinkedList<�ε���ǥ�ø��Item> res = new LinkedList<�ε���ǥ�ø��Item>();
	Logger logger = Logger.getLogger("log");

	public �ε���ǥ�ø��() {
	}

	public �ε���ǥ�ø��(String xml) throws ParserConfigurationException,
			SAXException, IOException {
//		logger.info(xml);
		parse(xml);
	}

	public List<�ε���ǥ�ø��Item> getItem(���� ����) {

		List<�ε���ǥ�ø��Item> result = new LinkedList<�ε���ǥ�ø��Item>();
		HashSet<�ε���ǥ�ø��Item> temp = new HashSet<�ε���ǥ�ø��Item>();

		List<������Ȳ> statuses = ����.������Ȳ;
		for (������Ȳ status : statuses) {
//			logger.info("getItem " + status.get�ּ�());

			String address = removeAllSpaces(status.get�ּ�());
			address = removeBracedContents(address);

			for (�ε���ǥ�ø��Item item : res) {
				String addr4 = removeAllSpaces(item.getAddress().addr4);
//				logger.info("Check address addr3=" + item.getAddress().addr3
//						+ ",addr4=" + item.getAddress().addr4);
				// ���� �ּҰ� ǥ�ø���� "��������"�� ������ �͸� �߰��Ѵ�.
				if (address.endsWith(addr4)) {
					temp.add(item);
				}
			}
		}
		if (result.size() == 0) {
//			logger.info("getItem " + ����.get�ּ�());
			String address = removeAllSpaces(����.get�ּ�());

			for (�ε���ǥ�ø��Item item : res) {
				String addr4 = removeAllSpaces(item.getAddress().addr4);
//				logger.info("Check address addr3=" + item.getAddress().addr3
//						+ ",addr4=" + item.getAddress().addr4);

				// ���� �ּҰ� ǥ�ø���� "��������"�� ������ �͸� �߰��Ѵ�.
				if (address.endsWith(addr4)) {
					logger.info("�ε��� ǥ�� ����� ���ǿ� �߰��մϴ�.");
					temp.add(item);
				}
			}
		}
		result.addAll(temp);
		return result;
	}

	public List<�ε���ǥ�ø��Item> getResults() {
		return res;
	}

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

	private String getNodeValue(Node findNode) {
		if (findNode.getChildNodes().getLength() < 1) {
			return "";
		}
		return findNode.getChildNodes().item(0).getNodeValue();
	}

	private void parse(String xml) throws ParserConfigurationException,
			SAXException, IOException {
		Document document;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		Node root = document.getChildNodes().item(1);

		for (int i = 0; i < root.getChildNodes().getLength(); i++) {
			Node infoNode = root.getChildNodes().item(i);
			if ("�ε�������".equals(infoNode.getNodeName())) {
				String serialNo = infoNode.getAttributes().getNamedItem("�Ϸù�ȣ")
						.getNodeValue();
				�ε���ǥ�ø���ּ� address = parseAddress(infoNode);
				String detail = parseDetail(infoNode);
				�ε���ǥ�ø��Item item = new �ε���ǥ�ø��Item(serialNo, address, detail);
//				logger.info("Add item=" + item);
				res.add(item);
			}
		}
	}

	private �ε���ǥ�ø���ּ� parseAddress(Node infoNode) {
		NodeList childs = infoNode.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node item = childs.item(i);

			if ("�ּ�".equals(item.getNodeName())) {
				String addr1 = getNodeValue(findNode(item.getChildNodes(), "�õ�"));
				String addr2 = getNodeValue(findNode(item.getChildNodes(),
						"�ñ���"));
				String addr3 = getNodeValue(findNode(item.getChildNodes(),
						"���鵿"));
				String addr4 = getNodeValue(findNode(item.getChildNodes(),
						"��������"));
				return new �ε���ǥ�ø���ּ�(addr1, addr2, addr3, addr4);
			}
		}
		return null;
	}

	private String parseDetail(Node infoNode) {
		NodeList childs = infoNode.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node item = childs.item(i);
			if ("�뵵��������".equals(item.getNodeName())) {
				return getNodeValue(item);// .getChildNodes().item(0).getNodeValue();
			}
		}
		return null;
	}

	private void print(NodeList chs) {
		for (int i = 0; i < chs.getLength(); i++) {
			Node n = chs.item(i);
			System.out.println(n.getNodeName());
		}
	}

	private String removeBracedContents(String address) {
		int startPos = address.indexOf("(");
		if (startPos == -1) {
			return address;
		}
		int endPos = address.indexOf(")");

		return address.substring(0, startPos) + address.substring(endPos + 1);
	}

	String removeAllSpaces(String src) {
		return src.replaceAll("\\s+", "");
	}

}
