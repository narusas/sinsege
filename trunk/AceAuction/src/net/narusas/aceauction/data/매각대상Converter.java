package net.narusas.aceauction.data;

public class �Ű����Converter {

	public static String convert(String �Ű����) {
		String res = �Ű����.replaceAll("/", " �� ");
		if (res.equals("�����ϰ��Ű�")) {
			return "�����Ű�";
		}
		if (res.equals("�ǹ��ϰ��Ű�")) {
			return "�������� �� �ǹ��Ű�";
		}
		if (res.equals("�������Ű�")) {
			return "�ǹ����� �� �����Ű�";
		}
		if (res.equals("�ǹ����Ű�")) {
			return "�������� �� �ǹ��Ű�";
		}
		return res;
	}

}
