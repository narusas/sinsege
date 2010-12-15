package net.narusas.aceauction.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.기일내역Item;
import net.narusas.aceauction.model.명세서;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.물건현황;
import net.narusas.aceauction.model.스피드옥션물건상세내역;
import net.narusas.aceauction.model.제시외건물;

public class 목록Task implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final BeansTableModel infoTableModel;

	private final 목록ListModel listModel;

	private final String 명령;

	private final String 목록선택;
	private final 물건 물건;
	public 목록Task(물건 물건, String 명령, String 목록선택, BeansTableModel infoTableModel,
			목록ListModel listModel) {
		this.물건 = 물건;
		this.명령 = 명령;
		this.목록선택 = 목록선택;
		this.infoTableModel = infoTableModel;
		this.listModel = listModel;
	}

	public void run() {
		logger.info("테이블을 정리합니다");
		infoTableModel.clear();
		if (물건 == null || 명령 == null) {
			logger.info("물건 또는 명령이 없습니다. ");
			return;
		}
		스피드옥션물건상세내역 detail = 물건.getDetail();
		if ("상세정보".equals(명령)) {
			logger.info("상세정보를 테이블에 표시합니다. ");
			listModel.clear();
			infoTableModel.setBeans(detail);
		}
		if ("건물현황".equals(명령)) {
			logger.info("건물현황을 목록에 표시합니다. ");
			update(detail.get건물현황());
		}
		if ("대지권현황".equals(명령)) {
			logger.info("대지권현황을 목록에 표시합니다. ");
			update(detail.get대지권현황());
		}
		if ("매각기일현황".equals(명령)) {
			logger.info("매각기일현황을 목록에 표시합니다. ");
			update(detail.get매각기일현황());
		}
		if ("토지현황".equals(명령)) {
			logger.info("토지현황을 목록에 표시합니다. ");
			update(detail.get토지현황());
		}

		if ("제시외건물현황".equals(명령)) {
			logger.info("토지현황을 목록에 표시합니다. ");
			update(detail.get제시외건물현황());
		}
		if ("기계기구".equals(명령)) {
			logger.info("토지현황을 목록에 표시합니다. ");
			update(detail.get기계기구());
		}

		if ("대법원 기일내역".equals(명령)) {
			logger.info("대법원 기일내역을 목록에 표시합니다. ");
			update(물건.get기일내역());
		}
		if ("매각물건명세서".equals(명령)) {
			logger.info("대법원 기일내역을 목록에 표시합니다. ");
			update(물건.get명세서());
		}

		if ("사진".equals(명령)) {
			logger.info("사진을 띄움니다. ");
			showImage(목록선택.substring(목록선택.indexOf(" ") + 1).trim());
		}

		if ("대법원물건현황".equals(명령)) {
			logger.info("대법원건물 내역을 목록에 표시합니다. ");
			update물건현황(물건.get물건현황());
		}

		if ("대법원제시외건물".equals(명령)) {
			logger.info("대법원제시외건물 내역을 목록에 표시합니다. ");
			update(물건.get제시외건물s());
		}

	}

	private void showImage(String url) {
		logger.info(url + "로 부터 사진을 얻어옵니다. ");
		try {
			URL u = new URL(url);
			BufferedImage img = ImageIO.read(u.openStream());
			ImageViewer viewer = new ImageViewer(url, img);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void update(LinkedList<제시외건물> 제시외건물s) {
		int index = Integer.parseInt(목록선택.substring(0, 목록선택.indexOf(" ")).trim());
		제시외건물 건물 = 제시외건물s.get(index - 1);
		infoTableModel.setBeans(건물);
	}

	private void update(List<기일내역Item> 기일내역) {
		for (int i = 0; i < 기일내역.size(); i++) {
			if (String.valueOf(i + 1).equals(목록선택.substring(0, 목록선택.indexOf(" ")))) {
				기일내역Item item = 기일내역.get(i);
				infoTableModel.setBeans(item);
				return;
			}
		}

	}

	private void update(Table status) {
		List<Row> r = status.getRows();
		if ("기타현황".equals(r.get(0))) {
			infoTableModel.addRow(new Object[] { r.get(0), r.get(1) });
		} else {
			for (Row record : r) {
				if (목록선택.equals(record.getValue(0))) {
					List<String> headers = status.getHeaders();

					for (int i = 0; i < record.getValues().size(); i++) {
						String header = headers.get(i);
						infoTableModel.addRow(new Object[] { header, record.getValue(i) });
					}
					break;
				}
			}
		}
	}

	private void update(명세서 명세서) {
		List<Row> records = 명세서.get점유자().getRows();
		for (Row record : records) {
			if (목록선택.equals(record.getValue(0))) {
				List<String> headers = 명세서.get점유자().getHeaders();

				for (int i = 0; i < record.getValues().size(); i++) {
					String header = headers.get(i);
					infoTableModel.addRow(new Object[] { header, record.getValue(i) });
				}
				break;
			}
		}
	}

	private void update물건현황(List<물건현황> 건물s) {
		int index = Integer.parseInt(목록선택.substring(0, 목록선택.indexOf(" ")).trim());
		물건현황 건물 = 건물s.get(index - 1);
		infoTableModel.setBeans(건물);
	}
}
