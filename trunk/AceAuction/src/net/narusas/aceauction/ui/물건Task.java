package net.narusas.aceauction.ui;

import java.io.IOException;
import java.util.Vector;

import net.narusas.aceauction.fetchers.스피드옥션_물건Updater;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.스피드옥션물건상세내역;

import org.apache.commons.httpclient.HttpException;

public class 물건Task implements Runnable {

	private final BeansTableModel infoTableModel;

	private final 물건 물건;

	private final 물건정보ListModel 물건정보Model;

	public 물건Task(물건 물건, BeansTableModel infoTableModel, 물건정보ListModel listModel) {
		this.물건 = 물건;
		this.infoTableModel = infoTableModel;
		this.물건정보Model = listModel;
	}

	public void run() {
		물건정보Model.clear();
		infoTableModel.clear();
		if (물건 == null) {
			return;
		}
		try {

			if (물건.getDetail() == null) {
				스피드옥션_물건Updater.updateDetail(물건, null);
			}

			infoTableModel.setBeans(물건);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Vector<String> cmds = new Vector<String>();
		스피드옥션물건상세내역 detail = 물건.getDetail();
		cmds.add("상세정보");

		if (detail != null) {
			if (detail.get건물현황() != null) {
				cmds.add("건물현황");
			}

			if (detail.get대지권현황() != null) {
				cmds.add("대지권현황");
			}
			if (detail.get매각기일현황() != null) {
				cmds.add("매각기일현황");
			}
			if (detail.get토지현황() != null) {
				cmds.add("토지현황");
			}
			if (detail.get제시외건물현황() != null) {
				cmds.add("제시외건물현황");
			}
			if (detail.get기계기구() != null) {
				cmds.add("기계기구");
			}
		}

		if (물건.get기일내역() != null) {
			cmds.add("대법원 기일내역");
		}
		if (물건.getHas명세서()) {
			cmds.add("매각물건명세서");
		}

		if (물건.get사진() != null && 물건.get사진().size() != 0) {
			cmds.add("사진");
		}

		if (물건.get물건현황() != null && 물건.get물건현황().size() != 0) {
			cmds.add("대법원물건현황");
		}

		if (물건.get제시외건물s() != null && 물건.get제시외건물s().size() != 0) {
			cmds.add("대법원제시외건물");
		}
		물건정보Model.update(cmds);
	}

}
