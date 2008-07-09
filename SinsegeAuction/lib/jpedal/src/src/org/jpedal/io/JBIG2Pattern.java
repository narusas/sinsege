package org.jpedal.io;

public class JBIG2Pattern  implements JBIG2Segment{

	int size;
	JBIG2Bitmap bitmaps[];
	int SegNum;


	public JBIG2Pattern(int segNumA, int sizeA)
	{
		size = sizeA;
		bitmaps[0] = new JBIG2Bitmap(0,0,0);
		SegNum = segNumA;
	}

	public JBIG2Pattern() {
		int i;

		for (i = 0; i < size; ++i) {
			bitmaps[i]=null;
		}
	}

	//JBIG2SegmentType getType() { return jbig2SegPatternDict; }
	public int getSize() { return size; }
	void setBitmap(int idx, JBIG2Bitmap bitmap) { bitmaps[idx] = bitmap; }
	JBIG2Bitmap getBitmap(int idx) { return bitmaps[idx]; }
	
    int type=3;
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
