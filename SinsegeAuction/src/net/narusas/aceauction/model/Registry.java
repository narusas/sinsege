/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.HashMap;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class Registry.
 */
public class Registry {
	
	/** The instance. */
	static Registry instance;
	
	/** The ���� list. */
	LinkedList<����> ����List = new LinkedList<����>();

	/** The ���� map. */
	HashMap<Long, ����> ����Map = new HashMap<Long, ����>();
	
	/** The ��� list. */
	LinkedList<���> ���List = new LinkedList<���>();
	
	/** The ��� map. */
	HashMap<Long, ���> ���Map = new HashMap<Long, ���>();

	/**
	 * Instantiates a new registry.
	 */
	private Registry() {

	}

	/**
	 * Add����.
	 * 
	 * @param charge the charge
	 */
	public static void add����(���� charge) {
		if (getInstance().����Map.containsKey(charge.getNo())) {
			return;
		}
		getInstance().����Map.put(charge.getNo(), charge);
		getInstance().����List.add(charge);
		if (getInstance().����List.size() > 20) {
			���� toRemove = getInstance().����List.remove(0);
			getInstance().����Map.remove(toRemove.getNo());
		}
	}

	/**
	 * Add���.
	 * 
	 * @param charge the charge
	 */
	public static void add���(��� charge) {
		if (getInstance().���Map.containsKey(charge.getDbid())) {
			return;
		}
		getInstance().���Map.put(charge.getDbid(), charge);
		getInstance().���List.add(charge);
		if (getInstance().���List.size() > 20) {
			��� toRemove = getInstance().���List.remove(0);
			getInstance().���Map.remove(toRemove.getDbid());
		}
	}

	/**
	 * Gets the single instance of Registry.
	 * 
	 * @return single instance of Registry
	 */
	public static Registry getInstance() {
		if (instance == null) {
			instance = new Registry();
		}
		return instance;
	}

	/**
	 * Gets the ����.
	 * 
	 * @param no the no
	 * 
	 * @return the ����
	 */
	public static ���� get����(long no) {
		return getInstance().����Map.get(no);
	}
	
	/**
	 * Gets the ���.
	 * 
	 * @param no the no
	 * 
	 * @return the ���
	 */
	public static ��� get���(long no) {
		return getInstance().���Map.get(no);
	}
}
