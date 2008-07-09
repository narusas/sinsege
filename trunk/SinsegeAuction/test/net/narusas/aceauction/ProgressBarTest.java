package net.narusas.aceauction;

import java.util.logging.Level;

import junit.framework.TestCase;

public class ProgressBarTest extends TestCase {
	public void testLevelValue() {
		assertTrue(Level.FINE.intValue() > Level.FINEST.intValue());
	}
}
