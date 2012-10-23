package net.narusas.si.auction.pdf.atested.modify;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.util.PDFOperator;
import org.junit.Test;

public class Test2 {

	@Test
	public void test() throws IOException {
		File f = new File("fixture2/087_인포_제주_2011-15393.pdf");
		PDDocument doc = PDDocument.load(f);
		List pages = doc.getDocumentCatalog().getAllPages();
        for( int i=0; i<pages.size(); i++ )
        {
            PDPage page = (PDPage)pages.get( i );
            PDGraphicsState graphicsState = new PDGraphicsState(page.findCropBox());
            PDStream contents = page.getContents();
            PDFStreamParser parser = new PDFStreamParser(contents.getStream() );
            parser.parse();
            List tokens = parser.getTokens();
            for( int j=0; j<tokens.size(); j++ )
            {
                Object next = tokens.get( j );
                if( next instanceof PDFOperator )
                {
                    PDFOperator op = (PDFOperator)next;
                    //Tj and TJ are the two operators that display
                    //strings in a PDF
                    if( op.getOperation().equals( "Tj" ) )
                    {
                        //Tj takes one operator and that is the string
                        //to display so lets update that operator
                        COSString previous = (COSString)tokens.get( j-1 );
                        
                        String string = previous.getString();
                        System.out.println(previous.getHexString());
                        
                        System.out.println(string);
                        byte[] bytes = previous.getBytes();
                        System.out.println(new String(bytes, "EUC-KR"));
//                        string = string.replaceFirst( strToFind, message );
                        previous.reset();
                        previous.append( string.getBytes("ISO-8859-1") );
                    }
                    else if( op.getOperation().equals( "TJ" ) )
                    {
                        COSArray previous = (COSArray)tokens.get( j-1 );
                        for( int k=0; k<previous.size(); k++ )
                        {
                            Object arrElement = previous.getObject( k );
                            if( arrElement instanceof COSString )
                            {
                                COSString cosString = (COSString)arrElement;
                                System.out.println(cosString.getHexString());
                                String string = cosString.getString();
                                byte[] bytes = cosString.getBytes();
                                System.out.println(Integer.toHexString(bytes[0] & 0xFF ) +Integer.toHexString(bytes[1] & 0xFF ));

                                System.out.println(new String(bytes, "UTF-16"));
                                PDType1Font font = PDType1Font.HELVETICA;
                                System.out.println(font.encode(bytes, 0, 2));
//                                System.out.println(graphicsState.getTextState().getFont().encode(bytes, 0, 2));
//                                string = string.replaceFirst( strToFind, message );
                                cosString.reset();
                                cosString.append( string.getBytes("ISO-8859-1") );
                            }
                        }
                    }
                }
            }
            //now that the tokens are updated we will replace the
            //page content stream.
            PDStream updatedStream = new PDStream(doc);
            OutputStream out = updatedStream.createOutputStream();
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            tokenWriter.writeTokens( tokens );
            page.setContents( updatedStream );
        }
	}

}
