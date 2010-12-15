package net.narusas.aceauction.data;

import java.util.regex.Matcher;

import junit.framework.TestCase;

public class �ǹ���Ȳ_�뵵ConverterTest extends TestCase {
	public void testRegx() {
		Matcher m = �ǹ���Ȳ_�뵵Converter.p1.matcher("�ǹ���");
		assertTrue(m.find());
		assertEquals("�ǹ�", m.group(1));
		
		m = �ǹ���Ȳ_�뵵Converter.p1.matcher("�ǹ����");
		assertTrue(m.find());
		assertEquals("�ǹ���", m.group(1));
		
		
		assertEquals("a",�ǹ���Ȳ_�뵵Converter.convert("a,b") );
		assertEquals("a",�ǹ���Ȳ_�뵵Converter.convert("a��b") );
		assertEquals("�μӰǹ�",�ǹ���Ȳ_�뵵Converter.convert("a�μӰǹ�") );
		
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("�Ϲݰǹ�") );
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("���߰ǹ�") );
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("���ðǹ�") );
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("��Ȱ�ǹ�") );
		
		assertEquals("�ٸ��ü�",�ǹ���Ȳ_�뵵Converter.convert("�ٸ���Ȱ�ü�") );
		
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("�ǹ���") );
		assertEquals("�ǹ���",�ǹ���Ȳ_�뵵Converter.convert("�ǹ����") );
		
		assertEquals("�ǹ�",�ǹ���Ȳ_�뵵Converter.convert("�ǹ�(���)") );
		assertEquals("�ǹ��ǹ�",�ǹ���Ȳ_�뵵Converter.convert("�ǹ�(���)�ǹ�(���)") );
		
		assertEquals("����",�ǹ���Ȳ_�뵵Converter.convert("�ǹ�����") );
		assertEquals("����",�ǹ���Ȳ_�뵵Converter.convert("�ǹ�����") );
		assertEquals("�����ü�",�ǹ���Ȳ_�뵵Converter.convert("��ȸ") );
		assertEquals("����",�ǹ���Ȳ_�뵵Converter.convert("���������") );
	}
}
