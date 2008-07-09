package org.jpedal.io;

public class JBIG2Bitmap implements JBIG2Segment{
	private int w;
	private int h;
	private int line;
	private byte dataA[];
	private byte data;
	private int segNum;
	private char p;
	private int shift;
	private int x;

	public JBIG2Bitmap(int segNumA, int wA, int hA){
		w=wA;
		h=hA;
		line=(wA + 7) >> 3;
		// need to allocate one extra guard byte for use in combine()
		dataA[h * line] = 0;
		segNum=segNumA;
	}

	public JBIG2Bitmap(int segNumA, JBIG2Bitmap bitmap)
	{
		w = bitmap.w;
		h = bitmap.h;
		line = bitmap.line;
		// need to allocate one extra guard byte for use in combine()
		//memcpy(data, bitmap.data, h * line);
		dataA[h * line] = 0;
	}

	public JBIG2Bitmap getSlice(int x, int y, int wA, int hA) {
		JBIG2Bitmap slice;
		int xx, yy;

		slice = new JBIG2Bitmap(0, wA, hA);
		slice.clearToZero();
		for (yy = 0; yy < hA; ++yy) {
			for (xx = 0; xx < wA; ++xx) {
				if (getPixel(x + xx, y + yy)<=0) {
					slice.setPixel(xx, yy);
				}
			}
		}
		return slice;
	}

	public void setSegNum(int segNumA){
		this.segNum = segNumA;
	}
	
	public void expand(int newH, int pixel) {
		if (newH <= h) {
			return;
		}
		// need to allocate one extra guard byte for use in combine()
		//data = (Guchar *)grealloc(data, newH * line + 1);
		if (pixel<=0) {
		//	memset(data + h * line, 0xff, (newH - h) * line);
		} else {
		//	memset(data + h * line, 0x00, (newH - h) * line);
		}
		h = newH;
		dataA[h * line] = 0;
	}

	public void clearToZero() {
		//memset(data, 0, h * line);
	}

	public void clearToOne() {
		//memset(data, 0xff, h * line);
	}

	public void getPixelPtr(int x, int y, JBIG2Bitmap ptr) {
		if (y < 0 || y >= h || x >= w) {
			ptr.p = 0;
		} else if (x < 0) {
			ptr.p = (char)dataA[y * line];
			ptr.shift = 7;
			ptr.x = x;
		} else {
			ptr.p = (char)dataA[y * line + (x >> 3)];
			ptr.shift = 7 - (x & 7);
			ptr.x = x;
		}
	}

	public int getWidth() { return w; }
	public int getHeight() { return h; }
	public int getPixel(int x, int y){ 
		return (x < 0 || x >= w || y < 0 || y >= h) ? 0 :
			(dataA[y * line + (x >> 3)] >> (7 - (x & 7))) & 1; }
	public void setPixel(int x, int y)
	{ dataA[y * line + (x >> 3)] |= 1 << (7 - (x & 7)); }
	public void clearPixel(int x, int y)
	{ dataA[y * line + (x >> 3)] &= 0x7f7f >> (x & 7); }

	public int nextPixel(JBIG2Bitmap ptr) {
		int pix;
//ADDED "==0" TO LET COMPILE, REMEMBER TO REMOVE
		if (ptr.p==0) {
			pix = 0;
		} else if (ptr.x < 0) {
			ptr.x++;
			pix = 0;
		} else {
			pix = (ptr.p >> ptr.shift) & 1;
			if (ptr.x++ == w) {
				ptr.p = (char)0;
			} else if (ptr.shift == 0) {
				ptr.p++;
				ptr.shift = 7;
			} else {
				ptr.shift--;
			}
		}
		return pix;
	}

	public void duplicateRow(int yDest, int ySrc) {
		//memcpy(data + yDest * line, data + ySrc * line, line);
	}


