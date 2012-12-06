package net.narusas.si.auction.app.attestedDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.PDFOperator;


public class AttestedDateChanger {
	private File attestedPDFFile;
	private static Stack<PDResources> streamResourcesStack = new Stack<PDResources>();
	private static PDFont font;
	private static CMap cmap;

	public AttestedDateChanger(File attestedPDFFile) {
		this.attestedPDFFile = attestedPDFFile;
	}

	public void convertTo(File saveFile) {
		try {
			Calendar calender = Calendar.getInstance();
			calender.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
			Date date = calender.getTime();
			String dateString = new SimpleDateFormat("yyyy년MM월dd일 HH시mm분ss초").format(date);
			PDDocument document = PDDocument.load(attestedPDFFile);
			if (document.isEncrypted()) {
				document.decrypt("");
			}
			List allPages = document.getDocumentCatalog().getAllPages();
			for (int pageNo = 0; pageNo < allPages.size(); pageNo++) {

				PDPage page = (PDPage) allPages.get(pageNo);
				PDResources resources = page.findResources();
				PDStream contentStream = page.getContents();
				COSStream contents = contentStream.getStream();
				PDFStreamParser parser = new PDFStreamParser(contents);
				parser.parse();
				streamResourcesStack.push(resources);
				List tokens = parser.getTokens();
				for (int j = 0; j < tokens.size(); j++) {
					Object next = tokens.get(j);
					if (next instanceof PDFOperator) {
						PDFOperator op = (PDFOperator) next;
						if (op.getOperation().equals("Tf")) {
							COSName fontName = (COSName) tokens.get(j - 2);
							font = streamResourcesStack.peek().getFonts().get(fontName.getName());
						} else if (op.getOperation().equals("Tj")) {

							COSString token = (COSString) tokens.get(j - 1);
							byte[] string = token.getBytes();
							StringBuffer buf = new StringBuffer();
							for (int i = 0; i < string.length; i += 2) {
								int code = 0;
								for (int k = 0; k < 2; k++) {
									code <<= 8;
									code |= (string[i + k] + 256) % 256;
								}
//								System.out.println(code);
								cmap = font.getUnicodeCMap();
								String c = cmap.lookup(code, 2);
								buf.append(c);

							}

							if (buf.toString().contains("열람일시 : ")) {
								System.out.println("------------- " + buf.toString());
								token.reset();
								token.append(cmap.convert("열람일시 : " + dateString));
							}
							//
							// if (buf.toString().contains("출력일시")) {
							// System.out.println("------------- " +
							// buf.toString());
							// token.reset();
							// token.append("1111111111".getBytes("ISO-8859-1"));
							// }
							// System.out.println("########");
							// System.out.println(buf.toString());

							// string = string.replaceFirst(strToFind, message);
							// previous.reset();
							// previous.append(string.getBytes("ISO-8859-1"));
						}
						else if (op.getOperation().equals("TJ")) {
							StringBuffer buf = new StringBuffer();
							COSArray previous = (COSArray) tokens.get(j - 1);
							for (int k = 0; k < previous.size(); k++) {
								Object arrElement = previous.getObject(k);
								if (arrElement instanceof COSString) {
									COSString cosString = (COSString) arrElement;
									byte[] string = cosString.getBytes();
									int codeLength = 1;
									for (int i = 0; i < string.length; i += codeLength) {
										codeLength = 1;
										String c = font.encode(string, i, codeLength);
										int[] codePoints = null;
										if (c == null && i + 1 < string.length) {
											// maybe a multibyte encoding
											codeLength++;
											c = font.encode(string, i, codeLength);
											codePoints = new int[] { font.getCodeFromArray(string, i, codeLength) };
										}
										buf.append(c);
									}
									System.out.println("###");
									System.out.println(buf.toString());
								}
							}
							if (buf.toString().contains("출력일시")) {
								System.out.println("############ 출력일시!!!!:"+buf.toString());
								StringBuffer timeBuf = new StringBuffer();
								
								timeBuf.append(new SimpleDateFormat("yyyy").format(date));
								timeBuf.append(new SimpleDateFormat("MM").format(date));
								timeBuf.append( new SimpleDateFormat("dd").format(date));
								timeBuf.append( new SimpleDateFormat("HH").format(date));
								timeBuf.append( new SimpleDateFormat("mm").format(date));
								timeBuf.append( new SimpleDateFormat("ss").format(date));
								System.out.println(timeBuf.toString());
								char[] chars = timeBuf.toString().toCharArray();
								boolean overColone = false;
								boolean overSpace = false;
								int k = 0;
								for (; k < previous.size(); k++) {
									Object arrElement = previous.getObject(k);
									if (arrElement instanceof COSString) {
										COSString cosString = (COSString) arrElement;
										String c = parseCode(cosString);
										System.out.println(c);
										if ( ":".equals(c)){
											System.out.println("Over Colone");
											overColone = true;
										}
										if (overColone && " ".equals(c)){
											System.out.println("Over overSpace");
											overSpace  = true;
										}
										if (overSpace && overColone){
											break;
										}
									}
								}
								System.out.println("k:"+k);
								int count = 0;
								for (; k < previous.size(); k++) {
									Object arrElement = previous.getObject(k);
									if (arrElement instanceof COSString) {
										COSString cosString = (COSString) arrElement;
										String c = parseCode(cosString);
										if (Pattern.matches("\\d", c)){
											String numberString = ""+chars[count++];
											cosString.reset();
											cosString.append(cmap.convert(numberString));
										}
									}
								}
							}
						}
					}
				}
				PDStream updatedStream = new PDStream(document);
				OutputStream out = updatedStream.createOutputStream();
				ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
				tokenWriter.writeTokens(tokens);
				page.setContents(updatedStream);
			}
			document.save(new FileOutputStream(saveFile));
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	private String parseCode(COSString cosString) throws IOException {
		String c = null;
		byte[] string = cosString.getBytes();
		int codeLength = 1;
		for (int i = 0; i < string.length; i += codeLength) {
			codeLength = 1;
			c = font.encode(string, i, codeLength);
			int[] codePoints = null;
			if (c == null && i + 1 < string.length) {
				// maybe a multibyte encoding
				codeLength++;
				c = font.encode(string, i, codeLength);
				codePoints = new int[] { font.getCodeFromArray(string, i, codeLength) };
			}
		}
		return c;
	}
}
