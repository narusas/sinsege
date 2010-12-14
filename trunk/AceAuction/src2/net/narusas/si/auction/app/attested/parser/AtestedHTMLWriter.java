package net.narusas.si.auction.app.attested.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.narusas.si.auction.model.등기부등본Item;
import biz.evot.util.lang.NFile;

public class AtestedHTMLWriter {
	public void writeToHTML(List<등기부등본Item> items) {
		StringBuffer buf = new StringBuffer(
				"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>");
		buf.append("<table border='1'><tr><td>순번</td><td>권리종류</td><td>접수일</td><td>접수번호</td><td>권리자</td><td>금액</td><td>대상소유자</td><td>소멸</td><td>비고</td></tr>");
		for (등기부등본Item item : items) {
			buf.append("<tr>")//
					.append("<td>" + convertHTML(item.get순번()) + "</td>")// 
					.append("<td>" + convertHTML(item.get권리종류()) + "</td>")//
					.append("<td>" + convertHTML(item.get접수일()) + "</td>")//
					.append("<td>" + convertHTML(""+item.get접수번호()) + "</td>")//
					.append("<td>" + convertHTML(item.get권리자()) + "</td>")//
					.append("<td>" + convertHTML(item.get금액()) + "</td>")//
					.append("<td>" + convertHTML(item.get대상소유자()) + "</td>")//
					.append("<td>" + convertHTML(item.get소멸기준()) + "</td>")//
					.append("<td>" + convertHTML(item.get비고()) + "</td>")//
					.append("</tr>")//
			;
		}

		buf.append("</table></body></html>");
		System.out.println(buf.toString());
		try {
			File f = File.createTempFile("pdf_converted", ".html");
			NFile.write(f, buf.toString(), "utf-8");
			java.awt.Desktop.getDesktop().browse(f.toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String convertHTML(String str) {
		if (str == null) {
			return null;
		}
		return str.replace("\n", "<br>");
	}
}
