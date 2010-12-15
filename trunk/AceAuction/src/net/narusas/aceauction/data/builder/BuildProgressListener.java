package net.narusas.aceauction.data.builder;

/**
 * ���� �������� ��� UI�� �ݿ��ϱ� ���� Listener.
 * 
 * @author narusas
 * 
 */
public interface BuildProgressListener {
	int LEVEL_���� = 2;

	int LEVEL_���� = 4;

	int LEVEL_���� = 1;

	int LEVEL_��� = 3;

	/**
	 * ����/���/������ �ϳ� ó���ɶ����� ȣ��ȴ�.
	 * 
	 * @param level
	 */
	void progress(int level);

	/**
	 * ������ �ٲ�� ������ ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param chargeSize
	 */
	void update����Size(int chargeSize);

	/**
	 * ����� �ٲ�� ������ ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param mulgunSize
	 */
	void update����Size(int mulgunSize);

	/**
	 * ���谡 �ٲ�� ����� ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param sagunSize
	 */
	void update���Size(int sagunSize);
}
