package net.narusas.si.auction.builder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.물건매각물건명세서Fetcher;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.매각물건명세서비고;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.*;
import net.narusas.util.lang.NFile;

public class 매각물건명세서ModeStrategy implements ModeStrategy {
	final Logger logger = LoggerFactory.getLogger("auction");
	private 사건 사건;

	public 매각물건명세서ModeStrategy(사건 사건) {
		this.사건 = 사건;
	}

	@Override
	public void execute() {
		사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
		사건 = 사건dao.find(사건.get법원(), 사건.get사건번호());
		if (사건 == null) {
			logger.info("신건처리되지 않은 사건입니다.");
			return;
		}
		물건Dao dao = (물건Dao) App.context.getBean("물건DAO");
		매각물건명세서Dao dao2 = (매각물건명세서Dao) App.context.getBean("매각물건명세서DAO");
		List<물건> goodsList = dao.get(사건);
		if (goodsList == null) {
			return;
		}
		for (물건 물건 : goodsList) {
			if (물건.is완료여부() == true) {
				logger.info(물건 + " 은 이미 완료된 물건입니다");
				continue;
			}
			try {
				logger.info(사건.get사건번호() + ":" + 물건.get물건번호() + "의 매각물건명세서를 갱신합니다. ");

				logger.info("매각물건명세서를 페이지를 얻어옵니다.");
				물건매각물건명세서Fetcher f = new 물건매각물건명세서Fetcher();
				String html = f.fetch(물건);

				logger.info("매각물건명세서 HTML을 저장합니다. ");
				save(html, 물건);
				logger.info("매각물건명세서 비고를 분석합니다. ");

				매각물건명세서비고 comment = f.parse비고(html);
				if (comment != null) {
					매각물건명세서비고 oldComment = 물건.get매각물건명세서비고();
					if (oldComment == null) {
						oldComment = comment;
					} else {
						oldComment.merge(comment);
					}
					logger.info("비고:" + oldComment.get비고());
					logger.info("비고란:" + oldComment.get비고란());
					logger.info("비소멸권리:" + oldComment.get비소멸권리());
					logger.info("지상권개요:" + oldComment.get지상권개요());
					물건.set매각물건명세서비고(comment);
					dao.saveOrUpdate(물건);
				} else {
					logger.info("매각물건명세서 비고가 없습니다.");
				}
				logger.info("매각물건명세서를 분석합니다. ");
				List<매각물건명세서> items = f.parse(html);
				f.update최선순위설정일자(물건, html);
				if (물건.get최선순위설정일자() != null && "".equals(물건.get최선순위설정일자().trim()) == false){
					logger.info("물건의 최우선순위설정일자를 갱신합니다:"+물건.get최선순위설정일자());
					dao.saveOrUpdate(물건);
					
				}
				if (items == null || items.size() == 0) {
					logger.info("매각물건명세서가 없거나 조사된 임차내역이 없습니다. ");
					continue;
				}
				
				
				logger.info("오래된 매각물건명세서를 제거합니다. ");
				dao2.removeFor(물건);
				logger.info("매각물건명세서를 DB에 입력합니다. ");
				for (매각물건명세서 매각물건명세서 : items) {
					logger.info(매각물건명세서.toString());
					매각물건명세서.set물건(물건);

					dao2.save(매각물건명세서);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void save(String html, 물건 물건) {
		File path = new File("download/" + 물건.getPath());
		if (path.exists() == false) {
			path.mkdirs();
		}
		try {
			File f = new File(path, "Mulgun.html");
			NFile.write(f, html, "euc-kr");
			FileUploaderBG.getInstance().upload(물건.getPath(), "Mulgun.html", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
