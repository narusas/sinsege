package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;

import org.apache.commons.httpclient.HttpException;

public class ���ǵ���ǹ��ǻ󼼳���Fetcher extends ���ǵ����Fetcher {

	static boolean debug = false;

	static Logger logger = Logger.getLogger("log");

	static Pattern tagPattern = Pattern.compile("(<[^>]*>)");

	static Pattern ��������Pattern = Pattern.compile("�������� : (\\d+-\\d+-\\d+)");

	static Pattern ��Ű��ð���Pattern = Pattern.compile("��Ű��ð��� : ([^<]+)");

	static Pattern ���䱸����Pattern = Pattern.compile("���䱸���� : ([^<]+)");

	static Pattern ���������Pattern = Pattern.compile("���������:(\\d+-\\d+-\\d+)");

	static Pattern �������Pattern = Pattern.compile("�������\\s+([^<^\\s]+)");
	static Pattern �������Pattern = Pattern.compile("������� : ([^<]+)");

	public ���ǵ���ǹ��ǻ󼼳���Fetcher() throws HttpException, IOException {
		super();

	}

	public String getPage(String court, String ����, int eventYear,
			int eventNo, String no) throws IOException {
		logger.info("��������� �� ���� ������ �������⸦ �õ��մϴ�");
		logger.info("/v2/info-result.htm?courtNo=" + court + "&courtNo2=" + ����
				+ "&eventNo1=" + eventYear + "&eventNo2=" + eventNo + "&objNo="
				+ no);

		String res = fetch("/v2/info-result.htm?courtNo=" + court
				+ "&courtNo2=" + ���� + "&eventNo1=" + eventYear + "&eventNo2="
				+ eventNo + "&objNo=" + no);
		System.out.println(res);
		logger.info("��������� �� ���� ������ �������⿡ �����߽��ϴ�.");

		return res;
	}

