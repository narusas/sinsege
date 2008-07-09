package org.jpedal.io;

import java.io.BufferedInputStream;

public class HuffmanTables {

	public int buf;
	public int bufLen;
	public int val;
	public int prefixLen;
	public int rangeLen;
	public int prefix;
	public byte data[]; //Input from JBIG as single value, all methods must be called in PDFfilteredReader.java
	public int position=0;
	BufferedInputStream str;   //Stream *str;
	public static int[] contextSize = { 16, 13, 10, 10 };
	public static int[] refContextSize = { 13, 10 };

	public int jbig2HuffmanLOW = 0xfffffffd;
	public int jbig2HuffmanOOB = 0xfffffffe;
	public int jbig2HuffmanEOT = 0xffffffff;

	HuffmanTables huffTableA[] = null;

	HuffmanTables huffTableB[] = null;

	HuffmanTables huffTableC[] = null;

	HuffmanTables huffTableD[] = null;

	HuffmanTables huffTableE[] = null;

	HuffmanTables huffTableF[] = null;
	
	HuffmanTables huffTableG[] = null;

	HuffmanTables huffTableH[] = null;

	HuffmanTables huffTableI[] = null;

	HuffmanTables huffTableJ[] = null;

	HuffmanTables huffTableK[] = null;

	HuffmanTables huffTableL[] = null;
	
	HuffmanTables huffTableM[] = null;

	HuffmanTables huffTableN[] = null;
	
	HuffmanTables huffTableO[] = null;
	
	HuffmanTables current[] =null;
	/*
	 * JBIG2 Huffman Decoder Code
	 */
	public void setTable(HuffmanTables Table[]){
		current=Table;
	}

	public HuffmanTables[] getTable(){
		return current;
	}


	public HuffmanTables(int tval, int tprefixLen, int trangeLen, int tprefix){
		val = tval;
		prefixLen = tprefixLen;
		rangeLen = trangeLen;
		prefix = tprefix;
	}
	
