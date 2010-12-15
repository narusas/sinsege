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
			+ "			  `no` int(10) unsigned NOT NULL COMMENT '���ǹ�ȣ',\n"
			+ "			  `type_code` int(10) unsigned NOT NULL COMMENT '��������',\n"
			+ "			  `sell_target` varchar(45) NOT NULL COMMENT '�Ű����',\n"
			+ "			  `sell_price` bigint(20) NOT NULL COMMENT '������',\n"
			+ "			  `lowest_price` bigint(20) unsigned NOT NULL COMMENT '������',\n"
			+ "			  `guarantee_price` varchar(45) default NULL COMMENT '������',\n"
			+ "			  `guarantee_ratio` int(10) unsigned NOT NULL default '0',\n"
			+ "			  `accept_date` date default NULL COMMENT '���������',\n"
			+ "			  `decision_date` date default NULL COMMENT '��Ű��ð�����',\n"
			+ "			  `devidend_date` date default NULL COMMENT '����䱸������',\n"
			+ "			  `registration_date` date default NULL COMMENT '���������',\n"
			+ "			  `building_area` varchar(45) default NULL COMMENT '�ǹ�����(M2)',\n"
			+ "			  `land_area` varchar(45) default NULL COMMENT '��������,������(M2)',\n"
			+ "			  `exclusive_area` varchar(45) default NULL COMMENT '�������(M2)',\n"
			+ "			  `exclusion_area` varchar(45) default NULL COMMENT '���ÿܸ���(M2)',\n"
			+ "			  `view_count` int(10) unsigned NOT NULL default '0' COMMENT '��ȸ��',\n"
			+ "			  `comment` varchar(255) default NULL COMMENT '���',\n"
			+ "			  `tools_addr` varchar(255) default NULL COMMENT '���ⱸ_����',\n"
			+ "			  `tools_breakdown` varchar(255) default NULL COMMENT '���ⱸ_����',\n"
			+ "			  `tools_comment` varchar(255) default NULL COMMENT '���ⱸ_���',\n"
			+ "			  `goods_statememt_comment` mediumtext COMMENT '�Ű����Ǹ���_���',\n"
			+ "			  `goods_statememt_comment2` varchar(512) default NULL COMMENT '�Ű����Ǹ���_��Ҹ�Ǹ�',\n"
			+ "			  `goods_statememt_comment3` varchar(512) default NULL COMMENT '�Ű����Ǹ���_����ǰ���',\n"
			+ "			  `goods_statememt_comment4` varchar(512) default NULL COMMENT '�Ű����Ǹ���_����',\n"
			+ "			  `building_statement_comment` mediumtext COMMENT '�ǹ���Ȳ_������',\n"
			+ "			  `land_statement_comment` mediumtext COMMENT '��������Ȳ_������',\n"
			+ "			  `appoint_statemet_comment` mediumtext COMMENT '���ϳ���_������',\n"
			+ "			  `attested_right` varchar(255) default NULL COMMENT '���ε_�Ǹ�',\n"
			+ "			  `attested_supremacy` varchar(255) default NULL COMMENT '���ε_�����',\n"
			+ "			  `attested_comment` mediumtext COMMENT '���ε_���',\n"
			+ "			  `judgement_office` varchar(45) default NULL COMMENT '�����򰡼�_���',\n"
			+ "			  `judgement_date` date default NULL COMMENT '�����򰡼�_����',\n"
			+ "			  `judgement_land_price` varchar(20) default NULL COMMENT '�����򰡼�_�����ǰ���',\n"
			+ "			  `judgement_bld_price` varchar(20) default NULL COMMENT '�����򰡼�_�ǹ�����',\n"
			+ "			  `judgement_in_exclusion_price` varchar(20) default NULL COMMENT '�����򰡼�_���ÿ�����',\n"
			+ "			  `judgement_out_exclusion_price` varchar(20) default NULL COMMENT '�����򰡼�_���ÿܹ�����',\n"
			+ "			  `judgement_tool_price` varchar(20) default NULL COMMENT '�����򰡼�_���ⱸ',\n"
			+ "			  `judgement_price` varchar(20) default NULL COMMENT '�����򰡼�_�հ�',\n"
			+ "			  `judgement_comment` varchar(255) default NULL COMMENT '�����򰡼�_���',\n"
			+ "			  `land_right_comment` mediumtext COMMENT '������_���',\n"
			+ "			  \n"
			+ "			  PRIMARY KEY  USING BTREE (`id`,`court_code`,`charge_id`,`event_no`,`area_code`,`usage_code`)\n"
			+ "			) ENGINE=InnoDB DEFAULT CHARSET=euckr COMMENT='���� ���̺�';";

	public static void main(String[] args) throws Exception {
		DB db = new DB();
		db.dbConnect();
		Statement stmt = db.createStatement();
		stmt.execute(query);
	}
}
