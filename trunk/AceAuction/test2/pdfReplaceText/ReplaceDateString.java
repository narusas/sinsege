package pdfReplaceText;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.PDFOperator;

import com.ibm.icu.text.SimpleDateFormat;

public class ReplaceDateString {
	private static Stack<PDResources> streamResourcesStack = new Stack<PDResources>();
	private static PDFont font;
	private static CMap cmap;

	public static void main(String[] args) throws IOException, CryptographyException, InvalidPasswordException, COSVisitorException {
		Date date = new Date();
		String dateString = new SimpleDateFormat("yyyy년MM월dd일 hh시mm분ss초").format(date);
		PDDocument document = PDDocument.load("fixture2/087_인포_제주_2011-15393.pdf");
		if (document.isEncrypted()) {
			document.decrypt("");
		}
		List allPages = document.getDocumentCatalog().getAllPages();
		for (int pageNo = 0; pageNo < allPages.size(); pageNo++) {

			System.out.println("Work Page:"+pageNo);
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
						 System.out.println("\nFONT NAME:"+fontName.getName());
						font = streamResourcesStack.peek().getFonts().get(fontName.getName());
						// System.out.println(font);
						// graphicsState.getTextState().setFontSize(fontSize);
						// graphicsState.getTextState().setFont((PDFont)
						// getFonts().get(fontName.getName()));

					} else if (op.getOperation().equals("Tj")) {
						System.out.println("Tj");
						// Tj takes one operator and that is the string
						// to display so lets update that operator
						COSString token = (COSString) tokens.get(j - 1);
						byte[] string = token.getBytes();
						int codeLength = 1;
						StringBuffer buf = new StringBuffer();

						// for (int i = 0; i < string.length; i += codeLength) {
						// codeLength = 1;
						// String c = font.encode(string, i, codeLength);
						// int[] codePoints = null;
						// if (c == null && i + 1 < string.length) {
						// // maybe a multibyte encoding
						// codeLength++;
						// c = font.encode(string, i, codeLength);
						// codePoints = new int[] {
						// font.getCodeFromArray(string, i, codeLength) };
						//
						// }
						// buf.append(c);
						// // System.out.print(c);
						// }
						for (int i = 0; i < string.length; i += 2) {
							int code = 0;
							for (int k = 0; k < 2; k++) {
								code <<= 8;
								code |= (string[i + k] + 256) % 256;
							}
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
						System.out.println("#### TJ");
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
							}
						}
						System.out.println(buf.toString());
						if (buf.toString().contains("출력일시")) {
							System.out.println("############ 출력일시!!!!:"+buf.toString());
						}
						
					}
					// COSArray previous = (COSArray) tokens.get(j - 1);
					// for (int k = 0; k < previous.size(); k++) {
					// Object arrElement = previous.getObject(k);
					// if (arrElement instanceof COSString) {
					// COSString cosString = (COSString) arrElement;
					// String string = cosString.getString();
					// // string = string.replaceFirst(strToFind, message);
					// cosString.reset();
					// cosString.append(string.getBytes("ISO-8859-1"));
					// }
					// }
					// }
				}
			}
			PDStream updatedStream = new PDStream(document);
			OutputStream out = updatedStream.createOutputStream();
			ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
			tokenWriter.writeTokens(tokens);
			page.setContents(updatedStream);
		}
		document.save("tttt.pdf");

	}
}
