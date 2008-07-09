package org.jpedal.io;

import java.io.BufferedInputStream;

public class MMRDecoder {

	public CCITT CC = new CCITT((short)0,(short)0);

	private int buf;
	private int bufLen;
	private byte data[]; //Input from JBIG as single value, all methods must be called in PDFfilteredReader.java
	private int nBytesRead;
	private int position = 0;
	BufferedInputStream str;

	public void setStream(BufferedInputStream str) {
		System.out.println("TRACER :: MMRDecoder.setStream");
		this.str = str;
	}

	public MMRDecoder(){
		System.out.println("MMR Decoder");
	}

	/*
	 * JBIG2MMR Decoder Code
	 */
	public void MMRreset() {
		buf = 0;
		bufLen = 0;
		nBytesRead = 0;
	}

	public int get2DCode() {
		CCITT p = new CCITT((short)0,(short)0);

		if (bufLen == 0) {
			buf = data[position++] & 0xff;
			bufLen = 8;
			++nBytesRead;
			p = p.getTwoDimTab1()[(buf >> 1) & 0x7f];
		} else if (bufLen == 8) {
			p = p.getTwoDimTab1()[(buf >> 1) & 0x7f];
		} else {
			p = p.getTwoDimTab1()[(buf << (7 - bufLen)) & 0x7f];
			if (p.bits < 0 || p.bits > (int)bufLen) {
				buf = (buf << 8) | (data[position++] & 0xff);
				bufLen += 8;
				++nBytesRead;
				p = p.getTwoDimTab1()[(buf >> (bufLen - 7)) & 0x7f];
			}
		}
		if (p.bits < 0) {
			System.err.println("Bad two dim code in JBIG2 MMR stream");
			return 0;
		}
		bufLen -= p.bits;
		return p.n;
	}

	public int getWhiteCode() {
		CCITT p = new CCITT((short)0,(short)0);
		int code;

		if (bufLen == 0) {
			buf = data[position++] & 0xff;
			bufLen = 8;
			++nBytesRead;
		}
		while (true) {
			if (bufLen >= 7 && ((buf >> (bufLen - 7)) & 0x7f) == 0) {
				if (bufLen <= 12) {
					code = buf << (12 - bufLen);
				} else {
					code = buf >> (bufLen - 12);
				}
				p = p.getWhiteTab1()[code & 0x1f];
			} else {
				if (bufLen <= 9) {
					code = buf << (9 - bufLen);
				} else {
					code = buf >> (bufLen - 9);
				}
				p = p.getWhiteTab2()[code & 0x1ff];
			}
			if (p.bits > 0 && p.bits <= (int)bufLen) {
				bufLen -= p.bits;
				return p.n;
			}
			if (bufLen >= 12) {
				break;
			}
			buf = (buf << 8) | (data[position++] & 0xff);
			bufLen += 8;
			++nBytesRead;
		}
		System.err.println("Bad white code in JBIG2 MMR stream");
		// eat a bit and return a positive number so that the caller doesn't
		// go into an infinite loop
		--bufLen;
		return 1;
	}

	public int getBlackCode() {
		CCITT p = new CCITT((short)0,(short)0);
		int code;

		if (bufLen == 0) {
			buf = data[position++] & 0xff;
			bufLen = 8;
			++nBytesRead;
		}
		while (true) {
			if (bufLen >= 6 && ((buf >> (bufLen - 6)) & 0x3f) == 0) {
				if (bufLen <= 13) {
					code = buf << (13 - bufLen);
				} else {
					code = buf >> (bufLen - 13);
				}
				p = p.getBlackTab1()[code & 0x7f];
			} else if (bufLen >= 4 && ((buf >> (bufLen - 4)) & 0x0f) == 0) {
				if (bufLen <= 12) {
					code = buf << (12 - bufLen);
				} else {
					code = buf >> (bufLen - 12);
				}
				p = p.getBlackTab2()[(code & 0xff) - 64];
			} else {
				if (bufLen <= 6) {
					code = buf << (6 - bufLen);
				} else {
					code = buf >> (bufLen - 6);
				}
				p = p.getBlackTab3()[code & 0x3f];
			}
			if (p.bits > 0 && p.bits <= (int)bufLen) {
				bufLen -= p.bits;
				return p.n;
			}
			if (bufLen >= 13) {
				break;
			}
			buf = (buf << 8) | (data[position++] & 0xff);
			bufLen += 8;
			++nBytesRead;
		}
		System.err.println("Bad black code in JBIG2 MMR stream");
		// eat a bit and return a positive number so that the caller doesn't
		// go into an infinite loop
		--bufLen;
		return 1;
	}

	public int get24Bits() {
		while (bufLen < 24) {
			buf = (buf << 8) | (data[position++] & 0xff);
			bufLen += 8;
			++nBytesRead;
		}
		return (buf >> (bufLen - 24)) & 0xffffff;
	}

	//MAYBE NEED TO DROP THIS BY ONE
	public void skipTo(int length) {
		position++;
	}
}
