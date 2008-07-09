package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ArithmeticDecoder {
//	------------------------------------------------------------------------
//	ArithmeticDecoder
//	------------------------------------------------------------------------

	int buf0, buf1;
	int c, a;
	int ct;
	int point=0;

	int prev;			// for the integer decoder

	BufferedInputStream str;   //Stream *str;
	
	int dataLen;
	boolean limitStream;

	int[] qeTab = {
			0x56010000, 0x34010000, 0x18010000, 0x0AC10000,
			0x05210000, 0x02210000, 0x56010000, 0x54010000,
			0x48010000, 0x38010000, 0x30010000, 0x24010000,
			0x1C010000, 0x16010000, 0x56010000, 0x54010000,
			0x51010000, 0x48010000, 0x38010000, 0x34010000,
			0x30010000, 0x28010000, 0x24010000, 0x22010000,
			0x1C010000, 0x18010000, 0x16010000, 0x14010000,
			0x12010000, 0x11010000, 0x0AC10000, 0x09C10000,
			0x08A10000, 0x05210000, 0x04410000, 0x02A10000,
			0x02210000, 0x01410000, 0x01110000, 0x00850000,
			0x00490000, 0x00250000, 0x00150000, 0x00090000,
			0x00050000, 0x00010000, 0x56010000
	};

	int[] nmpsTab = {
			1,  2,  3,  4,  5, 38,  7,  8,  9, 10, 11, 12, 13, 29, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
			33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 45, 46
	};

	int[] nlpsTab = {
			1,  6,  9, 12, 29, 33,  6, 14, 14, 14, 17, 18, 20, 21, 14, 14,
			15, 16, 17, 18, 19, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
			30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46
	};

	int[] switchTab = {
			1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};

	public ArithmeticDecoder() {
		str = null;
		dataLen = 0;
		limitStream = false;
	}

	int readByte() {
		if (limitStream) {
			--dataLen;
			if (dataLen < 0) {
				return 0xff;
			}
		}
		//NEED OR CHECK THIS TO MOVE ALONG ARRAY	  
		//point++;
		int result = 0xff;
		try {
			result= (int)str.read() & 0xff;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	void start() {
		buf0 = readByte();
		buf1 = readByte();

		// INITDEC
		c = (buf0 ^ 0xff) << 16;
		byteIn();
		c <<= 7;
		ct -= 7;
		a = 0x80000000;
	}

	void restart(int dataLenA) {
		int oldDataLen;

		oldDataLen = dataLen;
		dataLen = dataLenA;
		if (oldDataLen == -1) {
			buf1 = readByte();
		} else if (oldDataLen <= -2) {
			buf0 = readByte();
			buf1 = readByte();
		}
	}

	void cleanup() {
		if (limitStream) {
			while (dataLen > 0) {
				buf0 = buf1;
				buf1 = readByte();
			}
		}
	}

	int decodeBit(int context,
			ArithmeticDecoderStats stats) {
		int bit;
		int qe;
		int iCX, mpsCX;

		iCX = stats.cxTab[context] >> 1;
		mpsCX = stats.cxTab[context] & 1;
		qe = qeTab[iCX];
		a -= qe;
		if (c < a) {
			if (a == 0x80000000) {
				bit = mpsCX;
			} else {
				// MPS_EXCHANGE
				if (a < qe) {
					bit = 1 - mpsCX;
					if (switchTab[iCX]!=-1) {
						stats.cxTab[context] = (char) ((nlpsTab[iCX] << 1) | (1 - mpsCX));
					} else {
						stats.cxTab[context] = (char) ((nlpsTab[iCX] << 1) | mpsCX);
					}
				} else {
					bit = mpsCX;
					stats.cxTab[context] = (char) ((nmpsTab[iCX] << 1) | mpsCX);
				}
				// RENORMD
				do {
					if (ct == 0) {
						byteIn();
					}
					a <<= 1;
					c <<= 1;
					--ct;
				} while ((a != 0x80000000));
			}
		} else {
			c -= a;
			// LPS_EXCHANGE
			if (a < qe) {
				bit = mpsCX;
				stats.cxTab[context] = (char) ((nmpsTab[iCX] << 1) | mpsCX);
			} else {
				bit = 1 - mpsCX;
				if (switchTab[iCX]!=-1) {
					stats.cxTab[context] = (char) ((nlpsTab[iCX] << 1) | (1 - mpsCX));
				} else {
					stats.cxTab[context] = (char) ((nlpsTab[iCX] << 1) | mpsCX);
				}
			}
			a = qe;
			// RENORMD
			do {
				if (ct == 0) {
					byteIn();
				}
				a <<= 1;
				c <<= 1;
				--ct;
			}while ((a != 0x80000000));
		}
		return bit;
	}

	int decodeByte(int context,
			ArithmeticDecoderStats stats) {
		int mByte;
		int i;

		mByte = 0;
		for (i = 0; i < 8; ++i) {
			mByte = (mByte << 1) | decodeBit(context, stats);
		}
		return mByte;
	}

	boolean decodeInt(int x, ArithmeticDecoderStats stats) {
		int s;
		int v;
		int i;

		prev = 1;
		s = decodeIntBit(stats);
		if (decodeIntBit(stats)!=-1) {
			if (decodeIntBit(stats)!=-1) {
				if (decodeIntBit(stats)!=-1) {
					if (decodeIntBit(stats)!=-1) {
						if (decodeIntBit(stats)!=-1) {
							v = 0;
							for (i = 0; i < 32; ++i) {
								v = (v << 1) | decodeIntBit(stats);
							}
							v += 4436;
						} else {
							v = 0;
							for (i = 0; i < 12; ++i) {
								v = (v << 1) | decodeIntBit(stats);
							}
							v += 340;
						}
					} else {
						v = 0;
						for (i = 0; i < 8; ++i) {
							v = (v << 1) | decodeIntBit(stats);
						}
						v += 84;
					}
				} else {
					v = 0;
					for (i = 0; i < 6; ++i) {
						v = (v << 1) | decodeIntBit(stats);
					}
					v += 20;
				}
			} else {
				v = decodeIntBit(stats);
				v = (v << 1) | decodeIntBit(stats);
				v = (v << 1) | decodeIntBit(stats);
				v = (v << 1) | decodeIntBit(stats);
				v += 4;
			}
		} else {
			v = decodeIntBit(stats);
			v = (v << 1) | decodeIntBit(stats);
		}

		if(s!=-1) {
			if (v == 0) {
				return false;
			}
			x = -(int)v;
		} else {
			x = (int)v;
		}
		return true;
	}

	int decodeIntBit(ArithmeticDecoderStats stats) {
		int bit;

		bit = decodeBit(prev, stats);
		if (prev < 0x100) {
			prev = (prev << 1) | bit;
		} else {
			prev = (((prev << 1) | bit) & 0x1ff) | 0x100;
		}
		return bit;
	}

	int decodeIAID(int codeLen,
			ArithmeticDecoderStats stats) {
		int i;
		int bit;

		prev = 1;
		for (i = 0; i < codeLen; ++i) {
			bit = decodeBit(prev, stats);
			prev = (prev << 1) | bit;
		}
		return prev - (1 << codeLen);
	}

	void byteIn() {
		if (buf0 == 0xff) {
			if (buf1 > 0x8f) {
				ct = 8;
			} else {
				buf0 = buf1;
				buf1 = readByte();
				c = c + 0xfe00 - (buf0 << 9);
				ct = 7;
			}
		} else {
			buf0 = buf1;
			buf1 = readByte();
			c = c + 0xff00 - (buf0 << 8);
			ct = 8;
		}
	}

	public void setStream(BufferedInputStream str) {
		System.out.println("TRACER :: ArithmeticDecoder.setStream");
		this.str = str;
	}

}