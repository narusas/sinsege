package net.narusas.aceauction.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Registry {
	static Registry instance;
	LinkedList<����> ����List = new LinkedList<����>();

	HashMap<Long, ����> ����Map = new HashMap<Long, ����>();
	LinkedList<���> ���List = new LinkedList<���>();
	HashMap<Long, ���> ���Map = new HashMap<Long, ���>();

	private Registry() {

	}

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

	public static Registry getInstance() {
		if (instance == null) {
			instance = new Registry();
		}
		return instance;
	}

	public static ���� get����(long no) {
		return getInstance().����Map.get(no);
	}
	
	public static ��� get���(long no) {
		return getInstance().���Map.get(no);
	}
}
