package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.�ε���ǥ�ø��;
import net.narusas.aceauction.model.�ε���ǥ�ø��Item;
import net.narusas.util.lang.NFile;

import org.xml.sax.SAXException;

public class �ε���ǥ�ø��Test extends TestCase {
	public void test1() throws IOException, ParserConfigurationException,
			SAXException {
		String xml = NFile.getText(new File("fixtures/�ε���ǥ�ø��.xml"));

		�ε���ǥ�ø�� item = new �ε���ǥ�ø��(xml);
		List<�ε���ǥ�ø��Item> res = item.getResults();
		assertEquals(3, res.size());
	}

}
