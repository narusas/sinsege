/*
 * 
 */
package net.narusas.aceauction.data.builder;

// TODO: Auto-generated Javadoc
/**
 * 빌딩 과정을을 듣고 UI에 반영하기 위한 Listener.
 * 
 * @author narusas
 */
public interface BuildProgressListener {
	
	/** The LEVE l_담당계. */
	int LEVEL_담당계 = 2;

	/** The LEVE l_물건. */
	int LEVEL_물건 = 4;

	/** The LEVE l_법원. */
	int LEVEL_법원 = 1;

	/** The LEVE l_사건. */
	int LEVEL_사건 = 3;

	/**
	 * 담당계/사건/물건이 하나 처리될때마다 호출된다.
	 * 
	 * @param level the level
	 */
	void progress(int level);

	/**
	 * 법원이 바뀌어 담당계의 숫자가 갱신될때 호출된다.
	 * 
	 * @param chargeSize the charge size
	 */
	void update담당계Size(int chargeSize);

	/**
	 * 사건이 바뀌어 물건의 숫자가 갱신될때 호출된다.
	 * 
	 * @param mulgunSize the mulgun size
	 */
	void update물건Size(int mulgunSize);

	/**
	 * 담당계가 바뀌어 사건의 숫자가 갱신될때 호출된다.
	 * 
	 * @param sagunSize the sagun size
	 */
	void update사건Size(int sagunSize);
}
