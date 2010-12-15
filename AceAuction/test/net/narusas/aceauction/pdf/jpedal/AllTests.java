package net.narusas.aceauction.pdf.jpedal;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for net.narusas.aceauction.pdf.jpedal");
		//$JUnit-BEGIN$
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(등기부등본Test.class);
		suite.addTestSuite(JPedalTest.class);
		suite.addTestSuite(사항Test.class);
		//$JUnit-END$
		return suite;
	}

}
