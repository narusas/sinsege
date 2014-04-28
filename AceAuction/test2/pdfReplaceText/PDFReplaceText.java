package pdfReplaceText;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
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
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;

import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.pdfbox.util.TextPosition;
import org.apache.pdfbox.util.operator.OperatorProcessor;

public class PDFReplaceText {

	Vector<List<TextPosition>> charactersByArticle = new Vector<List<TextPosition>>();

	Map<String, TreeMap<Float, TreeSet<Float>>> characterListMapping = new HashMap<String, TreeMap<Float, TreeSet<Float>>>();
	private PDGraphicsState graphicsState = null;

	private Matrix textMatrix = null;
	private Matrix textLineMatrix = null;
	private Stack<PDGraphicsState> graphicsStack = new Stack<PDGraphicsState>();

	private Map<String, OperatorProcessor> operators = new HashMap<String, OperatorProcessor>();

	private Stack<PDResources> streamResourcesStack = new Stack<PDResources>();

	private PDResources resources;
	
	

	@Test
	public void test() throws COSVisitorException, IOException, CryptographyException, InvalidPasswordException {
		PDDocument document = PDDocument.load("fixture2/087_인포_제주_2011-15393.pdf");
		if (document.isEncrypted()) {
			document.decrypt("");
		}
		List allPages = document.getDocumentCatalog().getAllPages();
		PDPage page = (PDPage) allPages.get(allPages.size()-1);

		PDStream contentStream = page.getContents();
		COSStream contents = contentStream.getStream();

		List pageArticles = page.getThreadBeads();
		int numberOfArticleSections = 1 + pageArticles.size() * 2;
		int originalSize = charactersByArticle.size();
		charactersByArticle.setSize(numberOfArticleSections);

		for (int i = 0; i < numberOfArticleSections; i++) {
			if (numberOfArticleSections < originalSize) {
				((List<TextPosition>) charactersByArticle.get(i)).clear();
			} else {
				charactersByArticle.set(i, new ArrayList<TextPosition>());
			}
		}
		characterListMapping.clear();
		graphicsState = new PDGraphicsState(page.getTrimBox());
		textMatrix = null;
		textLineMatrix = null;
		graphicsStack.clear();
		streamResourcesStack.clear();
		resources = page.findResources();
		if (resources != null) {
			streamResourcesStack.push(resources);
			try {
				processSubStream(contents);
			} finally {
				streamResourcesStack.pop().clear();
			}
		} else {
			processSubStream(contents);
		}
		
//		PDStream updatedStream = new PDStream(document);
//        OutputStream out = updatedStream.createOutputStream();
//        ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
//        tokenWriter.writeTokens( tokens );
//        page.setContents( updatedStream );

		// PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		// stripper.setSortByPosition( true );
		// Rectangle rect = new Rectangle( 10, 280, 275, 600 );
		// stripper.addRegion( "class1", rect );
		// stripper.extractRegions( firstPage );
		// System.out.println( "Text in the area:" + rect );
		// System.out.println( stripper.getTextForRegion( "class1" ) );
	}

	private void processSubStream(COSStream contents) throws IOException {
		List<COSBase> arguments = new ArrayList<COSBase>();
		PDFStreamParser parser = new PDFStreamParser(contents, false);
		Iterator<Object> iter = parser.getTokenIterator();
		while (iter.hasNext()) {
			Object next = iter.next();
//			System.out.println(next);
			if (next instanceof COSObject) {
				arguments.add(((COSObject) next).getObject());
			} else if (next instanceof PDFOperator) {
				processOperator((PDFOperator) next, arguments);
				arguments = new ArrayList<COSBase>();
			} else {
				arguments.add((COSBase) next);
			}
		}

	}

	private void processOperator(PDFOperator operator, List<COSBase> arguments) throws IOException {
		String operation = operator.getOperation();
//		System.out.println(operation);
		if ("Tf".equals(operation)) { // set text font
			COSName fontName = (COSName) arguments.get(0);
			float fontSize = ((COSNumber) arguments.get(1)).floatValue();
			graphicsState.getTextState().setFontSize(fontSize);
			graphicsState.getTextState().setFont((PDFont) getFonts().get(fontName.getName()));
			System.out.println("\nFONT NAME:"+fontName.getName());
		}
		else if ("Tj".equals(operation)){
			COSString token = (COSString)arguments.get( 0 );
			byte[] string = token.getBytes();
			PDFont font = graphicsState.getTextState().getFont();
			int codeLength = 1;
			StringBuffer buf = new StringBuffer();
            for( int i=0; i<string.length; i+=codeLength) {
            	codeLength = 1;
                String c = font.encode( string, i, codeLength );
                int[] codePoints = null;
                if( c == null && i+1<string.length)
                {
                    //maybe a multibyte encoding
                    codeLength++;
                    c = font.encode( string, i, codeLength );
                    codePoints = new int[] {font.getCodeFromArray(string, i, codeLength)};
                    
                }
                buf.append(c);
                System.out.print(c);
            }
            if (buf.toString().contains("열람일시 : ")){
            	System.out.println("------------- "+buf.toString());
            	token.reset();
            	token.append("바뀜".getBytes());
            }
		}
		
		else if ("TJ".equals(operation)){
			COSArray array = (COSArray)arguments.get( 0 );
			int arraySize = array.size();
//			float fontsize = graphicsState.getTextState().getFontSize();
//	        float horizontalScaling = graphicsState.getTextState().getHorizontalScalingPercent()/100;
			StringBuffer buf = new StringBuffer();
	        for( int k=0; k<arraySize; k++ )
	        {
	            COSBase next = array.get( k );
//	            if( next instanceof COSNumber )
//	            {
//	                float adjustment = ((COSNumber)next).floatValue();
//	                Matrix adjMatrix = new Matrix();
//	                adjustment=-(adjustment/1000)*horizontalScaling*fontsize;
//	                // TODO vertical writing mode
//	                adjMatrix.setValue( 2, 0, adjustment );
//	                context.setTextMatrix( adjMatrix.multiply(context.getTextMatrix(), adjMatrix) );
//	            }
//	            else 
	            if( next instanceof COSString )
	            {
	            	PDFont font = graphicsState.getTextState().getFont();
	            	byte[] string = ((COSString)next).getBytes();
	            	int codeLength = 1;
	                for( int i=0; i<string.length; i+=codeLength) {
	                	codeLength = 1;
	                    String c = font.encode( string, i, codeLength );
	                    int[] codePoints = null;
	                    if( c == null && i+1<string.length)
	                    {
	                        //maybe a multibyte encoding
	                        codeLength++;
	                        c = font.encode( string, i, codeLength );
	                        codePoints = new int[] {font.getCodeFromArray(string, i, codeLength)};
	                    }
	                    buf.append(c);
	                }
	                
	                
	                
	                	
	                	
//	            	String c = font.encode( string, k, codeLength );
//	            	System.out.println("#########2 "+new String());
//	                context.processEncodedText( ((COSString)next).getBytes() );
	            }
	        }
	        System.out.println(buf.toString());
		}
//		*/
		
		
	}

	public Map<String, PDFont> getFonts() {
		if (streamResourcesStack.isEmpty()) {
			return Collections.emptyMap();
		}

		return streamResourcesStack.peek().getFonts();
	}

}
