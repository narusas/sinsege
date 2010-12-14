package org.jpedal.io;

public class JBIG2CodeTable  implements JBIG2Segment{

	HuffmanTables table;
	int segNum;
	
	//JBIG2SegmentType getType() { return jbig2SegCodeTable; }
	public HuffmanTables getHuffTable() { return table; }
	
	public JBIG2CodeTable(int segNumA, HuffmanTables tableA)
		{
		  table = tableA;
		  segNum = segNumA;
		}

		public void JBIG2CodeTable() {
		  table=null;
		}

	    int type=2;
		public int getType() {
			// TODO Auto-generated method stub
			return type;
		}

		public void setType(int type) {
			// TODO Auto-generated method stub
			this.type=type;
		}

		public int getSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getSegNum() {
			// TODO Auto-generated method stub
			return segNum;
		}

}
