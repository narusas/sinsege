package net.narusas.si.auction.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import net.narusas.si.auction.fetchers.기간입찰목록Fetcher;
import net.narusas.si.auction.fetchers.기일입찰목록Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.dao.담당계Dao;
import net.narusas.si.auction.model.dao.담당계DaoHibernate;
import net.narusas.si.auction.model.dao.법원DaoHibernate;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@SuppressWarnings("unchecked")
public class Initializer {
	final Logger logger = LoggerFactory.getLogger("auction");

	public void init() {
		loadConfiguration();
		load법원목록();
		update담당계();
	}

	protected void update담당계() {
		new Thread() {
			@Override
			public void run() {
				try {
					init기간입찰();
					init기일입찰();
					logger.info("담당계를 모두 얻어왔습니다. \n\n");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(" 담당계 목록을 얻어오는데 실패했습니다. 인터넷이 연결되어 있지 않거나 대법원 웹사이트에 문제가 있습니다. ");
				}
			}
		}.start();
	}

	protected void loadConfiguration() {
		logger.info("DB 연결을 초기화 합니다.");
		App.context = new FileSystemXmlApplicationContext("cfg/spring.cfg.xml");
	}

	protected void load법원목록() {
		logger.info("법원 목록을 가져옵니다.");
		if (System.getProperty("useCoutCache") == null) {
			법원.법원목록 = ((법원DaoHibernate) App.context.getBean("법원DAO")).getAll("order");
			saveCache(법원.법원목록);
		} else {
			법원.법원목록 = loadCache();
		}

		logger.info("가져온 법원목록 " + 법원.법원목록.toString());
	}

	private List<법원> loadCache() {
		File f = new File("courtList.cache");
		try {
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(f));
			List<법원> res = (List<법원>) oin.readObject();
			oin.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	private void saveCache(List<법원> 법원목록) {
		try {
			File f = new File("courtList.cache");
			ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(f));
			oout.writeObject(법원목록);
			oout.flush();
			oout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void init기일입찰() throws HttpException, IOException, Exception {
		logger.info("기일입찰 목록을 가져옵니다");
		for (법원 court : 법원.법원목록) {
			// FIXME 법원명이 바뀌었는지 확인.
			기일입찰목록Fetcher fetcher = new 기일입찰목록Fetcher();
			List<담당계> temp = fetcher.fetch(court);
			List<담당계> res = new LinkedList<담당계>();
			for (담당계 담당계 : temp) {
				res.add(validate(담당계));
			}
			logger.info("가져온 기일찰목록 " + court.get법원명() + res);
			court.add담당계목록(res);
		}
	}

	protected void init기간입찰() throws IOException, Exception {
		logger.info("기간입찰 목록을 가져옵니다");
		기간입찰목록Fetcher fetcher = new 기간입찰목록Fetcher();
		// {
		// @Override
		// public String fetchPage() throws IOException {
		// return NFile.getText(new File("fixture2/004_기간별검색.html"), "euc-kr");
		// }
		// };
		List<담당계> temp = fetcher.fetch();
		logger.info("가져온 기간입찰 목록 " + temp);

		for (담당계 담당계 : temp) {
			담당계.get소속법원().add담당계(validate(담당계));
		}
	}

	protected 담당계 validate(담당계 charge) {
		법원 court = charge.get소속법원();
		담당계Dao 담당계dao = (담당계DaoHibernate) App.context.getBean("담당계DAO");
		담당계 temp = 담당계dao.get(charge.get소속법원(), charge.get담당계코드(), charge.get매각기일());

		if (temp != null) {
			temp.setTime(charge.getTime());
			담당계dao.saveOrUpdate(temp);
			return temp;
		}

		charge.set소속법원(court);
		담당계dao.save(charge);
		return charge;
	}
}
