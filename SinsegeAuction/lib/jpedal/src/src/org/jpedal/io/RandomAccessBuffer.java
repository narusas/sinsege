/*
* ---------------
* RandomAccessBuffer.java
* ---------------
*/

package org.jpedal.io;

import java.io.IOException;

/**
 * Common interface for classes allowing random access to the data they
 * provide. Existing methods conform to those that are found in
 * java.io.RandomAccessFile. Implementation is still incomplete: Only the
 * methods needed by JPedal are available right now.
 *
 * @author Christian Graefe
 */
public interface RandomAccessBuffer {

  public long getFilePointer() throws IOException;
  public void seek(long pos) throws IOException;
  public int read() throws IOException;
  public String readLine() throws IOException;
  public long length() throws IOException;
  public void close() throws IOException;
  public int read(byte[] b) throws IOException;
  public byte[] getPdfBuffer();
}