package net.narusas.aceauction.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Registry {
	static Registry instance;
	LinkedList<담당계> 담당계List = new LinkedList<담당계>();

	HashMap<Long, 담당계> 담당계Map = new HashMap<Long, 담당계>();
	LinkedList<사건> 사건List = new LinkedList<사건>();
	HashMap<Long, 사건> 사건Map = new HashMap<Long, 사건>();

	private Registry() {

	}

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

	public static Registry getInstance() {
		if (instance == null) {
			instance = new Registry();
		}
		return instance;
	}

	public static 담당계 get담당계(long no) {
		return getInstance().담당계Map.get(no);
	}
	
	public static 사건 get사건(long no) {
		return getInstance().사건Map.get(no);
	}
}
