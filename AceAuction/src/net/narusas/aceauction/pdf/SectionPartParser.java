package net.narusas.aceauction.pdf;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

public class SectionPartParser {

	private final PDFSection section;
	LinkedList<등기부등본_사항> items = new LinkedList<등기부등본_사항>();

	public SectionPartParser(PDFSection section) {
		this.section = section;
	}

	public SectionPart parse() {
		SectionPart part = new SectionPart();
		parseParts(part);
		return part;
	}

	private void collectChilds(SectionPart part, Entity startEntity, List<Entity> childs) {
//		System.out.println("#A##A#A##A#");
//		System.out.println(childs);
		등기부등본_사항 item = new 등기부등본_사항(startEntity.getText());
		for (Entity entity : childs) {
//			System.out.println(entity);
//			System.out.println(part.purposes.contains(entity) + " "
//					+ part.acceptDays.contains(entity) + " " + part.becauses.contains(entity) + " "
//					+ part.rightAndEtc.contains(entity));
			if (part.purposes.contains(entity)) {
				item.addPurpose(entity);
			} else if (part.acceptDays.contains(entity)) {
				item.addAcceptDate(entity);
			} else if (part.becauses.contains(entity)) {
//				System.out.println(entity);
				item.addBecause(entity);
			} else if (part.rightAndEtc.contains(entity)) {
				item.addRightAndEtc(entity);
			}
		}
		items.add(item);
	}

	private void parseParts(SectionPart part) {
		collectAllPages(part);
		sort(part);
	}

	private List<Entity> parseValues(int pageNo, List<TextPosition> priotyNo) {
		return new EntityParser().parseValues(pageNo, priotyNo);
	}

	private void sort(SectionPart part) {
		LinkedList<Entity> list = new LinkedList<Entity>();
		list.addAll(part.priotyNos);
		list.addAll(part.purposes);
		list.addAll(part.acceptDays);
		list.addAll(part.becauses);
		list.addAll(part.rightAndEtc);

		Collections.sort(list, new Comparator<Entity>() {

			public int compare(Entity o1, Entity o2) {
				if (o1.getPageNo() < o2.getPageNo()) {
					return -1;
				}
				if (o1.getPageNo() > o2.getPageNo()) {
					return 1;
				}

				if (o1.getY() < o2.getY()) {
					return -1;
				}
				if (o1.getY() > o2.getY()) {
					return 1;
				}

				if (o1.getX() < o2.getX()) {
					return -1;
				}
				if (o1.getX() > o2.getX()) {
					return 1;
				}
				return 0;
			}
		});

		int start = -1;
		Entity startEntity = null;
		for (int i = 0; i < list.size(); i++) {
			Entity e = list.get(i);

			if (part.priotyNos.contains(e)) {

				if (start == -1) {
					startEntity = e;
					start = i;
					continue;
				}

				List<Entity> res = new LinkedList<Entity>();
				for (int j = start + 1; j < i; j++) {
					res.add(list.get(j));
				}
				collectChilds(part, startEntity, res);

				start = i;
				startEntity = e;
			}
		}

		// for (int j = start + 1; j < list.size(); j++) {
		// System.out.println(list.get(j));
		// }

		List<Entity> res = new LinkedList<Entity>();
		for (int j = start + 1; j < list.size(); j++) {
			res.add(list.get(j));
		}
		collectChilds(part, startEntity, res);
		// System.out.println(list);

		// List<Entity> priotyNos = create(part.priotyNos);
		// List<Entity> purposes = create(part.purposes);
		// List<Entity> acceptDates = create(part.acceptDays);
		// List<Entity> becauses = create(part.becauses);
		// List<Entity> rights = create(part.rightAndEtc);

		// for (Entity entity : acceptDates) {
		// System.out.println(entity);
		// System.out.println("------------");
		// }

		// 마지막 Entity가 계산에 포함되지 않게 하여 계산함.

		// for (int index1 = 0; index1 < priotyNos.size() - 1; index1++) {
		// Entity currentPrioty = priotyNos.get(index1);
		// Entity nextPrioty = priotyNos.get(index1 + 1);
		// Item prioty = new Item(currentPrioty);
		// List<Item> purposeItems = findChilds(purposes, currentPrioty,
		// nextPrioty);
		// if (purposeItems.size() == 0) {
		// purposeItems.add(new Item(new Entity("", currentPrioty.getPageNo(),
		// currentPrioty.getY())));
		// }
		//
		// List<Item> purposeBuf = new LinkedList<Item>(purposeItems);
		// purposeBuf.add(new Item(nextPrioty));
		//
		// for (int index2 = 0; index2 < purposeBuf.size() - 1; index2++) {
		// Item currentPurpose = purposeBuf.get(index2);
		// Item nextPurpose = purposeBuf.get(index2 + 1);
		// List<Item> acceptDateItems = findChilds(acceptDates, purposeBuf,
		// index2);
		// if (acceptDateItems.size() == 0) {
		// acceptDateItems.add(new Item(new Entity("",
		// currentPurpose.entity.getPageNo(),
		// currentPurpose.entity.getY())));
		// }
		//
		// List<Item> acceptBuf = new LinkedList<Item>(acceptDateItems);
		// acceptBuf.add(new Item(nextPurpose.entity));
		//
		// for (int index3 = 0; index3 < acceptBuf.size() - 1; index3++) {
		//
		// Item currentAccept = acceptBuf.get(index3);
		// Item nextAccept = acceptBuf.get(index3 + 1);
		// List<Item> befauseItems = findChilds(becauses, acceptBuf, index3);
		// if (befauseItems.size() == 0) {
		// befauseItems.add(new Item(new Entity("",
		// currentAccept.entity.getPageNo(), currentAccept.entity
		// .getY())));
		// }
		// List<Item> becauseBuf = new LinkedList<Item>(befauseItems);
		// becauseBuf.add(new Item(nextPurpose.entity));
		//
		// for (int index4 = 0; index4 < becauseBuf.size() - 1; index4++) {
		//
		// Item currentBecause = becauseBuf.get(index4);
		// List<Item> rightItems = findChilds(rights, becauseBuf, index4);
		//
		// currentBecause.setItem(rightItems);
		// currentAccept.addItem(currentBecause);
		// }
		//
		// currentAccept.setItem(befauseItems);
		// currentPurpose.addItem(currentAccept);
		// }
		//
		// currentPurpose.setItem(acceptDateItems);
		// prioty.addItem(currentPurpose);
		// }
		//
		// prioty.setItem(purposeItems);
		// part.addItem(prioty);
		// }

	}

