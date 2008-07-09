package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;

public class JBIGStreamDecoder {
//	------------------------------------------------------------------------
//	JBIGStreamDecoder
//	------------------------------------------------------------------------
	JBIG2Bitmap pageBitmap = null;
	
	int jbig2SegSymbolDict = 1;
	int jbig2SegCodeTable = 2;
	int jbig2SegPatternDict = 3;
	int jbig2SegBitmap = 4;
	int EOF = -1;
	int pageW = 0;
	int pageH = 0;
	int curPageW = 0;
	int curPageH = 0;
	int pageDefPixel = 0;
	int defCombOp = 0;
	
	ArithmeticDecoder arithDecoder = new ArithmeticDecoder();
	ArithmeticDecoderStats genericRegionStats = new ArithmeticDecoderStats(1 << 1);
	ArithmeticDecoderStats refinementRegionStats = new ArithmeticDecoderStats(1 << 1);
	ArithmeticDecoderStats iadhStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iadwStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iaexStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iaaiStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iadtStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iaitStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iafsStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iadsStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iardxStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iardyStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iardwStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iardhStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iariStats = new ArithmeticDecoderStats(1 << 9);
	ArithmeticDecoderStats iaidStats = new ArithmeticDecoderStats(1 << 1);
	HuffmanTables huffDecoder = new HuffmanTables();
	MMRDecoder mmrDecoder = new MMRDecoder(); 
	
	int segNum =0, segFlags =0, segType =0, page =0, segLength =0;
	int refFlags =0, nRefSegs =0;
	int[] refSegs =null;
	int c1 =0, c2 =0, c3 =0;
	int i =0;

	//segments = globalSegments = new GList();
	Vector segments = new Vector();
	Vector globalSegments = new Vector();
	BufferedInputStream curStr;
	BufferedInputStream str;
	int dataPtr=0,dataEnd=0;
	public JBIGStreamDecoder(BufferedInputStream strA, byte[] data) throws IOException
	{	
		System.out.println("START TRACER");
		curStr = strA;
		System.out.println("BEGIN TRACER");
		
		arithDecoder.setStream(curStr);
		huffDecoder.setStream(curStr);
		mmrDecoder.setStream(curStr);
		readSegments();
		
		segments = null;
		curStr = null;
	}

	public JBIGStreamDecoder() {
		arithDecoder=null;
		genericRegionStats=null;
		refinementRegionStats=null;
		iadhStats=null;
		iadwStats=null;
		iaexStats=null;
		iaaiStats=null;
		iadtStats=null;
		iaitStats=null;
		iafsStats=null;
		iadsStats=null;
		iardxStats=null;
		iardyStats=null;
		iardwStats=null;
		iardhStats=null;
		iariStats=null;
		iaidStats=null;
		huffDecoder=null;
		mmrDecoder=null;
		if (pageBitmap!=null) {
			pageBitmap=null;
		}
		if (segments!=null) {
			segments=null;
		}
		if (globalSegments!=null) {
			globalSegments=null;
		}
		curStr=null;
	}

	public void reset() throws IOException {
		if (pageBitmap!=null) {
			pageBitmap = null;
		}
		if (segments!=null) {
			segments=null;
		}
		segments = new Vector();

		curStr = str;
		curStr.reset();
		arithDecoder.setStream(curStr);
		huffDecoder.setStream(curStr);
		mmrDecoder.setStream(curStr);
		readSegments();

		if (pageBitmap!=null) {
			//dataPtr = pageBitmap.getDataPtr();
			//dataEnd = dataPtr + pageBitmap.getDataSize();
		} else {
			//dataPtr = null;
		}
	}

	public int read() {
		if (dataPtr!=-1 && dataPtr < dataEnd) {
			return (dataPtr++ ^ 0xff) & 0xff;
		}
		return EOF;
	}

	public int lookChar() {
		if (dataPtr!=-1 && dataPtr < dataEnd) {
			return (dataPtr ^ 0xff) & 0xff;
		}
		return EOF;
	}

	public String getPSFilter(int psLevel, char[] indent) {
		return null;
	}

	//boolean isBinary(boolean last) {
	//	return str.isBinary(true);
	//}

	public void readSegments() throws IOException {
		System.out.println("TRACER :: READ SEGEMENT");
		
		System.out.println("START TRACER");
		while (readULong(segNum)) {
			System.out.println("TRACER");
			// segment header flags
			if (!readUByte(segFlags)) {
				System.out.println("FAILED1");
			}
			segType = segFlags & 0x3f;

			// referred-to segment count and retention flags
			if (!readUByte(refFlags)) {
				System.out.println("FAILED1");
			}
			nRefSegs = refFlags >> 5;
			if (nRefSegs == 7) {
				if ((c1 = curStr.read()) == EOF ||
						(c2 = curStr.read()) == EOF ||
						(c3 = curStr.read()) == EOF) {
					System.out.println("FAILED1");
				}
				refFlags = (refFlags << 24) | (c1 << 16) | (c2 << 8) | c3;
				nRefSegs = refFlags & 0x1fffffff;
				for (i = 0; i < (nRefSegs + 9) >> 3; ++i) {
					c1 = curStr.read();
				}
			}
			
			
			// referred-to segment numbers
			refSegs = new int[nRefSegs];
			if (segNum <= 256) {
				for (i = 0; i < nRefSegs; ++i) {
					if (!readUByte(refSegs[i])) {
						System.out.println("FAILED2");
					}
				}
			} else if (segNum <= 65536) {
				for (i = 0; i < nRefSegs; ++i) {
					if (!readUWord(refSegs[i])) {
						System.out.println("FAILED2");
					}
				}
			} else {
				for (i = 0; i < nRefSegs; ++i) {
					if (!readULong(refSegs[i])) {
						System.out.println("FAILED2");
					}
				}
			}
			
			// segment page association
			if ((segFlags & 0x40)!=-1) {
				if (!readULong(page)) {
					System.out.println("FAILED2");
				}
			} else {
				if (!readUByte(page)) {
					System.out.println("FAILED2");
				}
			}
			
			// segment data length
			if (!readULong(segLength)) {
				System.out.println("FAILED2");
			}

			// read the segment data
			switch (segType) {
			case 0:
				if (!readSymbolDictSeg(segNum, segLength, refSegs, nRefSegs)) {
					System.out.println("SYNTAX ERROR");
				}
				break;
			case 4:
				readTextRegionSeg(segNum, false, false, segLength, refSegs, nRefSegs);
				break;
			case 6:
				readTextRegionSeg(segNum, true, false, segLength, refSegs, nRefSegs);
				break;
			case 7:
				readTextRegionSeg(segNum, true, true, segLength, refSegs, nRefSegs);
				break;
			case 16:
				readPatternDictSeg(segNum, segLength);
				break;
			case 20:
				readHalftoneRegionSeg(segNum, false, false, segLength,
						refSegs, nRefSegs);
				break;
			case 22:
				readHalftoneRegionSeg(segNum, true, false, segLength,
						refSegs, nRefSegs);
				break;
			case 23:
				readHalftoneRegionSeg(segNum, true, true, segLength,
						refSegs, nRefSegs);
				break;
			case 36:
				readGenericRegionSeg(segNum, false, false, segLength);
				break;
			case 38:
				readGenericRegionSeg(segNum, true, false, segLength);
				break;
			case 39:
				readGenericRegionSeg(segNum, true, true, segLength);
				break;
			case 40:
				readGenericRefinementRegionSeg(segNum, false, false, segLength,
						refSegs, nRefSegs);
				break;
			case 42:
				readGenericRefinementRegionSeg(segNum, true, false, segLength,
						refSegs, nRefSegs);
				break;
			case 43:
				readGenericRefinementRegionSeg(segNum, true, true, segLength,
						refSegs, nRefSegs);
				break;
			case 48:
				readPageInfoSeg(segLength);
				break;
			case 50:
				readEndOfStripeSeg(segLength);
				break;
			case 52:
				readProfilesSeg(segLength);
				break;
			case 53:
				readCodeTableSeg(segNum, segLength);
				break;
			case 62:
				readExtensionSeg(segLength);
				break;
			default:
				System.out.println("Unknown segment type in JBIG2 stream");
			for (i = 0; i < segLength; ++i) {
				if ((c1 = curStr.read()) == EOF) {
					System.out.println("FAILED2");
				}
			}
			break;
			}

			refSegs=null;

			System.out.println("TRACER :: END LOOP");
		}
		System.out.println("TRACER :: END READ SEGEMENT");
		return;
	}

