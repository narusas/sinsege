package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.부동산표시목록;
import net.narusas.aceauction.model.부동산표시목록Item;
import net.narusas.util.lang.NFile;

import org.xml.sax.SAXException;

public class 부동산표시목록Test extends TestCase {
	public void test1() throws IOException, ParserConfigurationException,
			SAXException {
		String xml = NFile.getText(new File("fixtures/부동산표시목록.xml"));

		부동산표시목록 item = new 부동산표시목록(xml);
		List<부동산표시목록Item> res = item.getResults();
		assertEquals(3, res.size());
	}

}