	/**
	 * @param part
	 */
	void collectAllPages(SectionPart part) {
		for (int pageNo = 0; pageNo < section.pages.size(); pageNo++) {
			part.addPriotyNos(parseValues(pageNo, section.pages.get(pageNo).priotyNo));
			part.addPurpose(parseValues(pageNo, section.pages.get(pageNo).purpose));
			part.addAcceptDays(parseValues(pageNo, section.pages.get(pageNo).acceptDate));
			part.addBecauses(parseValues(pageNo, section.pages.get(pageNo).because));
			part.addRightAndEtc(parseValues(pageNo, section.pages.get(pageNo).rightAndEtc));
		}
	}

	List<Entity> create(List<Entity> src) {
		List<Entity> res = new LinkedList<Entity>();
		res.addAll(src);
		res.add(new Entity("", 1000, 1000, 0, 0));
		return res;
	}

	// private List<Item> findChilds(List<Entity> scope, List<Item> src, int
	// pos) {
	// List<Item> res = new LinkedList<Item>();
	// for (Entity e : scope) {
	// if (e.getY() >= src.get(pos).entity.getY() - 3 && e.getY() <=
	// src.get(pos).entity.getY() - 3) {
	// res.add(new Item(e));
	// }
	// }
	// return res;
	// }

	/**
	 * 
	 * @param scope
	 *            매치되는 CHild들을 가지고 있는 목록
	 * @param currentTarget
	 * @param nextTarget
	 * @return
	 */
	// private List<Item> findChilds(List<Entity> scope, Entity currentTarget,
	// Entity nextTarget) {
	// List<Item> res = new LinkedList<Item>();
	// for (Entity e : scope) {
	// if (e.getY() >= currentTarget.getY() - 3 && e.getY() < nextTarget.getY()
	// - 3) {
	// res.add(new Item(e));
	// }
	// }
	// return res;
	// }
}
