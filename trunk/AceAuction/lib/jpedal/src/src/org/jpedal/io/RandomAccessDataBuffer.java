/*
* ---------------
* RandomAccessBuffer.java
* ---------------
*/

package org.jpedal.io;

import java.io.IOException;

/**
 * <p>This class emulates part of the java.io.RandomAccessFile functionality
 * which is provided via the RandomAccessBuffer interface. All data
 * originates from a byte[] array instead of a "normal" file on disk.
 *
 * <p>All methods behave like their counterparts in java.io.RandomAccessFile.
 *
 * <p><b>Note: This class is NOT thread-safe!</b>
 *
 * @author Christian Graefe
 */
public class RandomAccessDataBuffer implements RandomAccessBuffer {

  private byte[] data;
  private long pointer;

  public RandomAccessDataBuffer(byte[] data)
  {
    this.data = data;
    this.pointer = -1;
  }

  public long getFilePointer() throws IOException {
    return pointer;
  }

  public void seek(long pos) throws IOException {
    if ( checkPos(pos) ) {
      this.pointer = pos;
    } else {
      throw new IOException("Position out of bounds");
    }
  }

  public void close() throws IOException {
    this.data = null;
    this.pointer = -1;
  }

  public long length() throws IOException {
  
    if (data!=null) {
      return data.length;
    } else {
      throw new IOException("Data buffer not initialized.");
    }
  }

  public int read() throws IOException {
    if (checkPos(this.pointer)) {
      return b2i(this.data[(int)pointer++]);
    } else {
      return -1;
    }
  }

  private int peek() throws IOException {
    if (checkPos(this.pointer)) {
      return b2i(this.data[(int)pointer]);
    } else {
      return -1;
    }
  }

  /**
   * return next line (returns null if no line)
   */
  public String readLine() throws IOException {

        if (this.pointer >= this.data.length - 1) {
            return null;
        } else {

            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = read()) >= 0) {
                if ((c == 10) || (c == 13)) {
                    if (((peek() == 10) || (peek() == 13)) && (peek() != c))
                        read();
                    break;
                }
                buf.append((char) c);
            }
            return buf.toString();
        }
    }

  public int read(byte[] b) throws IOException {
    if (data==null) throw new IOException("Data buffer not initialized.");
    if (pointer<0 || pointer>=data.length) return -1;
    final int length=Math.min(b.length, data.length-(int)pointer);
    for (int i=0; i<length; i++) {
      b[i] = data[ (int)pointer++ ];
    }
    return length;
  }

  private static final int b2i(byte b) {
    if (b>=0) return b;
    return 256+b;
  }

  private boolean checkPos(long pos) throws IOException {
    return ( (pos>=0) && (pos<length()) );
  }

/* returns the byte data*/
public byte[] getPdfBuffer(){
	return data;
}
}

