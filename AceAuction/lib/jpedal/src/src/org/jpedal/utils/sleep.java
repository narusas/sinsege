/**
* ===========================================
* Storypad
* ===========================================
*
* Project Info:  http://www.idrsolutions.com
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
*
* ---------------
* sleep.java.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.utils;

/**
 * provide a delay
 */
public class sleep extends Thread
{
	
	/**
	 *The time the program sleeps (in seconds) before repolling incoming dir
	 */
	private int delay = 5;
	public void destroy()
	{
		this.destroy();
		this.stop();
	}
	
	/**
	 * pass through value of sleep in seconds
	 */
	public sleep( int delay ) 
	{
		this.delay = delay;
		try
		{
			yield();
			sleep( delay);
		}
		catch( Exception e )
		{e.printStackTrace();}
	}
	
}