package net.narusas.aceauction.pdf.itext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class ���ε���ں��� {
	static Random r = new Random(System.currentTimeMillis());

	public static File convert(String srcPDFPath) throws DocumentException, IOException {
		File f = File.createTempFile("���ε", ".pdf");
		return convert(srcPDFPath, f.getAbsolutePath());
	}

	public static File convert(String srcPDFPath, String targetPDFPath) throws DocumentException,
			IOException {
		Calendar c = Calendar.getInstance();
		Date d = new Date(System.currentTimeMillis());

		int hour = r.nextInt(7) + 10;
		d.setHours(hour);
		c.setTimeInMillis(d.getTime());

//		return convert(srcPDFPath, targetPDFPath, c);
		return new File(srcPDFPath);
	}

	/**
	 * ���ε�� ���� ���ڸ� ������ ���ڷ� ������Ѵ�.
	 * 
	 * @param srcPDFPath
	 * @param time
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static File convert(String srcPDFPath, String targetPDFPath, Calendar time)
			throws DocumentException, IOException {
		PdfReader reader = new PdfReader(srcPDFPath);
		File res = new File(targetPDFPath);
		int n = reader.getNumberOfPages();

		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(targetPDFPath));

		int i = 0;
		PdfContentByte under;
		PdfContentByte over;
		// BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
		// BaseFont.WINANSI,
		// BaseFont.EMBEDDED);
		BaseFont bf = BaseFont.createFont("HYSMyeongJo-Medium", "UniKS-UCS2-H",
				BaseFont.NOT_EMBEDDED);
		// BaseFont bf = BaseFont.createFont("c:\\windows\\fonts\\gulim.ttc",
		// null, BaseFont.NOT_EMBEDDED);

		while (i < n) {
			i++;
			// watermark under the existing page
			under = stamp.getUnderContent(i);
			// text over the existing page
			over = stamp.getOverContent(i);

			if (i == 1) {
				drawRect(over, new Rectangle(600, 511, 900, 600)); // barcode
			}

			drawRect(over, new Rectangle(80, 55, 230, 70)); // year
			drawRect(over, new Rectangle(166, 55, 176, 70)); // month
			drawRect(over, new Rectangle(187, 55, 196, 70)); // day

			drawRect(over, new Rectangle(207, 55, 230, 70)); // am/pm

			drawRect(over, new Rectangle(230, 55, 300, 70)); // hours
			drawRect(over, new Rectangle(252, 55, 262, 70)); // mininutes
			drawRect(over, new Rectangle(270, 55, 280, 70)); // seconds

			String dateStr = "�����Ͻ� : " + time.get(Calendar.YEAR) + "�� "
					+ (time.get(Calendar.MONTH) + 1) + "�� " + time.get(Calendar.DAY_OF_MONTH)
					+ "��  ";

			String hour = time.get(Calendar.HOUR_OF_DAY) > 12 ? "����  "
					+ (time.get(Calendar.HOUR_OF_DAY) - 12) : "����  "
					+ time.get(Calendar.HOUR_OF_DAY);
			String timeStr = hour + "�� " + time.get(Calendar.MINUTE) + "�� "
					+ time.get(Calendar.SECOND) + "��";

			markText(over, bf, dateStr + timeStr, 80);

			// markText(over, bf, String.valueOf(time.get(Calendar.YEAR)), 133);
			// markText(over, bf, String.valueOf(time.get(Calendar.MONTH) + 1),
			// 166);
			// markText(over, bf,
			// String.valueOf(time.get(Calendar.DAY_OF_MONTH)),
			// 187);

			// markText(over, bf, timeStr, 230);
			// markText(over, bf,
			// String.valueOf(time.get(Calendar.HOUR_OF_DAY)),
			// 230);
			// markText(over, bf, String.valueOf(time.get(Calendar.MINUTE)),
			// 252);
			// markText(over, bf, String.valueOf(time.get(Calendar.SECOND))+"��",
			// 270);
		}
		stamp.close();
		return res;
	}

	private static void drawRect(PdfContentByte over, Rectangle rect) {
		rect.setGrayFill(1f);
		over.rectangle(rect);
	}

	private static void markText(PdfContentByte over, BaseFont bf, String text, int x) {
		over.beginText();
		over.setFontAndSize(bf, 19);
		over.setTextMatrix(30, 30);
		over.setFontAndSize(bf, 10);
		over.showTextAligned(Element.ALIGN_LEFT, text, x, 58, 0);
		over.endText();
	}
}
