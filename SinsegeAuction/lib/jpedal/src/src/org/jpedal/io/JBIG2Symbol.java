package org.jpedal.io;

public class JBIG2Symbol  implements JBIG2Segment{
	int size;
	JBIG2Bitmap bitmaps[];
	int SegNum;
	ArithmeticDecoderStats genericRegionStats = null;
	ArithmeticDecoderStats refinementRegionStats = null;
	//JBIG2SegmentType getType() { return jbig2SegSymbolDict; }
	public int getSize() { return size; }
	void setBitmap(int idx, JBIG2Bitmap bitmap) { bitmaps[idx] = bitmap; }
	JBIG2Bitmap getBitmap(int idx) { return bitmaps[idx]; }
	void setGenericRegionStats(ArithmeticDecoderStats stats)
	{ genericRegionStats = stats; }
	void setRefinementRegionStats(ArithmeticDecoderStats stats)
	{ refinementRegionStats = stats; }
	ArithmeticDecoderStats getGenericRegionStats()
	{ return genericRegionStats; }
	ArithmeticDecoderStats getRefinementRegionStats()
	{ return refinementRegionStats; }

	public JBIG2Symbol(int segNumA, int sizeA)
	{
		size = sizeA;
		bitmaps[0] = new JBIG2Bitmap(0,0,0);

		genericRegionStats = null;
		refinementRegionStats = null;

		SegNum = segNumA;
	}

	public JBIG2Symbol() {
		int i;

		for (i = 0; i < size; ++i) {
			bitmaps[i]=null;
		}


		if(genericRegionStats!=null)
			genericRegionStats=null;

		if(refinementRegionStats!=null)
			refinementRegionStats=null;

	}

	int type=1;
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public void setType(int type) {
		// TODO Auto-generated method stub
		this.type=type;
	}
	public int getSegNum() {
		// TODO Auto-generated method stub
		return SegNum;
	}
}
