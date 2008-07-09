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
	
	/** The 담당계 list. */
	LinkedList<담당계> 담당계List = new LinkedList<담당계>();

	/** The 담당계 map. */
	HashMap<Long, 담당계> 담당계Map = new HashMap<Long, 담당계>();
	
	/** The 사건 list. */
	LinkedList<사건> 사건List = new LinkedList<사건>();
	
	/** The 사건 map. */
	HashMap<Long, 사건> 사건Map = new HashMap<Long, 사건>();

	/**
	 * Instantiates a new registry.
	 */
	private Registry() {

	}

	/**
	 * Add담당계.
	 * 
	 * @param charge the charge
	 */
	public static void add담당계(담당계 charge) {
		if (getInstance().담당계Map.containsKey(charge.getNo())) {
			return;
		}
		getInstance().담당계Map.put(charge.getNo(), charge);
		getInstance().담당계List.add(charge);
		if (getInstance().담당계List.size() > 20) {
			담당계 toRemove = getInstance().담당계List.remove(0);
			getInstance().담당계Map.remove(toRemove.getNo());
		}
	}

	/**
	 * Add사건.
	 * 
	 * @param charge the charge
	 */
	public static void add사건(사건 charge) {
		if (getInstance().사건Map.containsKey(charge.getDbid())) {
			return;
		}
		getInstance().사건Map.put(charge.getDbid(), charge);
		getInstance().사건List.add(charge);
		if (getInstance().사건List.size() > 20) {
			사건 toRemove = getInstance().사건List.remove(0);
			getInstance().사건Map.remove(toRemove.getDbid());
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
	 * Gets the 담당계.
	 * 
	 * @param no the no
	 * 
	 * @return the 담당계
	 */
	public static 담당계 get담당계(long no) {
		return getInstance().담당계Map.get(no);
	}
	
	/**
	 * Gets the 사건.
	 * 
	 * @param no the no
	 * 
	 * @return the 사건
	 */
	public static 사건 get사건(long no) {
		return getInstance().사건Map.get(no);
	}
}