	public void combine(JBIG2Bitmap bitmap, int x, int y,
			int combOp) {
		int x0, x1, y0, y1, xx, yy;
		char srcPtr, destPtr;
		int src0, src1, src, dest, s1, s2, m1, m2, m3;
		boolean oneByte;

		if (y < 0) {
			y0 = -y;
		} else {
			y0 = 0;
		}
		if (y + bitmap.h > h) {
			y1 = h - y;
		} else {
			y1 = bitmap.h;
		}
		if (y0 >= y1) {
			return;
		}

		if (x >= 0) {
			x0 = x & ~7;
		} else {
			x0 = 0;
		}
		x1 = x + bitmap.w;
		if (x1 > w) {
			x1 = w;
		}
		if (x0 >= x1) {
			return;
		}

		s1 = x & 7;
		s2 = 8 - s1;
		m1 = 0xff >> (x1 & 7);
		m2 = 0xff << (((x1 & 7) == 0) ? 0 : 8 - (x1 & 7));
		m3 = (0xff >> s1) & m2;

		oneByte = x0 == ((x1 - 1) & ~7);

		for (yy = y0; yy < y1; ++yy) {

			// one byte per line -- need to mask both left and right side
			if (oneByte) {
				if (x >= 0) {
					destPtr = (char) (data + (y + yy) * line + (x >> 3));
					srcPtr = (char) (bitmap.data + yy * bitmap.line);
					dest = destPtr;
					src1 = srcPtr;
					switch (combOp) {
					case 0: // or
						dest |= (src1 >> s1) & m2;
						break;
					case 1: // and
						dest &= ((0xff00 | src1) >> s1) | m1;
						break;
					case 2: // xor
						dest ^= (src1 >> s1) & m2;
						break;
					case 3: // xnor
						dest ^= ((src1 ^ 0xff) >> s1) & m2;
						break;
					case 4: // replace
						dest = (dest & ~m3) | ((src1 >> s1) & m3);
						break;
					}
					destPtr = (char)dest;
				} else {
					destPtr = (char) (data + (y + yy) * line);
					srcPtr = (char) (bitmap.data + yy * bitmap.line + (-x >> 3));
					dest = destPtr;
					src1 = srcPtr;
					switch (combOp) {
					case 0: // or
						dest |= src1 & m2;
						break;
					case 1: // and
						dest &= src1 | m1;
						break;
					case 2: // xor
						dest ^= src1 & m2;
						break;
					case 3: // xnor
						dest ^= (src1 ^ 0xff) & m2;
						break;
					case 4: // replace
						dest = (src1 & m2) | (dest & m1);
						break;
					}
					destPtr = (char)dest;
				}

				// multiple bytes per line -- need to mask left side of left-most
				// byte and right side of right-most byte
			} else {

				// left-most byte
				if (x >= 0) {
					destPtr = (char) (data + (y + yy) * line + (x >> 3));
					srcPtr = (char) (bitmap.data + yy * bitmap.line);
					src1 = srcPtr++;
					dest = destPtr;
					switch (combOp) {
					case 0: // or
						dest |= src1 >> s1;
						break;
					case 1: // and
						dest &= (0xff00 | src1) >> s1;
						break;
					case 2: // xor
						dest ^= src1 >> s1;
						break;
					case 3: // xnor
						dest ^= (src1 ^ 0xff) >> s1;
						break;
					case 4: // replace
						dest = (dest & (0xff << s2)) | (src1 >> s1);
						break;
					}
					destPtr++;
					destPtr = (char) dest;
					xx = x0 + 8;
				} else {
					destPtr = (char) (data + (y + yy) * line);
					srcPtr = (char) (bitmap.data + yy * bitmap.line + (-x >> 3));
					src1 = srcPtr++;
					xx = x0;
				}

				// middle bytes
				for (; xx < x1 - 8; xx += 8) {
					dest = destPtr;
					src0 = src1;
					src1 = srcPtr++;
					src = (((src0 << 8) | src1) >> s1) & 0xff;
					switch (combOp) {
					case 0: // or
						dest |= src;
						break;
					case 1: // and
						dest &= src;
						break;
					case 2: // xor
						dest ^= src;
						break;
					case 3: // xnor
						dest ^= src ^ 0xff;
						break;
					case 4: // replace
						dest = src;
						break;
					}
					destPtr++;
					destPtr = (char) dest;
				}

				// right-most byte
				// note: this last byte (src1) may not actually be used, depending
				// on the values of s1, m1, and m2 - and in fact, it may be off
				// the edge of the source bitmap, which means we need to allocate
				// one extra guard byte at the end of each bitmap
				dest = destPtr;
				src0 = src1;
				src1 = srcPtr++;
				src = (((src0 << 8) | src1) >> s1) & 0xff;
				switch (combOp) {
				case 0: // or
					dest |= src & m2;
					break;
				case 1: // and
					dest &= src | m1;
					break;
				case 2: // xor
					dest ^= src & m2;
					break;
				case 3: // xnor
					dest ^= (src ^ 0xff) & m2;
					break;
				case 4: // replace
					dest = (src & m2) | (dest & m1);
					break;
				}
				destPtr = (char) dest;
			}
		}
	}
    int type=4;
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