	public HuffmanTables(){
		HuffmanTables huffTableA[] ={ 	
				new HuffmanTables(	   0, 1,  4,			  0x000),
				new HuffmanTables(	  16, 2,  8,			  0x002),
				new HuffmanTables(	 272, 3, 16,			  0x006),
				new HuffmanTables( 65808, 3, 32,			  0x007),
				new HuffmanTables(	   0, 0, jbig2HuffmanEOT, 0)};

		HuffmanTables huffTableB[] = {
				new HuffmanTables(	   0, 1,  0,              0x000 ),
				new HuffmanTables(	   1, 2,  0,              0x002 ),
				new HuffmanTables(     2, 3,  0,              0x006 ),
				new HuffmanTables(     3, 4,  3,              0x00e ),
				new HuffmanTables(    11, 5,  6,              0x01e ),
				new HuffmanTables(    75, 6, 32,              0x03e ),
				new HuffmanTables(     0, 6, jbig2HuffmanOOB, 0x03f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableC[] = {
				new HuffmanTables(     0, 1,  0,              0x000 ),
				new HuffmanTables(     1, 2,  0,              0x002 ),
				new HuffmanTables(     2, 3,  0,              0x006 ),
				new HuffmanTables(     3, 4,  3,              0x00e ),
				new HuffmanTables(    11, 5,  6,              0x01e ),
				new HuffmanTables(     0, 6, jbig2HuffmanOOB, 0x03e ),
				new HuffmanTables(    75, 7, 32,              0x0fe ),
				new HuffmanTables(  -256, 8,  8,              0x0fe ),
				new HuffmanTables(  -257, 8, jbig2HuffmanLOW, 0x0ff ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableD[] = {
				new HuffmanTables(     1, 1,  0,              0x000 ),
				new HuffmanTables(     2, 2,  0,              0x002 ),
				new HuffmanTables(     3, 3,  0,              0x006 ),
				new HuffmanTables(     4, 4,  3,              0x00e ),
				new HuffmanTables(    12, 5,  6,              0x01e ),
				new HuffmanTables(    76, 5, 32,              0x01f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableE[] = {
				new HuffmanTables(     1, 1,  0,              0x000 ),
				new HuffmanTables(     2, 2,  0,              0x002 ),
				new HuffmanTables(     3, 3,  0,              0x006 ),
				new HuffmanTables(     4, 4,  3,              0x00e ),
				new HuffmanTables(    12, 5,  6,              0x01e ),
				new HuffmanTables(    76, 6, 32,              0x03e ),
				new HuffmanTables(  -255, 7,  8,              0x07e ),
				new HuffmanTables(  -256, 7, jbig2HuffmanLOW, 0x07f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableF[] = {
				new HuffmanTables(     0, 2,  7,              0x000 ),
				new HuffmanTables(   128, 3,  7,              0x002 ),
				new HuffmanTables(   256, 3,  8,              0x003 ),
				new HuffmanTables( -1024, 4,  9,              0x008 ),
				new HuffmanTables(  -512, 4,  8,              0x009 ),
				new HuffmanTables(  -256, 4,  7,              0x00a ),
				new HuffmanTables(   -32, 4,  5,              0x00b ),
				new HuffmanTables(   512, 4,  9,              0x00c ),
				new HuffmanTables(  1024, 4, 10,              0x00d ),
				new HuffmanTables( -2048, 5, 10,              0x01c ),
				new HuffmanTables(  -128, 5,  6,              0x01d ),
				new HuffmanTables(   -64, 5,  5,              0x01e ),
				new HuffmanTables( -2049, 6, jbig2HuffmanLOW, 0x03e ),
				new HuffmanTables(  2048, 6, 32,              0x03f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableG[] = {
				new HuffmanTables(  -512, 3,  8,              0x000 ),
				new HuffmanTables(   256, 3,  8,              0x001 ),
				new HuffmanTables(   512, 3,  9,              0x002 ),
				new HuffmanTables(  1024, 3, 10,              0x003 ),
				new HuffmanTables( -1024, 4,  9,              0x008 ),
				new HuffmanTables(  -256, 4,  7,              0x009 ),
				new HuffmanTables(   -32, 4,  5,              0x00a ),
				new HuffmanTables(     0, 4,  5,              0x00b ),
				new HuffmanTables(   128, 4,  7,              0x00c ),
				new HuffmanTables(  -128, 5,  6,              0x01a ),
				new HuffmanTables(   -64, 5,  5,              0x01b ),
				new HuffmanTables(    32, 5,  5,              0x01c ),
				new HuffmanTables(    64, 5,  6,              0x01d ),
				new HuffmanTables( -1025, 5, jbig2HuffmanLOW, 0x01e ),
				new HuffmanTables(  2048, 5, 32,              0x01f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableH[] = {
				new HuffmanTables(     0, 2,  1,              0x000 ),
				new HuffmanTables(     0, 2, jbig2HuffmanOOB, 0x001 ),
				new HuffmanTables(     4, 3,  4,              0x004 ),
				new HuffmanTables(    -1, 4,  0,              0x00a ),
				new HuffmanTables(    22, 4,  4,              0x00b ),
				new HuffmanTables(    38, 4,  5,              0x00c ),
				new HuffmanTables(     2, 5,  0,              0x01a ),
				new HuffmanTables(    70, 5,  6,              0x01b ),
				new HuffmanTables(   134, 5,  7,              0x01c ),
				new HuffmanTables(     3, 6,  0,              0x03a ),
				new HuffmanTables(    20, 6,  1,              0x03b ),
				new HuffmanTables(   262, 6,  7,              0x03c ),
				new HuffmanTables(   646, 6, 10,              0x03d ),
				new HuffmanTables(    -2, 7,  0,              0x07c ),
				new HuffmanTables(   390, 7,  8,              0x07d ),
				new HuffmanTables(   -15, 8,  3,              0x0fc ),
				new HuffmanTables(    -5, 8,  1,              0x0fd ),
				new HuffmanTables(    -7, 9,  1,              0x1fc ),
				new HuffmanTables(    -3, 9,  0,              0x1fd ),
				new HuffmanTables(   -16, 9, jbig2HuffmanLOW, 0x1fe ),
				new HuffmanTables(  1670, 9, 32,              0x1ff ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableI[] = {
				new HuffmanTables(     0, 2, jbig2HuffmanOOB, 0x000 ),
				new HuffmanTables(    -1, 3,  1,              0x002 ),
				new HuffmanTables(     1, 3,  1,              0x003 ),
				new HuffmanTables(     7, 3,  5,              0x004 ),
				new HuffmanTables(    -3, 4,  1,              0x00a ),
				new HuffmanTables(    43, 4,  5,              0x00b ),
				new HuffmanTables(    75, 4,  6,              0x00c ),
				new HuffmanTables(     3, 5,  1,              0x01a ),
				new HuffmanTables(   139, 5,  7,              0x01b ),
				new HuffmanTables(   267, 5,  8,              0x01c ),
				new HuffmanTables(     5, 6,  1,              0x03a ),
				new HuffmanTables(    39, 6,  2,              0x03b ),
				new HuffmanTables(   523, 6,  8,              0x03c ),
				new HuffmanTables(  1291, 6, 11,              0x03d ),
				new HuffmanTables(    -5, 7,  1,              0x07c ),
				new HuffmanTables(   779, 7,  9,              0x07d ),
				new HuffmanTables(   -31, 8,  4,              0x0fc ),
				new HuffmanTables(   -11, 8,  2,              0x0fd ),
				new HuffmanTables(   -15, 9,  2,              0x1fc ),
				new HuffmanTables(    -7, 9,  1,              0x1fd ),
				new HuffmanTables(   -32, 9, jbig2HuffmanLOW, 0x1fe ),
				new HuffmanTables(  3339, 9, 32,              0x1ff ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableJ[] = {
				new HuffmanTables(    -2, 2,  2,              0x000 ),
				new HuffmanTables(     6, 2,  6,              0x001 ),
				new HuffmanTables(     0, 2, jbig2HuffmanOOB, 0x002 ),
				new HuffmanTables(    -3, 5,  0,              0x018 ),
				new HuffmanTables(     2, 5,  0,              0x019 ),
				new HuffmanTables(    70, 5,  5,              0x01a ),
				new HuffmanTables(     3, 6,  0,              0x036 ),
				new HuffmanTables(   102, 6,  5,              0x037 ),
				new HuffmanTables(   134, 6,  6,              0x038 ),
				new HuffmanTables(   198, 6,  7,              0x039 ),
				new HuffmanTables(   326, 6,  8,              0x03a ),
				new HuffmanTables(   582, 6,  9,              0x03b ),
				new HuffmanTables(  1094, 6, 10,              0x03c ),
				new HuffmanTables(   -21, 7,  4,              0x07a ),
				new HuffmanTables(    -4, 7,  0,              0x07b ),
				new HuffmanTables(     4, 7,  0,              0x07c ),
				new HuffmanTables(  2118, 7, 11,              0x07d ),
				new HuffmanTables(    -5, 8,  0,              0x0fc ),
				new HuffmanTables(     5, 8,  0,              0x0fd ),
				new HuffmanTables(   -22, 8, jbig2HuffmanLOW, 0x0fe ),
				new HuffmanTables(  4166, 8, 32,              0x0ff ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableK[] = {
				new HuffmanTables(     1, 1,  0,              0x000 ),
				new HuffmanTables(     2, 2,  1,              0x002 ),
				new HuffmanTables(     4, 4,  0,              0x00c ),
				new HuffmanTables(     5, 4,  1,              0x00d ),
				new HuffmanTables(     7, 5,  1,              0x01c ),
				new HuffmanTables(     9, 5,  2,              0x01d ),
				new HuffmanTables(    13, 6,  2,              0x03c ),
				new HuffmanTables(    17, 7,  2,              0x07a ),
				new HuffmanTables(    21, 7,  3,              0x07b ),
				new HuffmanTables(    29, 7,  4,              0x07c ),
				new HuffmanTables(    45, 7,  5,              0x07d ),
				new HuffmanTables(    77, 7,  6,              0x07e ),
				new HuffmanTables(   141, 7, 32,              0x07f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableL[] = {
				new HuffmanTables(     1, 1,  0,              0x000 ),
				new HuffmanTables(     2, 2,  0,              0x002 ),
				new HuffmanTables(     3, 3,  1,              0x006 ),
				new HuffmanTables(     5, 5,  0,              0x01c ),
				new HuffmanTables(     6, 5,  1,              0x01d ),
				new HuffmanTables(     8, 6,  1,              0x03c ),
				new HuffmanTables(    10, 7,  0,              0x07a ),
				new HuffmanTables(    11, 7,  1,              0x07b ),
				new HuffmanTables(    13, 7,  2,              0x07c ),
				new HuffmanTables(    17, 7,  3,              0x07d ),
				new HuffmanTables(    25, 7,  4,              0x07e ),
				new HuffmanTables(    41, 8,  5,              0x0fe ),
				new HuffmanTables(    73, 8, 32,              0x0ff ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableM[] = {
				new HuffmanTables(     1, 1,  0,              0x000 ),
				new HuffmanTables(     2, 3,  0,              0x004 ),
				new HuffmanTables(     7, 3,  3,              0x005 ),
				new HuffmanTables(     3, 4,  0,              0x00c ),
				new HuffmanTables(     5, 4,  1,              0x00d ),
				new HuffmanTables(     4, 5,  0,              0x01c ),
				new HuffmanTables(    15, 6,  1,              0x03a ),
				new HuffmanTables(    17, 6,  2,              0x03b ),
				new HuffmanTables(    21, 6,  3,              0x03c ),
				new HuffmanTables(    29, 6,  4,              0x03d ),
				new HuffmanTables(    45, 6,  5,              0x03e ),
				new HuffmanTables(    77, 7,  6,              0x07e ),
				new HuffmanTables(   141, 7, 32,              0x07f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableN[] = {
				new HuffmanTables(     0, 1,  0,              0x000 ),
				new HuffmanTables(    -2, 3,  0,              0x004 ),
				new HuffmanTables(    -1, 3,  0,              0x005 ),
				new HuffmanTables(     1, 3,  0,              0x006 ),
				new HuffmanTables(     2, 3,  0,              0x007 ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};

		HuffmanTables huffTableO[] = {
				new HuffmanTables(     0, 1,  0,              0x000 ),
				new HuffmanTables(    -1, 3,  0,              0x004 ),
				new HuffmanTables(     1, 3,  0,              0x005 ),
				new HuffmanTables(    -2, 4,  0,              0x00c ),
				new HuffmanTables(     2, 4,  0,              0x00d ),
				new HuffmanTables(    -4, 5,  1,              0x01c ),
				new HuffmanTables(     3, 5,  1,              0x01d ),
				new HuffmanTables(    -8, 6,  2,              0x03c ),
				new HuffmanTables(     5, 6,  2,              0x03d ),
				new HuffmanTables(   -24, 7,  4,              0x07c ),
				new HuffmanTables(     9, 7,  4,              0x07d ),
				new HuffmanTables(   -25, 7, jbig2HuffmanLOW, 0x07e ),
				new HuffmanTables(    25, 7, 32,              0x07f ),
				new HuffmanTables(     0, 0, jbig2HuffmanEOT, 0     )
		};
		HuffmanTables current[] =null;
	}

	public void setTable(){

	}

	public int getVal(){
		return val;
	}
	public int getprefixLen(){
		return prefixLen;
	}
	public int getrangeLen(){
		return rangeLen;
	}
	public int getprefix(){
		return prefix;
	}

	public void setData(byte d[]){
		data=d;
	}

	public boolean decodeInt(int x, HuffmanTables table[]){

		int i=0;
		int len =0;
		int prefix =0;

		while(table[i].getrangeLen() != jbig2HuffmanEOT){
			while(len < table[i].getprefixLen()){
				prefix = (prefix << 1) | readBit();
				len++;
			}
			if (prefix == table[i].prefix) {
				if (table[i].rangeLen == jbig2HuffmanOOB) {
					return false;
				}
				if (table[i].rangeLen == jbig2HuffmanLOW) {
					x = table[i].val - readBits(32);
				} else if (table[i].rangeLen > 0) {
					x = table[i].val + readBits(table[i].rangeLen);
				} else {
					x = table[i].val;
				}
				return true;
			}
			i++;
		}

		return false;
	}

	public int readBit(){
		if (bufLen == 0) {
			buf = data[position++];
			bufLen = 8;
		}
		bufLen--;
		return (buf >> bufLen) & 1;
	}

	public int readBits(int n){
		int x, mask, nLeft;

		mask = (n == 32) ? 0xffffffff : ((1 << n) - 1);
		if (bufLen >= n) {
			x = (buf >> (bufLen - n)) & mask;
			bufLen -= n;
		} else {
			x = buf & ((1 << bufLen) - 1);
			nLeft = n - bufLen;
			bufLen = 0;
			while (nLeft >= 8) {
				x = (x << 8) | (data[position++] & 0xff);
				nLeft -= 8;
			}
			if (nLeft > 0) {
				buf = data[position++];
				bufLen = 8 - nLeft;
				x = (x << nLeft) | ((buf >> bufLen) & ((1 << nLeft) - 1));
			}
		}
		return x;
	}

	public void buildTable(HuffmanTables table[], int len){
		int i, j, k, prefix;
		HuffmanTables tab;

		for (i = 0; i < len; ++i) {
			for (j = i; j < len && table[j].prefixLen == 0; ++j) ;
			if (j == len) {
				break;
			}
			for (k = j + 1; k < len; ++k) {
				if (table[k].prefixLen > 0 &&
						table[k].prefixLen < table[j].prefixLen) {
					j = k;
				}
			}
			if (j != i) {
				tab = table[j];
				for (k = j; k > i; --k) {
					table[k] = table[k - 1];
				}
				table[i] = tab;
			}
		}
		table[i] = table[len];

		// assign prefixes
		i = 0;
		prefix = 0;
		table[i++].prefix = prefix++;
		for (; table[i].rangeLen != jbig2HuffmanEOT; ++i) {
			prefix <<= table[i].prefixLen - table[i-1].prefixLen;
			table[i].prefix = prefix++;
		}

	}

	public void Huffreset(){
		buf=0;
		bufLen=0;
	}

	public void setStream(BufferedInputStream str){
		System.out.println("TRACER :: HuffmanTable.setStream");
		this.str = str;
	}
	
	public void reset(){
		
	}
}
