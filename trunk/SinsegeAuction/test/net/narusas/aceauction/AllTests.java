package net.narusas.aceauction;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.narusas.aceauction.fetchers.GGAuctionFetcherTest;
import net.narusas.aceauction.fetchers.XSLTTest;
import net.narusas.aceauction.fetchers.����FetcherTest;
import net.narusas.aceauction.fetchers.�����FetcherTest;
import net.narusas.aceauction.fetchers.�����_�Ű����_����FetcherTest;
import net.narusas.aceauction.fetchers.�����_�Ű����_���FetcherTest;
import net.narusas.aceauction.fetchers.����������򰡼�FetcherTest;
import net.narusas.aceauction.fetchers.������⺻����FetcherTest;
import net.narusas.aceauction.fetchers.��������ϳ���FetcherTest;
import net.narusas.aceauction.fetchers.������Ű����Ǹ���FetcherTest;
import net.narusas.aceauction.fetchers.��������FetcherMergeTest;
import net.narusas.aceauction.fetchers.��������FetcherTest;
import net.narusas.aceauction.fetchers.��������Fetcher_���Test;
import net.narusas.aceauction.fetchers.��������ParserTest;
import net.narusas.aceauction.fetchers.��������ÿܰǹ�ParserTest;
import net.narusas.aceauction.fetchers.�������Ȳ���缭FetcherTest;
import net.narusas.aceauction.fetchers.���ǵ����FetcherTest;
import net.narusas.aceauction.fetchers.���ǵ���ǵ��εFetcherTest;
import net.narusas.aceauction.fetchers.���ǵ���ǹ��Ǹ��FetcherTest;
import net.narusas.aceauction.fetchers.���ǵ���ǹ��ǻ󼼳���FetcherTest;
import net.narusas.aceauction.fetchers.���ǵ���ǻ���FetcherTest;
import net.narusas.aceauction.model.����Test;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.narusas.aceauction");
		//$JUnit-BEGIN$
		suite.addTestSuite(�������Ȳ���缭FetcherTest.class);
		suite.addTestSuite(�����FetcherTest.class);
		suite.addTestSuite(���ǵ����FetcherTest.class);
		suite.addTestSuite(��������ϳ���FetcherTest.class);
		suite.addTestSuite(������Ű����Ǹ���FetcherTest.class);
		suite.addTestSuite(��������ÿܰǹ�ParserTest.class);
		suite.addTestSuite(���ǵ���ǹ��Ǹ��FetcherTest.class);
		suite.addTestSuite(��������ParserTest.class);
		suite.addTestSuite(���ǵ���ǹ��ǻ󼼳���FetcherTest.class);
		suite.addTestSuite(��������Fetcher_���Test.class);
		suite.addTestSuite(��������FetcherTest.class);
		suite.addTestSuite(FileTest.class);
		suite.addTestSuite(FileUploadTest.class);
		suite.addTestSuite(�����_�Ű����_����FetcherTest.class);
		suite.addTestSuite(TempTest.class);
		suite.addTestSuite(GGAuctionFetcherTest.class);
		suite.addTestSuite(�����_�Ű����_���FetcherTest.class);
		suite.addTestSuite(���ǵ���ǻ���FetcherTest.class);
		suite.addTestSuite(����������򰡼�FetcherTest.class);
		suite.addTestSuite(����FetcherTest.class);
		suite.addTestSuite(ShowBrowserTest.class);
		suite.addTestSuite(����Test.class);
		suite.addTestSuite(PropertiesTest.class);
		suite.addTestSuite(������⺻����FetcherTest.class);
		suite.addTestSuite(���ǵ���ǵ��εFetcherTest.class);
		suite.addTestSuite(��������FetcherMergeTest.class);
		suite.addTest(AllTests.suite());
		suite.addTestSuite(RegularExpressTest.class);
		suite.addTestSuite(TableParserTest.class);
		suite.addTestSuite(XSLTTest.class);
		//$JUnit-END$
		return suite;
	}

}
