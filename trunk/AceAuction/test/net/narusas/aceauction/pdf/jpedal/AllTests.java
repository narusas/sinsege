package net.narusas.aceauction.pdf.jpedal;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for net.narusas.aceauction.pdf.jpedal");
		//$JUnit-BEGIN$
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(���εTest.class);
		suite.addTestSuite(JPedalTest.class);
		suite.addTestSuite(����Test.class);
		//$JUnit-END$
		return suite;
	}

}
