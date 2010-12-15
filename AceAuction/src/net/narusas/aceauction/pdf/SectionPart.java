package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

public class SectionPart extends Item {

	List<Entity> acceptDays = new LinkedList<Entity>();

	List<Entity> becauses = new LinkedList<Entity>();

	List<Entity> priotyNos = new LinkedList<Entity>();

	List<Entity> purposes = new LinkedList<Entity>();

	List<Entity> rightAndEtc = new LinkedList<Entity>();

	public SectionPart() {
		super(new Entity("", 0, 0, 0, 0));
	}

	public void addAcceptDays(List<Entity> values) {
		acceptDays.addAll(values);
	}

	public void addBecauses(List<Entity> values) {
		becauses.addAll(values);
	}

	public void addPriotyNos(List<Entity> values) {
		priotyNos.addAll(values);
	}

	public void addPurpose(List<Entity> values) {
		purposes.addAll(values);
	}

	public void addRightAndEtc(List<Entity> values) {
		rightAndEtc.addAll(values);
	}

	public Item get(int i) {
		return childs.get(i);
	}

}
