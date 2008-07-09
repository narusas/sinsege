/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.������Ȳ;

// TODO: Auto-generated Javadoc
/**
 * ����� ����Ʈ���� ���� ���� ��Ȳ�� ó���ϴ� Ŭ����.
 * 
 * @author narusas
 */
public class �����������ȲBuilder {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The id. */
	private final int id;

	/** The ������Ȳs. */
	private final List<������Ȳ> ������Ȳs;

	/**
	 * Instantiates a new �����������Ȳ builder.
	 * 
	 * @param id the id
	 * @param ������Ȳ the ������Ȳ
	 */
	public �����������ȲBuilder(int id, List<������Ȳ> ������Ȳ) {
		this.id = id;
		this.������Ȳs = ������Ȳ;
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		if (������Ȳs == null || ������Ȳs.size() == 0) {
			return;
		}
		insert������Ȳs();
	}

	/**
	 * Insert������Ȳ.
	 * 
	 * @param no the no
	 * @param item the item
	 * @param db��Ȳ the db��Ȳ
	 * 
	 * @throws Exception the exception
	 */
	private void insert������Ȳ(int no, ������Ȳ item, List<������Ȳ> db��Ȳ)
			throws Exception {
		if (������Ȳ.exitIn(db��Ȳ, no)) {
			return;
		}

		item.setGoodsId(id);
		item.setNo(no);

		item.insert();
	}

	/**
	 * Insert������Ȳs.
	 * 
	 * @throws Exception the exception
	 */
	private void insert������Ȳs() throws Exception {
		DB.reConnect();
		List<������Ȳ> db��Ȳ = ������Ȳ.findByGoodsId(id);
		for (int i = 0; i < ������Ȳs.size(); i++) {
			int no = i + 1;// ��ȣ�� 1���� �����Ѵ�.
			insert������Ȳ(no, ������Ȳs.get(i), db��Ȳ);
		}
	}
}
