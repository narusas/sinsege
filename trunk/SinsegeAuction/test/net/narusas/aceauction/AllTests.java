package net.narusas.aceauction;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.narusas.aceauction.fetchers.GGAuctionFetcherTest;
import net.narusas.aceauction.fetchers.XSLTTest;
import net.narusas.aceauction.fetchers.담당계FetcherTest;
import net.narusas.aceauction.fetchers.대법원FetcherTest;
import net.narusas.aceauction.fetchers.대법원_매각결과_담당계FetcherTest;
import net.narusas.aceauction.fetchers.대법원_매각결과_사건FetcherTest;
import net.narusas.aceauction.fetchers.대법원감정평가서FetcherTest;
import net.narusas.aceauction.fetchers.대법원기본내역FetcherTest;
import net.narusas.aceauction.fetchers.대법원기일내역FetcherTest;
import net.narusas.aceauction.fetchers.대법원매각물건명세서FetcherTest;
import net.narusas.aceauction.fetchers.대법원사건FetcherMergeTest;
import net.narusas.aceauction.fetchers.대법원사건FetcherTest;
import net.narusas.aceauction.fetchers.대법원사건Fetcher_결과Test;
import net.narusas.aceauction.fetchers.대법원사건ParserTest;
import net.narusas.aceauction.fetchers.대법원제시외건물ParserTest;
import net.narusas.aceauction.fetchers.대법원현황조사서FetcherTest;
import net.narusas.aceauction.fetchers.스피드옥션FetcherTest;
import net.narusas.aceauction.fetchers.스피드옥션등기부등본FetcherTest;
import net.narusas.aceauction.fetchers.스피드옥션물건목록FetcherTest;
import net.narusas.aceauction.fetchers.스피드옥션물건상세내용FetcherTest;
import net.narusas.aceauction.fetchers.스피드옥션사진FetcherTest;
import net.narusas.aceauction.model.담당계Test;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.narusas.aceauction");
		//$JUnit-BEGIN$
		suite.addTestSuite(대법원현황조사서FetcherTest.class);
		suite.addTestSuite(대법원FetcherTest.class);
		suite.addTestSuite(스피드옥션FetcherTest.class);
		suite.addTestSuite(대법원기일내역FetcherTest.class);
		suite.addTestSuite(대법원매각물건명세서FetcherTest.class);
		suite.addTestSuite(대법원제시외건물ParserTest.class);
		suite.addTestSuite(스피드옥션물건목록FetcherTest.class);
		suite.addTestSuite(대법원사건ParserTest.class);
		suite.addTestSuite(스피드옥션물건상세내용FetcherTest.class);
		suite.addTestSuite(대법원사건Fetcher_결과Test.class);
		suite.addTestSuite(대법원사건FetcherTest.class);
		suite.addTestSuite(FileTest.class);
		suite.addTestSuite(FileUploadTest.class);
		suite.addTestSuite(대법원_매각결과_담당계FetcherTest.class);
		suite.addTestSuite(TempTest.class);
		suite.addTestSuite(GGAuctionFetcherTest.class);
		suite.addTestSuite(대법원_매각결과_사건FetcherTest.class);
		suite.addTestSuite(스피드옥션사진FetcherTest.class);
		suite.addTestSuite(대법원감정평가서FetcherTest.class);
		suite.addTestSuite(담당계FetcherTest.class);
		suite.addTestSuite(ShowBrowserTest.class);
		suite.addTestSuite(담당계Test.class);
		suite.addTestSuite(PropertiesTest.class);
		suite.addTestSuite(대법원기본내역FetcherTest.class);
		suite.addTestSuite(스피드옥션등기부등본FetcherTest.class);
		suite.addTestSuite(대법원사건FetcherMergeTest.class);
		suite.addTest(AllTests.suite());
		suite.addTestSuite(RegularExpressTest.class);
		suite.addTestSuite(TableParserTest.class);
		suite.addTestSuite(XSLTTest.class);
		//$JUnit-END$
		return suite;
	}

}
