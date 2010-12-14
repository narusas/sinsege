/*
 * Created on 30-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jpedal.function;

import java.io.ByteArrayInputStream;

/**
 * @author markee
 *
 * Add unread to B|yteArrayInputStream
 */
public class CustomByteArrayInputStream extends ByteArrayInputStream {

	private int lastChar=65536;
	
	
	public CustomByteArrayInputStream(byte[] buf, int offset, int length) {
		super(buf, offset, length);
		
	}
	
	public CustomByteArrayInputStream(byte[] buf) {
		super(buf);
		
	}
	
	public void unread(int c){
		lastChar=c;
		//System.out.println("unread="+lastChar);
	}

	public int read(){
		if(lastChar!=65536){
			//System.out.println("lastChar="+lastChar);
			int next=lastChar;
			lastChar=65536;
			return next;
		}else 
			return super.read();
	}
}
