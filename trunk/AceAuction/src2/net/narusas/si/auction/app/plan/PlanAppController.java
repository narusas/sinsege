package net.narusas.si.auction.app.plan;

import java.io.IOException;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.ui.법원ListModel;
import net.narusas.si.auction.fetchers.매각예정_사건목록Fetcher;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.사건Dao;
import net.narusas.si.auction.updater.경매결과Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanAppController {
	protected final Logger logger = LoggerFactory.getLogger("auction");
	private JList 법원List;
	
	public void bind(PlanAppView view) {
		set법원List(view.getCourtList());
	}
	
	

	public void executeRequested() {
		new Thread(){
			@Override
			public void run() {
				Object[] values = 법원List.getSelectedValues();
				if (values == null || values.length == 0) {
					logger.info("선택된 법원이 없습니다. ");
					return;
				}

				for (Object obj : values) {

					try {
						법원 법원 = (법원) obj;
						
						logger.info(법원.get법원명() + "의 기일내역을 갱신합니다.");
						매각예정_사건목록Fetcher f = new 매각예정_사건목록Fetcher();
						사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
						try {
							logger.info(법원.get법원명() + "의 사건 목록을 매각결과검색에서 가져 옵니다. 사건수에 따라 시간이 많이 걸릴수 있습니다. ");
							List<사건> 사건List = f.fetchAll(법원);
							logger.info("가져온 사건의 수:{}", 사건List.size());
							for (사건 fetched사건 : 사건List) {
								사건 사건 = 사건dao.find(법원, fetched사건.get사건번호());
								try {
									new 경매결과Updater(사건,fetched사건, false, "전체",null,null, true).execute();
								} catch (Exception e) {
									e.printStackTrace();
									logger.info("처리중에 오류가 발생했습니다:"+e.getMessage());
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
							logger.info("처리중에 오류가 발생했습니다:"+e.getMessage());
						}
					} catch (Exception e) {
					}
				}
			}
		}.start();
		
	}
	
	public void set법원List(JList list) {
		this.법원List = list;
		법원List.setModel(new 법원ListModel(법원.법원목록));
		법원List.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}


}