	public boolean readSymbolDictSeg(int segNum, int length,
			int[] refSegs, int nRefSegs) throws IOException {
		JBIG2Symbol symbolDict = new JBIG2Symbol();
		HuffmanTables huffDHTable=new HuffmanTables(), huffDWTable=new HuffmanTables();
		HuffmanTables huffBMSizeTable=new HuffmanTables(), huffAggInstTable=new HuffmanTables();
		JBIG2Segment seg = null;
		Vector codeTables = new Vector();
		JBIG2Symbol inputSymbolDict = new JBIG2Symbol();
		int flags=0, sdTemplate=0, sdrTemplate=0, huff=0, refAgg=0;
		int huffDH=0, huffDW=0, huffBMSize=0, huffAggInst=0;
		int contextUsed=0, contextRetained=0;
		int[] sdATX, sdATY, sdrATX, sdrATY;
		sdATX = new int[4];
		sdATY = new int[4];
		sdrATX = new int[2];
		sdrATY = new int[2];
		int numExSyms=0, numNewSyms=0, numInputSyms=0, symCodeLen=0;
		JBIG2Bitmap[] bitmaps = null;
		JBIG2Bitmap collBitmap = null, refBitmap = null;
		int[] symWidths = null;
		int symHeight=0, symWidth=0, totalWidth=0, x=0, symID=0;
		int dh=0, dw=0, refAggNum=0, refDX=0, refDY=0, bmSize=0;
		boolean ex = true;
		int run=0, cnt=0;
		int i=0, j=0, k=0;
		char[] p = null;

		// symbol dictionary flags
		if (!readUWord(flags)) {
			System.out.println("Unexpected EOF");
		}
		sdTemplate = (flags >> 10) & 3;
		sdrTemplate = (flags >> 12) & 1;
		huff = flags & 1;
		refAgg = (flags >> 1) & 1;
		huffDH = (flags >> 2) & 3;
		huffDW = (flags >> 4) & 3;
		huffBMSize = (flags >> 6) & 1;
		huffAggInst = (flags >> 7) & 1;
		contextUsed = (flags >> 8) & 1;
		contextRetained = (flags >> 9) & 1;

		// symbol dictionary AT flags
		if (huff!=-1) {
			if (sdTemplate == 0) {
				if (!readByte(sdATX[0]) ||
						!readByte(sdATY[0]) ||
						!readByte(sdATX[1]) ||
						!readByte(sdATY[1]) ||
						!readByte(sdATX[2]) ||
						!readByte(sdATY[2]) ||
						!readByte(sdATX[3]) ||
						!readByte(sdATY[3])) {
					System.out.println("Unexpected EOF");
				}
			} else {
				if (!readByte(sdATX[0]) ||
						!readByte(sdATY[0])) {
					System.out.println("Unexpected EOF");
				}
			}
		}

		// symbol dictionary refinement AT flags
		if (refAgg!=-1 && sdrTemplate!=-1) {
			if (!readByte(sdrATX[0]) ||
					!readByte(sdrATY[0]) ||
					!readByte(sdrATX[1]) ||
					!readByte(sdrATY[1])) {
				System.out.println("Unexpected EOF");
			}
		}

		// SDNUMEXSYMS and SDNUMNEWSYMS
		if (!readULong(numExSyms) || !readULong(numNewSyms)) {
			System.out.println("Unexpected EOF");
		}

		// get referenced segments: input symbol dictionaries and code tables
		codeTables = new Vector(9);
		numInputSyms = 0;
		for (i = 0; i < nRefSegs; ++i) {
			seg = findSegment(refSegs[i]);
			if (seg.getType() == jbig2SegSymbolDict) {
				numInputSyms += inputSymbolDict.getSize();
			} else if (seg.getType() == jbig2SegCodeTable) {
				codeTables.add(seg);
			}
		}

		// compute symbol code length
		symCodeLen = 0;
		i = 1;
		while (i < numInputSyms + numNewSyms) {
			++symCodeLen;
			i <<= 1;
		}

		// get the input symbol bitmaps
		bitmaps = new JBIG2Bitmap[numInputSyms + numNewSyms];
		for (i = 0; i < numInputSyms + numNewSyms; ++i) {
			bitmaps[i] = null;
		}
		k = 0;
		inputSymbolDict = null;
		for (i = 0; i < nRefSegs; ++i) {
			seg = findSegment(refSegs[i]);
			if (seg.getType() == jbig2SegSymbolDict) {
				inputSymbolDict = (JBIG2Symbol)seg;
				for (j = 0; j < inputSymbolDict.getSize(); ++j) {
					bitmaps[k++] = inputSymbolDict.getBitmap(j);
				}
			}
		}

		// get the Huffman tables
		huffDHTable = huffDWTable = null; // make gcc happy
		huffBMSizeTable = huffAggInstTable = null; // make gcc happy
		i = 0;

		if (huff!=-1) {
			if (huffDH == 0) {
				huffDHTable.setTable(huffDHTable.huffTableD);
			} else if (huffDH == 1) {
				huffDHTable.setTable(huffDHTable.huffTableE);
			} else {
				huffDHTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffDW == 0) {
				huffDWTable.setTable(huffDHTable.huffTableB);
			} else if (huffDW == 1) {
				huffDWTable.setTable(huffDHTable.huffTableC);
			} else {
				huffDWTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffBMSize == 0) {
				huffBMSizeTable.setTable(huffDHTable.huffTableA);
			} else {
				huffBMSizeTable =
					((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffAggInst == 0) {
				huffAggInstTable.setTable(huffDHTable.huffTableA);
			} else {
				huffAggInstTable =
					((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
		}
		codeTables = null;

		// set up the Huffman decoder
		if (huff!=-1) {
			huffDecoder=null;

			// set up the arithmetic decoder
		} else {
			if (contextUsed!=-1 && inputSymbolDict!=null) {
				resetGenericStats(sdTemplate, inputSymbolDict.getGenericRegionStats());
			} else {
				resetGenericStats(sdTemplate, null);
			}
			resetIntStats(symCodeLen);
			arithDecoder.start();
		}

		// set up the arithmetic decoder for refinement/aggregation
		if (refAgg!=-1) {
			if (contextUsed!=-1 && inputSymbolDict!=null) {
				resetRefinementStats(sdrTemplate,
						inputSymbolDict.getRefinementRegionStats());
			} else {
				resetRefinementStats(sdrTemplate, null);
			}
		}

		// allocate symbol widths storage
		symWidths = null;
		if (huff!=-1 && refAgg==-1) {
			symWidths = new int[numNewSyms];
		}

		symHeight = 0;
		i = 0;
		while (i < numNewSyms) {

			// read the height class delta height
			if (huff!=-1) {
				huffDecoder.decodeInt(dh, huffDHTable.getTable());
			} else {
				arithDecoder.decodeInt(dh, iadhStats);
			}
			if (dh < 0 && (int)-dh >= symHeight)
				System.out.println("Bad delta-height value in JBIG2 symbol dictionary");

			symHeight += dh;
			symWidth = 0;
			totalWidth = 0;
			j = i;

			// read the symbols in this height class
			while (true) {

				// read the delta width
				if (huff!=-1) {
					if (!huffDecoder.decodeInt(dw, huffDWTable.getTable())) {
						break;
					}
				} else {
					if (!arithDecoder.decodeInt(dw, iadwStats)) {
						break;
					}
				}
				if (dw < 0 && (int)-dw >= symWidth) 
					System.out.println("Bad delta-height value in JBIG2 symbol dictionary");

				symWidth += dw;

				// using a collective bitmap, so don't read a bitmap here
				if (huff!=-1 && refAgg==-1) {
					symWidths[i] = symWidth;
					totalWidth += symWidth;

					// refinement/aggregate coding
				} else if (refAgg!=-1) {
					if (huff!=-1) {
						if (!huffDecoder.decodeInt(refAggNum, huffAggInstTable.getTable())) {
							break;
						}
					} else {
						if (!arithDecoder.decodeInt(refAggNum, iaaiStats)) {
							break;
						}
					}
					//#if 0 //~ This special case was added about a year before the final draft
					//~ of the JBIG2 spec was released.  I have encountered some old
					//~ JBIG2 images that predate it.
					/*if (0) {
						//#else
							if (refAggNum == 1) {
								#endif
								if (huff) {
									symID = huffDecoder.readBits(symCodeLen);
									huffDecoder.decodeInt(&refDX, huffTableO);
									huffDecoder.decodeInt(&refDY, huffTableO);
									huffDecoder.decodeInt(&bmSize, huffTableA);
									huffDecoder.reset();
									arithDecoder.start();
								} else {
									symID = arithDecoder.decodeIAID(symCodeLen, iaidStats);
									arithDecoder.decodeInt(&refDX, iardxStats);
									arithDecoder.decodeInt(&refDY, iardyStats);
								}
								refBitmap = bitmaps[symID];
								bitmaps[numInputSyms + i] =
									readGenericRefinementRegion(symWidth, symHeight,
											sdrTemplate, false,
											refBitmap, refDX, refDY,
											sdrATX, sdrATY);
								//~ do we need to use the bmSize value here (in Huffman mode)?
							} else {
								bitmaps[numInputSyms + i] =
									readTextRegion(huff, true, symWidth, symHeight,
											refAggNum, 0, numInputSyms + i, null,
											symCodeLen, bitmaps, 0, 0, 0, 1, 0,
											huffTableF, huffTableH, huffTableK, huffTableO,
											huffTableO, huffTableO, huffTableO, huffTableA,
											sdrTemplate, sdrATX, sdrATY);
							}

						// non-ref/agg coding
					} else {
						bitmaps[numInputSyms + i] =
							readGenericBitmap(false, symWidth, symHeight,
									sdTemplate, false, false, null,
									sdATX, sdATY, 0);
					}
					 */
					++i;
				}

				// read the collective bitmap
				if (huff!=-1 && refAgg==-1) {
					huffDecoder.decodeInt(bmSize, huffBMSizeTable.getTable());
					reset();
					if (bmSize == 0) {
						collBitmap = new JBIG2Bitmap(0, totalWidth, symHeight);
						bmSize = symHeight * ((totalWidth + 7) >> 3);
						for (k = 0; k < (int)bmSize; ++k) {
							curStr.read();
						}
					} else {
						collBitmap = readGenericBitmap(true, totalWidth, symHeight,
								0, false, false, null, null, null,
								bmSize);
					}
					x = 0;
					for (; j < i; ++j) {
						bitmaps[numInputSyms + j] =
							collBitmap.getSlice(x, 0, symWidths[j], symHeight);
						x += symWidths[j];
					}
					collBitmap=null;
				}
			}

			// create the symbol dict object
			symbolDict = new JBIG2Symbol(segNum, numExSyms);

			// exported symbol list
			i = j = 0;
			ex = false;
			while (i < numInputSyms + numNewSyms) {
				if (huff!=-1) {
					huffDecoder.decodeInt(run, huffDecoder.huffTableA);
				} else {
					arithDecoder.decodeInt(run, iaexStats);
				}
				if (ex) {
					for (cnt = 0; cnt < run; ++cnt) {
						symbolDict.setBitmap(j++, bitmaps[i++]);
					}
				} else {
					i += run;
				}
				ex = !ex;
			}

			for (i = 0; i < numNewSyms; ++i) {
				bitmaps[numInputSyms + i]=null;
			}
			bitmaps=null;
			if (symWidths!=null) {
				symWidths=null;
			}

			// save the arithmetic decoder stats
			if (huff==-1 && contextRetained!=-1) {
				symbolDict.setGenericRegionStats(genericRegionStats.copy());
				if (refAgg!=-1) {
					symbolDict.setRefinementRegionStats(refinementRegionStats.copy());
				}
			}

			// store the new symbol dict
			segments.add(symbolDict);
			return true;

		}
		return false;
	}

	public void readTextRegionSeg(int segNum, boolean imm,
			boolean lossless, int length,
			int[] refSegs, int nRefSegs) throws IOException {
		JBIG2Bitmap bitmap = null;
		HuffmanTables[] runLengthTab = new HuffmanTables[36];
		HuffmanTables[] symCodeTab = null;
		HuffmanTables huffFSTable = null, huffDSTable = null, huffDTTable = null;
		HuffmanTables huffRDWTable = null, huffRDHTable = null;
		HuffmanTables huffRDXTable = null, huffRDYTable = null, huffRSizeTable = null;
		JBIG2Segment seg = null;
		Vector codeTables = new Vector();
		JBIG2Symbol symbolDict = null;
		JBIG2Bitmap[] syms = null;
		int w = 0, h = 0, x = 0, y = 0, segInfoFlags = 0, extCombOp;
		int flags = 0, huff = 0, refine = 0, logStrips = 0, refCorner = 0, transposed = 0;
		int combOp = 0, defPixel = 0, templ = 0;
		int sOffset = 0;
		int huffFlags = 0, huffFS = 0, huffDS = 0, huffDT = 0;
		int huffRDW = 0, huffRDH = 0, huffRDX = 0, huffRDY = 0, huffRSize = 0;
		int numInstances = 0, numSyms = 0, symCodeLen = 0;
		int[] atx = new int[2];
		int[] aty = new int[2];
		int i = 0, k = 0, kk = 0;
		int j = 0;

		// region segment info field
		if (!readULong(w) || !readULong(h) ||
				!readULong(x) || !readULong(y) ||
				!readUByte(segInfoFlags)) {
			System.out.println("FAILED WITH EOF");
		}
		extCombOp = segInfoFlags & 7;

		// rest of the text region header
		if (!readUWord(flags)) {
			System.out.println("FAILED WITH EOF");
		}
		huff = flags & 1;
		refine = (flags >> 1) & 1;
		logStrips = (flags >> 2) & 3;
		refCorner = (flags >> 4) & 3;
		transposed = (flags >> 6) & 1;
		combOp = (flags >> 7) & 3;
		defPixel = (flags >> 9) & 1;
		sOffset = (flags >> 10) & 0x1f;
		if ((sOffset & 0x10)!=-1) {
			sOffset |= -1 - 0x0f;
		}
		templ = (flags >> 15) & 1;
		huffFS = huffDS = huffDT = 0; // make gcc happy
		huffRDW = huffRDH = huffRDX = huffRDY = huffRSize = 0; // make gcc happy
		if (huff!=-1) {
			if (!readUWord(huffFlags)) {
				System.out.println("FAILED WITH EOF");
			}
			huffFS = huffFlags & 3;
			huffDS = (huffFlags >> 2) & 3;
			huffDT = (huffFlags >> 4) & 3;
			huffRDW = (huffFlags >> 6) & 3;
			huffRDH = (huffFlags >> 8) & 3;
			huffRDX = (huffFlags >> 10) & 3;
			huffRDY = (huffFlags >> 12) & 3;
			huffRSize = (huffFlags >> 14) & 1;
		}
		if (refine!=-1 && templ == 0) {
			if (!readByte(atx[0]) || !readByte(aty[0]) ||
					!readByte(atx[1]) || !readByte(aty[1])) {
				System.out.println("FAILED WITH EOF");
			}
		}
		if (!readULong(numInstances)) {
			System.out.println("FAILED WITH EOF");
		}

		// get symbol dictionaries and tables
		codeTables = new Vector();
		numSyms = 0;
		for (i = 0; i < nRefSegs; ++i) {
			if ((seg = findSegment(refSegs[i]))!=null) {
				if (seg.getType() == jbig2SegSymbolDict) {
					numSyms += seg.getSize();
				} else if (seg.getType() == jbig2SegCodeTable) {
					codeTables.add(seg);
				}
			} else {
				System.out.println("Invalid segment reference in JBIG2 text region");
			}
		}
		symCodeLen = 0;
		i = 1;
		while (i < numSyms) {
			++symCodeLen;
			i <<= 1;
		}

		// get the symbol bitmaps
		syms = new JBIG2Bitmap[numSyms];
		kk = 0;
		for (i = 0; i < nRefSegs; ++i) {
			if ((seg = findSegment(refSegs[i]))!=null) {
				if (seg.getType() == jbig2SegSymbolDict) {
					symbolDict = (JBIG2Symbol)seg;
					for (k = 0; k < symbolDict.getSize(); ++k) {
						syms[kk++] = symbolDict.getBitmap(k);
					}
				}
			}
		}

		// get the Huffman tables
		huffFSTable = huffDSTable = huffDTTable = null; // make gcc happy
		huffRDWTable = huffRDHTable = null; // make gcc happy
		huffRDXTable = huffRDYTable = huffRSizeTable = null; // make gcc happy
		i = 0;
		if (huff!=-1) {
			if (huffFS == 0) {
				huffFSTable.setTable(huffFSTable.huffTableF);
			} else if (huffFS == 1) {
				huffFSTable.setTable(huffFSTable.huffTableG);
			} else {
				huffFSTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffDS == 0) {
				huffDSTable.setTable(huffDSTable.huffTableH);
			} else if (huffDS == 1) {
				huffDSTable.setTable(huffDSTable.huffTableI);
			} else if (huffDS == 2) {
				huffDSTable.setTable(huffDSTable.huffTableJ);
			} else {
				huffDSTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffDT == 0) {
				huffDTTable.setTable(huffDTTable.huffTableK);
			} else if (huffDT == 1) {
				huffDTTable.setTable(huffDTTable.huffTableL);
			} else if (huffDT == 2) {
				huffDTTable.setTable(huffDTTable.huffTableM);
			} else {
				huffDTTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffRDW == 0) {
				huffRDWTable.setTable(huffRDWTable.huffTableN);
			} else if (huffRDW == 1) {
				huffRDWTable.setTable(huffRDWTable.huffTableO);
			} else {
				huffRDWTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffRDH == 0) {
				huffRDHTable.setTable(huffRDHTable.huffTableN);
			} else if (huffRDH == 1) {
				huffRDHTable.setTable(huffRDHTable.huffTableO);
			} else {
				huffRDHTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffRDX == 0) {
				huffRDXTable.setTable(huffRDXTable.huffTableN);
			} else if (huffRDX == 1) {
				huffRDXTable.setTable(huffRDXTable.huffTableO);
			} else {
				huffRDXTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffRDY == 0) {
				huffRDYTable.setTable(huffRDYTable.huffTableN);
			} else if (huffRDY == 1) {
				huffRDYTable.setTable(huffRDYTable.huffTableO);
			} else {
				huffRDYTable = ((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
			if (huffRSize == 0) {
				huffRSizeTable.setTable(huffRSizeTable.huffTableA);
			} else {
				huffRSizeTable =
					((JBIG2CodeTable)codeTables.get(i++)).getHuffTable();
			}
		}
		codeTables=null;

		// symbol ID Huffman decoding table
		if (huff!=-1) {
			huffDecoder=null;
			for (i = 0; i < 32; ++i) {
				runLengthTab[i].val = i;
				runLengthTab[i].prefixLen = huffDecoder.readBits(4);
				runLengthTab[i].rangeLen = 0;
			}
			runLengthTab[32].val = 0x103;
			runLengthTab[32].prefixLen = huffDecoder.readBits(4);
			runLengthTab[32].rangeLen = 2;
			runLengthTab[33].val = 0x203;
			runLengthTab[33].prefixLen = huffDecoder.readBits(4);
			runLengthTab[33].rangeLen = 3;
			runLengthTab[34].val = 0x20b;
			runLengthTab[34].prefixLen = huffDecoder.readBits(4);
			runLengthTab[34].rangeLen = 7;
			runLengthTab[35].prefixLen = 0;
			runLengthTab[35].rangeLen = runLengthTab[35].jbig2HuffmanEOT;
			huffDecoder.buildTable(runLengthTab, 35);
			symCodeTab = new HuffmanTables[numSyms + 1];
			for (i = 0; i < numSyms; ++i) {
				symCodeTab[i].val = i;
				symCodeTab[i].rangeLen = 0;
			}
			i = 0;
			while (i < numSyms) {
				huffDecoder.decodeInt(j, runLengthTab);
				if (j > 0x200) {
					for (j -= 0x200; (j < numSyms) && (i < numSyms); --j) {
						symCodeTab[i++].prefixLen = 0;
					}
				} else if (j > 0x100) {
					for (j -= 0x100; (j < numSyms) && (i < numSyms); --j) {
						symCodeTab[i].prefixLen = symCodeTab[i-1].prefixLen;
						++i;
					}
				} else {
					symCodeTab[i++].prefixLen = j;
				}
			}
			symCodeTab[numSyms].prefixLen = 0;
			symCodeTab[numSyms].rangeLen = symCodeTab[numSyms].jbig2HuffmanEOT;
			huffDecoder.buildTable(symCodeTab, numSyms);
			huffDecoder=null;

			// set up the arithmetic decoder
		} else {
			symCodeTab = null;
			resetIntStats(symCodeLen);
			arithDecoder.start();
		}
		if (refine!=-1) {
			resetRefinementStats(templ, null);
		}

		bitmap = readTextRegion(huff!=-1, refine!=-1, w, h, numInstances,
				logStrips, numSyms, symCodeTab, symCodeLen, syms,
				defPixel, combOp, transposed, refCorner, sOffset,
				huffFSTable, huffDSTable, huffDTTable,
				huffRDWTable, huffRDHTable,
				huffRDXTable, huffRDYTable, huffRSizeTable,
				templ, atx, aty);

		syms=null;

		// combine the region bitmap into the page bitmap
		if (imm) {
			int pageH = 0;
			int curPageH = pageBitmap.getHeight();
			
			if (pageH == 0xffffffff && y + h > curPageH) {
				pageBitmap.expand(y + h, pageBitmap.nextPixel(pageBitmap));
			}
			pageBitmap.combine(bitmap, x, y, extCombOp);
			bitmap=null;

			// store the region bitmap
		} else {
			bitmap.setSegNum(segNum);
			segments.add(bitmap);
		}

		// clean up the Huffman decoder
		if (huff!=-1) {
			symCodeTab=null;
		}

		return;
	}

	public JBIG2Bitmap readTextRegion(boolean huff, boolean refine,
			int w, int h,
			int numInstances,
			int logStrips,
			int numSyms,
			HuffmanTables[] symCodeTab,
			int symCodeLen,
			JBIG2Bitmap[] syms,
			int defPixel, int combOp,
			int transposed, int refCorner,
			int sOffset,
			HuffmanTables huffFSTable,
			HuffmanTables huffDSTable,
			HuffmanTables huffDTTable,
			HuffmanTables huffRDWTable,
			HuffmanTables huffRDHTable,
			HuffmanTables huffRDXTable,
			HuffmanTables huffRDYTable,
			HuffmanTables huffRSizeTable,
			int templ,
			int[] atx, int[] aty) {
		JBIG2Bitmap bitmap = null;
		JBIG2Bitmap symbolBitmap = null;
		int strips=0;
		int t=0, dt=0, tt=0, s=0, ds=0, sFirst=0, j=0;
		int rdw=0, rdh=0, rdx=0, rdy=0, ri=0, refDX=0, refDY=0, bmSize = 0;
		int symID=0, inst=0, bw=0, bh=0;

		strips = 1 << logStrips;

		// allocate the bitmap
		bitmap = new JBIG2Bitmap(0, w, h);
		if (defPixel!=-1) {
			bitmap.clearToOne();
		} else {
			bitmap.clearToZero();
		}

		// decode initial T value
		if (huff) {
			huffDecoder.decodeInt(t, huffDTTable.getTable());
		} else {
			arithDecoder.decodeInt(t, iadtStats);
		}
		t *= -(int)strips;

		inst = 0;
		sFirst = 0;
		while (inst < numInstances) {

			// decode delta-T
			if (huff) {
				huffDecoder.decodeInt(dt, huffDTTable.getTable());
			} else {
				arithDecoder.decodeInt(dt, iadtStats);
			}
			t += dt * strips;

			// first S value
			if (huff) {
				huffDecoder.decodeInt(ds, huffFSTable.getTable());
			} else {
				arithDecoder.decodeInt(ds, iafsStats);
			}
			sFirst += ds;
			s = sFirst;

			// read the instances
			while (true) {

				// T value
				if (strips == 1) {
					dt = 0;
				} else if (huff) {
					dt = huffDecoder.readBits(logStrips);
				} else {
					arithDecoder.decodeInt(dt, iaitStats);
				}
				tt = t + dt;

				// symbol ID
				if (huff) {
					if (symCodeTab!=null) {
						huffDecoder.decodeInt(j, symCodeTab);
						symID = (int)j;
					} else {
						symID = huffDecoder.readBits(symCodeLen);
					}
				} else {
					symID = arithDecoder.decodeIAID(symCodeLen, iaidStats);
				}

				if (symID >= (int)numSyms) {
					System.out.println("Invalid symbol number in JBIG2 text region");
				} else {

					// get the symbol bitmap
					symbolBitmap = null;
					if (refine) {
						if (huff) {
							ri = (int)huffDecoder.readBit();
						} else {
							arithDecoder.decodeInt(ri, iariStats);
						}
					} else {
						ri = 0;
					}
					if (ri!=0) {
						if (huff) {
							huffDecoder.decodeInt(rdw, huffRDWTable.getTable());
							huffDecoder.decodeInt(rdh, huffRDHTable.getTable());
							huffDecoder.decodeInt(rdx, huffRDXTable.getTable());
							huffDecoder.decodeInt(rdy, huffRDYTable.getTable());
							huffDecoder.decodeInt(bmSize, huffRSizeTable.getTable());
							huffDecoder=null;
							arithDecoder.start();
						} else {
							arithDecoder.decodeInt(rdw, iardwStats);
							arithDecoder.decodeInt(rdh, iardhStats);
							arithDecoder.decodeInt(rdx, iardxStats);
							arithDecoder.decodeInt(rdy, iardyStats);
						}
						refDX = ((rdw >= 0) ? rdw : rdw - 1) / 2 + rdx;
						refDY = ((rdh >= 0) ? rdh : rdh - 1) / 2 + rdy;

						symbolBitmap =
							readGenericRefinementRegion(rdw + syms[symID].getWidth(),
									rdh + syms[symID].getHeight(),
									templ, false, syms[symID],
									refDX, refDY, atx, aty);
						//~ do we need to use the bmSize value here (in Huffman mode)?
					} else {
						symbolBitmap = syms[symID];
					}

					// combine the symbol bitmap into the region bitmap
					//~ something is wrong here - refCorner shouldn't degenerate into
					//~   two cases
					bw = symbolBitmap.getWidth() - 1;
					bh = symbolBitmap.getHeight() - 1;
					if (transposed!=-1) {
						switch (refCorner) {
						case 0: // bottom left
							bitmap.combine(symbolBitmap, tt, s, combOp);
							break;
						case 1: // top left
							bitmap.combine(symbolBitmap, tt, s, combOp);
							break;
						case 2: // bottom right
							bitmap.combine(symbolBitmap, tt - bw, s, combOp);
							break;
						case 3: // top right
							bitmap.combine(symbolBitmap, tt - bw, s, combOp);
							break;
						}
						s += bh;
					} else {
						switch (refCorner) {
						case 0: // bottom left
							bitmap.combine(symbolBitmap, s, tt - bh, combOp);
							break;
						case 1: // top left
							bitmap.combine(symbolBitmap, s, tt, combOp);
							break;
						case 2: // bottom right
							bitmap.combine(symbolBitmap, s, tt - bh, combOp);
							break;
						case 3: // top right
							bitmap.combine(symbolBitmap, s, tt, combOp);
							break;
						}
						s += bw;
					}
					if (ri!=0) {
						symbolBitmap=null;
					}
				}

				// next instance
				++inst;

				// next S value
				if (huff) {
					if (!huffDecoder.decodeInt(ds, huffDSTable.getTable())) {
						break;
					}
				} else {
					if (!arithDecoder.decodeInt(ds, iadsStats)) {
						break;
					}
				}
				s += sOffset + ds;
			}
		}

		return bitmap;
	}

	public void readPatternDictSeg(int segNum, int length) throws IOException {
		JBIG2Pattern patternDict = null;
		JBIG2Bitmap bitmap = null;
		int flags=0, patternW=0, patternH=0, grayMax=0, templ=0, mmr=0;
		int[] atx = new int[4], aty = new int[4];
		int i=0, x=0;

		// halftone dictionary flags, pattern width and height, max gray value
		if (!readUByte(flags) ||
				!readUByte(patternW) ||
				!readUByte(patternH) ||
				!readULong(grayMax)) {
			System.out.println("EOF ERROR");
		}
		templ = (flags >> 1) & 3;
		mmr = flags & 1;

		// set up the arithmetic decoder
		if (mmr==-1) {
			resetGenericStats(templ, null);
			arithDecoder.start();
		}

		// read the bitmap
		atx[0] = -(int)patternW; aty[0] =  0;
		atx[1] = -3;             aty[1] = -1;
		atx[2] =  2;             aty[2] = -2;
		atx[3] = -2;             aty[3] = -2;
		bitmap = readGenericBitmap(mmr!=-1, (grayMax + 1) * patternW, patternH,
				templ, false, false, null,
				atx, aty, length - 7);

		// create the pattern dict object
		patternDict = new JBIG2Pattern(segNum, grayMax + 1);

		// split up the bitmap
		x = 0;
		for (i = 0; i <= grayMax; ++i) {
			patternDict.setBitmap(i, bitmap.getSlice(x, 0, patternW, patternH));
			x += patternW;
		}

		// free memory
		bitmap = null;

		// store the new pattern dict
		segments.add(patternDict);

		return;
	}

	public void readHalftoneRegionSeg(int segNum, boolean imm,
			boolean lossless, int length,
			int[] refSegs, int nRefSegs) throws IOException {
		JBIG2Bitmap bitmap = null;
		JBIG2Segment seg = null;
		JBIG2Pattern patternDict = null;
		JBIG2Bitmap skipBitmap = null;
		int[] grayImg;
		JBIG2Bitmap grayBitmap = null;
		JBIG2Bitmap patternBitmap = null;
		int w=0, h=0, x=0, y=0, segInfoFlags=0, extCombOp = 0;
		int flags=0, mmr=0, templ=0, enableSkip=0, combOp = 0;
		int gridW=0, gridH=0, stepX=0, stepY=0, patW=0, patH=0;
		int[] atx = new int[4], aty = new int[4];
		int gridX=0, gridY=0, xx=0, yy=0, bit=0, j=0;
		int bpp=0, m=0, n=0, i=0;

		// region segment info field
		if (!readULong(w) || !readULong(h) ||
				!readULong(x) || !readULong(y) ||
				!readUByte(segInfoFlags)) {
			System.out.println("EOF ERROR");
		}
		extCombOp = segInfoFlags & 7;

		// rest of the halftone region header
		if (!readUByte(flags)) {
			System.out.println("EOF ERROR");
		}
		mmr = flags & 1;
		templ = (flags >> 1) & 3;
		enableSkip = (flags >> 3) & 1;
		combOp = (flags >> 4) & 7;
		if (!readULong(gridW) || !readULong(gridH) ||
				!readLong(gridX) || !readLong(gridY) ||
				!readUWord(stepX) || !readUWord(stepY)) {
			System.out.println("EOF ERROR");
		}

		// get pattern dictionary
		if (nRefSegs != 1) {
			System.out.println("Bad symbol dictionary reference in JBIG2 halftone segment");
			return;
		}
		seg = findSegment(refSegs[0]);
		if (seg.getType() != jbig2SegPatternDict) {
			System.out.println("Bad symbol dictionary reference in JBIG2 halftone segment");
			return;
		}
		patternDict = (JBIG2Pattern)seg;
		bpp = 0;
		i = 1;
		while (i < patternDict.getSize()) {
			++bpp;
			i <<= 1;
		}
		patW = patternDict.getBitmap(0).getWidth();
		patH = patternDict.getBitmap(0).getHeight();

		// set up the arithmetic decoder
		if (mmr==-1) {
			resetGenericStats(templ, null);
			arithDecoder.start();
		}

		// allocate the bitmap
		bitmap = new JBIG2Bitmap(segNum, w, h);
		if ((flags & 0x80)!=-1) { // HDEFPIXEL
			bitmap.clearToOne();
		} else {
			bitmap.clearToZero();
		}

		// compute the skip bitmap
		skipBitmap = null;
		if (enableSkip!=-1) {
			skipBitmap = new JBIG2Bitmap(0, gridW, gridH);
			skipBitmap.clearToZero();
			for (m = 0; m < gridH; ++m) {
				xx = gridX + m * stepY;
				yy = gridY + m * stepX;
				for (n = 0; n < gridW; ++n) {
					if (((xx + (int)patW) >> 8) <= 0 || (xx >> 8) >= (int)w ||
							((yy + (int)patH) >> 8) <= 0 || (yy >> 8) >= (int)h) {
						skipBitmap.setPixel(n, m);
					}
				}
			}
		}

		// read the gray-scale image
		grayImg = new int[gridW * gridH];

		atx[0] = templ <= 1 ? 3 : 2;  aty[0] = -1;
		atx[1] = -3;                  aty[1] = -1;
		atx[2] =  2;                  aty[2] = -2;
		atx[3] = -2;                  aty[3] = -2;
		for (j = bpp - 1; j >= 0; --j) {
			grayBitmap = readGenericBitmap(mmr!=-1, gridW, gridH, templ, false,
					enableSkip!=-1, skipBitmap, atx, aty, -1);
			i = 0;
			for (m = 0; m < gridH; ++m) {
				for (n = 0; n < gridW; ++n) {
					bit = grayBitmap.getPixel(n, m) ^ (grayImg[i] & 1);
					grayImg[i] = (grayImg[i] << 1) | bit;
					++i;
				}
			}
			grayBitmap = null;
		}

		// decode the image
		i = 0;
		for (m = 0; m < gridH; ++m) {
			xx = gridX + m * stepY;
			yy = gridY + m * stepX;
			for (n = 0; n < gridW; ++n) {
				if ((enableSkip!=-1 && skipBitmap.getPixel(n, m)!=-1)) {
					patternBitmap = patternDict.getBitmap(grayImg[i]);
					bitmap.combine(patternBitmap, xx >> 8, yy >> 8, combOp);
				}
				xx += stepX;
				yy -= stepY;
				++i;
			}
		}

		grayImg = null;

		// combine the region bitmap into the page bitmap
		if (imm) {
			int pageH = 0;
			int curPageH = pageBitmap.getHeight();
			if (pageH == 0xffffffff && y + h > curPageH) {
				pageBitmap.expand(y + h, pageBitmap.nextPixel(pageBitmap));
			}
			pageBitmap.combine(bitmap, x, y, extCombOp);
			bitmap = null;

			// store the region bitmap
		} else {
			segments.add(bitmap);
		}

		return;
	}

	public void readGenericRegionSeg(int segNum, boolean imm,
			boolean lossless, int length) throws IOException {
		JBIG2Bitmap bitmap;
		int w=0, h=0, x=0, y=0, segInfoFlags=0, extCombOp=0;
		int flags=0, mmr=0, templ=0, tpgdOn=0;
		int[] atx = new int[4], aty = new int[4];

		// region segment info field
		if (!readULong(w) || !readULong(h) ||
				!readULong(x) || !readULong(y) ||
				!readUByte(segInfoFlags)) {
			System.out.println("Unexpected EOF in JBIG2 stream");
		}
		extCombOp = segInfoFlags & 7;

		// rest of the generic region segment header
		if (!readUByte(flags)) {
			System.out.println("Unexpected EOF in JBIG2 stream");
		}
		mmr = flags & 1;
		templ = (flags >> 1) & 3;
		tpgdOn = (flags >> 3) & 1;

		// AT flags
		if (mmr!=-1) {
			if (templ == 0) {
				if (!readByte(atx[0]) ||
						!readByte(aty[0]) ||
						!readByte(atx[1]) ||
						!readByte(aty[1]) ||
						!readByte(atx[2]) ||
						!readByte(aty[2]) ||
						!readByte(atx[3]) ||
						!readByte(aty[3])) {
					System.out.println("Unexpected EOF in file");
				}
			} else {
				if (!readByte(atx[0]) ||
						!readByte(aty[0])) {
					System.out.println("Unexpected EOF in file");
				}
			}
		}

		// set up the arithmetic decoder
		if (mmr!=-1) {
			resetGenericStats(templ, null);
			arithDecoder.start();
		}

		// read the bitmap
		bitmap = readGenericBitmap(mmr!=-1, w, h, templ, tpgdOn!=-1, false,
				null, atx, aty, mmr!=-1 ? 0 : length - 18);

		// combine the region bitmap into the page bitmap
		if (imm) {
			int pageH = 0;
			int curPageH = pageBitmap.getHeight();
			if (pageH == 0xffffffff && y + h > curPageH) {
				pageBitmap.expand(y + h, pageBitmap.nextPixel(pageBitmap));
			}
			pageBitmap.combine(bitmap, x, y, extCombOp);
			bitmap = null;

			// store the region bitmap
		} else {
			bitmap.setSegNum(segNum);
			segments.add(bitmap);
		}

		return;
	}

	public JBIG2Bitmap readGenericBitmap(boolean mmr, int w, int h,
			int templ, boolean tpgdOn,
			boolean useSkip, JBIG2Bitmap skip,
			int[] atx, int[] aty,
			int mmrDataLength) {
		JBIG2Bitmap bitmap=null;
		boolean ltp = false;
		int ltpCX, cx, cx0, cx1, cx2;
		JBIG2Bitmap cxPtr0=null, cxPtr1=null;
		JBIG2Bitmap atPtr0=null, atPtr1=null, atPtr2=null, atPtr3=null;
		int[] refLine = null, codingLine=null;
		int code1=0, code2=0, code3=0;
		int x=0, y=0, a0=0, pix=0, i=0, refI=0, codingI=0;

		bitmap = new JBIG2Bitmap(0, w, h);
		bitmap.clearToZero();

		//----- MMR decode

		
		
		if (mmr) {

			mmrDecoder=null;
			refLine = new int[w + 2];
			codingLine = new int[w + 2];
			codingLine[0] = codingLine[1] = w;

			CCITT ct = new CCITT();
			
			int TwoDimHoriz = ct.getTwoDimHoriz();
			int TwoDimPass = ct.getTwoDimPass();
			int TwoDimVert0 = ct.getTwoDimVert0();
			int TwoDimVertR1 = ct.getTwoDimVertR1();
			int TwoDimVertR2 = ct.getTwoDimVertR2();
			int TwoDimVertR3 = ct.getTwoDimVertR3();
			int TwoDimVertL1 = ct.getTwoDimVertL1();
			int TwoDimVertL2 = ct.getTwoDimVertL2();
			int TwoDimVertL3 = ct.getTwoDimVertL3();
			
			
			for (y = 0; y < h; ++y) {

				// copy coding line to ref line
				for (i = 0; codingLine[i] < w; ++i) {
					refLine[i] = codingLine[i];
				}
				refLine[i] = refLine[i + 1] = w;

				// decode a line
				refI = 0;     // b1 = refLine[refI]
				codingI = 0;  // a1 = codingLine[codingI]
				a0 = 0;
				do {
					code1 = mmrDecoder.get2DCode();
					switch (code1) {
					case 0:
						if (refLine[refI] < w) {
							a0 = refLine[refI + 1];
							refI += 2;
						}
						break;
					case 1:
						if ((codingI & 1) == 1) {
							code1 = 0;
							do {
								code1 += code3 = mmrDecoder.getBlackCode();
							} while (code3 >= 64);
							code2 = 0;
							do {
								code2 += code3 = mmrDecoder.getWhiteCode();
							} while (code3 >= 64);
						} else {
							code1 = 0;
							do {
								code1 += code3 = mmrDecoder.getWhiteCode();
							} while (code3 >= 64);
							code2 = 0;
							do {
								code2 += code3 = mmrDecoder.getBlackCode();
							} while (code3 >= 64);
						}
						if (code1 > 0 || code2 > 0) {
							a0 = codingLine[codingI++] = a0 + code1;
							a0 = codingLine[codingI++] = a0 + code2;
							while (refLine[refI] <= a0 && refLine[refI] < w) {
								refI += 2;
							}
						}
						break;
					case 2:
						a0 = codingLine[codingI++] = refLine[refI];
						if (refLine[refI] < w) {
							++refI;
						}
						break;
					case 3:
						a0 = codingLine[codingI++] = refLine[refI] + 1;
						if (refLine[refI] < w) {
							++refI;
							while (refLine[refI] <= a0 && refLine[refI] < w) {
								refI += 2;
							}
						}
						break;
					case 5:
						a0 = codingLine[codingI++] = refLine[refI] + 2;
						if (refLine[refI] < w) {
							++refI;
							while (refLine[refI] <= a0 && refLine[refI] < w) {
								refI += 2;
							}
						}
						break;
					case 7:
						a0 = codingLine[codingI++] = refLine[refI] + 3;
						if (refLine[refI] < w) {
							++refI;
							while (refLine[refI] <= a0 && refLine[refI] < w) {
								refI += 2;
							}
						}
						break;
					case 4:
						a0 = codingLine[codingI++] = refLine[refI] - 1;
						if (refI > 0) {
							--refI;
						} else {
							++refI;
						}
						while (refLine[refI] <= a0 && refLine[refI] < w) {
							refI += 2;
						}
						break;
					case 6:
						a0 = codingLine[codingI++] = refLine[refI] - 2;
						if (refI > 0) {
							--refI;
						} else {
							++refI;
						}
						while (refLine[refI] <= a0 && refLine[refI] < w) {
							refI += 2;
						}
						break;
					case 8:
						a0 = codingLine[codingI++] = refLine[refI] - 3;
						if (refI > 0) {
							--refI;
						} else {
							++refI;
						}
						while (refLine[refI] <= a0 && refLine[refI] < w) {
							refI += 2;
						}
						break;
					default:
						System.out.println("Illegal code in JBIG2 MMR bitmap data");
					break;
					}
				} while (a0 < w);
				codingLine[codingI++] = w;

				// convert the run lengths to a bitmap line
				i = 0;
				while (codingLine[i] < w) {
					for (x = codingLine[i]; x < codingLine[i+1]; ++x) {
						bitmap.setPixel(x, y);
					}
					i += 2;
				}
			}

			if (mmrDataLength >= 0) {
				mmrDecoder.skipTo(mmrDataLength);
			} else {
				if (mmrDecoder.get24Bits() != 0x001001) {
					System.out.println("Missing EOFB in JBIG2 MMR bitmap data");
				}
			}

			refLine=null;
			codingLine=null;

			//----- arithmetic decode

		} else {
			// set up the typical row context
			ltpCX = 0; // make gcc happy
			if (tpgdOn) {
				switch (templ) {
				case 0:
					ltpCX = 0x3953; // 001 11001 0101 0011
					break;
				case 1:
					ltpCX = 0x079a; // 0011 11001 101 0
					break;
				case 2:
					ltpCX = 0x0e3; // 001 1100 01 1
					break;
				case 3:
					ltpCX = 0x18a; // 01100 0101 1
					break;
				}
			}

			ltp = false;
			cx = cx0 = cx1 = cx2 = 0; // make gcc happy
			for (y = 0; y < h; ++y) {

				// check for a "typical" (duplicate) row
				if (tpgdOn) {
					if (arithDecoder.decodeBit(ltpCX, genericRegionStats)!=0) {
						ltp = !ltp;
					}
					if (ltp) {
						bitmap.duplicateRow(y, y-1);
						continue;
					}
				}

				switch (templ) {
				case 0:

					// set up the context
					bitmap.getPixelPtr(0, y-2, cxPtr0);
					cx0 = bitmap.nextPixel(cxPtr0);
					cx0 = (cx0 << 1) | bitmap.nextPixel(cxPtr0);
					bitmap.getPixelPtr(0, y-1, cxPtr1);
					cx1 = bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx2 = 0;
					bitmap.getPixelPtr(atx[0], y + aty[0], atPtr0);
					bitmap.getPixelPtr(atx[1], y + aty[1], atPtr1);
					bitmap.getPixelPtr(atx[2], y + aty[2], atPtr2);
					bitmap.getPixelPtr(atx[3], y + aty[3], atPtr3);

					// decode the row
					for (x = 0; x < w; ++x) {

						// build the context
						cx = (cx0 << 13) | (cx1 << 8) | (cx2 << 4) |
						(bitmap.nextPixel(atPtr0) << 3) |
						(bitmap.nextPixel(atPtr1) << 2) |
						(bitmap.nextPixel(atPtr2) << 1) |
						bitmap.nextPixel(atPtr3);

						// check for a skipped pixel
						if (useSkip && skip.getPixel(x, y)!=-1) {
							pix = 0;

							// decode the pixel
						} else if ((pix = arithDecoder.decodeBit(cx, genericRegionStats))!=-1) {
							bitmap.setPixel(x, y);
						}

						// update the context
						cx0 = ((cx0 << 1) | bitmap.nextPixel(cxPtr0)) & 0x07;
						cx1 = ((cx1 << 1) | bitmap.nextPixel(cxPtr1)) & 0x1f;
						cx2 = ((cx2 << 1) | pix) & 0x0f;
					}
					break;

				case 1:

					// set up the context
					bitmap.getPixelPtr(0, y-2, cxPtr0);
					cx0 = bitmap.nextPixel(cxPtr0);
					cx0 = (cx0 << 1) | bitmap.nextPixel(cxPtr0);
					cx0 = (cx0 << 1) | bitmap.nextPixel(cxPtr0);
					bitmap.getPixelPtr(0, y-1, cxPtr1);
					cx1 = bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx2 = 0;
					bitmap.getPixelPtr(atx[0], y + aty[0], atPtr0);

					// decode the row
					for (x = 0; x < w; ++x) {

						// build the context
						cx = (cx0 << 9) | (cx1 << 4) | (cx2 << 1) |
						bitmap.nextPixel(atPtr0);

						// check for a skipped pixel
						if (useSkip && skip.getPixel(x, y)!=-1) {
							pix = 0;

							// decode the pixel
						} else if ((pix = arithDecoder.decodeBit(cx, genericRegionStats))!=-1) {
							bitmap.setPixel(x, y);
						}

						// update the context
						cx0 = ((cx0 << 1) | bitmap.nextPixel(cxPtr0)) & 0x0f;
						cx1 = ((cx1 << 1) | bitmap.nextPixel(cxPtr1)) & 0x1f;
						cx2 = ((cx2 << 1) | pix) & 0x07;
					}
					break;

				case 2:

					// set up the context
					bitmap.getPixelPtr(0, y-2, cxPtr0);
					cx0 = bitmap.nextPixel(cxPtr0);
					cx0 = (cx0 << 1) | bitmap.nextPixel(cxPtr0);
					bitmap.getPixelPtr(0, y-1, cxPtr1);
					cx1 = bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx2 = 0;
					bitmap.getPixelPtr(atx[0], y + aty[0], atPtr0);

					// decode the row
					for (x = 0; x < w; ++x) {

						// build the context
						cx = (cx0 << 7) | (cx1 << 3) | (cx2 << 1) |
						bitmap.nextPixel(atPtr0);

						// check for a skipped pixel
						if (useSkip && skip.getPixel(x, y)!=-1) {
							pix = 0;

							// decode the pixel
						} else if ((pix = arithDecoder.decodeBit(cx, genericRegionStats))!=-1) {
							bitmap.setPixel(x, y);
						}

						// update the context
						cx0 = ((cx0 << 1) | bitmap.nextPixel(cxPtr0)) & 0x07;
						cx1 = ((cx1 << 1) | bitmap.nextPixel(cxPtr1)) & 0x0f;
						cx2 = ((cx2 << 1) | pix) & 0x03;
					}
					break;

				case 3:

					// set up the context
					bitmap.getPixelPtr(0, y-1, cxPtr1);
					cx1 = bitmap.nextPixel(cxPtr1);
					cx1 = (cx1 << 1) | bitmap.nextPixel(cxPtr1);
					cx2 = 0;
					bitmap.getPixelPtr(atx[0], y + aty[0], atPtr0);

					// decode the row
					for (x = 0; x < w; ++x) {

						// build the context
						cx = (cx1 << 5) | (cx2 << 1) |
						bitmap.nextPixel(atPtr0);

						// check for a skipped pixel
						if (useSkip && skip.getPixel(x, y)!=-1) {
							pix = 0;

							// decode the pixel
						} else if ((pix = arithDecoder.decodeBit(cx, genericRegionStats))!=-1) {
							bitmap.setPixel(x, y);
						}

						// update the context
						cx1 = ((cx1 << 1) | bitmap.nextPixel(cxPtr1)) & 0x1f;
						cx2 = ((cx2 << 1) | pix) & 0x0f;
					}
					break;
				}
			}
		}

		return bitmap;
	}

	public void readGenericRefinementRegionSeg(int segNum, boolean imm,
			boolean lossless, int length,
			int[] refSegs,
			int nRefSegs) throws IOException {
		JBIG2Bitmap bitmap=null, refBitmap=null;
		int w=0, h=0, x=0, y=0, segInfoFlags=0, extCombOp=0;
		int flags=0, templ=0, tpgrOn=0;
		int[] atx = new int[2], aty = new int[2];
		JBIG2Segment seg = null;

		// region segment info field
		if (!readULong(w) || !readULong(h) ||
				!readULong(x) || !readULong(y) ||
				!readUByte(segInfoFlags)) {
			System.out.println("EOF ERROR");
		}
		extCombOp = segInfoFlags & 7;

		// rest of the generic refinement region segment header
		if (!readUByte(flags)) {
			System.out.println("EOF ERROR");
		}
		templ = flags & 1;
		tpgrOn = (flags >> 1) & 1;

		// AT flags
		if (templ!=0) {
			if (!readByte(atx[0]) || !readByte(aty[0]) ||
					!readByte(atx[1]) || !readByte(aty[1])) {
				System.out.println("EOF ERROR");
			}
		}

		// resize the page bitmap if needed
		if (nRefSegs == 0 || imm) {
			int pageH = 0;
			int curPageH = pageBitmap.getHeight();
			if (pageH == 0xffffffff && y + h > curPageH) {
				pageBitmap.expand(y + h, pageBitmap.nextPixel(pageBitmap));
			}
		}

		// get referenced bitmap
		if (nRefSegs > 1) {
			System.out.println("Bad reference in JBIG2 generic refinement segment");
			return;
		}
		if (nRefSegs == 1) {
			seg = findSegment(refSegs[0]);
			if (seg.getType() != jbig2SegBitmap) {
				System.out.println("Bad bitmap reference in JBIG2 generic refinement segment");
				return;
			}
			refBitmap = (JBIG2Bitmap)seg;
		} else {
			refBitmap = pageBitmap.getSlice(x, y, w, h);
		}

		// set up the arithmetic decoder
		resetRefinementStats(templ, null);
		arithDecoder.start();

		// read
		bitmap = readGenericRefinementRegion(w, h, templ, tpgrOn!=0,
				refBitmap, 0, 0, atx, aty);

		// combine the region bitmap into the page bitmap
		if (imm) {
			pageBitmap.combine(bitmap, x, y, extCombOp);
			bitmap=null;

			// store the region bitmap
		} else {
			bitmap.setSegNum(segNum);
			segments.add(bitmap);
		}

		// delete the referenced bitmap
		if (nRefSegs == 1) {
			discardSegment(refSegs[0]);
		} else {
			refBitmap=null;
		}

		return;
	}

	public JBIG2Bitmap readGenericRefinementRegion(int w, int h,
			int templ, boolean tpgrOn,
			JBIG2Bitmap refBitmap,
			int refDX, int refDY,
			int[] atx, int[] aty) {
		JBIG2Bitmap bitmap=null;
		boolean ltp=false;
		int ltpCX=0, cx=0, cx0=0, cx2=0, cx3=0, cx4=0, tpgrCX0=0, tpgrCX1=0, tpgrCX2=0;
		JBIG2Bitmap cxPtr0=null, cxPtr1=null, cxPtr2=null, cxPtr3=null, cxPtr4=null, cxPtr5=null, cxPtr6=null;
		JBIG2Bitmap tpgrCXPtr0=null, tpgrCXPtr1=null, tpgrCXPtr2=null;
		int x=0, y=0, pix=0;

		bitmap = new JBIG2Bitmap(0, w, h);
		bitmap.clearToZero();

		// set up the typical row context
		if (templ!=0) {
			ltpCX = 0x008;
		} else {
			ltpCX = 0x0010;
		}

		ltp = false;
		for (y = 0; y < h; ++y) {

			if (templ!=0) {

				// set up the context
				bitmap.getPixelPtr(0, y-1, cxPtr0);
				cx0 = bitmap.nextPixel(cxPtr0);
				bitmap.getPixelPtr(-1, y, cxPtr1);
				refBitmap.getPixelPtr(-refDX, y-1-refDY, cxPtr2);
				refBitmap.getPixelPtr(-1-refDX, y-refDY, cxPtr3);
				cx3 = refBitmap.nextPixel(cxPtr3);
				cx3 = (cx3 << 1) | refBitmap.nextPixel(cxPtr3);
				refBitmap.getPixelPtr(-refDX, y+1-refDY, cxPtr4);
				cx4 = refBitmap.nextPixel(cxPtr4);

				// set up the typical prediction context
				tpgrCX0 = tpgrCX1 = tpgrCX2 = 0; // make gcc happy
				if (tpgrOn) {
					refBitmap.getPixelPtr(-1-refDX, y-1-refDY, tpgrCXPtr0);
					tpgrCX0 = refBitmap.nextPixel(tpgrCXPtr0);
					tpgrCX0 = (tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0);
					tpgrCX0 = (tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0);
					refBitmap.getPixelPtr(-1-refDX, y-refDY, tpgrCXPtr1);
					tpgrCX1 = refBitmap.nextPixel(tpgrCXPtr1);
					tpgrCX1 = (tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1);
					tpgrCX1 = (tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1);
					refBitmap.getPixelPtr(-1-refDX, y+1-refDY, tpgrCXPtr2);
					tpgrCX2 = refBitmap.nextPixel(tpgrCXPtr2);
					tpgrCX2 = (tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2);
					tpgrCX2 = (tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2);
				}

				for (x = 0; x < w; ++x) {

					// update the context
					cx0 = ((cx0 << 1) | bitmap.nextPixel(cxPtr0)) & 7;
					cx3 = ((cx3 << 1) | refBitmap.nextPixel(cxPtr3)) & 7;
					cx4 = ((cx4 << 1) | refBitmap.nextPixel(cxPtr4)) & 3;

					if (tpgrOn) {
						// update the typical predictor context
						tpgrCX0 = ((tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0)) & 7;
						tpgrCX1 = ((tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1)) & 7;
						tpgrCX2 = ((tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2)) & 7;

						// check for a "typical" pixel
						if (arithDecoder.decodeBit(ltpCX, refinementRegionStats)!=0) {
							ltp = !ltp;
						}
						if (tpgrCX0 == 0 && tpgrCX1 == 0 && tpgrCX2 == 0) {
							bitmap.clearPixel(x, y);
							continue;
						} else if (tpgrCX0 == 7 && tpgrCX1 == 7 && tpgrCX2 == 7) {
							bitmap.setPixel(x, y);
							continue;
						}
					}

					// build the context
					cx = (cx0 << 7) | (bitmap.nextPixel(cxPtr1) << 6) |
					(refBitmap.nextPixel(cxPtr2) << 5) |
					(cx3 << 2) | cx4;

					// decode the pixel
					if ((pix = arithDecoder.decodeBit(cx, refinementRegionStats))!=0) {
						bitmap.setPixel(x, y);
					}
				}

			} else {

				// set up the context
				bitmap.getPixelPtr(0, y-1, cxPtr0);
				cx0 = bitmap.nextPixel(cxPtr0);
				bitmap.getPixelPtr(-1, y, cxPtr1);
				refBitmap.getPixelPtr(-refDX, y-1-refDY, cxPtr2);
				cx2 = refBitmap.nextPixel(cxPtr2);
				refBitmap.getPixelPtr(-1-refDX, y-refDY, cxPtr3);
				cx3 = refBitmap.nextPixel(cxPtr3);
				cx3 = (cx3 << 1) | refBitmap.nextPixel(cxPtr3);
				refBitmap.getPixelPtr(-1-refDX, y+1-refDY, cxPtr4);
				cx4 = refBitmap.nextPixel(cxPtr4);
				cx4 = (cx4 << 1) | refBitmap.nextPixel(cxPtr4);
				bitmap.getPixelPtr(atx[0], y+aty[0], cxPtr5);
				refBitmap.getPixelPtr(atx[1]-refDX, y+aty[1]-refDY, cxPtr6);

				// set up the typical prediction context
				tpgrCX0 = tpgrCX1 = tpgrCX2 = 0; // make gcc happy
				if (tpgrOn) {
					refBitmap.getPixelPtr(-1-refDX, y-1-refDY, tpgrCXPtr0);
					tpgrCX0 = refBitmap.nextPixel(tpgrCXPtr0);
					tpgrCX0 = (tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0);
					tpgrCX0 = (tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0);
					refBitmap.getPixelPtr(-1-refDX, y-refDY, tpgrCXPtr1);
					tpgrCX1 = refBitmap.nextPixel(tpgrCXPtr1);
					tpgrCX1 = (tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1);
					tpgrCX1 = (tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1);
					refBitmap.getPixelPtr(-1-refDX, y+1-refDY, tpgrCXPtr2);
					tpgrCX2 = refBitmap.nextPixel(tpgrCXPtr2);
					tpgrCX2 = (tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2);
					tpgrCX2 = (tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2);
				}

				for (x = 0; x < w; ++x) {

					// update the context
					cx0 = ((cx0 << 1) | bitmap.nextPixel(cxPtr0)) & 3;
					cx2 = ((cx2 << 1) | refBitmap.nextPixel(cxPtr2)) & 3;
					cx3 = ((cx3 << 1) | refBitmap.nextPixel(cxPtr3)) & 7;
					cx4 = ((cx4 << 1) | refBitmap.nextPixel(cxPtr4)) & 7;

					if (tpgrOn) {
						// update the typical predictor context
						tpgrCX0 = ((tpgrCX0 << 1) | refBitmap.nextPixel(tpgrCXPtr0)) & 7;
						tpgrCX1 = ((tpgrCX1 << 1) | refBitmap.nextPixel(tpgrCXPtr1)) & 7;
						tpgrCX2 = ((tpgrCX2 << 1) | refBitmap.nextPixel(tpgrCXPtr2)) & 7;

						// check for a "typical" pixel
						if (arithDecoder.decodeBit(ltpCX, refinementRegionStats)!=0) {
							ltp = !ltp;
						}
						if (tpgrCX0 == 0 && tpgrCX1 == 0 && tpgrCX2 == 0) {
							bitmap.clearPixel(x, y);
							continue;
						} else if (tpgrCX0 == 7 && tpgrCX1 == 7 && tpgrCX2 == 7) {
							bitmap.setPixel(x, y);
							continue;
						}
					}

					// build the context
					cx = (cx0 << 11) | (bitmap.nextPixel(cxPtr1) << 10) |
					(cx2 << 8) | (cx3 << 5) | (cx4 << 2) |
					(bitmap.nextPixel(cxPtr5) << 1) |
					refBitmap.nextPixel(cxPtr6);

					// decode the pixel
					if ((pix = arithDecoder.decodeBit(cx, refinementRegionStats))!=0) {
						bitmap.setPixel(x, y);
					}
				}
			}
		}

		return bitmap;
	}

	public void readPageInfoSeg(int length) throws IOException {
		int xRes=0, yRes=0, flags=0, striping=0;
		
		if (!readULong(pageW) || !readULong(pageH) ||
				!readULong(xRes) || !readULong(yRes) ||
				!readUByte(flags) || !readUWord(striping)) {
			System.out.println("EOF Error");
		}
		pageDefPixel = (flags >> 2) & 1;
		defCombOp = (flags >> 3) & 3;

		// allocate the page bitmap
		if (pageH == 0xffffffff) {
			curPageH = striping & 0x7fff;
		} else {
			curPageH = pageH;
		}
		pageBitmap = new JBIG2Bitmap(0, pageW, curPageH);

		// default pixel value
		if (pageDefPixel!=0) {
			pageBitmap.clearToOne();
		} else {
			pageBitmap.clearToZero();
		}

		return;
	}

	public void readEndOfStripeSeg(int length) throws IOException {
		int i;

		// skip the segment
		for (i = 0; i < length; ++i) {
			curStr.read();
		}
	}

	public void readProfilesSeg(int length) throws IOException {
		int i;

		// skip the segment
		for (i = 0; i < length; ++i) {
			curStr.read();
		}
	}

	public void readCodeTableSeg(int segNum, int length) throws IOException {
		HuffmanTables[] huffTab=null;
		int flags=0, oob=0, prefixBits=0, rangeBits=0;
		int lowVal=0, highVal=0, val=0;
		int huffTabSize=0, i=0;

		if (!readUByte(flags) || !readLong(lowVal) || !readLong(highVal)) {
			System.out.println("EOF ERROR");
		}
		oob = flags & 1;
		prefixBits = ((flags >> 1) & 7) + 1;
		rangeBits = ((flags >> 4) & 7) + 1;

		huffDecoder.reset();
		huffTabSize = 8;
		huffTab = new HuffmanTables[huffTabSize];
		i = 0;
		val = lowVal;
		while (val < highVal) {
			if (i == huffTabSize) {
				huffTabSize *= 2;
				huffTab = new HuffmanTables[huffTabSize];
			}
			huffTab[i].val = val;
			huffTab[i].prefixLen = huffDecoder.readBits(prefixBits);
			huffTab[i].rangeLen = huffDecoder.readBits(rangeBits);
			val += 1 << huffTab[i].rangeLen;
			++i;
		}
		if (i + oob + 3 > huffTabSize) {
			huffTabSize = i + oob + 3;
			huffTab = new HuffmanTables[huffTabSize];
		}
		huffTab[i].val = lowVal - 1;
		huffTab[i].prefixLen = huffDecoder.readBits(prefixBits);
		huffTab[i].rangeLen = huffDecoder.jbig2HuffmanLOW;
		++i;
		huffTab[i].val = highVal;
		huffTab[i].prefixLen = huffDecoder.readBits(prefixBits);
		huffTab[i].rangeLen = 32;
		++i;
		if (oob!=0) {
			huffTab[i].val = 0;
			huffTab[i].prefixLen = huffDecoder.readBits(prefixBits);
			huffTab[i].rangeLen = huffDecoder.jbig2HuffmanOOB;
			++i;
		}
		huffTab[i].val = 0;
		huffTab[i].prefixLen = 0;
		huffTab[i].rangeLen = huffDecoder.jbig2HuffmanEOT;
		huffDecoder.buildTable(huffTab, i);

		// create and store the new table segment
		segments.add(new JBIG2CodeTable(segNum, huffTab[i]));

		return;
	}

	public void readExtensionSeg(int length) throws IOException {
		int i;

		// skip the segment
		for (i = 0; i < length; ++i) {
			curStr.read();
		}
	}

	public JBIG2Segment findSegment(int segNum) {
		JBIG2Segment seg;
		int i;

		for (i = 0; i < globalSegments.size(); ++i) {
			seg = (JBIG2Segment)globalSegments.get(i);
			if (seg.getSegNum() == segNum) {
				return seg;
			}
		}
		for (i = 0; i < segments.size(); ++i) {
			seg = (JBIG2Segment)segments.get(i);
			if (seg.getSegNum() == segNum) {
				return seg;
			}
		}
		return null;
	}

	public void discardSegment(int segNum) {
		JBIG2Segment seg;
		int i;

		for (i = 0; i < globalSegments.size(); ++i) {
			seg = (JBIG2Segment)globalSegments.get(i);
			if (seg.getSegNum() == segNum) {
				globalSegments.remove(i);
				return;
			}
		}
		for (i = 0; i < segments.size(); ++i) {
			seg = (JBIG2Segment)segments.get(i);
			if (seg.getSegNum() == segNum) {
				segments.remove(i);
				return;
			}
		}
	}

	public void resetGenericStats(int templ,
			ArithmeticDecoderStats prevStats) {
		int size;

		size = huffDecoder.contextSize[templ];
		if (prevStats!=null && prevStats.getContextSize() == size) {
			if (genericRegionStats.getContextSize() == size) {
				genericRegionStats.copyFrom(prevStats);
			} else {
				genericRegionStats=null;
				genericRegionStats = prevStats.copy();
			}
		} else {
			if (genericRegionStats.getContextSize() == size) {
				genericRegionStats.reset();
			} else {
				genericRegionStats=null;
				genericRegionStats = new ArithmeticDecoderStats(1 << size);
			}
		}
	}

	public void resetRefinementStats(int templ,
			ArithmeticDecoderStats prevStats) {
		int size;

		size = huffDecoder.refContextSize[templ];
		if (prevStats!=null && prevStats.getContextSize() == size) {
			if (refinementRegionStats.getContextSize() == size) {
				refinementRegionStats.copyFrom(prevStats);
			} else {
				refinementRegionStats=null;
				refinementRegionStats = prevStats.copy();
			}
		} else {
			if (refinementRegionStats.getContextSize() == size) {
				refinementRegionStats.reset();
			} else {
				refinementRegionStats=null;
				refinementRegionStats = new ArithmeticDecoderStats(1 << size);
			}
		}
	}

	public void resetIntStats(int symCodeLen) {
		iadhStats.reset();
		iadwStats.reset();
		iaexStats.reset();
		iaaiStats.reset();
		iadtStats.reset();
		iaitStats.reset();
		iafsStats.reset();
		iadsStats.reset();
		iardxStats.reset();
		iardyStats.reset();
		iardwStats.reset();
		iardhStats.reset();
		iariStats.reset();
		if (iaidStats.getContextSize() == symCodeLen + 1) {
			iaidStats.reset();
		} else {
			iaidStats=null;
			iaidStats = new ArithmeticDecoderStats(1 << (symCodeLen + 1));
		}
	}

	public boolean readUByte(int x) throws IOException {
		int c0;

		if ((c0 = curStr.read()) == EOF) {
			return false;
		}
		x = (int)c0;
		return true;
	}

	public boolean readByte(int x) throws IOException {
		int c0;

		if ((c0 = curStr.read()) == EOF) {
			return false;
		}
		x = c0;
		if ((c0 & 0x80)!=0) {
			x |= -1 - 0xff;
		}
		return true;
	}

	public boolean readUWord(int x) throws IOException {
		int c0, c1;

		if ((c0 = curStr.read()) == EOF ||
				(c1 = curStr.read()) == EOF) {
			return false;
		}
		x = (int)((c0 << 8) | c1);
		return true;
	}

	public boolean readULong(int x) throws IOException {
		int c0, c1, c2, c3;
		
		System.out.println("Begining of readULong");
		
		if ((c0 = curStr.read()) == EOF ||
				(c1 = curStr.read()) == EOF ||
				(c2 = curStr.read()) == EOF ||
				(c3 = curStr.read()) == EOF) {
			System.out.println("FALSE AND EXIT");
			return false;
		}
		System.out.println("TRUE AND CONTINUE");
		x = (int)((c0 << 24) | (c1 << 16) | (c2 << 8) | c3);
		return true;
	}

	public boolean readLong(int x) throws IOException {
		int c0, c1, c2, c3;

		if ((c0 = curStr.read()) == EOF ||
				(c1 = curStr.read()) == EOF ||
				(c2 = curStr.read()) == EOF ||
				(c3 = curStr.read()) == EOF) {
			return false;
		}
		x = ((c0 << 24) | (c1 << 16) | (c2 << 8) | c3);
		if ((c0 & 0x80)!=0){
			x |= -1 - (int)0xffffffff;
		}
		return true;
	}
}
