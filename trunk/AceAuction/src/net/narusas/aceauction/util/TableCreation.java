package net.narusas.aceauction.util;

import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class TableCreation {
	static String query = "CREATE TABLE  `test`.`ac_goods` (\n"
			+ "			  `id` int(10) unsigned NOT NULL auto_increment,\n"
			+ "			  `court_code` int(10) unsigned NOT NULL,\n"
			+ "			  `charge_id` int(10) unsigned NOT NULL,\n"
			+ "			  `event_no` bigint(20) unsigned NOT NULL,\n"
			+ "			  `area_code` int(10) unsigned NOT NULL default '0',\n"
			+ "			  `usage_code` int(10) unsigned NOT NULL default '0',\n"
			+ "			  `no` int(10) unsigned NOT NULL COMMENT '물건번호',\n"
			+ "			  `type_code` int(10) unsigned NOT NULL COMMENT '물건종별',\n"
			+ "			  `sell_target` varchar(45) NOT NULL COMMENT '매각대상',\n"
			+ "			  `sell_price` bigint(20) NOT NULL COMMENT '감정가',\n"
			+ "			  `lowest_price` bigint(20) unsigned NOT NULL COMMENT '최저가',\n"
			+ "			  `guarantee_price` varchar(45) default NULL COMMENT '보증금',\n"
			+ "			  `guarantee_ratio` int(10) unsigned NOT NULL default '0',\n"
			+ "			  `accept_date` date default NULL COMMENT '사건접수일',\n"
			+ "			  `decision_date` date default NULL COMMENT '경매개시결정일',\n"
			+ "			  `devidend_date` date default NULL COMMENT '배장요구종기일',\n"
			+ "			  `registration_date` date default NULL COMMENT '보존등기일',\n"
			+ "			  `building_area` varchar(45) default NULL COMMENT '건물면적(M2)',\n"
			+ "			  `land_area` varchar(45) default NULL COMMENT '토지면적,대지권(M2)',\n"
			+ "			  `exclusive_area` varchar(45) default NULL COMMENT '전용면적(M2)',\n"
			+ "			  `exclusion_area` varchar(45) default NULL COMMENT '제시외면적(M2)',\n"
			+ "			  `view_count` int(10) unsigned NOT NULL default '0' COMMENT '조회수',\n"
			+ "			  `comment` varchar(255) default NULL COMMENT '비고',\n"
			+ "			  `tools_addr` varchar(255) default NULL COMMENT '기계기구_지번',\n"
			+ "			  `tools_breakdown` varchar(255) default NULL COMMENT '기계기구_내역',\n"
			+ "			  `tools_comment` varchar(255) default NULL COMMENT '기계기구_비고',\n"
			+ "			  `goods_statememt_comment` mediumtext COMMENT '매각물건명세서_비고',\n"
			+ "			  `goods_statememt_comment2` varchar(512) default NULL COMMENT '매각물건명세서_비소멸권리',\n"
			+ "			  `goods_statememt_comment3` varchar(512) default NULL COMMENT '매각물건명세서_지상권개요',\n"
			+ "			  `goods_statememt_comment4` varchar(512) default NULL COMMENT '매각물건명세서_비고란',\n"
			+ "			  `building_statement_comment` mediumtext COMMENT '건물현황_평가정리',\n"
			+ "			  `land_statement_comment` mediumtext COMMENT '대지권현황_평가정리',\n"
			+ "			  `appoint_statemet_comment` mediumtext COMMENT '기일내역_평가정리',\n"
			+ "			  `attested_right` varchar(255) default NULL COMMENT '등기부등본_권리',\n"
			+ "			  `attested_supremacy` varchar(255) default NULL COMMENT '등기부등본_지상권',\n"
			+ "			  `attested_comment` mediumtext COMMENT '등기부등본_비고',\n"
			+ "			  `judgement_office` varchar(45) default NULL COMMENT '감정평가서_기관',\n"
			+ "			  `judgement_date` date default NULL COMMENT '감정평가서_시점',\n"
			+ "			  `judgement_land_price` varchar(20) default NULL COMMENT '감정평가서_대지권가격',\n"
			+ "			  `judgement_bld_price` varchar(20) default NULL COMMENT '감정평가서_건물가격',\n"
			+ "			  `judgement_in_exclusion_price` varchar(20) default NULL COMMENT '감정평가서_제시외포함',\n"
			+ "			  `judgement_out_exclusion_price` varchar(20) default NULL COMMENT '감정평가서_제시외미포함',\n"
			+ "			  `judgement_tool_price` varchar(20) default NULL COMMENT '감정평가서_기계기구',\n"
			+ "			  `judgement_price` varchar(20) default NULL COMMENT '감정평가서_합계',\n"
			+ "			  `judgement_comment` varchar(255) default NULL COMMENT '감정평가서_비고',\n"
			+ "			  `land_right_comment` mediumtext COMMENT '대지권_비고',\n"
			+ "			  \n"
			+ "			  PRIMARY KEY  USING BTREE (`id`,`court_code`,`charge_id`,`event_no`,`area_code`,`usage_code`)\n"
			+ "			) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='물건 테이블';";

	public static void main(String[] args) throws Exception {
		DB db = new DB();
		db.dbConnect();
		Statement stmt = db.createStatement();
		stmt.execute(query);
	}
}