	public ���ǵ���ǹ��ǻ󼼳��� parse(String src) {
		// System.out.println(src);
		logger.info("��������� �� ���� �������� �м��� �����մϴ�.");

		String �������� = TableParser.getNextTDValueStripped(src, "��������");
		String ä���� = TableParser.getNextTDValueStripped(src, "ä �� ��");
		String ä���� = TableParser.getNextTDValueStripped(src, "ä �� ��");
		String ������ = TableParser.getNextTDValueStripped(src, "�� �� ��");

		String �������� = TableParser.getNextTDValueStripped(src, "��������");
		String ������ = TableParser.getNextTDValueStripped(src,
				"<font color=366AB3>������");
		String �ǹ����� = TableParser.getNextTDValueStripped(src, "�ǹ�����");
		String ������� = TableParser.getNextTDValueStripped(src, "�������");
		String ���ÿܸ��� = TableParser.getNextTDValueStripped(src, "���ÿܸ���");

		String ������ = TableParser.getNextTDValueStripped(src, "�� �� ��");
		String ������ = TableParser.getNextTDValueStripped(src, "�� �� ��");
		String ������ = TableParser.getNextTDValueStripped(src, "�� �� ��");

		String �Ű���� = TableParser.getNextTDValueStripped(src, "�Ű����");
		String û���ݾ� = TableParser.getNextTDValueStripped(src, "û���ݾ�");
		String ��������Ȳ = TableParser.getNextTDValueStripped(src, "��������Ȳ")
				.replace('��', ' ').trim();

		logger.info("�⺻ ������ �м��� �Ϸ��߽��ϴ�. ");

		String �������� = re(src, ��������Pattern);
		String ������� = re(src, �������Pattern);
		String ���䱸���� = re(src, ���䱸����Pattern);
		String ��Ű��ð��� = re(src, ��Ű��ð���Pattern);
		String ������� = re(src, �������Pattern);
		String ��������� = re(src, ���������Pattern);

		logger.info("���������� �м��� �Ϸ��߽��ϴ�. ");

		int pos = src.indexOf("<strong>�հ�</strong>");
		String ������_���� = null;
		String ������_�ǹ� = null;
		String ������_���ÿܰǹ����� = null;
		String ������_���ÿܰǹ������� = null;
		String ������_��Ÿ = null;
		String ������_�հ� = null;
		String ������_��� = null;
		if (pos != -1) {
			TableParser.TDValue res = TableParser.getNextTDValueByPos(src, pos);
			������_���� = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_�ǹ� = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_���ÿܰǹ����� = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_���ÿܰǹ������� = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_��Ÿ = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_�հ� = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			res = TableParser.getNextTDValueByPos(src, res.pos);
			������_��� = res.text;

			logger.info("������ ������ �м��� �Ϸ��߽��ϴ�. ");

		}

		���ǵ���ǹ��ǻ󼼳��� detail = new ���ǵ���ǹ��ǻ󼼳���();
		detail.set��������(��������);
		detail.setä����(ä����);
		detail.setä����(ä����);
		detail.set������(������);
		detail.set��������(��������);
		detail.set��������(��������);
		detail.set������(������);
		detail.set�ǹ�����(�ǹ�����);
		detail.set�������(�������);
		detail.set���ÿܸ���(���ÿܸ���);

		detail.set�������(�������);
		detail.set���䱸����(���䱸����);
		detail.set��Ű��ð���(��Ű��ð���);

		detail.set�������(�������);

		detail.set�Ű����(�Ű����);

		detail.set������(������);
		detail.set������(������);
		detail.set������(������);
		detail.setû���ݾ�(û���ݾ�);

		detail.set��������Ȳ(��������Ȳ);
		detail.set��������(��������);
		detail.set������_����(������_����);
		detail.set������_�ǹ�(������_�ǹ�);
		detail.set������_���ÿܰǹ�����(������_���ÿܰǹ�����);
		detail.set������_���ÿܰǹ�������(������_���ÿܰǹ�������);
		detail.set������_��Ÿ(������_��Ÿ);
		detail.set������_���ⱸ(������_��Ÿ);
		detail.set������_�հ�(������_�հ�);
		detail.set������_���(������_���);

		detail.set���������(���������);

		int startPos = src.indexOf("<font color=#000000> �ǹ���Ȳ</font>");
		if (startPos == -1) {
			startPos = src.indexOf("<font color=#000000> <b>�ǹ���Ȳ</font>");
		}

		logger.info("�ǹ���Ȳ ������ ���翩�� :" + (startPos >= 0));
		if (startPos != -1) {
			startPos = src.indexOf("<TABLE cellSpacing=0 ", startPos);
			int endPos = src.indexOf("</table", startPos);
			// HashMap<String, List<String>> map = TableParser.parseTable(src,
			// startPos, endPos, 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set�ǹ���Ȳ(map);
			// debug = true;
			logger.info("�ǹ���Ȳ ������ �м��� �Ϸ��߽��ϴ�. ");

		}

		startPos = src.indexOf("<font color=#000000><b> ��������Ȳ</font>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table", startPos);
			endPos = src.indexOf("</table", endPos + 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set��������Ȳ(map);
		}
		logger.info("��������Ȳ������ �м��� �Ϸ��߽��ϴ�. ");

		startPos = src.indexOf("<font color=#000000><b> ������Ȳ</font>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table", startPos);
			endPos = src.indexOf("</table", endPos + 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set������Ȳ(map);
		}
		logger.info("������Ȳ ������ �м��� �Ϸ��߽��ϴ�. ");

		startPos = src.indexOf("�Ű�������Ȳ		    </font>");
		if (startPos == -1) {
			startPos = src.indexOf("�Ű�/����������Ȳ    </font>");
		}
		if (startPos != -1) {
			startPos = src.indexOf("<table", startPos);
			int endPos = src.indexOf("</table", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			// System.out.println(map.getRecords());
			detail.set�Ű�������Ȳ(map);
		}
		logger.info("�Ű����� ������ �м��� �Ϸ��߽��ϴ�. ");

		startPos = src.indexOf("<font color=#000000><b> ���ÿܰǹ���Ȳ</font>");
		if (startPos != -1) {
			startPos = src.indexOf("<TR", startPos);
			int endPos = src.indexOf("</table", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set���ÿܰǹ���Ȳ(map);
		}
		logger.info("���ÿܰǹ���Ȳ�� �м��� �Ϸ��߽��ϴ�. ");

		startPos = src.indexOf("<font color=366AB3>���ⱸ</font>");
		if (startPos != -1) {
			startPos = src.indexOf("<TR", startPos);
			int endPos = src.indexOf("</TABLE", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set���ⱸ(map);
		}
		logger.info("���ⱸ�� �м��� �Ϸ��߽��ϴ�. ");

		logger.info("�м��� �Ϸ��߽��ϴ�. ");

		return detail;
	}

	private String re(String src, Pattern p) {
		Matcher m;
		String ���䱸����;
		m = p.matcher(src);
		String temp = null;
		if (m.find()) {
			temp = m.group(1);
		}
		���䱸���� = temp;
		return ���䱸����;
	}

}
