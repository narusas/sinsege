package org.jpedal.io;

public class ArithmeticDecoderStats {

	char[] cxTab;
	int contextSize;
	
	public ArithmeticDecoderStats(int contextSizeA) {
	  contextSize = contextSizeA;
	  cxTab = new char[contextSize];
	  reset();
	}

	public ArithmeticDecoderStats() {
	  cxTab=null;
	}

	public ArithmeticDecoderStats copy() {
	  ArithmeticDecoderStats stats;

	  stats = new ArithmeticDecoderStats(contextSize);
	  int i=0;
	  while(i<contextSize){
		  stats.cxTab[i]=cxTab[i];
	  }
	  return stats;
	}

	public void reset() {
	 // memset(cxTab, 0, contextSize);
		int i=0;
		System.out.println("reset called");
		char[] temp = new char[contextSize+cxTab.length];
		System.out.println("contextSize=="+contextSize+"<");
		while(i!=contextSize){
			System.out.println("i=="+i+"<");
			temp[i] = 0;
			i++;
		}
		System.out.println("cxTab.length=="+cxTab.length+"<");
		i=0;
		while(i!=cxTab.length){
			System.out.println("i=="+i+"<");
			temp[i] = cxTab[i];
			i++;
		}
		cxTab = temp;
		System.out.println("reset finished");
	}

	public void copyFrom(ArithmeticDecoderStats stats) {
		stats = new ArithmeticDecoderStats(contextSize);
		  int i=0;
		  while(i<contextSize){
			  stats.cxTab[i]=cxTab[i];
		  }
	}

	public void setEntry(int cx, int i, int mps) {
	  cxTab[cx] = (char) ((i << 1) + mps);
	}

	public int getContextSize() {
		return contextSize;
	}
}