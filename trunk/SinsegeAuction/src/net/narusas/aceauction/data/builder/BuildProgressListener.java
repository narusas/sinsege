/*
 * 
 */
package net.narusas.aceauction.data.builder;

// TODO: Auto-generated Javadoc
/**
 * ���� �������� ��� UI�� �ݿ��ϱ� ���� Listener.
 * 
 * @author narusas
 */
public interface BuildProgressListener {
	
	/** The LEVE l_����. */
	int LEVEL_���� = 2;

	/** The LEVE l_����. */
	int LEVEL_���� = 4;

	/** The LEVE l_����. */
	int LEVEL_���� = 1;

	/** The LEVE l_���. */
	int LEVEL_��� = 3;

	/**
	 * ����/���/������ �ϳ� ó���ɶ����� ȣ��ȴ�.
	 * 
	 * @param level the level
	 */
	void progress(int level);

	/**
	 * ������ �ٲ�� ������ ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param chargeSize the charge size
	 */
	void update����Size(int chargeSize);

	/**
	 * ����� �ٲ�� ������ ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param mulgunSize the mulgun size
	 */
	void update����Size(int mulgunSize);

	/**
	 * ���谡 �ٲ�� ����� ���ڰ� ���ŵɶ� ȣ��ȴ�.
	 * 
	 * @param sagunSize the sagun size
	 */
	void update���Size(int sagunSize);
}
